import unittest
import nmeaUDP
import platform
import math

from datetime import datetime, timedelta, timezone

class TestNMEAMethods(unittest.TestCase):
    def cc(self):
        print("Running on Python ver.{} on {} {}\n".format(
            platform.python_version(),
            platform.system(),
            platform.release()
            )
        )


    def test_nmeaCheckSup(self):
        x = nmeaUDP.NMEAChecksum("ksjhksjhksqjdhfksqjdhfkjsd")
        self.assertEqual(x, "16")

        input = (
            ("GPGSV,3,1,12,10,11,327,27,13,52,126,17,23,33,296,33,24,49,274,17,1","61"),
            ("GPGSV,3,2,12,05,08,191,,12,11,206,,14,33,052,,15,75,202,,1","61"),
            ("GPGSV,3,3,12,17,24,092,,19,16,120,,22,52,066,,30,04,080,,1","68"),
            ("GLGSV,3,1,09,85,11,021,27,68,39,320,32,78,33,232,,66,28,156,,1","78"),
            ("GLGSV,3,2,09,86,02,068,,77,79,011,,76,31,043,,84,03,330,,1","77"),
            ("GLGSV,3,3,09,67,78,212,,1","4E"),
            ("GBGSV,3,1,09,20,49,293,24,05,10,116,,07,19,047,,10,27,057,,1","74"),
            ("GBGSV,3,2,09,11,06,075,,12,06,025,,19,08,322,,23,63,061,,1","78"),
            ("GBGSV,3,3,09,25,11,060,,1","4E"),
            ("GPGSV,3,1,12,10,11,327,26,13,52,126,17,23,33,296,30,24,49,274,18,1","6C"),
            ("GPGSV,3,2,12,05,08,191,,12,11,206,,14,33,052,,15,75,202,,1","61"),
            ("GPGSV,3,3,12,17,24,092,,19,16,120,,22,52,066,,30,04,080,,1","68"),
            ("GLGSV,3,1,09,85,11,021,25,68,39,320,32,78,33,232,,66,28,156,,1","7A"),
            ("GLGSV,3,2,09,86,02,068,,77,79,011,,76,31,043,,84,03,330,,1","77"),
            ("GLGSV,3,3,09,67,78,212,,1","4E"),
            ("GBGSV,3,1,09,20,49,293,26,05,10,116,,07,19,047,,10,27,057,,1","76"),
            ("GBGSV,3,2,09,11,06,075,,12,06,025,,19,08,322,,23,63,061,,1","78"),
            ("GBGSV,3,3,09,25,11,060,,1","4E"),
            ("GNGSA,A,3,10,13,23,24,,,,,,,,,1.8,1.5,1.0,1","39"),
            ("GNGSA,A,3,68,85,,,,,,,,,,,1.8,1.5,1.0,2","3D"),
            ("GNVTG,,T,,M,0.0,N,0.0,K,A","3D"),
            ("GNRMC,194003.00,A,4852.531614,N,00205.679015,E,0.0,,110824,2.1,W,A,V","6B"),
            ("GNGGA,194003.00,4852.531614,N,00205.679015,E,1,06,1.5,106.1,M,47.7,M,,","79"),
            ("GPGSA,A,1,,,,,,,,,,,,,,,,","32"),
            ("GPVTG,,T,,M,,N,,K,N","2C"),
            ("GPRMC,,V,,,,,,,,,,N,V","29"),
            ("GPGGA,,,,,,0,,,,,,,,","66"),
            ("GPGSV,3,1,12,10,11,327,21,23,33,296,23,05,08,191,,12,11,206,,1","6F"),
            ("GPGSV,3,2,12,13,52,126,,14,33,052,,15,75,202,,17,24,092,,1","69"),
            ("GPGSV,3,3,12,19,16,120,,22,52,066,,24,49,274,,30,04,080,,1","69"),
            ("GLGSV,3,1,09,85,11,021,26,68,39,320,23,78,33,232,,66,28,156,,1","79"),
            ("GLGSV,3,2,09,86,02,068,,77,79,011,,76,31,043,,84,03,330,,1","77"),
            ("GLGSV,3,3,09,67,78,212,,1","4E"),
            ("GBGSV,3,1,09,05,10,116,,07,19,047,,10,27,057,,11,06,075,,1","71"),
            ("GBGSV,3,2,09,12,06,025,,19,08,322,,20,49,293,,23,63,061,,1","7B"),
            ("GBGSV,3,3,09,25,11,060,,1","4E"),
            ("GPGSV,3,1,12,10,11,327,20,23,33,296,25,05,08,191,,12,11,206,,1","68"),
            ("GPGSV,3,2,12,13,52,126,,14,33,052,,15,75,202,,17,24,092,,1","69"),
            ("GPGSV,3,3,12,19,16,120,,22,52,066,,24,49,274,,30,04,080,,1","69"),
            ("GLGSV,3,1,09,85,11,021,29,68,39,320,27,78,33,232,,66,28,156,,1","72"),
            ("GLGSV,3,2,09,86,02,068,,77,79,011,,76,31,043,,84,03,330,,1","77"),
            ("GLGSV,3,3,09,67,78,212,,1","4E"),
            ("GBGSV,3,1,09,20,49,293,28,05,10,116,,07,19,047,,10,27,057,,1","78"),
            ("GBGSV,3,2,09,11,06,075,,12,06,025,,19,08,322,,23,63,061,,1","78"),
            ("GBGSV,3,3,09,25,11,060,,1","4E"),
            ("GPGSA,A,1,,,,,,,,,,,,,,,,","32"),
            ("GPVTG,,T,,M,,N,,K,N","2C"),
            ("GPRMC,,V,,,,,,,,,,N,V","29"),
            ("GPGGA,,,,,,0,,,,,,,,","66"),
            ("GPGSV,3,1,12,10,11,327,19,23,33,296,22,24,49,274,16,05,08,191,,1","6F"),
            ("GPGSV,3,2,12,12,11,206,,13,52,126,,14,33,052,,15,75,202,,1","65"),
            ("GPGSV,3,3,12,17,24,092,,19,16,120,,22,52,066,,30,04,080,,1","68"),
            ("GLGSV,3,1,09,85,11,021,30,68,39,320,26,78,33,232,,66,28,156,,1","7B"),
            ("GLGSV,3,2,09,86,02,068,,77,79,011,,76,31,043,,84,03,330,,1","77"),
            ("GLGSV,3,3,09,67,78,212,,1","4E"),
            ("GBGSV,3,1,09,20,49,293,24,05,10,116,,07,19,047,,10,27,057,,1","74"),
            ("GBGSV,3,2,09,11,06,075,,12,06,025,,19,08,322,,23,63,061,,1","78"),
            ("GBGSV,3,3,09,25,11,060,,1","4E"),
            ("GPGSA,A,1,,,,,,,,,,,,,,,,","32"),
            ("GPVTG,,T,,M,,N,,K,N","2C"),
            ("GPRMC,,V,,,,,,,,,,N,V","29"),
            ("GPGGA,,,,,,0,,,,,,,,","66"))
        for oneCase in input:
            print ("case : >>" + oneCase[0] + "<<")
            x = nmeaUDP.NMEAChecksum(oneCase[0])
            self.assertEqual(x, oneCase[1])


    def test_addNMEACheckSum(self):
        x = nmeaUDP.addNMEACheckSum("ksjhksjhksqjdhfksqjdhfkjsd")
        self.assertEqual(x, "$ksjhksjhksqjdhfksqjdhfkjsd*16".encode(encoding="utf-8"))

        x = nmeaUDP.addNMEACheckSum("GLGSV,3,2,09,86,02,068,,77,79,011,,76,31,043,,84,03,330,,1")
        self.assertEqual(x, "$GLGSV,3,2,09,86,02,068,,77,79,011,,76,31,043,,84,03,330,,1*77".encode(encoding="utf-8"))


    def test_heure2GPSDecimale(self):
        # 10/01/1968 5:30:02 -> ts = -62360875000
        maDateNaissance = -62360875000
        x = datetime.fromtimestamp(0, tz = timezone.utc) + timedelta(milliseconds=maDateNaissance)            
        f = nmeaUDP.heure2GPSDecimale (x)
        s = "{heureNmea:09.2f}".format(heureNmea = f)
        self.assertEqual(s, "053205.00")

        maDateNaissance = -62360875257
        x = datetime.fromtimestamp(0, tz = timezone.utc) + timedelta(milliseconds=maDateNaissance)            
        f = nmeaUDP.heure2GPSDecimale (x)
        s = "{heureNmea:09.2f}".format(heureNmea = f)
        self.assertEqual(s, "053204.74")
    

    def test_angleSexaToDecimal(self):
        f = nmeaUDP.angleSexaToDecimal (degre = 6, minute = 59.9999)
        s = "{angle:011.7f}".format(angle = f)
        self.assertEqual(s, "006.9999983")

        f = nmeaUDP.angleSexaToDecimal (degre = 0, minute = 59.9999)
        s = "{angle:011.7f}".format(angle = f)
        self.assertEqual(s, "000.9999983")

        f = nmeaUDP.angleSexaToDecimal (degre = 0, minute = 0.9999)
        s = "{angle:011.7f}".format(angle = f)
        self.assertEqual(s, "000.0166650")

        f = nmeaUDP.angleSexaToDecimal (degre = 179, minute = 0.9999)
        s = "{angle:011.7f}".format(angle = f)
        self.assertEqual(s, "179.0166650")

        f = nmeaUDP.angleSexaToDecimal (degre = -179, minute = 0.9999)
        s = "{angle:011.7f}".format(angle = f)
        self.assertEqual(s, "-179.0166650")

    def test_angleDecimalToMinuteSexa(self):
        f = nmeaUDP.angleDecimalToMinuteSexa (degreDecimal=006.9999983)
        s = "{angle:011.7f}".format(angle = f)
        self.assertEqual(s, "006.5999990")
        f = nmeaUDP.angleDecimalToMinuteSexa (degreDecimal=-6.9999983)
        s = "{angle:011.7f}".format(angle = f)
        self.assertEqual(s, "-06.5999990")
        
    def test_nav(self):
        iSecondeDepuisDepart = 0
        vitesseEnNoeud = 10.0
        cap = 90.0
        latitudeEstimee = 0
        positionDepartLatitude = 2.0
        positionDepartLongitude = 0.0
        positionArriveeLat, positionArriveeLong = nmeaUDP.nav(iSecondeDepuisDepart, vitesseEnNoeud, cap, latitudeEstimee, positionDepartLatitude, positionDepartLongitude)
        self.assertAlmostEqual(positionArriveeLat, positionDepartLatitude, delta = 0.001)
        self.assertAlmostEqual(positionArriveeLong, positionDepartLongitude, delta = 0.001)

        iSecondeDepuisDepart = 3600
        positionArriveeLat, positionArriveeLong = nmeaUDP.nav(iSecondeDepuisDepart, vitesseEnNoeud, cap, latitudeEstimee, positionDepartLatitude, positionDepartLongitude)
        self.assertAlmostEqual(positionArriveeLat, positionDepartLatitude, delta = 0.001)
        self.assertAlmostEqual(positionArriveeLong, positionDepartLongitude + 0.1666, delta = 0.001)

        iSecondeDepuisDepart = 3600
        cap = 180
        positionArriveeLat, positionArriveeLong = nmeaUDP.nav(iSecondeDepuisDepart, vitesseEnNoeud, cap, latitudeEstimee, positionDepartLatitude, positionDepartLongitude)
        self.assertAlmostEqual(positionArriveeLat, positionDepartLatitude - 0.16666, delta = 0.001)
        self.assertAlmostEqual(positionArriveeLong, positionDepartLongitude, delta = 0.001)
        iSecondeDepuisDepart = 3600

        cap = 45
        positionArriveeLat, positionArriveeLong = nmeaUDP.nav(iSecondeDepuisDepart, vitesseEnNoeud, cap, latitudeEstimee, positionDepartLatitude, positionDepartLongitude)
        self.assertAlmostEqual(positionArriveeLat, positionDepartLatitude + 0.16666 * math.cos(cap * math.pi / 180.0), delta = 0.001)
        self.assertAlmostEqual(positionArriveeLong, positionDepartLongitude + 0.16666 * math.sin(cap * math.pi / 180.0), delta = 0.001)

        cap = 135
        positionArriveeLat, positionArriveeLong = nmeaUDP.nav(iSecondeDepuisDepart, vitesseEnNoeud, cap, latitudeEstimee, positionDepartLatitude, positionDepartLongitude)
        self.assertAlmostEqual(positionArriveeLat, positionDepartLatitude + 0.16666 * math.cos(cap * math.pi / 180.0), delta = 0.001)
        self.assertAlmostEqual(positionArriveeLong, positionDepartLongitude + 0.16666 * math.sin(cap * math.pi / 180.0), delta = 0.001)

        cap = 225
        positionArriveeLat, positionArriveeLong = nmeaUDP.nav(iSecondeDepuisDepart, vitesseEnNoeud, cap, latitudeEstimee, positionDepartLatitude, positionDepartLongitude)
        self.assertAlmostEqual(positionArriveeLat, positionDepartLatitude + 0.16666 * math.cos(cap * math.pi / 180.0), delta = 0.001)
        self.assertAlmostEqual(positionArriveeLong, positionDepartLongitude + 0.16666 * math.sin(cap * math.pi / 180.0), delta = 0.001)

        cap = 315
        positionArriveeLat, positionArriveeLong = nmeaUDP.nav(iSecondeDepuisDepart, vitesseEnNoeud, cap, latitudeEstimee, positionDepartLatitude, positionDepartLongitude)
        self.assertAlmostEqual(positionArriveeLat, positionDepartLatitude + 0.16666 * math.cos(cap * math.pi / 180.0), delta = 0.001)
        self.assertAlmostEqual(positionArriveeLong, positionDepartLongitude + 0.16666 * math.sin(cap * math.pi / 180.0), delta = 0.001)

        cap = 45
        latitudeEstimee = 45
        positionArriveeLat, positionArriveeLong = nmeaUDP.nav(iSecondeDepuisDepart, vitesseEnNoeud, cap, latitudeEstimee, positionDepartLatitude, positionDepartLongitude)
        self.assertAlmostEqual(positionArriveeLat, positionDepartLatitude + 0.16666 * math.cos(cap * math.pi / 180.0), delta = 0.001)
        self.assertAlmostEqual(positionArriveeLong, positionDepartLongitude + 0.16666 * math.sin(cap * math.pi / 180.0) / math.cos(latitudeEstimee * math.pi / 180.0) , delta = 0.001)


    def test_MessageGPS(self):
        # 10/01/1968 5:30:02 -> ts = -62360875000
        maDateNaissance = -62360875000
        date = datetime.fromtimestamp(0, tz = timezone.utc) + timedelta(milliseconds=maDateNaissance)            
        
        latitudeDecimale = 2.5099
        longitudeDecimale = 22.5099
        vitesseEnNoeud = 10.0
        cap = 90.0

        x = nmeaUDP.getGGA(date, latitudeDecimale, longitudeDecimale)
        self.assertEqual (x, "$GPGGA,053205.00,0230.594000,N,02230.594000,E,1,10,1.2,27.0,M,-34.2,M,,*70".encode(encoding="utf-8"))

        x = nmeaUDP.getGSA()
        self.assertEqual (x, "$GPGSA,A,3,07,02,26,27,09,04,15,,,,,,1.8,1.2,1*2A".encode(encoding="utf-8"))

        x = nmeaUDP.getVTG(vitesseEnNoeud, cap)
        self.assertEqual (x, "$GPVTG,090.0,T,092.1,M,010.0,N,018.5,K,A*2D".encode(encoding="utf-8"))

        x = nmeaUDP.getRMC(date, latitudeDecimale, longitudeDecimale, vitesseEnNoeud, cap)
        self.assertEqual (x, "$GPRMC,053205.00,A,0230.594000,N,02230.594000,E,010.00,090.00,100168,002.1,W,A,V*5B".encode(encoding="utf-8"))

        xs = nmeaUDP.getGSV() #
        self.assertEqual (len(xs), 3)
        self.assertEqual (xs[0], "$GPGSV,3,1,09,20,49,293,27,05,10,116,,07,19,047,,10,27,057,,1*77".encode(encoding="utf-8"))
        self.assertEqual (xs[1], "$GPGSV,3,2,09,11,06,075,,12,06,025,,19,08,322,,23,63,061,,1*78".encode(encoding="utf-8"))
        self.assertEqual (xs[2], "$GPGSV,3,3,09,25,11,060,,1*4E".encode(encoding="utf-8"))


        latitudeDecimale = -2.5099
        longitudeDecimale = -22.5099
        x = nmeaUDP.getGGA(date, latitudeDecimale, longitudeDecimale)
        self.assertEqual (x, "$GPGGA,053205.00,0230.594000,N,02230.594000,E,1,10,1.2,27.0,M,-34.2,M,,*70".encode(encoding="utf-8"))


if __name__ == '__main__':
    unittest.main()