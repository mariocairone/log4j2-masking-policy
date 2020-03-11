package com.mariocairone.log4j2.core.data.masker;

import java.io.CharArrayReader;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.amariocairone.log4j2.api.data.exceptions.InvalidInputException;
import com.mariocairone.log4j2.api.data.masker.Masker;
import com.mariocairone.log4j2.api.data.masker.MaskingEngine;
import com.mariocairone.log4j2.core.data.masker.BasicMaskingEngine;


public class XmlMasker implements Masker {

    private final Set<String> whitelistedKeys;
    private final Set<XPathExpression> whitelistedXPaths;
    private final boolean enabled;
    private XPathFactory factory = XPathFactory.newInstance();

    private MaskingEngine maskingEngine;

	
    public XmlMasker(Collection<String> whitelist, boolean enabled,MaskingEngine maskingEngine )  {
      
        this.maskingEngine = maskingEngine;
        this.enabled = enabled;
       
        
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        try {
			documentBuilderFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
         
        whitelistedKeys = new HashSet<>();;
        whitelistedXPaths = new HashSet<>();
         
		whitelist.forEach(item -> {
			if (item.startsWith("/")) {
				whitelistedXPaths.add(toXPathExpression(item));
			} else {
				whitelistedKeys.add(item.toUpperCase());
			}
		});
    }
	
    public XmlMasker(Collection<String> fieldList, boolean enabled) {
       this(fieldList,enabled, new BasicMaskingEngine());
    }

    public XmlMasker() {
        this(Collections.emptySet(), true);
    }

    public XmlMasker(boolean enabled) {
        this(Collections.emptySet(), enabled);
    }

    public XmlMasker(Collection<String> fieldList) {
        this(fieldList, true);
    }    
    
	@Override
	public char[] mask(char[] payload) {
        
		if (!enabled)
            return payload;
        
        if (payload == null)
             throw new InvalidInputException("null cannot be masked");
		
        Document document = toXmlDocument(payload); 
        Set<Node> nodes = getNodesFromXpathExpressions(document);
        
		traverseAndMask(document.getDocumentElement(), nodes);;
		
		return toXmlString(document).toCharArray() ;
	}

	public void traverseAndMask(Node node, Set<Node> nodes) {
	    NodeList nodeList = node.getChildNodes();
	 
	    for (int i = 0; i < nodeList.getLength(); i++) {
	        Node currentNode = nodeList.item(i);
	        
	        if(nodes.contains(currentNode))
	        	continue;
	        
	        maskAttributes(nodes, currentNode);
	        	        // recursively call maskElements until you find a Leaf node
	        if (currentNode.getNodeType() == Node.ELEMENT_NODE) {
	        	
	        	traverseAndMask(currentNode, nodes);
	        } else if (currentNode.getNodeType() == Node.TEXT_NODE) {
	            // leaf node.. apply masking logic
	            String name = currentNode.getParentNode().getNodeName();
	            
	            name = removeNamespace(name);
	            if(name ==null)
	            	continue;
	            
	            if (!whitelistedKeys.contains(name.toUpperCase())) 
	            	currentNode.setTextContent(maskNode(currentNode));
	      
	        }
	 
	    }
	 
	}

	private NamedNodeMap maskAttributes(Set<Node> nodes, Node currentNode) {
		
		NamedNodeMap attributes = currentNode.getAttributes();
		if(attributes != null) {
		    for(int j = 0;j< attributes.getLength(); j++) {

		        
		    	Node attribute = attributes.item(j);
		        
		    	if(nodes.contains(attribute) 
		    			|| whitelistedKeys.contains(attribute.getNodeName().toUpperCase()))
		        	continue;
		        
		    	attribute.setNodeValue(maskNode(attribute));
		    	
		    }
		}
		return attributes;
	}

	private String maskNode(Node currentNode) {
		String value = currentNode.getNodeValue();
		if (value.matches("-?\\d+(\\.\\d+)?"))
			value = maskingEngine.maskNumber(value);
		else
			value = maskingEngine.maskString(value);
		return value;
	}

	private String removeNamespace(String name) {
		int index = name.indexOf(':');
		if(index > 0)
			name = name.substring(index+1);
		
		return name;
	}
	
	  private  Document toXmlDocument(char[] str) {
	 
	        DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory
	                .newInstance();
	        DocumentBuilder docBuilder = null;
	        Document document = null;
			try {
				docBuilder = docBuilderFactory.newDocumentBuilder();
				document= docBuilder.parse(new InputSource(new CharArrayReader(str)));
			} catch (ParserConfigurationException | SAXException | IOException e) {
				e.printStackTrace();
			}
	     
	        return document;
	    }
	 
	    private  String toXmlString(Document document)
	        {

	    	 StringWriter strWriter = new StringWriter();
	        try {
		        TransformerFactory transformerFactory = TransformerFactory
		                .newInstance();
		        Transformer transformer = transformerFactory.newTransformer();
		        
		        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
		        
		        DOMSource source = new DOMSource(document);
		     
		        StreamResult result = new StreamResult(strWriter);	        	
				transformer.transform(source, result);
			} catch (TransformerException e) {
				e.printStackTrace();
			}
	 
	        return strWriter.getBuffer().toString();
	 
	        
	    }

		public Document maskObject(Document payload, Set<Node> nodes) {

		        if (payload == null)
		            return null;
				
				 traverseAndMask(payload.getDocumentElement(), nodes);

				 return payload;
		}
		
		private XPathExpression toXPathExpression(String expression) {
			try {
				return factory.newXPath().compile(expression);
			} catch (XPathExpressionException e) {
				throw new InvalidInputException("Invalid XPath expression: "+ expression, e );
			}
		}
		
		private Set<Node> getNodesFromXpathExpressions(Document document) {
			 Set<Node> nodes = new HashSet<>();
	     
			for(XPathExpression expression : whitelistedXPaths) {
				Node node = null;
				try {
					node = (Node)  expression.evaluate(document, XPathConstants.NODE);
				} catch (XPathExpressionException e) {
					e.printStackTrace();
				}
				nodes.add(node);
			}
			return nodes;
		}
	
}