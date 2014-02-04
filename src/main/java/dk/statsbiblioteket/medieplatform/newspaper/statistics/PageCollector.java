package dk.statsbiblioteket.medieplatform.newspaper.statistics;

import java.util.Properties;

import dk.statsbiblioteket.medieplatform.autonomous.iterator.common.AttributeParsingEvent;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.statistics.SinkCollector;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.statistics.StatisticCollector;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.statistics.StatisticWriter;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.statistics.WeightedMean;
import dk.statsbiblioteket.medieplatform.newspaper.manualQA.AltoWordAccuracyChecker;
import dk.statsbiblioteket.medieplatform.newspaper.manualQA.ConfigConstants;

/**
 * Handles the collection of page level statistics.
 *
 * Uses SinkCollectors as children.
 */
public class PageCollector extends StatisticCollector {
    public static final String NUMBER_OF_SECTIONS_STAT = "NumberOfSections";
    public static final String OCR_ACCURACY_STAT = "OCR-Accuracy";
    private boolean ignoreZeroAccuracy;

    @Override
    protected void initialize(String name, StatisticCollector parentCollector, StatisticWriter writer, Properties properties) {
        super.initialize(name, parentCollector, writer, properties);
        ignoreZeroAccuracy = Boolean.parseBoolean(properties.getProperty(ConfigConstants.ALTO_IGNORE_ZERO_ACCURACY));
    }

    /**
     * Adds OCR accuracy statistics for the page, if this is a alto.xml attribute.
     * @param event The event to read the alto xml from
     */
    @Override
    public void handleAttribute(AttributeParsingEvent event) {
        if (event.getName().endsWith("alto.xml")) {
            Double accuracy = AltoWordAccuracyChecker.readAccuracy(event);
            if (!ignoreZeroAccuracy || accuracy > 0) {
                getStatistics().addRelative(
                        OCR_ACCURACY_STAT,
                        new WeightedMean(AltoWordAccuracyChecker.readAccuracy(event), 1)
                );
            }
        }
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
