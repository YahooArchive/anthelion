package com.yahoo.research.robme.anthelion.simulation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.Properties;
import java.util.Queue;

import com.yahoo.research.robme.anthelion.framework.AnthProcessor;
import com.yahoo.research.robme.anthelion.framework.FeedbackPusher;
import com.yahoo.research.robme.anthelion.models.AnthURL;

import de.uni_mannheim.informatik.dws.dwslib.util.InputUtil;

/**
 * Quick and dirty version of the first crawl simulator. Please use
 * {@link CCFakeCrawler} instead.
 * 
 * @author Robert Meusel (robert@dwslab.de)
 * 
 */
public class FakeCrawler {

	/**
	 * This is a quick and dirty runner for the {@link FakeCrawler}. In the
	 * beginning after starting this method {@link FakeCrawler#main(String[])}
	 * the crawler will l a number of items from the file (args[0]) and
	 * instanciate the Anthelion Module (which means the {@link AnthProcessor}).
	 * The user has the following options to type during the process runs.
	 * <ul>
	 * <li>'s' for getting the status of the running threads</li>
	 * <li>'r' to run the module without returning feedback (from
	 * "crawled pages") directly</li>
	 * <li>'rf' to run the module with direct feedback</li>
	 * <li>'f' to push currently "crawled" pages and give feedback to the
	 * module.</li>
	 * <li>'m [NUMBER]' to fill up the item queue with [NUMBER] items from the
	 * file.</li>
	 * <li>'e' to stop all threads</li>
	 * <li>'exit' to exit the program)</li>
	 * </ul>
	 * 
	 * @param args
	 *            1= the file to read items from, empty if default should be
	 *            used.
	 * @throws NumberFormatException
	 *             If format of file does not match expectations
	 * @throws IOException
	 *             If file could not be read.
	 */
	public static void main(String[] args) throws NumberFormatException,
			IOException {
		// input queue
		Queue<AnthURL> newUrls = new LinkedList<AnthURL>();
		String fileName = "/home/rmeusel/Documents/Anthelion/UrlClassification/v3allfeatures-00000";
		if (args != null && args.length > 0) {
			fileName = args[0];
		}
		BufferedReader br = InputUtil.getBufferedReader(new File(fileName),
				"UTF-8");
		// readFileToQueue(br, newUrls, 10000);
		// output list which the crawler processes
		Queue<AnthURL> readyUrls = new LinkedList<AnthURL>();
		// processor

		InputStream in = new FileInputStream(args[0]);
		Properties configProp = new Properties();
		try {
			configProp.load(in);
			System.out.println("Properties loaded.");
		} catch (IOException e) {
			System.out.println("Could not load properties.");
			e.printStackTrace();
			System.exit(0);
		}
		AnthProcessor p = new AnthProcessor(newUrls, readyUrls,
				new Properties());

		// open up standard input
		BufferedReader br2 = new BufferedReader(
				new InputStreamReader(System.in));

		String line = null;
		FeedbackPusher pusher = new FeedbackPusher(p, readyUrls);
		Thread t = new Thread(pusher, "FakeCrawler Feedback Pusher");

		while (true) {
			System.out
					.println("What to do? (s = status, m [number] = read [number] lines more, r = run, rf = run with direct feedback, f = feedback, e = stop, exit = exit): ");
			try {
				line = br2.readLine();
				if (line != null && !line.equals("")) {
					if (line.equals("r")) {
						p.start();
					} else if (line.equals("rf")) {
						p.start();
						t.start();
					} else if (line.equals("s")) {
						p.getStatus();
						System.out
								.println("FakeCrawlerThread: " + t.getState());
					} else if (line.equals("e")) {
						p.stop();
						pusher.switchOf();
					} else if (line.equals("f")) {
						while (!readyUrls.isEmpty()) {
							AnthURL url = readyUrls.poll();
							p.addFeedback(url.uri, url.sem);
						}
					} else if (line.matches("m \\d+")) {
						try {
							Integer num = Integer.parseInt(line.replaceFirst(
									"m (\\d+)", "$1"));
							readFileToQueue(br, newUrls, num);
						} catch (Exception e) {
							System.out
									.println("Could not get number of items to add. Please try again.");
						}

					} else if (line.equals("exit")) {
						break;
					}
				} else {
					System.out.println("Line was null or empty.");
				}
			} catch (IOException ioe) {
				System.out.println("IO error trying to read your command!");
			}

		}
		System.out.println("Done.");
	}

	/**
	 * Refills items from a given {@link BufferedReader} into the given
	 * {@link Queue}.
	 * 
	 * @param br
	 *            the {@link BufferedReader}
	 * @param urls
	 *            the {@link Queue} of {@link AnthURL}s
	 * @param max
	 *            the maximal of new items added. If {@link BufferedReader} is
	 *            empty beforehand maximal number cannot be reached.
	 * @throws IOException
	 *             if {@link BufferedReader} cannot get the next item/line.
	 * @throws NumberFormatException
	 *             , if input line does not fit the expected format.
	 */
	private static void readFileToQueue(BufferedReader br, Queue<AnthURL> urls,
			int max) throws IOException, NumberFormatException {

		int cnt = 0;
		while (br.ready() && cnt < max) {

			String line = br.readLine();
			String tok[] = line.split("\t");
			String url = "http://" + tok[2] + tok[3] + tok[4] + tok[5];
			try {
				AnthURL aurl = new AnthURL(new URI(url),
						(Integer.parseInt(tok[9]) > 0 ? true : false),
						(Integer.parseInt(tok[10]) > 0 ? true : false),
						(Integer.parseInt(tok[11]) > 0 ? true : false),
						(Integer.parseInt(tok[12]) > 0 ? true : false));
				aurl.sem = (tok[1].equals("sem") ? true : false);
				urls.add(aurl);
				cnt++;
			} catch (URISyntaxException s) {
				System.out.println("Could not create URI from " + url);
			}
		}
		System.out.println("Loaded " + cnt + " items.");
	}
}
