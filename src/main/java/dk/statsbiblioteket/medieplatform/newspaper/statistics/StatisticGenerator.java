package dk.statsbiblioteket.medieplatform.newspaper.statistics;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import dk.statsbiblioteket.medieplatform.autonomous.Batch;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.statistics.EventProcessor;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.statistics.writer.StatisticWriter;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.statistics.writer.XmlFileIncrementalWriter;
import net.sf.json.xml.XMLSerializer;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Processes ParsingEvents for a into a xml file containing the statistics for the batch. After the full statistics
 * file has been generated, the following custom statistics will be generated (through xslt): <ol>
 *     <li>General counting statistics in a <code>${BATCHID}-statistics.xml</code> file.</li>
 *     <li>Summary of dates included in film and batches. This is written to a JSON <code>${BATCHID}-statistics.json</code> file</li>
 * </ol>
 */
public class StatisticGenerator extends EventProcessor {
    private Logger log = LoggerFactory.getLogger(getClass());

    public static String STATISTICS_FILE_LOCATION_PROPERTY = "manualqaflagger.statistics.dir";
    public static String XSL_FILE_LOCATION = "statistics/transformers/";

    public static String FULL_STATISTICS_XML_FILE_NAME = "full-statistics.xml";
    public static String COUNT_STATISTICS_XML_FILE_NAME = "count-statistics.xml";
    public static String DATE_STATISTICS_XML_FILE_NAME = "date-statistics.xml";
    public static String DATE_STATISTICS_JSON_FILE_NAME = "date-statistics.json";

    private final StatisticWriter writer;
    private final String statisticsDir;
    private final String statisticsFile;

    /**
     * @param batch Used for naming the output file.
     * @param properties Defines specifics for the statistics.
     */
    public StatisticGenerator(Batch batch, Properties properties) {
        super(new BatchCollector());
        statisticsDir = properties.getProperty(STATISTICS_FILE_LOCATION_PROPERTY, "target/statistics/Integration") +
                "/" + batch.getFullID() + "/";
        statisticsFile = statisticsDir + FULL_STATISTICS_XML_FILE_NAME;
        writer = new XmlFileIncrementalWriter(statisticsFile);
        collector.initialize(batch.getFullID(), null, writer, properties);
    }

    @Override
    public void handleFinish() {
        super.handleFinish();
        writer.finish();
        try {
            postprocess("generateCountStatistics.xsl", statisticsDir + COUNT_STATISTICS_XML_FILE_NAME);
            postprocess("generateDateStatistics.xsl", statisticsDir + DATE_STATISTICS_XML_FILE_NAME);
            convertToJson(statisticsDir + DATE_STATISTICS_XML_FILE_NAME, statisticsDir + DATE_STATISTICS_JSON_FILE_NAME);
        } catch (Exception e) {
            log.error("Failed to postprocess statistics", e);
        }
    }

    private static StreamSource getXslStream(String xslFileName) {
        return new StreamSource(
                Thread.currentThread().getContextClassLoader().getResourceAsStream(
                        XSL_FILE_LOCATION + xslFileName
                ));
    }

    private void postprocess(String xslfile, String outputfil) throws Exception {
        Transformer xmlTransformer = TransformerFactory.newInstance().newTransformer(getXslStream(xslfile));

        xmlTransformer.transform(
                new StreamSource(new FileInputStream(statisticsFile)), new StreamResult(new FileOutputStream(outputfil))
        );
    }

    private void convertToJson(String inputFile, String outputfile) throws IOException {
        String xml = IOUtils.toString(new FileInputStream(inputFile));
        XMLSerializer xmlSerializer = new XMLSerializer();
        FileWriter writer = new FileWriter(outputfile);
        xmlSerializer.read(xml).write(writer);
        writer.flush();
    }
}
