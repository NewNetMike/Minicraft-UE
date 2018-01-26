package studio.bytesize.helper;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class MyXML
{
	public static String dataXML;
	
	public static Document xmlDoc;
	
	public static Document getDocument(String docString)
	{
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setIgnoringComments(true);
		factory.setIgnoringElementContentWhitespace(true);
		factory.setValidating(true);
		
		DocumentBuilder builder = null;
		try
		{
			builder = factory.newDocumentBuilder();
		}
		catch (ParserConfigurationException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		try
		{
			return builder.parse(new InputSource(docString));
		}
		catch (SAXException | IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

		return null;
	}
	
	public static void downloadXML()
	{
		URL website = null;
		try
		{
			website = new URL("http://itsyourpalmike.github.io/Minicraft-Ultimate-Edition/data.xml");
		}
		catch (MalformedURLException e2)
		{
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		try (InputStream in = website.openStream()) {
		    try
			{
				Files.copy(in, Paths.get(dataXML), StandardCopyOption.REPLACE_EXISTING);
			}
			catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		catch (IOException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	public static void initXML()
	{
		dataXML = MyOS.getAppdataLocation() + "/.minicraft/data.xml";
		
		downloadXML();
		
		xmlDoc = MyXML.getDocument(MyXML.dataXML);

		File file = new File(dataXML);
		file.delete();
	}
	
	public static String getReleaseVersion()
	{
		NodeList release = xmlDoc.getElementsByTagName("release");
		return release.item(0).getTextContent();
	}
	
	public static NodeList getPluginNodes()
	{
		return xmlDoc.getElementsByTagName("plugin");
	}
}
