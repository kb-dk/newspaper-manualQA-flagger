package dk.statsbiblioteket.medieplatform.newspaper.statistics;

import dk.statsbiblioteket.medieplatform.autonomous.iterator.statistics.Statistics;

/**
 * Overides the addSubstatistic operation so PageCollector.PAGES_IN_SECTIONS_STAT are converted into
 * FilmCollector.SECTIONS_STAT, eg. the numberOfPages are ignored and each section is only counted once.
 */
public class FilmStatistics extends Statistics {
    @Override
    public void addSubstatistic(String name, Statistics statisticsToAdd) {
        if (name.equals(PageCollector.PAGES_IN_SECTIONS_STAT)) {
            if (!substatisticsMap.containsKey(FilmCollector.SECTIONS_STAT)) {
                substatisticsMap.put(FilmCollector.SECTIONS_STAT, new SectionStatistics());
            }
            super.addSubstatistic(FilmCollector.SECTIONS_STAT, statisticsToAdd);
        } else {
            super.addSubstatistic(name, statisticsToAdd);
        }
    }

    /**
     * Overrides the addCount so the count number is ignore, and the count is just increment by 1.
     */
    private static class CountNormalizerStatistics extends Statistics  {
        @Override
        public void addCount(String name, Long countToAdd) {
            super.addCount(name, 1L);
        }
    }

    private static class SectionStatistics extends CountNormalizerStatistics {

        @Override
        public String getSummary() {
            return countMap.size() + "";
        }
    }
}
