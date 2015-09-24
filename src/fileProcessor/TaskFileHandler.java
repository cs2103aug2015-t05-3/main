// Taken from http://www.mkyong.com/java/how-to-read-xml-file-in-java-dom-parser/
// Modified for CS2103T Project

// Zander Chai

package fileProcessor;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class TaskFileHandler {
	
	DocumentBuilderFactory dbFactory;
	DocumentBuilder dBuilder;
	Document doc;
	Element eElement;
	File xmlFile;
	Node nNode;
	NodeList nList;
	
	// Can modify this to specify filename
	public TaskFileHandler() throws Exception {
		xmlFile = new File("tasks.xml");
		dbFactory = DocumentBuilderFactory.newInstance();
		dBuilder = dbFactory.newDocumentBuilder();
		doc = dBuilder.parse(xmlFile);
		doc.getDocumentElement().normalize();
	}
	
	public void parseTask() {
		System.out.println("Root element : " + doc.getDocumentElement().getNodeName());
		nList = doc.getElementsByTagName("task");
		for (int i = 0; i < nList.getLength(); i++) {
			nNode = nList.item(i);
			System.out.println();
					
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				eElement = (Element) nNode;				
				System.out.println("Task ID : " + eElement.getAttribute("id"));
				System.out.println("Title   : " + eElement.getElementsByTagName("title").item(0).getTextContent());
				System.out.println("Start   : " + eElement.getElementsByTagName("startDate").item(0).getTextContent() + " " + eElement.getElementsByTagName("startTime").item(0).getTextContent());
				System.out.println("End     : " + eElement.getElementsByTagName("endDate").item(0).getTextContent() + " " + eElement.getElementsByTagName("endTime").item(0).getTextContent());
				System.out.println("Done?   : " + eElement.getElementsByTagName("done").item(0).getTextContent());
			}
		}
	}
	
	public static void main(String[] args) throws Exception {
		TaskFileHandler runT = new TaskFileHandler();
		runT.parseTask();
	}
}
