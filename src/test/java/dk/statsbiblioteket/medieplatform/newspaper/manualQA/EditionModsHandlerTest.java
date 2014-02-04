package dk.statsbiblioteket.medieplatform.newspaper.manualQA;

import dk.statsbiblioteket.medieplatform.autonomous.Batch;
import dk.statsbiblioteket.medieplatform.autonomous.ResultCollector;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.common.AttributeParsingEvent;
import dk.statsbiblioteket.medieplatform.newspaper.manualQA.flagging.FlaggingCollector;
import dk.statsbiblioteket.medieplatform.newspaper.manualQA.mockers.EditionModsMocker;
import dk.statsbiblioteket.util.xml.DOM;
import org.testng.annotations.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 */
public class EditionModsHandlerTest {

    /**
     * Test the case where the no flag is raised because the edition nu,ber is <= 3.
     * @throws Exception
     */
    @Test
    public void testHandleAttribute() throws Exception {
        Batch batch = new Batch();
        FlaggingCollector flaggingCollector = new FlaggingCollector(batch, DOM.streamToDOM(
                        Thread.currentThread().getContextClassLoader().getResourceAsStream(
                                "batchStructure.xml")), "42.24", 100);
        ResultCollector resultCollector = new ResultCollector("foo", "bar");
        EditionModsHandler editionModsHandler = new EditionModsHandler(resultCollector, flaggingCollector, batch);
        AttributeParsingEvent attributeParsingEvent = new AttributeParsingEvent("B400022028241-RT1/400022028241-1/1795-06-15-01/adresseavisen1759-1795-06-15-01.edition.xml") {
            @Override
            public InputStream getData() throws IOException {
                return new ByteArrayInputStream( EditionModsMocker.getEditionMods("2").getBytes());
            }

            @Override
            public String getChecksum() throws IOException {
                return null;
            }
        };
        editionModsHandler.handleAttribute(attributeParsingEvent);
        assertFalse(flaggingCollector.hasFlags());
    }

    /**
     * Tests the case where a flag is raised because the edition number is >3.
     * @throws Exception
     */
    @Test
    public void testHandleAttributeWithFlag() throws Exception {
        Batch batch = new Batch();
        FlaggingCollector flaggingCollector = new FlaggingCollector(batch, DOM.streamToDOM(
                        Thread.currentThread().getContextClassLoader().getResourceAsStream(
                                "batchStructure.xml")), "42.24", 100);
        ResultCollector resultCollector = new ResultCollector("foo", "bar");
        EditionModsHandler editionModsHandler = new EditionModsHandler(resultCollector, flaggingCollector, batch);
        AttributeParsingEvent attributeParsingEvent = new AttributeParsingEvent("B400022028241-RT1/400022028241-1/1795-06-15-01/adresseavisen1759-1795-06-15-01.edition.xml") {
            @Override
            public InputStream getData() throws IOException {
                return new ByteArrayInputStream( EditionModsMocker.getEditionMods("4").getBytes());
            }

            @Override
            public String getChecksum() throws IOException {
                return null;
            }
        };
        editionModsHandler.handleAttribute(attributeParsingEvent);
        assertTrue(flaggingCollector.hasFlags());
    }

    /**
         * Tests the case where a flag is raised because the edition number is >3.
         * @throws Exception
         */
        @Test
        public void testHandleAttributeWithInvalidEditionNumber() throws Exception {
            Batch batch = new Batch();
            FlaggingCollector flaggingCollector = new FlaggingCollector(batch, DOM.streamToDOM(
                            Thread.currentThread().getContextClassLoader().getResourceAsStream(
                                    "batchStructure.xml")), "42.24", 100);
            ResultCollector resultCollector = new ResultCollector("foo", "bar");
            EditionModsHandler editionModsHandler = new EditionModsHandler(resultCollector, flaggingCollector, batch);
            AttributeParsingEvent attributeParsingEvent = new AttributeParsingEvent("B400022028241-RT1/400022028241-1/1795-06-15-01/adresseavisen1759-1795-06-15-01.edition.xml") {
                @Override
                public InputStream getData() throws IOException {
                    return new ByteArrayInputStream( EditionModsMocker.getEditionMods("blip").getBytes());
                }

                @Override
                public String getChecksum() throws IOException {
                    return null;
                }
            };
            editionModsHandler.handleAttribute(attributeParsingEvent);
            assertFalse(flaggingCollector.hasFlags());
            assertTrue(resultCollector.toReport().contains("blip"));
        }
}
