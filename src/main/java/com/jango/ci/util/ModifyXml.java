package com.jango.ci.util;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.jango.ci.exception.XmlAttributeNoteFoundException;
import com.jango.ci.exception.XmlNodeNotFoundException;

/**
 * 
 * @author Jango Chu
 * 
 */
public class ModifyXml {
	/**
	 * 
	 * @param filePath
	 * @param tagName
	 * @param attributName
	 * @param attributeValue
	 * @param newValue
	 * @return
	 * @throws XmlNodeNotFoundException
	 * @throws ParserConfigurationException
	 * @throws IOException
	 * @throws SAXException
	 * @throws TransformerException
	 */
	public static void modifyNodeTextByTagName(String filePath, String tagName,
			String newValue) throws XmlNodeNotFoundException, SAXException,
			IOException, ParserConfigurationException, TransformerException {

		Document document = null;
		document = loadInit(filePath);
		Document aDocument = setNodeTextByTagName(document, tagName, newValue);
		saveXML(aDocument, filePath);
	}

	/**
	 * 
	 * @param filePath
	 * @param tagName
	 * @param attributName
	 * @param attributeValue
	 * @param newValue
	 * @return
	 * @throws XmlNodeNotFoundException
	 * @throws ParserConfigurationException
	 * @throws IOException
	 * @throws SAXException
	 * @throws TransformerException
	 */
	public static void modifyNodeValueByTagNameAndAttributeAndAttributeValue(
			String filePath, String tagName, String attributName,
			String attributeValue, String newValue)
			throws XmlNodeNotFoundException, SAXException, IOException,
			ParserConfigurationException, TransformerException {

		Document document = null;
		document = loadInit(filePath);
		Document aDocument = setNodeValueByTagNameAndAttributeAndAttributeValue(
				document, tagName, attributName, attributeValue, newValue);
		saveXML(aDocument, filePath);
	}

	/**
	 * 
	 * @param filePath
	 * @param tagName
	 * @param attributName
	 * @param attributeValue
	 * @param newValue
	 * @return
	 * @throws XmlAttributeNoteFoundException
	 * @throws ParserConfigurationException
	 * @throws IOException
	 * @throws SAXException
	 * @throws TransformerException
	 */
	public static void modifyAttributeValueByTagNameAndAttribute(
			String filePath, String tagName, String attributName,
			String newValue) throws XmlAttributeNoteFoundException,
			SAXException, IOException, ParserConfigurationException,
			TransformerException {

		Document document = null;
		document = loadInit(filePath);
		Document aDocument = setAttributeValueByTagNameAndAttribute(document,
				tagName, attributName, newValue);
		saveXML(aDocument, filePath);
	}

	/**
	 * 
	 * @param filePath
	 * @param tagName
	 * @param attributName
	 * @param attributeValue
	 * @param newValue
	 * @return
	 * @throws XmlAttributeNoteFoundException
	 * @throws ParserConfigurationException
	 * @throws IOException
	 * @throws SAXException
	 * @throws TransformerException
	 */
	public static void modifyAttributeValueByTagNameAndAttributeAndAttributeValue(
			String filePath, String tagName, String attributName,
			String attributeValue, String newValue)
			throws XmlAttributeNoteFoundException, SAXException, IOException,
			ParserConfigurationException, TransformerException {

		Document document = null;
		document = loadInit(filePath);
		Document aDocument = setAttributeValueByTagNameAndAttributeAndAttributeValue(
				document, tagName, attributName, attributeValue, newValue);
		saveXML(aDocument, filePath);
	}

	/**
	 * 根据标签名称、属性名称、属性值，设置节点文本
	 * 
	 * @param document
	 * @param tagName
	 * @param attributName
	 * @param attributeValue
	 * @param newAttributeValue
	 * @return
	 * @throws XmlNodeNotFoundException
	 */
	public static Document setNodeValueByTagNameAndAttributeAndAttributeValue(
			Document document, String tagName, String attributName,
			String attributeValue, String newAttributeValue)
			throws XmlNodeNotFoundException {
		Node aa = getNodeByTagNameAndAttributeAndAttributeValue(document,
				tagName, attributName, attributeValue);
		aa.setTextContent(newAttributeValue);
		return document;
	}

	/**
	 * 根据标签名称、属性名称、属性值，设置属性值
	 * 
	 * @param document
	 * @param tagName
	 * @param attributName
	 * @param attributeValue
	 * @param newAttributeValue
	 * @return
	 * @throws XmlAttributeNoteFoundException
	 */
	public static Document setAttributeValueByTagNameAndAttributeAndAttributeValue(
			Document document, String tagName, String attributName,
			String attributeValue, String newAttributeValue)
			throws XmlAttributeNoteFoundException {
		Node aa = getAttributeByTagNameAndAttributeAndAttributeValue(document,
				tagName, attributName, attributeValue);
		aa.setNodeValue(newAttributeValue);
		return document;
	}

