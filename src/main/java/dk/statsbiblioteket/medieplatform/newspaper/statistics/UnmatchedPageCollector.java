package dk.statsbiblioteket.medieplatform.newspaper.statistics;

import dk.statsbiblioteket.medieplatform.autonomous.iterator.statistics.SinkCollector;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.statistics.StatisticCollector;

/**
 * Handles the collection of page level statistics.
 *
 * Uses SinkCollectors as children.
 */
public class UnmatchedPageCollector extends StatisticCollector {
    @Override
    public StatisticCollector createChild(String event) {
        return new SinkCollector();
    }

    /**
     * Suppress creation of unmatched page nodes in the output.
     */
    @Override
    protected boolean writeNode() {
        return false;
    }

    @Override
    public String getType() {
        return "UnmatchedPage";
    }
}
