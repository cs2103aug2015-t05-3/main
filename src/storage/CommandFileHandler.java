/**
 * Attempts to retrieve the list of commands that will be utilized.
 * @author Zander Chai
 */

//Source: http://www.mkyong.com/java/how-to-read-xml-file-in-java-dom-parser/

package storage;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class CommandFileHandler {
	
	DocumentBuilderFactory dbFactory;
	DocumentBuilder dBuilder;
	Document doc;
	File xmlFile;
	HashMap<String, String> cmdTable;
	
	// Can modify this to specify filename
	public CommandFileHandler(String fileName) {
		xmlFile = new File(fileName);
		dbFactory = DocumentBuilderFactory.newInstance();
		try {
			dBuilder = dbFactory.newDocumentBuilder();
			doc = dBuilder.parse(xmlFile);
			doc.getDocumentElement().normalize();
			cmdTable = new HashMap<>();
			parseCmd();
		} catch (ParserConfigurationException e) {
			System.err.println("Parser Config Error.");
		} catch (SAXException e) {
			System.err.println("SAX Exception.");
		} catch (IOException e) {
			System.err.println("IO Error.");
		}
	}
	
	private void parseCmd() {
		Element eElement;
		Node nNode;
		NodeList nList = doc.getElementsByTagName("command");
		
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
