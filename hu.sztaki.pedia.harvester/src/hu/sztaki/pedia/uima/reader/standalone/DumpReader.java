package hu.sztaki.pedia.uima.reader.standalone;

import hu.sztaki.pedia.uima.reader.util.SaxWikiHandler;
import hu.sztaki.pedia.uima.reader.util.WikiArticleFilter;

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
		if (args.length < 7) {
			System.out
					.println("Too few arguments! \n "
							+ "Usage: java DumpReader <inputFilename> <destinationHostname> <destinationPort> <applicationName> <language> <redirectsFilePath> <nonArticleTitlesFilePath> ");
			System.exit(0);
		}
		final String inputFileName = args[0];
		final String destinationHostname = args[1];
		final Integer destinationPort = new Integer(args[2]);
		final String applicationName = args[3];
		final String language = args[4];
		final String redirectsPath = args[5];
		final String nonArticleTitlesPath = args[6];

		final File inputFile = new File(inputFileName.trim());

		Thread saxParserThread = new Thread(new Runnable() {
			@Override
			public void run() {
				FileInputStream fis;
				InputStreamReader isr;
				File redirectsFile = new File(redirectsPath);
				File nonArticleTitlesFile = new File(nonArticleTitlesPath);
				WikiArticleFilter articleFilter;
				if (redirectsFile.exists() && nonArticleTitlesFile.exists()) {
					try {
						fis = new FileInputStream(inputFile);
						isr = new InputStreamReader(fis, "UTF-8");
						try {
							articleFilter = new WikiArticleFilter(nonArticleTitlesFile,
									redirectsFile);
						} catch (IOException e) {
							articleFilter = null;
							logger.error("Error creating WikiArticleFilter", e);
						}
						XMLReader xmlReader = XMLReaderFactory.createXMLReader();
						// Construct a SaxWikiHandler with HTTP output mode
						SaxWikiHandler saxWikiHandler = new SaxWikiHandler(destinationHostname,
								destinationPort, applicationName, language, articleFilter);
						xmlReader.setContentHandler(saxWikiHandler);
						xmlReader.setErrorHandler(saxWikiHandler);
						// start the parsing
						xmlReader.parse(new InputSource(isr));

					} catch (FileNotFoundException e) {
						logger.error("Input file:" + inputFileName + " not found!", e);
					} catch (UnsupportedEncodingException e) {
						logger.error("Unsupported encoding!", e);
					} catch (SAXException e) {
						logger.error("SAX exception:", e);
					} catch (IOException e) {
						logger.error("IO exception ", e);
					}
				} else {
					logger.error("Files " + redirectsPath + " , and/or " + nonArticleTitlesPath
							+ " does not exist! Exiting!");
				}

			}
		});
		logger.info("Starting reading articles from XML");
		saxParserThread.start();
	}
}
