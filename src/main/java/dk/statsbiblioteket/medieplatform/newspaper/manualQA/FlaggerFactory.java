package dk.statsbiblioteket.medieplatform.newspaper.manualQA;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import dk.statsbiblioteket.medieplatform.autonomous.Batch;
import dk.statsbiblioteket.medieplatform.autonomous.ResultCollector;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.eventhandlers.EventHandlerFactory;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.eventhandlers.TreeEventHandler;
import dk.statsbiblioteket.medieplatform.newspaper.manualQA.flagging.FlaggingCollector;

public class FlaggerFactory implements EventHandlerFactory {
    // MissingColorsHistogramChecker:
    private static final int MISSING_COLORS_NUMBER_OF_MISSING_ALLOWED = 0;
    private static final int MISSING_COLORS_MAX_VAL_TO_DEEM_A_COLOR_MISSING = 10;

    // ChoppyCurveHistogramChecker:
    // How much a value is allowed to deviate from the average of its two neighbours (in pct, >0) before
    // it is considered an irregularity (only happens if both neighbours are either higher or lower)
    private static final double CHOPPY_CHECK_THRESHOLD = 0.5;
    // The maximum number of peaks/valleys allowed before flagged as an error
    private static final int CHOPPY_CHECK_MAX_IRREGULARITIES = 4;

    // EditionModsHandler:
    // The maximum number of editions of a newspaper per day before we raise a flag
    private static final int EDITION_MODS_MAX_EDITIONS_PER_DAY = 3;

    // DarknessHistogramChecker:
    private static final int DARKNESS_MAX_NUM_OF_DARK_IMAGES_ALLOWED = 300;
    private static final int DARKNESS_LOWEST_HISTOGRAM_INDEX_NOT_CONSIDERED_BLACK = 3;
    private static final int DARKNESS_LOWEST_ACCEPTABLE_PEAK_POSITION = 128;
    // Min number of text lines on page before we consider this a text (non image-only) page, and therefore check for darkness
    private static final int DARKNESS_MIN_NUM_OF_TEXT_LINES = 50;

    // EndSpikeHistogramChecker:
    private static final double END_SPIKE_THRESHOLD = 0.1;
    private static final int END_SPIKE_MIN_COLOR_CONSIDERED_BLACK = 0;
    private static final int END_SPIKE_MAX_COLOR_CONSIDERED_BLACK = 2;
    private static final int END_SPIKE_MIN_COLOR_CONSIDERED_WHITE = 255;
    private static final int END_SPIKE_MAX_COLOR_CONSIDERED_WHITE = 255;
    private static final double END_SPIKE_MAX_PERCENT_ALLOWED_NEAR_BLACK = 2;
    private static final double END_SPIKE_MAX_PERCENT_ALLOWED_NEAR_WHITE = 0.5;

    private final ResultCollector resultCollector;
    private final Batch batch;
    private FlaggingCollector flaggingCollector;
    private Properties properties;

    public FlaggerFactory(ResultCollector resultCollector, Batch batch,
                          FlaggingCollector flaggingCollector, Properties properties) {
        this.resultCollector = resultCollector;
        this.batch = batch;
        this.flaggingCollector = flaggingCollector;
        this.properties = properties;
    }

    @Override
    public List<TreeEventHandler> createEventHandlers() {
        ArrayList<TreeEventHandler> treeEventHandlers = new ArrayList<>();
        treeEventHandlers.add(new MissingColorsHistogramChecker(resultCollector, flaggingCollector,
                MISSING_COLORS_NUMBER_OF_MISSING_ALLOWED, MISSING_COLORS_MAX_VAL_TO_DEEM_A_COLOR_MISSING));
        treeEventHandlers.add(new ChoppyCurveHistogramChecker(resultCollector, flaggingCollector, CHOPPY_CHECK_THRESHOLD,
                CHOPPY_CHECK_MAX_IRREGULARITIES));
        treeEventHandlers.add(new EditionModsHandler(resultCollector, flaggingCollector, batch,
                EDITION_MODS_MAX_EDITIONS_PER_DAY));
        treeEventHandlers.add(new FilmHandler(resultCollector, flaggingCollector));
        treeEventHandlers.add(new MixHandler(resultCollector, properties, flaggingCollector));
        treeEventHandlers.add(new AltoWordAccuracyChecker(resultCollector, flaggingCollector, properties));
        treeEventHandlers.add(new DarknessHistogramChecker(resultCollector, flaggingCollector, batch,
                DARKNESS_MAX_NUM_OF_DARK_IMAGES_ALLOWED, DARKNESS_LOWEST_HISTOGRAM_INDEX_NOT_CONSIDERED_BLACK,
                DARKNESS_LOWEST_ACCEPTABLE_PEAK_POSITION, DARKNESS_MIN_NUM_OF_TEXT_LINES));
        treeEventHandlers.add(new EndSpikeHistogramChecker(resultCollector, flaggingCollector, END_SPIKE_THRESHOLD,
                END_SPIKE_MIN_COLOR_CONSIDERED_BLACK, END_SPIKE_MAX_COLOR_CONSIDERED_BLACK, END_SPIKE_MIN_COLOR_CONSIDERED_WHITE,
                END_SPIKE_MAX_COLOR_CONSIDERED_WHITE, END_SPIKE_MAX_PERCENT_ALLOWED_NEAR_BLACK,
                END_SPIKE_MAX_PERCENT_ALLOWED_NEAR_WHITE));
        return treeEventHandlers;
    }
}
