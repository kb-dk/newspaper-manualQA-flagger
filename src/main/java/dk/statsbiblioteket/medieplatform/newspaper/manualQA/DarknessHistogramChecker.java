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
import dk.statsbiblioteket.util.xml.DOM;
import dk.statsbiblioteket.util.xml.XPathSelector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import java.io.IOException;
import java.util.Properties;


/**
 * This checker flags films in which there are too many dark images.
 */
public class DarknessHistogramChecker extends DefaultTreeEventHandler {

    static Logger log = LoggerFactory.getLogger(DarknessHistogramChecker.class);

    private final ResultCollector resultCollector;
    private final FlaggingCollector flaggingCollector;
    private final HistogramCache histogramCache;
    private Batch batch;
    private final AltoCache altoCache;
    private static final XPathSelector xpath = DOM.createXPathSelector("alto", "http://www.loc.gov/standards/alto/ns-v2#");
    private long[] histogram;
    private int numberOfTextLinesFromAlto;

    private int numberOfTooDarkImages;
    private int maxNumberOfDarkImagesAllowed;
    private int lowestHistogramIndexNotConsideredBlack;
    private int lowestAcceptablePeakPosition;
    private int minNumberOfTextLines;

    public DarknessHistogramChecker(ResultCollector resultCollector, FlaggingCollector flaggingCollector, HistogramCache histogramCache,Batch batch,
                                    AltoCache altoCache, Properties properties) {
        log.debug("Enabling {}",getClass().getName());
        this.resultCollector = resultCollector;
        this.flaggingCollector = flaggingCollector;
        this.histogramCache = histogramCache;
        this.batch = batch;
        this.altoCache = altoCache;
        this.maxNumberOfDarkImagesAllowed = Integer.parseInt(properties.getProperty(
                ConfigConstants.DARKNESS_MAX_NUM_OF_DARK_IMAGES_ALLOWED));
        this.lowestHistogramIndexNotConsideredBlack = Integer.parseInt(properties.getProperty(
                        ConfigConstants.FLAG_IGNORE_COLORS_BELOW));
        this.lowestAcceptablePeakPosition = Integer.parseInt(properties.getProperty(
                ConfigConstants.DARKNESS_LOWEST_ACCEPTABLE_PEAK_POSITION));
        this.minNumberOfTextLines = Integer.parseInt(properties.getProperty(
                ConfigConstants.DARKNESS_MIN_NUM_OF_TEXT_LINES));
    }


    /**
     * Handles attributes edition, histogram, and alto. On a histogram or alto, picks up the needed data for the histogram or alto
     * file. When both have been collected, the checking will be done. On an edition, data are cleared in preparation for the next
     * pick ups and check.
     *
     * @param event The attribute event that will be handled
     */
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
                histogram = histogramCache.getHistogram(event).values();
                checkHistogramDataIfReady();
            }

            if (event.getName().endsWith(".alto.xml")) {
                numberOfTextLinesFromAlto = getNumberOfTextLines(event);
                checkHistogramDataIfReady();
            }
        } catch (Exception e) {
            log.error("Caught Exception",e);
            resultCollector.addFailure(event.getName(), "exception", getComponent(), e.toString());
        }
    }


    private void checkHistogramDataIfReady() {
        if (histogram != null && numberOfTextLinesFromAlto >= 0) {
            if (histogramIsTooDark()) {
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
            log.error("Caught Exception", e);
            resultCollector.addFailure(event.getName(), "exception", getComponent(), e.toString());
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
            log.error("Caught Exception", e);
            resultCollector.addFailure(event.getName(), "exception", getComponent(), e.toString());
        }
    }


    private boolean histogramIsTooDark() {
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


    public  int getNumberOfTextLines(AttributeParsingEvent event) throws NumberFormatException {
        Document doc;
        try {
            doc = altoCache.getAlto(event);
            if (doc == null) {
                throw new RuntimeException("Could not parse xml");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
/*
        final String numberOfTextLinesXPath
                = "count(alto:alto/alto:Layout/alto:Page/alto:PrintSpace/alto:TextBlock/alto:TextLine)";
        String numberOfTextLinesString = xpath.selectString(doc, numberOfTextLinesXPath);
        return Integer.parseInt(numberOfTextLinesString);
*/

        final String numberOfTextLinesXPath
                = "alto:alto/alto:Layout/alto:Page/alto:PrintSpace/alto:TextBlock/alto:TextLine";
        NodeList nodeList = xpath.selectNodeList(doc, numberOfTextLinesXPath);
        return nodeList.getLength();
    }


    private String getComponent() {
        return getClass().getName();
    }
}
