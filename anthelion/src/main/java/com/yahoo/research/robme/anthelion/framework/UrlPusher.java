package com.yahoo.research.robme.anthelion.framework;

import java.util.Date;

import com.yahoo.research.robme.anthelion.models.AnthHost;
import com.yahoo.research.robme.anthelion.models.AnthURL;

/**
 * Class which simply pushs URLs from the sorted list of domains (belonging to the
 * {@link AnthProcessor}) to the real crawler. URLs are removed from the  corresponding domain/host. 
 * {@link UrlPusher} will only push one URL at the time.
 * 
 * @author Robert Meusel (robert@dwslab.de)
 *
 */
public class UrlPusher implements Runnable {

	private AnthProcessor processor;
	private boolean run;
	protected long pushedUrl;
	protected long processingTime;
	protected long good;
	protected long bad;
	protected long predictedRight;
	private boolean classifyOnPush;

	public UrlPusher(AnthProcessor p, boolean classifyOnPush) {
		this.processor = p;
		this.classifyOnPush = classifyOnPush;

	}

	public void switchOf() {
		run = false;
	}

	@Override
	public void run() {
		run = true;
		while (run) {
			if (processor.queuedDomains.peek() != null) {
				long timeStart = new Date().getTime();
				AnthHost nextHost = processor.queuedDomains.poll();
				if (classifyOnPush) {
					nextHost.classify(processor.onlineLearner);
				}
				AnthURL aurl = nextHost.getNextUrlToCrawl();
				if (aurl != null) {
					if (aurl.sem) {
						good++;
						if (aurl.prediction >= 0) {
							predictedRight++;
						}
					} else {
						bad++;
						if (aurl.prediction <= 0) {
							predictedRight++;
						}
					}
					processor.outputList.add(aurl);
					nextHost.dequeue();
					pushedUrl++;
					processingTime += new Date().getTime() - timeStart;
				}
			} else {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
