package dk.statsbiblioteket.medieplatform.autonomous.iterator.statistics;

import java.lang.reflect.Method;

import dk.statsbiblioteket.medieplatform.autonomous.iterator.common.NodeBeginsParsingEvent;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.common.NodeEndParsingEvent;
import dk.statsbiblioteket.medieplatform.newspaper.statistics.EditionCollector;
import dk.statsbiblioteket.medieplatform.newspaper.statistics.FilmCollector;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class StatisticCollectorTest extends XmlFileTest {
    /** Instance of the StatisticCollector Under Test. */
    private StatisticCollector collectorUT;
    private static final String DEFAULT_BATCH = "4099";

    @BeforeMethod
    public void setupMethod(Method method) {
        collectorUT = new StatisticCollector(createWriter(method.getName()));
    }

    @Test
    public void batchNodeLocationTest() {
        collectorUT.handleNodeBegin(new NodeBeginsParsingEvent(DEFAULT_BATCH));
        assertEquals(collectorUT.getCollector().getName(), DEFAULT_BATCH);
    }

    @Test
    public void filmNodeLocationTest() {
        collectorUT.handleNodeBegin(new NodeBeginsParsingEvent(DEFAULT_BATCH));
        String filmNode= DEFAULT_BATCH + "/film1";
        collectorUT.handleNodeBegin(new NodeBeginsParsingEvent(filmNode));
        GeneralCollector filmCollector = collectorUT.getCollector();
        assertNotNull(filmCollector);
        assertTrue(filmCollector instanceof FilmCollector);
        assertEquals(filmCollector.getName(), filmNode);
    }

    @Test
    public void editionNodeLocationTest() {
        collectorUT.handleNodeBegin(new NodeBeginsParsingEvent(DEFAULT_BATCH));
        collectorUT.handleNodeBegin(new NodeBeginsParsingEvent(DEFAULT_BATCH + "/FILM1"));
        String nodeName = DEFAULT_BATCH+"/FILM1/2012-11-11-1";
        collectorUT.handleNodeBegin(new NodeBeginsParsingEvent(nodeName));
        GeneralCollector editionCollector = collectorUT.getCollector();
        assertNotNull(editionCollector);
        assertTrue(editionCollector instanceof EditionCollector);
        assertEquals(editionCollector.getName(), nodeName);
    }

    @Test
    public void emptyBatchOutputTest() {
        collectorUT.handleNodeBegin(new NodeBeginsParsingEvent(DEFAULT_BATCH));
        collectorUT.handleNodeEnd(new NodeEndParsingEvent(DEFAULT_BATCH));
        collectorUT.handleFinish();
        assertOutputEqual("<Statistics><Batch name=\"4099\"></Batch></Statistics>");
    }

    @Test
    public void filmCountTest() {
        collectorUT.handleNodeBegin(new NodeBeginsParsingEvent(DEFAULT_BATCH));
        String FILM1= "film1";
        String FILM2= "film2";
        collectorUT.handleNodeBegin(new NodeBeginsParsingEvent(DEFAULT_BATCH + "/" + FILM1));
        collectorUT.handleNodeEnd(new NodeEndParsingEvent(DEFAULT_BATCH + "/" + FILM1));
        collectorUT.handleNodeBegin(new NodeBeginsParsingEvent(DEFAULT_BATCH + "/" + FILM2));
        collectorUT.handleNodeEnd(new NodeEndParsingEvent(DEFAULT_BATCH + "/" + FILM2));
        collectorUT.handleNodeEnd(new NodeEndParsingEvent(DEFAULT_BATCH));
        collectorUT.handleFinish();
    }

    @Test
    public void editionCountTest() {
        collectorUT.handleNodeBegin(new NodeBeginsParsingEvent(DEFAULT_BATCH));
        String FILM1= DEFAULT_BATCH + "/film1";
        String EDITION1_1 = FILM1 + "/2012-11-11-1";
        String EDITION1_2 = FILM1 + "/2012-11-12-1";
        String FILM2= DEFAULT_BATCH + "/film2";
        String EDITION2_1 = FILM1 + "/2012-11-13-1";
        collectorUT.handleNodeBegin(new NodeBeginsParsingEvent(FILM1));
        collectorUT.handleNodeBegin(new NodeBeginsParsingEvent(EDITION1_1));
        collectorUT.handleNodeEnd(new NodeEndParsingEvent(EDITION1_1));
        collectorUT.handleNodeBegin(new NodeBeginsParsingEvent(EDITION1_2));
        collectorUT.handleNodeEnd(new NodeEndParsingEvent(EDITION1_2));
        collectorUT.handleNodeEnd(new NodeEndParsingEvent(FILM1));

        collectorUT.handleNodeBegin(new NodeBeginsParsingEvent(FILM2));
        collectorUT.handleNodeBegin(new NodeBeginsParsingEvent(EDITION2_1));
        collectorUT.handleNodeEnd(new NodeEndParsingEvent(EDITION2_1));
        collectorUT.handleNodeEnd(new NodeEndParsingEvent(FILM2));

        collectorUT.handleNodeEnd(new NodeEndParsingEvent(DEFAULT_BATCH));
        collectorUT.handleFinish();
    }

    @Test
    public void pageNodeCountTest() {
        collectorUT.handleNodeBegin(new NodeBeginsParsingEvent(DEFAULT_BATCH));
        String FILM1= DEFAULT_BATCH + "/film1";
        String EDITION1_1 = FILM1 + "/2012-11-11-1";
        String PAGE1_1 = EDITION1_1 + "/page1";
        String PAGE1_2 = EDITION1_1 + "/page2";
        String FILM2= DEFAULT_BATCH + "/film2";
        String EDITION2_1 = FILM1 + "/2012-11-13-1";
        String PAGE2_1 = EDITION2_1 + "/page3";
        collectorUT.handleNodeBegin(new NodeBeginsParsingEvent(FILM1));
        collectorUT.handleNodeBegin(new NodeBeginsParsingEvent(EDITION1_1));
        collectorUT.handleNodeBegin(new NodeBeginsParsingEvent(PAGE1_1));
        collectorUT.handleNodeEnd(new NodeEndParsingEvent(PAGE1_1));
        collectorUT.handleNodeBegin(new NodeBeginsParsingEvent(PAGE1_2));
        collectorUT.handleNodeEnd(new NodeEndParsingEvent(PAGE1_2));
        collectorUT.handleNodeEnd(new NodeEndParsingEvent(EDITION1_1));
        collectorUT.handleNodeEnd(new NodeEndParsingEvent(FILM1));

        collectorUT.handleNodeBegin(new NodeBeginsParsingEvent(FILM2));
        collectorUT.handleNodeBegin(new NodeBeginsParsingEvent(EDITION2_1));
        collectorUT.handleNodeBegin(new NodeBeginsParsingEvent(PAGE2_1));
        collectorUT.handleNodeEnd(new NodeEndParsingEvent(PAGE2_1));
        collectorUT.handleNodeEnd(new NodeEndParsingEvent(EDITION2_1));
        collectorUT.handleNodeEnd(new NodeEndParsingEvent(FILM2));

        collectorUT.handleNodeEnd(new NodeEndParsingEvent(DEFAULT_BATCH));
        collectorUT.handleFinish();
    }
}
