package dk.statsbiblioteket.medieplatform.newspaper.manualQA;

import dk.statsbiblioteket.medieplatform.autonomous.ResultCollector;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.common.AttributeParsingEvent;
import dk.statsbiblioteket.util.xml.DOM;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.traversal.DocumentTraversal;
import org.w3c.dom.traversal.NodeFilter;
import org.w3c.dom.traversal.NodeIterator;

import java.io.InputStream;

/**
 * Created with IntelliJ IDEA.
 * User: jrg
 * Date: 1/8/14
 * Time: 3:59 PM
 * To change this template use File | Settings | File Templates.
 */
public class HistogramParser {
    public long[] parseHistogram(AttributeParsingEvent event, ResultCollector resultCollector) {
        long[] histogram = new long[256];

        try {
            InputStream stream = event.getData();
            Document doc = DOM.streamToDOM(stream, true);
            histogram = parseDocToArray(doc);
        } catch (Exception e) {
            resultCollector.addFailure(event.getName(), "Exception", "component", e.getMessage());
        }

        return histogram;
    }


    private long[] parseDocToArray(Document doc) {
        long[] result = new long[256];
        for (int i = 0; i < 256; i++) {
            result[i] = 0;
        }
        DocumentTraversal traversal = (DocumentTraversal) doc;

        NodeIterator iterator = traversal.createNodeIterator(
                doc.getDocumentElement(), NodeFilter.SHOW_ELEMENT, null, true);

        for (Node n = iterator.nextNode(); n != null; n = iterator.nextNode()) {
            String tagName = ((Element) n).getTagName();

            if(tagName.equals("color")) {
                int code = -1;
                long count = -1;

                NodeList colorChildren = n.getChildNodes();
                for (int i = 0; i < colorChildren.getLength(); i++) {
                    Node node = colorChildren.item(i);
                    if (((Element) node).getTagName().equals("code")) {
                        String value = node.getNodeValue();
                        code = Integer.parseInt(value);
                    }
                    if (((Element) node).getTagName().equals("count")) {
                        String value = node.getNodeValue();
                        count = Long.parseLong(value);
                    }
                }

                if (code != -1 && count != -1 && code >= 0 && code <= 255) {
                    result[code] = count;
                }
            }
        }

        return result;
    }

}
