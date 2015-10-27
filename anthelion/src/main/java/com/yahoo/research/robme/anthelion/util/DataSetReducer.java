package com.yahoo.research.robme.anthelion.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import moa.core.InstancesHeader;
import moa.streams.ArffFileStream;
import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.SparseInstance;
import weka.core.converters.ArffSaver;

/**
 * Util class to create smaller test set with a fixed set of domains and an
 * fixed number of differen labels.
 * 
 * @author Robert Meusel (robert@dwslab.de)
 * 
 */
public class DataSetReducer {

	public static void main(String[] args) throws NumberFormatException,
			IOException {
		if (args == null || args.length < 6) {
			System.out.println("USAGE: DataSetReducer <FILE> <CLASSINDEX> "
					+ "<REDUCTIONATTRIBUTE> <MAXNUMOFINSTANCESBYATTRIBUTE> "
					+ "<MAXNUMEROFDIFATTRIBUTES> <OUTPUTFILE>");
		} else {
			reduce(args[0], Integer.parseInt(args[1]), args[2],
					Integer.parseInt(args[3]), Integer.parseInt(args[4]),
					args[5]);
		}
	}

	// reduce the dataset based a a attribute and a maximal number of record for
	// each characteristic of this attribute. has to be a nominal attribute.
	private static void reduce(String file, int classindex,
			String reductionAttribute, int maxNumber, int maxDomainNum,
			String outputFile) throws IOException {

		System.out.println("Config is:");
		System.out.println("Reduction Attribute: " + reductionAttribute);
		System.out.println("Max Number of different characteristics: "
				+ maxDomainNum);
		System.out.println("Max Number of instances per characteristics: "
				+ maxNumber);
		System.out.println("Starting reduction ...");
		// create the stream to read the data
		ArffFileStream stream = new ArffFileStream(file, classindex);

		InstancesHeader header = stream.getHeader();
		int reductionAttributeId = header.attribute(reductionAttribute).index();

		HashMap<String, Integer> counter = new HashMap<String, Integer>();
		ArrayList<Instance> instanceList = new ArrayList<Instance>();
		int instantCnt = 0;
		// we just want a subset
		while (stream.hasMoreInstances()) {
			if (++instantCnt % 10000 == 0) {
				System.out.println(".. parsed " + instantCnt + " instances.");
			}
			Instance inst = stream.nextInstance();
			String attributeChar = inst.stringValue(reductionAttributeId);

			if (!counter.containsKey(attributeChar)) {
				if (counter.keySet().size() < maxDomainNum) {
					System.out.println("New attribute characteristic ("
							+ (counter.keySet().size() + 1) + ") found: "
							+ attributeChar);
					counter.put(attributeChar, 1);
					instanceList.add(new SparseInstance(inst));
				}
			} else {
				if (counter.get(attributeChar) < maxNumber) {
					counter.put(attributeChar, counter.get(attributeChar) + 1);
					instanceList.add(new SparseInstance(inst));
				}
			}
		}
		System.out.println("Got " + instanceList.size() + " elements in list.");
		// now we shuffle and write back
		Collections.shuffle(instanceList);
		System.out.println("Shuffling ...");
		ArrayList<Attribute> attributeList = new ArrayList<Attribute>();
		for (int i = 0; i < header.numAttributes(); i++) {
			attributeList.add(header.attribute(i));
		}
		Instances dataSet = new Instances("reduced", attributeList, 2);
		for (Instance inst : instanceList) {
			dataSet.add(inst);
			inst.setDataset(dataSet);
		}
		System.out.println("Writing output ...");
		ArffSaver saver = new ArffSaver();
		saver.setInstances(dataSet);
		saver.setFile(new File(outputFile));
		saver.writeBatch();
		System.out.println("Done.");

	}
}
