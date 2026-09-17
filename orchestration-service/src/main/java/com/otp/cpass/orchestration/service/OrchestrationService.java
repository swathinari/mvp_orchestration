package com.swathi.cpass.orchestration.service;
import com.swathi.cpass.orchestration.repository.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Instant;
import java.util.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;
@Service public class OrchestrationService {
 private final MessageRepository messages;private final RestClient client;private final String routingUrl;private final String providerUrl;private final String internalSecret;
 public OrchestrationService(MessageRepository messages,RestClient.Builder builder,@Value("${cpass.routing-url}") String routingUrl,@Value("${cpass.provider-url}") String providerUrl,@Value("${cpass.internal-secret}") String internalSecret){this.messages=messages;this.client=builder.build();this.routingUrl=routingUrl;this.providerUrl=providerUrl;this.internalSecret=internalSecret;}
 public record VerifyRequest(String recipient,String templateId,String channel,String channelPolicy){}
 public record Route(List<String> providers,int maxAttempts){}
 public record ProviderSend(String recipient,String templateId,String channel){}
 public record ProviderResult(boolean accepted,String externalId,String failureCode){}
 public record AttemptView(String id,String provider,String status,String externalId,String failureCode){}
 public record MessageView(String verificationId,String messageId,String status,String selectedChannel,String selectedProvider,String traceId,List<AttemptView> attempts){}
 @Transactional public MessageView verify(String tenant,String token,String key,VerifyRequest request){
  if(key==null || key.isBlank())throw new IllegalArgumentException("Idempotency-Key required");
  String hash=sha256(request.recipient()+"|"+request.templateId()+"|"+request.channel()+"|"+request.channelPolicy());
  var prior=messages.findByTenantIdAndIdempotencyKey(tenant,key);
  if(prior.isPresent()){if(!prior.get().requestHash.equals(hash))throw new IllegalArgumentException("Key reused with different request");return view(prior.get());}
  Route route=client.get().uri(routingUrl+"/internal/routes?name={name}&channel={channel}",request.channelPolicy(),request.channel()).header(HttpHeaders.AUTHORIZATION,token).header("X-Internal-Secret",internalSecret).retrieve().body(Route.class);
  if(route==null || route.providers()==null || route.providers().isEmpty())throw new IllegalArgumentException("No route");
  var message=new MessageEntity();message.id="msg_"+UUID.randomUUID();message.verificationId="ver_"+UUID.randomUUID();message.traceId="tr_"+UUID.randomUUID();message.tenantId=tenant;message.idempotencyKey=key;message.requestHash=hash;message.channel=request.channel();message.status="INITIATED";message.createdAt=Instant.now();
  for(String provider:route.providers().stream().limit(Math.min(route.maxAttempts(),2)).toList()){
   var response=client.post().uri(providerUrl+"/internal/providers/{id}/send",provider).header(HttpHeaders.AUTHORIZATION,token).header("X-Internal-Secret",internalSecret).contentType(MediaType.APPLICATION_JSON).body(new ProviderSend(request.recipient(),request.templateId(),request.channel())).retrieve().body(ProviderResult.class);
   var attempt=new AttemptEntity();attempt.id="att_"+UUID.randomUUID();attempt.provider=provider;attempt.createdAt=Instant.now();attempt.status=response!=null && response.accepted()?"ACCEPTED":"FAILED";attempt.externalId=response==null?null:response.externalId();attempt.failureCode=response==null?"PROVIDER_ERROR":response.failureCode();message.addAttempt(attempt);message.selectedProvider=provider;
   if(response!=null && response.accepted()){message.status="SENT";break;}
   if(response==null || !("TIMEOUT".equals(response.failureCode()) || "PROVIDER_5XX".equals(response.failureCode())))break;
  }
  if(!"SENT".equals(message.status))message.status="FAILED";
  return view(messages.save(message));
 }
 @Transactional(readOnly=true) public MessageView get(String tenant,String id){return messages.findByTenantIdAndId(tenant,id).map(this::view).orElseThrow(()->new NoSuchElementException("Message not found"));}
 private MessageView view(MessageEntity m){return new MessageView(m.verificationId,m.id,m.status,m.channel,m.selectedProvider,m.traceId,m.attempts.stream().map(a->new AttemptView(a.id,a.provider,a.status,a.externalId,a.failureCode)).toList());}
 private String sha256(String x){try{return HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256").digest(x.getBytes(StandardCharsets.UTF_8)));}catch(Exception e){throw new IllegalStateException(e);}}
}