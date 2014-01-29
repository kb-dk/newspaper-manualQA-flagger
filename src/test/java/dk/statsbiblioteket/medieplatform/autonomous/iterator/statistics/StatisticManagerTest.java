package dk.statsbiblioteket.medieplatform.autonomous.iterator.statistics;

import java.lang.reflect.Method;

import dk.statsbiblioteket.medieplatform.autonomous.iterator.common.NodeBeginsParsingEvent;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.common.NodeEndParsingEvent;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class StatisticManagerTest extends XmlFileTest {
    /** Instance of the StatisticManager Under Test. */
    private StatisticManager collectorUT;
    private static final String DEFAULT_BATCH = "4099";

    @BeforeMethod
    public void setupMethod(Method method) {
        collectorUT = new StatisticManager(createWriter(method.getName()));
    }

    @Test
    public void emptyBatchOutputTest() {
        collectorUT.handleNodeBegin(new NodeBeginsParsingEvent(DEFAULT_BATCH));
        collectorUT.handleNodeEnd(new NodeEndParsingEvent(DEFAULT_BATCH));
        collectorUT.handleFinish();
        assertOutputEqual(
                "<Statistics>" +
                "  <Batch name=\"4099\"></Batch>" +
                "</Statistics>");
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
        assertOutputEqual(
                "<Statistics>\n" +
                "  <Batch name=\"4099\">\n" +
                "    <Film name=\"film1\"></Film>\n" +
                "    <Film name=\"film2\"></Film>\n" +
                "    <Films>2</Films>\n" +
                "  </Batch>\n" +
                "</Statistics>");
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
        assertOutputEqual(
                "<Statistics>\n" +
                "  <Batch name=\"4099\">\n" +
                "    <Film name=\"film1\">\n" +
                "      <Edition name=\"2012-11-11-1\"></Edition>\n" +
                "      <Edition name=\"2012-11-12-1\"></Edition>\n" +
                "      <Editions>2</Editions>\n" +
                "    </Film>\n" +
                "    <Film name=\"film2\">\n" +
                "      <Edition name=\"2012-11-13-1\"></Edition>\n" +
                "      <Editions>1</Editions>\n" +
                "    </Film>\n" +
                "    <Editions>3</Editions>\n" +
                "    <Films>2</Films>\n" +
                "  </Batch>\n" +
                "</Statistics>");
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
        assertOutputEqual(
                "<Statistics>\n" +
                "  <Batch name=\"4099\">\n" +
                "    <Film name=\"film1\">\n" +
                "      <Edition name=\"2012-11-11-1\">\n" +
                "        <Pages>2</Pages>\n" +
                "      </Edition>\n" +
                "      <Editions>1</Editions>\n" +
                "      <Pages>2</Pages>\n" +
                "    </Film>\n" +
                "    <Film name=\"film2\">\n" +
                "      <Edition name=\"2012-11-13-1\">\n" +
                "        <Pages>1</Pages>\n" +
                "      </Edition>\n" +
                "      <Editions>1</Editions>\n" +
                "      <Pages>1</Pages>\n" +
                "    </Film>\n" +
                "    <Editions>2</Editions>\n" +
                "    <Films>2</Films>\n" +
                "    <Pages>3</Pages>\n" +
                "  </Batch>\n" +
                "</Statistics>");
    }
}
