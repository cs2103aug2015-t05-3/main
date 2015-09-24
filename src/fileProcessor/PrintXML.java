package fileProcessor;
/*

Shamelessly taken from: https://stackoverflow.com/questions/25864316/pretty-print-xml-in-java-8
Modified to suit our project needs

*/
import java.io.File;
import java.io.StringReader;

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
import org.xml.sax.InputSource;


public class PrintXML {
	public static void main(String[] args) throws Exception {
		String[][] elements = new String[][] {
				{ "Buy Milk from Supermarket", "22/09/2015", "00:00", "26/09/2015", "23:59", "1", "3" },
				{ "CS2103T Surprise Quiz", "1/10/2015", "14:00", "1/10/2015", "15:00", "0", "0" },
				{ "Delete the Task Program", "1/12/2015", "12:00", "1/12/2015", "12:05", "0", "2" },
				{ "Run Around the Campus 10 Times", "31/10/2015", "12:00", "31/10/2015", "20:05", "0", "6" } };

		String toPrint = "<tasklist>";
		
		for (int i = 0; i < elements.length; i++) {
			toPrint += "<task>";
			for (int j = 0; j < elements[i].length; j++) {
				switch (j) {
					case 0:
						toPrint += "<title>" + elements[i][j] + "</title>";
						break;
					case 1:
						toPrint += "<startDate>" + elements[i][j] + "</startDate>";
						break;
					case 2:
						toPrint += "<startTime>" + elements[i][j] + "</startTime>";
						break;
					case 3:
						toPrint += "<endDate>" + elements[i][j] + "</endDate>";
						break;
					case 4:
						toPrint += "<endTime>" + elements[i][j] + "</endTime>";
						break;
					case 5:
						toPrint += "<flag>" + elements[i][j] + "</flag>";
						break;
					case 6:
						toPrint += "<priority>" + elements[i][j] + "</priority>";
						break;
				}
			}
			toPrint += "</task>";
		}		
		toPrint += "</tasklist>";
		
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document document = documentBuilder.parse(new InputSource(new StringReader(toPrint)));
        pretty(document, new File ("xml.xml"), 4);
	}
	
    private static void pretty(Document document, File outputStream, int indent) throws Exception {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        if (indent > 0) {
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", Integer.toString(indent));
        }
        Result result = new StreamResult(outputStream);
        Source source = new DOMSource(document);
        transformer.transform(source, result);
    }
}
