package com.yahoo.research.robme.anthelion.models.banditfunction;

import java.util.Properties;

import com.yahoo.research.robme.anthelion.models.AnthHost;
import com.yahoo.research.robme.anthelion.models.HostValueUpdateNecessity;

/**
 * Simple defines the score of a domain as the number of already semantic URLs
 * found.
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
public class AbsolutGoodFunction implements DomainValueFunction {

	@Override
	public void setProperty(Properties prop) {
		// not needed
	}

	@Override
	public double getDomainValue(AnthHost domain) {
		return domain.goodUrls;
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
