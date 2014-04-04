package dk.statsbiblioteket.medieplatform.newspaper.manualQA;

import dk.statsbiblioteket.medieplatform.autonomous.ResultCollector;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.common.AttributeParsingEvent;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.eventhandlers.DefaultTreeEventHandler;
import dk.statsbiblioteket.medieplatform.newspaper.manualQA.flagging.FlaggingCollector;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

/**
 * If we find the lightest and darkest colours on an image, there should be no colours between these that are zero (or close to
 * zero). The reason is that an analog system cannot produce such (close to) zero colours within an otherwise "coloured range".
 * So this would be a sign of post-processing, and that is what this checker checks for.
 */
public class MissingColorsHistogramChecker extends DefaultTreeEventHandler {
    private dk.statsbiblioteket.medieplatform.newspaper.manualQA.flagging.FlaggingCollector
            flaggingCollector;
    private ResultCollector resultCollector;
    private final int numberOfMissingColorsAllowed;
    private final int maxValueToDeemAColorMissing;

    /**
     * Create the Checker
     * @param resultCollector the result collector for real errors
     * @param flaggingCollector the flagging collector for raised flags
     */
    public MissingColorsHistogramChecker(ResultCollector resultCollector,
                                         FlaggingCollector flaggingCollector,
                                         Properties properties) {
        this.flaggingCollector = flaggingCollector;
        this.resultCollector = resultCollector;
        this.numberOfMissingColorsAllowed = Integer.parseInt(properties.getProperty(
                ConfigConstants.NUMBER_OF_MISSING_COLORS_ALLOWED));
        this.maxValueToDeemAColorMissing = Integer.parseInt(properties.getProperty(
                ConfigConstants.MAX_VAL_TO_DEEM_A_COLOR_MISSING));
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

                if (missingColorList.size() > numberOfMissingColorsAllowed) {
                    flaggingCollector.addFlag(
                            event,
                            "jp2file",
                            getComponent(),
                            "There are " + missingColorList.size() + " missing colors"
                                    + " in the picture. This could be the result of post processing."
                                    + " The missing colors have positions: "
                                    + integerListToString(missingColorList) + ".");
                }
            }
        } catch (Exception e) {
            resultCollector.addFailure(event.getName(), "exception", getComponent(), e.getMessage());
        }
    }

    private String getComponent() {
        return getClass().getSimpleName();
    }

    private List<Integer> findMissingColors(Histogram histogram) {
        long[] values = histogram.values();
        List<Integer> result = new ArrayList<>();
        int darkestColor = 0;
        int brightestColor = 255;
        int i;

        // Find darkest color
        for (i = 0; i < values.length; i++) {
            if (values[i] > 0) {
                break;
            }
        }
        if (i < values.length) {
            darkestColor = i;
        } else {
            darkestColor = -1;
        }

        // Find brightest color
        for (i = values.length - 1; i > -1; i--) {
            if (values[i] > 0) {
                break;
            }
        }
        if (i > -1) {
            brightestColor = i;
        } else {
            brightestColor = values.length;
        }

        // Only one color, so there's no interval to check, maybe we should check for
        // that special case somewhere else
        if ((darkestColor == values.length - 1) || (brightestColor == 0)) {
            return result;
        }

        // Find missing colors between darkest and brightest
        for (i = darkestColor + 1; i < brightestColor; i++) {
            if (values[i] <= maxValueToDeemAColorMissing) {
                result.add(i);
            }
        }

        return result;
    }

    private String integerListToString(List<Integer> l) {
        String s = "";

        Iterator<Integer> iterator = l.iterator();
        while(iterator.hasNext()) {
            s += iterator.next();
            if(iterator.hasNext()){
                s += ", ";
            }
        }

        return s;
    }

    /*
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
    */

}
