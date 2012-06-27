package hu.sztaki.pedia.uima.reader.standalone;

import hu.sztaki.pedia.uima.reader.util.WikiArticleFilter;
import hu.sztaki.pedia.uima.reader.util.WikiIRCBot;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

import org.apache.log4j.Logger;

public class IRCReader {

	public static Logger logger = Logger.getLogger(IRCReader.class);

	public static void main(String[] args) {
		if (args.length < 7) {
			System.out
					.println("Too few arguments! \n "
							+ "Usage: java hu.sztaki.pedia.uima.reader.standalone.IRCReader <en.wikipedia> <destinationHostname> <destinationPort> <applicationName> <language> <redirectsFilePath> <nonArticleTitlesFilePath> [<API user>] [<API password>] ");
			System.exit(0);
		}
		String domain = args[0];
		final String destinationHostname = args[1];
		final Integer destinationPort = new Integer(args[2]);
		final String applicationName = args[3];
		final String language = args[4];

		final String domainUrl = domain + ".org";
		final String ircChannel = "#" + domain;
		final String apiUser;
		final String apiPassword;
		final String redirectsPath = args[5];
		final String nonArticleTitlesPath = args[6];

		if (args.length == 9) {
			apiUser = args[7];
			apiPassword = args[8];
		} else {
			apiUser = null;
			apiPassword = null;
		}
		Thread botThread = new Thread(new Runnable() {

			@Override
			public void run() {
				WikiIRCBot ircBot;
				try {

					File redirectsFile = new File(redirectsPath);
					File nonArticleTitlesFile = new File(nonArticleTitlesPath);
					WikiArticleFilter articleFilter;
					if (redirectsFile.exists() && nonArticleTitlesFile.exists()) {
						try {
							articleFilter = new WikiArticleFilter(nonArticleTitlesFile,
									redirectsFile);
						} catch (IOException e) {
							articleFilter = null;
							e.printStackTrace();
						}

						ircBot = new WikiIRCBot(ircChannel, domainUrl, destinationHostname,
								destinationPort, articleFilter, applicationName, language, apiUser,
								apiPassword);
						ircBot.start();
					} else {
						logger.error("Files " + redirectsPath + " , and/or " + nonArticleTitlesPath
								+ " does not exist! Exiting!");
					}
				} catch (MalformedURLException e) {
					e.printStackTrace();
					System.exit(-1);
				}

			}
		});

		botThread.start();
	}
}
