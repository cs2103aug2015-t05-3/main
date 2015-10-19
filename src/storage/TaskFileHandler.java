package storage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import taskCollections.Task;
import taskCollections.Task.FLAG_TYPE;
import taskCollections.Task.PRIORITY_TYPE;
import util.TimeUtil;

public class TaskFileHandler {

	private ArrayList<Task> _tasks;
	private Document _doc;
	private Element _root;
	private File _xmlFile;
	
	public TaskFileHandler(String fileName) {
		
		DocumentBuilderFactory dbFactory;
		DocumentBuilder dBuilder;
		
		_tasks = new ArrayList<>();
		_xmlFile = new File(fileName);

		if (!_xmlFile.exists()) { 
			PrintWriter writer;
			try {
				writer = new PrintWriter(fileName, "UTF-8");
				writer.println("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>");
				writer.println("<tasklist>");
				writer.println("</tasklist>");
				writer.close();
			} catch (FileNotFoundException e) {
				System.err.println("File Not Found");
			} catch (UnsupportedEncodingException e) {
				System.err.println("Unsupported Encoding");
			}			
		}
			
		dbFactory = DocumentBuilderFactory.newInstance();
		try {
			dBuilder = dbFactory.newDocumentBuilder();
			_doc = dBuilder.parse(_xmlFile);
		} catch (ParserConfigurationException e) {
			System.err.println("Parser Config Error.");
		} catch (SAXException e) {
			System.err.println("SAX Exception.");
		} catch (IOException e) {
			System.err.println("IO Error.");
		}
		
		_doc.getDocumentElement().normalize();
		_root = _doc.getDocumentElement();
		
		importAllTasks();
	}

	/**
	 * @return ArrayList<Task> : List of Tasks
	 */
	public ArrayList<Task> retrieveTaskList() {
		importAllTasks();
		return _tasks;
	}

	/**
	 * Adds new task to XML file
	 * @param t
	 */
	public void add(Task t) {
		int id = t.getId();
		String[] headers = { "title", "startTime", "endTime", "flag", "priority" };
		//Task t = new Task("Add add method", 1447252200000L, 1452868200000L, FLAG_TYPE.NULL, PRIORITY_TYPE.HIGH);
	
		Element newTask = _doc.createElement("task");
		newTask.setAttribute("id", "" + id);
	
		_root.appendChild(newTask);
	
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
	public void delete(int id) {
		Element e = locateID(id);
		_root.removeChild(e);
		genXML();
	}
	
	/**
	 * Update task to XML file
	 * @param t
	 * @throws Exception
	 */
	public void update (Task t) {
		Element e = locateID(t.getId());
		
		NodeList nl = e.getChildNodes();
		
		for (int i = 0; i < nl.getLength(); i++) {
			switch (i) {
				case (0):
					nl.item(i).setTextContent(t.getName());
					break;
				case (1):
					nl.item(i).setTextContent(TimeUtil.getFormattedDate(t.getStartTime()));
					break;
				case (2):
					nl.item(i).setTextContent(TimeUtil.getFormattedDate(t.getEndTime()));
					break;
				case (3):
					nl.item(i).setTextContent(String.valueOf(t.getFlag()));
					break;
				case (4):
					nl.item(i).setTextContent(String.valueOf(t.getPriority()));
					break;
			}
		}
		genXML();
	}
	private Element locateID(int id) {
		
		Element e;
		Node nNode;
		NodeList nList = _doc.getElementsByTagName("task");
		
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
		NodeList nList = _doc.getElementsByTagName("task");
		
		for (int i = 0; i < nList.getLength(); i++) {
			nNode = nList.item(i);
			
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				e = (Element) nNode;
				e.setAttribute("id", String.valueOf(i));
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
		NodeList nList = _doc.getElementsByTagName("task");
		Task t;
		
		for (int i = 0; i < nList.getLength(); i++) {
			nNode = nList.item(i);

			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				eElement = (Element) nNode;
				
				int id = Integer.parseInt(eElement.getAttribute("id"));
				
				String title = retrieveElement(eElement, "title");
				long startTime = TimeUtil.getLongTime(retrieveElement(eElement, "startTime"));
				long endTime = TimeUtil.getLongTime(retrieveElement(eElement, "endTime"));
				FLAG_TYPE flag = detFlag(retrieveElement(eElement, "flag"));
				PRIORITY_TYPE priority = detPriority(retrieveElement(eElement, "priority"));
				
				t = new Task(id, title, startTime, endTime, flag, priority);
				_tasks.add(t);
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

	/*
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
	*/

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

	private void printFile(Document document, int indent) {

		removeEmptyText(document.getDocumentElement());

		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Result result = new StreamResult(_xmlFile);
		Source source = new DOMSource(document);
		Transformer transformer;
		try {
			transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "" + indent);
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.transform(source, result);			
		} catch (TransformerConfigurationException e) {
			System.err.println("Transformer Configuration Exeception.");
		} catch (TransformerException e) {
			System.err.println("Transformer Exeception.");
		}

	}

	private void genXML() {
		printFile(_doc, 4);
	}

	private Element addElement(String s, Task t) {
		Element e = _doc.createElement(s);
		switch (s) {
		case "title":
			e.appendChild(_doc.createTextNode(t.getName()));
			break;
		case "startTime":
			e.appendChild(_doc.createTextNode(TimeUtil.getFormattedDate(t.getStartTime())));
			break;
		case "endTime":
			e.appendChild(_doc.createTextNode(TimeUtil.getFormattedDate(t.getEndTime())));
			break;
		case "flag":
			e.appendChild(_doc.createTextNode("" + t.getFlag()));
			break;
		case "priority":
			e.appendChild(_doc.createTextNode("" + t.getPriority()));
			break;
		}
		return e;
	}

	public static void main(String[] args) {
		TaskFileHandler runT = new TaskFileHandler("tasks.xml");
		//Task t = new Task("Delete the Task Program", 0L, 0L, FLAG_TYPE.NULL, PRIORITY_TYPE.VERY_HIGH);
		//runT.update(t);
		//runT.add();
		//runT.genXML();
		//runT.delete();
		//runT.genXML();
		//runT.display();
	}
}
