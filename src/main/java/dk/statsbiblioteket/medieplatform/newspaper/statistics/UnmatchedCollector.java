package dk.statsbiblioteket.medieplatform.newspaper.statistics;

import dk.statsbiblioteket.medieplatform.autonomous.iterator.statistics.StatisticCollector;

/**
 * Handles the collection of statistics from the unmatched node.
 *
 * Uses SinkCollectors as children.
 */
public class UnmatchedCollector extends StatisticCollector {

    @Override
    public StatisticCollector createChild(String event) {
        return new UnmatchedPageCollector();
    }

    @Override
    public String getType() {
        return "Unmatched";
    }

    /**
     * Suppress creation of unmatches nodes in the output
     */
    @Override
    protected boolean writeNode() {
        return false;
    }

    /**
     * Returns null, no need to count UNMATCHED node, hopefully only one will appear.
     */
    @Override
    public String getStatisticsName() {
        return null;
    }
}
