package dk.statsbiblioteket.medieplatform.newspaper.manualQA;

import dk.statsbiblioteket.medieplatform.newspaper.histogram.ColorSchemeType;
import dk.statsbiblioteket.medieplatform.newspaper.histogram.ColorType;
import dk.statsbiblioteket.medieplatform.newspaper.histogram.ColorsType;
import dk.statsbiblioteket.medieplatform.newspaper.histogram.HistogramType;
import dk.statsbiblioteket.medieplatform.newspaper.histogram.ObjectFactory;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.List;


/**
 * This class represents a Histogram. It can be read from xml or from an array of long, and can be exported to xml or
 * an array of long
 */
public class Histogram {

    private final ObjectFactory objectFactory;
    private Marshaller marshaller;
    private Unmarshaller unmarshaller;

    private long[] values;

    /**
     * private constructor. Initialises the jaxb stuff
     * @throws JAXBException
     */
    private Histogram() throws JAXBException {
        objectFactory = new ObjectFactory();
        JAXBContext context = JAXBContext.newInstance(ObjectFactory.class);
        marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        unmarshaller = context.createUnmarshaller();
    }

    /**
     * Construct the Histogram from an Array of Longs. The array should be of length 255.
     * @param values
     * @throws JAXBException
     */
    public Histogram(long[] values) throws JAXBException {
        this();
        this.values = values;
    }

    /**
     * Create the Histogram from an xml representation.
     * @param xml
     * @throws JAXBException
     */
    public Histogram(InputStream xml) throws JAXBException {
        this();
        JAXBElement<HistogramType> histrogramJaxbXml = (JAXBElement<HistogramType>) unmarshaller.unmarshal(xml);
        HistogramType histrogramXml = histrogramJaxbXml.getValue();
        List<ColorType> colorList = histrogramXml.getColors().getColor();

        values = new long[255];
        for (ColorType colorType : colorList) {
            values[colorType.getCode()] = colorType.getCount();
        }
    }


    /**
     * Export the histogram as xml
     * @return the histogram as xml
     * @throws JAXBException
     */
    public String toXml() throws JAXBException {
        HistogramType histogram = objectFactory.createHistogramType();
        ColorSchemeType colorScheme = objectFactory.createColorSchemeType();
        colorScheme.setColorSpace("Grayscale");
        colorScheme.setColorDepth("8 bits");
        histogram.setColorScheme(colorScheme);
        ColorsType colors = objectFactory.createColorsType();
        List<ColorType> colorList = colors.getColor();
        for (int i = 0; i < values.length; i++) {
            long value = values[i];
            ColorType color = objectFactory.createColorType();
            color.setCode(i);
            color.setCount((int) value);
            colorList.add(color);
        }
        histogram.setColors(colors);
        StringWriter writer = new StringWriter();
        marshaller.marshal(objectFactory.createHistogram(histogram), writer);
        return writer.toString();
    }

    /**
     * Export the histogram as an array of long
     * @return
     */
    public long[] values() {
        return values;
    }
}
