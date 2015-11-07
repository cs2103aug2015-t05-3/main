package storage;

import java.io.File;
import java.io.IOException;
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

	private final int XML_INDENTAMT = 4;
	private final String EMPTY_TEXT = "";
	private final String PRIORITY_HIGH = "HIGH";
	private final String PRIORITY_NORMAL = "NORMAL";
	private final String PRIORITY_LOW = "LOW";
	private final String MARKED_TASK = "DONE";
	private final String UNMARKED_TASK = "NULL";	
	private final String TAG_TASK = "task";
	private final String TAG_ID = "id";
	private final String TAG_TITLE = "title";
	private final String TAG_STARTTIME = "startTime";
	private final String TAG_ENDTIME = "endTime";
	private final String TAG_FLAG = "flag";
	private final String TAG_PRIORITY = "priority";
	
	private ArrayList<Task> _tasks;
	private Document _doc;
	private Element _root;
	private File _xmlFile;
	
	public TaskFileHandler() {}
	
	/**
	 * Attempts to load XML File into ArrayList of Task Objects
	 * @return true if loading succeeded, false if failed.
	 */
	public boolean loadTaskFile(String fileName) {
		DocumentBuilderFactory dbFactory;
		DocumentBuilder dBuilder;
		
		_xmlFile = new File(fileName);
			
		dbFactory = DocumentBuilderFactory.newInstance();
			try {
				dBuilder = dbFactory.newDocumentBuilder();
				_doc = dBuilder.parse(_xmlFile);
			} catch (ParserConfigurationException e) {
				System.err.println("Parser Config Error.");
				return false;
			} catch (SAXException e) {
				System.err.println("SAX Exception.");
				return false;
			} catch (IOException e) {
				System.err.println("IO Error.");
				return false;
			}
		
		_doc.getDocumentElement().normalize();
		_root = _doc.getDocumentElement();
		
		return importAllTasks();
	}

	/**
	 * @return ArrayList<Task> : List of Tasks
	 */
	public ArrayList<Task> retrieveTaskList() {
		return _tasks;
	}

	/**
	 * Adds new task to XML file
	 * @param t
	 * @returns true if operation succeeded, false if failed.
	 */
	public boolean add(Task t) {
		int id = t.getId();
		String[] headers = { TAG_TITLE, TAG_STARTTIME, TAG_ENDTIME, TAG_FLAG, TAG_PRIORITY };
	
		Element newTask = _doc.createElement(TAG_TASK);
		newTask.setAttribute(TAG_ID, String.valueOf(id));
	
		_root.appendChild(newTask);
	
		for (int i = 0; i < headers.length; i++) {
			Element e = addElement(headers[i], t);
			newTask.appendChild(e);
		}
		return genXML();
	}
	
	/**
	 * Delete task from XML file
	 * @param id
	 * @returns true if operation succeeded, false if failed.
	 */
	public boolean delete(int id) {
		Element e = locateID(id);
		_root.removeChild(e);
		return genXML();
	}
	
	/**
	 * Update task to XML file
	 * @param t
	 * @returns true if operation succeeded, false if failed.
	 */
	public boolean update(Task t) {
		Element e = locateID(t.getId());
		
		NodeList nl = e.getChildNodes();
		
		for (int i = 0; i < nl.getLength(); i++) {
			switch (i) {
				case 0:
					String taskName = t.getName();
					nl.item(i).setTextContent(taskName);
					break;
				case 1:
					String startTime = TimeUtil.getFormattedDate(t.getStartTime());
					nl.item(i).setTextContent(startTime);
					break;
				case 2:
					String endTime = TimeUtil.getFormattedDate(t.getEndTime());
					nl.item(i).setTextContent(endTime);
					break;
				case 3:
					String markStatus = t.getFlag().toString();
					nl.item(i).setTextContent(markStatus);
					break;
				case 4:
					String priorityString = t.getPriority().toString();
					nl.item(i).setTextContent(priorityString);
					break;
			}
		}
		return genXML();
	}
	
	/**
	 * Returns an element of specified tag to the new task
	 * @param s Type of Tag
	 * @param t Task object
	 * @return Element
	 */
	private Element addElement(String s, Task t) {
		Element e = _doc.createElement(s);
		switch (s) {
			case TAG_TITLE:
				String taskName = t.getName();
				e.appendChild(_doc.createTextNode(taskName));
				break;
			case TAG_STARTTIME:
				String startTime = TimeUtil.getFormattedDate(t.getStartTime());
				e.appendChild(_doc.createTextNode(startTime));
				break;
			case TAG_ENDTIME:
				String endTime = TimeUtil.getFormattedDate(t.getEndTime());
				e.appendChild(_doc.createTextNode(endTime));
				break;
			case TAG_FLAG:
				String markStatus = t.getFlag().toString();
				e.appendChild(_doc.createTextNode(markStatus));
				break;
			case TAG_PRIORITY:
				String priorityString = t.getPriority().toString();
				e.appendChild(_doc.createTextNode(priorityString));
				break;
		}
		return e;
	}

	private FLAG_TYPE detFlag(String s) {
		switch (s) {
			case UNMARKED_TASK:
				return FLAG_TYPE.NULL;
			case MARKED_TASK:
				return FLAG_TYPE.DONE;
			default:
				return null;
			}
	}

	private PRIORITY_TYPE detPriority(String s) {
		switch (s) {
			case PRIORITY_HIGH:
				return PRIORITY_TYPE.HIGH;
			case PRIORITY_NORMAL:
				return PRIORITY_TYPE.NORMAL;
			case PRIORITY_LOW:
				return PRIORITY_TYPE.LOW;
			default:
				return null;
			}
	}
	
	/**
	 * Writes data to XML file with the parameters.
	 * @return true if succeeded, false if failed
	 */
	private boolean genXML() {
		return printFile(_doc, XML_INDENTAMT);
	}

	/**
	 * Read data from XML file and stores them in arrayList
	 * @return true if operation succeeded, false if failed
	 */
	private boolean importAllTasks() {
		_tasks = new ArrayList<>();
		optimizeID();
		
		Element eElement;
		Node nNode;
		NodeList nList = _doc.getElementsByTagName(TAG_TASK);
		Task t;
		
		try {
			for (int i = 0; i < nList.getLength(); i++) {
				nNode = nList.item(i);
	
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					eElement = (Element) nNode;
				
					int id = Integer.parseInt(eElement.getAttribute(TAG_ID));
				
					String title = retrieveElement(eElement, TAG_TITLE);
					long startTime = TimeUtil.getLongTime(retrieveElement(eElement, TAG_STARTTIME));
					long endTime = TimeUtil.getLongTime(retrieveElement(eElement, TAG_ENDTIME));
					
					if (startTime == -1 || endTime == -1) {
						return false;
					}
					
					FLAG_TYPE flag = detFlag(retrieveElement(eElement, TAG_FLAG));
					PRIORITY_TYPE priority = detPriority(retrieveElement(eElement, TAG_PRIORITY));
				
					if (flag == null || priority == null) {
						return false;
					}
					
					t = new Task(id, title, EMPTY_TEXT, startTime, endTime, flag, priority);
					_tasks.add(t);
				}
			}
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	/**
	 * Locate and returns the element in the document list
	 * @param id the id number of task entry
	 * @return Element e the task element with the corresponding id
	 */
	private Element locateID(int id) {
		
		Element e;
		Node nNode;
		NodeList nList = _doc.getElementsByTagName(TAG_TASK);
		
		for (int i = 0; i < nList.getLength(); i++) {
			nNode = nList.item(i);
			
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				e = (Element)nNode;
				if (e.getAttribute(TAG_ID).equals(String.valueOf(id))) {
					return e;
				}
			}
		}
		return null;
	}
	
	/**
	 * Reformats the ID number in XML file to ascending order, starting from 0.
	 */
	private void optimizeID() {
		Element e;
		Node nNode;
		NodeList nList = _doc.getElementsByTagName(TAG_TASK);
		
		for (int i = 0; i < nList.getLength(); i++) {
			nNode = nList.item(i);
			
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				e = (Element) nNode;
				e.setAttribute(TAG_ID, String.valueOf(i));
			}
		}
		genXML();
	}

	/**
	 * Remove text nodes that are used for indentation.
	 * 
	 * @param Node A single node in XML file
	 */
	private void removeEmptyText(Node node) {
		Node child = node.getFirstChild();
		while (child != null) {
			Node sibling = child.getNextSibling();
			if (child.getNodeType() == Node.TEXT_NODE) {
				if (child.getTextContent().trim().isEmpty()) {
					node.removeChild(child);
				}
			} else {
				removeEmptyText(child);
			}
			child = sibling;
		}
	}

	private String retrieveElement(Element e, String s) {
		return e.getElementsByTagName(s).item(0).getTextContent();
	}

	private boolean printFile(Document document, int indent) {

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
			return true;
		} catch (TransformerConfigurationException e) {
			System.err.println("Transformer Configuration Exeception.");
			return false;
		} catch (TransformerException e) {
			System.err.println("Transformer Exeception.");
			return false;
		}

	}

	/*
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
	*/
}
