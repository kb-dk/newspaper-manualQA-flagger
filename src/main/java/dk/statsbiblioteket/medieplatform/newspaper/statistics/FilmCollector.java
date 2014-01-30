package dk.statsbiblioteket.medieplatform.newspaper.statistics;

import dk.statsbiblioteket.medieplatform.autonomous.iterator.common.AttributeParsingEvent;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.common.NodeBeginsParsingEvent;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.statistics.StatisticCollector;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.statistics.SinkCollector;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.statistics.StatisticWriter;
/**
 * Handles the collection of film level statistics.
 */
public class FilmCollector extends StatisticCollector {
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
}
