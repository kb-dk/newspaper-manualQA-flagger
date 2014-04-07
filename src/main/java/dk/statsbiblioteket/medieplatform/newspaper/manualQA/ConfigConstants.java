package dk.statsbiblioteket.medieplatform.newspaper.manualQA;

/**
 * Created by csr on 15/01/14.
 */
public class ConfigConstants {

    private ConfigConstants(){}

    public static final String NUMBER_OF_MISSING_COLORS_ALLOWED = "flag.numberofmissingcolorsallowed";
    public static final String MAX_VAL_TO_DEEM_A_COLOR_MISSING = "flag.maxvaluetodeemacolormissing";

    public static final String CHOPPY_CHECK_THRESHOLD = "flag.choppycheckthreshold";
    public static final String CHOPPY_CHECK_MAX_IRREGULARITIES = "flag.maxvaluetodeemacolormissing";

    public static final String EDITION_MODS_MAX_EDITIONS_PER_DAY = "flag.maxeditionsperday";

    public static final String DARKNESS_MAX_NUM_OF_DARK_IMAGES_ALLOWED = "flag.darknessmaxnumofdarkimagesallowed";
    public static final String DARKNESS_LOWEST_HISTOGRAM_INDEX_NOT_CONSIDERED_BLACK
            = "flag.darknesslowesthistogramindexnotconsideredblack";
    public static final String DARKNESS_LOWEST_ACCEPTABLE_PEAK_POSITION = "flag.darknesslowestacceptablepeakposition";
    public static final String DARKNESS_MIN_NUM_OF_TEXT_LINES = "flag.darknessminnumoftextlines";
    public static final String DARKNESS_HISTOGRAM_CHECKER_ON = "flag.darknesshistogramcheckeron";

    public static final String END_SPIKE_THRESHOLD = "flag.endspikethreshold";
    public static final String END_SPIKE_MIN_COLOR_CONSIDERED_BLACK = "flag.endspikemincolorconsideredblack";
    public static final String END_SPIKE_MAX_COLOR_CONSIDERED_BLACK = "flag.endspikemaxcolorconsideredblack";
    public static final String END_SPIKE_MIN_COLOR_CONSIDERED_WHITE = "flag.endspikemincolorconsideredwhite";
    public static final String END_SPIKE_MAX_COLOR_CONSIDERED_WHITE = "flag.endspikemaxcolorconsideredwhite";
    public static final String END_SPIKE_MAX_PERCENT_ALLOWED_NEAR_BLACK = "flag.endspikemaxpercentallowednearblack";
    public static final String END_SPIKE_MAX_PERCENT_ALLOWED_NEAR_WHITE = "flag.endspikemaxpercentallowednearwhite";

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
