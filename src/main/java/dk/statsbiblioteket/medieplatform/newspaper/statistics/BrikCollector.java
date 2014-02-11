package dk.statsbiblioteket.medieplatform.newspaper.statistics;

import dk.statsbiblioteket.medieplatform.autonomous.iterator.statistics.SinkCollector;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.statistics.StatisticCollector;

/**
 * Handles the collection of brik level statistics. No statistics are currently handled
 *
 * Uses SinkCollectors as children.
 */
public class BrikCollector extends StatisticCollector {
    public BrikCollector() {
        doNotWrite();
    }

    @Override
    protected StatisticCollector createChild(String eventName) {
        return new SinkCollector();
    }
}
