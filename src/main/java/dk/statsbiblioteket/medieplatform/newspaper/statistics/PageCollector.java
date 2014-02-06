package dk.statsbiblioteket.medieplatform.newspaper.statistics;

import java.io.IOException;
import java.util.Properties;

import dk.statsbiblioteket.medieplatform.autonomous.iterator.common.AttributeParsingEvent;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.statistics.SinkCollector;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.statistics.StatisticCollector;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.statistics.StatisticWriter;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.statistics.Statistics;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.statistics.WeightedMean;
import dk.statsbiblioteket.medieplatform.newspaper.manualQA.AltoWordAccuracyChecker;
import dk.statsbiblioteket.medieplatform.newspaper.manualQA.ConfigConstants;
import dk.statsbiblioteket.util.xml.DOM;
import dk.statsbiblioteket.util.xml.XPathSelector;
import org.w3c.dom.Document;

/**
 * Handles the collection of page level statistics.
 *
 * Uses SinkCollectors as children.
 */
public class PageCollector extends StatisticCollector {
    public static final String PAGES_IN_SECTIONS_STAT = "Pages-In-Sections";
    public static final String OCR_ACCURACY_STAT = "OCR-Accuracy";
    private boolean ignoreZeroAccuracy;

    private static final XPathSelector xpath = DOM.createXPathSelector("mods", "http://www.loc.gov/mods/v3");

    @Override
    public void initialize(String name, StatisticCollector parentCollector, StatisticWriter writer, Properties properties) {
        ignoreZeroAccuracy = Boolean.parseBoolean(properties.getProperty(ConfigConstants.ALTO_IGNORE_ZERO_ACCURACY));
        super.initialize(name, parentCollector, writer, properties);
    }

    /**
     * Adds OCR accuracy statistics for the page, if this is a alto.xml attribute.
     * @param event The event to read the alto xml from
     */
    @Override
    public void handleAttribute(AttributeParsingEvent event) {
        if (event.getName().endsWith("alto.xml")) {
            addAltoWordAccuracyStatistics(event);
        } else if (event.getName().endsWith("mods.xml")) {
            addSectionStatistics(event);
        }
    }

    private void addAltoWordAccuracyStatistics(AttributeParsingEvent event) {
        Double accuracy = AltoWordAccuracyChecker.readAccuracy(event);
        if (!ignoreZeroAccuracy || accuracy > 0) {
            getStatistics().addRelative( OCR_ACCURACY_STAT, new WeightedMean(accuracy, 1));
        }
    }

    private void addSectionStatistics(AttributeParsingEvent event) {
        String section = readSection(event);
        Statistics sectionStatistics = new Statistics();
        sectionStatistics.addCount(section, 1L);
        getStatistics().addSubstatistic(PAGES_IN_SECTIONS_STAT, sectionStatistics);
    }

    public static String readSection(AttributeParsingEvent event) throws NumberFormatException {
        Document doc;
        try {
            doc = DOM.streamToDOM(event.getData(), true);
            if (doc == null) {
                throw new RuntimeException("Could not parse xml");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        final String sectionXPath="mods:mods/mods:part/mods:detail[@type=\"sectionLabel\"]/mods:number";
        String section = xpath.selectString(doc, sectionXPath);
        return section;
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
