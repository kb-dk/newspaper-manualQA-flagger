package dk.statsbiblioteket.medieplatform.newspaper.manualQA;

import dk.statsbiblioteket.medieplatform.autonomous.Batch;
import dk.statsbiblioteket.medieplatform.autonomous.ResultCollector;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.common.AttributeParsingEvent;
import dk.statsbiblioteket.medieplatform.newspaper.manualQA.flagging.FlaggingCollector;
import dk.statsbiblioteket.medieplatform.newspaper.manualQA.mockers.FilmMocker;
import dk.statsbiblioteket.medieplatform.newspaper.manualQA.mockers.MixMocker;
import dk.statsbiblioteket.util.xml.DOM;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
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

    Properties properties;
    AttributeParsingEvent event;
    FlaggingCollector flaggingCollector;
    ResultCollector resultCollector;


    @BeforeMethod
    public void setUp() {
        properties = new Properties();
        properties.setProperty(ConfigConstants.IMAGE_PRODUCERS, "Statsbiblioteket,Bob,Anand");
        properties.setProperty(ConfigConstants.SCANNER_MANUFACTURERS, "AcmeCorp");
        properties.setProperty(ConfigConstants.SCANNER_MODEL_NUMBERS, "AK47,Mark2");
        properties.setProperty(ConfigConstants.SCANNER_MODELS, "Nutrimat");
        properties.setProperty(ConfigConstants.SCANNER_SERIAL_NOS, "h2s04,h2r07,h2z12");
        properties.setProperty(ConfigConstants.SCANNER_SOFTWARES, "vi;0.0.1beta,emacs;0.0.2alpha");
        Batch batch = new Batch();
        flaggingCollector = new FlaggingCollector(batch, DOM.streamToDOM(
                Thread.currentThread().getContextClassLoader().getResourceAsStream(
                        "batchStructure.xml")), "42.24");
        resultCollector = new ResultCollector("foo", "bar");
        final String additionalSoftware = extraSoftware.replace("___softwares___", "emacs").replace("___versions___", "0.0.2alpha");
        final String xml =  MixMocker.getMixXml(
                "AcmeCorp",
                "Nutrimat",
                "AK47",
                "h2r07",
                "vi",
                "0.0.1beta",
                "Statsbiblioteket;Anand",
                additionalSoftware
        );
        event = new AttributeParsingEvent("B400022028241-RT1/400022028241-1/1795-06-15-01/adresseavisen1759-1795-06-15-01-0003A.mix.xml") {
            @Override
            public InputStream getData() throws IOException {
                return new ByteArrayInputStream(xml.getBytes());
            }

            @Override
            public String getChecksum() throws IOException {
                return null;
            }
        };
    }

    @Test
    public void testHandleNoFlag() {
        MixHandler mixHandler= new MixHandler(resultCollector, properties, flaggingCollector);
        mixHandler.handleAttribute(event);
        assertFalse(flaggingCollector.hasFlags(), flaggingCollector.toReport());
        assertTrue(resultCollector.isSuccess());
    }

    @Test
    public void testHandleModelSerialNumbers() {
        properties.setProperty(ConfigConstants.SCANNER_SERIAL_NOS, "h2s04,h2s05");
        MixHandler mixHandler= new MixHandler(resultCollector, properties, flaggingCollector);
        mixHandler.handleAttribute(event);
        assertTrue(flaggingCollector.hasFlags(), flaggingCollector.toReport());
        assertTrue(resultCollector.isSuccess());
    }

    @Test
    public void testHandleModel() {
        properties.setProperty(ConfigConstants.SCANNER_MODELS, "MODEL_A,MODEL_B");
        MixHandler mixHandler= new MixHandler(resultCollector, properties, flaggingCollector);
        mixHandler.handleAttribute(event);
        assertTrue(flaggingCollector.hasFlags(), flaggingCollector.toReport());
        assertTrue(resultCollector.isSuccess());
    }

    @Test
    public void testHandleProduder() {
        properties.setProperty(ConfigConstants.IMAGE_PRODUCERS, "Anand, Sigismund");
        MixHandler mixHandler= new MixHandler(resultCollector, properties, flaggingCollector);
        mixHandler.handleAttribute(event);
        assertTrue(flaggingCollector.hasFlags(), flaggingCollector.toReport());
        assertTrue(resultCollector.isSuccess());
    }

    @Test
    public void testHandleModelNumber() {
        properties.setProperty(ConfigConstants.SCANNER_MODEL_NUMBERS, "AK48,Mark2");
        MixHandler mixHandler= new MixHandler(resultCollector, properties, flaggingCollector);
        mixHandler.handleAttribute(event);
        assertTrue(flaggingCollector.hasFlags(), flaggingCollector.toReport());
        assertTrue(resultCollector.isSuccess());
    }

    @Test
    public void testHandleAttributeFlagSoftwareVersion() throws Exception {
        properties.setProperty(ConfigConstants.SCANNER_SOFTWARES, "vi;0.0.1alpha,emacs;0.0.2alpha");
        MixHandler mixHandler= new MixHandler(resultCollector, properties, flaggingCollector);
        mixHandler.handleAttribute(event);
        assertTrue(flaggingCollector.hasFlags());
        assertTrue(resultCollector.isSuccess());
    }

}
