package dk.statsbiblioteket.medieplatform.newspaper.manualQA;


public class AverageHistogram {
    private static int NUMBER_OF_VALUES_IN_HISTOGRAM = 256;

    // Holds the sums of each value in the histograms
    private long[] sumOfValue = new long[NUMBER_OF_VALUES_IN_HISTOGRAM];

    // The number of histograms, the values of which have been added to the array of sums
    private long histogramCount;


    public AverageHistogram() {
        resetAverageHistogram();
    }


    public void resetAverageHistogram() {
        for (int i = 0; i < NUMBER_OF_VALUES_IN_HISTOGRAM; i++) {
            sumOfValue[i] = 0;
        }
        histogramCount = 0;
    }


    public void addHistogram(long[] histogram) {
        for (int i = 0; i < NUMBER_OF_VALUES_IN_HISTOGRAM; i++) {
            sumOfValue[i] += histogram[i];
        }

        histogramCount++;
    }


    public long[] getAverageHistogramAsArray() {
        long[] averageOfValue = new long[NUMBER_OF_VALUES_IN_HISTOGRAM];

        for (int i = 0; i < NUMBER_OF_VALUES_IN_HISTOGRAM; i++) {
            averageOfValue[i] = sumOfValue[i] / histogramCount;
        }

        return averageOfValue;
    }

    // TODO make getAverageHistogramAsXml() if needed
}
