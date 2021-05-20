package com.code;

import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class Utils {
    public static String readCustomer(InputStream is) {
	try {
	    DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
	    Document doc = builder.parse(is);
	    Element root = doc.getDocumentElement();

	    String cust = new String();
	    if (root.getAttribute("id") != null
		    && !root.getAttribute("id").trim().equals("")) {
		cust += Integer.valueOf(root.getAttribute("id")) + ",";
	    }
	    NodeList nodes = root.getChildNodes();
	    for (int i = 0; i < nodes.getLength(); i++) {
		Element element = (Element) nodes.item(i);
		if (element.getTagName().equals("first-name")) {
		    cust += element.getTextContent() + ",";
		} else if (element.getTagName().equals("last-name")) {
		    cust += element.getTextContent() + ",";
		} else if (element.getTagName().equals("street")) {
		    cust += element.getTextContent() + ",";
		} else if (element.getTagName().equals("city")) {
		    cust += element.getTextContent() + ",";
		} else if (element.getTagName().equals("state")) {
		    cust += element.getTextContent() + ",";
		} else if (element.getTagName().equals("zip")) {
		    cust += element.getTextContent() + ",";
		} else if (element.getTagName().equals("country")) {
		    cust += element.getTextContent() + ",";
		}
	    }
	    return cust;

	} catch (Exception e) {
	    return e.getStackTrace().toString();
	}
    }
}
