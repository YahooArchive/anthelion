package com.yahoo.research.robme.anthelion.models.banditfunction;

import java.util.Properties;

import org.apache.commons.math.MathException;
import org.apache.commons.math.distribution.BetaDistributionImpl;

import com.yahoo.research.robme.anthelion.models.AnthHost;
import com.yahoo.research.robme.anthelion.models.AnthURL;
import com.yahoo.research.robme.anthelion.models.HostValueUpdateNecessity;

/**
 * Bandit Function using Thompson Sampling combined with the Best Score Function
 * (Based on selected Classifier). Thompson Sampling uses a
 * {@link BetaDistributionImpl} using number of good URLs as ALPHA and number of
 * bad URLs as BETA.
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
public class ThompsonBestScoreFunction implements DomainValueFunction {

	private int alpha = 1;
	private int beta = 1;

	@Override
	public double getDomainValue(AnthHost domain) {
		// first we do the beta function and get the sample.
		// if something goes wrong we say its 0.
		BetaDistributionImpl bd = new BetaDistributionImpl(domain.goodUrls
				+ alpha, domain.badUrls + beta);
		double sample = 0.0;
		try {
			sample = bd.sample();
		} catch (MathException e) {
			System.out
					.println("Could not get random value from BetaDistribution for "
							+ domain.host);
		}
		// than we use the best score.
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
		return out * sample;

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
