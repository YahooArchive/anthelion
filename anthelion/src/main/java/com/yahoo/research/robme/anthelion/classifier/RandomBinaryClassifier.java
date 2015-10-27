package com.yahoo.research.robme.anthelion.classifier;

import java.util.Random;

import moa.classifiers.AbstractClassifier;
import moa.core.Measurement;
import moa.core.StringUtils;
import weka.core.Instance;

/**
 * Classifier which pseudo randomly assigns true/false (0/1) to unlabeled
 * instances.
 * 
 * @author Robert Meusel (robert@dwslab.de)
 * 
 */
public class RandomBinaryClassifier extends AbstractClassifier {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Random rnd = new Random();

	@Override
	public boolean isRandomizable() {
		return false;
	}

	@Override
	public double[] getVotesForInstance(Instance inst) {
		double[] re = new double[2];
		if (rnd.nextBoolean()) {
			re[0] = 0;
			re[1] = 1;
		} else {
			re[0] = 1;
			re[1] = 0;
		}
		return re;
	}

	@Override
	public void resetLearningImpl() {
		rnd = new Random();
	}

	@Override
	public void trainOnInstanceImpl(Instance inst) {
		// Rnd is Rnd
	}

	@Override
	protected Measurement[] getModelMeasurementsImpl() {
		return null;
	}

	@Override
	public void getModelDescription(StringBuilder out, int indent) {
		StringUtils.appendNewlineIndented(out, indent,
				"BinaryRandomClassifiere is random and returns [1,0] or [0,1]");
		StringUtils.appendNewline(out);
	}
}
