package reportdao.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

/**
 * XML操作工具类，以jdom作为引擎
 * 
 * @author AHONG
 * 
 */
public class XmlUtil {

	public static Document openXml(File xml) {
		SAXBuilder builder = new SAXBuilder();
		Document doc = null;
		try {
			doc = builder.build(xml);
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return doc;
	}
	
	public static Document openXml(String xml) {
		return openXml(new File(xml));
	}
	
	public static void updateXmlFile(Document ndoc, String xml) {
		XMLOutputter xmlOutputter = new XMLOutputter();
		xmlOutputter.setFormat(Format.getPrettyFormat().setEncoding("utf-8"));
		try {
			xmlOutputter.output(ndoc, new FileOutputStream(xml));
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
}
