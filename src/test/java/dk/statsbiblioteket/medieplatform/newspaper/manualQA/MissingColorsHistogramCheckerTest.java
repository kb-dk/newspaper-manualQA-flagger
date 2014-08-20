package dk.statsbiblioteket.medieplatform.newspaper.manualQA;

import dk.statsbiblioteket.medieplatform.autonomous.Batch;
import dk.statsbiblioteket.medieplatform.autonomous.ResultCollector;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.common.AttributeParsingEvent;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.eventhandlers.DefaultTreeEventHandler;
import dk.statsbiblioteket.medieplatform.newspaper.manualQA.caches.HistogramCache;
import dk.statsbiblioteket.medieplatform.newspaper.manualQA.flagging.FlaggingCollector;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class MissingColorsHistogramCheckerTest {
    Properties properties;

    @BeforeMethod
    public void setUp() {
        properties = new Properties();
        properties.setProperty(ConfigConstants.NUMBER_OF_MISSING_COLORS_ALLOWED, "0");
        properties.setProperty(ConfigConstants.MAX_VAL_TO_DEEM_A_COLOR_MISSING, "10");
    }

    @Test
    public void testHandleAttributeGood() throws Exception {
        ResultCollector resultCollector = new ResultCollector("blah", "blah");
        FlaggingCollector flaggingCollector = new FlaggingCollector(new Batch("40000"), null, "0.1-SNAPSHOT", 100);
        //Threshold 0 so this expects perfect linearity
        DefaultTreeEventHandler histogramHandler = new MissingColorsHistogramChecker(
                resultCollector, flaggingCollector, new HistogramCache(),properties);
        AttributeParsingEvent event = createAttributeEvent(
                "B400022028252-RT1/400022028252-08/1795-12-20-01/adresseavisen1759-1795-12-20-01-0079.jp2.histogram.xml",
                HistogramXml.getSampleGoodHistogram());
        histogramHandler.handleAttribute(event);
        Assert.assertFalse(flaggingCollector.hasFlags(), flaggingCollector.toReport());
    }

    @Test
    public void testHandleAttributeBad() throws Exception {
        ResultCollector resultCollector = new ResultCollector("blah", "blah");
        FlaggingCollector flaggingCollector = new FlaggingCollector(new Batch("40000"), null, "0.1-SNAPSHOT", 100);
        //Threshold 0 so this expects perfect linearity
        DefaultTreeEventHandler histogramHandler = new MissingColorsHistogramChecker(
                resultCollector, flaggingCollector,new HistogramCache(), properties);
        AttributeParsingEvent event = createAttributeEvent(
                "B400022028252-RT1/400022028252-08/1795-12-20-01/adresseavisen1759-1795-12-20-01-0079.jp2.histogram.xml",
                HistogramXml.getSampleBadHistogram());
        histogramHandler.handleAttribute(event);
        Assert.assertTrue(flaggingCollector.hasFlags(), flaggingCollector.toReport());
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
