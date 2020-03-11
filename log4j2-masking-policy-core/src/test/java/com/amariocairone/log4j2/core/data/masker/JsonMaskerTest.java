package com.amariocairone.log4j2.core.data.masker;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Arrays;

import org.junit.Test;

import com.amariocairone.log4j2.api.data.exceptions.InvalidInputException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mariocairone.log4j2.api.data.finder.Result;
import com.mariocairone.log4j2.api.data.masker.Masker;
import com.mariocairone.log4j2.core.data.finder.JsonFinder;
import com.mariocairone.log4j2.core.data.masker.JsonMasker;
import com.mariocairone.log4j2.core.data.utils.FileUtils;

public class JsonMaskerTest {

    private static final ObjectMapper mapper = new ObjectMapper();
    
  @Test
  public void testEnabled() throws Exception {
      String payload = "{\"a\": \"abc\", \"nested\": {\"b\": \"xyz\"}}";
      Masker masker = new JsonMasker(false);
      char[] result = masker.mask(payload.toCharArray());
      JsonNode masked = mapper.readTree(new String(result));
      assertEquals("abc", masked.get("a").asText());
      assertEquals("xyz", masked.get("nested").get("b").asText());
  }   

  @Test
  public void testMaskTextFiledInRoot() throws Exception {
      String payload = "{\"a\": \"abc\", \"nested\": {\"b\": \"xyz\"}}";
      Masker masker = new JsonMasker();

      char[] result = masker.mask(payload.toCharArray());
      JsonNode masked = mapper.readTree(new String(result));
      assertEquals("xxx", masked.get("a").asText());
  } 
  
 

  @Test
  public void testMaskBaseLatinWithX() throws IOException {
      String payload = "{\"a\": \"Qwerty\"}";
      Masker masker = new JsonMasker();
      char[] result = masker.mask(payload.toCharArray());
      JsonNode masked = mapper.readTree(new String(result));
      
      assertEquals("Xxxxxx", masked.get("a").asText());
  }

  @Test
  public void testMaskNotBaseLatinWithX() throws IOException {
      String payload = "{\"a\": \"Ĕőєחβ\"}";
      Masker masker = new JsonMasker();
      char[] result = masker.mask(payload.toCharArray());
      JsonNode masked = mapper.readTree(new String(result));
      
      assertEquals("xxxxx", masked.get("a").asText());
  }
  
  
  @Test
  public void testMaskDigitsWithAsterisk() throws IOException {
      String payload = "{\"a\": \"8301\"}";
      Masker masker = new JsonMasker();
      char[] result = masker.mask(payload.toCharArray());
      JsonNode masked = mapper.readTree(new String(result));
      
      assertEquals("****", masked.get("a").asText());
  }
  

  @Test
  public void testNotMaskPunctuationAndCommonSigns() throws IOException {
      String payload = "{\"a\": \"-+.,!?@%$[]()\"}";
      Masker masker = new JsonMasker();
      char[] result = masker.mask(payload.toCharArray());
      JsonNode masked = mapper.readTree(new String(result));
      
      assertEquals("-+.,!?@%$[]()", masked.get("a").asText());
  }
  
  

  @Test
  public void testMaskComplexString() throws IOException {
      String payload = "{\"a\": \"Phone: +1-313-85-93-62, Salary: $100, Name: Κοτζιά;\"}";
      Masker masker = new JsonMasker();
      char[] result = masker.mask(payload.toCharArray());
      JsonNode masked = mapper.readTree(new String(result));
  
      assertEquals("Xxxxx: +*-***-**-**-**, Xxxxxx: $***, Xxxx: xxxxxx;", masked.get("a").asText());
  }

  @Test
  public void testMaskNumberWithStringOfAsterisks() throws IOException {
      String payload = "{\"a\": 201}";
      Masker masker = new JsonMasker();
      char[] result = masker.mask(payload.toCharArray());
      JsonNode masked = mapper.readTree(new String(result));
 
      
      assertEquals("***", masked.get("a").asText());
  }

  @Test
  public void testMaskPropertiesDeeply() throws IOException {
      String payload = "{\"foo\": {\"bar\": {\"a\": 123, \"b\": \"!?%\"}}, \"c\": [\"sensitive\"]}";
      Masker masker = new JsonMasker();
      char[] result = masker.mask(payload.toCharArray());
      JsonNode masked = mapper.readTree(new String(result));
      
      assertEquals(mapper.readTree("{\"foo\": {\"bar\": {\"a\": \"***\", \"b\": \"!?%\"}}, \"c\": [\"xxxxxxxxx\"]}"), masked);
  }
  

  @Test
  public void testMaskNumberWithFractionPart() throws IOException {
      String payload = "{\"a\": 20.12}";
      Masker masker = new JsonMasker();
      char[] result = masker.mask(payload.toCharArray());
      JsonNode masked = mapper.readTree(new String(result));

      assertEquals("**.**", masked.get("a").asText());
  }

