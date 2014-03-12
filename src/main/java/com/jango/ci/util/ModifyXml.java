package com.jango.ci.util;

import hudson.model.BuildListener;

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
	 */
	public static boolean modifyNodeTextByTagName(BuildListener listener,
			String filePath, String tagName, String newValue) {
		Document document = loadInit(listener, filePath);
		if (document != null) {
			Document aDocument = null;
			aDocument = setNodeTextByTagName(listener, document, tagName,
					newValue);
			if (aDocument != null) {
				boolean result = saveXML(listener, aDocument, filePath);
				if (result) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 
	 * @param filePath
	 * @param tagName
	 * @param attributName
	 * @param attributeValue
	 * @param newValue
	 * @return
	 */
	public static boolean modifyNodeValueByTagNameAndAttributeAndAttributeValue(
			BuildListener listener, String filePath, String tagName,
			String attributName, String attributeValue, String newValue) {

		Document document = null;
		document = loadInit(listener, filePath);
		if (document != null) {
			Document aDocument = setNodeValueByTagNameAndAttributeAndAttributeValue(
					listener, document, tagName, attributName, attributeValue,
					newValue);
			if (aDocument != null) {
				boolean result = saveXML(listener, aDocument, filePath);
				if (result) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 
	 * @param filePath
	 * @param tagName
	 * @param attributName
	 * @param attributeValue
	 * @param newValue
	 * @return
	 */
	public static boolean modifyAttributeValueByTagNameAndAttribute(
			BuildListener listener, String filePath, String tagName,
			String attributName, String newValue) {

		Document document = null;
		document = loadInit(listener, filePath);
		if (document != null) {
			Document aDocument = setAttributeValueByTagNameAndAttribute(
					listener, document, tagName, attributName, newValue);
			if (aDocument != null) {
				boolean result = saveXML(listener, aDocument, filePath);
				if (result) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 
	 * @param filePath
	 * @param tagName
	 * @param attributName
	 * @param attributeValue
	 * @param newValue
	 * @return
	 */
	public static boolean modifyAttributeValueByTagNameAndAttributeAndAttributeValue(
			BuildListener listener, String filePath, String tagName,
			String attributName, String attributeValue, String newValue) {

		Document document = null;
		document = loadInit(listener, filePath);
		if (document != null) {
			Document aDocument = setAttributeValueByTagNameAndAttributeAndAttributeValue(
					listener, document, tagName, attributName, attributeValue,
					newValue);
			if (aDocument != null) {
				boolean result = saveXML(listener, aDocument, filePath);
				if (result) {
					return true;
				}
			}
		}
		return false;
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
			BuildListener listener, Document document, String tagName,
			String attributName, String attributeValue, String newAttributeValue) {
		Node aa = null;
		try {
			aa = getNodeByTagNameAndAttributeAndAttributeValue(document,
					tagName, attributName, attributeValue);
		} catch (XmlNodeNotFoundException e) {
			listener.getLogger().println(
					"[ERROR]没有找到匹配节点名称为\"" + tagName + "\",属性名称为\""
							+ attributName + "\"且属性值为\"" + attributeValue
							+ "\"的节点");
			listener.getLogger().println(e);
			return null;
		}
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
			BuildListener listener, Document document, String tagName,
			String attributName, String attributeValue, String newAttributeValue) {
		Node aa = null;
		try {
			aa = getAttributeByTagNameAndAttributeAndAttributeValue(document,
					tagName, attributName, attributeValue);
		} catch (XmlAttributeNoteFoundException e) {
			listener.getLogger().println(
					"[ERROR]没有找到匹配节点名称为\"" + tagName + "\",属性名称为\""
							+ attributName + "\"且属性值为\"" + attributeValue
							+ "\"的属性");
			listener.getLogger().println(e);
			return null;
		}
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
			BuildListener listener, Document document, String tagName,
			String attributName, String newAttributeValue) {
		Node aa = null;
		try {
			aa = getAttributeByTagNameAndAttribute(document, tagName,
					attributName);
		} catch (XmlAttributeNoteFoundException e) {
			listener.getLogger().println(
					"[ERROR]没有找到匹配节点名称\"" + tagName + "\",属性名称为\""
							+ attributName + "\"的属性");
			listener.getLogger().println(e);
			return null;
		}
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
	public static Document setNodeTextByTagName(BuildListener listener,
			Document document, String tagName, String newNodeText) {
		Node aa = null;
		try {
			aa = getNodeByName(document, tagName);
		} catch (XmlNodeNotFoundException e) {
			listener.getLogger().println(
					"[ERROR]没有找到名称为：\"" + tagName + "\"的节点。");
			listener.getLogger().println(e);
			return null;
		}
		aa.setTextContent(newNodeText);
		return document;
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
	private static boolean saveXML(BuildListener listener, Document document,
			String filePath) {
		TransformerFactory tFactory = TransformerFactory.newInstance();
		Transformer transformer = null;
		try {
			transformer = tFactory.newTransformer();
		} catch (TransformerConfigurationException e) {
			listener.getLogger().println(e);
			return false;
		}
		DOMSource source = new DOMSource(document);
		StreamResult result = new StreamResult(new File(filePath));
		try {
			transformer.transform(source, result);
		} catch (TransformerException e) {
			listener.getLogger().println(e);
			return false;
		}
		return true;
	}

	/**
	 * 
	 * @param filePath
	 * @return
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	private static Document loadInit(BuildListener listener, String filePath) {
		Document document = null;
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = null;
		try {
			builder = factory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
			return document;
		}
		try {
			document = builder.parse(new File(filePath));
		} catch (SAXException e) {
			e.printStackTrace();
			return document;
		} catch (IOException e) {
			listener.getLogger().println(e);
			return document;
		}
		document.normalize();
		return document;
	}
}
