package dk.statsbiblioteket.medieplatform.newspaper.manualQA;

import dk.statsbiblioteket.medieplatform.autonomous.Batch;
import dk.statsbiblioteket.medieplatform.autonomous.ResultCollector;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.common.AttributeParsingEvent;
import dk.statsbiblioteket.medieplatform.newspaper.manualQA.flagging.FlaggingCollector;
import dk.statsbiblioteket.medieplatform.newspaper.manualQA.mockers.EditionModsMocker;
import dk.statsbiblioteket.medieplatform.newspaper.manualQA.mockers.FilmMocker;
import dk.statsbiblioteket.util.xml.DOM;
import org.testng.annotations.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 *
 */
public class FilmHandlerTest {

    /**
     * Tests the case where no flags are raised.
     * @throws Exception
     */
    @Test
    public void testHandleAttributeNoFlag() throws Exception {
        Batch batch = new Batch();
        FlaggingCollector flaggingCollector = new FlaggingCollector(batch, DOM.streamToDOM(
                Thread.currentThread().getContextClassLoader().getResourceAsStream(
                        "batchStructure.xml")), "42.24");
        ResultCollector resultCollector = new ResultCollector("foo", "bar");
        FilmHandler filmHandler = new FilmHandler(resultCollector, flaggingCollector);
        AttributeParsingEvent attributeParsingEvent = new AttributeParsingEvent("B400022028241-RT1/400022028241-1/adresseavisen1759-400022028241-1.film.xml") {
            @Override
            public InputStream getData() throws IOException {
                return new ByteArrayInputStream( FilmMocker.getFilmXml("350", "17").getBytes());
            }

            @Override
            public String getChecksum() throws IOException {
                return null;
            }
        };
        filmHandler.handleAttribute(attributeParsingEvent);
        assertFalse(flaggingCollector.hasFlags());
        assertTrue(resultCollector.isSuccess());
    }

    /**
     * Tests the case with unexpectedly high resolution.
     * @throws Exception
     */
    @Test
    public void testHandleAttributeResolutionFlag() throws Exception {
        Batch batch = new Batch();
        FlaggingCollector flaggingCollector = new FlaggingCollector(batch, DOM.streamToDOM(
                Thread.currentThread().getContextClassLoader().getResourceAsStream(
                        "batchStructure.xml")), "42.24");
        ResultCollector resultCollector = new ResultCollector("foo", "bar");
        FilmHandler filmHandler = new FilmHandler(resultCollector, flaggingCollector);
        AttributeParsingEvent attributeParsingEvent = new AttributeParsingEvent("B400022028241-RT1/400022028241-1/adresseavisen1759-400022028241-1.film.xml") {
            @Override
            public InputStream getData() throws IOException {
                return new ByteArrayInputStream( FilmMocker.getFilmXml("450", "17").getBytes());
            }

            @Override
            public String getChecksum() throws IOException {
                return null;
            }
        };
        filmHandler.handleAttribute(attributeParsingEvent);
        assertTrue(flaggingCollector.hasFlags());
        assertTrue(resultCollector.isSuccess());
    }

    /**
     * Tests the case with unexpectedly high reduction ratio.
     * @throws Exception
     */
    @Test
    public void testHandleAttributeReductionFlag() throws Exception {
        Batch batch = new Batch();
        FlaggingCollector flaggingCollector = new FlaggingCollector(batch, DOM.streamToDOM(
                Thread.currentThread().getContextClassLoader().getResourceAsStream(
                        "batchStructure.xml")), "42.24");
        ResultCollector resultCollector = new ResultCollector("foo", "bar");
        FilmHandler filmHandler = new FilmHandler(resultCollector, flaggingCollector);
        AttributeParsingEvent attributeParsingEvent = new AttributeParsingEvent("B400022028241-RT1/400022028241-1/adresseavisen1759-400022028241-1.film.xml") {
            @Override
            public InputStream getData() throws IOException {
                return new ByteArrayInputStream( FilmMocker.getFilmXml("350", "23").getBytes());
            }

            @Override
            public String getChecksum() throws IOException {
                return null;
            }
        };
        filmHandler.handleAttribute(attributeParsingEvent);
        assertTrue(flaggingCollector.hasFlags());
        assertTrue(resultCollector.isSuccess());
    }

    /**
     * Tests the case of invalid values for the elements.
     * @throws Exception
     */
    @Test
    public void testHandleAttributeFailure() throws Exception {
        Batch batch = new Batch();
        FlaggingCollector flaggingCollector = new FlaggingCollector(batch, DOM.streamToDOM(
                Thread.currentThread().getContextClassLoader().getResourceAsStream(
                        "batchStructure.xml")), "42.24");
        ResultCollector resultCollector = new ResultCollector("foo", "bar");
        FilmHandler filmHandler = new FilmHandler(resultCollector, flaggingCollector);
        AttributeParsingEvent attributeParsingEvent = new AttributeParsingEvent("B400022028241-RT1/400022028241-1/adresseavisen1759-400022028241-1.film.xml") {
            @Override
            public InputStream getData() throws IOException {
                return new ByteArrayInputStream( FilmMocker.getFilmXml("350x", "17").getBytes());
            }

            @Override
            public String getChecksum() throws IOException {
                return null;
            }
        };
        filmHandler.handleAttribute(attributeParsingEvent);
        assertFalse(flaggingCollector.hasFlags());
        assertFalse(resultCollector.isSuccess());
    }

    /**
     * Tests the case with an xml parsing error.
     * @throws Exception
     */
    @Test
    public void testHandleAttributeParsingFailure() throws Exception {
        Batch batch = new Batch();
        FlaggingCollector flaggingCollector = new FlaggingCollector(batch, DOM.streamToDOM(
                Thread.currentThread().getContextClassLoader().getResourceAsStream(
                        "batchStructure.xml")), "42.24");
        ResultCollector resultCollector = new ResultCollector("foo", "bar");
        FilmHandler filmHandler = new FilmHandler(resultCollector, flaggingCollector);
        AttributeParsingEvent attributeParsingEvent = new AttributeParsingEvent("B400022028241-RT1/400022028241-1/adresseavisen1759-400022028241-1.film.xml") {
            @Override
            public InputStream getData() throws IOException {
                return new ByteArrayInputStream( FilmMocker.getFilmXml("350>", "17").getBytes());
            }

            @Override
            public String getChecksum() throws IOException {
                return null;
            }
        };
        filmHandler.handleAttribute(attributeParsingEvent);
        assertFalse(flaggingCollector.hasFlags());
        assertFalse(resultCollector.isSuccess());
    }
}
