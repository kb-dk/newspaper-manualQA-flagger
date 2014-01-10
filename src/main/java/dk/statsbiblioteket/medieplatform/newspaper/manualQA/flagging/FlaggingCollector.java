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

public class FlaggingCollector {


    private final ObjectFactory objectFactory;
    private final Manualqainput report;
    private Batch batch;
    private FlaggingFinder flaggingFinder;

    public FlaggingCollector(Batch batch, Document batchXmlStructure, String version) {

        this.batch = batch;
        flaggingFinder = new FlaggingFinder(batchXmlStructure);
        objectFactory = new ObjectFactory();
        report = objectFactory.createManualqainput();
        report.setManualqafiles(objectFactory.createManualqafiles());
        report.setVersion(toBigDecimal(version));
    }


    private BigDecimal toBigDecimal(String componentVersion) {
        return new BigDecimal(componentVersion.replaceAll("[a-zA-Z-]*", ""));
    }


    public void addFlag(ParsingEvent reference, String type, String component, String description) {
        addFlagPrivate(reference, type, component, description);
    }

    public void addFlag(ParsingEvent reference, String type, String component, String description, String... details) {
        addFlagPrivate(reference, type, component, description, details);
    }

    private void addFlagPrivate(ParsingEvent reference, String type, String component, String description, String... details) {
        Manualqafile manualQAFile = objectFactory.createManualqafile();
        manualQAFile.setComponent(component);
        manualQAFile.setDescription(description);
        manualQAFile.setType(type);
        manualQAFile.setFilereference(flaggingFinder.getFileReferenceFromEvent(reference));
        if (details != null && details.length > 0){
            Details detailsBlock = objectFactory.createDetails();
            for (String detail : details) {
                detailsBlock.getContent().add(detail);
            }
            manualQAFile.setDetails(detailsBlock);

        }
        report.getManualqafiles().getManualqafile().add(manualQAFile);
    }


    public Batch getBatch() {
        return batch;
    }

    public String toReport() {
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

    public boolean hasFlags() {
        return !report.getManualqafiles().getManualqafile().isEmpty();
    }
}
