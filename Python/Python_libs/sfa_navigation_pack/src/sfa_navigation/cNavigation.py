from __future__ import annotations

import math
from enum import Enum
from typing import Tuple

from . import cVelocite, cDistance, cCap
from . import cPosition
from . import cLongitude
from . import cLatitude
from . import eAngleFormat, cAngle


class cMethodeCalcul(Enum):
    FromENMM = 0
    FromWikiPedia = 1

class cSensNavigation(Enum):
    undef = 0
    versNE = 4
    versNW = 5
    versSE = 6
    versSW = 7


class cNavigation:
    def __init__(self, position: cPosition):
        self._position = position

    @property
    def position(self) -> cPosition:
        return self._position

    @position.setter
    def position(self, p: cPosition) -> None:
        self._position = p

    # format latitude, long
    def __str__(self) -> str:
        return self._position.toString()

    def __repr__(self) -> str:
        return "[cNavigation: " + self._position.toString() + "]"

    def toString(self, format: eAngleFormat = eAngleFormat.DD) -> str:
        return self._position.toString(format)

    # see https://fr.wikipedia.org/wiki/Loxodromie
    def routeLoxodromique(self, arrivee: cPosition) -> tuple[cCap, cDistance]:
        depart: cPosition = self._position
        varLatEnDeg: float = arrivee.latitude.latitudeEnDeg - depart.latitude.latitudeEnDeg
        varLongEnDeg : float 
        sensNavigation : cSensNavigation 
        (sensNavigation, varLongEnDeg)= self._routeSensNavigationLePlusCourtGradientEnDeg(arrivee)

        latMoyenneEnRad: float = (arrivee.latitude.latitudeEnRad + depart.latitude.latitudeEnRad) / 2.0

        # calcul de la Rv
        LatitudeCroissanteRadArrivee: float = arrivee.gudermannInverse()
        LatitudeCroissanteRadDepart: float = depart.gudermannInverse()

        # debug ...
        LatitudeCroissanteDegArrivee: float = LatitudeCroissanteRadArrivee * cAngle.RAD2DEG
        LatitudeCroissanteDegDepart: float = LatitudeCroissanteRadDepart * cAngle.RAD2DEG
        
        RouteQuartFond : float = 0.0
        RouteQuartFondEnDeg : float = 0.0
        routeFondEnDeg: float = 0.0  

        if math.fabs(varLatEnDeg) > (1 / 3600):
            tangentRv = math.fabs(varLongEnDeg)
            tangentRv = tangentRv / math.fabs(LatitudeCroissanteDegArrivee - LatitudeCroissanteDegDepart)
            RouteQuartFond = math.atan(math.fabs(tangentRv))    

        else:
            RouteQuartFond = math.pi / 2.0

        RouteQuartFondEnDeg = RouteQuartFond * cAngle.RAD2DEG

        routeFondEnDeg = cNavigation.RouteQuartFond2RouteFond(sensNavigation=sensNavigation, RouteQuartFond=RouteQuartFondEnDeg)
        
        # calcul de la distance
        distanceEnMille: float = 0.0
        if RouteQuartFondEnDeg > 89.0:
            distanceEnMille = math.cos(latMoyenneEnRad) * varLongEnDeg * 60.0 # noeud = mille/h - 1 mille = 1 minute d'arc
            distanceEnMille /= math.sin(RouteQuartFond )
        else:
            distanceEnMille = varLatEnDeg * 60.0 # noeud = mille/h - 1 mille = 1 minute d'arc
            distanceEnMille /= math.cos (routeFondEnDeg * cAngle.DEG2RAD)
        distanceEnMille = math.fabs(distanceEnMille)

        c: cCap = cCap(valAsDeg=routeFondEnDeg)
        dist: cDistance = cDistance(valAsMilleNautique=distanceEnMille)
        return (c, dist)


    @staticmethod
    def RouteQuartFond2RouteFond(sensNavigation: cSensNavigation, RouteQuartFond: float) -> float:
        """
        Cette route notée Rfq a un équivalent Rf compris entre 0° et 360°. 
        Par exemple :
            si Rfq = N60°E alors Rf = 060° (= 000°+060°)
            si Rfq = N60°W alors Rf = 300° (= 360°-060°)
            si Rfq = S60°E alors Rf = 120° (= 180°-060°)
            si Rfq = S60°W alors Rf = 240° (= 180°+060°)
        """
        routeFond: float = 0.0
        if sensNavigation == cSensNavigation.versNE:
            routeFond = RouteQuartFond
        if sensNavigation == cSensNavigation.versNW:
            routeFond = 360 - RouteQuartFond
        if sensNavigation == cSensNavigation.versSE:
            routeFond = 180 - RouteQuartFond
        if sensNavigation == cSensNavigation.versSW:
            routeFond = 180 + RouteQuartFond
        return routeFond
    

    # https://fr.wikipedia.org/wiki/Orthodromie#Distance_orthodromique
    def routeOrthodromique(self, arrivee: cPosition, methode: cMethodeCalcul) -> tuple[cCap, cDistance, cPosition]:
        """
        Quelques notes sur l'orthodromie:
            1. la dichotomie
            Le vertex est a la 1/2 de chemin
            On relance une othodromie entre depart et vertex -> on a un second vertex qui est le 1/4 de chemin
            et de proche ne proche on a le chemin detaillé
            épétition de la dichotomie

            - Principe
            Après avoir obtenu la "semi-loxodromie", on peut répéter de nouveau le processus sur
            chacun des 2 portions de loxodromie, et ainsi de suite jusqu'à atteindre la longueur désirée
            (qui doit cependant être supérieure à la longueur de l'orthodromie).


            2. le chemin inverse
            A -> B puis B -> A donne les caps de depart et d'arrivee

            3. calcul du milieu de la nav - Recherche du milieu de l'orthodromie
            Pour trouver le milieu d' une orthodromie entre deux points A et B, on transforme tout
            d'abord les coordonnées géographiques de A et de B en coordonnées cartésiennes pour
            trouver le milieu de [AB]. Ce point étant aligné avec le milieu de l'orthodromie et le centre de la
            Terre, sa latitude et sa longitude sont les mêmes que le milieu de l'orthodromie.
            On part des coordonnées géographiques des points A et B : (latA, longA) et (latB, longB). On
            transforme ensuite ces coordonnées géographiques en coordonnées cartésiennes.
                xA = R*cos[latA]*cos[longA]
                yA = -R*cos[latA]*sin[longA]
                zA = R*sin[latA]

                xB = R*cos[latB*cos[longB]
                yB = -R*cos[latB]*sin[longB]
                zB = R*sin[latB]

            On calule ensuite les coordonnées de T , milieu de [AB], en coordonnées cartésiennes.
                xT = (xA + xB)/2
                yT = (yA+yB)/2
                zT = (zA+zB)/2
            On transforme ensuite ces coordonnées cartésiennes en coordonnées géographiques.
            Ces coordonnées sont celles du milieu de l'orthodromie.
                latT = 90 - arccos[zT/sqrt(xT² + yT² + zT²)]
                longT = arccos[xT/(xT² + yT²)]

        Dans le PDF livre detail de la nav


        Note: 
        Une orthodromie = la trajectoire la plus courte entre deux points sur la surface de la Terre (arc de grand cercle).

        Contrairement à une loxodromie (route à cap constant), l’orthodromie :
        change progressivement de cap monte vers une latitude maximale puis redescend
        
        Le vertex est donc le point où :
            la latitude est la plus élevée (dans l’hémisphère Nord)
            ou la plus basse (dans l’hémisphère Sud)

            et le cap est exactement 090° ou 270° (route Est/Ouest)

        Pourquoi c’est important ?
            Le vertex permet de :
                connaître la latitude maximale atteinte
                vérifier si la route passe dans une zone dangereuse (glaces, météo, etc.)
                planifier des routes composites (orthodromie + loxodromie)

        """
        return self._routeOrthodromiqueENSMM(arrivee)
        
        


    def _routeOrthodromiqueENSMM(self, arrivee: cPosition) -> tuple[cCap, cDistance, cPosition]:
        A : cPosition = arrivee
        D : cPosition = self.position # depart

        # distance en Rad
        distanceEnRad: float = self._routeOrthodromiqueDistanceEnRad(arrivee = A) 

        # distance en Mn
        distanceEnMn: float = distanceEnRad * cAngle.RAD2DEG * 60.0 # noeud = mille/h - 1 mille = 1 minute d'arc 
        
        # angle de route initial orthodromique
        Ad_enRad : float = math.sin(A.latitude.latitudeEnRad) - math.sin(D.latitude.latitudeEnRad) * math.cos(distanceEnRad)
        Ad_enRad = Ad_enRad / (math.sin(distanceEnRad) * math.cos(D.latitude.latitudeEnRad))
        Ad_enRad = math.acos(Ad_enRad)
        Ad : float = Ad_enRad * cAngle.RAD2DEG

        # coordonnees du vertex 1/2 route
        latitudeVertexEnRad : float = math.cos(D.latitude.latitudeEnRad) * math.sin(Ad_enRad) 
        latitudeVertexEnRad = math.acos(latitudeVertexEnRad)
        if Ad < 90.0: 
            latitudeVertexEnRad = math.fabs(latitudeVertexEnRad)
        else: 
            latitudeVertexEnRad = -1.0 * math.fabs(latitudeVertexEnRad)
        latitudeVertex : float = latitudeVertexEnRad * cAngle.RAD2DEG

        sensNavigation : cSensNavigation
        diffLongitude : float = 0
        (sensNavigation, diffLongitude)= self._routeSensNavigationLePlusCourtGradientEnDeg(arrivee)

        longitudeVertexEnRad : float = D.longitude.longitudeEnRad + (diffLongitude/math.fabs(diffLongitude)) * math.acos(math.tan(D.latitude.latitudeEnRad) / math.tan(latitudeVertexEnRad))
        if longitudeVertexEnRad > math.pi:
            longitudeVertexEnRad -= math.pi
        if longitudeVertexEnRad < -1.0 * math.pi:
            longitudeVertexEnRad += math.pi
            
        longitudeVertex : float = longitudeVertexEnRad * cAngle.RAD2DEG
         
        # Route fond initiale
        correctionDeGivryEnDeg : float = self._routeOrthodromiqueCorrectionDeGivryEnDeg(arrivee=A)
        correctionDeGivryEnDeg = 0.0
        
        Rfi : float = Ad if (sensNavigation == cSensNavigation.versNE or sensNavigation == cSensNavigation.versSE)  else 360 -Ad
        Rfi = Rfi + correctionDeGivryEnDeg
        
        return (cCap(valAsDeg=Rfi), cDistance(valAsMilleNautique=distanceEnMn), cPosition(lat=cLatitude(valAsDeg=latitudeVertex), lon=cLongitude(valAsDeg=longitudeVertex)))



    def _routeOrthodromiqueDistanceEnRad(self, arrivee : cPosition) -> float:
        A : cPosition = arrivee
        D : cPosition = self.position # depart
        dG : float
        (_, dG)= self._routeSensNavigationLePlusCourtGradientEnDeg(arrivee)

        # distance en Mn
        distanceEnRad: float = 0.0
        distanceEnRad = math.sin(A.latitude.latitudeEnRad) * math.sin(D.latitude.latitudeEnRad) 
        distanceEnRad += math.cos(A.latitude.latitudeEnRad) * math.cos(D.latitude.latitudeEnRad) * math.cos(dG * cAngle.DEG2RAD)
        distanceEnRad = math.acos(distanceEnRad)   
        return distanceEnRad

    def _routeOrthodromiqueCorrectionDeGivryEnDeg(self, arrivee : cPosition) -> float:
        A : cPosition = arrivee
        D : cPosition = self.position # depart
        latitudeMoyenneEnRad : float = (A.latitude.latitudeEnRad + D.latitude.latitudeEnRad) / 2.0
        dG : float
        (_, dG)= self._routeSensNavigationLePlusCourtGradientEnDeg(arrivee)

        # distance en Mn
        givryEnDeg: float = 0.5 * (dG) - math.sin(latitudeMoyenneEnRad)
        return givryEnDeg

    def _routeSensNavigationLePlusCourtGradientEnDeg(self, a : cPosition) -> Tuple[cSensNavigation, float]:
        d : cPosition = self.position 
        dLat = a.latitude.latitudeEnDeg - d.latitude.latitudeEnDeg

        dG : float = a.longitude.longitudeEnDeg - d.longitude.longitudeEnDeg

        """
        Si |Δλ| > 180°, on prend la différence la plus courte :

        Δλcorrige={Δλ - 360°	si Δλ>180°
                    Δλ+360°	si Δλ<-180°

        Cela donne le sens le plus court.
        """
        if math.fabs(dG) > 180.0:
            dG = dG - 360 if dG > 180 else dG + 360
        
        sensNavigation : cSensNavigation = cSensNavigation.undef
        if dG >= 0 and dLat >= 0:
            sensNavigation = cSensNavigation.versNE
        if dG >= 0 and dLat < 0:
            sensNavigation = cSensNavigation.versSE
        if dG < 0 and dLat >= 0:
            sensNavigation = cSensNavigation.versNW
        if dG < 0 and dLat < 0:
            sensNavigation = cSensNavigation.versSW
        return (sensNavigation, dG)
    

    def navLoxodromiqueCapEtVitesseDonnes(self, tempsDeNavEnSeconde: float, v: cVelocite) -> cPosition:

        vitesseEnNoeud : float = v.vitesse.asNoeud
        distanceEnMille : float = vitesseEnNoeud * (tempsDeNavEnSeconde / 3600.0)

        capRad: float = v.sens.asAngleTrigonometriqueEnRad
        latitudeEstimeeRad: float = self.position.latitude.latitudeEnRad
        
        pasEnLatitudeEnDeg: float =  ( math.sin(capRad) * distanceEnMille / 60.0 )  # noeud = mille/h - 1 mille = 1 minute d'arc
        pasEnLongitudeEnDeg: float = ( math.cos(capRad) * distanceEnMille / 60.0) * math.cos(latitudeEstimeeRad)

        positionLatitudeDeg: float = self.position.latitude.latitudeEnDeg + pasEnLatitudeEnDeg
        positionLongitudeDeg: float = self.position.longitude.longitudeEnDeg + pasEnLongitudeEnDeg
        
        return cPosition(lat=cLatitude(valAsDeg=positionLatitudeDeg), lon=cLongitude(valAsDeg=positionLongitudeDeg))
