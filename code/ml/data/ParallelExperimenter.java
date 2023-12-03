package ml.data;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import ml.classifiers.AveragePerceptronClassifier;
import ml.classifiers.Classifier;
import ml.classifiers.Experimenter;

public class ParallelExperimenter {

    public static void main(String[] args) {
        System.out.println("Starting");

        // set up experiments
        String path = "data/suicide-test.train";

        Experimenter experiment = new Experimenter();
        experiment.setRepetitions(5);
        System.out.println("Generating n-fold");
        // generate n-fold
        System.out.println("start loading dataset");

        WordFilter reader = new WordFilter(path);
        DataSet data = new DataSet(path, reader, 1);
        System.out.println("Complete loading dataset");
        CrossValidationSet tenFolds = new CrossValidationSet(data, 10, true);

        // variables for experiments
        Classifier avg = new AveragePerceptronClassifier();
        Classifier classifiers = avg;

        ExecutorService executor = Executors.newFixedThreadPool(10); // Adjust the pool size based on your needs

        List<Future<Double>> foldResults = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            Callable<Double> foldTask = new FoldTask(experiment, classifiers, tenFolds, i);
            Future<Double> future = executor.submit(foldTask);
            foldResults.add(future);
        }

        executor.shutdown();

        double overallFoldAccuracy = 0.0;
        try {
            for (Future<Double> result : foldResults) {
                overallFoldAccuracy += result.get();
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        System.out.println("Overall Accuracy: " + (overallFoldAccuracy / 10));
    }

    static class FoldTask implements Callable<Double> {
        private final Experimenter experiment;
        private final Classifier classifiers;
        private final CrossValidationSet tenFolds;
        private final int foldIndex;

        public FoldTask(Experimenter experiment, Classifier classifiers, CrossValidationSet tenFolds, int foldIndex) {
            this.experiment = experiment;
            this.classifiers = classifiers;
            this.tenFolds = tenFolds;
            this.foldIndex = foldIndex;
        }

        @Override
        public Double call() throws Exception {
            System.out.println("Running test on fold " + foldIndex);
            double foldAccuracies = 0.0;
            for (int k = 0; k < experiment.getRepetitions(); k++) {
                DataSetSplit currentData = tenFolds.getValidationSet(foldIndex);
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

                double foldAccuracy = ((double) (correct * 100) / total);
                foldAccuracies += foldAccuracy;
            }
            double averageAccuracy = foldAccuracies / experiment.getRepetitions();
            System.out.println("fold " + foldIndex + " ==" + averageAccuracy);
            return averageAccuracy;
        }
    }
}
