package com.swathi.cpass.provider.service;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
class MockProviderServiceTest {
 @Test void timeoutCanFallBackToSecondSmsProvider(){
  var service=new MockProviderService("TIMEOUT");
  var request=new MockProviderService.SendRequest("+919000000000","demo_login","SMS");
  var first=service.send("sms_a",request);
  var second=service.send("sms_b",request);
  assertFalse(first.accepted());assertEquals("TIMEOUT",first.failureCode());
  assertTrue(second.accepted());assertNotNull(second.externalId());
 }
 @Test void rejectsWrongChannel(){
  var service=new MockProviderService("ACCEPT");
  assertThrows(IllegalArgumentException.class,()->service.send("sms_a",new MockProviderService.SendRequest("+919000000000","demo_login","VOICE")));
 }
}
