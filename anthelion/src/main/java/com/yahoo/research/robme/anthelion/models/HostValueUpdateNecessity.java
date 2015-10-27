package com.yahoo.research.robme.anthelion.models;

/**
 * Indicates when the {@link AnthHost#score} needs to be updated using the given
 * {@link DomainValueFunction}
 * 
 * @author Robert Meusel (robert@dwslab.de)
 * 
 */
public enum HostValueUpdateNecessity {

	ON_QUEUE_CHANGE, ON_STAT_CHANGE;

}
