package com.yahoo.research.robme.anthelion.framework;

import com.yahoo.research.robme.anthelion.models.AnthHost;
import com.yahoo.research.robme.anthelion.models.AnthURL;
import com.yahoo.research.robme.anthelion.models.banditfunction.DomainValueFunction;

/**
 * Class which simply pulls URLs from a given List (belonging to the
 * {@link AnthProcessor}) and includes them into Anthelion modul. URLs coming
 * from outside are added to the corresponding domain/host. If the host does not
 * exist it is created on the fly. {@link UrlPuller} will only pull one URL at
 * the time.
 * 
 * @author Robert Meusel (robert@dwslab.de)
 * 
 */
public class UrlPuller implements Runnable {

	private AnthProcessor p;
	private boolean run;
	protected long pulledUrl;
	protected long goodPulledUrl;
	private int minInputListSize;
	private boolean classifyOnPull;
	private DomainValueFunction domainValueFunction;

	public UrlPuller(AnthProcessor p, int minInputListSize,
			boolean classifyOnPull, DomainValueFunction domainValueFunction) {
		this.minInputListSize = minInputListSize;
		this.classifyOnPull = classifyOnPull;
		this.p = p;
		this.domainValueFunction = domainValueFunction;
	}

	public void switchOf() {
		run = false;
	}

	@Override
	public void run() {
		run = true;
		while (run) {
			if (p.inputList.size() > minInputListSize) {
				AnthURL aurl = p.inputList.poll();
				if (aurl != null) {
					if (classifyOnPull) {
						p.onlineLearner.classifyUrl(aurl);
					}
					if (!p.knownDomains.containsKey(aurl.getHost())) {
						p.knownDomains.put(aurl.getHost(),
								new AnthHost(aurl.getHost(),
										domainValueFunction));
					}
					try {
						p.knownDomains.get(aurl.getHost()).enqueue(aurl);
						if (aurl.sem) {
							goodPulledUrl++;
						}
						pulledUrl++;
					} catch (NullPointerException npe) {
						// clap your hands.
					}
				}
			} else {
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

	}
}
