package dk.statsbiblioteket.medieplatform.newspaper.statistics;

import dk.statsbiblioteket.medieplatform.autonomous.iterator.common.AttributeParsingEvent;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.common.NodeBeginsParsingEvent;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.statistics.GeneralCollector;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.statistics.StatisticWriter;

/**
 * Handles the collection of statistics from the unmatched node.
 *
 * Uses SinkCollectors as children.
 */
public class UnmatchedCollector extends GeneralCollector {
    public static final String NUMBER_OF_UNMATCHED_PAGES_STAT = "NumberOfUnmatchedPages";

    public UnmatchedCollector(String name, GeneralCollector parentCollector, StatisticWriter writer) {
        super(name, parentCollector, writer);
    }

    @Override
    public GeneralCollector handleNodeBegin(NodeBeginsParsingEvent event) {
        if (event.getName().split("/").length == 4) {
            getStatistics().addCount(NUMBER_OF_UNMATCHED_PAGES_STAT, 1);
            return new PageCollector(event.getName(), this, getWriter());
        } else throw new RuntimeException("Unexpected event " + event);
    }

    @Override
    public void handleAttribute(AttributeParsingEvent event) {

    }

    @Override
    public String getType() {
        return "Unmatched";
    }
}
