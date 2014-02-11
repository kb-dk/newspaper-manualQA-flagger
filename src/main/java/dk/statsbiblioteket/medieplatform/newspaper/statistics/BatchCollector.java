package dk.statsbiblioteket.medieplatform.newspaper.statistics;

import dk.statsbiblioteket.medieplatform.autonomous.iterator.statistics.SinkCollector;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.statistics.StatisticCollector;

/**
 * Handles the collection of batch level statistics.
 */
public class BatchCollector extends StatisticCollector {

    @Override
    protected StatisticCollector createChild(String eventName) {
        if (eventName.equals("WORKSHIFT-ISO-TARGET")) return new SinkCollector();
        else return new FilmCollector();
    }
}
