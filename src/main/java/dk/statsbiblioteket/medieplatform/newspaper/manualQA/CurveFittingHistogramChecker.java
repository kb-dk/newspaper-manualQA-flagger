package dk.statsbiblioteket.medieplatform.newspaper.manualQA;

import dk.statsbiblioteket.medieplatform.autonomous.iterator.eventhandlers.DefaultTreeEventHandler;


/**
 * Curve Fitting
 Den mest avancerede er curve fitting. Det ligner (for mig) at histogrammerne passer rigtigt godt på en poisson fordeling
 http://en.wikipedia.org/wiki/Poisson_distribution
 Det var sjovt at se at billeder tilsyneladende har en anden fordeling end tekst. Man kan næsten se at grafen er en kombination af 2 poisson fordelinger, den ene for teksten og den anden for billederne.
 Med dette kan vi se "bredden" af spiken, dvs. et mål for hvor "over" eksponeret billedet er. En meget spids spike betyder at billedet bruger meget lidt af farve intervallet.
 Dette er nok den eneste af checkene der fornuftigt kan gøres på det samlede histogram for en film.
 */
public class CurveFittingHistogramChecker extends DefaultTreeEventHandler{
}
