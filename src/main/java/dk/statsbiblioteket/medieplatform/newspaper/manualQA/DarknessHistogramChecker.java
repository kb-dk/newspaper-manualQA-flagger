package dk.statsbiblioteket.medieplatform.newspaper.manualQA;

import dk.statsbiblioteket.medieplatform.autonomous.Batch;
import dk.statsbiblioteket.medieplatform.autonomous.ResultCollector;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.common.AttributeParsingEvent;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.common.NodeBeginsParsingEvent;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.common.NodeEndParsingEvent;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.eventhandlers.DefaultTreeEventHandler;
import dk.statsbiblioteket.medieplatform.newspaper.manualQA.flagging.FlaggingCollector;
import dk.statsbiblioteket.util.xml.DOM;
import dk.statsbiblioteket.util.xml.XPathSelector;
import org.w3c.dom.Document;

import java.io.IOException;


public class DarknessHistogramChecker extends DefaultTreeEventHandler {
    private final ResultCollector resultCollector;
    private final FlaggingCollector flaggingCollector;
    private Batch batch;
    private static final XPathSelector xpath = DOM.createXPathSelector("alto", "http://www.loc.gov/standards/alto/ns-v2#");
    private long[] histogram;
    private int numberOfTextLinesFromAlto;

    private int numberOfTooDarkImages;
    private int maxNumberOfDarkImagesAllowed;
    private int lowestHistogramIndexNotConsideredBlack;
    private int lowestAcceptablePeakPosition;
    private int minNumberOfTextLines;

    public DarknessHistogramChecker(ResultCollector resultCollector, FlaggingCollector flaggingCollector, Batch batch,
                                    int maxNumberOfDarkImagesAllowed, int lowestHistogramIndexNotConsideredBlack,
                                    int lowestAcceptablePeakPosition, int minNumberOfTextLines) {
        this.resultCollector = resultCollector;
        this.flaggingCollector = flaggingCollector;
        this.batch = batch;
        this.maxNumberOfDarkImagesAllowed = maxNumberOfDarkImagesAllowed;
        this.lowestHistogramIndexNotConsideredBlack = lowestHistogramIndexNotConsideredBlack;
        this.lowestAcceptablePeakPosition = lowestAcceptablePeakPosition;
        this.minNumberOfTextLines = minNumberOfTextLines;
    }


    @Override
    public void handleAttribute(AttributeParsingEvent event) {
        try {
            if (event.getName().endsWith(".edition.xml")) {
                // We have entered a new edition, all page nodes will now follow, before entering the next edition

                // We now expect histogram and alto attributes coming up soon, and both a histogram and its corresponding alto
                // will appear before the next "pair" of these appears
                histogram = null;
                numberOfTextLinesFromAlto = -1;
            }

            if (event.getName().endsWith(".histogram.xml")) {
                histogram = new Histogram(event.getData()).values();
                checkHistogramDataIfReady();
            }

            if (event.getName().endsWith(".alto.xml")) {
                numberOfTextLinesFromAlto = getNumberOfTextLines(event);
                checkHistogramDataIfReady();
            }
        } catch (Exception e) {
            resultCollector.addFailure(event.getName(), "exception", getComponent(), e.getMessage());
        }
    }


    private void checkHistogramDataIfReady() {
        if (histogram != null && numberOfTextLinesFromAlto >= 0) {
            if (histogramIsTooDark(histogram, numberOfTextLinesFromAlto)) {
                numberOfTooDarkImages++;
            }
            // We have consumed the data, so clear them
            histogram = null;
            numberOfTextLinesFromAlto = -1;
        }
    }


    @Override
    public void handleNodeBegin(NodeBeginsParsingEvent event) {
        try {
            if (event.getName().matches("/" + batch.getBatchID() + "-" + "[0-9]{2}$")) {
                // We have now entered a film, so initialize
                numberOfTooDarkImages = 0;
            }
        } catch (Exception e) {
            resultCollector.addFailure(event.getName(), "exception", getComponent(), e.getMessage());
        }
    }


    @Override
    public void handleNodeEnd(NodeEndParsingEvent event) {
        try {
            if (event.getName().matches("/" + batch.getBatchID() + "-" + "[0-9]{2}$")) {
                // We have now left a film, so flag if there were too many dark pages
                if (numberOfTooDarkImages > maxNumberOfDarkImagesAllowed) {
                    flaggingCollector.addFlag(event, "jp2file", getComponent(),
                            "This film has a high number of dark images! " + numberOfTooDarkImages + " images were found that appear"
                                    + " dark. You get this message because the number is higher than " + maxNumberOfDarkImagesAllowed + ".");
                }
            }
        } catch (Exception e) {
            resultCollector.addFailure(event.getName(), "exception", getComponent(), e.getMessage());
        }
    }


    private boolean histogramIsTooDark(long[] histogram, int numberOfTextLinesFromAlto) {
        // Ignore if there is a very small amount of text on the image, for that often means much of the page is covered by
        // pictures, and then the page often is dark - and "legally" so
        if (numberOfTextLinesFromAlto < minNumberOfTextLines) {
            return false;
        }

        // Find highest value that is not considered black
        long highestPeakValueFound = 0;
        int highestPeakPosition = 255;
        for (int i = lowestHistogramIndexNotConsideredBlack; i < 256; i++) {
            if (histogram[i] > highestPeakValueFound) {
                highestPeakValueFound = histogram[i];
                highestPeakPosition = i;
            }
        }

        // If it is too far to the "left" on the histogram, mark as too dark
        if (highestPeakPosition < lowestAcceptablePeakPosition) {
            return true;
        }

        return false;
    }


    public static int getNumberOfTextLines(AttributeParsingEvent event) throws NumberFormatException {
        Document doc;
        try {
            doc = DOM.streamToDOM(event.getData(), true);
            if (doc == null) {
                throw new RuntimeException("Could not parse xml");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        final String numberOfTextLinesXPath="count(alto:alto/alto:Layout/alto:Page/alto:PrintSpace/alto:TextBlock/alto:TextLine)";
        String numberOfTextLinesString = xpath.selectString(doc, numberOfTextLinesXPath);
        return Integer.parseInt(numberOfTextLinesString);
    }


    private String getComponent() {
        return getClass().getName();
    }
}
