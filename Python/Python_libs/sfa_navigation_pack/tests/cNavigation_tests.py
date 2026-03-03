import pytest

from sfa_navigation import (
    cPosition,
    cLatitude,
    cLongitude,
    cVelocite,
    eAngleFormat,
    cVitesse,
    cNavigation, 
    cCap, 
    cDistance,
)
from sfa_navigation import cMethodeCalcul 

from myEnv import myEnv


class cNavigation_tests:
    def test_init(self):
        ll: cLatitude = cLatitude(0.0)
        longi: cLongitude = cLongitude(0.0)
        p: cPosition = cPosition(lat=ll, lon=longi)
        assert p.toString(format=eAngleFormat.DMM) == "N 000°00.000', E 000°00.000'"

    def test_positionnementRelatif(self):
        a: cPosition = myEnv.postionStQuay
        d: cPosition = myEnv.postionTrinitee
        n: cNavigation = cNavigation(position=d)

        sens : cCap
        distance : cDistance
        (sens , distance) =  n.routeLoxodromique(a)
        assert distance.asMn == pytest.approx(65.5779, 0.001)
        assert sens.capAsDeg == pytest.approx(6.95, 0.01)

    def test_positionnementRelatif2(self):
        d: cPosition = cPosition(lat=cLatitude(valAsDeg=49.1), lon=cLongitude(valAsDeg=2.5))
        a: cPosition = cPosition(lat=cLatitude(valAsDeg=28.1), lon=cLongitude(valAsDeg=-20.5))
        n: cNavigation = cNavigation(position=d)

        sens : cCap
        distance : cDistance
        (sens , distance) =  n.routeLoxodromique(a)
        assert distance.asMn == pytest.approx(1644, 1)
        assert sens.capAsDeg == pytest.approx(210, 1)

    def test_positionnementRelatif3(self):
        d: cPosition = cPosition(lat=cLatitude(valAsDeg=49.1), lon=cLongitude(valAsDeg=2.5))
        a: cPosition = cPosition(lat=cLatitude(valAsDeg=49.5), lon=cLongitude(valAsDeg=-50.7))
        n: cNavigation = cNavigation(position=d)

        sens : cCap
        distance : cDistance
        (sens , distance) =  n.routeLoxodromique(a)
        assert distance.asMn == pytest.approx(2042, 1)
        assert sens.capAsDeg == pytest.approx(270, 1)

    def test_positionnementRelatif4(self):
        d: cPosition = cPosition(lat=cLatitude(valAsDeg=23.7), lon=cLongitude(valAsDeg=-67.1))
        a: cPosition = cPosition(lat=cLatitude(valAsDeg=49.5), lon=cLongitude(valAsDeg=-0.8))
        n: cNavigation = cNavigation(position=d)

        sens : cCap
        distance : cDistance
        (sens , distance) =  n.routeLoxodromique(a)
        assert distance.asMn == pytest.approx(3427, 1)
        assert sens.capAsDeg == pytest.approx(45, 1)

    def test_positionnementRelatif5(self):
        d: cPosition = cPosition(lat=cLatitude(valAsDeg=23.7), lon=cLongitude(valAsDeg=-67.1))
        a: cPosition = cPosition(lat=cLatitude(valAsDeg=-34), lon=cLongitude(valAsDeg=18.8))
        n: cNavigation = cNavigation(position=d)

        sens : cCap
        distance : cDistance
        (sens , distance) =  n.routeLoxodromique(a)
        assert distance.asMn == pytest.approx(6003, 1)
        assert sens.capAsDeg == pytest.approx(125, 1)

    def test_positionnementRelatif6(self):
        d: cPosition = cPosition(lat=cLatitude(valAsDeg=49.5), lon=cLongitude(valAsDeg=-56.5))
        a: cPosition = cPosition(lat=cLatitude(valAsDeg=49.5), lon=cLongitude(valAsDeg=-5.7))
        n: cNavigation = cNavigation(position=d)

        sens : cCap
        distance : cDistance
        (sens , distance) =  n.routeLoxodromique(a)
        assert distance.asMn == pytest.approx(1981, 1)
        assert sens.capAsDeg == pytest.approx(90, 1)

    def test_positionnementRelatif7(self):
        d: cPosition = cPosition(lat=cLatitude(valAsDeg=80), lon=cLongitude(valAsDeg=-56.5))
        a: cPosition = cPosition(lat=cLatitude(valAsDeg=80), lon=cLongitude(valAsDeg=-5.7))
        n: cNavigation = cNavigation(position=d)

        sens : cCap
        distance : cDistance
        (sens , distance) =  n.routeLoxodromique(a)
        assert distance.asMn == pytest.approx(604, 1)
        assert sens.capAsDeg == pytest.approx(90, 1)


    def test_Navivation(self):
        d: cPosition = cPosition(lat=cLatitude(valAsDeg=70), lon=cLongitude(valAsDeg=-56.5))
        n: cNavigation = cNavigation(position=d)
        norme : cVitesse = cVitesse(valAsNoeud=10)
        sens : cCap = cCap(valAsDeg=0)
        v: cVelocite = cVelocite(vitesse=norme, sens=sens)

        a : cPosition = n.navLoxodromiqueCapEtVitesseDonnes(tempsDeNavEnSeconde=60*60, v=v)
        sens2: cCap
        distance: cDistance
        (sens2, distance) = n.routeLoxodromique(arrivee=a)
        
        assert distance.asMn == pytest.approx(10, 0.01)
        assert sens2.capAsDeg == pytest.approx(0, 0.01)

    # exo : https://ressources.univ-lemans.fr/AccesLibre/UM/Pedago/physique/02/divers/ortholoxo.html 
    positionParis : cPosition = cPosition (lat=cLatitude.fromString("48°51' N"), lon=cLongitude.fromString("2°21' E")) # Paris
    positionNewYork : cPosition = cPosition(lat=cLatitude.fromString("40°43' N"), lon=cLongitude.fromString("74°00' W")) # New York
    positionTokyo : cPosition = cPosition(lat=cLatitude.fromString("35°41' N"), lon=cLongitude.fromString("139°45' E")) # Tokyo
    positionSaoPaulo : cPosition = cPosition(lat=cLatitude.fromString("23°33' S"), lon=cLongitude.fromString("46°38' W")) # Sao Paulo

    def test_routeLoxodromique_ParisNewYork(self):
        d: cPosition = cNavigation_tests.positionParis
        n: cNavigation = cNavigation(position=d)
        
        a: cPosition =  cNavigation_tests.positionNewYork
        sens: cCap
        distance: cDistance
        (sens, distance) = n.routeLoxodromique(arrivee=a)
        
        assert distance.asKm == pytest.approx(12547.00, 0.01) 
        assert sens.capAsDeg == pytest.approx(261.43, 0.01)

    def test_routeOrthodromique_ParisNewYork(self):
        d: cPosition = cNavigation_tests.positionParis
        n: cNavigation = cNavigation(position=d)
        
        a: cPosition = cNavigation_tests.positionNewYork
        sens: cCap
        distance: cDistance
        vertex: cPosition
        (sens, distance, vertex) = n.routeOrthodromique(arrivee=a, methode=cMethodeCalcul.FromENMM)

        assert distance.asKm == pytest.approx(11630, 0.01) 
        assert sens.capAsDeg == pytest.approx(291.79, 0.01) 
        vertexRef = cPosition(lat=cLatitude.fromString("52°20' N"), lon=cLongitude.fromString("25°35' W"))
        (_, distance2) = cNavigation(vertexRef).routeLoxodromique(arrivee=vertex)
        assert distance2.asMn == pytest.approx(1.5 , 1) 
                

    def test_routeOrthodromique_ParisTokyo(self):
        d: cPosition = cNavigation_tests.positionParis
        n: cNavigation = cNavigation(position=d)
        
        a: cPosition = cNavigation_tests.positionTokyo
        sens: cCap
        distance: cDistance
        vertex: cPosition
        (sens, distance, vertex) = n.routeOrthodromique(arrivee=a, methode = cMethodeCalcul.FromENMM)

        assert distance.asMn == pytest.approx(5253, 0.01) # 6130 en loxodromie 
        assert sens.capAsDeg == pytest.approx(33.40, 0.01) 
        vertexRef = cPosition(lat=cLatitude.fromString("68°45' N"), lon=cLongitude.fromString("65°55' E"))
        (_, distance2) = cNavigation(vertexRef).routeLoxodromique(arrivee=vertex)
        assert distance2.asMn == pytest.approx(1.5 , 1) 

    def test_routeOrthodromique_ParisSaoPaulo(self):
        d: cPosition = cNavigation_tests.positionParis
        n: cNavigation = cNavigation(position=d)
        
        a: cPosition = cNavigation_tests.positionSaoPaulo 
        sens: cCap
        distance: cDistance
        vertex: cPosition
        (sens, distance, vertex) = n.routeOrthodromique(arrivee=a, methode = cMethodeCalcul.FromENMM)

        assert distance.asKm == pytest.approx(9401.7, 0.01) # 6130 en loxodromie 
        assert sens.capAsDeg == pytest.approx(44.0, 0.01) 
        vertexRef = cPosition(lat=cLatitude.fromString("62°46' S"), lon=cLongitude.fromString("123°44' W"))
        (_, distance2) = cNavigation(vertexRef).routeLoxodromique(arrivee=vertex)
        assert distance2.asMn == pytest.approx(1.5 , 1) 
