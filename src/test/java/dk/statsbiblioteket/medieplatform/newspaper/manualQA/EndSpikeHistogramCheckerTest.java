package dk.statsbiblioteket.medieplatform.newspaper.manualQA;

import dk.statsbiblioteket.medieplatform.autonomous.Batch;
import dk.statsbiblioteket.medieplatform.autonomous.ResultCollector;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.common.AttributeParsingEvent;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.eventhandlers.TreeEventHandler;
import dk.statsbiblioteket.medieplatform.newspaper.manualQA.flagging.FlaggingCollector;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import static org.testng.Assert.*;


public class EndSpikeHistogramCheckerTest {
    @Test
    public void testHandleAttributeGood() throws Exception {
        ResultCollector resultCollector = new ResultCollector("blah", "blah");
        FlaggingCollector flaggingCollector = new FlaggingCollector(new Batch("40000"), null, "0.1-SNAPSHOT", 100);
        //Threshold 0 so this expects perfect linearity
        TreeEventHandler histogramHandler = new EndSpikeHistogramChecker(
                resultCollector, flaggingCollector, 0.1, 0, 2, 255, 255, 2, 0.5);
        AttributeParsingEvent event = createAttributeEvent(
                "B400022028252-RT1/400022028252-08/1795-12-20-01/adresseavisen1759-1795-12-20-01-0079.jp2.histogram.xml",
                HistogramXml.getSampleGoodHistogram());
        histogramHandler.handleAttribute(event);
        assertFalse(flaggingCollector.hasFlags(), flaggingCollector.toReport());
    }

    @Test
    public void testHandleAttributeBadSpike() throws Exception {
        ResultCollector resultCollector = new ResultCollector("blah", "blah");
        FlaggingCollector flaggingCollector = new FlaggingCollector(new Batch("40000"), null, "0.1-SNAPSHOT", 100);
        TreeEventHandler histogramHandler = new EndSpikeHistogramChecker(
                resultCollector, flaggingCollector, 0.05, 0, 2, 255, 255, 100, 100);
        AttributeParsingEvent event = createAttributeEvent(
                "B400022028252-RT1/400022028252-08/1795-12-20-01/adresseavisen1759-1795-12-20-01-0079.film.histogram.xml",
                HistogramXml.getSampleBadHistogram());
        histogramHandler.handleAttribute(event);
        assertTrue(flaggingCollector.hasFlags(), flaggingCollector.toReport());
    }

    @Test
    public void testHandleAttributeBadLowLightBlowout() throws Exception {
        ResultCollector resultCollector = new ResultCollector("blah", "blah");
        FlaggingCollector flaggingCollector = new FlaggingCollector(new Batch("40000"), null, "0.1-SNAPSHOT", 100);
        TreeEventHandler histogramHandler = new EndSpikeHistogramChecker(
                resultCollector, flaggingCollector, 1, 0, 2, 255, 255, 2, 100);
        AttributeParsingEvent event = createAttributeEvent(
                "B400022028252-RT1/400022028252-08/1795-12-20-01/adresseavisen1759-1795-12-20-01-0079.film.histogram.xml",
                HistogramXml.getSampleBadHistogram());
        histogramHandler.handleAttribute(event);
        assertTrue(flaggingCollector.hasFlags(), flaggingCollector.toReport());
    }

    @Test
    public void testHandleAttributeBadHighLightBlowout() throws Exception {
        ResultCollector resultCollector = new ResultCollector("blah", "blah");
        FlaggingCollector flaggingCollector = new FlaggingCollector(new Batch("40000"), null, "0.1-SNAPSHOT", 100);
        TreeEventHandler histogramHandler = new EndSpikeHistogramChecker(
                resultCollector, flaggingCollector, 1, 0, 2, 255, 255, 100, 0.5);
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
