package dk.statsbiblioteket.medieplatform.newspaper.manualQA.mockers;

/**
 * Created by csr on 15/01/14.
 */
public class FilmMocker {

    public static String getFilmXml(String originalResolution, String reductionRatio) {
        String film = "<avis:reelMetadata xmlns:avis=\"http://www.statsbiblioteket.dk/avisdigitalisering/microfilm/1/0/\">\n" +
                "    <avis:titles>Adresse Contoirs Efterretninger</avis:titles>\n" +
                "    <avis:startDate>1795-06-13</avis:startDate>\n" +
                "    <avis:endDate>1795-06-15</avis:endDate>\n" +
                "    <avis:batchIdFilmId>400022028240-14</avis:batchIdFilmId>\n" +
                "    <avis:numberOfPictures>14</avis:numberOfPictures>\n" +
                "    <avis:reductionRatio>___REDUCTIONRATIO___</avis:reductionRatio>\n" +
                "    <avis:captureResolutionOriginal measurement=\"pixels/inch\">___ORIGINALRESOLUTION___</avis:captureResolutionOriginal>\n" +
                "    <avis:captureResolutionFilm measurement=\"pixels/inch\">6000</avis:captureResolutionFilm>\n" +
                "    <avis:dateMicrofilmCreated>1970-06-05</avis:dateMicrofilmCreated>\n" +
                "    <avis:resolutionOfNegative>6.3</avis:resolutionOfNegative>\n" +
                "    <avis:resolutionCommentNegative>No comments</avis:resolutionCommentNegative>\n" +
                "    <avis:densityReadingNegative>0.11</avis:densityReadingNegative>\n" +
                "    <avis:densityReadingNegative>0.12</avis:densityReadingNegative>\n" +
                "    <avis:densityReadingNegative>0.12</avis:densityReadingNegative>\n" +
                "    <avis:densityReadingNegative>0.12</avis:densityReadingNegative>\n" +
                "    <avis:densityReadingNegative>0.12</avis:densityReadingNegative>\n" +
                "    <avis:densityReadingNegative>0.13</avis:densityReadingNegative>\n" +
                "    <avis:densityReadingNegative>0.12</avis:densityReadingNegative>\n" +
                "    <avis:densityReadingNegative>0.12</avis:densityReadingNegative>\n" +
                "    <avis:densityReadingNegative>0.12</avis:densityReadingNegative>\n" +
                "    <avis:densityReadingNegative>0.12</avis:densityReadingNegative>\n" +
                "    <avis:averageDensityNegative>0.12</avis:averageDensityNegative>\n" +
                "    <avis:dminNegative>0.12</avis:dminNegative>\n" +
                "</avis:reelMetadata>";
        return film.replace("___REDUCTIONRATIO___", reductionRatio).replace("___ORIGINALRESOLUTION___", originalResolution);
    }

}
