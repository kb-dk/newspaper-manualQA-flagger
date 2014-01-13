package dk.statsbiblioteket.medieplatform.newspaper.manualQA;

import dk.statsbiblioteket.medieplatform.autonomous.ResultCollector;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.common.AttributeParsingEvent;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.eventhandlers.DefaultTreeEventHandler;
import dk.statsbiblioteket.medieplatform.newspaper.manualQA.flagging.FlaggingCollector;

import java.util.ArrayList;
import java.util.List;

/**
 * Hvis vi finder den lyseste og den mørkeste farve på billedet, har vi et krav om at ingen farver mellem de to må være
 * 0 eller meget lave. Det er ikke muligt for et analogt system at producere dette, så det er et klart tegn på
 * efterbehandling.
 */
public class MissingColorsHistogramChecker extends DefaultTreeEventHandler {

    private dk.statsbiblioteket.medieplatform.newspaper.manualQA.flagging.FlaggingCollector flaggingCollector;
    private ResultCollector resultCollector;
    private int threshold;

    /**
     * Create the Checker
     * @param resultCollector the result collector for real errors
     * @param flaggingCollector the flagging collector for raised flags
     * @param threshold the threshold, in this case the number of missing colors allowed
     */
    public MissingColorsHistogramChecker(ResultCollector resultCollector, FlaggingCollector flaggingCollector,
                                         int threshold) {
        this.flaggingCollector = flaggingCollector;
        this.resultCollector = resultCollector;
        this.threshold = threshold;
    }

    @Override
    /**
     * Triggers on .histogram.xml files.
     */
    public void handleAttribute(AttributeParsingEvent event) {
        try {
            if (event.getName().endsWith(".histogram.xml")) {
                Histogram histogram = new Histogram(event.getData());
                List<Integer> missingColorList = findMissingColors(histogram);
                if (missingColorList.size() > threshold) {
                    flaggingCollector.addFlag(
                            event,
                            "jp2file",
                            getComponent(),
                            "There are '" + missingColorList.size() + " in the picture. This could be the result of post processing");
                }
            }
        } catch (Exception e) {
            resultCollector.addFailure(event.getName(), "Exception", "component", e.getMessage());
        }

    }

    private String getComponent() {
        //TODO
        return null;
    }

    private List<Integer> findMissingColors(Histogram histogram) {
        long[] values = histogram.values();
        List<Integer> result = new ArrayList<>();

        int lowest = -1;
        int highest = -1;
        for (int i = 0; i < values.length; i++) {
            long value = values[i];
            if (lowest == -1 && value > 0) {
                lowest = i;
            }
            if (value > 0) {
                highest = i;
            }

        }
        for (int i = lowest; i <= highest; i++) {
            long value = values[i];

            if (value == 0) {
                result.add(i);

            }
        }
        return result;

    }
}
