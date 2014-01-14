package dk.statsbiblioteket.medieplatform.newspaper.manualQA;

import java.util.Arrays;

/**
 * This class represents a histogram that is value-by-value average of several histgorams.
 */
public class AverageHistogram {
    private static int NUMBER_OF_VALUES_IN_HISTOGRAM = 256;

    // Holds the sums of each value in the histograms for which this one is an average
    private long[] sumOfValue = new long[NUMBER_OF_VALUES_IN_HISTOGRAM];

    // The number of histograms, the values of which have been added to the array of sums
    private long histogramCount;


    public AverageHistogram() {
    }


    public void resetAverageHistogram() {
        Arrays.fill(sumOfValue, 0);
        histogramCount = 0;
    }


    public void addHistogram(long[] histogram) {
        if (histogram.length != 256) {
            throw new IllegalArgumentException("Expected array of length 256");
        }

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
