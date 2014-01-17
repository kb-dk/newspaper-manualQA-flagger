package dk.statsbiblioteket.medieplatform.newspaper.manualQA;

import dk.statsbiblioteket.medieplatform.autonomous.ResultCollector;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.common.AttributeParsingEvent;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.eventhandlers.DefaultTreeEventHandler;
import dk.statsbiblioteket.medieplatform.newspaper.manualQA.flagging.FlaggingCollector;
import dk.statsbiblioteket.util.xml.DOM;
import dk.statsbiblioteket.util.xml.XPathSelector;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 * This class handles issues raised by metadata in mix-files which require manual QA intervention. These are typically
 * changes in scanner-related hardware, software, and wetware.
 */
public class MixHandler extends DefaultTreeEventHandler {

    private ResultCollector resultCollector;
    private FlaggingCollector flaggingCollector;
    private Properties properties;
    private XPathSelector mixXpathSelector;

    /**
     * Constructor for this class.
     * @param resultCollector for reporting on errors.
     * @param properties properties used by this class. See Readme.md for the definition of the required properties
     *                   for this class.
     * @param flaggingCollector for reporting of issues requiring manual QA.
     */
    public MixHandler(ResultCollector resultCollector, Properties properties, FlaggingCollector flaggingCollector) {
        this.resultCollector = resultCollector;
        this.properties = properties;
        this.flaggingCollector = flaggingCollector;
        this.mixXpathSelector = DOM.createXPathSelector(
                "avis", "http://www.statsbiblioteket.dk/avisdigitalisering/microfilm/1/0/",
                "mix", "http://www.loc.gov/mix/v20");

    }

    @Override
    public void handleAttribute(AttributeParsingEvent event) {
        if (!event.getName().endsWith(".mix.xml")) {
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

        final String xpathManufacturer = "/mix:mix/mix:ImageCaptureMetadata/mix:ScannerCapture/mix:scannerManufacturer";
        validate(event, doc, ConfigConstants.SCANNER_MANUFACTURERS, xpathManufacturer, "Found a new scanner manufacturer");
        final String xpathModel = "/mix:mix/mix:ImageCaptureMetadata/mix:ScannerCapture/mix:ScannerModel/mix:scannerModelName";
        validate(event, doc, ConfigConstants.SCANNER_MODELS, xpathModel, "Found new scanner model name");
        final String xpathModelNumber = "/mix:mix/mix:ImageCaptureMetadata/mix:ScannerCapture/mix:ScannerModel/mix:scannerModelNumber";
        validate(event, doc, ConfigConstants.SCANNER_MODEL_NUMBERS, xpathModelNumber, "Found new scanner model number");
        final String xpathModelSerialNo = "/mix:mix/mix:ImageCaptureMetadata/mix:ScannerCapture/mix:ScannerModel/mix:scannerModelSerialNo";
        validate(event, doc, ConfigConstants.SCANNER_SERIAL_NOS, xpathModelSerialNo, "Found new scanner serial number");
        validateProducers(event, doc);
        validateSoftwareVersions(event, doc);
    }

    /**
     * Check if a previously-unknown software version has been used to process this scanning.
     * @param event
     * @param doc
     */
    private void validateSoftwareVersions(AttributeParsingEvent event, Document doc) {
        String[] allowedSoftwareVersions = properties.getProperty(ConfigConstants.SCANNER_SOFTWARES).split(",");
        final String xpathSoftwareVersions = "mix:mix/mix:ImageCaptureMetadata/mix:ScannerCapture/mix:ScanningSystemSoftware";
        NodeList softwareVersionNodes = mixXpathSelector.selectNodeList(doc, xpathSoftwareVersions);
        for (int i = 0; i < softwareVersionNodes.getLength(); i++) {
            Node versionNode = softwareVersionNodes.item(i);
            String softwareName = mixXpathSelector.selectString(versionNode, "mix:scanningSoftwareName");
            String softwareVersion = mixXpathSelector.selectString(versionNode, "mix:scanningSoftwareVersionNo");
            String foundSoftwareVersion = softwareName + ";" + softwareVersion;
            if (!Arrays.asList(allowedSoftwareVersions).contains(foundSoftwareVersion)) {
                addFlag(event, "Found new software version: " + foundSoftwareVersion);
            }
        }
    }

    /**
     * Check if a previously unknown producer (e.g. scanner operator) has been involved in this scanning.
     * @param event
     * @param doc
     */
    private void validateProducers(AttributeParsingEvent event, Document doc) {
        String[] allowedProducers = properties.getProperty(ConfigConstants.IMAGE_PRODUCERS).split(",");
        final String xpathProducers = "/mix:mix/mix:ImageCaptureMetadata/mix:GeneralCaptureInformation/mix:imageProducer";
        String[] producers = mixXpathSelector.selectString(doc, xpathProducers).split(";");
        for (String producer: producers) {
            if (!Arrays.asList(allowedProducers).contains(producer)) {
                addFlag(event, "Unknown or new producer found: " + producer);
            }
        }
    }

    /**
     * Generic check used to determine whether the scanning machine (manufacturer, model, model number and serial
     * number) match with previously known machines.
     * @param event
     * @param doc
     * @param property
     * @param xpath
     * @param message
     */
    private void validate(AttributeParsingEvent event, Document doc, String property, String xpath, String message) {
        String[] allowedValues = properties.getProperty(property).split(",");
        String actualValue = mixXpathSelector.selectString(doc, xpath).trim();
        if (!Arrays.asList(allowedValues).contains(actualValue)) {
            addFlag(event, message + ": " + actualValue);
        }
    }


    private void addFlag(AttributeParsingEvent event, String message) {
        flaggingCollector.addFlag(event, "metadata", getClass().getSimpleName(), message);
    }

}
