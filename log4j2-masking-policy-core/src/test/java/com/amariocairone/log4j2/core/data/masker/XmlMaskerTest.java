package com.amariocairone.log4j2.core.data.masker;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.mariocairone.log4j2.api.data.masker.Masker;
import com.mariocairone.log4j2.core.data.masker.XmlMasker;
import com.mariocairone.log4j2.core.data.utils.TestUtils;

public class XmlMaskerTest {

	  @Test
	  public void testEnabled() throws Exception {
	        String payload = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
	                + "<Tutorial><user>test</user><password>abcdef</password><technology>Java, Big data, Database</technology>"
	                + "<address>topjavatutorial.com</address></Tutorial>";
	      
	      Masker masker = new XmlMasker(false);
	      char[] result = masker.mask(payload.toCharArray());
	      assertEquals(payload, new String(result));
	  }  
	  
	  @Test
	  public void testMaskRootField() throws Exception {
		  String payload = TestUtils.getResourceFileAsString("xml/file.xml");
	      
	      Masker masker = new XmlMasker();
	      char[] result = masker.mask(payload.toCharArray());

	      Document document = TestUtils.toXmlDocument(result);
	      Element rootElement = document.getDocumentElement();
	      
          String name = TestUtils.getString("ContactName", rootElement);
          String phone = TestUtils.getString("Phone", rootElement);
	      assertEquals("Xxxxxx Xxxxxx", name);
	      assertEquals("(***) ***-****", phone);
         
	     
	  }  
  	
	  
	  @Test
	  public void testXMLWhitelist() throws Exception {
		 
		  String payload = TestUtils.getResourceFileAsString("xml/simple.xml");
		 
	      Masker masker = new XmlMasker(Arrays.asList("id","/company/staff[1]/firstname", "salary"));
	      char[] result = masker.mask(payload.toCharArray());

	      Document document = TestUtils.toXmlDocument(result);
	      
	      Element rootElement = document.getDocumentElement();
	      
          String firstName = TestUtils.getString("firstname", rootElement);
          String lastName = TestUtils.getString("lastname", rootElement);
          String salary = TestUtils.getString("salary", rootElement);
          
	      assertEquals("yong", firstName);
	      
	      assertEquals("xxxx xxx", lastName);
	      
	      assertEquals("100000", salary);
	      

	  } 
	  
	
}
