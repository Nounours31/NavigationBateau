from typing import Dict, List

import pytest

from sfa_navigation import (
    cAngle,
    cPosition,
    cLatitude,
    cLongitude,
    cVelocite,
    eAngleFormat,
    cVitesse,
    cNavigationFormules, 
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
        n: cNavigationFormules = cNavigationFormules(position=d)

        sens : cCap
        distance : cDistance
        (sens , distance) =  n.routeLoxodromique(a)
        assert distance.asMn == pytest.approx(65.5779, 0.001)
        assert sens.capAsDeg == pytest.approx(6.95, 0.01)

    def test_positionnementRelatif2(self):
        d: cPosition = cPosition(lat=cLatitude(valAsDeg=49.1), lon=cLongitude(valAsDeg=2.5))
        a: cPosition = cPosition(lat=cLatitude(valAsDeg=28.1), lon=cLongitude(valAsDeg=-20.5))
        n: cNavigationFormules = cNavigationFormules(position=d)

        sens : cCap
        distance : cDistance
        (sens , distance) =  n.routeLoxodromique(a)
        assert distance.asMn == pytest.approx(1644, 1)
        assert sens.capAsDeg == pytest.approx(210, 1)

    def test_positionnementRelatif3(self):
        d: cPosition = cPosition(lat=cLatitude(valAsDeg=49.1), lon=cLongitude(valAsDeg=2.5))
        a: cPosition = cPosition(lat=cLatitude(valAsDeg=49.5), lon=cLongitude(valAsDeg=-50.7))
        n: cNavigationFormules = cNavigationFormules(position=d)

        sens : cCap
        distance : cDistance
        (sens , distance) =  n.routeLoxodromique(a)
        assert distance.asMn == pytest.approx(2042, 1)
        assert sens.capAsDeg == pytest.approx(270, 1)

    def test_positionnementRelatif4(self):
        d: cPosition = cPosition(lat=cLatitude(valAsDeg=23.7), lon=cLongitude(valAsDeg=-67.1))
        a: cPosition = cPosition(lat=cLatitude(valAsDeg=49.5), lon=cLongitude(valAsDeg=-0.8))
        n: cNavigationFormules = cNavigationFormules(position=d)

        sens : cCap
        distance : cDistance
        (sens , distance) =  n.routeLoxodromique(a)
        assert distance.asMn == pytest.approx(3427, 1)
        assert sens.capAsDeg == pytest.approx(45, 1)

    def test_positionnementRelatif5(self):
        d: cPosition = cPosition(lat=cLatitude(valAsDeg=23.7), lon=cLongitude(valAsDeg=-67.1))
        a: cPosition = cPosition(lat=cLatitude(valAsDeg=-34), lon=cLongitude(valAsDeg=18.8))
        n: cNavigationFormules = cNavigationFormules(position=d)

        sens : cCap
        distance : cDistance
        (sens , distance) =  n.routeLoxodromique(a)
        assert distance.asMn == pytest.approx(6003, 1)
        assert sens.capAsDeg == pytest.approx(125, 1)

    def test_positionnementRelatif6(self):
        d: cPosition = cPosition(lat=cLatitude(valAsDeg=49.5), lon=cLongitude(valAsDeg=-56.5))
        a: cPosition = cPosition(lat=cLatitude(valAsDeg=49.5), lon=cLongitude(valAsDeg=-5.7))
        n: cNavigationFormules = cNavigationFormules(position=d)

        sens : cCap
        distance : cDistance
        (sens , distance) =  n.routeLoxodromique(a)
        assert distance.asMn == pytest.approx(1981, 1)
        assert sens.capAsDeg == pytest.approx(90, 1)

    def test_positionnementRelatif7(self):
        d: cPosition = cPosition(lat=cLatitude(valAsDeg=80), lon=cLongitude(valAsDeg=-56.5))
        a: cPosition = cPosition(lat=cLatitude(valAsDeg=80), lon=cLongitude(valAsDeg=-5.7))
        n: cNavigationFormules = cNavigationFormules(position=d)

        sens : cCap
        distance : cDistance
        (sens , distance) =  n.routeLoxodromique(a)
        assert distance.asMn == pytest.approx(604, 1)
        assert sens.capAsDeg == pytest.approx(90, 1)





    # voir le pdf : AC_FP_ortho.pdf 
    def test_routeLoxodromique_TD_AC_FP_ortho(self):
        d: cPosition = cPosition(lat=cLatitude.fromString("48°23'"), lon=cLongitude.fromString("4°29'"))
        n: cNavigationFormules = cNavigationFormules(position=d)
        
        a: cPosition = cPosition(lat=cLatitude.fromString("20°27'"), lon=cLongitude.fromString("68°32'"))
        sens: cCap
        distance: cDistance
        vertex: cPosition
        (sens, distance) = n.routeLoxodromique(arrivee=a)

        assert distance.asMn == pytest.approx(3531.6, 0.001) 
        assert sens.capAsDeg == pytest.approx(118.2, 0.01) 


    def test_routeOrthodromique_TD_AC_FP_ortho(self):
        d: cPosition = cPosition(lat=cLatitude.fromString("48°23'"), lon=cLongitude.fromString("4°29'"))
        n: cNavigationFormules = cNavigationFormules(position=d)
        
        a: cPosition = cPosition(lat=cLatitude.fromString("20°27'"), lon=cLongitude.fromString("68°32'"))
        sens: cCap
        distance: cDistance
        vertex: cPosition
        (sens, distance, vertex) = n.routeOrthodromique(arrivee=a, methode=cMethodeCalcul.FromENMM)

        assert distance.asMn == pytest.approx(3465.4, 0.001) 
        assert sens.capAsDeg == pytest.approx(95.1, 0.01) 
        vertexRef = cPosition(lat=cLatitude.fromString("52°20' N"), lon=cLongitude.fromString("25°35' W"))
        (_, distance2) = cNavigationFormules(vertexRef).routeLoxodromique(arrivee=vertex)
        # pas clair et peu utile je zap
        # assert distance2.asMn == pytest.approx(1.5 , 1) 








    # exo : https://ressources.univ-lemans.fr/AccesLibre/UM/Pedago/physique/02/divers/ortholoxo.html 
    positionParis : cPosition = cPosition (lat=cLatitude.fromString("48°51' N"), lon=cLongitude.fromString("2°21' E")) # Paris
    positionNewYork : cPosition = cPosition(lat=cLatitude.fromString("40°43' N"), lon=cLongitude.fromString("74°00' W")) # New York
    positionTokyo : cPosition = cPosition(lat=cLatitude.fromString("35°41' N"), lon=cLongitude.fromString("139°45' E")) # Tokyo
    positionSaoPaulo : cPosition = cPosition(lat=cLatitude.fromString("23°33' S"), lon=cLongitude.fromString("46°38' W")) # Sao Paulo


    def test_routeLoxodromique_ParisNewYork(self):
        d: cPosition = cNavigation_tests.positionParis
        n: cNavigationFormules = cNavigationFormules(position=d)
        
        a: cPosition =  cNavigation_tests.positionNewYork
        sens: cCap
        distance: cDistance
        (sens, distance) = n.routeLoxodromique(arrivee=a)
        
        assert distance.asMn == pytest.approx(3279.9, 0.001) 
        assert sens.capAsDeg == pytest.approx(261.43, 0.01)



    def test_routeOrthodromique_ParisNewYork(self):
        d: cPosition = cNavigation_tests.positionParis
        n: cNavigationFormules = cNavigationFormules(position=d)
        
        a: cPosition = cNavigation_tests.positionNewYork
        sens: cCap
        distance: cDistance
        vertex: cPosition
        (sens, distance, vertex) = n.routeOrthodromique(arrivee=a, methode=cMethodeCalcul.FromENMM)

        assert distance.asMn == pytest.approx(3149.4, 0.001) 
        assert sens.capAsDeg == pytest.approx(291.79, 0.01) 
        vertexRef = cPosition(lat=cLatitude.fromString("52°20' N"), lon=cLongitude.fromString("25°35' W"))
        (_, distance2) = cNavigationFormules(vertexRef).routeLoxodromique(arrivee=vertex)
        assert distance2.asMn == pytest.approx(1.5 , 1) 
                

    def test_routeOrthodromique_ParisTokyo(self):
        d: cPosition = cNavigation_tests.positionParis
        n: cNavigationFormules = cNavigationFormules(position=d)
        
        a: cPosition = cNavigation_tests.positionTokyo
        sens: cCap
        distance: cDistance
        vertex: cPosition
        (sens, distance, vertex) = n.routeOrthodromique(arrivee=a, methode = cMethodeCalcul.FromENMM)

        assert distance.asMn == pytest.approx(5242.5, 0.001) # 12140 en loxodromie 
        assert sens.capAsDeg == pytest.approx(33.40, 0.01) 
        vertexRef = cPosition(lat=cLatitude.fromString("68°45' N"), lon=cLongitude.fromString("65°55' E"))
        (_, distance2) = cNavigationFormules(vertexRef).routeLoxodromique(arrivee=vertex)
        assert distance2.asMn == pytest.approx(1.5 , 1) 

    def test_routeLoxodromique_ParisTokyo(self):
        d: cPosition = cNavigation_tests.positionParis
        n: cNavigationFormules = cNavigationFormules(position=d)
        
        a: cPosition = cNavigation_tests.positionTokyo
        sens: cCap
        distance: cDistance
        vertex: cPosition
        (sens, distance) = n.routeLoxodromique(arrivee=a)

        assert distance.asMn == pytest.approx(6116.2, 0.001) # 12140 en loxodromie 
        assert sens.capAsDeg == pytest.approx(97.42, 0.01) 

    def test_routeOrthodromique_ParisSaoPaulo(self):
        d: cPosition = cNavigation_tests.positionParis
        n: cNavigationFormules = cNavigationFormules(position=d)
        
        a: cPosition = cNavigation_tests.positionSaoPaulo 
        sens: cCap
        distance: cDistance
        vertex: cPosition
        (sens, distance, vertex) = n.routeOrthodromique(arrivee=a, methode = cMethodeCalcul.FromENMM)

        assert distance.asMn == pytest.approx(5072.8, 0.001) # 6130 en loxodromie 
        assert sens.capAsDeg == pytest.approx(224.0, 0.01) 
        vertexRef = cPosition(lat=cLatitude.fromString("62°46' S"), lon=cLongitude.fromString("123°44' W"))
        (_, distance2) = cNavigationFormules(vertexRef).routeLoxodromique(arrivee=vertex)
        assert distance2.asMn == pytest.approx(1.5 , 1) 

    def test_routeLoxodromique_ParisSaoPaulo(self):
        d: cPosition = cNavigation_tests.positionParis
        n: cNavigationFormules = cNavigationFormules(position=d)
        
        a: cPosition = cNavigation_tests.positionSaoPaulo 
        sens: cCap
        distance: cDistance
        (sens, distance) = n.routeLoxodromique(arrivee=a)

        assert distance.asMn == pytest.approx(5087, 0.001) # 6130 en loxodromie 
        assert sens.capAsDeg == pytest.approx(211.35, 0.01) 



    # voir le ods : orthosimple.ods 
    def test_routeLoxodromique_Orthodromique(self):
        test : List[Dict[str, object]] = [
            {
                "depart": cPosition(lat=cLatitude.fromString("60°17' N"), lon=cLongitude.fromString("100° W")),
                "arrivee": cPosition(lat=cLatitude.fromString("78°51' N"), lon=cLongitude.fromString("2°21' E")),
                "ortho": {
                    "distance": 2024.2,
                    "cap": 19.885
                },
                "loxo": {
                    "distance": 2280.8,
                    "cap": 60.7
                }
            },
            {
                "depart": cPosition(lat=cLatitude.fromString("60°17' N"), lon=cLongitude.fromString("100° W")),
                "arrivee": cPosition(lat=cLatitude.fromString("48°51' N"), lon=cLongitude.fromString("2°21' E")),
                "ortho": {
                    "distance": 3255.2,
                    "cap": 52.37
                },
                "loxo": {
                    "distance": 3596.8,
                    "cap": 101
                }
            },
            {
                "depart": cPosition(lat=cLatitude.fromString("48°51' N"), lon=cLongitude.fromString("2°21' E")),
                "arrivee": cPosition(lat=cLatitude.fromString("60°17' N"), lon=cLongitude.fromString("100° W")),
                "ortho": {
                    "distance": 3255.2,
                    "cap": 323.37
                },
                "loxo": {
                    "distance": 3596.8,
                    "cap": 280.99
                }
            },
            {
                "depart": cPosition(lat=cLatitude.fromString("48°51' N"), lon=cLongitude.fromString("2°21' E")),
                "arrivee": cPosition(lat=cLatitude.fromString("10°17' N"), lon=cLongitude.fromString("100° W")),
                "ortho": {
                    "distance": 5414.0,
                    "cap": 286.02
                },
                "loxo": {
                    "distance": 5665.2,
                    "cap": 245.89
                }
            },
            {
                "depart": cPosition(lat=cLatitude.fromString("43°03' N"), lon=cLongitude.fromString("2°10' E")),
                "arrivee": cPosition(lat=cLatitude.fromString("56°17' N"), lon=cLongitude.fromString("122°46' W")),
                "ortho": {
                    "distance": 4223.7,
                    "cap": 331.1
                },
                "loxo": {
                    "distance": 4875.7,
                    "cap": 279.3
                }
            },
            {
                "depart": cPosition(lat=cLatitude.fromString("1°03' S"), lon=cLongitude.fromString("175°10' E")),
                "arrivee": cPosition(lat=cLatitude.fromString("1°17' N"), lon=cLongitude.fromString("174°46' W")),
                "ortho": {
                    "distance": 620,
                    "cap": 76.97
                },
                "loxo": {
                    "distance": 620,
                    "cap": 76.95
                }
            }
        ]

        sensOrtho: cCap
        distanceOrtho: cDistance
        vertex: cPosition
        sensLoxo: cCap
        distanceLoxo: cDistance
        iCasDeTest : int = 0
        for t in test:
            iCasDeTest +=1
            print (f"cas de test {iCasDeTest:2d}")

            d : cPosition = cPosition.fromObject(t["depart"])
            a : cPosition = cPosition.fromObject(t["arrivee"])
            n: cNavigationFormules = cNavigationFormules(position=d)

            (sensOrtho, distanceOrtho, vertex) = n.routeOrthodromique(arrivee=a, methode=cMethodeCalcul.FromENMM)
            assert distanceOrtho.asMn == pytest.approx(t["ortho"]["distance"], 0.001) 
            assert sensOrtho.capAsDeg == pytest.approx(t["ortho"]["cap"], 0.01) 

            (sensLoxo, distanceLoxo) = n.routeLoxodromique(arrivee=a)
            assert distanceLoxo.asMn == pytest.approx(t["loxo"]["distance"], 0.001) 
            assert sensLoxo.capAsDeg == pytest.approx(t["loxo"]["cap"], 0.01) 





    def test_Navivation(self):
        d: cPosition = cPosition(lat=cLatitude(valAsDeg=70), lon=cLongitude(valAsDeg=-56.5))
        n: cNavigationFormules = cNavigationFormules(position=d)
        norme : cVitesse = cVitesse(valAsNoeud=10)
        sens : cCap = cCap(valAsDeg=0)
        v: cVelocite = cVelocite(vitesse=norme, sens=sens)

        a : cPosition = n.navACapEtVitesseDonnes(tempsDeNavEnSeconde=60*60, v=v)
        sens2: cCap
        distance: cDistance
        (sens2, distance) = n.routeLoxodromique(arrivee=a)
        
        assert distance.asMn == pytest.approx(10, 0.01)
        assert sens2.capAsDeg == pytest.approx(0, 0.01)


    def test_Navivation_poleNord(self):
        d: cPosition = cPosition(lat=cLatitude(valAsDeg=89), lon=cLongitude(valAsDeg=2))
        n: cNavigationFormules = cNavigationFormules(position=d)
        norme : cVitesse = cVitesse(valAsNoeud=120)
        sens : cCap = cCap(valAsDeg=0)
        v: cVelocite = cVelocite(vitesse=norme, sens=sens)

        a : cPosition = n.navACapEtVitesseDonnes(tempsDeNavEnSeconde=60*60, v=v)
        sens2: cCap
        distance: cDistance
        (sens2, distance, _) = n.routeOrthodromique(arrivee=a)
        
        assert distance.asMn == pytest.approx(120, 0.001)
        assert sens2.capAsDeg == pytest.approx(360, 0.01)

    def test_Navivation_poleSud(self):
        d: cPosition = cPosition(lat=cLatitude(valAsDeg=-89), lon=cLongitude(valAsDeg=2))
        n: cNavigationFormules = cNavigationFormules(position=d)
        norme : cVitesse = cVitesse(valAsNoeud=120)
        sens : cCap = cCap(valAsDeg=180)
        v: cVelocite = cVelocite(vitesse=norme, sens=sens)

        a : cPosition = n.navACapEtVitesseDonnes(tempsDeNavEnSeconde=60*60, v=v)
        sens2: cCap
        distance: cDistance
        (sens2, distance, _) = n.routeOrthodromique(arrivee=a)
        
        assert distance.asMn == pytest.approx(120, 0.001)
        assert sens2.capAsDeg == pytest.approx(181, 0.01)

    def test_Navivation_antemeridienVersEst(self):
        d: cPosition = cPosition(lat=cLatitude(valAsDeg=10), lon=cLongitude(valAsDeg=179))
        n: cNavigationFormules = cNavigationFormules(position=d)
        norme : cVitesse = cVitesse(valAsNoeud=120)
        sens : cCap = cCap(valAsDeg=90)
        v: cVelocite = cVelocite(vitesse=norme, sens=sens)

        a : cPosition = n.navACapEtVitesseDonnes(tempsDeNavEnSeconde=60*60, v=v)
        sens2: cCap
        distance: cDistance
        (sens2, distance, _) = n.routeOrthodromique(arrivee=a)
        
        assert distance.asMn == pytest.approx(120, 0.001)
        assert sens2.capAsDeg == pytest.approx(90, 0.01)

    def test_Navivation_antemeridienVersOuest(self):
        d: cPosition = cPosition(lat=cLatitude(valAsDeg=10), lon=cLongitude(valAsDeg=-179))
        n: cNavigationFormules = cNavigationFormules(position=d)
        norme : cVitesse = cVitesse(valAsNoeud=120)
        sens : cCap = cCap(valAsDeg=270)
        v: cVelocite = cVelocite(vitesse=norme, sens=sens)

        a : cPosition = n.navACapEtVitesseDonnes(tempsDeNavEnSeconde=60*60, v=v)
        sens2: cCap
        distance: cDistance
        (sens2, distance, _) = n.routeOrthodromique(arrivee=a)
        
        assert distance.asMn == pytest.approx(120, 0.001)
        assert sens2.capAsDeg == pytest.approx(270, 0.01)


    def test_Navivation_navACapVitesseCourantVentDonnes_1(self):
        d: cPosition = cPosition(lat=cLatitude(valAsDeg=40), lon=cLongitude(valAsDeg=1))
        n: cNavigationFormules = cNavigationFormules(position=d)

        norme : cVitesse = cVitesse(valAsNoeud=120)
        sens : cCap = cCap(valAsDeg=90)
        vitesse: cVelocite = cVelocite(vitesse=norme, sens=sens)

        courant : cVelocite = cVelocite(vitesse=cVitesse(valAsNoeud=1.0), sens=cCap(valAsDeg=180))
        vent : cVelocite = cVelocite(vitesse=cVitesse(valAsNoeud=20.0), sens=cCap(valAsDeg=180))
        derive : cAngle = cAngle(valAsDeg=5.0)
        a : cPosition = n.navACapVitesseCourantVentDonnes(tempsDeNavEnSeconde=60*60, v=vitesse, courant=courant, vent = vent, derive = derive)
        sens2: cCap
        distance: cDistance
        (sens2, distance) = n.routeLoxodromique(arrivee=a)
        
        assert distance.asMn == pytest.approx(120.106, 0.0001)
        assert sens2.capAsDeg == pytest.approx(95.4746, 0.01)

    def test_Navivation_navACapVitesseCourantVentDonnes_2(self):
        d: cPosition = cPosition(lat=cLatitude(valAsDeg=40), lon=cLongitude(valAsDeg=1))
        n: cNavigationFormules = cNavigationFormules(position=d)

        norme : cVitesse = cVitesse(valAsNoeud=120)
        sens : cCap = cCap(valAsDeg=90)
        vitesse: cVelocite = cVelocite(vitesse=norme, sens=sens)

        courant : cVelocite = cVelocite(vitesse=cVitesse(valAsNoeud=1.0), sens=cCap(valAsDeg=180))
        vent : cVelocite = cVelocite(vitesse=cVitesse(valAsNoeud=20.0), sens=cCap(valAsDeg=0))
        derive : cAngle = cAngle(valAsDeg=5.0)
        a : cPosition = n.navACapVitesseCourantVentDonnes(tempsDeNavEnSeconde=60*60, v=vitesse, courant=courant, vent = vent, derive = derive)

        sens3: cCap
        distance3: cDistance
        (sens3, distance3) = n.routeLoxodromique(arrivee=a)


        assert distance3.asMn == pytest.approx(119.93, 0.0001)
        assert sens3.capAsDeg == pytest.approx(85.47, 0.01)

    def test_Navivation_navACapVitesseCourantVentDonnes_3(self):
        d: cPosition = cPosition(lat=cLatitude(valAsDeg=40), lon=cLongitude(valAsDeg=1))
        n: cNavigationFormules = cNavigationFormules(position=d)

        norme : cVitesse = cVitesse(valAsNoeud=120)
        sens : cCap = cCap(valAsDeg=270)
        vitesse: cVelocite = cVelocite(vitesse=norme, sens=sens)

        courant : cVelocite = cVelocite(vitesse=cVitesse(valAsNoeud=1.0), sens=cCap(valAsDeg=180))
        vent : cVelocite = cVelocite(vitesse=cVitesse(valAsNoeud=20.0), sens=cCap(valAsDeg=180))
        derive : cAngle = cAngle(valAsDeg=5.0)
        a : cPosition = n.navACapVitesseCourantVentDonnes(tempsDeNavEnSeconde=60*60, v=vitesse, courant=courant, vent = vent, derive = derive)

        sens3: cCap
        distance3: cDistance
        (sens3, distance3) = n.routeLoxodromique(arrivee=a)


        assert distance3.asMn == pytest.approx(120.106, 0.0001)
        assert sens3.capAsDeg == pytest.approx(264.52, 0.01)

    def test_Navivation_navACapVitesseCourantVentDonnes_4(self):
        d: cPosition = cPosition(lat=cLatitude(valAsDeg=40), lon=cLongitude(valAsDeg=1))
        n: cNavigationFormules = cNavigationFormules(position=d)

        norme : cVitesse = cVitesse(valAsNoeud=120)
        sens : cCap = cCap(valAsDeg=270)
        vitesse: cVelocite = cVelocite(vitesse=norme, sens=sens)

        courant : cVelocite = cVelocite(vitesse=cVitesse(valAsNoeud=1.0), sens=cCap(valAsDeg=180))
        vent : cVelocite = cVelocite(vitesse=cVitesse(valAsNoeud=20.0), sens=cCap(valAsDeg=0))
        derive : cAngle = cAngle(valAsDeg=5.0)
        a : cPosition = n.navACapVitesseCourantVentDonnes(tempsDeNavEnSeconde=60*60, v=vitesse, courant=courant, vent = vent, derive = derive)

        sens3: cCap
        distance3: cDistance
        (sens3, distance3) = n.routeLoxodromique(arrivee=a)


        assert distance3.asMn == pytest.approx(119.93, 0.0001)
        assert sens3.capAsDeg == pytest.approx(274.52, 0.01)

    def test_Navivation_navACapVitesseCourantVentDonnes_5(self):
        d: cPosition = cPosition(lat=cLatitude(valAsDeg=40), lon=cLongitude(valAsDeg=1))
        n: cNavigationFormules = cNavigationFormules(position=d)

        norme : cVitesse = cVitesse(valAsNoeud=120)
        sens : cCap = cCap(valAsDeg=270)
        vitesse: cVelocite = cVelocite(vitesse=norme, sens=sens)

        courant : cVelocite = cVelocite(vitesse=cVitesse(valAsNoeud=1.0), sens=cCap(valAsDeg=0))
        vent : cVelocite = cVelocite(vitesse=cVitesse(valAsNoeud=20.0), sens=cCap(valAsDeg=0))
        derive : cAngle = cAngle(valAsDeg=5.0)
        a : cPosition = n.navACapVitesseCourantVentDonnes(tempsDeNavEnSeconde=60*60, v=vitesse, courant=courant, vent = vent, derive = derive)

        sens3: cCap
        distance3: cDistance
        (sens3, distance3) = n.routeLoxodromique(arrivee=a)


        assert distance3.asMn == pytest.approx(120.076, 0.0001)
        assert sens3.capAsDeg == pytest.approx(275.47, 0.01)
