package dk.statsbiblioteket.medieplatform.newspaper.manualQA;

import dk.statsbiblioteket.medieplatform.autonomous.Batch;
import dk.statsbiblioteket.medieplatform.autonomous.ResultCollector;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.common.AttributeParsingEvent;
import dk.statsbiblioteket.medieplatform.newspaper.manualQA.flagging.FlaggingCollector;
import dk.statsbiblioteket.medieplatform.newspaper.manualQA.mockers.FilmMocker;
import dk.statsbiblioteket.medieplatform.newspaper.manualQA.mockers.MixMocker;
import dk.statsbiblioteket.util.xml.DOM;
import org.testng.annotations.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 *
 */
public class MixHandlerTest {

    String extraSoftware = "            <mix:ScanningSystemSoftware><!--Repeatable-->\n" +
            "                <mix:scanningSoftwareName>___softwares___</mix:scanningSoftwareName>\n" +
            "                <mix:scanningSoftwareVersionNo>___versions___</mix:scanningSoftwareVersionNo>\n" +
            "            </mix:ScanningSystemSoftware>\n";

    @Test
    public void testHandleAttributeNoFlag() throws Exception {
        Batch batch = new Batch();
        FlaggingCollector flaggingCollector = new FlaggingCollector(batch, DOM.streamToDOM(
                Thread.currentThread().getContextClassLoader().getResourceAsStream(
                        "batchStructure.xml")), "42.24");
        ResultCollector resultCollector = new ResultCollector("foo", "bar");
        Properties properties = new Properties();
        properties.setProperty(ConfigConstants.IMAGE_PRODUCERS, "Statsbiblioteket,Bob,Anand");
        properties.setProperty(ConfigConstants.SCANNER_MANUFACTURERS, "AcmeCorp");
        properties.setProperty(ConfigConstants.SCANNER_MODEL_NUMBERS, "AK47,Mark2");
        properties.setProperty(ConfigConstants.SCANNER_MODELS, "Nutrimat");
        properties.setProperty(ConfigConstants.SCANNER_SERIAL_NOS, "h2s04,h2r07,h2z12");
        properties.setProperty(ConfigConstants.SCANNER_SOFTWARES, "vi;0.0.1beta,emacs;0.0.2alpha");
        MixHandler mixHandler= new MixHandler(resultCollector, properties, flaggingCollector);
        final String additionalSoftware = extraSoftware.replace("___softwares___", "emacs").replace("___versions___", "0.0.2alpha");
        AttributeParsingEvent attributeParsingEvent = new AttributeParsingEvent("B400022028241-RT1/400022028241-1/1795-06-15-01/adresseavisen1759-1795-06-15-01-0003A.mix.xml") {
            @Override
            public InputStream getData() throws IOException {
                return new ByteArrayInputStream( MixMocker.getMixXml(
                        "AcmeCorp",
                        "Nutrimat",
                        "AK47",
                        "h2r07",
                        "vi",
                        "0.0.1beta",
                        "Statsbiblioteket;Anand",
                        additionalSoftware
                ).getBytes());
            }

            @Override
            public String getChecksum() throws IOException {
                return null;
            }
        };
        mixHandler.handleAttribute(attributeParsingEvent);
        assertFalse(flaggingCollector.hasFlags(), flaggingCollector.toReport());
        assertTrue(resultCollector.isSuccess());
    }

    @Test
    public void testHandleAttributeFlagSoftwareVersion() throws Exception {
        Batch batch = new Batch();
        FlaggingCollector flaggingCollector = new FlaggingCollector(batch, DOM.streamToDOM(
                Thread.currentThread().getContextClassLoader().getResourceAsStream(
                        "batchStructure.xml")), "42.24");
        ResultCollector resultCollector = new ResultCollector("foo", "bar");
        Properties properties = new Properties();
        properties.setProperty(ConfigConstants.IMAGE_PRODUCERS, "Statsbiblioteket,Bob,Anand");
        properties.setProperty(ConfigConstants.SCANNER_MANUFACTURERS, "AcmeCorp");
        properties.setProperty(ConfigConstants.SCANNER_MODEL_NUMBERS, "AK47,Mark2");
        properties.setProperty(ConfigConstants.SCANNER_MODELS, "Nutrimat");
        properties.setProperty(ConfigConstants.SCANNER_SERIAL_NOS, "h2s04,h2r07,h2z12");
        properties.setProperty(ConfigConstants.SCANNER_SOFTWARES, "vi;0.0.1beta,emacs;0.0.2alpha");
        MixHandler mixHandler= new MixHandler(resultCollector, properties, flaggingCollector);
        AttributeParsingEvent attributeParsingEvent = new AttributeParsingEvent("B400022028241-RT1/400022028241-1/1795-06-15-01/adresseavisen1759-1795-06-15-01-0003A.mix.xml") {
            @Override
            public InputStream getData() throws IOException {
                return new ByteArrayInputStream( MixMocker.getMixXml(
                        "AcmeCorp",
                        "Nutrimat",
                        "AK47",
                        "h2r07",
                        "vi",
                        "0.0.1alpha",
                        "Statsbiblioteket;Anand",
                        ""
                ).getBytes());
            }

            @Override
            public String getChecksum() throws IOException {
                return null;
            }
        };
        mixHandler.handleAttribute(attributeParsingEvent);
        assertTrue(flaggingCollector.hasFlags());
        assertTrue(resultCollector.isSuccess());
    }

}
