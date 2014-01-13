package dk.statsbiblioteket.medieplatform.newspaper.manualQA.flagging;

import dk.statsbiblioteket.medieplatform.autonomous.iterator.common.AttributeParsingEvent;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.common.DataFileNodeBeginsParsingEvent;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.common.DataFileNodeEndsParsingEvent;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.common.NodeBeginsParsingEvent;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.common.NodeEndParsingEvent;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.common.ParsingEvent;
import dk.statsbiblioteket.util.xml.DOM;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.io.InputStream;

public class FlaggingFinderTest {


    /*---------DATAFILES*/
    @Test
    public void testJp2FileReference() throws Exception {
        AttributeParsingEvent mixEvent = createAttributeParsingEvent(
                "B400022028241-RT1/400022028241-1/1795-06-15-01/adresseavisen1759-1795-06-15-01-0002.jp2/contents");
        FlaggingFinder flaggingFinder = getFlaggingFinder();
        String mixMapping = flaggingFinder.getFileReferenceFromEvent(mixEvent);
        Assert.assertEquals(
                mixMapping,
                "B400022028241-RT1/400022028241-1/1795-06-15-01/adresseavisen1759-1795-06-15-01-0002.jp2");
    }

    @Test
    public void testJp2FolderReference1() throws Exception {
        ParsingEvent mixEvent = createDataFileNodeBeginsParsingEvent(
                "B400022028241-RT1/400022028241-1/1795-06-15-01/adresseavisen1759-1795-06-15-01-0002.jp2");
        FlaggingFinder flaggingFinder = getFlaggingFinder();
        String mixMapping = flaggingFinder.getFileReferenceFromEvent(mixEvent);
        Assert.assertEquals(
                mixMapping,
                "B400022028241-RT1/400022028241-1/1795-06-15-01/adresseavisen1759-1795-06-15-01-0002.jp2");
    }

    @Test
    public void testJp2FolderReference2() throws Exception {
        ParsingEvent mixEvent = createDataFileNodeEndsParsingEvent(
                "B400022028241-RT1/400022028241-1/1795-06-15-01/adresseavisen1759-1795-06-15-01-0002.jp2");
        FlaggingFinder flaggingFinder = getFlaggingFinder();
        String mixMapping = flaggingFinder.getFileReferenceFromEvent(mixEvent);
        Assert.assertEquals(
                mixMapping,
                "B400022028241-RT1/400022028241-1/1795-06-15-01/adresseavisen1759-1795-06-15-01-0002.jp2");
    }

    /*-------------PAGES*/

    @Test
    public void testPageNodeReference1() throws Exception {
        ParsingEvent mixEvent = createNodeBeginsParsingEvent(
                "B400022028241-RT1/400022028241-1/1795-06-15-01/adresseavisen1759-1795-06-15-01-0003-brik");

        FlaggingFinder flaggingFinder = getFlaggingFinder();
        String mixMapping = flaggingFinder.getFileReferenceFromEvent(mixEvent);
        Assert.assertEquals(
                mixMapping,
                "B400022028241-RT1/400022028241-1/1795-06-15-01/adresseavisen1759-1795-06-15-01-0003-brik.jp2");
    }

    @Test
    public void testPageNodeReference2() throws Exception {
        ParsingEvent mixEvent = createNodeBeginsParsingEvent(
                "B400022028241-RT1/400022028241-1/1795-06-15-02/adresseavisen1759-1795-06-15-02-0005B");

        FlaggingFinder flaggingFinder = getFlaggingFinder();
        String mixMapping = flaggingFinder.getFileReferenceFromEvent(mixEvent);
        Assert.assertEquals(
                mixMapping,
                "B400022028241-RT1/400022028241-1/1795-06-15-02/adresseavisen1759-1795-06-15-02-0005B.jp2");
    }


    @Test
    public void testPageMixFileReference() throws Exception {
        AttributeParsingEvent mixEvent = createAttributeParsingEvent(
                "B400022028241-RT1/400022028241-1/1795-06-15-01/adresseavisen1759-1795-06-15-01-0002.mix.xml");
        FlaggingFinder flaggingFinder = getFlaggingFinder();
        String mixMapping = flaggingFinder.getFileReferenceFromEvent(mixEvent);
        Assert.assertEquals(
                mixMapping,
                "B400022028241-RT1/400022028241-1/1795-06-15-01/adresseavisen1759-1795-06-15-01-0002.jp2");
    }