	/**
	 * 根据标签名称、属性名称，设置属性值
	 * 
	 * @param document
	 * @param tagName
	 * @param attributName
	 * @param newAttributeValue
	 * @return
	 * @throws XmlAttributeNoteFoundException
	 */
	public static Document setAttributeValueByTagNameAndAttribute(
			Document document, String tagName, String attributName,
			String newAttributeValue) throws XmlAttributeNoteFoundException {
		Node aa = getAttributeByTagNameAndAttribute(document, tagName,
				attributName);
		aa.setNodeValue(newAttributeValue);
		return document;
	}

	/**
	 * 根据标签名称，设置节点文本
	 * 
	 * @param document
	 * @param tagName
	 * @param newNodeText
	 * @return
	 * @throws XmlNodeNotFoundException
	 */
	public static Document setNodeTextByTagName(Document document,
			String tagName, String newNodeText) throws XmlNodeNotFoundException {
		Node aa = getNodeByName(document, tagName);
		if (aa != null) {
			aa.setTextContent(newNodeText);
			return document;
		}
		return null;
	}

	/**
	 * 根据标签名称，返回第一个标签节点
	 * 
	 * @param document
	 * @param tagName
	 * @return
	 * @throws XmlNodeNotFoundException
	 */
	public static Node getNodeByName(Document document, String tagName)
			throws XmlNodeNotFoundException {
		NodeList nodeList = document.getElementsByTagName(tagName);
		if (nodeList.getLength() > 0) {
			return nodeList.item(0);
		}
		throw new XmlNodeNotFoundException();
	}

	/**
	 * 根据标签名称、属性名称、属性值，返回标签节点
	 * 
	 * @param document
	 * @param tagName
	 * @param attributName
	 * @param attributeValue
	 * @return
	 * @throws XmlNodeNotFoundException
	 */
	public static Node getNodeByTagNameAndAttributeAndAttributeValue(
			Document document, String tagName, String attributName,
			String attributeValue) throws XmlNodeNotFoundException {
		NodeList nodeList = document.getElementsByTagName(tagName);
		for (int i = 0; i < nodeList.getLength(); i++) {
			NamedNodeMap oldAttribute = nodeList.item(i).getAttributes();
			for (int j = 0; j < oldAttribute.getLength(); j++) {
				String oldAttributeName = oldAttribute.item(j).getNodeName();
				String oldAttributeValue = oldAttribute.item(j).getNodeValue();
				if (oldAttributeName.equals(attributName)
						&& oldAttributeValue.equals(attributeValue)) {
					return nodeList.item(i);
				}
			}
		}
		throw new XmlNodeNotFoundException();
	}

	/**
	 * 根据标签名称、属性名称，返回属性节点
	 * 
	 * @param document
	 * @param tagName
	 * @param attributName
	 * @return
	 * @throws XmlAttributeNoteFoundException
	 */
	public static Node getAttributeByTagNameAndAttribute(Document document,
			String tagName, String attributName)
			throws XmlAttributeNoteFoundException {
		NodeList nodeList = document.getElementsByTagName(tagName);
		for (int i = 0; i < nodeList.getLength(); i++) {
			NamedNodeMap oldAttribute = nodeList.item(i).getAttributes();
			for (int j = 0; j < oldAttribute.getLength(); j++) {
				String oldAttributeName = oldAttribute.item(j).getNodeName();
				if (oldAttributeName.equals(attributName)) {
					return oldAttribute.item(j);
				}
			}
		}
		throw new XmlAttributeNoteFoundException();
	}

	/**
	 * 根据标签名称、属性名称、属性值，返回属性节点
	 * 
	 * @param document
	 * @param tagName
	 * @param attributName
	 * @param attributeValue
	 * @return
	 * @throws XmlAttributeNoteFoundException
	 */
	public static Node getAttributeByTagNameAndAttributeAndAttributeValue(
			Document document, String tagName, String attributName,
			String attributeValue) throws XmlAttributeNoteFoundException {
		NodeList nodeList = document.getElementsByTagName(tagName);
		for (int i = 0; i < nodeList.getLength(); i++) {
			NamedNodeMap oldAttribute = nodeList.item(i).getAttributes();
			for (int j = 0; j < oldAttribute.getLength(); j++) {
				String oldAttributeName = oldAttribute.item(j).getNodeName();
				String oldAttributeValue = oldAttribute.item(j).getNodeValue();
				if (oldAttributeName.equals(attributName)
						&& oldAttributeValue.equals(attributeValue)) {
					return oldAttribute.item(j);
				}
			}
		}
		throw new XmlAttributeNoteFoundException();
	}

	/**
	 * 
	 * @param document
	 * @param filePath
	 * @throws TransformerException
	 */
	private static void saveXML(Document document, String filePath)
			throws TransformerException {
		TransformerFactory tFactory = TransformerFactory.newInstance();
		Transformer transformer = tFactory.newTransformer();
		DOMSource source = new DOMSource(document);
		StreamResult result = new StreamResult(new File(filePath));
		transformer.transform(source, result);
	}

	/**
	 * 
	 * @param filePath
	 * @return
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	private static Document loadInit(String filePath) throws SAXException,
			IOException, ParserConfigurationException {
		Document document = null;
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		document = builder.parse(new File(filePath));
		document.normalize();
		return document;
	}
}
