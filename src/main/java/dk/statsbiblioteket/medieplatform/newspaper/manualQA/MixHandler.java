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
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * This class handles issues raised by metadata in mix-files which require manual QA intervention.
 * These are typically changes in scanner-related hardware, software, and wetware.
 */
public class MixHandler extends DefaultTreeEventHandler {
    private ResultCollector resultCollector;
    private FlaggingCollector flaggingCollector;
    private Properties properties;
    private XPathSelector mixXpathSelector;

    private static class FlaggableElement {
        AttributeParsingEvent firstOccurrence;
        String newName;
        String message;
        int count;

        private FlaggableElement(AttributeParsingEvent firstOccurrence, String newName, String message) {
            this.newName = newName;
            this.message = message;
            this.firstOccurrence = firstOccurrence;
            count = 1;
        }

        public void incrementCount() {
            count++;
        }

        @Override
        public String toString() {
            return message + newName + ". This occurs " + count + " time(s).";
        }
    }

    private Map<String, FlaggableElement> newManufacturers;
    private Map<String, FlaggableElement> newModels;
    private Map<String, FlaggableElement> newModelNumbers;
    private Map<String, FlaggableElement> newSerialNumbers;
    private Map<String, FlaggableElement> newSoftwares;
    private Map<String, FlaggableElement> newProducers;

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
        newManufacturers = new HashMap<>();
        newModels = new HashMap<>();
        newModelNumbers = new HashMap<>();
        newSerialNumbers = new HashMap<>();
        newSoftwares = new HashMap<>();
        newProducers = new HashMap<>();

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
        validate(event, doc, ConfigConstants.SCANNER_MANUFACTURERS, xpathManufacturer, "2K-17: The name of the Scanner Manufacturer is not" +
                " one of those in the list of known manufacturers. This may mean that a new scanner has been deployed or that the" +
                " designation of the scanner in the metadata has been changed. New manufacturer: ");
        final String xpathModel = "/mix:mix/mix:ImageCaptureMetadata/mix:ScannerCapture/mix:ScannerModel/mix:scannerModelName";
        validate(event, doc, ConfigConstants.SCANNER_MODELS, xpathModel, "2K-18: The name of the Scanner Model is not" +
                " one of those on the list of known models. This may mean that a new model has been deployed or that the" +
                " designation of the scanner in the metadata has been changed. New model: ");
        final String xpathModelNumber
                = "/mix:mix/mix:ImageCaptureMetadata/mix:ScannerCapture/mix:ScannerModel/mix:scannerModelNumber";
        validate(event, doc, ConfigConstants.SCANNER_MODEL_NUMBERS, xpathModelNumber, "2K-19: The model-number for the scanner " +
                "is not one of those in the list of known model numbers. This may mean that a new scanner model has been " +
                "deployed or that the designation of the scanner in the metadata has changed. New mode number: ");
        final String xpathModelSerialNo
                = "/mix:mix/mix:ImageCaptureMetadata/mix:ScannerCapture/mix:ScannerModel/mix:scannerModelSerialNo";
        validate(event, doc, ConfigConstants.SCANNER_SERIAL_NOS, xpathModelSerialNo, "2K-20: The serial number for the scanner" +
                " is not one of those in the list of known serial numbers. This may mean that a new scanner has been deployed" +
                " and therefore that scans generated from it should be carefully checked to ensure that it is set up" +
                " as required. New Serial Number: ");
        validateProducers(event, doc);
        validateSoftwareVersions(event, doc);
        if (!event.getName().endsWith("-brik.mix.xml")) {
            validateDimensions(event, doc);
        }
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
                String message = "2K-20, 2K-21: The metadata refers to a version of the software used in the scanning process which" +
                        " is not among the known software. This may mean that Ninestars has deployed new or updated software. The new" +
                        " software is: ";
                addOrIncrement(newSoftwares, event, message, foundSoftwareVersion);
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
                String message = "2K-16: The list of producers in the metadata contains a previously unknown name, possibly a new" +
                        " scanner-operator. The scans generated should be carefully checked to ensure that they" +
                        " are satisfactory. The new producer is: ";
                addOrIncrement(newProducers, event, message, producer);
            }
        }
    }

    /**
     * Check that image dimensions fall within the allowed ranges.
     * @param event
     * @param doc
     */
    private void validateDimensions(AttributeParsingEvent event, Document doc) {
        int minImageWidth, maxImageWidth, minImageHeight, maxImageHeight, width, height;
        try {
            minImageWidth = Integer.parseInt(properties.getProperty(ConfigConstants.MIN_IMAGE_WIDTH));
        } catch (NumberFormatException e) {
            addFlag(event, "Internal error: couldn't get and parse MIN_IMAGE_WIDTH. " + e.getMessage());
            return;
        }
        try {
            maxImageWidth = Integer.parseInt(properties.getProperty(ConfigConstants.MAX_IMAGE_WIDTH));
        } catch (NumberFormatException e) {
            addFlag(event, "Internal error: couldn't get and parse MAX_IMAGE_WIDTH. " + e.getMessage());
            return;
        }
        try {
            minImageHeight = Integer.parseInt(properties.getProperty(ConfigConstants.MIN_IMAGE_HEIGHT));
        } catch (NumberFormatException e) {
            addFlag(event, "Internal error: couldn't get and parse MIN_IMAGE_HEIGHT. " + e.getMessage());
            return;
        }
        try {
            maxImageHeight = Integer.parseInt(properties.getProperty(ConfigConstants.MAX_IMAGE_HEIGHT));
        } catch (NumberFormatException e) {
            addFlag(event, "Internal error: couldn't get and parse MAX_IMAGE_HEIGHT. " + e.getMessage());
            return;
        }
        final String xpathWidth = "/mix:mix/mix:BasicImageInformation/mix:BasicImageCharacteristics/mix:imageWidth";
        final String xpathHeight = "/mix:mix/mix:BasicImageInformation/mix:BasicImageCharacteristics/mix:imageHeight";
        try {
            width = Integer.parseInt(mixXpathSelector.selectString(doc, xpathWidth));
        } catch (NumberFormatException e) {
            addFlag(event, "Couldn't get and parse xpathWidth. " + e.getMessage());
            return;
        }
        try {
            height = Integer.parseInt(mixXpathSelector.selectString(doc, xpathHeight));
        } catch (NumberFormatException e) {
            addFlag(event, "Couldn't get and parse xpathHeight. " + e.getMessage());
            return;
        }

        if ((minImageWidth> width) || (width > maxImageWidth)) {
            addFlag(event, "Image width should have a value from " + minImageWidth + " to " + maxImageWidth
                    + " but was found " + " to be: " + width);
        }

        if ((minImageHeight > height) || (height > maxImageHeight)) {
            addFlag(event, "Image height should have a value from " + minImageHeight + " to " + maxImageHeight
                    + " but was found " + " to be: " + height);
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
            switch (property) {
                case ConfigConstants.SCANNER_MANUFACTURERS :
                    addOrIncrement(newManufacturers, event, message, actualValue);
                    break;
                case ConfigConstants.SCANNER_MODELS :
                    addOrIncrement(newModels, event, message, actualValue);
                    break;
                case ConfigConstants.SCANNER_MODEL_NUMBERS :
                    addOrIncrement(newModelNumbers, event, message, actualValue);
                    break;
                case ConfigConstants.SCANNER_SERIAL_NOS :
                    addOrIncrement(newSerialNumbers, event, message, actualValue);
                    break;
                default:
                    break;
            }
        }
    }

    private void addOrIncrement(Map<String, FlaggableElement> flaggableElementMap, AttributeParsingEvent event, String message, String actualValue) {
        if (flaggableElementMap.containsKey(actualValue)) {
            flaggableElementMap.get(actualValue).incrementCount();
        } else {
            FlaggableElement newManufacturer = new FlaggableElement(event, actualValue, message);
            flaggableElementMap.put(actualValue, newManufacturer);
        }
    }

    private void addFlag(AttributeParsingEvent event, String message) {
        flaggingCollector.addFlag(event, "metadata", getClass().getSimpleName(), message);
    }

    @Override
    public void handleFinish() {
        for (FlaggableElement flaggableElement: newManufacturers.values()) {
            addFlag(flaggableElement.firstOccurrence, flaggableElement.toString());
        }
        for (FlaggableElement flaggableElement: newProducers.values()) {
            addFlag(flaggableElement.firstOccurrence, flaggableElement.toString());
        }
        for (FlaggableElement flaggableElement: newModels.values()) {
            addFlag(flaggableElement.firstOccurrence, flaggableElement.toString());
        }
        for (FlaggableElement flaggableElement: newModelNumbers.values()) {
            addFlag(flaggableElement.firstOccurrence, flaggableElement.toString());
        }
        for (FlaggableElement flaggableElement: newSerialNumbers.values()) {
            addFlag(flaggableElement.firstOccurrence, flaggableElement.toString());
        }
        for (FlaggableElement flaggableElement: newSoftwares.values()) {
            addFlag(flaggableElement.firstOccurrence, flaggableElement.toString());
        }

    }
}
