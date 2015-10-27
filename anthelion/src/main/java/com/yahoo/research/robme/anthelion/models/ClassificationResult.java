package com.yahoo.research.robme.anthelion.models;

/**
 * Representation of classification results.
 * 
 * @author Robert Meusel (robert@dwslab.de)
 * 
 */
public class ClassificationResult {
	public double accuracy;
	public int samplesNummer;
	public int correctClassified;
	public String label;

	public ClassificationResult() {
	}

	public ClassificationResult(String label) {
		this.label = label;
	}

	public ClassificationResult(double accuracy, int samplesNummer,
			int correctClassified, String label) {
		this.accuracy = accuracy;
		this.samplesNummer = samplesNummer;
		this.correctClassified = correctClassified;
		this.label = label;
	}
}