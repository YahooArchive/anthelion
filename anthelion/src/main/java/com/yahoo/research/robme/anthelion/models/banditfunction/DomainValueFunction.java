package com.yahoo.research.robme.anthelion.models.banditfunction;

import java.util.Properties;

import com.yahoo.research.robme.anthelion.models.AnthHost;
import com.yahoo.research.robme.anthelion.models.HostValueUpdateNecessity;

/**
 * Interface which should be implemented for any new DomainValueFunction which
 * serves the bandit as get the best domain at time t.
 * 
 * @author Robert Meusel (robert@dwslab.de)
 * 
 */
public interface DomainValueFunction {

	public void setProperty(Properties prop);

	double getDomainValue(AnthHost host);

	boolean getNecessities(HostValueUpdateNecessity nec);

}
