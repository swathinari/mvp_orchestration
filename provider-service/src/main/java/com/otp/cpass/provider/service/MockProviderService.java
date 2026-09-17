package com.swathi.cpass.provider.service;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
@Service public class MockProviderService {
 private final String primaryMode;public MockProviderService(@Value("${cpass.provider-a-mode:ACCEPT}") String mode){this.primaryMode=mode;}
 public record SendRequest(String recipient,String templateId,String channel){}
 public record SendResult(boolean accepted,String externalId,String failureCode){}
 public SendResult send(String provider,SendRequest request){
  if(!java.util.Set.of("sms_a","sms_b","whatsapp_mock","rcs_mock","voice_mock").contains(provider))throw new IllegalArgumentException("Unknown provider");
  String channel=request.channel().toLowerCase(java.util.Locale.ROOT);
  if(!(channel.equals("sms") && provider.startsWith("sms_")) && !provider.equals(channel+"_mock"))throw new IllegalArgumentException("Provider/channel mismatch");
  if(provider.equals("sms_a") && primaryMode.equalsIgnoreCase("TIMEOUT"))return new SendResult(false,null,"TIMEOUT");
  if(provider.equals("sms_a") && primaryMode.equalsIgnoreCase("ERROR"))return new SendResult(false,null,"PROVIDER_5XX");
  return new SendResult(true,"mock_"+UUID.randomUUID(),null);
 }
}