    @Test
    public void testPageModsFileReference() throws Exception {
        AttributeParsingEvent mixEvent = createAttributeParsingEvent(
                "B400022028241-RT1/400022028241-1/1795-06-15-01/adresseavisen1759-1795-06-15-01-0002.mods.xml");
        FlaggingFinder flaggingFinder = getFlaggingFinder();
        String mixMapping = flaggingFinder.getFileReferenceFromEvent(mixEvent);
        Assert.assertEquals(
                mixMapping,
                "B400022028241-RT1/400022028241-1/1795-06-15-01/adresseavisen1759-1795-06-15-01-0002.jp2");
    }

    @Test
    public void testPageAnyXmlFileReference() throws Exception {
        AttributeParsingEvent mixEvent = createAttributeParsingEvent(
                "B400022028241-RT1/400022028241-1/1795-06-15-01/adresseavisen1759-1795-06-15-01-0002.jabba.xml");
        FlaggingFinder flaggingFinder = getFlaggingFinder();
        String mixMapping = flaggingFinder.getFileReferenceFromEvent(mixEvent);
        Assert.assertEquals(
                mixMapping,
                "B400022028241-RT1/400022028241-1/1795-06-15-01/adresseavisen1759-1795-06-15-01-0002.jp2");
    }

    /*--------------------EDITIONS*/

    @Test
    public void testEditionXmlReference1() throws Exception {
        ParsingEvent mixEvent = createAttributeParsingEvent(
                "B400022028241-RT1/400022028241-1/1795-06-15-01/adresseavisen1759-1795-06-15-01.edition.xml");

        FlaggingFinder flaggingFinder = getFlaggingFinder();
        String mixMapping = flaggingFinder.getFileReferenceFromEvent(mixEvent);
        Assert.assertEquals(
                mixMapping,
                "B400022028241-RT1/400022028241-1/1795-06-15-01/adresseavisen1759-1795-06-15-01-0002.jp2");
    }

    @Test
    public void testEditionXmlReference2() throws Exception {
        ParsingEvent mixEvent = createAttributeParsingEvent(
                "B400022028241-RT1/400022028241-1/1795-06-15-02/adresseavisen1759-1795-06-15-02.edition.xml");

        FlaggingFinder flaggingFinder = getFlaggingFinder();
        String mixMapping = flaggingFinder.getFileReferenceFromEvent(mixEvent);
        Assert.assertEquals(
                mixMapping,
                "B400022028241-RT1/400022028241-1/1795-06-15-02/adresseavisen1759-1795-06-15-02-0004.jp2");
    }


    @Test
    public void testEditionNodeReference1() throws Exception {
        ParsingEvent mixEvent = createNodeBeginsParsingEvent("B400022028241-RT1/400022028241-1/1795-06-15-01");

        FlaggingFinder flaggingFinder = getFlaggingFinder();
        String mixMapping = flaggingFinder.getFileReferenceFromEvent(mixEvent);
        Assert.assertEquals(
                mixMapping,
                "B400022028241-RT1/400022028241-1/1795-06-15-01/adresseavisen1759-1795-06-15-01-0002.jp2");
    }



    /*--------------------------FILM*/

    @Test
    public void testFilmXmlReference() throws Exception {
        ParsingEvent mixEvent = createAttributeParsingEvent(
                "B400022028241-RT1/400022028241-1/adresseavisen1759-400022028241-1.film.xml");

        FlaggingFinder flaggingFinder = getFlaggingFinder();
        String mixMapping = flaggingFinder.getFileReferenceFromEvent(mixEvent);
        Assert.assertEquals(
                mixMapping,
                "B400022028241-RT1/400022028241-1/FILM-ISO-target/adresseavisen1759-400022028241-1-ISO-0001.jp2");
    }


