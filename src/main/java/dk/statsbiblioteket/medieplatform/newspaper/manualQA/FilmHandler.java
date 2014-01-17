package dk.statsbiblioteket.medieplatform.newspaper.manualQA;

import dk.statsbiblioteket.medieplatform.autonomous.ResultCollector;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.common.AttributeParsingEvent;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.eventhandlers.DefaultTreeEventHandler;
import dk.statsbiblioteket.medieplatform.newspaper.manualQA.flagging.FlaggingCollector;
import dk.statsbiblioteket.util.xml.DOM;
import dk.statsbiblioteket.util.xml.XPathSelector;
import org.w3c.dom.Document;

import java.io.IOException;

/**
 * This class flags issues with film.xml containing unexpected, but not necessarily erroneous values.
 */
public class FilmHandler extends DefaultTreeEventHandler {

    private XPathSelector filmXPathSelector;
    private ResultCollector resultCollector;
    private FlaggingCollector flaggingCollector;

    /**
     * Constructor for this class.
     * @param resultCollector the ResultCollector for reporting failures.
     * @param flaggingCollector the FlaggingCollector for reporting issues requiring manual QA.
     */
    public FilmHandler(ResultCollector resultCollector, FlaggingCollector flaggingCollector) {
        this.resultCollector = resultCollector;
        this.flaggingCollector = flaggingCollector;
        filmXPathSelector = DOM.createXPathSelector("avis",
                "http://www.statsbiblioteket.dk/avisdigitalisering/microfilm/1/0/");
    }

    /**
     * This class tests two specific requirements which can trigger manual QA:
     * i) the captureOriginalResolution should not normally be higher than 400. Anything above this causes a flag to be raised.
     * ii) the reductionRatio should normally not be higher than 19. between 19.1 and 25 raises a flag. (Anything higher
     * than 25 is marked as an error in newspaper-batch-metadata-checker.)
     * @param event
     */
    @Override
    public void handleAttribute(AttributeParsingEvent event) {
        if (!event.getName().endsWith("film.xml")) {
            return;
        }
        Document doc;
        try {
            doc = DOM.streamToDOM(event.getData(), true);
            if (doc == null) {
                resultCollector.addFailure(
                        event.getName(), "exception", getClass().getSimpleName(),
                        "Could not parse xml");
                return;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        doValidate(event, doc);


    }

    private void doValidate(AttributeParsingEvent event, Document doc) {
        validateReductionRatio(event, doc);
        validateOriginalResolution(event, doc);
    }

    private void validateOriginalResolution(AttributeParsingEvent event, Document doc) {
        String originalNewspaperResolutionString = filmXPathSelector.selectString(doc, "/avis:reelMetadata/avis:captureResolutionOriginal");
        try {
            int resolution = Integer.parseInt(originalNewspaperResolutionString);
            if (resolution > 400) {
                flaggingCollector.addFlag(event, "metadata", getClass().getSimpleName(), "2E-7: captureOriginalResolution is expected " +
                        "to be no more than 400 pixels/inch, not '" + resolution + "'");
            }
        } catch (NumberFormatException e) {
            resultCollector.addFailure(event.getName(), "metadata", getClass().getSimpleName(), "2E-7: originalNewspaperResolution should be" +
                    " an integer, not '" + originalNewspaperResolutionString + "'" );
        }
    }

    private void validateReductionRatio(AttributeParsingEvent event, Document doc) {
        String reductionRatioString = filmXPathSelector.selectString(doc, "/avis:reelMetadata/avis:reductionRatio");
        try {
            Double reductionRatio = Double.parseDouble(reductionRatioString);
            if (reductionRatio >= 19.1 && reductionRatio <= 25) {
                flaggingCollector.addFlag(event, "metadata", getClass().getSimpleName(), "2E-6: reductionRatio expected to" +
                        " be 19 or less. Actual value is " + reductionRatio );
            }
        } catch (NumberFormatException e) {
            resultCollector.addFailure(event.getName(), "metadata", getClass().getSimpleName(), "2E-6: reductionRatio should be" +
                    " a number, not '" + reductionRatioString + "'" );
        }
    }


}
