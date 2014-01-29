package dk.statsbiblioteket.medieplatform.newspaper.manualQA;

import dk.statsbiblioteket.medieplatform.autonomous.ResultCollector;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.common.AttributeParsingEvent;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.eventhandlers.DefaultTreeEventHandler;
import dk.statsbiblioteket.medieplatform.newspaper.manualQA.flagging.FlaggingCollector;

import java.util.Arrays;

/**
 * Hakker i kurven
 * Vi så en del kurver der ikke manglede farver, men havde vilde variationer mellem to farver.
 * En test er at rulle et
 * window af bredde 3 hen over kurven. For hver punkt skal det gælde at (x0+x2)/2 ~= x1.
 * Undervejs tæller vi så op hvor
 * meget off den ligning er, og hvis den slutteligt er over en hvis størrelse,
 * kan vi flagge billedet.
 */
public class ChoppyCurveHistogramChecker extends DefaultTreeEventHandler {

    private final ResultCollector resultCollector;
    private final FlaggingCollector flaggingCollector;
    private final double threshold;
    private final int maxIrregularities;
    private boolean[] errorPositions = new boolean[256];

    /**
     * Create the Checker
     * @param resultCollector the result collector for real errors
     * @param flaggingCollector the flagging collector for raised flags
     * @param threshold how much a value is allowed to deviate from the average of its two
     *                  neighbours (in pct, >0) before it is considered an irregularity
     * @param maxIrregularities the maximum number of peaks/valleys allowed before flagged as an error
     */
    public ChoppyCurveHistogramChecker(ResultCollector resultCollector, FlaggingCollector flaggingCollector,
                                       double threshold, int maxIrregularities) {
        this.resultCollector = resultCollector;
        this.flaggingCollector = flaggingCollector;
        this.threshold = threshold;
        this.maxIrregularities = maxIrregularities;
    }


    @Override
    public void handleAttribute(AttributeParsingEvent event) {
        try {
            if (event.getName().endsWith(".histogram.xml")) {
                Histogram histogram = new Histogram(event.getData());
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
