package com.yahoo.research.robme.anthelion.models.banditfunction;

import java.util.Properties;

import com.yahoo.research.robme.anthelion.models.AnthHost;
import com.yahoo.research.robme.anthelion.models.HostValueUpdateNecessity;

/**
 * Calculates the score of a domain as the ratio between already found semantic
 * and non-semantic URLs.
 * 
 * For more information see Meusel et al. 2014 @ CIKM'14
 * (http://dws.informatik.uni
 * -mannheim.de/fileadmin/lehrstuehle/ki/pub/Meusel-etal
 * -FocusedCrawlingForStructuredData-CIKM14.pdf)
 * 
 * @author Robert Meusel (robert@dwslab.de)
 * 
 */
public class DomainSuccessRateFunction implements DomainValueFunction {

	private int alpha = 1;
	private int beta = 1;

	@Override
	public double getDomainValue(AnthHost domain) {
		return ((double) domain.goodUrls + alpha)
				/ ((double) domain.badUrls + beta);
	}

	@Override
	public void setProperty(Properties prop) {
		// not necessary for this function
	}

	@Override
	public boolean getNecessities(HostValueUpdateNecessity nec) {
		if (nec == HostValueUpdateNecessity.ON_STAT_CHANGE) {
			return true;
		}
		if (nec == HostValueUpdateNecessity.ON_QUEUE_CHANGE) {
			return false;
		}
		return false;
	}

}
