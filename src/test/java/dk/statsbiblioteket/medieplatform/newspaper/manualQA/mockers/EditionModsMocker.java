package dk.statsbiblioteket.medieplatform.newspaper.manualQA.mockers;

/**
 */
public class EditionModsMocker {

    public static String getEditionMods(String editionNumber) {
        String rawMods = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"+
        "<mods:mods xmlns:mods=\"http://www.loc.gov/mods/v3\">" +
                "<mods:titleInfo>" +
                "        <mods:title>Politiken</mods:title>" +
                "    </mods:titleInfo>" +
                "<mods:titleInfo type=\"uniform\" authority=\"Statens Avissamling\">" +
                "        <mods:title>politiken</mods:title>" +
                "    </mods:titleInfo>" +
                "    <mods:originInfo>" +
                "        <mods:place>" +
                "            <mods:placeTerm type=\"text\">København</mods:placeTerm>" +
                "        </mods:place>" +
                "        <!--required-->" +
                "        <mods:dateIssued encoding=\"iso8601\"><!--issueDate (required)--></mods:dateIssued>" +
                "        <mods:dateIssued encoding=\"iso8601\" qualifier=\"questionable\"><!--issueDateAsLabeled (optional)--></mods:dateIssued>" +
                "    </mods:originInfo>" +
                "    <mods:note type=\"noteAboutReproduction\">present</mods:note>" +
                "    <mods:note type=\"noteAboutReproduction\" displayLabel=\"comment\">present</mods:note>" +
                "    <mods:relatedItem type=\"host\">" +
                "        <mods:part>" +
                "            <mods:detail type=\"volume\">" +
                "                <mods:number>27</mods:number>" +
                "            </mods:detail>" +
                "            <mods:detail type=\"edition\">" +
                "                <mods:number>___EDITIONNUMBER___</mods:number>" +
                "                <mods:caption>Ekstra udgave</mods:caption>" +
                "            </mods:detail>" +
                "            <mods:detail type=\"issue\">" +
                "                <mods:number>212</mods:number>" +
                "            </mods:detail>" +
                "        </mods:part>" +
                "    </mods:relatedItem>" +
                "</mods:mods>";
        return rawMods.replace("___EDITIONNUMBER___", editionNumber);
    }

}
