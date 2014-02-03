package dk.statsbiblioteket.medieplatform.newspaper.manualQA.flagging;

import dk.statsbiblioteket.medieplatform.autonomous.Batch;
import dk.statsbiblioteket.medieplatform.autonomous.iterator.common.ParsingEvent;
import dk.statsbiblioteket.medieplatform.newspaper.manualQA.Details;
import dk.statsbiblioteket.medieplatform.newspaper.manualQA.Manualqafile;
import dk.statsbiblioteket.medieplatform.newspaper.manualQA.Manualqainput;
import dk.statsbiblioteket.medieplatform.newspaper.manualQA.ObjectFactory;
import org.w3c.dom.Document;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.StringWriter;
import java.math.BigDecimal;

/**
 * This collector should be used to collect the flags for the manual QA
 */
public class FlaggingCollector {


    private final ObjectFactory objectFactory;
    private final Manualqainput report;
    private Batch batch;
    private FlaggingFinder flaggingFinder;
    private Manualqafile lastQaFile;

    private int flagCount;
    private int maxFlags;

    /**
     * Create a new flagging collector
     *
     * @param batch             the batch
     * @param batchXmlStructure the batch structure xml
     * @param version           the version of the component?
     * @param maxFlags          the maximum number of flags which can be added. Any additional flags will simply be
     *                          reported as a raw total as part of the description of the last flag added.
     */
    public FlaggingCollector(Batch batch, Document batchXmlStructure, String version, int maxFlags) {
        this.batch = batch;
        flaggingFinder = new FlaggingFinder(batchXmlStructure);
        objectFactory = new ObjectFactory();
        report = objectFactory.createManualqainput();
        report.setManualqafiles(objectFactory.createManualqafiles());
        report.setVersion(toBigDecimal(version));
        this.maxFlags = maxFlags;
        flagCount = 0;
    }


    private BigDecimal toBigDecimal(String componentVersion) {
        return new BigDecimal(componentVersion.replaceAll("[a-zA-Z-]*", ""));
    }




    /**
     * Add a flag
     *
     * @param reference   the event which caused the flag to be raised
     * @param type        the type, Should be "jp2file" or "metadata"
     * @param component   the component causing the flag
     * @param description description of the problem
     * @param details     additional details of the problem
     */
    public void addFlag(ParsingEvent reference, String type, String component, String description, String... details) {
        addFlagPrivate(reference, type, component, description, details);
    }

    private void addFlagPrivate(ParsingEvent reference, String type, String component, String description,
                                String... details) {
        flagCount++;
        Manualqafile manualQAFile = objectFactory.createManualqafile();
        manualQAFile.setComponent(component);
        manualQAFile.setDescription(description);
        manualQAFile.setType(type);
        manualQAFile.setFilereference(flaggingFinder.getFileReferenceFromEvent(reference));
        if (details != null && details.length > 0) {
            Details detailsBlock = objectFactory.createDetails();
            for (String detail : details) {
                detailsBlock.getContent().add(detail);
            }
            manualQAFile.setDetails(detailsBlock);

        }
        if (flagCount < maxFlags) {
           report.getManualqafiles().getManualqafile().add(manualQAFile);
        } else {
            manualQAFile.setDescription("The number of issues found that require checking exceeds the maximum expected." +
                    " The number of issues found was " + flagCount + ", but only " + maxFlags + " can be reported. The " +
                    "cause of the last issue was '" + description + "'");
            lastQaFile = manualQAFile;
        }
    }

    /**
     * Get the batch
     * @return the batch
     */
    public Batch getBatch() {
        return batch;
    }

    /**
     * Convert the collector to an xml report
     * @return the xml report as String
     */
    public String toReport() {
        if (lastQaFile != null) {
            report.getManualqafiles().getManualqafile().add(lastQaFile);
        }
        try {
            JAXBContext context = JAXBContext.newInstance(ObjectFactory.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            StringWriter writer = new StringWriter();
            marshaller.marshal(report, writer);
            return writer.toString();
        } catch (JAXBException e) {
            return null;
        }
    }

    /**
     * Return true if any flags have been reported
     */
    public boolean hasFlags() {
        return !report.getManualqafiles().getManualqafile().isEmpty();
    }
}
