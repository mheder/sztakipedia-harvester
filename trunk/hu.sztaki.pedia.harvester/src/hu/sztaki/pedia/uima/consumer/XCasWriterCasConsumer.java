package hu.sztaki.pedia.uima.consumer;

/*
 *******************************************************************************************
 * N O T E :     The XML format (XCAS) that this Cas Consumer outputs, is eventually 
 *               being superceeded by the more standardized and compact XMI format.  However
 *               it is used currently as the expected form for remote services, and there is
 *               existing tooling for doing stand-alone component development and debugging 
 *               that uses this format to populate an initial CAS.  So it is not 
 *               deprecated yet;  it is also being kept for compatibility with older versions.
 *               
 *               New code should consider using the XmiWriterCasConsumer where possible,
 *               which uses the current XMI format for XML externalizations of the CAS
 *******************************************************************************************               
 */
import hu.sztaki.pedia.uima.engines.AbstractMultiSofaAnnotator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.uima.SourceDocumentInformation;
import org.apache.uima.UimaContext;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.CAS;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.cas.impl.XCASSerializer;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.resource.ResourceProcessException;
import org.apache.uima.util.XMLSerializer;
import org.xml.sax.SAXException;

/**
 * A simple CAS consumer that generates XCAS (XML representation of the CAS)
 * files in the filesystem.
 * <p>
 * This CAS Consumer takes one parameters:
 * <ul>
 * <li><code>OutputDirectory</code> - path to directory into which output files
 * will be written</li>
 * </ul>
 * 
 * CasConsumer_ImplBase
 */
public class XCasWriterCasConsumer extends AbstractMultiSofaAnnotator {
	/**
	 * Name of configuration parameter that must be set to the path of a
	 * directory into which the output files will be written.
	 */
	public static final String PARAM_OUTPUTDIR = "OutputDirectory";

	private String baseOutputDirName;
	private int mDocNum;

	public final int SUBLIMIT = 100000;
	public final int SUBSUBLIMIT = 10000;

	private int subDirCount, subsubDirCount;

	public void initialize(UimaContext aContext) throws ResourceInitializationException {
		super.initialize(aContext);
		mDocNum = 0;
		subDirCount = 0;
		subsubDirCount = 0;
		baseOutputDirName = (String) aContext.getConfigParameterValue(PARAM_OUTPUTDIR);
		File baseOutputDir = new File(baseOutputDirName);
		if (!baseOutputDir.exists()) {
			baseOutputDir.mkdirs();
		}
	}

	/**
	 * Processes the CasContainer which was populated by the
	 * TextAnalysisEngines. <br>
	 * In this case, the CAS is converted to XML and written into the output
	 * file .
	 * 
	 * @param aCAS
	 *            CasContainer which has been populated by the TAEs
	 * 
	 * @throws ResourceProcessException
	 *             if there is an error in processing the Resource
	 * 
	 * @see org.apache.uima.collection.base_cpm.CasObjectProcessor#processCas(org.apache.uima.cas.CAS)
	 */
	public void process(JCas aJCas) throws AnalysisEngineProcessException {
		// JCas jcas;
		// try {
		// jcas = aJCas.getJCas();
		// } catch (CASException e) {
		// throw new ResourceProcessException(e);
		// }

		ArrayList<JCas> casList = getJCasList(aJCas);
		SourceDocumentInformation sdocInfo = null;
		for (JCas jcas : casList) {
			// retreive the filename of the input file from the CAS
			FSIterator it = jcas.getAnnotationIndex(SourceDocumentInformation.type).iterator();
			File outFile = null;
			if (it.hasNext()) {
				SourceDocumentInformation fileLoc = (SourceDocumentInformation) it.next();
				String outFileName = fileLoc.getUri();
				// File inFile;
				// try {
				// inFile = new File(new URL(fileLoc.getUri()).getPath());
				// String outFileName = inFile.getName();
				// if (fileLoc.getOffsetInSource() > 0) {
				// outFileName += fileLoc.getOffsetInSource();
				// }
				outFile = new File(getOutputDir(outFileName), outFileName);
				// } catch (MalformedURLException e1) {
				// // invalid URL, use default processing below
				// }
			}
			if (outFile == null) {
				outFile = new File(getOutputDir(), "doc" + mDocNum);
			}
			// serialize XCAS and write to output file
			try {
				mDocNum++;
				writeXCas(jcas.getCas(), outFile);
			} catch (IOException e) {
				throw new AnalysisEngineProcessException(e);
			} catch (SAXException e) {
				throw new AnalysisEngineProcessException(e);
			}
		}
	}

	private File getOutputDir(String outFileName) {
		int suffixPos = outFileName.indexOf(".xml");
		long id = Long.parseLong(outFileName.substring(0, suffixPos));
		long subDir = id / SUBLIMIT;
		long subSubDir = (id / SUBSUBLIMIT) % 10;

		File outdir = new File(baseOutputDirName + File.separator + subDir + File.separator
				+ subSubDir);
		if (!outdir.exists()) {
			outdir.mkdirs();
		}
		return outdir;
	}

	private File getOutputDir() {
		if (mDocNum % SUBLIMIT == 0) {
			subDirCount++;
			subsubDirCount = 0;
		}
		if (mDocNum % SUBSUBLIMIT == 0) {
			subsubDirCount++;
		}
		File outdir = new File(baseOutputDirName + File.separator + subDirCount + File.separator
				+ subsubDirCount);
		if (!outdir.exists()) {
			outdir.mkdirs();
		}
		return outdir;
	}

	/**
	 * Serialize a CAS to a file in XCAS format
	 * 
	 * @param aCas
	 *            CAS to serialize
	 * @param name
	 *            output file
	 * 
	 * @throws IOException
	 *             if an I/O failure occurs
	 * @throws SAXException
	 *             if an error occurs generating the XML text
	 */
	private void writeXCas(CAS aCas, File name) throws IOException, SAXException {
		FileOutputStream out = null;

		try {
			out = new FileOutputStream(name);
			XCASSerializer ser = new XCASSerializer(aCas.getTypeSystem());
			XMLSerializer xmlSer = new XMLSerializer(out, true);
			ser.serialize(aCas, xmlSer.getContentHandler());
		} finally {
			if (out != null) {
				out.close();
			}
		}
	}

}
