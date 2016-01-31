import javax.xml.parsers.SAXParser;  
import javax.xml.parsers.SAXParserFactory;  
import org.xml.sax.Attributes;  
import org.xml.sax.SAXException;  
import org.xml.sax.helpers.DefaultHandler;  
/**
 *   
 * @author Ben Guo
 *
 */
public class Config extends DefaultHandler{  
private static String value;	
public static String read(String xml){
  try {    
   SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();   
   SAXParser saxParser = saxParserFactory.newSAXParser();   
   DefaultHandler defaultHandler = new DefaultHandler(){  
  
    String data ="close";   
 
    public void startElement(String uri, String localName, String qName,  
      Attributes attributes) throws SAXException {  
     
     if (qName.equalsIgnoreCase(xml)) {  
      data = "open";  
     } 
    }  
    
    public void characters(char ch[], int start, int length)  
      throws SAXException {  
       
     if (data.equals("open")) {  
      value = new String(ch, start, length);
     }
    }    
    
    public void endElement(String uri, String localName, String qName)  
      throws SAXException {  
 
     if (qName.equalsIgnoreCase(xml)) {  
      data = "close";
     }
    }
   };
   saxParser.parse("config.xml", defaultHandler);  
  } catch (Exception e) {  
   e.printStackTrace();  
  }
return value;  
 }  
}  