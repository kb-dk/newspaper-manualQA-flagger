package dk.statsbiblioteket.medieplatform.newspaper.manualQA.mockers;

/**
 *
 */
public class MixMocker {

    public static String getMixXml(String manufacturers, String models, String modelnumbers,
                                   String serials, String softwares,
                                   String versions, String producers,
                                   String extrasoftware) {
        if (extrasoftware == null) {
            extrasoftware = "";
        }
        String mix = "<mix:mix xmlns:mix=\"http://www.loc.gov/mix/v20\">\n" +
                "    <mix:BasicDigitalObjectInformation>\n" +
                "        <mix:ObjectIdentifier>\n" +
                "            <mix:objectIdentifierType>Image Unique ID</mix:objectIdentifierType>\n" +
                "            <mix:objectIdentifierValue>000001-0001</mix:objectIdentifierValue>\n" +
                "        </mix:ObjectIdentifier>\n" +
                "        <mix:fileSize>7190470</mix:fileSize>\n" +
                "        <mix:FormatDesignation>\n" +
                "            <mix:formatName>JPEG2000 – part 1</mix:formatName>\n" +
                "            <mix:formatVersion>ISO-IEC 15444-1:2004</mix:formatVersion>\n" +
                "        </mix:FormatDesignation>\n" +
                "        <mix:Compression>\n" +
                "            <mix:compressionScheme>JP2 lossless</mix:compressionScheme>\n" +
                "        </mix:Compression>\n" +
                "        <mix:Fixity>\n" +
                "            <mix:messageDigestAlgorithm>MD5</mix:messageDigestAlgorithm>\n" +
                "            <mix:messageDigest>AAC20A9ACE772BC5A92B7D7B00048B91</mix:messageDigest>\n" +
                "            <mix:messageDigestOriginator>Ninestars</mix:messageDigestOriginator>\n" +
                "        </mix:Fixity>\n" +
                "    </mix:BasicDigitalObjectInformation>\n" +
                "    <mix:BasicImageInformation>\n" +
                "        <mix:BasicImageCharacteristics>\n" +
                "            <mix:imageWidth>5108</mix:imageWidth>\n" +
                "            <mix:imageHeight>3667</mix:imageHeight>\n" +
                "            <mix:PhotometricInterpretation>\n" +
                "                <mix:colorSpace>greyscale</mix:colorSpace>\n" +
                "            </mix:PhotometricInterpretation>\n" +
                "        </mix:BasicImageCharacteristics>\n" +
                "    </mix:BasicImageInformation>\n" +
                "    <mix:ImageCaptureMetadata>\n" +
                "        <mix:SourceInformation>\n" +
                "            <mix:sourceType>microfilm</mix:sourceType>\n" +
                "            <mix:SourceID>\n" +
                "                <mix:sourceIDType>Microfilm reel barcode #</mix:sourceIDType>\n" +
                "                <mix:sourceIDValue>000001</mix:sourceIDValue>\n" +
                "            </mix:SourceID>\n" +
                "            <mix:SourceID>\n" +
                "                <mix:sourceIDType>Location on microfilm</mix:sourceIDType>\n" +
                "                <mix:sourceIDValue>0001</mix:sourceIDValue>\n" +
                "            </mix:SourceID>\n" +
                "        </mix:SourceInformation>\n" +
                "        <mix:GeneralCaptureInformation>\n" +
                "            <mix:dateTimeCreated>2013-11-12T11:48:06</mix:dateTimeCreated>\n" +
                "            <mix:imageProducer>___producers___</mix:imageProducer>\n" +
                "        </mix:GeneralCaptureInformation>\n" +
                "        <mix:ScannerCapture>\n" +
                "            <mix:scannerManufacturer>___manufacturers___</mix:scannerManufacturer>\n" +
                "            <mix:ScannerModel>\n" +
                "                <mix:scannerModelName>___models___</mix:scannerModelName>\n" +
                "                <mix:scannerModelNumber>___modelnumbers___</mix:scannerModelNumber>\n" +
                "                <mix:scannerModelSerialNo>___serials___</mix:scannerModelSerialNo>\n" +
                "            </mix:ScannerModel>\n" +
                "            <mix:ScanningSystemSoftware><!--Repeatable-->\n" +
                "                <mix:scanningSoftwareName>___softwares___</mix:scanningSoftwareName>\n" +
                "                <mix:scanningSoftwareVersionNo>___versions___</mix:scanningSoftwareVersionNo>\n" +
                "            </mix:ScanningSystemSoftware>\n" +
                "___extrasoftware___" +
                "        </mix:ScannerCapture>\n" +
                "    </mix:ImageCaptureMetadata>\n" +
                "    <mix:ImageAssessmentMetadata>\n" +
                "        <mix:SpatialMetrics>\n" +
                "            <mix:samplingFrequencyUnit>in.</mix:samplingFrequencyUnit>\n" +
                "            <mix:xSamplingFrequency>\n" +
                "                <mix:numerator>400</mix:numerator>\n" +
                "                <mix:denominator>1</mix:denominator>\n" +
                "            </mix:xSamplingFrequency>\n" +
                "            <mix:ySamplingFrequency>\n" +
                "                <mix:numerator>400</mix:numerator>\n" +
                "                <mix:denominator>1</mix:denominator>\n" +
                "            </mix:ySamplingFrequency>\n" +
                "        </mix:SpatialMetrics>\n" +
                "        <mix:ImageColorEncoding>\n" +
                "            <mix:BitsPerSample>\n" +
                "                <mix:bitsPerSampleValue>8</mix:bitsPerSampleValue>\n" +
                "                <mix:bitsPerSampleUnit>integer</mix:bitsPerSampleUnit>\n" +
                "            </mix:BitsPerSample>\n" +
                "            <mix:samplesPerPixel>1</mix:samplesPerPixel>\n" +
                "        </mix:ImageColorEncoding>\n" +
                "    </mix:ImageAssessmentMetadata>\n" +
                "</mix:mix>\n" +
                "\n";
        return mix
                .replace("___manufacturers___", manufacturers).replace("___models___", models)
                .replace("___modelnumbers___", modelnumbers).replace("___serials___", serials)
                .replace("___softwares___", softwares).replace("___versions___", versions)
                .replace("___producers___", producers)
                .replace("___extrasoftware___", extrasoftware);
    }

}
