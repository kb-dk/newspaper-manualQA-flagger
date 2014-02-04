package dk.statsbiblioteket.medieplatform.newspaper.statistics;

import java.util.Properties;

import dk.statsbiblioteket.medieplatform.autonomous.iterator.statistics.SinkCollector;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.statistics.StatisticCollector;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.statistics.StatisticWriter;

/**
 * Handles the collection of batch level statistics.
 */
public class BatchCollector extends StatisticCollector {
    public BatchCollector(String name, StatisticWriter writer, Properties properties) {
        initialize(name, null, writer, properties);
    }

    @Override
    protected StatisticCollector createChild(String eventName) {
        if (eventName.equals("WORKSHIFT-ISO-TARGET")) return new SinkCollector();
        else return new FilmCollector();
    }

    @Override
    public String getType() {
        return "Batch";
    }
}
