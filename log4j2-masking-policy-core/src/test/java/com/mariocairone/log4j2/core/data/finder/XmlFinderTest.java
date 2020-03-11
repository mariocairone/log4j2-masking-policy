package com.mariocairone.log4j2.core.data.finder;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.mariocairone.log4j2.api.data.finder.Result;
import com.mariocairone.log4j2.core.data.finder.XmlFinder;
import com.mariocairone.log4j2.core.data.utils.FileUtils;

public class XmlFinderTest {

	XmlFinder finder = new XmlFinder();
	
	@Test
	public void testNull() throws Exception {
		char[] input = null;
		Result result = finder.find(input);
		assertNotNull(result);
		assertTrue(result.size() == 0);
	}

	@Test
	public void testEmpty() throws Exception {
		String input = "";
		Result result = finder.find(input.toCharArray());		
		assertNotNull(result);
		assertTrue(result.size() == 0);
	}
	
	@Test
	public void testPlainchars() throws Exception {
		String input = "Exercitationem distinctio provident voluptate velit sint accusantium blanditiis";
		Result result = finder.find(input.toCharArray());		
		assertNotNull(result);
		assertTrue(result.size() == 0);
	}	
	
	@Test
	public void testSimpleTag() throws Exception {
		
		String input = "<mario>Prova<mario/>";
		Result result = finder.find(input.toCharArray());
		assertTrue(result.size() == 1);
		
	}
	
	@Test
	public void testSimple() throws Exception {
		
		String input = FileUtils.getResourceFileAsString("xml/simple.xml");
		Result result = finder.find(input.toCharArray());
		assertTrue(result.size() == 1);
	} 
	
	@Test
	public void testSimpleWithPrefix() throws Exception {
		String prefix = "Exercitationem distinctio provident voluptate velit sint accusantium blanditiis";
		String input = FileUtils.getResourceFileAsString("xml/simple.xml");
		Result result = finder.find((prefix + input).toCharArray());
		assertTrue(result.size() == 1);
	} 
	
	
	@Test
	public void testSimpleWithMultiple() throws Exception {
		String text = "Exercitationem distinctio provident voluptate velit sint accusantium blanditiis";
		String input = FileUtils.getResourceFileAsString("xml/simple.xml");
		
		String second = "<mario>Prova<mario/>";

		Result result = finder.find((text + input + text + second).toCharArray());
		assertTrue(result.size() == 2);
	} 
	
	
	
}