    @Test
    public void testFilmNodeReference1() throws Exception {
        ParsingEvent mixEvent = createNodeBeginsParsingEvent("B400022028241-RT1/400022028241-1");

        FlaggingFinder flaggingFinder = getFlaggingFinder();
        String mixMapping = flaggingFinder.getFileReferenceFromEvent(mixEvent);
        Assert.assertEquals(
                mixMapping,
                "B400022028241-RT1/400022028241-1/FILM-ISO-target/adresseavisen1759-400022028241-1-ISO-0001.jp2");
    }


    /*----------------------WORKSHIFT ISO*/


    @Test
    public void testWorkShiftISONodeReference1() throws Exception {
        ParsingEvent mixEvent = createNodeBeginsParsingEvent("B400022028241-RT1/WORKSHIFT-ISO-TARGET");

        FlaggingFinder flaggingFinder = getFlaggingFinder();
        String mixMapping = flaggingFinder.getFileReferenceFromEvent(mixEvent);
        Assert.assertEquals(mixMapping, "B400022028241-RT1/WORKSHIFT-ISO-TARGET/Target-000001-0001.jp2");
    }


    @Test
    public void testWorkShiftISONodeReference2() throws Exception {
        ParsingEvent mixEvent
                = createNodeBeginsParsingEvent("B400022028241-RT1/WORKSHIFT-ISO-TARGET/Target-000001-0001");

        FlaggingFinder flaggingFinder = getFlaggingFinder();
        String mixMapping = flaggingFinder.getFileReferenceFromEvent(mixEvent);
        Assert.assertEquals(mixMapping, "B400022028241-RT1/WORKSHIFT-ISO-TARGET/Target-000001-0001.jp2");
    }

    @Test
    public void testWorkShiftISONodeReference3() throws Exception {
        ParsingEvent mixEvent = createAttributeParsingEvent(
                "B400022028241-RT1/WORKSHIFT-ISO-TARGET/Target-000001-0001.mix.xml");

        FlaggingFinder flaggingFinder = getFlaggingFinder();
        String mixMapping = flaggingFinder.getFileReferenceFromEvent(mixEvent);
        Assert.assertEquals(mixMapping, "B400022028241-RT1/WORKSHIFT-ISO-TARGET/Target-000001-0001.jp2");
    }

    @Test
    public void testWorkShiftISONodeReference4() throws Exception {
        ParsingEvent mixEvent = createDataFileNodeBeginsParsingEvent(
                "B400022028241-RT1/WORKSHIFT-ISO-TARGET/Target-000001-0001.jp2");

        FlaggingFinder flaggingFinder = getFlaggingFinder();
        String mixMapping = flaggingFinder.getFileReferenceFromEvent(mixEvent);
        Assert.assertEquals(mixMapping, "B400022028241-RT1/WORKSHIFT-ISO-TARGET/Target-000001-0001.jp2");
    }


    @Test
    public void testWorkShiftISONodeReference5() throws Exception {
        ParsingEvent mixEvent = createAttributeParsingEvent(
                "B400022028241-RT1/WORKSHIFT-ISO-TARGET/Target-000001-0001.jp2/contents");

        FlaggingFinder flaggingFinder = getFlaggingFinder();
        String mixMapping = flaggingFinder.getFileReferenceFromEvent(mixEvent);
        Assert.assertEquals(mixMapping, "B400022028241-RT1/WORKSHIFT-ISO-TARGET/Target-000001-0001.jp2");
    }


    @Test
    public void testWorkShiftISONodeReference6() throws Exception {
        ParsingEvent mixEvent = createAttributeParsingEvent(
                "B400022028241-RT1/WORKSHIFT-ISO-TARGET/Target-000001-0001.jp2.jpylyzer.xml");

        FlaggingFinder flaggingFinder = getFlaggingFinder();
        String mixMapping = flaggingFinder.getFileReferenceFromEvent(mixEvent);
        Assert.assertEquals(mixMapping, "B400022028241-RT1/WORKSHIFT-ISO-TARGET/Target-000001-0001.jp2");
    }

    /*-------------------FILM-ISO-target */

    @Test
    public void testFilmISONodeReference1() throws Exception {
        ParsingEvent mixEvent = createNodeBeginsParsingEvent("B400022028241-RT1/400022028241-1/FILM-ISO-target");

        FlaggingFinder flaggingFinder = getFlaggingFinder();
        String mixMapping = flaggingFinder.getFileReferenceFromEvent(mixEvent);
        Assert.assertEquals(
                mixMapping,
                "B400022028241-RT1/400022028241-1/FILM-ISO-target/adresseavisen1759-400022028241-1-ISO-0001.jp2");
    }


