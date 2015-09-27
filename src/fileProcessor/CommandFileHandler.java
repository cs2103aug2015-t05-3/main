// Taken from http://www.mkyong.com/java/how-to-read-xml-file-in-java-dom-parser/
// Modified for CS2103T Project

// Zander Chai

package fileProcessor;

import java.io.File;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class CommandFileHandler {
	
	DocumentBuilderFactory dbFactory;
	DocumentBuilder dBuilder;
	Document doc;
	Element eElement;
	File xmlFile;
	HashMap<String, String> cmdTable;
	Node nNode;
	NodeList nList;
	
	// Can modify this to specify filename
	public CommandFileHandler(String fileName) throws Exception {
		xmlFile = new File(fileName);
		dbFactory = DocumentBuilderFactory.newInstance();
		dBuilder = dbFactory.newDocumentBuilder();
		doc = dBuilder.parse(xmlFile);
		doc.getDocumentElement().normalize();
		cmdTable = new HashMap<>();
	}
	
	private void parseCmd() {
		nList = doc.getElementsByTagName("command");
		
		for (int i = 0; i < nList.getLength(); i++) {
			nNode = nList.item(i);
					
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				eElement = (Element) nNode;				
				String category = eElement.getAttribute("category");
				
				for (int j = 0; j < eElement.getElementsByTagName("word").getLength(); j++) {
					String word = eElement.getElementsByTagName("word").item(j).getTextContent();
					cmdTable.put(word, category);
				}
			}
		}
	}
	
	public HashMap<String, String> getCmdTable() {
		parseCmd();
		return cmdTable;
	}
	

	/*public static void main(String[] args) throws Exception {
		CommandFileHandler runC = new CommandFileHandler("commands.xml");
		HashMap<String, String> sample = runC.getCmdTable();
		for (HashMap.Entry<String, String> entry : sample.entrySet()) {
			   System.out.println("key=" + entry.getKey() + ", value=" + entry.getValue());
		}
	}*/

}
