package dk.statsbiblioteket.medieplatform.newspaper.statistics;

import dk.statsbiblioteket.medieplatform.autonomous.iterator.common.AttributeParsingEvent;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.common.NodeBeginsParsingEvent;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.statistics.GeneralCollector;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.statistics.SinkCollector;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.statistics.StatisticWriter;

/**
 * Handles the collection of batch level statistics.
 */
public class BatchCollector extends GeneralCollector {
    public static final String NUMBER_OF_FILMS_STAT = "NumberOfFilms";

    public BatchCollector(String name, StatisticWriter writer) {
        super(name, null, writer);
    }

    @Override
    public GeneralCollector handleNodeBegin(NodeBeginsParsingEvent event) {
        String[] nameParts = event.getName().split("/");
        if (nameParts.length == 2) {
            if (nameParts[1].equals("WORKSHIFT-ISO-TARGET")) {
                return new SinkCollector(event.getName(), this);
            } else {
                getStatistics().addCount(NUMBER_OF_FILMS_STAT, 1);
                return new FilmCollector(event.getName(), this, getWriter());
            }
        } else {
            throw new RuntimeException("Received unexpected " + event.getName() + " event.");
        }
    }

    @Override
    public void handleAttribute(AttributeParsingEvent event) {

    }

    @Override
    public String getType() {
        return "Batch";
    }
}
