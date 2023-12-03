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
		for (int l = 2; l < 9; l++ ) {
		filter.setMinimumWordLength(l);
		
		DataSet data = new DataSet(path, filter, 1);
	
		System.out.println("testing length:" + l);
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
		
		System.out.println(overallFoldAccuracy);
		overallFoldAccuracy = 0.0;
		}
	}		
		Experimenter experiment2 = new Experimenter();
		experiment2.setRepetitions(5);
		System.out.println("Generating sfecond n-fold");
		// generate n-fold
		System.out.println("start second loading dataset");
		ArrayList<String> string = new ArrayList<String>();
		string.add("death");
		string.add("life");
		string.add("depression");
		string.add("suicide");
		string.add("tired");
		string.add("trapped");
		string.add("ending");
		string.add("lonely");
		string.add("kill");
		string.add("anexity");
		string.add("overwhelmed");
		string.add("dying");
		string.add("dead");
		string.add("depressed");
		string.add("depressing");
		string.add("anxious");
		string.add("guilt");
		string.add("insane");
		string.add("insanity");
		string.add("fail");
		string.add("faliure");
		string.add("pain");
		string.add("painful");
		string.add("crying");

		
	for(String word: string){
		WordFilter filter2 = new WordFilter(path);
		filter2.filtervs(false);
		filter2.setwordlist(word);
		DataSet data2 = new DataSet(path, filter2, 1);
	
		System.out.println("Testing word: " + word);
		System.out.println("Complete loading dataset");
		CrossValidationSet tenFolds2 = new CrossValidationSet(data2, 10, true);
		
		// variables for experiments

		Classifier avg2 = new AveragePerceptronClassifier();
		
		Classifier classifiers2 = avg2;
		
	
		
		DataSet train2 = tenFolds2.getValidationSet(1).getTrain();
		DataSet test2 =  train2 = tenFolds2.getValidationSet(1).getTest();

		double overallFoldAccuracy2 = 0.0;
		for (int i = 0 ; i < 10; i++) {
			System.out.println("Running test on fold " + String.valueOf(i));
			double foldAccuracies2 = 0.0;
			for (int k = 0; k < experiment2.getRepetitions(); k++) {
				DataSetSplit currentData2 = tenFolds2.getValidationSet(i);
				DataSet trainNfold2 = currentData2.getTrain();
				experiment2.train(classifiers2, trainNfold2);
				int correct2 = 0;
				int total2 = 0;
				for (int j = 0; j < currentData2.getTest().getData().size(); j++) {
					double pred2 = experiment2.classify(classifiers2, currentData2.getTest().getData().get(j));
					if (pred2 == currentData2.getTest().getData().get(j).getLabel()) {
						correct2++;
					}
					total2++;
					
				}
				System.out.println(correct2 + "/ " + total2);
				double foldAccuracy2 = ((double)(correct2 * 100)/ total2);
				foldAccuracies2+= foldAccuracy2;
			}
			
			overallFoldAccuracy2 += foldAccuracies2/experiment2.getRepetitions();
			
			System.out.println("fold " + String.valueOf(i) + " ==" + String.valueOf(foldAccuracies2/experiment.getRepetitions()));
			foldAccuracies2 = 0.0; 
		
		System.out.println(overallFoldAccuracy2);
		overallFoldAccuracy2 = 0.0;
		}

}	

	}
}
