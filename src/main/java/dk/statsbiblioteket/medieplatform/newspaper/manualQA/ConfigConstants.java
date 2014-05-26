package dk.statsbiblioteket.medieplatform.newspaper.manualQA;

public class ConfigConstants {
    private ConfigConstants(){}

    public static final String FLAG_IGNORE_COLORS_BELOW = "flag.ignoreColorsBelow";
    public static final String MAX_VAL_TO_DEEM_A_COLOR_MISSING = "flag.maxValueToDeemAColorMissing";

    public static final String NUMBER_OF_MISSING_COLORS_ALLOWED = "flag.missingcolors.numberOfMissingColorsAllowed";

    public static final String CHOPPY_CHECK_MAX_IRREGULARITIES = "flag.choppyCurve.checkMaxIrregularities";
    public static final String CHOPPY_CHECK_THRESHOLD = "flag.choppyCurve.checkthreshold";

    public static final String EDITION_MODS_MAX_EDITIONS_PER_DAY = "flag.editions.maxeditionsperday";

    public static final String DARKNESS_MAX_NUM_OF_DARK_IMAGES_ALLOWED = "flag.darkness.MaxNumOfDarkImagesAllowed";
    public static final String DARKNESS_LOWEST_ACCEPTABLE_PEAK_POSITION = "flag.darkness.lowestAcceptablePeakPosition";
    public static final String DARKNESS_MIN_NUM_OF_TEXT_LINES = "flag.darkness.minNumOfTextLines";
    public static final String DARKNESS_HISTOGRAM_CHECKER_ON = "flag.darkness.histogramCheckerOn";

    public static final String END_SPIKE_THRESHOLD = "flag.endspike.threshold";
    public static final String END_SPIKE_MIN_COLOR_CONSIDERED_BLACK = "flag.endspike.minColorConsideredBlack";
    public static final String END_SPIKE_MIN_COLOR_CONSIDERED_WHITE = "flag.endspike.minColorConsideredWhite";
    public static final String END_SPIKE_MAX_COLOR_CONSIDERED_WHITE = "flag.endspike.maxColorConsideredWhite";
    public static final String END_SPIKE_MAX_PERCENT_ALLOWED_NEAR_BLACK = "flag.endspike.maxPercentAllowedNearBlack";
    public static final String END_SPIKE_MAX_PERCENT_ALLOWED_NEAR_WHITE = "flag.endspike.maxPercentAllowedNearWhite";

    public static final String SCANNER_MANUFACTURERS = "flag.mix.scannermanufacturers";
    public static final String SCANNER_MODELS = "flag.mix.scannermodels";
    public static final String SCANNER_MODEL_NUMBERS = "flag.mix.modelnumbers";
    public static final String SCANNER_SERIAL_NOS = "flag.mix.scannerserialnos";
    public static final String SCANNER_SOFTWARES = "flag.mix.scannersoftwares";
    public static final String IMAGE_PRODUCERS = "flag.mix.imageproducers";
    public static final String MIN_IMAGE_WIDTH = "flag.mix.minimagewidth";
    public static final String MAX_IMAGE_WIDTH = "flag.mix.maximagewidth";
    public static final String MIN_IMAGE_HEIGHT = "flag.mix.minimageheight";
    public static final String MAX_IMAGE_HEIGHT = "flag.mix.maximageheight";
    public static final String MIX_HANDLER_ON = "flag.mix.handler.on";

    public static final String MINIMUM_ALTO_AVERAGE_ACCURACY = "flag.alto.accuracy";
    public static final String MINIMUM_ALTO_PERFILE_ACCURACY = "flag.alto.accuracy.perfile";
    public static final String ALTO_IGNORE_ZERO_ACCURACY = "flag.alto.zeroaccuracy.ignore";
    public static final String ALTO_WORD_ACCURACY_CHECKER_ON = "flag.alto.checker.on";

    public static final String MAX_FLAGS = "flag.maxtotalflags";
    
    public static final String MANUAL_QA_INPUTFILES_DIR = "manualqa.input.files.dir";
}
