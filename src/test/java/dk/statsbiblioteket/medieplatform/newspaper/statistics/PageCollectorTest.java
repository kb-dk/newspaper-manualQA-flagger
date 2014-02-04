package dk.statsbiblioteket.medieplatform.newspaper.statistics;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.Properties;

import dk.statsbiblioteket.medieplatform.autonomous.iterator.common.AttributeParsingEvent;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.common.NodeEndParsingEvent;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.statistics.StatisticCollector;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.statistics.StatisticWriter;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.statistics.Statistics;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.statistics.WeightedMean;
import dk.statsbiblioteket.medieplatform.newspaper.manualQA.ConfigConstants;
import org.mockito.ArgumentCaptor;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.mockito.Mockito.*;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

public class PageCollectorTest {
    private StatisticCollector parentCollector;
    private Statistics statistics;
    private StatisticWriter writer;
    private Properties properties;

    @BeforeMethod
    public void setupMethod(Method method) {
        parentCollector = mock(StatisticCollector.class);
        statistics = mock(Statistics.class);
        when(parentCollector.getStatistics()).thenReturn(statistics);
        writer = mock(StatisticWriter.class);
        properties = new Properties();
    }

    @Test
    public void doNotIgnoreZeroAccuracyTest() throws IOException {
        PageCollector pageCollectorUT = new PageCollector();
        pageCollectorUT.initialize("page1", parentCollector, writer, properties);

        pageCollectorUT.handleAttribute(createAltoEvent("Page1.alto.xml", 0.0));

        pageCollectorUT.handleNodeEnd(new NodeEndParsingEvent("page1"));
        ArgumentCaptor<Statistics> statisticsCaptor = ArgumentCaptor.forClass(Statistics.class);
        verify(statistics).addStatistic(statisticsCaptor.capture());
        assertEquals(
                statisticsCaptor.getValue().relativeCountMap.get(PageCollector.OCR_ACCURACY_STAT),
                new WeightedMean(0.0, 1));
    }

    @Test
    public void gnoreZeroAccuracyTest() throws IOException {
        properties.setProperty(ConfigConstants.ALTO_IGNORE_ZERO_ACCURACY, "true");
        PageCollector pageCollectorUT = new PageCollector();
        pageCollectorUT.initialize("page1", parentCollector, writer, properties);

        pageCollectorUT.handleAttribute(createAltoEvent("Page1.alto.xml", 0.0));

        pageCollectorUT.handleNodeEnd(new NodeEndParsingEvent("page1"));
        ArgumentCaptor<Statistics> statisticsCaptor = ArgumentCaptor.forClass(Statistics.class);
        verify(statistics).addStatistic(statisticsCaptor.capture());
        assertNull(statisticsCaptor.getValue().relativeCountMap.get(PageCollector.OCR_ACCURACY_STAT));
    }

    private AttributeParsingEvent createAltoEvent(String name, double accuracy) {
        final String altoXmlStructure = "<alto xmlns=\"http://www.loc.gov/standards/alto/ns-v2#\">" +
                "<Layout><Page ACCURACY=\"" + accuracy + "\"></Page></Layout>" +
                "</alto>";

        return new AttributeParsingEvent(name) {
            @Override
            public InputStream getData() throws IOException {
                return new ByteArrayInputStream(altoXmlStructure.getBytes());
            }

            @Override
            public String getChecksum() throws IOException {
                throw new RuntimeException("not implemented");
            }
        };
    }
}
