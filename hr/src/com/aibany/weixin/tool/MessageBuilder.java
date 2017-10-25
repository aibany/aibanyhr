package com.aibany.weixin.tool;

import com.aibany.weixin.model.Article;
import com.aibany.weixin.model.NewsMessage;
import com.aibany.weixin.model.TextMessage;
import com.thoughtworks.xstream.XStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.Writer;
import javax.servlet.http.HttpServletRequest;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.core.util.QuickWriter;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import com.thoughtworks.xstream.io.xml.XppDriver;

public class MessageBuilder
{
  public static Map<String, String> parseXml(HttpServletRequest request)
    throws Exception
  {
    Map<String, String> map = new HashMap();
    InputStream inputStream = request.getInputStream();
    SAXReader reader = new SAXReader();
    Document document = reader.read(inputStream);
    Element root = document.getRootElement();
    List<Element> elementList = root.elements();
    
    for (Element e : elementList) {
      if (e.elements().size() > 0) {
        List<Element> list = e.elements();
        for (Element e2 : list) {
          map.put(e2.getName(), e2.getText());
        }
      } else {
        map.put(e.getName(), e.getText());
      }
    }
    
    inputStream.close();
    inputStream = null;
    
    return map;
  }
  
  public static String textMessageToXml(TextMessage textMessage) {
    xstream.alias("xml", textMessage.getClass());
    return xstream.toXML(textMessage);
  }
  
  public static String newsMessageToXml(NewsMessage newsMessage) {
    xstream.alias("xml", newsMessage.getClass());
    xstream.alias("item", new Article().getClass());
    return xstream.toXML(newsMessage);
  }
  
  private static XStream xstream = new XStream(new XppDriver() {
		public HierarchicalStreamWriter createWriter(Writer out) {
			return new PrettyPrintWriter(out) {
				boolean cdata = true;

				protected void writeText(QuickWriter writer, String text) {
					if (cdata) {
						writer.write("<![CDATA[");
						writer.write(text);
						writer.write("]]>");
					} else {
						writer.write(text);
					}
				}
			};
		}
	});
}
