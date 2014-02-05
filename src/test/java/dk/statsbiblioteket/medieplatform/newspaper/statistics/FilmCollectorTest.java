package dk.statsbiblioteket.medieplatform.newspaper.statistics;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Properties;

import dk.statsbiblioteket.medieplatform.autonomous.iterator.common.NodeEndParsingEvent;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.statistics.StatisticCollector;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.statistics.StatisticWriter;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.statistics.Statistics;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.mockito.Mockito.*;

public class FilmCollectorTest {
        private StatisticCollector parentCollector;
        private StatisticWriter writer;
        private Properties properties;

        @BeforeMethod
        public void setupMethod(Method method) {
            parentCollector = mock(StatisticCollector.class);
            writer = mock(StatisticWriter.class);
            properties = new Properties();
        }

    @Test
    public void sectionsInEditionTest() throws IOException {
        FilmCollector filmCollectorUT = new FilmCollector();
        filmCollectorUT.initialize("Film1", parentCollector, writer, properties);

        addPagesInSectionStat(filmCollectorUT, "Sektion 1");
        addPagesInSectionStat(filmCollectorUT, "Sektion 1");
        addPagesInSectionStat(filmCollectorUT, "Sektion 2");
        filmCollectorUT.handleNodeEnd(new NodeEndParsingEvent("Film1"));
        verify(writer).addNode(filmCollectorUT.getType(), filmCollectorUT.getName());
        verify(writer).addNode(FilmCollector.SECTIONS_STAT, "2");
        verify(writer).addStatistic("Sektion 1", 2L);
        verify(writer).addStatistic("Sektion 2", 1L);
        verify(writer, times(0)).addStatistic(PageCollector.PAGES_IN_SECTIONS_STAT, 1L);
    }

    private void addPagesInSectionStat(FilmCollector filmCollectorUT, String section) {
        Statistics pagesInSectionStat = new Statistics();
        pagesInSectionStat.addCount(section, 1L);
        Statistics pageStatistics = new Statistics();
        pageStatistics.addSubstatistic(PageCollector.PAGES_IN_SECTIONS_STAT, pagesInSectionStat);
        filmCollectorUT.addStatistics(pageStatistics);
    }
}
