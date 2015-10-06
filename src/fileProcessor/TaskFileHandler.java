package fileProcessor;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import tds.Task;
import tds.Task.FLAG_TYPE;
import tds.Task.PRIORITY_TYPE;
import util.TimeUtil;

public class TaskFileHandler {

	ArrayList<Task> tasks;
	DocumentBuilderFactory dbFactory;
	DocumentBuilder dBuilder;
	Document doc;
	Element root;
	File xmlFile;
	
	public TaskFileHandler(String fileName) throws Exception {
		tasks = new ArrayList<>();
		xmlFile = new File(fileName);

		if (!xmlFile.exists()) { 
			PrintWriter writer = new PrintWriter(fileName, "UTF-8");
			writer.println("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>");
			writer.println("<tasklist>");
			writer.println("</tasklist>");
			writer.close();
		}
			
		dbFactory = DocumentBuilderFactory.newInstance();
		dBuilder = dbFactory.newDocumentBuilder();
		doc = dBuilder.parse(xmlFile);
		doc.getDocumentElement().normalize();
		root = doc.getDocumentElement();
		
		importAllTasks();
	}

	/**
	 * @return ArrayList<Task> : List of Tasks
	 */
	public ArrayList<Task> retrieveTaskList() {
		importAllTasks();
		return tasks;
	}

	/**
	 * Adds new task to XML file
	 * @param t
	 */
	public void add(Task t) throws Exception {
		int id = t.getId();
		String[] headers = { "title", "startTime", "endTime", "flag", "priority" };
		//Task t = new Task("Add add method", 1447252200000L, 1452868200000L, FLAG_TYPE.NULL, PRIORITY_TYPE.HIGH);
	
		Element newTask = doc.createElement("task");
		newTask.setAttribute("id", "" + id);
	
		root.appendChild(newTask);
	
		for (int i = 0; i < headers.length; i++) {
			Element e = addElement(headers[i], t);
			newTask.appendChild(e);
		}
		genXML();
	}
	
	/**
	 * Delete task from XML file
	 * @param id
	 * @throws Exception
	 */
	public void delete(int id) throws Exception {
		Element e = locateID(id);
		root.removeChild(e);
		genXML();
	}
	
	private Element locateID(int id) {
		
		Element e;
		Node nNode;
		NodeList nList = doc.getElementsByTagName("task");
		
		for (int i = 0; i < nList.getLength(); i++) {
			nNode = nList.item(i);
			
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				e = (Element)nNode;
				if (e.getAttribute("id").equals(id+"")) {
					return e;
				}
			}
		}
		return null;
	}
	
	/*
	 * Reorganizes the ID number in XML file in ascending order
	 */
	private void optimizeID() {
		
		Element e;
		Node nNode;
		NodeList nList = doc.getElementsByTagName("task");
		
		for (int i = 0; i < nList.getLength(); i++) {
			nNode = nList.item(i);
			
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				e = (Element) nNode;
				e.setAttribute("id", Integer.toString(i));
			}
		}
		
		try {
			genXML();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	/*
	 * Imports all tasks from specified XML file and add them to ArrayList
	 */
	private void importAllTasks() {
		
		optimizeID();
		
		Element eElement;
		Node nNode;
		NodeList nList = doc.getElementsByTagName("task");
		Task t;
		
		for (int i = 0; i < nList.getLength(); i++) {
			nNode = nList.item(i);

			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				eElement = (Element) nNode;
				// System.out.println("Task ID : " +
				// eElement.getAttribute("id"));
				int id = Integer.parseInt(eElement.getAttribute("id"));
				
				String title = retrieveElement(eElement, "title");
				long startTime = TimeUtil.getLongTime(retrieveElement(eElement, "startTime"));
				long endTime = TimeUtil.getLongTime(retrieveElement(eElement, "endTime"));
				FLAG_TYPE flag = detFlag(retrieveElement(eElement, "flag"));
				PRIORITY_TYPE priority = detPriority(retrieveElement(eElement, "priority"));
				
				t = new Task(id, title, startTime, endTime, flag, priority);
				tasks.add(t);
			}
		}
	}

	private String retrieveElement(Element e, String s) {
		return e.getElementsByTagName(s).item(0).getTextContent();
	}

	private FLAG_TYPE detFlag(String s) {
		switch (s) {
		case "NULL":
			return FLAG_TYPE.NULL;
		case "DONE":
			return FLAG_TYPE.DONE;
		default:
			return FLAG_TYPE.NULL;
		}
	}

	private PRIORITY_TYPE detPriority(String s) {
		switch (s) {
		case "VERY_HIGH":
			return PRIORITY_TYPE.VERY_HIGH;
		case "HIGH":
			return PRIORITY_TYPE.HIGH;
		case "ABOVE_NORMAL":
			return PRIORITY_TYPE.ABOVE_NORMAL;
		case "NORMAL":
			return PRIORITY_TYPE.NORMAL;
		case "BELOW_NORMAL":
			return PRIORITY_TYPE.BELOW_NORMAL;
		case "LOW":
			return PRIORITY_TYPE.LOW;
		case "VERY_LOW":
			return PRIORITY_TYPE.VERY_LOW;
		default:
			return PRIORITY_TYPE.NORMAL;
		}
	}

	private void display() {
		for (Task t : tasks) {
			System.out.println(t.getId());
			System.out.println(t.getName());
			System.out.println(TimeUtil.getFormattedDate(t.getStartTime()));
			System.out.println(TimeUtil.getFormattedDate(t.getEndTime()));
			System.out.println(t.getFlag());
			System.out.println(t.getPriority());

			System.out.println();
		}
	}

	/**
	 * Remove text nodes that are used for indentation.
	 * 
	 * @param Node
	 */
	// Source: https://stackoverflow.com/a/31421664
	private static void removeEmptyText(Node node) {
		Node child = node.getFirstChild();
		while (child != null) {
			Node sibling = child.getNextSibling();
			if (child.getNodeType() == Node.TEXT_NODE) {
				if (child.getTextContent().trim().isEmpty())
					node.removeChild(child);
			} else
				removeEmptyText(child);
			child = sibling;
		}
	}

	private void printFile(Document document, int indent) throws Exception {

		removeEmptyText(document.getDocumentElement());

		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

		transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "" + indent);
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");

		Result result = new StreamResult(xmlFile);
		Source source = new DOMSource(document);
		transformer.transform(source, result);
	}

	private void genXML() throws Exception {
		printFile(doc, 4);
	}

	private Element addElement(String s, Task t) {
		Element e = doc.createElement(s);
		switch (s) {
		case "title":
			e.appendChild(doc.createTextNode(t.getName()));
			break;
		case "startTime":
			e.appendChild(doc.createTextNode(TimeUtil.getFormattedDate(t.getStartTime())));
			break;
		case "endTime":
			e.appendChild(doc.createTextNode(TimeUtil.getFormattedDate(t.getEndTime())));
			break;
		case "flag":
			e.appendChild(doc.createTextNode("" + t.getFlag()));
			break;
		case "priority":
			e.appendChild(doc.createTextNode("" + t.getPriority()));
			break;
		}
		return e;
	}

	public static void main(String[] args) throws Exception {
		TaskFileHandler runT = new TaskFileHandler("_tasks.xml");
		//runT.add();
		//runT.genXML();
		//runT.delete();
		//runT.genXML();
		// runT.display();
	}
}
