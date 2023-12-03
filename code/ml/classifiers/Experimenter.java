package ml.classifiers;

import ml.classifiers.PerceptronClassifier;
import ml.classifiers.AveragePerceptronClassifier;
 
import ml.data.CrossValidationSet;
import ml.data.DataSet;
import ml.data.DataSetSplit;
import ml.data.Example;
import ml.data.WordFilter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;

import javax.xml.crypto.Data;

import ml.classifiers.*;

/**
 * 
 */
public class Experimenter {
	int repetitions;
	
	/**
	 * 
	 */
	public void setRepetitions(int repetition) {
		this.repetitions = repetition;
	}
	
	public int getRepetitions() {
		return this.repetitions;
	}
	
	
	public void train(Classifier classifier, DataSet trainData) {
		classifier.train(trainData);
	}
	
	public double classify(Classifier classifier, Example example) {
		return classifier.classify(example);
	}
	

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("Starting");
	
		// set up experiments
		String path = "data/suicide-test.train";
		
		Experimenter experiment = new Experimenter();
		experiment.setRepetitions(5);
		System.out.println("Generating n-fold");
		// generate n-fold
		System.out.println("start loading dataset");
		WordFilter filter = new WordFilter(path);
		for (int l = 2; l < 10; l++ ) {
		filter.setMinimumWordLength(l);
		
		DataSet data = new DataSet(path, filter, 1);
	

		System.out.println("Complete loading dataset");
		CrossValidationSet tenFolds = new CrossValidationSet(data, 10, true);
		
		// variables for experiments

		Classifier avg = new AveragePerceptronClassifier();
		
		Classifier classifiers = avg;
		
	
		
		DataSet train1 = tenFolds.getValidationSet(1).getTrain();
		DataSet test1 =  train1 = tenFolds.getValidationSet(1).getTest();

		double overallFoldAccuracy = 0.0;
		for (int i = 0 ; i < 10; i++) {
			System.out.println("Running test on fold " + String.valueOf(i));
			double foldAccuracies = 0.0;
			for (int k = 0; k < experiment.getRepetitions(); k++) {
				DataSetSplit currentData = tenFolds.getValidationSet(i);
				DataSet trainNfold = currentData.getTrain();
				experiment.train(classifiers, trainNfold);
				int correct = 0;
				int total = 0;
				for (int j = 0; j < currentData.getTest().getData().size(); j++) {
					double pred = experiment.classify(classifiers, currentData.getTest().getData().get(j));
					if (pred == currentData.getTest().getData().get(j).getLabel()) {
						correct++;
					}
					total++;
					
				}
				System.out.println(correct + "/ " + total);
				
				double foldAccuracy = ((double)(correct * 100)/ total);
				foldAccuracies+= foldAccuracy;
			}
			
			overallFoldAccuracy += foldAccuracies/experiment.getRepetitions();
			
			System.out.println("fold " + String.valueOf(i) + " ==" + String.valueOf(foldAccuracies/experiment.getRepetitions()));
			foldAccuracies = 0.0; 
		}
		System.out.println(overallFoldAccuracy);
		
	}		

		

		

	}
}
