package dk.statsbiblioteket.medieplatform.newspaper.manualQA.flagging;

import dk.statsbiblioteket.medieplatform.autonomous.iterator.common.AttributeParsingEvent;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.common.DataFileNodeBeginsParsingEvent;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.common.DataFileNodeEndsParsingEvent;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.common.ParsingEvent;
import dk.statsbiblioteket.util.xml.DOM;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * This class is meant to translate from arbitrary parsing events to the jp2 file reference
 *
 * The list of parsing events that is handled is thus
 * 1. DataFileNodes (ie. nodes denoting the virtual data file folder)
 * 2. jp2 content attribute nodes (ie. nodes denoting the content of the jp2 file)
 * 3. xml attribute nodes in a page node (ie. the mix, mods and so on xml files)
 * 4. Page nodes (ie. pages in an edition)
 * 5. edition.xml attribute nodes (should map to the first (by sequence numbers) jp2 file in the edition)
 * 6. Edition nodes, ie. the edition folder. See above
 * 7. Film.xml attribute nodes (should map to the first jp2 (by sequence numbers) in FILM-ISO-target, or if none found,
 * the first jp2 on the film)
 * 8. Film nodes, ie. the film folder. See above
 * 9. Batch nodes. TODO
 * 10. Unmatched folder. This is an edition node.
 * 11. Unmatched image folder. This is a page node
 * 12. Unmatched image xml. This is as above
 * 13. Unmatched image jp2. Obvious
 * 14. Unmatched image jp2 contents. Obvious
 * 15. FILM-ISO-Target. Treated as as the unmatched folder
 * 16. Workshift ISO Target. Treated as an edition node.
 */
public class FlaggingFinder {

    /**
     * The regexp to recognize if a path denotes a film node
     */
    private static final String FILM_REGEXP = "^B(\\d{12})-RT\\d+/\\1-\\d+$";

    /**
     * The regexp to recognize if a path denotes a workshift iso target node
     */
    private static final String WORKSHIFT_ISO_REGEXP = "^B(\\d{12})-RT\\d+/WORKSHIFT-ISO-TARGET$";

    /**
     * The regexp to recognize if a path denotes a virtual workshift iso scan node
     */
    private static final String WORKSHIFT_ISO_SCAN_REGEXP
            = "^B(\\d{12})-RT\\d+/WORKSHIFT-ISO-TARGET/Target-\\d{6}-\\d{4}$";


    /*
    The regexp to recognize if a path denote an edition node
     */
    private static final String EDITION_REGEXP = "^B(\\d{12})-RT\\d+/\\1-\\d+/\\d{4}(?:-\\d{2})+$";

    /*
        The regexp to recognize if a path denote an FILM ISO target node
         */
    private static final String FILM_ISO_REGEXP = "^B(\\d{12})-RT\\d+/\\1-\\d+/FILM-ISO-target$";

    /*
    The regexp to recognize if a path denote an FILM ISO scan node
     */
    private static final String FILM_ISO_SCAN_REGEXP
            = "^B(\\d{12})-RT\\d+/\\1-(\\d+)/FILM-ISO-target/\\w+-\\1-\\2-ISO-\\d{4}\\w?(-brik)?$";


    /*
    The regexp to recognize if a path denote an UNMATCHED node
     */
    private static final String UNMATCHED_REGEXP = "^B(\\d{12})-RT\\d+/\\1-\\d+/UNMATCHED";

    /*
The regexp to recognize if a path denote an Unmatched scan node
 */
    private static final String UNMATCHED_SCAN_REGEXP
            = "^B(\\d{12})-RT\\d+/\\1-(\\d+)/UNMATCHED/\\w+-\\1-\\2-\\d{4}\\w?(-brik)?$";


    /**
     * The regexp to recognize if a path denote an edition node
     */
    private static final String PAGE_REGEXP
            = "^B(\\d{12})-RT\\d+/\\1-\\d+/(\\d{4}(?:-\\d{2})+)/\\w+-\\2-\\d{4}\\w?(-brik)?$";
    private static final String JP2 = ".jp2";


    private Document batchXmlStructure;


    public FlaggingFinder(Document batchXmlStructure) {
        this.batchXmlStructure = batchXmlStructure;
    }

    public String getFileReferenceFromEvent(ParsingEvent event) {
        try {
            switch (event.getType()) {
                case Attribute:
                    if (event instanceof AttributeParsingEvent) {
                        AttributeParsingEvent attributeParsingEvent = (AttributeParsingEvent) event;
                        return getFileReferenceFromEvent(attributeParsingEvent);
                    }
                case NodeBegin:
                case NodeEnd:
                    return getFileReferenceFromNodeEvent(event);
                default:
                    return null;
            }
        } catch (NodeNotFoundException e) {
            //TODO log the exception
            return null;
        }
    }

