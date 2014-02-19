package dk.statsbiblioteket.medieplatform.newspaper.manualQA;

import dk.statsbiblioteket.medieplatform.autonomous.Batch;
import dk.statsbiblioteket.medieplatform.autonomous.ResultCollector;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.common.AttributeParsingEvent;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.common.NodeBeginsParsingEvent;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.common.NodeEndParsingEvent;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.eventhandlers.TreeEventHandler;
import dk.statsbiblioteket.medieplatform.newspaper.manualQA.flagging.FlaggingCollector;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class DarknessHistogramCheckerTest {
    private FlaggingCollector flaggingCollector;
    private ResultCollector resultCollector;
    private int maxNumberOfDarkImagesAllowed;
    private int lowestHistogramIndexNotConsideredBlack;
    private int lowestAcceptablePeakPosition;
    private int minNumberOfTextLines;
    private String batchID;
    private Batch batch;
    private static final String ATTRIBUTE_EVENT_NAME_PREFIX = "B400022028252-RT1/400022028252-08/1795-12-20-01/"
            + "adresseavisen1759-1795-12-20-01";


    @BeforeMethod
    public void initialize() {
        batchID = "40000";
        batch = new Batch(batchID);
        flaggingCollector = new FlaggingCollector(batch, null, "0.1-SNAPSHOT", 100);
        resultCollector = new ResultCollector("blah", "blah");
        maxNumberOfDarkImagesAllowed = 2;
        lowestHistogramIndexNotConsideredBlack = 3;
        lowestAcceptablePeakPosition = 128;
        minNumberOfTextLines = 10;
    }


    @Test
    public void testHandleAttributeGood() throws Exception {
        TreeEventHandler histogramHandler = new DarknessHistogramChecker(resultCollector, flaggingCollector, batch,
                maxNumberOfDarkImagesAllowed, lowestHistogramIndexNotConsideredBlack, lowestAcceptablePeakPosition,
                minNumberOfTextLines);

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

    @Test
    public void testHandleAttributeBad() throws Exception {
        maxNumberOfDarkImagesAllowed = 1;

        TreeEventHandler histogramHandler = new DarknessHistogramChecker(resultCollector, flaggingCollector, batch,
                maxNumberOfDarkImagesAllowed, lowestHistogramIndexNotConsideredBlack, lowestAcceptablePeakPosition,
                minNumberOfTextLines);

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


    @Test
    public void testHandleAttributeGoodBecauseOfAlto() throws Exception {
        maxNumberOfDarkImagesAllowed = 1;

        TreeEventHandler histogramHandler = new DarknessHistogramChecker(resultCollector, flaggingCollector, batch,
                maxNumberOfDarkImagesAllowed, lowestHistogramIndexNotConsideredBlack, lowestAcceptablePeakPosition,
                minNumberOfTextLines);

        // This test should succeed even though there are "too many" dark images - because there are low amounts of text on the
        // pages (according to alto) and so they're disregarded as picture-heavy pages

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
