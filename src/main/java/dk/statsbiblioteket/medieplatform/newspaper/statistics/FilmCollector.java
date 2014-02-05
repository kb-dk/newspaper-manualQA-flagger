package dk.statsbiblioteket.medieplatform.newspaper.statistics;

import dk.statsbiblioteket.medieplatform.autonomous.iterator.statistics.SinkCollector;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.statistics.StatisticCollector;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.statistics.Statistics;

/**
 * Handles the collection of film level statistics.
 */
public class FilmCollector extends StatisticCollector {
    public static final String SECTIONS_STAT = "Sections";
    private final FilmStatistics statistics;

    public FilmCollector() {
        statistics = new FilmStatistics();
    }

    @Override
    protected StatisticCollector createChild(String eventName) {
        if (eventName.equals("UNMATCHED")) {
            return new UnmatchedCollector();
        } else if (eventName.equals("FILM-ISO-target")) {
            return new SinkCollector();
        } else {
            return new EditionCollector();
        }
    }
    @Override
    public String getType() {
        return "Film";
    }

    /**
     * @return A specialized FilmStatistics.
     *
     * @see dk.statsbiblioteket.medieplatform.newspaper.statistics.FilmStatistics
     */
    @Override
    protected Statistics getStatistics() {
        return statistics;
    }
}