    @Test
    public void testFilmISONodeReference2() throws Exception {
        ParsingEvent mixEvent = createNodeBeginsParsingEvent(
                "B400022028241-RT1/400022028241-1/FILM-ISO-target/adresseavisen1759-400022028241-1-ISO-0001");

        FlaggingFinder flaggingFinder = getFlaggingFinder();
        String mixMapping = flaggingFinder.getFileReferenceFromEvent(mixEvent);
        Assert.assertEquals(
                mixMapping,
                "B400022028241-RT1/400022028241-1/FILM-ISO-target/adresseavisen1759-400022028241-1-ISO-0001.jp2");
    }

    @Test
    public void testFilmISONodeReference3() throws Exception {
        ParsingEvent mixEvent = createAttributeParsingEvent(
                "B400022028241-RT1/400022028241-1/FILM-ISO-target/adresseavisen1759-400022028241-1-ISO-0001.mix.xml");

        FlaggingFinder flaggingFinder = getFlaggingFinder();
        String mixMapping = flaggingFinder.getFileReferenceFromEvent(mixEvent);
        Assert.assertEquals(
                mixMapping,
                "B400022028241-RT1/400022028241-1/FILM-ISO-target/adresseavisen1759-400022028241-1-ISO-0001.jp2");
    }

    @Test
    public void testFilmISONodeReference4() throws Exception {
        ParsingEvent mixEvent = createDataFileNodeBeginsParsingEvent(
                "B400022028241-RT1/400022028241-1/FILM-ISO-target/adresseavisen1759-400022028241-1-ISO-0001.jp2");

        FlaggingFinder flaggingFinder = getFlaggingFinder();
        String mixMapping = flaggingFinder.getFileReferenceFromEvent(mixEvent);
        Assert.assertEquals(
                mixMapping,
                "B400022028241-RT1/400022028241-1/FILM-ISO-target/adresseavisen1759-400022028241-1-ISO-0001.jp2");
    }


    @Test
    public void testFilmISONodeReference5() throws Exception {
        ParsingEvent mixEvent = createAttributeParsingEvent(
                "B400022028241-RT1/400022028241-1/FILM-ISO-target/adresseavisen1759-400022028241-1-ISO-0001.jp2/contents");

        FlaggingFinder flaggingFinder = getFlaggingFinder();
        String mixMapping = flaggingFinder.getFileReferenceFromEvent(mixEvent);
        Assert.assertEquals(
                mixMapping,
                "B400022028241-RT1/400022028241-1/FILM-ISO-target/adresseavisen1759-400022028241-1-ISO-0001.jp2");
    }


    @Test
    public void testFilmISONodeReference6() throws Exception {
        ParsingEvent mixEvent = createAttributeParsingEvent(
                "B400022028241-RT1/400022028241-1/FILM-ISO-target/adresseavisen1759-400022028241-1-ISO-0001.jp2.jpylyzer.xml");

        FlaggingFinder flaggingFinder = getFlaggingFinder();
        String mixMapping = flaggingFinder.getFileReferenceFromEvent(mixEvent);
        Assert.assertEquals(
                mixMapping,
                "B400022028241-RT1/400022028241-1/FILM-ISO-target/adresseavisen1759-400022028241-1-ISO-0001.jp2");
    }


    /*---------------UNMATCHED*/


    @Test
    public void testUnmatchedNodeReference1() throws Exception {
        ParsingEvent mixEvent = createNodeBeginsParsingEvent("B400022028241-RT1/400022028241-1/UNMATCHED");

        FlaggingFinder flaggingFinder = getFlaggingFinder();
        String mixMapping = flaggingFinder.getFileReferenceFromEvent(mixEvent);
        Assert.assertEquals(
                mixMapping,
                "B400022028241-RT1/400022028241-1/UNMATCHED/adresseavisen1759-400022028241-1-0008.jp2");
    }


