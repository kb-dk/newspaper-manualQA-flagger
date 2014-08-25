package dk.statsbiblioteket.medieplatform.newspaper.manualQA;

import dk.statsbiblioteket.medieplatform.autonomous.ResultCollector;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.common.AttributeParsingEvent;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.eventhandlers.DefaultTreeEventHandler;
import dk.statsbiblioteket.medieplatform.newspaper.manualQA.caches.HistogramCache;
import dk.statsbiblioteket.medieplatform.newspaper.manualQA.flagging.FlaggingCollector;
import dk.statsbiblioteket.medieplatform.newspaper.manualQA.utils.HistogramUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    private final int startingColor;
    private Logger log = LoggerFactory.getLogger(getClass());
    private static final String DEFAULT_MIN_ALLOWED_COLOR_VALUE = "0";

    private dk.statsbiblioteket.medieplatform.newspaper.manualQA.flagging.FlaggingCollector
            flaggingCollector;
    private ResultCollector resultCollector;
    private final HistogramCache histogramCache;
    private final int numberOfMissingColorsAllowed;
    private final int maxValueToDeemAColorMissing;

    /**
     * Create the Checker
     * @param resultCollector the result collector for real errors
     * @param flaggingCollector the flagging collector for raised flags
     */
    public MissingColorsHistogramChecker(ResultCollector resultCollector,
                                         FlaggingCollector flaggingCollector, HistogramCache histogramCache,
                                         Properties properties) {
        this.flaggingCollector = flaggingCollector;
        this.resultCollector = resultCollector;
        this.histogramCache = histogramCache;
        this.numberOfMissingColorsAllowed = Integer.parseInt(properties.getProperty(
                ConfigConstants.NUMBER_OF_MISSING_COLORS_ALLOWED));
        this.maxValueToDeemAColorMissing = Integer.parseInt(properties.getProperty(
                ConfigConstants.MAX_VAL_TO_DEEM_A_COLOR_MISSING));
        this.startingColor = Integer.parseInt(properties.getProperty(ConfigConstants.FLAG_IGNORE_COLORS_BELOW,
                DEFAULT_MIN_ALLOWED_COLOR_VALUE));
    }

    @Override
    /**
     * Triggers on .histogram.xml files.
     */
    public void handleAttribute(AttributeParsingEvent event) {
        try {
            if (event.getName().endsWith(".jp2.histogram.xml")) {
                Histogram histogram = histogramCache.getHistogram(event);
                List<Integer> missingColorList = findMissingColors(event.getName(),histogram);

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
            log.error("Caught exception", e);
            resultCollector.addFailure(event.getName(), "exception", getComponent(), e.toString());
        }
    }

    private String getComponent() {
        return getClass().getSimpleName();
    }

    private List<Integer> findMissingColors(String name, Histogram histogram) {
        long[] values = histogram.values();
        List<Integer> result = new ArrayList<>();

        int darkestColor = HistogramUtils.findDarkestColor(values,  startingColor, maxValueToDeemAColorMissing);
        int brightestColor = HistogramUtils.findBrightestColor(values, maxValueToDeemAColorMissing);


        // Only one color, so there's no interval to check, maybe we should check for
        // that special case somewhere else
        if ((darkestColor == values.length - 1) || (brightestColor == 0)) {
            return result;
        }

        // Find missing colors between darkest and brightest
        long minColor = Long.MAX_VALUE;
        for (int i = darkestColor + 1; i < brightestColor; i++) {
            if (values[i] < minColor){
                minColor = values[i];
            }
            if (values[i] <= 0) {
                result.add(i);
            }
        }

        log.debug("{} min color is {}",name, minColor);

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
}
