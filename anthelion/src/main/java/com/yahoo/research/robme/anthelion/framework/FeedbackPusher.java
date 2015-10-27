package com.yahoo.research.robme.anthelion.framework;

import java.util.Queue;

import com.yahoo.research.robme.anthelion.models.AnthURL;

/**
 * Class which based on the configuration pushes feedback to the online
 * classification method used.
 * 
 * @author Robert Meusel (robert@dwslab.de)
 * 
 */
public class FeedbackPusher implements Runnable {
	public AnthProcessor p;
	public Queue<AnthURL> readyUrls;
	protected boolean run;

	public FeedbackPusher(AnthProcessor p, Queue<AnthURL> readyUrls) {
		super();
		this.p = p;
		this.readyUrls = readyUrls;
	}

	public void switchOf() {
		run = false;
	}

	@Override
	public void run() {
		run = true;
		while (run) {
			AnthURL url = readyUrls.poll();
			if (url != null) {
				p.addFeedback(url.uri, url.sem);

			} else {
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

	}
}