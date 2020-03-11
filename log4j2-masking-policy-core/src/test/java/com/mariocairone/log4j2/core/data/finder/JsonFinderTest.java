package com.mariocairone.log4j2.core.data.finder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import com.mariocairone.log4j2.api.data.finder.Result;
import com.mariocairone.log4j2.core.data.finder.JsonFinder;
import com.mariocairone.log4j2.core.data.utils.FileUtils;

public class JsonFinderTest {

	
	private JsonFinder parser = new JsonFinder();
	
	@Test
	public void ParseNull() throws Exception {
		char[] input = null;
		Result result = parser.find(input);
		assertNotNull(result);
		assertTrue(result.size() == 0);
	}
	
	@Test
	public void ParseEmpty() throws Exception {
		String input = "";
		Result result = parser.find(input.toCharArray());		
		assertNotNull(result);
		assertTrue(result.size() == 0);
	}
	
	@Test
	public void ParsePlainchars() throws Exception {
		String input = "Exercitationem distinctio provident voluptate velit sint accusantium blanditiis";
		Result result = parser.find(input.toCharArray());		
		assertNotNull(result);
		assertTrue(result.size() == 0);
	}
	
	@Test
	public void ParseEmptyJsonObject() throws Exception {
		
		String input = "{}";
		Result result = parser.find(input.toCharArray());
		JSONAssert.assertEquals(input, new String(result.get(0).chars()), true);
	}
	
	@Test
	public void ParseEmptyJsonArray() throws Exception {
		
		String input = "[]";
		Result result = parser.find(input.toCharArray());
		JSONAssert.assertEquals(input, new String(result.get(0).chars()), true);
	} 
	
	@Test
	public void ParseJsonObject() throws Exception {
		
		String input = FileUtils.getResourceFileAsString("json/simple_object.json");
		Result result = parser.find(input.toCharArray());
		char[] text = result.get(0).chars();
		
		JSONAssert.assertEquals(input, new String(text), true);
	} 
	
	@Test
	public void ParseJsonArray() throws Exception {
		
		String input = FileUtils.getResourceFileAsString("json/simple_array.json");		
		Result result = parser.find(input.toCharArray());
		char[] text = result.get(0).chars();
		
		JSONAssert.assertEquals(input, new String(text), true);
	}
	
	@Test
	public void ParsePlainTextWithBrackets() throws Exception {
		
		String input = "{ Exercitationem [ distinctio provident] {]} voluptate velit sint accusantium blanditiis";
		Result result = parser.find(input.toCharArray());
		assertNotNull(result);
		assertTrue(result.size() == 0);
			
	}	

	@Test
	public void ParseMixedString_Json_Object_With_Prefix_String() throws Exception {
		String plainInput = "Exercitationem distinctio provident voluptate velit sint accusantium blanditiis";
		String jsonInput = FileUtils.getResourceFileAsString("json/simple_object.json"); 
		
		String input = plainInput + jsonInput;
		char[] text = parser.find(input.toCharArray()).get(0).chars();
		assertEquals(jsonInput, new String(text));
		
	}

	@Test
	public void ParseMixedString_Json_Array_With_Prefix_String() throws Exception {
		String plainInput = "Exercitationem distinctio provident voluptate velit sint accusantium blanditiis";
		String jsonInput = FileUtils.getResourceFileAsString("json/simple_array.json");		 
		
		String input = plainInput + jsonInput;
		char[] result = parser.find(input.toCharArray()).get(0).chars();
		assertEquals(jsonInput, new String(result));
		
	}	

	@Test
	public void ParseMixedString_Json_Object_With_Suffix_String() throws Exception {
		String plainInput = "Exercitationem distinctio provident voluptate velit sint accusantium blanditiis";
		String jsonInput = FileUtils.getResourceFileAsString("json/simple_object.json"); 
		
		String input =  jsonInput + plainInput;
		
		char[] result = parser.find(input.toCharArray()).get(0).chars();
		assertEquals(jsonInput, new String(result));
		
	}
	

	
	@Test
	public void ParseMixedString_Json_Array_With_Suffix_String() throws Exception {
		String plainInput = "Exercitationem distinctio provident voluptate velit sint accusantium blanditiis";
		String jsonInput = FileUtils.getResourceFileAsString("json/simple_array.json");		 
		
		String input = jsonInput + plainInput;
		char[] result = parser.find(input.toCharArray()).get(0).chars();
		assertEquals(jsonInput, new String(result));
		
	}		


	@Test
	public void ParseMixedString_MultipleJson_String() throws Exception {
		String plainInput = "Exercitationem distinctio provident voluptate velit sint accusantium blanditiis";
		String jsonArray = FileUtils.getResourceFileAsString("json/simple_array.json");		 
		String jsonObject = FileUtils.getResourceFileAsString("json/simple_object.json"); 

		String input = plainInput + jsonArray + plainInput + jsonObject + plainInput;
		Result results = parser.find(input.toCharArray());
		char[] array = results.get(0).chars();
		char[] object = results.get(1).chars();
		assertEquals(jsonArray, new String(array));
		assertEquals(jsonObject,new String(object));
		
	}			
	
	@Test
	public void ParseMixedString_ComplexJson_String() throws Exception {
		String plainInput = "Exercitationem distinctio provident voluptate velit sint accusantium blanditiis";
		String jsonObject = FileUtils.getResourceFileAsString("json/complex_object.json"); 

		String input = plainInput +jsonObject + plainInput;
		char[] result = parser.find(input.toCharArray()).get(0).chars();
		assertEquals(jsonObject, new String(result));
		
	}			

	
	@Test
	public void ParseMixedString_ComplexJson_And_complexString() throws Exception {
		String plainInput = "Exercitationem distinctio (((( {{{[}[}[}[}  pro[}vident voluptate velit sint accusantium blanditiis";
		String jsonObject = FileUtils.getResourceFileAsString("json/complex_object.json"); 

		String input = plainInput +jsonObject + plainInput;
		char[] result = parser.find(input.toCharArray()).get(0).chars();
		assertEquals(jsonObject, new String(result));
		
	}	

	
	
}
