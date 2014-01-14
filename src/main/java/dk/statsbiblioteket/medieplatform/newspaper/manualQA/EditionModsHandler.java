package dk.statsbiblioteket.medieplatform.newspaper.manualQA;

import dk.statsbiblioteket.medieplatform.autonomous.Batch;
import dk.statsbiblioteket.medieplatform.autonomous.ResultCollector;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.common.AttributeParsingEvent;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.eventhandlers.DefaultTreeEventHandler;
import dk.statsbiblioteket.medieplatform.newspaper.manualQA.flagging.FlaggingCollector;
import dk.statsbiblioteket.util.xml.DOM;
import dk.statsbiblioteket.util.xml.XPathSelector;
import org.w3c.dom.Document;

import java.io.IOException;

/**
 * Performs the necessary checks for any worrying issues in the editon-mods file.
 */
public class EditionModsHandler extends DefaultTreeEventHandler {

    private ResultCollector resultCollector;
    private FlaggingCollector flaggingCollector;
    private Batch batch;

    /**
     * The maximum number of editions of a newspaper per day before we raise a flag.
     */
    private int maxEditionsPerDay = 3;

    /**
     * Constructor for this class.
     * @param resultCollector the result collector.
     * @param flaggingCollector the flagging collector.
     * @param batch  the batch being analysed.
     */
    public EditionModsHandler(ResultCollector resultCollector, FlaggingCollector flaggingCollector, Batch batch) {
        this.resultCollector = resultCollector;
        this.batch = batch;
        this.flaggingCollector = flaggingCollector;
    }

    @Override
    public void handleAttribute(AttributeParsingEvent event) {
        if (event.getName().endsWith(".edition.xml")) {
            doValidate(event);
        }
    }

    /**
     * The only relevant issue is the edition number. If this is higher than three then the issue is flagged for
     * manual QA.
     * @param event the event to be validated.
     */
    private void doValidate(AttributeParsingEvent event) {
        XPathSelector xpath = DOM.createXPathSelector("mods", "http://www.loc.gov/mods/v3");
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
        final String xpathForEditionNumberXPath =
                "/mods:mods/mods:relatedItem[@type='host']/mods:part/mods:detail[@type='edition']/mods:number";
        String xpathForEditionNumber = xpath.selectString(doc, xpathForEditionNumberXPath);
        try {
            int editionNumber = Integer.parseInt(xpathForEditionNumber);
            if (editionNumber > maxEditionsPerDay) {
              flaggingCollector.addFlag(event, "metadata", getClass().getSimpleName(), "2D-9: Edition number is larger than the maximum " +
                      "expected value (" + maxEditionsPerDay + "): " + editionNumber);
            }
        } catch (NumberFormatException nfe) {
            addFailure(event, "2D-9: Unable to parse '" + xpathForEditionNumber + "' as a number");
        }
    }

    private void addFailure(AttributeParsingEvent event, String description) {
         resultCollector.addFailure(
                 event.getName(), "metadata", getClass().getSimpleName(), description);
     }

}
