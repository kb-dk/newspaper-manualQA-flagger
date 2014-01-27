package dk.statsbiblioteket.medieplatform.autonomous.iterator.statistics;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.apache.commons.io.FileUtils;

/**
 * Writes statistics to a xml fil as the different statistics are generated minimising the
 * in-memory model.
 */
public class XmlFileIncrementalWriter implements StatisticWriter {
    private final XMLStreamWriter out;

    public XmlFileIncrementalWriter(String outputFilePath) {
        try {
            File outputFile = new File(outputFilePath);
            FileUtils.forceMkdir(outputFile.getParentFile());
            OutputStream outputStream = new FileOutputStream(new File(outputFilePath));

            out = XMLOutputFactory.newInstance().createXMLStreamWriter(
                    new OutputStreamWriter(outputStream, "utf-8"));
            out.writeStartDocument();
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize xml writer for statistics.", e);
        }
    }

    @Override
    public void addNode(String type, String name) {
        try {
            out.writeStartElement(type);
            out.writeAttribute("name", name);
        } catch (XMLStreamException e) {
            throw new RuntimeException("Failed to add node: " + name, e);
        }
    }

    @Override
    public void endNode() {
        try {
            out.writeEndElement();
        } catch (XMLStreamException e) {
            throw new RuntimeException("Failed to end node.", e);
        }
    }

    /**
     * Will write a statistic element. Ex: addStatistic(pages, 3)
     * will add a line:
     * <pages>1</pages>.
     */
    @Override
    public void addStatistic(String name, long metric) {
        try {
            out.writeStartElement(name);
            out.writeCharacters(Long.toString(metric));
            out.writeEndElement();
        } catch (XMLStreamException e) {
            throw new RuntimeException("Failed to write statistic.", e);
        }
    }

    /**
     * Will write a statistic element. Ex: addStatistic(ocrquality, new WeightedMean(12,24))
     * will add a line:
     * <ocrquality>0.5</ocrquality>.
     */
    @Override
    public void addStatistic(String name, WeightedMean metric) {
        try {
            out.writeStartElement(name);
            out.writeCharacters(metric.toString());
            out.writeEndElement();
        } catch (XMLStreamException e) {
            throw new RuntimeException("Failed to write statistic.", e);
        }
    }

    @Override
    public void finish() {
        try {
            out.writeEndDocument();
            out.flush();
            out.close();
        } catch (XMLStreamException e) {
            throw new RuntimeException("Failed to close xml writer.", e);
        }
    }
}
