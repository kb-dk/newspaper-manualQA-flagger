package dk.statsbiblioteket.medieplatform.newspaper.manualQA;

import dk.statsbiblioteket.medieplatform.autonomous.Batch;
import dk.statsbiblioteket.medieplatform.autonomous.ResultCollector;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.common.AttributeParsingEvent;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.eventhandlers.TreeEventHandler;
import dk.statsbiblioteket.medieplatform.newspaper.manualQA.caches.HistogramCache;
import dk.statsbiblioteket.medieplatform.newspaper.manualQA.flagging.FlaggingCollector;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;


public class EndSpikeHistogramCheckerTest {
    Properties properties;

    @BeforeMethod
    public void setUp() {
        properties = new Properties();
        properties.setProperty(ConfigConstants.END_SPIKE_THRESHOLD, "0.1");
        properties.setProperty(ConfigConstants.END_SPIKE_MIN_COLOR_CONSIDERED_BLACK, "0");
        properties.setProperty(ConfigConstants.FLAG_IGNORE_COLORS_BELOW, "2");
        properties.setProperty(ConfigConstants.END_SPIKE_MIN_COLOR_CONSIDERED_WHITE, "255");
        properties.setProperty(ConfigConstants.END_SPIKE_MAX_COLOR_CONSIDERED_WHITE, "255");
        properties.setProperty(ConfigConstants.END_SPIKE_MAX_PERCENT_ALLOWED_NEAR_BLACK, "2");
        properties.setProperty(ConfigConstants.END_SPIKE_MAX_PERCENT_ALLOWED_NEAR_WHITE, "0.5");
    }

    @Test
    public void testHandleAttributeGood() throws Exception {
        ResultCollector resultCollector = new ResultCollector("blah", "blah");
        FlaggingCollector flaggingCollector = new FlaggingCollector(new Batch("40000"), null, "0.1-SNAPSHOT", 100);
        //Threshold 0 so this expects perfect linearity
        TreeEventHandler histogramHandler = new EndSpikeHistogramChecker(
                resultCollector, flaggingCollector, new HistogramCache(),properties);
        AttributeParsingEvent event = createAttributeEvent(
                "B400022028252-RT1/400022028252-08/1795-12-20-01/adresseavisen1759-1795-12-20-01-0079.jp2.histogram.xml",
                HistogramXml.getSampleGoodHistogram());
        histogramHandler.handleAttribute(event);
        assertFalse(flaggingCollector.hasFlags(), flaggingCollector.toReport());
    }

    @Test
    public void testHandleAttributeBadSpike() throws Exception {
        properties.setProperty(ConfigConstants.END_SPIKE_THRESHOLD, "0.05");
        properties.setProperty(ConfigConstants.END_SPIKE_MAX_PERCENT_ALLOWED_NEAR_BLACK, "100");
        properties.setProperty(ConfigConstants.END_SPIKE_MAX_PERCENT_ALLOWED_NEAR_WHITE, "100");

        ResultCollector resultCollector = new ResultCollector("blah", "blah");
        FlaggingCollector flaggingCollector = new FlaggingCollector(new Batch("40000"), null, "0.1-SNAPSHOT", 100);
        TreeEventHandler histogramHandler = new EndSpikeHistogramChecker(
                resultCollector, flaggingCollector, new HistogramCache(), properties);
        AttributeParsingEvent event = createAttributeEvent(
                "B400022028252-RT1/400022028252-08/1795-12-20-01/adresseavisen1759-1795-12-20-01-0079.film.histogram.xml",
                HistogramXml.getSampleBadHistogram());
        histogramHandler.handleAttribute(event);
        assertTrue(flaggingCollector.hasFlags(), flaggingCollector.toReport());
    }

    @Test
    public void testHandleAttributeBadLowLightBlowout() throws Exception {
        properties.setProperty(ConfigConstants.END_SPIKE_THRESHOLD, "1");
        properties.setProperty(ConfigConstants.END_SPIKE_MAX_PERCENT_ALLOWED_NEAR_BLACK, "2");
        properties.setProperty(ConfigConstants.END_SPIKE_MAX_PERCENT_ALLOWED_NEAR_WHITE, "100");

        ResultCollector resultCollector = new ResultCollector("blah", "blah");
        FlaggingCollector flaggingCollector = new FlaggingCollector(new Batch("40000"), null, "0.1-SNAPSHOT", 100);
        TreeEventHandler histogramHandler = new EndSpikeHistogramChecker(
                resultCollector, flaggingCollector, new HistogramCache(),properties);
        AttributeParsingEvent event = createAttributeEvent(
                "B400022028252-RT1/400022028252-08/1795-12-20-01/adresseavisen1759-1795-12-20-01-0079.film.histogram.xml",
                HistogramXml.getSampleBadHistogram());
        histogramHandler.handleAttribute(event);
        assertTrue(flaggingCollector.hasFlags(), flaggingCollector.toReport());
    }

    @Test
    public void testHandleAttributeBadHighLightBlowout() throws Exception {
        properties.setProperty(ConfigConstants.END_SPIKE_THRESHOLD, "1");
        properties.setProperty(ConfigConstants.END_SPIKE_MAX_PERCENT_ALLOWED_NEAR_BLACK, "100");
        properties.setProperty(ConfigConstants.END_SPIKE_MAX_PERCENT_ALLOWED_NEAR_WHITE, "0.5");

        ResultCollector resultCollector = new ResultCollector("blah", "blah");
        FlaggingCollector flaggingCollector = new FlaggingCollector(new Batch("40000"), null, "0.1-SNAPSHOT", 100);
        TreeEventHandler histogramHandler = new EndSpikeHistogramChecker(
                resultCollector, flaggingCollector, new HistogramCache(),properties);
        AttributeParsingEvent event = createAttributeEvent(
                "B400022028252-RT1/400022028252-08/1795-12-20-01/adresseavisen1759-1795-12-20-01-0079.film.histogram.xml",
                HistogramXml.getSampleBadHistogram());
        histogramHandler.handleAttribute(event);
        assertTrue(flaggingCollector.hasFlags(), flaggingCollector.toReport());
    }

    private AttributeParsingEvent createAttributeEvent(String name, final String contents) {
        return new AttributeParsingEvent(name) {
            @Override
            public InputStream getData() throws IOException {
                return new ByteArrayInputStream(contents.getBytes());

            }

            @Override
            public String getChecksum() throws IOException {
                return null;
            }
        };
    }

}
