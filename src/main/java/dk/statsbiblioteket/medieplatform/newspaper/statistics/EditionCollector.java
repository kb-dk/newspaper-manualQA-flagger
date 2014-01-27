package dk.statsbiblioteket.medieplatform.newspaper.statistics;

import dk.statsbiblioteket.medieplatform.autonomous.iterator.statistics.StatisticCollector;

/**
 * Handles the collection of edition level statistics.
 */
public class EditionCollector extends StatisticCollector {
    @Override
    public StatisticCollector createChild(String event) {
            if (event.endsWith("brik")) {
                return new BrikCollector();
            } else {
                return new PageCollector();
            }
    }

    @Override
    public String getType() {
        return "Edition";
    }
}
