from __future__ import annotations

import math
import numpy as np

from enum import Enum
from typing import List, Tuple

from pSfaTools.mMyException import cMyException

from .mAngle import cAngle, eAngleFormat
from .mCap import cCap
from .mDistance import cDistance
from .mVelocite import cVelocite
from .mPosition import cPosition
from .mLongitude import cLongitude
from .mLatitude import cLatitude


class cMethodeCalcul(Enum):
    FromENMM = 0
    FromWikiPedia = 1

class cSensNavigation(Enum):
    undef = 0
    versNE = 4
    versNW = 5
    versSE = 6
    versSW = 7

    @staticmethod
    def versEst(x : cSensNavigation) -> bool :
        if (x == cSensNavigation.versNE or x == cSensNavigation.versSE):
            return True
        return False

    @staticmethod
    def versOuest(x : cSensNavigation) -> bool :
        return not cSensNavigation.versEst(x)

    @staticmethod
    def versNord(x : cSensNavigation) -> bool :
        if (x == cSensNavigation.versNE or x == cSensNavigation.versNW):
            return True
        return False

    @staticmethod
    def versSud(x : cSensNavigation) -> bool :
        return not cSensNavigation.versNord(x)


class cNavigationFormules:
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

        routeFondEnDeg = cNavigationFormules._RouteQuartFond2RouteFond(sensNavigation=sensNavigation, RouteQuartFond=RouteQuartFondEnDeg)
        
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
    def _RouteQuartFond2RouteFond(sensNavigation: cSensNavigation, RouteQuartFond: float) -> float:
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
    def routeOrthodromique(self, arrivee: cPosition, methode: cMethodeCalcul = cMethodeCalcul.FromENMM) -> tuple[cCap, cDistance, cPosition]:
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
        if math.fabs(Ad_enRad) > 1.0:
            Ad_enRad = Ad_enRad / math.fabs(Ad_enRad)
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
        
        Rfi : float = Ad if cSensNavigation.versEst(sensNavigation)  else 360 -Ad
        Rfi = Rfi + correctionDeGivryEnDeg

        # normalisation (cas particulier des cap au nord pur)
        if Rfi >= 360.0:
            Rfi -= 360.0
        if Rfi <= -360.0:
            Rfi += 360.0
        
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

    def _routeSensNavigationLePlusCourtGradientEnDeg(self, a : cPosition | None = None, v: cVelocite | None = None) -> Tuple[cSensNavigation, float]:
        d : cPosition = self.position 
        
        dLat : float = 0.0
        dG : float = 0.0
        
        if a is not None:
            dLat = a.latitude.latitudeEnDeg - d.latitude.latitudeEnDeg
            dG = a.longitude.longitudeEnDeg - d.longitude.longitudeEnDeg

            """
            Si |Δλ| > 180°, on prend la différence la plus courte :

            Δλcorrige={Δλ - 360°	si Δλ>180°
                        Δλ+360°	si Δλ<-180°

            Cela donne le sens le plus court.
            """
            if math.fabs(dG) > 180.0:
                dG = dG - 360 if dG > 180 else dG + 360

        elif v is not None:
            c : cCap = v.sens
            dLat = math.cos(c.angleAsRad)
            dG = math.cos(c.angleAsRad)

        else:
            raise cMyException ("a ou v doivent etre valuee")

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
    

    def navACapEtVitesseDonnes(self, tempsDeNavEnSeconde: float, v: cVelocite) -> cPosition:
        vitesseEnNoeud : float = v.vitesse.asNoeud
        tempsDeNavEnHeure : float = tempsDeNavEnSeconde / 3600.0
        distanceEnMille : float = vitesseEnNoeud * tempsDeNavEnHeure

        capRad: float = v.sens.angleAsRad
        latitudeEstimeeRad: float = self.position.latitude.latitudeEnRad
        
        pasEnLatitudeEnDeg: float =  ( math.cos(capRad) * distanceEnMille / 60.0 )  # noeud = mille/h - 1 mille = 1 minute d'arc
        latitudeMoyenneEnRad : float = latitudeEstimeeRad + (pasEnLatitudeEnDeg * cAngle.DEG2RAD) / 2.0
        pasEnLongitudeEnDeg: float = ( math.sin(capRad) * distanceEnMille / 60.0) / math.cos(latitudeMoyenneEnRad)

        positionLatitudeDeg: float = self.position.latitude.latitudeEnDeg + pasEnLatitudeEnDeg
        positionLongitudeDeg: float = self.position.longitude.longitudeEnDeg + pasEnLongitudeEnDeg

        # attention au passage du pole et de l'antemeridien
        sensNavigation : cSensNavigation
        (sensNavigation, _) = self._routeSensNavigationLePlusCourtGradientEnDeg (v=v)

        # les poles
        if math.fabs(positionLatitudeDeg) > 90.0:
            if cSensNavigation.versNord (sensNavigation):
                positionLatitudeDeg = 90.0 - (positionLatitudeDeg - 90.0)
                positionLongitudeDeg = positionLongitudeDeg + 180.0
            else:
                positionLatitudeDeg = -90 - (positionLatitudeDeg + 90.0)
                positionLongitudeDeg = positionLongitudeDeg + 180.0

        # l'ante meridien
        if math.fabs(positionLongitudeDeg) > 180.0:
            if positionLongitudeDeg >= 180:
                positionLongitudeDeg = -180.0 + (positionLongitudeDeg - 180.0)
            else:
                positionLongitudeDeg = 180.0 + (positionLongitudeDeg + 180.0)

        return cPosition(lat=cLatitude(valAsDeg=positionLatitudeDeg), lon=cLongitude(valAsDeg=positionLongitudeDeg))

    def navACapVitesseCourantVentDonnes(self, tempsDeNavEnSeconde: float, v: cVelocite, courant : cVelocite, vent: cVelocite, derive : cAngle ) -> cPosition:
        # calcul de la derive du au vent
        vecteurVent  = np.array([math.sin(vent.sens.capAsRad), math.cos(vent.sens.capAsRad), 0.0])
        vecteurVitesse  = np.array([math.sin(v.sens.capAsRad), math.cos(v.sens.capAsRad), 0.0])
        sensDeriveAsProdVect = np.cross(vecteurVitesse, vecteurVent)
        sensDeriveAsSign : float = 1.0
        if sensDeriveAsProdVect[2] > 0:
            sensDeriveAsSign = -1.0
        
        # calcul de la nav affectee de la derive
        v.sens = cCap(valAsDeg=(v.sens.angleAsDeg + sensDeriveAsSign * derive.angleAsDeg))
        navigationAvecVent : cPosition = self.navACapEtVitesseDonnes(tempsDeNavEnSeconde=tempsDeNavEnSeconde, v=v)


        # calcul du courant
        n : cNavigationFormules = cNavigationFormules(position=navigationAvecVent)
        navigationAvecVentEtCourant : cPosition
        navigationAvecVentEtCourant = n.navACapEtVitesseDonnes(tempsDeNavEnSeconde=tempsDeNavEnSeconde, v=courant)

        debug : bool = True
        if debug:
            xx : cNavigationFormules = cNavigationFormules(position = self.position)
            (a, b) = xx.routeLoxodromique(navigationAvecVent)
            print ((a, b))

            xx : cNavigationFormules = cNavigationFormules(position = navigationAvecVent)
            (a, b) = xx.routeLoxodromique(navigationAvecVentEtCourant)
            print ((a, b))

            xx : cNavigationFormules = cNavigationFormules(position = self.position)
            (a, b) = xx.routeLoxodromique(navigationAvecVentEtCourant)
            print ((a, b))

        # nouvelle position
        return navigationAvecVentEtCourant
