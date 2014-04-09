package dk.statsbiblioteket.medieplatform.newspaper.manualQA;

import dk.statsbiblioteket.medieplatform.autonomous.ResultCollector;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.common.AttributeParsingEvent;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.eventhandlers.DefaultTreeEventHandler;
import dk.statsbiblioteket.medieplatform.newspaper.manualQA.flagging.FlaggingCollector;

import java.util.Arrays;
import java.util.Properties;

/**
 * Checks for "chops" in the curve. We have seen several curves that did not have missing colours, but did have great variation
 * in adjacent colours on the histogram curve.
 */
public class ChoppyCurveHistogramChecker extends DefaultTreeEventHandler {

    private final ResultCollector resultCollector;
    private final FlaggingCollector flaggingCollector;
    private final HistogramCache histogramCache;
    private final double threshold;
    private final int maxIrregularities;
    private boolean[] errorPositions = new boolean[256];

    /**
     * Create the Checker
     * @param resultCollector the result collector for real errors
     * @param flaggingCollector the flagging collector for raised flags
     */
    public ChoppyCurveHistogramChecker(ResultCollector resultCollector, FlaggingCollector flaggingCollector, HistogramCache histogramCache,
                                       Properties properties) {
        this.resultCollector = resultCollector;
        this.flaggingCollector = flaggingCollector;
        this.histogramCache = histogramCache;
        this.threshold = Double.parseDouble(properties.getProperty(ConfigConstants.CHOPPY_CHECK_THRESHOLD));
        this.maxIrregularities = Integer.parseInt(properties.getProperty(
                        ConfigConstants.CHOPPY_CHECK_MAX_IRREGULARITIES));
    }


    @Override
    public void handleAttribute(AttributeParsingEvent event) {
        try {
            if (event.getName().endsWith(".jp2.histogram.xml")) {
                Histogram histogram = histogramCache.getHistogram(event);
                int irregularities = countIrregularities(histogram);

                if (irregularities > maxIrregularities) {
                    flaggingCollector.addFlag(
                            event,
                            "jp2file",
                            getComponent(),
                            "Unnaturally choppy histogram curve. Possible sign of post processing."
                                    + " Found " + irregularities + " peaks/valleys and only "
                                    + maxIrregularities + " were allowed."
                                    + " Positions of these: " + errorPositionsToString() + ".");
                }
            }
        } catch (Exception e) {
            resultCollector.addFailure(event.getName(), "exception", getComponent(), e.getMessage());
        }
    }


    private String getComponent() {
        return getClass().getName();
    }


    /**
     * Count local "peaks and valleys" that might break the continuity of the histogram curve
     * @param histogram The histogram for which to count local irregularities
     * @return number of irregularities
     */
    private int countIrregularities(Histogram histogram) {
        long[] values = histogram.values();
        long value, pre, post;
        double deviation;
        int count = 0;

        if (values.length != 256) {
            throw new IllegalArgumentException("Expected array of length 256");
        }

        Arrays.fill(errorPositions, false);
        for (int i = 0; i < values.length; i++) {
            if (i == 0 || i == values.length - 1) {
                // This value does not have both a pre-value and a post-value, so skip it
                continue;
            }

            value = values[i];
            pre = values[i - 1];
            post = values[i + 1];

            if (pre < value && value < post) {
                // We are on a rising edge, this should not be flagged as an error
                continue;
            }

            if (pre > value && value > post) {
                // We are on a falling edge, this should not be flagged as an error
                continue;
            }

            if (deviationTooBig(pre, value) && deviationTooBig(post, value)) {
                //System.out.println(i);
                count++;
                errorPositions[i] = true;
            }
        }

        return count;
    }

    /**
     * Calculates whether the given value deviates more than allowed from the given comparison-value
     * @param comparison Value to compare with
     * @param value Value in question
     * @return whether the given value deviates more than allowed from the given comparison-value
     */
    private boolean deviationTooBig(long comparison, long value) {
        long maxAllowedValue = (long)((1.0 + threshold) * comparison);
        long minAllowedValue = (long)((1.0 - threshold) * comparison);

        return value < minAllowedValue || value > maxAllowedValue;
    }

    /**
     * Formats a string for presentation, containing the positions (in the histogram) of the errors found
     * @return The formatted string
     */
    private String errorPositionsToString() {
        String s = "";
        boolean hadOne = false;
        for (int i = 0; i < errorPositions.length; i++) {
            if (errorPositions[i]) {
                if (hadOne) {
                    s += ", ";
                }
                s += i;
                hadOne = true;
            }
        }
        return s;
    }
}
