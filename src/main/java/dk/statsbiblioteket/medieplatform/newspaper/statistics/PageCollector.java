package dk.statsbiblioteket.medieplatform.newspaper.statistics;

import dk.statsbiblioteket.medieplatform.autonomous.iterator.common.AttributeParsingEvent;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.statistics.SinkCollector;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.statistics.StatisticCollector;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.statistics.WeightedMean;
import dk.statsbiblioteket.medieplatform.newspaper.manualQA.AltoWordAccuracyChecker;

/**
 * Handles the collection of page level statistics.
 *
 * Uses SinkCollectors as children.
 */
public class PageCollector extends StatisticCollector {
    public static final String NUMBER_OF_SECTIONS_STAT = "NumberOfSections";
    public static final String OCR_PERCENTAGE_STAT = "OCRPercentage";
    public static final String OCR_ACCURACY_STAT = "OCR-Accuracy";

    @Override
    public void handleAttribute(AttributeParsingEvent event) {
        //ToDo Below are examples of how to add OCR and sections statistics. SHould be replaced with
        // real implementation
        if (event.getName().endsWith("alto.xml")) {
            getStatistics().addRelative(
                    OCR_ACCURACY_STAT,
                    new WeightedMean(AltoWordAccuracyChecker.readAccuracy(event), 1)
            );
        }

        //getStatistics().addRelative(OCR_RELATIVE_STAT, new WeightedMean(40, 100));
        //getStatistics().addCount(NUMBER_OF_SECTIONS_STAT, 7);
    }

    /**
     * Suppress creation of page nodes in the output
     */
    @Override
    protected boolean writeNode() {
        return false;
    }

    @Override
    protected StatisticCollector createChild(String eventName) {
        return new SinkCollector();
    }

    @Override
    public String getType() {
        return "Page";
    }
}