    private String getFileReferenceFromNodeEvent(ParsingEvent event) throws NodeNotFoundException {

        if (event instanceof DataFileNodeBeginsParsingEvent || event instanceof DataFileNodeEndsParsingEvent) {
            return event.getName();
        } else if (event.getName().matches(PAGE_REGEXP)) {
            return jp2(event);
        } else if (event.getName().matches(EDITION_REGEXP)) {
            return getFirstFromEdition(event);
        } else if (event.getName().matches(FILM_REGEXP)) {
            return getFirstFromFilm(event);
        } else if (event.getName().matches(WORKSHIFT_ISO_REGEXP)) {
            return getFirstFromEdition(event);
        } else if (event.getName().matches(WORKSHIFT_ISO_SCAN_REGEXP)) {
            return jp2(event);
        } else if (event.getName().matches(FILM_ISO_REGEXP)) {
            return getFirstFromEdition(event);
        } else if (event.getName().matches(FILM_ISO_SCAN_REGEXP)) {
            return jp2(event);
        } else if (event.getName().matches(UNMATCHED_REGEXP)) {
            return getFirstFromEdition(event);
        } else if (event.getName().matches(UNMATCHED_SCAN_REGEXP)) {
            return jp2(event);
        } else {
            return null;
        }

    }


    private String getFirstFromFilm(ParsingEvent event) throws NodeNotFoundException {
        //Get the first from Film  iso target
        String name = getFolder(event);
        NodeList filmIsoTargetNodes = DOM.createXPathSelector().selectNodeList(
                batchXmlStructure, "//node[@name='" + name + "']/node[@shortName='FILM-ISO-target']/node");
        try {
            Node firstInFilmIsoTarget = getFirstBySequence(filmIsoTargetNodes);
            if (firstInFilmIsoTarget != null) {
                return jp2(firstInFilmIsoTarget);
            }
        } catch (NodeNotFoundException e) {

            //if no file in film iso target, get the first from editions
            NodeList editionPageNodes = DOM.createXPathSelector().selectNodeList(
                    batchXmlStructure, "//node[@shortName='" + name + "']/node[@shortName!='FILM-ISO-target']/node");
            Node firstInEditionPages = null;

            firstInEditionPages = getFirstBySequence(editionPageNodes);
            if (firstInEditionPages != null) {
                return jp2(firstInEditionPages);
            }


        }
        throw new NodeNotFoundException("Node not found");
    }


    private String getFirstFromEdition(ParsingEvent event) throws NodeNotFoundException {
        String name = getFolder(event);
        NodeList pageNodes = DOM.createXPathSelector()
                                .selectNodeList(batchXmlStructure, "//node[@name='" + name + "']/node");
        org.w3c.dom.Node node = getFirstBySequence(pageNodes);
        return jp2(node);
    }

    private String getFolder(ParsingEvent event) {
        String name = event.getName();
        if (event.getName().endsWith(".xml")) {
            name = event.getName().replaceFirst("/[^/]*$", "");
        }
        return name;
    }

    private String getName(org.w3c.dom.Node node) {
        return node.getAttributes().getNamedItem("name").getNodeValue();
    }

    private org.w3c.dom.Node getFirstBySequence(NodeList pageNodes) throws NodeNotFoundException {
        org.w3c.dom.Node lowest = null;
        for (int i = 0; i < pageNodes.getLength(); i++) {
            org.w3c.dom.Node current = pageNodes.item(i);
            if (lowest == null) {
                lowest = current;
            } else {
                if (getName(lowest).compareTo(getName(current)) > 0) {
                    lowest = current;
                }
            }

        }
        if (lowest == null) {
            throw new NodeNotFoundException("No nodes found");

        }
        return lowest;
    }

    private String getFileReferenceFromEvent(AttributeParsingEvent reference) throws NodeNotFoundException {
        String name = reference.getName();
        if (name.endsWith(".jp2/contents")) {
            return name.replace("/contents", "");
        }
        if (name.matches(".*\\.jp2\\.\\w+\\.xml$")) {
            return name.replaceFirst(".jp2.\\w+.xml$", JP2);
        }
        if (name.endsWith(".edition.xml")) {
            return getFirstFromEdition(reference);

        }

        if (name.endsWith(".film.xml")) {
            return getFirstFromFilm(reference);
        }
        if (name.endsWith(".film.histogram.xml")){
            return getFirstFromFilm((reference));
        }

        if (name.endsWith(".xml")) {
            return jp2(stripExtension(name));
        }
        return name;
    }

    private String jp2(ParsingEvent event) {
        return jp2(event.getName());
    }

    private String jp2(String name) {
        return name + JP2;
    }

    private String jp2(Node node) {
        return jp2(getName(node));
    }

    private String stripExtension(String name) {
        int filenameStart = name.lastIndexOf("/");
        int extensionStart = name.indexOf(".", filenameStart);
        return name.substring(0, extensionStart);
    }

    private class NodeNotFoundException extends Exception {
        private NodeNotFoundException() {
        }

        public NodeNotFoundException(String s) {
        }

        private NodeNotFoundException(String message, Throwable cause) {
            super(message, cause);
        }

        private NodeNotFoundException(Throwable cause) {
            super(cause);
        }
    }
}