    @Test
    public void testUnmatchedNodeReference2() throws Exception {
        ParsingEvent mixEvent = createNodeBeginsParsingEvent(
                "B400022028241-RT1/400022028241-1/UNMATCHED/adresseavisen1759-400022028241-1-0008");

        FlaggingFinder flaggingFinder = getFlaggingFinder();
        String mixMapping = flaggingFinder.getFileReferenceFromEvent(mixEvent);
        Assert.assertEquals(
                mixMapping,
                "B400022028241-RT1/400022028241-1/UNMATCHED/adresseavisen1759-400022028241-1-0008.jp2");
    }

    @Test
    public void testUnmatchedNodeReference3() throws Exception {
        ParsingEvent mixEvent = createAttributeParsingEvent(
                "B400022028241-RT1/400022028241-1/UNMATCHED/adresseavisen1759-400022028241-1-0008.alto.xml");

        FlaggingFinder flaggingFinder = getFlaggingFinder();
        String mixMapping = flaggingFinder.getFileReferenceFromEvent(mixEvent);
        Assert.assertEquals(
                mixMapping,
                "B400022028241-RT1/400022028241-1/UNMATCHED/adresseavisen1759-400022028241-1-0008.jp2");
    }

    @Test
    public void testUnmatchedNodeReference4() throws Exception {
        ParsingEvent mixEvent = createDataFileNodeBeginsParsingEvent(
                "B400022028241-RT1/400022028241-1/UNMATCHED/adresseavisen1759-400022028241-1-0008.jp2");

        FlaggingFinder flaggingFinder = getFlaggingFinder();
        String mixMapping = flaggingFinder.getFileReferenceFromEvent(mixEvent);
        Assert.assertEquals(
                mixMapping,
                "B400022028241-RT1/400022028241-1/UNMATCHED/adresseavisen1759-400022028241-1-0008.jp2");
    }


    @Test
    public void testUnmatchedNodeReference5() throws Exception {
        ParsingEvent mixEvent = createAttributeParsingEvent(
                "B400022028241-RT1/400022028241-1/UNMATCHED/adresseavisen1759-400022028241-1-0008.jp2/contents");

        FlaggingFinder flaggingFinder = getFlaggingFinder();
        String mixMapping = flaggingFinder.getFileReferenceFromEvent(mixEvent);
        Assert.assertEquals(
                mixMapping,
                "B400022028241-RT1/400022028241-1/UNMATCHED/adresseavisen1759-400022028241-1-0008.jp2");
    }


    @Test
    public void testUnmatchedNodeReference6() throws Exception {
        ParsingEvent mixEvent = createAttributeParsingEvent(
                "B400022028241-RT1/400022028241-1/UNMATCHED/adresseavisen1759-400022028241-1-0008.jp2.jpylyzer.xml");

        FlaggingFinder flaggingFinder = getFlaggingFinder();
        String mixMapping = flaggingFinder.getFileReferenceFromEvent(mixEvent);
        Assert.assertEquals(
                mixMapping,
                "B400022028241-RT1/400022028241-1/UNMATCHED/adresseavisen1759-400022028241-1-0008.jp2");
    }


    private FlaggingFinder getFlaggingFinder() {
        return new FlaggingFinder(
                DOM.streamToDOM(
                        Thread.currentThread().getContextClassLoader().getResourceAsStream(
                                "batchStructure.xml")));
    }


    private AttributeParsingEvent createAttributeParsingEvent(String name) {
        return new AttributeParsingEvent(name) {
            @Override
            public InputStream getData() throws IOException {
                return null;
            }

            @Override
            public String getChecksum() throws IOException {
                return null;
            }
        };
    }

    private NodeBeginsParsingEvent createNodeBeginsParsingEvent(String name) {
        return new NodeBeginsParsingEvent(name);
    }

    private NodeEndParsingEvent createNodeEndParsingEvent(String name) {
        return new NodeEndParsingEvent(name);
    }

    private DataFileNodeBeginsParsingEvent createDataFileNodeBeginsParsingEvent(String name) {
        return new DataFileNodeBeginsParsingEvent(name);
    }

    private DataFileNodeEndsParsingEvent createDataFileNodeEndsParsingEvent(String name) {
        return new DataFileNodeEndsParsingEvent(name);
    }
}
