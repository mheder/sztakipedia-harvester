package hu.sztaki.pedia.uima.reader.standalone;

import hu.sztaki.pedia.uima.reader.util.SztakipediaBot;

import java.net.MalformedURLException;

import org.apache.log4j.Logger;

public class IRCReader {

	public static Logger logger = Logger.getLogger(IRCReader.class);

	public static void main(String[] args) {
		if (args.length < 5) {
			System.out
					.println("Too few arguments! \n "
							+ "Usage: java hu.sztaki.pedia.uima.reader.standalone.IRCReader <en.wikipedia> <destinationHostname> <destinationPort> <applicationName> <language> [<API user>] [<API password>] ");
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
		if (args.length == 7) {
			apiUser = args[5];
			apiPassword = args[6];
		} else {
			apiUser = null;
			apiPassword = null;
		}
		Thread botThread = new Thread(new Runnable() {
			@Override
			public void run() {
				SztakipediaBot sztakipediaBot;
				try {
					// Construct a SztakipdeiaBot with HTTP output mode
					sztakipediaBot = new SztakipediaBot(ircChannel, domainUrl, destinationHostname,
							destinationPort, null, applicationName, language, apiUser, apiPassword);
					sztakipediaBot.start();
				} catch (MalformedURLException e) {
					e.printStackTrace();
					System.exit(-1);
				}

			}
		});

		botThread.start();
	}
}
