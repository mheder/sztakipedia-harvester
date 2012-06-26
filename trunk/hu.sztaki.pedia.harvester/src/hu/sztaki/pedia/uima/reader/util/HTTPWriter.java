package hu.sztaki.pedia.uima.reader.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class HTTPWriter {
	private String destinationUrl;
	private URL url;

	public HTTPWriter(String destinationHostname, Integer destinationPort)
			throws MalformedURLException {
		this.destinationUrl = "http://" + destinationHostname + ":" + destinationPort;
		url = new URL(destinationUrl);
	}

	public String writeArticle(WikiArticle article) {
		StringBuffer responseBuffer = new StringBuffer();
		OutputStreamWriter wr = null;
		BufferedReader rd = null;
		try {
			// Construct data
			String data = URLEncoder.encode("title", "UTF-8") + "="
					+ URLEncoder.encode(article.getTitle(), "UTF-8");
			data += "&" + URLEncoder.encode("articleid", "UTF-8") + "="
					+ URLEncoder.encode(article.getId().toString(), "UTF-8");
			data += "&" + URLEncoder.encode("revision", "UTF-8") + "="
					+ URLEncoder.encode(Long.toString(article.getRevision()), "UTF-8");
			data += "&" + URLEncoder.encode("applicationName", "UTF-8") + "="
					+ URLEncoder.encode(article.getApplication(), "UTF-8");
			data += "&" + URLEncoder.encode("language", "UTF-8") + "="
					+ URLEncoder.encode(article.getLanguage(), "UTF-8");
			data += "&" + URLEncoder.encode("text", "UTF-8") + "="
					+ URLEncoder.encode(article.getText(), "UTF-8");

			// Send data
			// URL url = new URL(destinationUrl);
			URLConnection conn = url.openConnection();
			conn.setDoOutput(true);
			conn.connect();
			wr = new OutputStreamWriter(conn.getOutputStream());
			wr.write(data);
			wr.flush();

			// Get the response
			rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = rd.readLine()) != null) {
				responseBuffer.append(line);
			}
			System.out.println(responseBuffer.toString());

			//
			// // Create a socket to the host
			// String hostname = "hostname.com";
			// int port = 80;
			// InetAddress addr = InetAddress.getByName(hostname);
			// Socket socket = new Socket(addr, port);
			//
			// // Send header
			// String path = "/servlet/SomeServlet";
			// BufferedWriter wr = new BufferedWriter(new
			// OutputStreamWriter(socket.getOutputStream(),
			// "UTF8"));
			// wr.write("POST " + path + " HTTP/1.0\r\n");
			// wr.write("Content-Length: " + data.length() + "\r\n");
			// wr.write("Content-Type: application/x-www-form-urlencoded\r\n");
			// wr.write("\r\n");
			//
			// // Send data
			// wr.write(data);
			// wr.flush();
			//
			// // Get response
			// BufferedReader rd = new BufferedReader(new
			// InputStreamReader(socket.getInputStream()));
			// String line;
			// while ((line = rd.readLine()) != null) {
			// // Process line...
			// }
			// wr.close();
			// rd.close();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (wr != null) {
					wr.close();
				}
				if (rd != null) {
					rd.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return responseBuffer.toString();
	}

}
