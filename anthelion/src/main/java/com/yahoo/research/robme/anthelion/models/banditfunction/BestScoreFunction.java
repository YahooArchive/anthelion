package com.yahoo.research.robme.anthelion.models.banditfunction;

import java.util.Properties;

import com.yahoo.research.robme.anthelion.models.AnthHost;
import com.yahoo.research.robme.anthelion.models.AnthURL;
import com.yahoo.research.robme.anthelion.models.HostValueUpdateNecessity;

/**
 * Simple BestScoreFunction which sets the value of a domain equal to the *best*
 * prediction of an readyUrl from the domain.
 * 
 * 
 * For more information see Meusel et al. 2014 @ CIKM'14
 * (http://dws.informatik.uni
 * -mannheim.de/fileadmin/lehrstuehle/ki/pub/Meusel-etal
 * -FocusedCrawlingForStructuredData-CIKM14.pdf)
 * 
 * @author Robert Meusel (robert@dwslab.de)
 * 
 */
public class BestScoreFunction implements DomainValueFunction {

	@Override
	public void setProperty(Properties prop) {
		// not necessary here.
	}

	@Override
	public double getDomainValue(AnthHost host) {
		double out = Double.MIN_VALUE;
		AnthURL url = null;
		if ((url = host.peekNextURL()) != null) {
			try {
				out = url.prediction;
			} catch (Exception e) {
				// just do nothing.
			}
		}
		return out;
	}

	@Override
	public boolean getNecessities(HostValueUpdateNecessity nec) {
		if (nec == HostValueUpdateNecessity.ON_STAT_CHANGE) {
			return false;
		}
		if (nec == HostValueUpdateNecessity.ON_QUEUE_CHANGE) {
			return true;
		}
		return false;
	}

}
