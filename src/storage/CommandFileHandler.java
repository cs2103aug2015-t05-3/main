/**
 * Attempts to retrieve the list of commands that will be utilized.
 * @author Zander Chai
 */


package storage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
	
	private static final int BYTE_ARRAY_NUMBER = 1024;
	private static final int EOF_NUMBER = -1;
	private static final int OFFSET_NUMBER = 0;
	
	private static final String TAG_COMMAND = "command";
	private static final String TAG_CATEGORY = "category";
	private static final String TAG_WORD = "word";
	private static final String _commandResource = "resources/commands.xml";
	
	private Document _doc;
	private File _xmlFile;
	private HashMap<String, String> _cmdTable;
	
	public CommandFileHandler() {
		
	}
	
	/**
	 * Attempts to load XML file elements into Document object
	 * @param fileName
	 * @return true if successful, false otherwise
	 */
	public boolean loadCommandFile(String fileName) {
		assert fileName != null;
		assert !fileName.isEmpty();
		
		DocumentBuilderFactory dbFactory;
		DocumentBuilder dBuilder;
		
		_xmlFile = new File(fileName);
		dbFactory = DocumentBuilderFactory.newInstance();
		try {
			dBuilder = dbFactory.newDocumentBuilder();
			_doc = dBuilder.parse(_xmlFile);
			_doc.getDocumentElement().normalize();
			_cmdTable = new HashMap<>();
			parseCmd();
			return true;
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
	}
	
	/**
	 * Copies commands.xml from resource package to program directory
	 * @param newFileStr
	 * @return true is successful, false if failed
	 */
	public boolean generateCommandFile(String newFile) {	
		boolean check = fileCopyFromResource(newFile);		
		check = loadCommandFile(newFile) && check;
		parseCmd();
		return check;
	}
	
	public boolean fileCopyFromResource(String newFileStr) {
		File newFile = new File(newFileStr);
		newFile.delete();

		InputStream inputStream = this.getClass().getClassLoader()
				.getResourceAsStream(_commandResource);
		
		OutputStream outputStream = null;
			try {
				outputStream = new FileOutputStream(newFileStr);
				int read = 0;
				byte[] bytes = new byte[BYTE_ARRAY_NUMBER];
				while ((read = inputStream.read(bytes)) != EOF_NUMBER) {
					outputStream.write(bytes, OFFSET_NUMBER, read);
				}
				inputStream.close();
				outputStream.close();
				return true;
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				return false;
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
	}
	
	public HashMap<String, String> getCmdTable() {
		return _cmdTable;
	}

	/**
	 * Stores each custom command into a category as laid out
	 * in commands.xml.
	 * 
	 * The HashMap mappings are <word | category>
	 */
	private void parseCmd() {
		Element eElement;
		Node nNode;
		NodeList nList = _doc.getElementsByTagName(TAG_COMMAND);
		
		for (int i = 0; i < nList.getLength(); i++) {
			nNode = nList.item(i);
					
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				eElement = (Element) nNode;				
				String category = eElement.getAttribute(TAG_CATEGORY);
				
				for (int j = 0; j < eElement.getElementsByTagName(TAG_WORD).getLength(); j++) {
					String word = eElement.getElementsByTagName(TAG_WORD).item(j)
							.getTextContent();
					
					//if existing key(word) is already found in HashMap, ignore it. 					
					if (!_cmdTable.containsKey(word)) {
						_cmdTable.put(word, category);
					}
				}
			}
		}
	}
	
	/*
	public static void main(String args[]) {
		CommandFileHandler h = new CommandFileHandler();
		
		boolean isSuccess = h.loadCommandFile("commands.xml");
		
		while (!isSuccess) {
			isSuccess = h.generateCommandFile("commands.xml");
		}
		
		System.out.println("Done");
		HashMap<String, String> cmdTable = h.getCmdTable();
	}
	*/
}
