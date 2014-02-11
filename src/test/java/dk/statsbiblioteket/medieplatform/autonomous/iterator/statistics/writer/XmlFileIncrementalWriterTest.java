package dk.statsbiblioteket.medieplatform.autonomous.iterator.statistics.writer;

import java.lang.reflect.Method;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class XmlFileIncrementalWriterTest extends XmlFileTest {
    private XmlFileIncrementalWriter writerUT;

    @BeforeMethod
    public void setupMethod(Method method) {
        writerUT = createWriter(method.getName());
    }

    @Test
     public void testFileCreation() throws Exception {
        writerUT.finish();
        assertOutputEqual("<Statistics></Statistics>");
    }

    @Test
    public void testNode() throws Exception {
        writerUT.addNode("Batch", "4209");
        writerUT.endNode();
        writerUT.endNode();
        writerUT.finish();
        assertOutputEqual("<Statistics><Batch name=\"4209\"></Batch></Statistics>");
    }

    @Test
    public void testStatisticElement() throws Exception {
        writerUT.addNode("Batch", "4209");
        writerUT.addStatistic("NumberOfPages", 21354);
        writerUT.endNode();
        writerUT.finish();
        assertOutputEqual(
                "<Statistics><Batch name=\"4209\"><NumberOfPages>21354</NumberOfPages></Batch></Statistics>");
    }
}
