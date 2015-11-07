/**
 * Attempts to retrieve the list of commands that will be utilized.
 * @author Zander Chai
 */

//Source: http://www.mkyong.com/java/how-to-read-xml-file-in-java-dom-parser/

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
	
	private final String TAG_COMMAND = "command";
	private final String TAG_CATEGORY = "category";
	private final String TAG_WORD = "word";
	private final String _commandResource = "resources/commands.xml";
	
	private Document _doc;
	private File _xmlFile;
	private HashMap<String, String> _cmdTable;
	
	public CommandFileHandler() {
		
	}
	
	public boolean loadCommandFile(String fileName) {
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
	
	public boolean generateCommandFile(String newFile) {
		newFile = "commands.xml";
		
		boolean check = fileCopyFromResource(newFile);		
		check = loadCommandFile(newFile) && check;
		parseCmd();
		return check;
	}
	
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
					String word = eElement.getElementsByTagName(TAG_WORD).item(j).getTextContent();
					_cmdTable.put(word, category);
				}
			}
		}
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
				byte[] bytes = new byte[1024];
				while ((read = inputStream.read(bytes)) != -1) {
					outputStream.write(bytes, 0, read);
				}
				inputStream.close();
				outputStream.close();
				return true;
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
	}
	
	public HashMap<String, String> getCmdTable() {
		return _cmdTable;
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