  @Test
  public void testMaskNumberZero() throws IOException {
      String payload = "{\"a\": 0.0}";
      Masker masker = new JsonMasker();
      char[] result = masker.mask(payload.toCharArray());
      JsonNode masked = mapper.readTree(new String(result));
      
      assertEquals("*.*", masked.get("a").asText());

      result = masker.mask("{\"a\": 0}".toCharArray());
      masked = mapper.readTree(new String(result));
      assertEquals("*", masked.get("a").asText());
  }
  

  @Test
  public void testEmptyJson() throws IOException {
      String payload = "{}";
      Masker masker = new JsonMasker();
      char[] result = masker.mask(payload.toCharArray());

      
      assertEquals("{}", new String(result));
  }

  @Test
  public void testEmptyArray() throws IOException {
      String payload = "[]";
      Masker masker = new JsonMasker();
      char[] result = masker.mask(payload.toCharArray());
      
      assertEquals("[]", new String(result));
  }

  @Test
  public void testEmptyArrayAsValue() throws IOException {

      String payload = "{\"a\": []}";
      Masker masker = new JsonMasker();
      char[] result = masker.mask(payload.toCharArray());
      JsonNode masked = mapper.readTree(new String(result));

      
      assertEquals("[]", masked.get("a").toString());
  }
  
  @Test
  public void testArraywithStringValues() throws IOException {


      String payload = "{\"a\": [\"test@mail.com\",\"3874\"]}";
      Masker masker = new JsonMasker();
      char[] result = masker.mask(payload.toCharArray());
      JsonNode masked = mapper.readTree(new String(result));

      
      assertEquals("[\"xxxx@xxxx.xxx\",\"****\"]", masked.get("a").toString());
  }
  
  
  @Test(expected = InvalidInputException.class)
  public void testNull() {

	 new JsonMasker().mask(null);


  }

  @Test
  public void testWhitelisting() throws IOException {
      String payload = "{" +
              "  \"myField\": \"Hi\"," +
              "  \"a\": \"8301975624\"," +
              "  \"nestedObj\": {" +
              "    \"b\": \"Qwerty\"," +
              "    \"field2\": 123" +
              "  }," +
              "  \"array1\": [{\"a\":1}, {\"b\":2}]" +
              "}";

      Masker masker = new JsonMasker(Arrays.asList("$.a", "$.array1[0].a"));
      char[] result = masker.mask(payload.toCharArray());
      JsonNode masked = mapper.readTree(new String(result));
      


      assertEquals("Xx", masked.get("myField").asText());
      assertEquals("8301975624", masked.get("a").asText());
      assertEquals("Xxxxxx", masked.get("nestedObj").get("b").asText());
      assertEquals("***", masked.get("nestedObj").get("field2").asText());
      assertEquals(1, masked.get("array1").get(0).get("a").asInt());
      assertEquals("*", masked.get("array1").get(1).get("b").asText());
  }


  @Test
  public void testWhitelistingAndEnabled() throws Exception {
      String payload = "{\"a\": \"abc\", \"b\": \"xyz\"}";

      Masker masker = new JsonMasker(Arrays.asList("$.b"), false);
      char[] result = masker.mask(payload.toCharArray());
      JsonNode masked = mapper.readTree(new String(result));

      
      assertEquals("abc", masked.get("a").asText());
      assertEquals("xyz", masked.get("b").asText());
  }
  
  
  @Test
  public void testWithPrefixTextBeforeJson() throws Exception {
      String payload = "Raw value retrieved from cache: [1580484595557]:{\"addresses\":{\"address\":\"107 COLLEGE RD, EPSOM KT17 4JA, UK\",\"name\":\"107 COLLEGE ROAD\",\"streetName\":\"COLLEGE ROAD\",\"town\":\"Epsom\",\"postcode\":\"KT17 4JA\",\"country\":\"GB\",\"latitude\":51.3264481,\"longitude\":-0.2406635,\"source\":\"GOOGLE\",\"accuracy\":\"M1\",\"hasBeneath\":false,\"relevance\":90,\"refined\":true}}\r\n" ;

      Masker masker = new JsonMasker();
      char[] result = masker.mask(payload.toCharArray());
      
      JsonFinder finder = new JsonFinder();
      Result results = finder.find(result);
      
      JsonNode masked = mapper.readTree(new String(results.get(1).chars()));

      
      assertEquals("*** XXXXXXX XX, XXXXX XX** *XX, XX", masked.get("addresses").get("address").asText());
  }
  
  

  @Test
  public void testLog4jMessages() throws Exception {
	  String payload = "Loaded MuleMessageBuilderFactory implementation 'org.mule.runtime.core.internal.message.DefaultMessageBuilderFactory' from classloader 'org.mule.runtime.module.reboot.internal.MuleContainerSystemClassLoader@372b7c4c'";

      Masker masker = new JsonMasker();
      char[] result = masker.mask(payload.toCharArray());
     
      assertEquals(payload, new String(result));
 
  }
 
  @Test
  public void testMuleLog() throws Exception {
	  String payload = FileUtils.getResourceFileAsString("test.txt");

      Masker masker = new JsonMasker();
      char[] result = masker.mask(payload.toCharArray());
     
      assertEquals(payload, new String(result));
 
  }  
  
  
}
