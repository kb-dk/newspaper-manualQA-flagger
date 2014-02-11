package dk.statsbiblioteket.medieplatform.newspaper.statistics;

import dk.statsbiblioteket.medieplatform.autonomous.iterator.statistics.StatisticCollector;

/**
 * Handles the collection of statistics from the unmatched node.
 *
 * Uses SinkCollectors as children.
 */
public class UnmatchedCollector extends StatisticCollector {
    /** No output */
    public UnmatchedCollector() {
        doNotWrite();
    }

    @Override
    public StatisticCollector createChild(String event) {
        return new UnmatchedPageCollector();
    }
}
