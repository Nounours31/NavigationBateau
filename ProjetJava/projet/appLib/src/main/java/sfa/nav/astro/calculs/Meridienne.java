/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sfa.nav.astro.calculs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sfa.nav.astro.calculs.correctionvisee.internal.CorrectionDeVisee.eTypeVisee;
import sfa.nav.infra.tools.error.NavException;
import sfa.nav.model.Longitude;
import sfa.nav.model.NavDateHeure;
import sfa.nav.model.PointGeographique;

/**
 *
 * @author pierr
 */
public class Meridienne {

    private static Logger _logger = LoggerFactory.getLogger(Meridienne.class);
	private CanevasMedienne canevas = null;

    private Meridienne() {
    }


    public void LatitudeMeridienne(eTypeVisee typevisee, PointGeographique positionEstimee, Ephemerides eph) throws NavException {
        _logger.debug("Calcul latitude meridienne");
        canevas = new CanevasMedienne(typevisee);
        
        if (typevisee == eTypeVisee.meridienneSoleil) 
        	this.LatitudeMeridienneSoleil( positionEstimee, eph);
        if (typevisee == eTypeVisee.meridiennePolaris)
        	this.LatitudeMeridiennePolaire( positionEstimee, eph);
    }

    public void LongitudeMeridienne(eTypeVisee typevisee, PointGeographique positionEstimee, Ephemerides eph) throws NavException {
        _logger.debug("Calcul Longitude Meridienne");
        canevas = new CanevasMedienne(typevisee);
        
        if (typevisee == eTypeVisee.meridienneSoleil) 
        	this.LongitudeMeridienneSoleil( positionEstimee, eph);
        if (typevisee == eTypeVisee.meridiennePolaris)
        	this.LongitudeMeridiennePolaire( positionEstimee, eph);
    }


	private void LatitudeMeridiennePolaire(PointGeographique positionEstimee, Ephemerides eph) {
		 _logger.error("LatitudeMeridiennePolaire n'existe pas encore");
	}


	private void LatitudeMeridienneSoleil(PointGeographique positionEstimee, Ephemerides eph) {
		 _logger.debug("-->> LatitudeMeridienneSoleil start");
		 
		 // 1. predetermination heure de la meridienne
		 _logger.debug("Predetermination de l'heure de la meridienne");
		 Longitude longitudeEstimee = positionEstimee.longitude();
		 double longitudeEstimeeAsDegre = longitudeEstimee.asDegre();
		 double intervalHoraireEnHeure = (longitudeEstimeeAsDegre / ConstantesCelestes.VitesseSoleinDegreParHeure);
		 
		 
		 NavDateHeure PassageGreenWitch = eph.getHeureGMTPassageGreenwich();
		 NavDateHeure heureMeridienneLocale = PassageGreenWitch.plusHeureDecimale(intervalHoraireEnHeure);
		 _logger.debug("-->> LatitudeMeridienneSoleil heure de passage :" + heureMeridienneLocale);
		  
		 _logger.debug("--<< LatitudeMeridienneSoleil stop");
	}

	private void LongitudeMeridiennePolaire(PointGeographique positionEstimee, Ephemerides eph) {
		 _logger.error("LongitudeMeridiennePolaire n'existe pas encore");
	}


	private void LongitudeMeridienneSoleil(PointGeographique positionEstimee, Ephemerides eph) {
		 _logger.error("LongitudeMeridienneSoleil n'existe pas encore");
	}


}
