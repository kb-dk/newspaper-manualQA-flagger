package dk.statsbiblioteket.medieplatform.newspaper.statistics;

import dk.statsbiblioteket.medieplatform.autonomous.iterator.common.AttributeParsingEvent;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.common.NodeBeginsParsingEvent;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.statistics.GeneralCollector;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.statistics.SinkCollector;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.statistics.StatisticWriter;

public class PageCollector extends GeneralCollector {
    public static final String NUMBER_OF_SECTIONS_STAT = "NumberOfSections";
    public static final String OCR_PERCENTAGE_STAT = "OCRPercentage";
    public static final String OCR_RELATIVE_STAT = "OCRRelative";

    public PageCollector(String name, GeneralCollector parentCollector, StatisticWriter writer) {
        super(name, parentCollector, writer);
        getStatistics().addRelative(OCR_PERCENTAGE_STAT, new RelativeCount(40, 1));
        getStatistics().addRelative(OCR_RELATIVE_STAT, new RelativeCount(40, 100));
        getStatistics().addCount(NUMBER_OF_SECTIONS_STAT, 7);
    }

    @Override
    public GeneralCollector handleNodeBegin(NodeBeginsParsingEvent event) {
        return new SinkCollector(event.getName(), this);
    }

    @Override
    public void handleAttribute(AttributeParsingEvent event) {

    }

    @Override
    public String getType() {
        return "Page ";
    }
}
