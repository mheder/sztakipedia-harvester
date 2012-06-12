package hu.sztaki.pedia.uima.reader.standalone;

import hu.sztaki.pedia.uima.reader.util.SaxWikiHandler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.apache.log4j.Logger;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

public class DumpReader {
	public static Logger logger = Logger.getLogger(DumpReader.class);

	public static void main(String[] args) {
		if (args.length < 5) {
			System.out
					.println("Too few arguments! \n "
							+ "Usage: java DumpReader <inputFilename> <destinationHostname> <destinationPort> <applicationName> <language> ");
			System.exit(0);
		}
		String inputFileName = args[0];
		final String destinationHostname = args[1];
		final Integer destinationPort = new Integer(args[2]);
		final String applicationName = args[3];
		final String language = args[4];
		final File inputFile = new File(inputFileName.trim());

		Thread saxParserThread = new Thread(new Runnable() {
			@Override
			public void run() {
				FileInputStream fis;
				InputStreamReader isr;
				try {
					fis = new FileInputStream(inputFile);
					isr = new InputStreamReader(fis, "UTF-8");
					XMLReader xmlReader = XMLReaderFactory.createXMLReader();
					SaxWikiHandler saxWikiHandler = new SaxWikiHandler(destinationHostname,
							destinationPort, applicationName, language);
					xmlReader.setContentHandler(saxWikiHandler);
					xmlReader.setErrorHandler(saxWikiHandler);
					// start the parsing
					xmlReader.parse(new InputSource(isr));

				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				} catch (SAXException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});
		logger.info("Starting reading articles from XML");
		saxParserThread.start();
	}
}
