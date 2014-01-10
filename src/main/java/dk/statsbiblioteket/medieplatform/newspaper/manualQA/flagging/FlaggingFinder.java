package dk.statsbiblioteket.medieplatform.newspaper.manualQA.flagging;

import dk.statsbiblioteket.medieplatform.autonomous.iterator.common.AttributeParsingEvent;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.common.DataFileNodeBeginsParsingEvent;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.common.DataFileNodeEndsParsingEvent;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.common.ParsingEvent;
import dk.statsbiblioteket.util.xml.DOM;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class FlaggingFinder {

    private static final String PAGE_REGEXP = "";
    private static final String EDITION_REGEXP = "";
    private static final String FILM_REGEXP = "";
    private Document batchXmlStructure;

    public FlaggingFinder(Document batchXmlStructure) {
        this.batchXmlStructure = batchXmlStructure;
    }

    public String getFileReferenceFromEvent(ParsingEvent event) {
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
    }

    private String getFileReferenceFromNodeEvent(ParsingEvent event) {
        if (event instanceof DataFileNodeBeginsParsingEvent || event instanceof DataFileNodeEndsParsingEvent) {
            return event.getName();
        } else if (event.getName().matches(PAGE_REGEXP)) {
            return event.getName()+".jp2";
        } else if (event.getName().matches(EDITION_REGEXP)) {
            return getFirstFromEdition(event);
        } else if (event.getName().matches(FILM_REGEXP)) {
            return getFirstFromFilm(event);
        } else {
            return null;
        }

        //REMEMBER WORKSHIFT ISO TARGET
    }

    private String getFirstFromFilm(ParsingEvent event) {
        //Get the first from Film  iso target
        String name = getFolder(event);
        NodeList filmIsoTargetNodes = DOM.createXPathSelector().selectNodeList(batchXmlStructure,"//node[@name='" + name + "']/node[@shortName='FILM-ISO-target']/node");
        Node firstInFilmIsoTarget = getFirstBySequence(filmIsoTargetNodes);
        if (firstInFilmIsoTarget != null){
            return getName(firstInFilmIsoTarget)+".jp2";
        }
        //if no file in film iso target, get the first from editions
        NodeList editionPageNodes = DOM.createXPathSelector().selectNodeList(batchXmlStructure,"//node[@name='" + name + "']/node[@shortName!='FILM-ISO-target']/node");
        Node firstInEditionPages = getFirstBySequence(filmIsoTargetNodes);
        if (firstInEditionPages != null){
            return getName(firstInEditionPages)+".jp2";
        }
        return null;

    }

    private String getFirstFromEdition(ParsingEvent event) {
        String name = getFolder(event);
        NodeList pageNodes = DOM.createXPathSelector().selectNodeList(batchXmlStructure,"//node[@name='" + name + "']/node");
        org.w3c.dom.Node node = getFirstBySequence(pageNodes);
        return getName(node) +".jp2";
    }

    private String getFolder(ParsingEvent event) {
        String name = event.getName();
        if (event.getName().endsWith(".xml")){
            name = event.getName().replaceFirst("/[^/]*$", "");
        }
        return name;
    }

    private String getName(org.w3c.dom.Node node) {
        return node.getAttributes().getNamedItem("name").getNodeValue();
    }

    private org.w3c.dom.Node getFirstBySequence(NodeList pageNodes) {
        org.w3c.dom.Node lowest = null;
        for (int i = 0; i < pageNodes.getLength(); i++) {
            org.w3c.dom.Node current = pageNodes.item(i);
            if (lowest == null){
                lowest = current;
            } else {
                if (getName(lowest).compareTo(getName(current)) > 0){
                    lowest = current;
                }
            }

        }
        return lowest;
    }

    private String getFileReferenceFromEvent(AttributeParsingEvent reference) {
        String name = reference.getName();
        if (name.endsWith(".jp2/contents")) {
            return name.replace("/contents","");
        }

        if (name.endsWith(".mix.xml") || name.endsWith(".mods.xml") || name.endsWith(".alto.xml")) {
            // TODO Add the rest of them
            return stripExtension(name) + ".jp2";
        }
        if (name.endsWith(".edition.xml")) {
            return getFirstFromEdition(reference);

        }
        if (name.endsWith(".film.xml")) {
            return getFirstFromFilm(reference);
        }
        return name;
    }

    private String stripExtension(String name) {
        int filenameStart = name.lastIndexOf("/");
        int extensionStart = name.indexOf(".", filenameStart);
        return name.substring(0, extensionStart);
    }

}
