package dk.statsbiblioteket.medieplatform.newspaper.statistics;

import dk.statsbiblioteket.medieplatform.autonomous.iterator.common.AttributeParsingEvent;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.common.NodeBeginsParsingEvent;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.statistics.GeneralCollector;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.statistics.StatisticWriter;

public class FilmCollector extends GeneralCollector {
    public static final String NUMBER_OF_EDITIONS_STAT = "NumberOfEditions";

    public FilmCollector(String name, GeneralCollector parentCollector, StatisticWriter writer) {
        super(name, parentCollector, writer);
    }

    @Override
    public GeneralCollector handleNodeBegin(NodeBeginsParsingEvent event) {
        String[] nameParts = event.getName().split("/");
        if (nameParts.length == 3) {
            if (nameParts[2].equals("UNMATCHED")) {
                return new UnmatchedCollector(event.getName(), this, getWriter());
            }
            getStatistics().addCount(NUMBER_OF_EDITIONS_STAT, 1);
            return new EditionCollector(event.getName(), this, getWriter());
        } else {
            throw new RuntimeException("");
        }
    }

    @Override
    public void handleAttribute(AttributeParsingEvent event) {

    }

    @Override
    public String getType() {
        return "Film";
    }
}
