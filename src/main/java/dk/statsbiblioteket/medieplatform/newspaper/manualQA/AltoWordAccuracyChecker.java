package dk.statsbiblioteket.medieplatform.newspaper.manualQA;

import dk.statsbiblioteket.medieplatform.autonomous.ResultCollector;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.common.AttributeParsingEvent;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.common.NodeEndParsingEvent;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.eventhandlers.DefaultTreeEventHandler;
import dk.statsbiblioteket.medieplatform.newspaper.manualQA.flagging.FlaggingCollector;
import dk.statsbiblioteket.util.xml.DOM;
import dk.statsbiblioteket.util.xml.XPathSelector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * This handler operates at the film-level and computes running averages of the ABBYY Predicted Accuracy for both
 * the whole film and each edition on the film.
 */
public class AltoWordAccuracyChecker extends DefaultTreeEventHandler {

    static Logger log = LoggerFactory.getLogger(AltoWordAccuracyChecker.class);

    ResultCollector resultCollector;
    FlaggingCollector flaggingCollector;

    Double minimumAcceptable;
    boolean ignoreZero;
    Double minimumAcceptablePerfile;

    /**
     * Map from an eventId to the running average accuracy of all alto files within the corresponding directory.
     */
    Map<String, RunningAverage> averages;
    private static final XPathSelector xpath = DOM.createXPathSelector("alto", "http://www.loc.gov/standards/alto/ns-v2#");

    public AltoWordAccuracyChecker(ResultCollector resultCollector, FlaggingCollector flaggingCollector, Properties properties) {
        this.resultCollector = resultCollector;
        this.flaggingCollector = flaggingCollector;
        averages = new HashMap<String, RunningAverage>();
        minimumAcceptable = Double.parseDouble(properties.getProperty(ConfigConstants.MINIMUM_ALTO_AVERAGE_ACCURACY));
        minimumAcceptablePerfile = Double.parseDouble(properties.getProperty(ConfigConstants.MINIMUM_ALTO_PERFILE_ACCURACY));
        ignoreZero = Boolean.parseBoolean(properties.getProperty(ConfigConstants.ALTO_IGNORE_ZERO_ACCURACY));
    }

    public static class RunningAverage {
        private Double currentValue;
        private long count;

        public RunningAverage() {
            currentValue = 0.0;
            count = 0L;
        }

        public void addValue(double value){
            currentValue = (count*currentValue + value)/(count + 1);
            count++;
        }

        public Double getCurrentValue() {
            return currentValue;
        }
    }

    @Override
    public void handleAttribute(AttributeParsingEvent event) {
        if (!event.getName().endsWith("alto.xml")) {
            return;
        }
        Double accuracy = null;
        try {
            accuracy = readAccuracy(event);
        } catch (NumberFormatException e) {
            flaggingCollector.addFlag(event, "metadata", getClass().getSimpleName(), "No ACCURACY attribute found in alto:Page" +
                    " element in this file.");
            return;
        }

        if (accuracy < minimumAcceptablePerfile && (!ignoreZero || accuracy > 0)) {
            flaggingCollector.addFlag(event, "metadata", getClass().getSimpleName(), "Accuracy for alto file is less " +
                    "than the prescribed minimum (" + minimumAcceptablePerfile + ") :" + accuracy);
        }

        String editionId = event.getName().substring(0, event.getName().lastIndexOf('/'));
        String filmId = editionId.substring(0, editionId.lastIndexOf('/'));

        //These initialisations could equivalently be made in the handleNodeBegin method for the given edition and film
        //nodes. Putting them here, in the first alto attribute for each node, requires less work because we don't need
        //to identify edition and film nodes by regexps. They're just the parent and grandparent of the current node.
        if (averages.get(editionId) == null) {
            averages.put(editionId, new RunningAverage());
        }
        if (averages.get(filmId) == null) {
            averages.put(filmId, new RunningAverage());
        }
        if (accuracy > 0 || !ignoreZero) {
            averages.get(editionId).addValue(accuracy);
            averages.get(filmId).addValue(accuracy);
        }
    }


    @Override
    public void handleNodeEnd(NodeEndParsingEvent event) {
        String eventName = event.getName();
        RunningAverage runningAverage = averages.get(eventName);
        if (runningAverage == null) {
            //this happens if we are at the end of any node other than a film or edition node.
            return;
        }
        // Strip '/' from end if any
        if (eventName.lastIndexOf("/") == eventName.length() - 1) {
            eventName = eventName.substring(0, eventName.length() - 2);
        }
        String lastElement = eventName.substring(eventName.lastIndexOf("/") + 1);
        String type;
        if (lastElement.matches("^[0-9]{4}-.*$")) {
            type = "Edition";
        } else {
            type = "Film";
        }
        log.debug("Average accuracy for the " + type + " " + event.getName() + " is " + runningAverage.getCurrentValue() + " from (" + runningAverage.count + ")");
        if (runningAverage.getCurrentValue() < minimumAcceptable) {
            flaggingCollector.addFlag(event, "metadata", getClass().getSimpleName(), "Average OCR accuracy for this " + type + " is less" +
                    " than the minimum expected (" + minimumAcceptable + ") : " + runningAverage.getCurrentValue());
        }
        //Remove the event from the map so it can be garbage collected.
        averages.remove(event.getName());
    }

    public static Double readAccuracy(AttributeParsingEvent event) throws NumberFormatException {
        Document doc;
        try {
            doc = streamToDOM(event.getData(), false);
            if (doc == null) {
                throw new RuntimeException("Could not parse xml");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        final String accuracyXPath="/alto/Layout/Page/@ACCURACY";
        String accuracyString = xpath.selectString(doc, accuracyXPath);
        return Double.parseDouble(accuracyString);
    }

    /**
     * Parses a XML document from a stream to a DOM or return
     * {@code null} on error.
     *
     * @param xmlStream      a stream containing an XML document.
     * @param namespaceAware if {@code true} the constructed DOM will reflect
     *                       the namespaces declared in the XML document
     *
     * @return The document in a DOM or {@code null} in case of errors
     */
    public static Document streamToDOM(InputStream xmlStream, boolean namespaceAware) {
        try {
            DocumentBuilderFactory dbFact = DocumentBuilderFactory.newInstance();
            dbFact.setNamespaceAware(namespaceAware);
            dbFact.setValidating(false);
            dbFact.setFeature("http://xml.org/sax/features/namespaces", false);
            dbFact.setFeature("http://xml.org/sax/features/validation", false);
            dbFact.setFeature("http://apache.org/xml/features/nonvalidating/load-dtd-grammar", false);
            dbFact.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
            return dbFact.newDocumentBuilder().parse(xmlStream);
        } catch (IOException e) {
            log.warn("I/O error when parsing stream :" + e.getMessage(), e);
        } catch (SAXException e) {
            log.warn("Parse error when parsing stream :" + e.getMessage(), e);
        } catch (ParserConfigurationException e) {
            log.warn(
                    "Parser configuration error when parsing XML stream: " + e.getMessage(), e);
        }
        return null;
    }

}
