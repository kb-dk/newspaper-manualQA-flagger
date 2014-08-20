package dk.statsbiblioteket.medieplatform.newspaper.manualQA;

import dk.statsbiblioteket.medieplatform.autonomous.Batch;
import dk.statsbiblioteket.medieplatform.autonomous.ResultCollector;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.common.AttributeParsingEvent;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.common.NodeBeginsParsingEvent;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.common.NodeEndParsingEvent;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.eventhandlers.DefaultTreeEventHandler;
import dk.statsbiblioteket.medieplatform.newspaper.manualQA.caches.AltoCache;
import dk.statsbiblioteket.medieplatform.newspaper.manualQA.caches.HistogramCache;
import dk.statsbiblioteket.medieplatform.newspaper.manualQA.flagging.FlaggingCollector;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class DarknessHistogramCheckerTest {
    private FlaggingCollector flaggingCollector;
    private ResultCollector resultCollector;
    private String batchID;
    private Batch batch;
    private static final String ATTRIBUTE_EVENT_NAME_PREFIX = "B400022028252-RT1/400022028252-08/1795-12-20-01/"
            + "adresseavisen1759-1795-12-20-01";

    Properties properties;

    @BeforeMethod
    public void setUp() {
        properties = new Properties();
        properties.setProperty(ConfigConstants.DARKNESS_MAX_NUM_OF_DARK_IMAGES_ALLOWED, "2");
        properties.setProperty(ConfigConstants.FLAG_IGNORE_COLORS_BELOW, "3");
        properties.setProperty(ConfigConstants.DARKNESS_LOWEST_ACCEPTABLE_PEAK_POSITION, "128");
        properties.setProperty(ConfigConstants.DARKNESS_MIN_NUM_OF_TEXT_LINES, "10");
    }


    @BeforeMethod
    public void initialize() {
        batchID = "40000";
        batch = new Batch(batchID);
        flaggingCollector = new FlaggingCollector(batch, null, "0.1-SNAPSHOT", 100);
        resultCollector = new ResultCollector("blah", "blah");
    }


    /**
     * Test that darkness checker produces no flags when presented with only an allowed amount of too dark images.
     */
    @Test
    public void testHandleAttributeGood() throws Exception {
        DefaultTreeEventHandler histogramHandler = new DarknessHistogramChecker(resultCollector, flaggingCollector,new HistogramCache(), batch, new AltoCache(),
                properties);

        histogramHandler.handleNodeBegin(createNodeBeginsParsingEvent("/" + batchID + "-99"));

        histogramHandler.handleAttribute(createAttributeEvent(ATTRIBUTE_EVENT_NAME_PREFIX + ".edition.xml", ""));

        histogramHandler.handleAttribute(createAttributeEvent(ATTRIBUTE_EVENT_NAME_PREFIX + "-0079.jp2.histogram.xml",
                HistogramXml.getSampleGoodHistogram()));

        histogramHandler.handleAttribute(createAttributeEvent(ATTRIBUTE_EVENT_NAME_PREFIX + "-0079.jp2.alto.xml",
                HistogramXml.getSampleAltoHiText()));

        histogramHandler.handleAttribute(createAttributeEvent(ATTRIBUTE_EVENT_NAME_PREFIX + "-0080.jp2.histogram.xml",
                HistogramXml.getSampleBadDarknessHistogram()));

        histogramHandler.handleAttribute(createAttributeEvent(ATTRIBUTE_EVENT_NAME_PREFIX + "-0080.jp2.alto.xml",
                HistogramXml.getSampleAltoHiText()));

        histogramHandler.handleAttribute(createAttributeEvent(ATTRIBUTE_EVENT_NAME_PREFIX + "-0081.jp2.histogram.xml",
                HistogramXml.getSampleBadDarknessHistogram()));

        histogramHandler.handleAttribute(createAttributeEvent(ATTRIBUTE_EVENT_NAME_PREFIX + "-0081.jp2.alto.xml",
                HistogramXml.getSampleAltoHiText()));

        histogramHandler.handleNodeEnd(createNodeEndParsingEvent("/" + batchID + "-99"));

        Assert.assertFalse(flaggingCollector.hasFlags(), flaggingCollector.toReport());
    }


    /**
     * Test that darkness checker produces flags there are more "too dark" images than allowed.
     */
    @Test
    public void testHandleAttributeBad() throws Exception {
        properties.setProperty(ConfigConstants.DARKNESS_MAX_NUM_OF_DARK_IMAGES_ALLOWED, "1");

        DefaultTreeEventHandler histogramHandler = new DarknessHistogramChecker(resultCollector, flaggingCollector, new HistogramCache(),batch, new AltoCache(),
                properties);

        histogramHandler.handleNodeBegin(createNodeBeginsParsingEvent("/" + batchID + "-99"));

        histogramHandler.handleAttribute(createAttributeEvent(ATTRIBUTE_EVENT_NAME_PREFIX + ".edition.xml", ""));

        histogramHandler.handleAttribute(createAttributeEvent(ATTRIBUTE_EVENT_NAME_PREFIX + "-0079.jp2.histogram.xml",
                HistogramXml.getSampleBadDarknessHistogram()));

        histogramHandler.handleAttribute(createAttributeEvent(ATTRIBUTE_EVENT_NAME_PREFIX + "-0079.jp2.alto.xml",
                HistogramXml.getSampleAltoHiText()));

        histogramHandler.handleAttribute(createAttributeEvent(ATTRIBUTE_EVENT_NAME_PREFIX + "-0080.jp2.histogram.xml",
                HistogramXml.getSampleBadDarknessHistogram()));

        histogramHandler.handleAttribute(createAttributeEvent(ATTRIBUTE_EVENT_NAME_PREFIX + "-0080.jp2.alto.xml",
                HistogramXml.getSampleAltoHiText()));

        histogramHandler.handleNodeEnd(createNodeEndParsingEvent("/" + batchID + "-99"));

        Assert.assertTrue(flaggingCollector.hasFlags(), flaggingCollector.toReport());
    }


    /**
     * Test that darkness checker produces no flags in the situation where, though there are "too many" dark images, there are
     * also low amounts of text on the pages (according to alto) and therefore the dark images are disregarded as picture-heavy
     * pages.
     */
    @Test
    public void testHandleAttributeGoodBecauseOfAlto() throws Exception {
        properties.setProperty(ConfigConstants.DARKNESS_MAX_NUM_OF_DARK_IMAGES_ALLOWED, "1");

        DefaultTreeEventHandler histogramHandler = new DarknessHistogramChecker(resultCollector, flaggingCollector,new HistogramCache(), batch, new AltoCache(),
                properties);

        histogramHandler.handleNodeBegin(createNodeBeginsParsingEvent("/" + batchID + "-99"));

        histogramHandler.handleAttribute(createAttributeEvent(ATTRIBUTE_EVENT_NAME_PREFIX + ".edition.xml", ""));

        histogramHandler.handleAttribute(createAttributeEvent(ATTRIBUTE_EVENT_NAME_PREFIX + "-0079.jp2.histogram.xml",
                HistogramXml.getSampleBadDarknessHistogram()));

        histogramHandler.handleAttribute(createAttributeEvent(ATTRIBUTE_EVENT_NAME_PREFIX + "-0079.jp2.alto.xml",
                HistogramXml.getSampleAltoLoText()));

        histogramHandler.handleAttribute(createAttributeEvent(ATTRIBUTE_EVENT_NAME_PREFIX + "-0080.jp2.histogram.xml",
                HistogramXml.getSampleBadDarknessHistogram()));

        histogramHandler.handleAttribute(createAttributeEvent(ATTRIBUTE_EVENT_NAME_PREFIX + "-0080.jp2.alto.xml",
                HistogramXml.getSampleAltoLoText()));

        histogramHandler.handleNodeEnd(createNodeEndParsingEvent("/" + batchID + "-99"));

        Assert.assertFalse(flaggingCollector.hasFlags(), flaggingCollector.toReport());
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


    private NodeBeginsParsingEvent createNodeBeginsParsingEvent(String name) {
        return new NodeBeginsParsingEvent(name);
    }


    private NodeEndParsingEvent createNodeEndParsingEvent(String name) {
        return new NodeEndParsingEvent(name);
    }
}
