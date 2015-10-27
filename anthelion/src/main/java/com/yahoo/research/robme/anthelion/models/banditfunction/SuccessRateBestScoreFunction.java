package com.yahoo.research.robme.anthelion.models.banditfunction;

import java.util.Properties;

import com.yahoo.research.robme.anthelion.models.AnthHost;
import com.yahoo.research.robme.anthelion.models.AnthURL;
import com.yahoo.research.robme.anthelion.models.HostValueUpdateNecessity;

/**
 * Bandit Function using number of correct semantic classified URLs per Host
 * (Based on selected Classifier).
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
public class SuccessRateBestScoreFunction implements DomainValueFunction {

	int alpha = 1;
	int beta = 1;

	@Override
	public double getDomainValue(AnthHost domain) {

		// initially its the minimal double value
		// as this can only happen when something is wrong with the host we want
		// to ignore it
		double out = Double.MIN_VALUE;
		AnthURL url = null;
		if ((url = domain.peekNextURL()) != null) {
			try {
				out = url.prediction;
			} catch (Exception e) {
				// just do nothing.
			}
		}
		return out
				* (((double) domain.goodUrls + alpha) / ((double) domain.badUrls + beta));

	}

	@Override
	public void setProperty(Properties prop) {
		// can be ignored
	}

	@Override
	public boolean getNecessities(HostValueUpdateNecessity nec) {
		if (nec == HostValueUpdateNecessity.ON_STAT_CHANGE) {
			return true;
		}
		if (nec == HostValueUpdateNecessity.ON_QUEUE_CHANGE) {
			return true;
		}
		return false;
	}
}
