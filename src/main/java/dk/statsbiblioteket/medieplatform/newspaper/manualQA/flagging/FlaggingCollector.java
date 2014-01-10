package dk.statsbiblioteket.medieplatform.newspaper.manualQA.flagging;

import dk.statsbiblioteket.medieplatform.autonomous.Batch;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.common.ParsingEvent;
import org.w3c.dom.Document;

public class FlaggingCollector {


    private Batch batch;
    private FlaggingFinder flaggingFinder;

    private int flags = 0;

    public FlaggingCollector(Batch batch, Document batchXmlStructure) {

        this.batch = batch;
        flaggingFinder = new FlaggingFinder(batchXmlStructure);
    }


    public void addFlag(ParsingEvent reference, String type, String component, String description) {
        flags++;
    }


    public Batch getBatch() {
        return batch;
    }

    public String toReport() {
        return "<mq:manualqainput xmlns:mq=\"http://schemas.statsbiblioteket.dk/manualqainput/\"\n" +
               "             version=\"0.5\">\n" +
               "    <mq:manualqafiles>\n" +
               "        <mq:manualqafile>\n" +
               "            <mq:filereference>B400022028241-RT1/400022028241-1/FILM-ISO-target/adresseavisen1759-400022028241-1-ISO-0001.jp2</mq:filereference>\n" +
               "            <mq:type>jp2file</mq:type>\n" +
               "            <mq:component>HistogramAnalyzer</mq:component>\n" +
               "            <mq:description>All files on this film seem suspiciously dark</mq:description>\n" +
               "        </mq:manualqafile>\n" +
               "        <mq:manualqafile>\n" +
               "            <mq:filereference>B400022028241-RT1/400022028241-1/1795-06-13-01/adresseavisen1759-1795-06-13-01-0006.jp2</mq:filereference>\n" +
               "            <mq:type>jp2file</mq:type>\n" +
               "            <mq:component>BlackEdgeAnalyzer</mq:component>\n" +
               "            <mq:description>There are no dark edges on this page. Please check the cropping.</mq:description>\n" +
               "        </mq:manualqafile>\n" +
               "        <mq:manualqafile>\n" +
               "            <mq:filereference>B400022028241-RT1/400022028241-1/FILM-ISO-target/adresseavisen1759-400022028241-1-ISO-0001.jp2</mq:filereference>\n" +
               "            <mq:type>metadata</mq:type>\n" +
               "            <mq:component>FilmMetadataAnalyzer</mq:component>\n" +
               "            <mq:description>The values in the adresseavisen1759-400022028241-1.film.xml reductionRatio has the suspicious value \"14x\". Please check for correctness.</mq:description>\n" +
               "        </mq:manualqafile>\n" +
               "        <mq:manualqafile>\n" +
               "            <mq:filereference>B400022028241-RT1/400022028241-1/1795-06-13-01/adresseavisen1759-1795-06-13-01-0006.jp2</mq:filereference>\n" +
               "            <mq:type>metadata</mq:type>\n" +
               "            <mq:component>ModsMetadataAnalyzer</mq:component>\n" +
               "            <mq:description>The value for edition label 'BÃ¸rsen' in the adresseavisen1759-1795-06-13-01-0006.mods.xml has suspicious characters. Please check for correctness.</mq:description>\n" +
               "        </mq:manualqafile>\n" +
               "    </mq:manualqafiles>\n" +
               "</mq:manualqainput>";
    }

    public boolean hasFlags() {
        return flags > 0;
    }
}
