package dk.statsbiblioteket.medieplatform.newspaper.manualQA.mockers;

/**
 * Created by csr on 22/01/14.
 */
public class AltoMocker {

    public static String getAlto(String accuracy) {
        String alto = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<alto xmlns=\"http://www.loc.gov/standards/alto/ns-v2#\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.loc.gov/standards/alto alto-v2.0.xsd\">\n" +
                "    <Description>\n" +
                "        <MeasurementUnit>inch1200</MeasurementUnit>\n" +
                "        <sourceImageInformation>\n" +
                "            <fileName>B400022028241-RT1\\400022028241-1\\1795-06-13-01\\adresseavisen1759-1795-06-13-01-0006.jp2</fileName>\n" +
                "        </sourceImageInformation>\n" +
                "        <OCRProcessing ID=\"OCR1\">\n" +
                "            <ocrProcessingStep>\n" +
                "                <processingDateTime>2013-07-16T10:43:34</processingDateTime>\n" +
                "                <processingAgency>Ninestars</processingAgency>\n" +
                "                <processingStepDescription>Scanning;Import;Recognition</processingStepDescription>\n" +
                "                <processingStepSettings>\n" +
                "                    version:ABBYY Recognition Server 3.0\n" +
                "                    language:Danish\n" +
                "                    dictionaryFlag.Danish\n" +
                "                    dictionaryOn:1\n" +
                "                    page-reoriented:UP\n" +
                "                    text-orientation:TO_Normal\n" +
                "                    ABBYY Recognition Server.option.analyze-zones:false\n" +
                "                    ABBYY Recognition Server.option.ocr-auto-pictures:false\n" +
                "                    ABBYY Recognition Server.option.detect-negative-images:false\n" +
                "                    Predicted Word Accuracy:80.95\n" +
                "                    ABBYY Recognition Server OCR Engine Predicted Accuracy:80.95\n" +
                "                    ABBYY Recognition Server OCR Engine Character Error Ratio:0.190533\n" +
                "                    ABBYY Recognition Server OCR Engine Character Count:4265\n" +
                "                    ABBYY Recognition Server OCR Engine Word Count:766\n" +
                "                    Node Count:766\n" +
                "                    width:2326\n" +
                "                    height:2852\n" +
                "                    xdpi:300\n" +
                "                    ydpi:300\n" +
                "                </processingStepSettings>\n" +
                "                <processingSoftware>\n" +
                "                    <softwareCreator>ABBYY</softwareCreator>\n" +
                "                    <softwareName>ABBYY Recognition Server</softwareName>\n" +
                "                    <softwareVersion>3.0</softwareVersion>\n" +
                "                </processingSoftware>\n" +
                "            </ocrProcessingStep>\n" +
                "            <postProcessingStep>\n" +
                "                <processingDateTime>2013-07-16T10:43:34</processingDateTime>\n" +
                "                <processingAgency>Ninestars</processingAgency>\n" +
                "                <processingStepDescription>Verification;Export</processingStepDescription>\n" +
                "                <processingStepSettings>Recognition Server Properties;Load Settings;Save Settings;OCR languages;Processing Station Properties</processingStepSettings>\n" +
                "                <processingSoftware>\n" +
                "                    <softwareCreator>Ninestars</softwareCreator>\n" +
                "                    <softwareName>NSExport</softwareName>\n" +
                "                    <softwareVersion>1.0</softwareVersion>\n" +
                "                    <applicationDescription>NSExport is a powerful yet easy document capture system that allows converting paper to electronic documents.</applicationDescription>\n" +
                "                </processingSoftware>\n" +
                "            </postProcessingStep>\n" +
                "        </OCRProcessing>\n" +
                "    </Description>\n" +
                "    <Styles>\n" +
                "        <TextStyle ID=\"TS21\" FONTSIZE=\"21\"/>\n" +
                "        <TextStyle ID=\"TS43\" FONTSIZE=\"43\"/>\n" +
                "        <TextStyle ID=\"TS13\" FONTSIZE=\"13\"/>\n" +
                "        <TextStyle ID=\"TS12.5\" FONTSIZE=\"12.5\"/>\n" +
                "        <TextStyle ID=\"TS9.5\" FONTSIZE=\"9.5\"/>\n" +
                "        <TextStyle ID=\"TS10\" FONTSIZE=\"10\"/>\n" +
                "        <TextStyle ID=\"TS8\" FONTSIZE=\"8\"/>\n" +
                "        <ParagraphStyle ID=\"PAR1\" ALIGN=\"Left\"/>\n" +
                "        <ParagraphStyle ID=\"PAR2\" ALIGN=\"Left\"/>\n" +
                "        <ParagraphStyle ID=\"PAR3\" ALIGN=\"Center\"/>\n" +
                "        <ParagraphStyle ID=\"PAR4\" ALIGN=\"Right\"/>\n" +
                "    </Styles>\n" +
                "    <Layout>\n" +
                "        <Page ID=\"PAGE1\" HEIGHT=\"8577\" WIDTH=\"6936\" PHYSICAL_IMG_NR=\"1\" QUALITY=\"OK\" POSITION=\"Single\" PROCESSING=\"OCR1\"  ACCURACY=\"___accuracy___\" PC=\"0.809467\">\n" +
                "            <PrintSpace ID=\"SPACE\" HEIGHT=\"8340\" WIDTH=\"7704\" HPOS=\"236\" VPOS=\"880\">\n" +
                "                <TextBlock ID=\"BLOCK1\" STYLEREFS=\"PAR1\" HEIGHT=\"1724\" WIDTH=\"7644\" HPOS=\"296\" VPOS=\"880\" language=\"dan\">\n" +
                "                    <TextLine ID=\"LINE1\" STYLEREFS=\"TS21\" HEIGHT=\"336\" WIDTH=\"1280\" HPOS=\"584\" VPOS=\"1000\">\n" +
                "                        <String ID=\"S1\" CONTENT=\"Ao.\" WC=\"0.926\" CC=\"8 8 9\" HEIGHT=\"296\" WIDTH=\"480\" HPOS=\"584\" VPOS=\"1000\"/>\n" +
                "                        <SP ID=\"SP1\" WIDTH=\"96\" HPOS=\"1068\" VPOS=\"1072\"/>\n" +
                "                        <String ID=\"S2\" CONTENT=\"1795\" WC=\"0.861\" CC=\"7 8 8 8\" HEIGHT=\"336\" WIDTH=\"696\" HPOS=\"1168\" VPOS=\"1072\"/>\n" +
                "                    </TextLine>\n" +
                "                    <TextLine ID=\"LINE2\" STYLEREFS=\"TS21\" HEIGHT=\"264\" WIDTH=\"1792\" HPOS=\"3192\" VPOS=\"976\">\n" +
                "                        <String ID=\"S3\" CONTENT=\"z?«\" WC=\"0.778\" CC=\"8 7 6\" HEIGHT=\"232\" WIDTH=\"424\" HPOS=\"3192\" VPOS=\"1016\"/>\n" +
                "                        <SP ID=\"SP2\" WIDTH=\"136\" HPOS=\"3620\" VPOS=\"976\"/>\n" +
                "                        <String ID=\"S4\" CONTENT=\"AargaM&apos;\" WC=\"0.509\" CC=\"7 8 9 9 8 6 8\" HEIGHT=\"264\" WIDTH=\"1224\" HPOS=\"3760\" VPOS=\"976\"/>\n" +
                "                    </TextLine>\n" +
                "                    <TextLine ID=\"LINE3\" STYLEREFS=\"TS21\" HEIGHT=\"304\" WIDTH=\"1224\" HPOS=\"6296\" VPOS=\"960\">\n" +
                "                        <String ID=\"S5\" CONTENT=\"Ro.\" WC=\"0.852\" CC=\"7 8 8\" HEIGHT=\"296\" WIDTH=\"544\" HPOS=\"6296\" VPOS=\"960\"/>\n" +
                "                        <SP ID=\"SP3\" WIDTH=\"96\" HPOS=\"6844\" VPOS=\"1032\"/>\n" +
                "                        <String ID=\"S6\" CONTENT=\"14?.\" WC=\"0.834\" CC=\"8 8 6 8\" HEIGHT=\"304\" WIDTH=\"576\" HPOS=\"6944\" VPOS=\"1032\"/>\n" +
                "                    </TextLine>\n" +
                "                    <TextLine ID=\"LINE4\" STYLEREFS=\"TS21\" HEIGHT=\"280\" WIDTH=\"2564\" HPOS=\"692\" VPOS=\"1456\">\n" +
                "                        <String ID=\"S7\" CONTENT=\"Kiobenhavns\" WC=\"0.899\" CC=\"8 8 8 8 8 8 8 8 9 8 8\" HEIGHT=\"276\" WIDTH=\"1396\" HPOS=\"692\" VPOS=\"1480\"/>\n" +
                "                        <SP ID=\"SP4\" WIDTH=\"200\" HPOS=\"2092\" VPOS=\"1464\"/>\n" +
                "                        <String ID=\"S8\" CONTENT=\"Kongelig\" WC=\"0.889\" CC=\"8 8 8 8 8 8 8 8\" HEIGHT=\"280\" WIDTH=\"960\" HPOS=\"2296\" VPOS=\"1464\"/>\n" +
                "                    </TextLine>\n" +
                "                    <TextLine ID=\"LINE5\" STYLEREFS=\"TS43\" HEIGHT=\"544\" WIDTH=\"3136\" HPOS=\"304\" VPOS=\"1776\">\n" +
                "                        <String ID=\"S9\" CONTENT=\"Adresse-Tvntvtrs\" WC=\"0.875\" CC=\"9 8 8 8 8 8 8 8 5 8 9 8 7 8 8 8\" HEIGHT=\"544\" WIDTH=\"3136\" HPOS=\"304\" VPOS=\"1776\"/>\n" +
                "                    </TextLine>\n" +
                "                    <TextLine ID=\"LINE6\" STYLEREFS=\"TS13\" HEIGHT=\"200\" WIDTH=\"2060\" HPOS=\"688\" VPOS=\"2388\">\n" +
                "                        <String ID=\"S10\" CONTENT=\"Udgives\" WC=\"0.905\" CC=\"8 8 8 9 8 8 8\" HEIGHT=\"196\" WIDTH=\"580\" HPOS=\"688\" VPOS=\"2392\"/>\n" +
                "                        <SP ID=\"SP5\" WIDTH=\"96\" HPOS=\"1272\" VPOS=\"2416\"/>\n" +
                "                        <String ID=\"S11\" CONTENT=\"ugentlig\" WC=\"0.903\" CC=\"8 8 8 8 8 8 8 9\" HEIGHT=\"200\" WIDTH=\"576\" HPOS=\"1372\" VPOS=\"2432\"/>\n" +
                "                        <SP ID=\"SP6\" WIDTH=\"96\" HPOS=\"1952\" VPOS=\"2424\"/>\n" +
                "                        <String ID=\"S12\" CONTENT=\"4\" WC=\"1\" CC=\"9\" HEIGHT=\"200\" WIDTH=\"200\" HPOS=\"2052\" VPOS=\"2440\"/>\n" +
                "                        <SP ID=\"SP7\" WIDTH=\"100\" HPOS=\"2152\" VPOS=\"2420\"/>\n" +
                "                        <String ID=\"S13\" CONTENT=\"gange.\" WC=\"0.908\" CC=\"8 8 8 9 8 8\" HEIGHT=\"200\" WIDTH=\"492\" HPOS=\"2256\" VPOS=\"2420\"/>\n" +
                "                    </TextLine>\n" +
                "                    <TextLine ID=\"LINE7\" STYLEREFS=\"TS21\" HEIGHT=\"280\" WIDTH=\"2184\" HPOS=\"5256\" VPOS=\"1424\">\n" +
                "                        <String ID=\"S14\" CONTENT=\"alene\" WC=\"0.889\" CC=\"8 8 8 8 8\" HEIGHT=\"232\" WIDTH=\"568\" HPOS=\"5256\" VPOS=\"1496\"/>\n" +
                "                        <SP ID=\"SP8\" WIDTH=\"184\" HPOS=\"5828\" VPOS=\"1480\"/>\n" +
                "                        <String ID=\"S15\" CONTENT=\"privilegerede\" WC=\"0.880\" CC=\"9 9 8 9 8 6 8 8 8 8 5 8 9\" HEIGHT=\"280\" WIDTH=\"1424\" HPOS=\"6016\" VPOS=\"1480\"/>\n" +
                "                    </TextLine>\n" +
                "                    <TextLine ID=\"LINE8\" STYLEREFS=\"TS43\" HEIGHT=\"544\" WIDTH=\"3408\" HPOS=\"4448\" VPOS=\"1776\">\n" +
                "                        <String ID=\"S16\" CONTENT=\"Efterretninger.\" WC=\"0.859\" CC=\"8 8 8 8 8 8 7 8 6 8 7 8 7 8 9\" HEIGHT=\"544\" WIDTH=\"3408\" HPOS=\"4448\" VPOS=\"1792\"/>\n" +
                "                    </TextLine>\n" +
                "                    <TextLine ID=\"LINE9\" STYLEREFS=\"TS12.5\" HEIGHT=\"204\" WIDTH=\"2176\" HPOS=\"5204\" VPOS=\"2364\">\n" +
                "                        <String ID=\"S17\" CONTENT=\"Lsverdagen\" WC=\"0.900\" CC=\"8 7 9 8 9 8 8 8 7 9\" HEIGHT=\"188\" WIDTH=\"876\" HPOS=\"5204\" VPOS=\"2380\"/>\n" +
                "                        <SP ID=\"SP9\" WIDTH=\"112\" HPOS=\"6084\" VPOS=\"2384\"/>\n" +
                "                        <String ID=\"S18\" CONTENT=\"den\" WC=\"0.889\" CC=\"8 8 8\" HEIGHT=\"188\" WIDTH=\"260\" HPOS=\"6200\" VPOS=\"2384\"/>\n" +
                "                        <SP ID=\"SP10\" WIDTH=\"124\" HPOS=\"6464\" VPOS=\"2408\"/>\n" +
                "                        <String ID=\"S19\" CONTENT=\"iz\" WC=\"0.889\" CC=\"8 8\" HEIGHT=\"188\" WIDTH=\"172\" HPOS=\"6592\" VPOS=\"2420\"/>\n" +
                "                        <SP ID=\"SP11\" WIDTH=\"112\" HPOS=\"6768\" VPOS=\"2364\"/>\n" +
                "                        <String ID=\"S20\" CONTENT=\"Juni!.\" WC=\"0.889\" CC=\"7 9 7 9 8 8\" HEIGHT=\"204\" WIDTH=\"496\" HPOS=\"6884\" VPOS=\"2364\"/>\n" +
                "                    </TextLine>\n" +
                "                </TextBlock>\n" +
                "                <TextBlock ID=\"BLOCK9\" STYLEREFS=\"PAR1\" HEIGHT=\"1948\" WIDTH=\"3840\" HPOS=\"4092\" VPOS=\"7272\" language=\"dan\">\n" +
                "                    <TextLine ID=\"LINE85\" STYLEREFS=\"TS9.5\" HEIGHT=\"156\" WIDTH=\"1772\" HPOS=\"6112\" VPOS=\"7296\">\n" +
                "                        <String ID=\"S690\" CONTENT=\"^»ld&apos;srand\" WC=\"0.445\" CC=\"1 7 8 6 2 7 8 6 8 7\" HEIGHT=\"156\" WIDTH=\"636\" HPOS=\"6112\" VPOS=\"7296\"/>\n" +
                "                        <SP ID=\"SP607\" WIDTH=\"44\" HPOS=\"6752\" VPOS=\"7304\"/>\n" +
                "                        <String ID=\"S691\" CONTENT=\"har\" WC=\"0.889\" CC=\"8 8 8\" HEIGHT=\"156\" WIDTH=\"212\" HPOS=\"6800\" VPOS=\"7304\"/>\n" +
                "                        <SP ID=\"SP608\" WIDTH=\"60\" HPOS=\"7016\" VPOS=\"7336\"/>\n" +
                "                        <String ID=\"S692\" CONTENT=\"inaattet\" WC=\"0.903\" CC=\"8 8 9 8 8 8 8 8\" HEIGHT=\"156\" WIDTH=\"476\" HPOS=\"7080\" VPOS=\"7340\"/>\n" +
                "                        <SP ID=\"SP609\" WIDTH=\"92\" HPOS=\"7560\" VPOS=\"7312\"/>\n" +
                "                        <String ID=\"S693\" CONTENT=\"fsr»\" WC=\"0.778\" CC=\"8 8 8 4\" HEIGHT=\"156\" WIDTH=\"228\" HPOS=\"7656\" VPOS=\"7312\"/>\n" +
                "                    </TextLine>\n" +
                "                    <TextLine ID=\"LINE86\" STYLEREFS=\"TS8\" HEIGHT=\"176\" WIDTH=\"3752\" HPOS=\"4140\" VPOS=\"7396\">\n" +
                "                        <String ID=\"S694\" CONTENT=\"lave\" WC=\"0.834\" CC=\"8 6 8 8\" HEIGHT=\"100\" WIDTH=\"240\" HPOS=\"4140\" VPOS=\"7396\"/>\n" +
                "                        <SP ID=\"SP610\" WIDTH=\"72\" HPOS=\"4384\" VPOS=\"7408\"/>\n" +
                "                        <String ID=\"S695\" CONTENT=\"min\" WC=\"0.889\" CC=\"8 8 8\" HEIGHT=\"104\" WIDTH=\"220\" HPOS=\"4460\" VPOS=\"7408\"/>\n" +
                "                        <SP ID=\"SP611\" WIDTH=\"84\" HPOS=\"4684\" VPOS=\"7396\"/>\n" +
                "                        <String ID=\"S696\" CONTENT=\"Bvexsl\" WC=\"0.796\" CC=\"7 6 9 8 5 8\" HEIGHT=\"124\" WIDTH=\"440\" HPOS=\"4772\" VPOS=\"7396\"/>\n" +
                "                        <SP ID=\"SP612\" WIDTH=\"84\" HPOS=\"5216\" VPOS=\"7400\"/>\n" +
                "                        <String ID=\"S697\" CONTENT=\"»-ed\" WC=\"0.806\" CC=\"5 8 8 8\" HEIGHT=\"124\" WIDTH=\"184\" HPOS=\"5304\" VPOS=\"7428\"/>\n" +
                "                        <SP ID=\"SP613\" WIDTH=\"88\" HPOS=\"5492\" VPOS=\"7404\"/>\n" +
                "                        <String ID=\"S698\" CONTENT=\"nammel\" WC=\"0.778\" CC=\"6 8 7 7 8 6\" HEIGHT=\"136\" WIDTH=\"472\" HPOS=\"5584\" VPOS=\"7440\"/>\n" +
                "                        <SP ID=\"SP614\" WIDTH=\"76\" HPOS=\"6060\" VPOS=\"7424\"/>\n" +
                "                        <String ID=\"S699\" CONTENT=\"Vtrmid,\" WC=\"0.857\" CC=\"5 9 9 7 8 8 8\" HEIGHT=\"160\" WIDTH=\"528\" HPOS=\"6140\" VPOS=\"7452\"/>\n" +
                "                        <SP ID=\"SP615\" WIDTH=\"72\" HPOS=\"6672\" VPOS=\"7444\"/>\n" +
                "                        <String ID=\"S700\" CONTENT=\"hvor\" WC=\"0.889\" CC=\"8 8 8 8\" HEIGHT=\"168\" WIDTH=\"260\" HPOS=\"6748\" VPOS=\"7444\"/>\n" +
                "                        <SP ID=\"SP616\" WIDTH=\"100\" HPOS=\"7012\" VPOS=\"7444\"/>\n" +
                "                        <String ID=\"S701\" CONTENT=\"jeg\" WC=\"0.889\" CC=\"8 8 8\" HEIGHT=\"176\" WIDTH=\"160\" HPOS=\"7116\" VPOS=\"7444\"/>\n" +
                "                        <SP ID=\"SP617\" WIDTH=\"84\" HPOS=\"7280\" VPOS=\"7448\"/>\n" +
                "                        <String ID=\"S702\" CONTENT=\"boede\" WC=\"0.911\" CC=\"9 7 9 8 8\" HEIGHT=\"176\" WIDTH=\"324\" HPOS=\"7368\" VPOS=\"7448\"/>\n" +
                "                        <SP ID=\"SP618\" WIDTH=\"48\" HPOS=\"7696\" VPOS=\"7468\"/>\n" +
                "                        <String ID=\"S703\" CONTENT=\"kik\" WC=\"0.778\" CC=\"7 7 7\" HEIGHT=\"176\" WIDTH=\"144\" HPOS=\"7748\" VPOS=\"7468\"/>\n" +
                "                    </TextLine>\n" +
                "                    <TextLine ID=\"LINE87\" STYLEREFS=\"TS9.5\" HEIGHT=\"184\" WIDTH=\"3752\" HPOS=\"4136\" VPOS=\"7516\">\n" +
                "                        <String ID=\"S704\" CONTENT=\"^&gt;e\" WC=\"0.333\" CC=\"1 8 9\" HEIGHT=\"184\" WIDTH=\"216\" HPOS=\"4136\" VPOS=\"7516\"/>\n" +
                "                        <SP ID=\"SP619\" WIDTH=\"80\" HPOS=\"4356\" VPOS=\"7552\"/>\n" +
                "                        <String ID=\"S705\" CONTENT=\"r\" WC=\"0.667\" CC=\"6\" HEIGHT=\"184\" WIDTH=\"128\" HPOS=\"4440\" VPOS=\"7552\"/>\n" +
                "                        <SP ID=\"SP620\" WIDTH=\"68\" HPOS=\"4500\" VPOS=\"7528\"/>\n" +
                "                        <String ID=\"S706\" CONTENT=\"d\" WC=\"0.667\" CC=\"6\" HEIGHT=\"184\" WIDTH=\"120\" HPOS=\"4572\" VPOS=\"7528\"/>\n" +
                "                        <SP ID=\"SP621\" WIDTH=\"60\" HPOS=\"4632\" VPOS=\"7528\"/>\n" +
                "                        <String ID=\"S707\" CONTENT=\"Himmel\" WC=\"0.685\" CC=\"3 6 7 8 4 9\" HEIGHT=\"184\" WIDTH=\"456\" HPOS=\"4696\" VPOS=\"7556\"/>\n" +
                "                        <SP ID=\"SP622\" WIDTH=\"60\" HPOS=\"5156\" VPOS=\"7536\"/>\n" +
                "                        <String ID=\"S708\" CONTENT=\"Sirand;\" WC=\"0.857\" CC=\"6 8 8 9 8 8 7\" HEIGHT=\"184\" WIDTH=\"524\" HPOS=\"5220\" VPOS=\"7536\"/>\n" +
                "                        <SP ID=\"SP623\" WIDTH=\"1112\" HPOS=\"5748\" VPOS=\"7576\"/>\n" +
                "                        <String ID=\"S709\" CONTENT=\"slyttek\" WC=\"0.794\" CC=\"6 6 8 7 8 8 7\" HEIGHT=\"184\" WIDTH=\"352\" HPOS=\"6864\" VPOS=\"7580\"/>\n" +
                "                        <SP ID=\"SP624\" WIDTH=\"52\" HPOS=\"7220\" VPOS=\"7588\"/>\n" +
                "                        <String ID=\"S710\" CONTENT=\"ud\" WC=\"0.889\" CC=\"8 8\" HEIGHT=\"184\" WIDTH=\"144\" HPOS=\"7276\" VPOS=\"7616\"/>\n" +
                "                        <SP ID=\"SP625\" WIDTH=\"52\" HPOS=\"7424\" VPOS=\"7592\"/>\n" +
                "                        <String ID=\"S711\" CONTENT=\"i\" WC=\"0.778\" CC=\"7\" HEIGHT=\"184\" WIDTH=\"132\" HPOS=\"7480\" VPOS=\"7596\"/>\n" +
                "                        <SP ID=\"SP626\" WIDTH=\"92\" HPOS=\"7520\" VPOS=\"7592\"/>\n" +
                "                        <String ID=\"S712\" CONTENT=\"stor^\" WC=\"0.645\" CC=\"7 7 7 7 1\" HEIGHT=\"184\" WIDTH=\"272\" HPOS=\"7616\" VPOS=\"7592\"/>\n" +
                "                    </TextLine>\n" +
                "                    <TextLine ID=\"LINE88\" STYLEREFS=\"TS9.5\" HEIGHT=\"208\" WIDTH=\"3744\" HPOS=\"4136\" VPOS=\"7652\">\n" +
                "                        <String ID=\"S713\" CONTENT=\"Konqe,!s\" WC=\"0.834\" CC=\"8 8 7 5 8 8 8 8\" HEIGHT=\"120\" WIDTH=\"512\" HPOS=\"4136\" VPOS=\"7652\"/>\n" +
                "                        <SP ID=\"SP627\" WIDTH=\"68\" HPOS=\"4652\" VPOS=\"7664\"/>\n" +
                "                        <String ID=\"S714\" CONTENT=\"Gate\" WC=\"0.834\" CC=\"8 8 6 8\" HEIGHT=\"132\" WIDTH=\"304\" HPOS=\"4724\" VPOS=\"7672\"/>\n" +
                "                        <SP ID=\"SP628\" WIDTH=\"56\" HPOS=\"5032\" VPOS=\"7668\"/>\n" +
                "                        <String ID=\"S715\" CONTENT=\"No\" WC=\"0.778\" CC=\"7 7\" HEIGHT=\"132\" WIDTH=\"184\" HPOS=\"5092\" VPOS=\"7668\"/>\n" +
                "                        <SP ID=\"SP629\" WIDTH=\"72\" HPOS=\"5280\" VPOS=\"7692\"/>\n" +
                "                        <String ID=\"S716\" CONTENT=\"71\" WC=\"0.667\" CC=\"7 5\" HEIGHT=\"136\" WIDTH=\"140\" HPOS=\"5356\" VPOS=\"7700\"/>\n" +
                "                        <SP ID=\"SP630\" WIDTH=\"84\" HPOS=\"5500\" VPOS=\"7708\"/>\n" +
                "                        <String ID=\"S717\" CONTENT=\"&apos;&quot;cn\" WC=\"0.222\" CC=\"8 5 8 7\" HEIGHT=\"152\" WIDTH=\"260\" HPOS=\"5588\" VPOS=\"7708\"/>\n" +
                "                        <SP ID=\"SP631\" WIDTH=\"56\" HPOS=\"5852\" VPOS=\"7716\"/>\n" +
                "                        <String ID=\"S718\" CONTENT=\"mig\" WC=\"0.926\" CC=\"8 9 8\" HEIGHT=\"164\" WIDTH=\"212\" HPOS=\"5912\" VPOS=\"7716\"/>\n" +
                "                        <SP ID=\"SP632\" WIDTH=\"60\" HPOS=\"6128\" VPOS=\"7696\"/>\n" +
                "                        <String ID=\"S719\" CONTENT=\"forhen\" WC=\"0.870\" CC=\"8 8 9 7 7 8\" HEIGHT=\"176\" WIDTH=\"368\" HPOS=\"6192\" VPOS=\"7696\"/>\n" +
                "                        <SP ID=\"SP633\" WIDTH=\"32\" HPOS=\"6564\" VPOS=\"7724\"/>\n" +
                "                        <String ID=\"S720\" CONTENT=\"til?!0bleBaseraaa&gt;»,\" WC=\"0.734\" CC=\"8 8 8 6 9 8 8 9 8 7 8 8 9 8 8 7 8 8 1 8\" HEIGHT=\"208\" WIDTH=\"1280\" HPOS=\"6600\" VPOS=\"7724\"/>\n" +
                "                    </TextLine>\n" +
                "                    <TextLine ID=\"LINE89\" STYLEREFS=\"TS9.5\" HEIGHT=\"212\" WIDTH=\"3756\" HPOS=\"4136\" VPOS=\"7796\">\n" +
                "                        <String ID=\"S721\" CONTENT=\"/S\" WC=\"0.333\" CC=\"2 4\" HEIGHT=\"212\" WIDTH=\"188\" HPOS=\"5328\" VPOS=\"7840\"/>\n" +
                "                        <SP ID=\"SP634\" WIDTH=\"52\" HPOS=\"5520\" VPOS=\"7816\"/>\n" +
                "                        <String ID=\"S722\" CONTENT=\"^^r\" WC=\"0.370\" CC=\"1 1 8\" HEIGHT=\"212\" WIDTH=\"236\" HPOS=\"5576\" VPOS=\"7816\"/>\n" +
                "                        <SP ID=\"SP635\" WIDTH=\"52\" HPOS=\"5816\" VPOS=\"7852\"/>\n" +
                "                        <String ID=\"S723\" CONTENT=\"v.?a\" WC=\"0.639\" CC=\"7 8 5 3\" HEIGHT=\"212\" WIDTH=\"208\" HPOS=\"5872\" VPOS=\"7856\"/>\n" +
                "                        <SP ID=\"SP636\" WIDTH=\"56\" HPOS=\"6084\" VPOS=\"7840\"/>\n" +
                "                        <String ID=\"S724\" CONTENT=\"koftct\" WC=\"0.686\" CC=\"5 7 6 6 5 8\" HEIGHT=\"212\" WIDTH=\"328\" HPOS=\"6144\" VPOS=\"7840\"/>\n" +
                "                        <SP ID=\"SP637\" WIDTH=\"100\" HPOS=\"6476\" VPOS=\"7860\"/>\n" +
                "                        <String ID=\"S725\" CONTENT=\".iiSt&apos;l\" WC=\"0.482\" CC=\"8 8 7 5 8 8 8\" HEIGHT=\"212\" WIDTH=\"-4\" HPOS=\"6892\" VPOS=\"7856\"/>\n" +
                "                        <SP ID=\"SP638\" WIDTH=\"88\" HPOS=\"6892\" VPOS=\"7856\"/>\n" +
                "                        <String ID=\"S726\" CONTENT=\"mnie\" WC=\"0.834\" CC=\"8 5 9 8\" HEIGHT=\"212\" WIDTH=\"300\" HPOS=\"6984\" VPOS=\"7876\"/>\n" +
                "                        <SP ID=\"SP639\" WIDTH=\"40\" HPOS=\"7288\" VPOS=\"7864\"/>\n" +
                "                        <String ID=\"S727\" CONTENT=\"Vcrreller\" WC=\"0.803\" CC=\"7 5 8 9 8 9 6 7 6\" HEIGHT=\"212\" WIDTH=\"560\" HPOS=\"7332\" VPOS=\"7864\"/>\n" +
                "                    </TextLine>\n" +
                "                    <TextLine ID=\"LINE90\" STYLEREFS=\"TS9.5\" HEIGHT=\"196\" WIDTH=\"3740\" HPOS=\"4136\" VPOS=\"7928\">\n" +
                "                        <String ID=\"S728\" CONTENT=\"rr\" WC=\"0.889\" CC=\"7 9\" HEIGHT=\"80\" WIDTH=\"112\" HPOS=\"4136\" VPOS=\"7952\"/>\n" +
                "                        <SP ID=\"SP640\" WIDTH=\"60\" HPOS=\"4252\" VPOS=\"7952\"/>\n" +
                "                        <String ID=\"S729\" CONTENT=\"iS.&apos;nbsa,.\" WC=\"0.548\" CC=\"9 7 9 6 5 6 8 8 7 9\" HEIGHT=\"132\" WIDTH=\"484\" HPOS=\"4316\" VPOS=\"7956\"/>\n" +
                "                        <SP ID=\"SP641\" WIDTH=\"80\" HPOS=\"4884\" VPOS=\"7944\"/>\n" +
                "                        <String ID=\"S730\" CONTENT=\"har\" WC=\"0.889\" CC=\"8 8 8\" HEIGHT=\"132\" WIDTH=\"192\" HPOS=\"4968\" VPOS=\"7944\"/>\n" +
                "                        <SP ID=\"SP642\" WIDTH=\"88\" HPOS=\"5164\" VPOS=\"7972\"/>\n" +
                "                        <String ID=\"S731\" CONTENT=\"alle\" WC=\"0.834\" CC=\"8 7 7 8\" HEIGHT=\"132\" WIDTH=\"200\" HPOS=\"5256\" VPOS=\"7976\"/>\n" +
                "                        <SP ID=\"SP643\" WIDTH=\"88\" HPOS=\"5460\" VPOS=\"7972\"/>\n" +
                "                        <String ID=\"S732\" CONTENT=\"v?or.«cr\" WC=\"0.764\" CC=\"6 6 6 7 9 6 7 8\" HEIGHT=\"148\" WIDTH=\"420\" HPOS=\"5552\" VPOS=\"7972\"/>\n" +
                "                        <SP ID=\"SP644\" WIDTH=\"92\" HPOS=\"5976\" VPOS=\"7992\"/>\n" +
                "                        <String ID=\"S733\" CONTENT=\"at\" WC=\"0.889\" CC=\"8 8\" HEIGHT=\"160\" WIDTH=\"124\" HPOS=\"6072\" VPOS=\"7996\"/>\n" +
                "                        <SP ID=\"SP645\" WIDTH=\"92\" HPOS=\"6200\" VPOS=\"7976\"/>\n" +
                "                        <String ID=\"S734\" CONTENT=\"s«lee,\" WC=\"0.870\" CC=\"8 6 9 8 9 7\" HEIGHT=\"184\" WIDTH=\"364\" HPOS=\"6296\" VPOS=\"7976\"/>\n" +
                "                        <SP ID=\"SP646\" WIDTH=\"84\" HPOS=\"6664\" VPOS=\"7980\"/>\n" +
                "                        <String ID=\"S735\" CONTENT=\"som\" WC=\"0.926\" CC=\"8 9 8\" HEIGHT=\"184\" WIDTH=\"216\" HPOS=\"6752\" VPOS=\"7980\"/>\n" +
                "                        <SP ID=\"SP647\" WIDTH=\"76\" HPOS=\"6972\" VPOS=\"7984\"/>\n" +
                "                        <String ID=\"S736\" CONTENT=\"forben\" WC=\"0.870\" CC=\"8 8 8 7 8 8\" HEIGHT=\"192\" WIDTH=\"380\" HPOS=\"7052\" VPOS=\"7984\"/>\n" +
                "                        <SP ID=\"SP648\" WIDTH=\"84\" HPOS=\"7436\" VPOS=\"8004\"/>\n" +
                "                        <String ID=\"S737\" CONTENT=\"i\" WC=\"0.889\" CC=\"8\" HEIGHT=\"192\" WIDTH=\"120\" HPOS=\"7524\" VPOS=\"8004\"/>\n" +
                "                        <SP ID=\"SP649\" WIDTH=\"84\" HPOS=\"7560\" VPOS=\"8004\"/>\n" +
                "                        <String ID=\"S738\" CONTENT=\"rnm\" WC=\"0.741\" CC=\"6 8 6\" HEIGHT=\"196\" WIDTH=\"228\" HPOS=\"7648\" VPOS=\"8040\"/>\n" +
                "                    </TextLine>\n" +
                "                    <TextLine ID=\"LINE91\" STYLEREFS=\"TS9.5\" HEIGHT=\"220\" WIDTH=\"3364\" HPOS=\"4340\" VPOS=\"8092\">\n" +
                "                        <String ID=\"S739\" CONTENT=\"&quot;^°sekr!rnnn!.7&quot;-n&apos;^^&apos;&apos;^\" WC=\"0.338\" CC=\"7 1 8 8 8 8 8 4 6 7 6 8 9 8 5 7 8 7 7 1 1 8 8 1\" HEIGHT=\"220\" WIDTH=\"1676\" HPOS=\"4340\" VPOS=\"8092\"/>\n" +
                "                        <SP ID=\"SP650\" WIDTH=\"240\" HPOS=\"6020\" VPOS=\"8108\"/>\n" +
                "                        <String ID=\"S740\" CONTENT=\"N&apos;ck-ls«I«.\" WC=\"0.563\" CC=\"7 8 5 5 8 8 8 7 8 8 9\" HEIGHT=\"220\" WIDTH=\"580\" HPOS=\"7124\" VPOS=\"8136\"/>\n" +
                "                    </TextLine>\n" +
                "                    <TextLine ID=\"LINE92\" STYLEREFS=\"TS9.5\" HEIGHT=\"156\" WIDTH=\"2796\" HPOS=\"4448\" VPOS=\"8224\">\n" +
                "                        <String ID=\"S741\" CONTENT=\"yott.r^rmlne^\" WC=\"0.658\" CC=\"3 8 3 7 9 8 1 7 8 8 8 6 1\" HEIGHT=\"112\" WIDTH=\"796\" HPOS=\"4448\" VPOS=\"8224\"/>\n" +
                "                        <SP ID=\"SP651\" WIDTH=\"68\" HPOS=\"5248\" VPOS=\"8224\"/>\n" +
                "                        <String ID=\"S742\" CONTENT=\"P\" WC=\"0.889\" CC=\"8\" HEIGHT=\"124\" WIDTH=\"148\" HPOS=\"5320\" VPOS=\"8224\"/>\n" +
                "                        <SP ID=\"SP652\" WIDTH=\"56\" HPOS=\"5412\" VPOS=\"8224\"/>\n" +
                "                        <String ID=\"S743\" CONTENT=\"der\" WC=\"0.778\" CC=\"7 6 8\" HEIGHT=\"124\" WIDTH=\"176\" HPOS=\"5472\" VPOS=\"8228\"/>\n" +
                "                        <SP ID=\"SP653\" WIDTH=\"716\" HPOS=\"5652\" VPOS=\"8268\"/>\n" +
                "                        <String ID=\"S744\" CONTENT=\"som\" WC=\"0.815\" CC=\"7 7 8\" HEIGHT=\"144\" WIDTH=\"212\" HPOS=\"6488\" VPOS=\"8248\"/>\n" +
                "                        <SP ID=\"SP654\" WIDTH=\"80\" HPOS=\"6704\" VPOS=\"8256\"/>\n" +
                "                        <String ID=\"S745\" CONTENT=\"fvr\" WC=\"0.778\" CC=\"7 7 7\" HEIGHT=\"156\" WIDTH=\"172\" HPOS=\"6788\" VPOS=\"8256\"/>\n" +
                "                        <SP ID=\"SP655\" WIDTH=\"84\" HPOS=\"6964\" VPOS=\"8268\"/>\n" +
                "                        <String ID=\"S746\" CONTENT=\"den\" WC=\"0.593\" CC=\"3 6 7\" HEIGHT=\"156\" WIDTH=\"192\" HPOS=\"7052\" VPOS=\"8268\"/>\n" +
                "                    </TextLine>\n" +
                "                    <TextLine ID=\"LINE93\" STYLEREFS=\"TS9.5\" HEIGHT=\"224\" WIDTH=\"1428\" HPOS=\"4128\" VPOS=\"8616\">\n" +
                "                        <String ID=\"S747\" CONTENT=\"a-chstr^-\" WC=\"0.605\" CC=\"7 8 1 1 8 8 7 1 8\" HEIGHT=\"120\" WIDTH=\"600\" HPOS=\"4128\" VPOS=\"8636\"/>\n" +
                "                        <SP ID=\"SP656\" WIDTH=\"52\" HPOS=\"4732\" VPOS=\"8628\"/>\n" +
                "                        <String ID=\"S748\" CONTENT=\"No.\" WC=\"0.852\" CC=\"8 6 9\" HEIGHT=\"132\" WIDTH=\"224\" HPOS=\"4788\" VPOS=\"8628\"/>\n" +
                "                        <SP ID=\"SP657\" WIDTH=\"76\" HPOS=\"5016\" VPOS=\"8668\"/>\n" +
                "                        <String ID=\"S749\" CONTENT=\"h^\" WC=\"0.500\" CC=\"8 1\" HEIGHT=\"224\" WIDTH=\"196\" HPOS=\"5360\" VPOS=\"8644\"/>\n" +
                "                    </TextLine>\n" +
                "                    <TextLine ID=\"LINE94\" STYLEREFS=\"TS9.5\" HEIGHT=\"196\" WIDTH=\"3756\" HPOS=\"4128\" VPOS=\"8764\">\n" +
                "                        <String ID=\"S750\" CONTENT=\"&gt;ed\" WC=\"0.444\" CC=\"8 8 8\" HEIGHT=\"96\" WIDTH=\"192\" HPOS=\"4128\" VPOS=\"8776\"/>\n" +
                "                        <SP ID=\"SP658\" WIDTH=\"56\" HPOS=\"4324\" VPOS=\"8764\"/>\n" +
                "                        <String ID=\"S751\" CONTENT=\"Kl\" WC=\"0.778\" CC=\"5 9\" HEIGHT=\"108\" WIDTH=\"128\" HPOS=\"4384\" VPOS=\"8764\"/>\n" +
                "                        <SP ID=\"SP659\" WIDTH=\"28\" HPOS=\"4516\" VPOS=\"8768\"/>\n" +
                "                        <String ID=\"S752\" CONTENT=\"rd^k\" WC=\"0.694\" CC=\"8 8 1 8\" HEIGHT=\"112\" WIDTH=\"236\" HPOS=\"4548\" VPOS=\"8792\"/>\n" +
                "                        <SP ID=\"SP660\" WIDTH=\"64\" HPOS=\"4788\" VPOS=\"8772\"/>\n" +
                "                        <String ID=\"S753\" CONTENT=\"crmmer\" WC=\"0.834\" CC=\"7 8 7 7 8 8\" HEIGHT=\"120\" WIDTH=\"416\" HPOS=\"4856\" VPOS=\"8792\"/>\n" +
                "                        <SP ID=\"SP661\" WIDTH=\"36\" HPOS=\"5276\" VPOS=\"8772\"/>\n" +
                "                        <String ID=\"S754\" CONTENT=\"C\" WC=\"0.667\" CC=\"6\" HEIGHT=\"132\" WIDTH=\"176\" HPOS=\"5316\" VPOS=\"8772\"/>\n" +
                "                        <SP ID=\"SP662\" WIDTH=\"80\" HPOS=\"5412\" VPOS=\"8772\"/>\n" +
                "                        <String ID=\"S755\" CONTENT=\"s»\" WC=\"0.889\" CC=\"9 7\" HEIGHT=\"140\" WIDTH=\"100\" HPOS=\"5496\" VPOS=\"8780\"/>\n" +
                "                        <SP ID=\"SP663\" WIDTH=\"60\" HPOS=\"5600\" VPOS=\"8812\"/>\n" +
                "                        <String ID=\"S756\" CONTENT=\"rs\" WC=\"0.834\" CC=\"8 7\" HEIGHT=\"148\" WIDTH=\"96\" HPOS=\"5664\" VPOS=\"8824\"/>\n" +
                "                        <SP ID=\"SP664\" WIDTH=\"44\" HPOS=\"5764\" VPOS=\"8792\"/>\n" +
                "                        <String ID=\"S757\" CONTENT=\"n\" WC=\"0.889\" CC=\"8\" HEIGHT=\"148\" WIDTH=\"184\" HPOS=\"5812\" VPOS=\"8824\"/>\n" +
                "                        <SP ID=\"SP665\" WIDTH=\"116\" HPOS=\"5880\" VPOS=\"8796\"/>\n" +
                "                        <String ID=\"S758\" CONTENT=\"h&gt;\" WC=\"0.356\" CC=\"8 8\" HEIGHT=\"156\" WIDTH=\"100\" HPOS=\"6000\" VPOS=\"8796\"/>\n" +
                "                        <SP ID=\"SP666\" WIDTH=\"40\" HPOS=\"6104\" VPOS=\"8832\"/>\n" +
                "                        <String ID=\"S759\" CONTENT=\"^r\" WC=\"0.500\" CC=\"1 8\" HEIGHT=\"156\" WIDTH=\"116\" HPOS=\"6148\" VPOS=\"8832\"/>\n" +
                "                        <SP ID=\"SP667\" WIDTH=\"64\" HPOS=\"6268\" VPOS=\"8832\"/>\n" +
                "                        <String ID=\"S760\" CONTENT=\"!ea\" WC=\"0.852\" CC=\"7 8 8\" HEIGHT=\"164\" WIDTH=\"152\" HPOS=\"6336\" VPOS=\"8836\"/>\n" +
                "                        <SP ID=\"SP668\" WIDTH=\"52\" HPOS=\"6492\" VPOS=\"8840\"/>\n" +
                "                        <String ID=\"S761\" CONTENT=\"paa\" WC=\"0.852\" CC=\"8 7 8\" HEIGHT=\"180\" WIDTH=\"204\" HPOS=\"6548\" VPOS=\"8848\"/>\n" +
                "                        <SP ID=\"SP669\" WIDTH=\"56\" HPOS=\"6756\" VPOS=\"8820\"/>\n" +
                "                        <String ID=\"S762\" CONTENT=\"bcd?e\" WC=\"0.845\" CC=\"8 8 8 6 8\" HEIGHT=\"184\" WIDTH=\"328\" HPOS=\"6816\" VPOS=\"8820\"/>\n" +
                "                        <SP ID=\"SP670\" WIDTH=\"48\" HPOS=\"7148\" VPOS=\"8828\"/>\n" +
                "                        <String ID=\"S763\" CONTENT=\"Bkaadc\" WC=\"0.796\" CC=\"7 5 8 8 7 8\" HEIGHT=\"196\" WIDTH=\"428\" HPOS=\"7200\" VPOS=\"8828\"/>\n" +
                "                        <SP ID=\"SP671\" WIDTH=\"68\" HPOS=\"7632\" VPOS=\"8880\"/>\n" +
                "                        <String ID=\"S764\" CONTENT=\"v»&lt;\" WC=\"0.408\" CC=\"6 8 8\" HEIGHT=\"196\" WIDTH=\"180\" HPOS=\"7704\" VPOS=\"8884\"/>\n" +
                "                    </TextLine>\n" +
                "                    <TextLine ID=\"LINE95\" STYLEREFS=\"TS9.5\" HEIGHT=\"216\" WIDTH=\"3844\" HPOS=\"4120\" VPOS=\"8940\">\n" +
                "                        <String ID=\"S765\" CONTENT=\"A\" WC=\"0.444\" CC=\"4\" HEIGHT=\"132\" WIDTH=\"2116\" HPOS=\"4120\" VPOS=\"9024\"/>\n" +
                "                        <SP ID=\"SP672\" WIDTH=\"1972\" HPOS=\"4264\" VPOS=\"8940\"/>\n" +
                "                        <String ID=\"S766\" CONTENT=\"e»Mr^mm«.arer-\" WC=\"0.722\" CC=\"8 7 6 6 1 6 7 6 8 8 8 7 6 7\" HEIGHT=\"216\" WIDTH=\"1724\" HPOS=\"6240\" VPOS=\"8940\"/>\n" +
                "                    </TextLine>\n" +
                "                </TextBlock>\n" +
                "            </PrintSpace>\n" +
                "        </Page>\n" +
                "    </Layout>\n" +
                "</alto>";
        return alto.replace("___accuracy___", accuracy);
    }

}
