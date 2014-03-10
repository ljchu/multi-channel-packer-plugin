package com.jango.ci.util;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.log4j.helpers.Transform;
import org.dom4j.DocumentFactory;
import org.jvnet.libpam.impl.PAMLibrary.pam_conv;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class ModifyXml {
	
	public static boolean modifyNodeTextByTagName(String filePath,String tagName,String attributName,String attributeValue) {
		
		Document document = null;
		try {
			document = loadInit(filePath);
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		String newAttributeValue = "123";
//		Document aDocument = setNodeValueByTagNameAndAttribute(document, tagName, attributName, attributeValue, newAttributeValue);
		Document aDocument = setNodeValueByTagName(document, tagName, newAttributeValue);
		try {
			saveXML(aDocument, "d:\\22.xml");
		} catch (TransformerException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public static Document setNodeValueByTagNameAndAttribute(Document document,String tagName,String attributName,
			String attributeValue,String newAttributeValue) {
		Node aa = getNodeByTagNameAndAttribute(document, tagName, attributName,attributeValue);
		System.out.println("node name:"+aa.getNodeName());
		System.out.println("node old value:"+aa.getNodeValue());
		aa.setTextContent(newAttributeValue);
		System.out.println("node value:"+aa.getNodeValue());
		System.out.println("node text:"+aa.getTextContent());
		return document;
	}
	
	private static void saveXML(Document document, String filePath) throws TransformerException {
		TransformerFactory tFactory = TransformerFactory.newInstance();
		Transformer transformer = tFactory.newTransformer();
		DOMSource source = new DOMSource(document);
		StreamResult result = new StreamResult(new File(filePath));
		transformer.transform(source, result);
	}

	public static Document setAttributeNodeValueByTagNameAndAttribute(Document document,String tagName,String attributName,
			String attributeValue,String newAttributeValue) {
		Node aa = getAttributeNodeByTagNameAndAttribute(document, tagName, attributName,attributeValue);
		System.out.println("node name:"+aa.getNodeName());
		System.out.println("node old value:"+aa.getNodeValue());
		aa.setNodeValue(newAttributeValue);
		System.out.println("node value:"+aa.getNodeValue());
		System.out.println("node text:"+aa.getTextContent());
		return document;
	}
	public static Node getNodeByName(Document document,String tagName) {
		NodeList nodeList = document.getElementsByTagName(tagName);
		if (nodeList.getLength()>0) {
			return nodeList.item(0);
		}
		return null;
	}
	
	public static Document setNodeValueByTagName(Document document,String tagName,String newNodeValue) {
		Node aa = getNodeByName(document, tagName);
		if (aa!=null) {
			aa.setTextContent(newNodeValue);
			return document;
		}
		return null;
	}
	
	public static Node getNodeByTagNameAndAttribute(Document document,
			String tagName,String attributName,String attributeValue) {
		NodeList nodeList = document.getElementsByTagName(tagName);
		for (int i = 0; i < nodeList.getLength(); i++) {
			NamedNodeMap oldAttribute = nodeList.item(i).getAttributes();
			for (int j = 0; j < oldAttribute.getLength(); j++) {
				String oldAttributeName = oldAttribute.item(j).getNodeName();
				String oldAttributeValue = oldAttribute.item(j).getNodeValue();
				if (oldAttributeName.equals(attributName) && oldAttributeValue.equals(attributeValue)) {
					System.out.println(oldAttribute.item(j));
					System.out.print("aaa");
					return nodeList.item(i);
				}
			}
		}
		return null;
	}
	public static Node getAttributeNodeByTagNameAndAttribute(Document document,
			String tagName,String attributName,String attributeValue) {
		NodeList nodeList = document.getElementsByTagName(tagName);
		for (int i = 0; i < nodeList.getLength(); i++) {
			NamedNodeMap oldAttribute = nodeList.item(i).getAttributes();
			for (int j = 0; j < oldAttribute.getLength(); j++) {
				String oldAttributeName = oldAttribute.item(j).getNodeName();
				String oldAttributeValue = oldAttribute.item(j).getNodeValue();
				if (oldAttributeName.equals(attributName) && oldAttributeValue.equals(attributeValue)) {
					System.out.println(oldAttribute.item(j));
					System.out.print("aaa");
					return oldAttribute.item(j);
				}
			}
		}
		return null;
	}
	private static Document loadInit(String filePath) throws ParserConfigurationException, SAXException, IOException {
		Document document = null;
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		document = builder.parse(new File(filePath));
		document.normalize();
		return document;
	}
	
	public static void main(String[] args) {
		boolean aa=modifyNodeTextByTagName("d:\\default_config.xml", "baseurl", "xx", "4051");
	}
}
