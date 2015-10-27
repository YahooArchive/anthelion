package com.yahoo.research.robme.anthelion.models;

import com.yahoo.research.robme.anthelion.classifier.RandomBinaryClassifier;

import moa.classifiers.Classifier;
import moa.classifiers.bayes.NaiveBayes;
import moa.classifiers.trees.AdaHoeffdingOptionTree;
import moa.classifiers.trees.DecisionStump;
import moa.classifiers.trees.HoeffdingAdaptiveTree;
import moa.classifiers.trees.HoeffdingTree;

/**
 * Enumeration for a selected number of classifier included in the
 * moa.classifiers package.
 * 
 * @author Robert Meusel (robert@dwslab.de)
 * 
 */
public class ClassifierEnum {

	/**
	 * Return a new classifier for the given a string
	 * 
	 * @param cn
	 *            the name of the classifier
	 * @return the {@link Classifier}
	 */
	public static Classifier getClassifier(String cn) {
		Classifier classifier = null;
		switch (cn) {
		case "NaiveBayes":
			classifier = new NaiveBayes();
			break;
		case "DecisionStump":
			classifier = new DecisionStump();
			break;
		case "HoeffdingTree":
			classifier = new HoeffdingTree();
			break;
		// we removed this one because of license issues
		// case "HoeffdingTreeNG":
		// classifier = new HoeffdingTreeNG();
		// break;
		case "HoeffdingAdaptiveTree":
			classifier = new HoeffdingAdaptiveTree();
			break;
		case "AdaHoeffdingTree":
			classifier = new AdaHoeffdingOptionTree();
			break;
		case "RandomBinary":
			classifier = new RandomBinaryClassifier();
		}

		return classifier;
	}
}
