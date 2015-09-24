package fileProcessor;

// Taken from http://www.mkyong.com/java/how-to-read-xml-file-in-java-dom-parser/
// Modified for CS2103T Project

// Zander Chai

//package fileProcessor;

import java.io.File;

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
	Node nNode;
	NodeList nList;
	String[] basicCmd;
	
	// Can modify this to specify filename
	public CommandFileHandler() throws Exception {
		xmlFile = new File("commands.xml");
		dbFactory = DocumentBuilderFactory.newInstance();
		dBuilder = dbFactory.newDocumentBuilder();
		doc = dBuilder.parse(xmlFile);
		doc.getDocumentElement().normalize();
		basicCmd = new String[] {"add", "edit", "delete", "list", "undo", "search"};
	}
	
	public void parseCmd() {
		nList = doc.getElementsByTagName("command");
		
		for (int i = 0; i < nList.getLength(); i++) {
			nNode = nList.item(i);
			System.out.println();
					
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				eElement = (Element) nNode;				
				System.out.println("Category : " + eElement.getAttribute("category"));
				
				for (int j = 0; j < eElement.getElementsByTagName("word").getLength(); j++) {
					System.out.println("Word     : " + eElement.getElementsByTagName("word").item(j).getTextContent());
				}
			}
		}
	}
	

	public static void main(String[] args) throws Exception {
		CommandFileHandler runC = new CommandFileHandler();
		runC.parseCmd();
	}

}
