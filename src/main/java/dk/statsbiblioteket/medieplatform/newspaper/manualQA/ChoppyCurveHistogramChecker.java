package dk.statsbiblioteket.medieplatform.newspaper.manualQA;

import dk.statsbiblioteket.medieplatform.autonomous.ResultCollector;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.common.AttributeParsingEvent;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.eventhandlers.DefaultTreeEventHandler;
import dk.statsbiblioteket.medieplatform.newspaper.manualQA.flagging.FlaggingCollector;

/**
 * Hakker i kurven
 * Vi så en del kurver der ikke manglede farver, men havde vilde variationer mellem to farver. En test er at rulle et
 * window af bredde 3 hen over kurven. For hver punkt skal det gælde at (x0+x2)/2 ~= x1. Undervejs tæller vi så op hvor
 * meget off den ligning er, og hvis den slutteligt er over en hvis størrelse, kan vi flagge billedet.
 */
public class ChoppyCurveHistogramChecker extends DefaultTreeEventHandler {

    private final ResultCollector resultCollector;
    private final FlaggingCollector flaggingCollector;
    private final long threshold;

    /**
     * Create the Checker
     * @param resultCollector the result collector for real errors
     * @param flaggingCollector the flagging collector for raised flags
     * @param threshold the threshold, the total amount which the curve can deviate from a straight line
     */
    public ChoppyCurveHistogramChecker(ResultCollector resultCollector, FlaggingCollector flaggingCollector,
                                       long threshold) {
        this.resultCollector = resultCollector;
        this.flaggingCollector = flaggingCollector;
        this.threshold = threshold;
    }


    @Override
    public void handleAttribute(AttributeParsingEvent event) {
        try {
            if (event.getName().endsWith(".histogram.xml")) {
                Histogram histogram = new Histogram(event.getData());
                long error = testChoppyness(histogram);
                if (error > threshold) {
                    flaggingCollector.addFlag(
                            event,
                            "jp2file",
                            getComponent(),
                            "Unnaturally choppy histogram curve. Possible sign of post processing");
                }
            }
        } catch (Exception e) {
            resultCollector.addFailure(event.getName(), "exception", getComponent(), e.getMessage());
        }

    }

    private String getComponent() {
        return getClass().getName();
    }

    private long testChoppyness(Histogram histogram) {

        long error = 0;
        long[] window = new long[3];
        long[] values = histogram.values();
        for (int i = 0; i < values.length; i++) {
            long value = values[i];
            window[0] = window[1];
            window[1] = window[2];
            window[2] = value;
            if (i < 2) { //we have not filled the first window yet
                continue;
            }
            long expected = (window[0] + window[2]) / 2;
            long increase = Math.abs(expected - window[1]);
            error += increase;
            System.out.println(i - 1 + " " + increase);
        }
        return error;
    }
}
