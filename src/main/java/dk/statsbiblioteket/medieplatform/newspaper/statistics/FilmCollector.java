package dk.statsbiblioteket.medieplatform.newspaper.statistics;

import dk.statsbiblioteket.medieplatform.autonomous.iterator.common.NodeEndParsingEvent;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.statistics.SinkCollector;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.statistics.StatisticCollector;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.statistics.model.Statistics;

/**
 * Handles the collection of film level statistics.
 */
public class FilmCollector extends StatisticCollector {
    public static final String SECTIONS_STAT = "Sections";
    public static final String EDITION_DATE_STAT = "Edition-dates";
    private final FilmStatistics statistics = new FilmStatistics();
    private final Statistics editionDates = new Statistics();

    @Override
    protected StatisticCollector createChild(String eventName) {
        if (eventName.equals("UNMATCHED")) {
            return new UnmatchedCollector();
        } else if (eventName.equals("FILM-ISO-target")) {
            return new SinkCollector();
        } else {
            editionDates.addCount("E" + getSimpleName(eventName).substring(0, eventName.lastIndexOf('-')), 1L);
            return new EditionCollector();
        }
    }

    /**
     * @return A specialized FilmStatistics.
     *
     * @see dk.statsbiblioteket.medieplatform.newspaper.statistics.FilmStatistics
     */
    @Override
    public Statistics getStatistics() {
        return statistics;
    }

    @Override
    public StatisticCollector handleNodeEnd(NodeEndParsingEvent event) {
        getStatistics().addSubstatistic(EDITION_DATE_STAT, editionDates);
        return super.handleNodeEnd(event);
    }
}
