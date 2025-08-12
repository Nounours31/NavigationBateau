from datetime import datetime, timezone
import logging
import logging.config
import math
from skyfield import api
from skyfield.api import Angle, Star, load, wgs84, N, E
from skyfield.units import Angle as SkyAngle
from skyfield.data import hipparcos

import numpy as np

import os.path


from cAngle import cAngle
from cPosition import cPosition
from cLatitude import cLatitude
from cLongitude import cLongitude

logging.config.fileConfig("logging.conf")
logger = logging.getLogger("cEphemerides")


class cEphemerides:
    """gestion des heures"""

    rayon_terre_en_km = 6378.137

    __BORD_SUP: float = -0.5
    __BORD_INF: float = 0.5
    __BORD_MILIEU: float = 0

    planete_db = [
        {"nom": "moon", "nom_sky_fiel": "moon", "mean_rad_in_km": 1737.4},
        {"nom": "sun", "nom_sky_fiel": "sun", "mean_rad_in_km": 695700.0},
        {"nom": "venus", "nom_sky_fiel": "venus", "mean_rad_in_km": 6051.8},
        {"nom": "jupiter", "nom_sky_fiel": "jupiter barycenter", "mean_rad_in_km": 69911.0},
        {"nom": "saturn", "nom_sky_fiel": "saturn barycenter", "mean_rad_in_km": 58232.0},
        {"nom": "mars", "nom_sky_fiel": "mars barycenter", "mean_rad_in_km": 3389.5},
    ]
    stars_db = """
    Alpheratz,677
    Ankaa,2081
    Schedar,3179
    Diphda,3419
    Achernar,7588
    Hamal,9884
    Polaris,11767
    Acamar,13847
    Menkar,14135
    Mirfak,15863
    Aldebaran,21421
    Rigel,24436
    Capella,24608
    Bellatrix,25336
    Elnath,25428
    Alnilam,26311
    Betelgeuse,27989
    Canopus,30438
    Sirius,32349
    Adhara,33579
    Procyon,37279
    Pollux,37826
    Avior,41037
    Suhail,44816
    Miaplacidus,45238
    Alphard,46390
    Regulus,49669
    Dubhe,54061
    Denebola,57632
    Gienah,59803
    Acrux,60718
    Gacrux,61084
    Alioth,62956
    Spica,65474
    Alkaid,67301
    Hadar,68702
    Menkent,68933
    Arcturus,69673
    Rigil Kent.,71683
    Kochab,72607
    Zuben'ubi,72622
    Alphecca,76267
    Antares,80763
    Atria,82273
    Sabik,84012
    Shaula,85927
    Rasalhague,86032
    Eltanin,87833
    Kaus Aust.,90185
    Vega,91262
    Nunki,92855
    Altair,97649
    Peacock,100751
    Deneb,102098
    Enif,107315
    Al Na'ir,109268
    Fomalhaut,113368
    Scheat,113881
    Markab,113963
    """

    @staticmethod
    def getPlaneteStarNom() -> list[str]:
        retour: list[str] = []
        retour.append(cEphemerides.getPlaneteNom())
        retour.append(cEphemerides.getStarNom())

        return retour

    @staticmethod
    def getPlaneteNom() -> list[str]:
        retour: list[str] = []
        for planete in cEphemerides.planete_db:
            planete_humannom = planete["nom"]
            retour.append(planete_humannom)

        return retour

    @staticmethod
    def getStarNom() -> list[str]:
        retour: list[str] = []
        for line in cEphemerides.stars_db.strip().split("\n"):
            x1 = line.index(",")
            name = line[0:x1]
            while name.startswith(" "):
                name = name[1:]
            retour.append(name)

        return retour

    def __init__(self):
        self.__secondeDecimale: float = 0.0

    def __str__(self) -> str:
        raise NotImplemented

    @staticmethod
    def getViseeInfo() :
        viseeInfo = [
            {"nom": "sup [Bord Sup]", "val": -0.5},
            {"nom": "milieu", "val": 0.0},
            {"nom": "inf [Bord Inf]", "val": +0.5}
        ]
        return viseeInfo
    
    @staticmethod
    def getViseeDefaut() :
            return (cEphemerides.getViseeInfo())[2]["nom"]

    def isPlanete (self, astre: str) -> bool:
        for p in self.getPlaneteNom() :
            if p == astre:
                return True
        return False 

    def __getEpherideEtoile(self, tMeusure : datetime , astre: str, dr: cPosition) -> str:
        ts = load.timescale()
        ut1 = ts.ut1(tMeusure.year, tMeusure.month, tMeusure.day, tMeusure.hour, tMeusure.minute, tMeusure.second)    
        maposition = wgs84.latlon(dr._lat.asDeg(), dr._long.asDeg())

        # recup de l'astre
        eph = load("de421.bsp")
        earth = eph["earth"]

        # load the Hipparcos catalog as a 118,218 row Pandas dataframe.
        with load.open(hipparcos.URL) as f:
            df = hipparcos.load_dataframe(f)


        bFind = False
        HIPnum = ""
        for line in cEphemerides.stars_db.strip().split("\n"):
            x1 = line.index(",")
            name = line[0:x1]
            while name.startswith(" "):
                name = name[1:]
            HIPnum = line[x1 + 1 :]


            print (f"{name}")
            if name == astre:
                bFind =  True
                break
        print (f"{bFind} - {line}")
        if not bFind:
            return None
        
        star = Star.from_dataframe(df.loc[int(HIPnum)])
        
        # les calculs
        observateur = earth.at(ut1).observe(star)
        ra, dec, dis = observateur.apparent().radec(epoch="date")
        gha = cEphemerides.fmtgha(ut1.gast, ra.hours, False)

        GHAAries = self.ariesGHA(tMeusure)

        lha = gha - cEphemerides.fromDegreLongitudeToAngleHoraire(dr.getLongitude().asDeg())
        while lha < 0:
            lha += 360.0
        while lha > 360.0:
            lha -= 360.0

        sha = gha - GHAAries
        while sha < 0:
            sha += 360.0
        while sha > 360.0:
            sha -= 360.0

        # distanceTerreAstreenKm = (earth - star).at(ut1).distance().km
        distanceTerreAstreenKm = dis.km
        semiDiametre = ((math.atan(0.0 / distanceTerreAstreenKm)) * 180.0 / math.pi)  
        
        utc = ts.from_datetime(tMeusure)
        planete_pos = (earth + maposition).at(utc).observe(star).apparent()
        alt, az, _ = planete_pos.altaz()

        HP_rad = math.asin(cEphemerides.rayon_terre_en_km / (distanceTerreAstreenKm ))
        HP = (HP_rad) * 180.0 / math.pi

        parallaxe = (math.asin(math.sin(HP_rad) * math.cos(alt.radians))) * 180.0 / math.pi

        
        print( "*******************************************************************")
        print(f"   DR:  Lat   {dr.getLatitude().toString()}")
        print(f"        Long  {dr.getLongitude().toString()}")
        print(f"   Heure:     {tMeusure.strftime("%Y/%m/%d %H:%M:%S [%Z]")}")
        print(f"   Astre:     {astre}")
        print( "--------------------------------------------------")
        print(f"   Az:        {cAngle.fromDeg(float(az.degrees)).toString()}")
        print(f"   Hc:        {cAngle.fromDeg(float(alt.degrees)).toString()}")
        print(f"   Hc + HP:   {cAngle.fromDeg(float((alt.degrees + HP))).toString()}")
        print(f"   Hc + P:    {cAngle.fromDeg(float((alt.degrees + parallaxe))).toString()}")
        print(f"   GHA Aries: {cAngle.fromDeg(float(GHAAries)).toString()}")
        print( "--------------------------------------------------")
        print(f"   GHA:       {cAngle.fromDeg(float(gha)).toString()}")
        print(f"   SHA:       {cAngle.fromDeg(float(sha)).toString()}")
        print(f"   LHA:       {cAngle.fromDeg(float(lha)).toString()}")
        print(f"   Dec:       {cAngle.fromDeg(float(dec.degrees)).toString()}")
        print(f"   SD:        {cAngle.fromDeg(float(semiDiametre)).toString()}")
        print( "--------------------------------------------------")
        print(f"   HP:        {cAngle.fromDeg(float(HP)).toString()}")
        print(f"   P:         {cAngle.fromDeg(float(parallaxe)).toString()}")
        print( "*******************************************************************")

        return {
            "gha_aries": GHAAries,
            "gha": gha,
            "lha": lha,
            "dec": dec.degrees,
            "sd": semiDiametre,
            "az": az.degrees,
            "hp": HP,
            "hauteurObservee": alt.degrees,
            "hauteurObserveeCorrigeeParallaxe": (alt.degrees + HP)
        }

    def __getEpheridePlanete(self, tMeusure : datetime , astre: str, dr: cPosition) -> str:
        ts = load.timescale()
        ut1 = ts.ut1(tMeusure.year, tMeusure.month, tMeusure.day, tMeusure.hour, tMeusure.minute, tMeusure.second)    
        maposition = wgs84.latlon(dr._lat.asDeg(), dr._long.asDeg())

        # recup de l'astre
        eph = load("de421.bsp")
        planete_eph = eph[astre]
        earth = eph["earth"]
        
        planete_rayon = 0.0
        for planete in cEphemerides.planete_db:
            if planete["nom"] == astre:
                planete_rayon = planete["mean_rad_in_km"]

        # les calculs
        observateur = earth.at(ut1).observe(planete_eph)
        ra, dec, _ = observateur.apparent().radec(epoch="date")
        gha = cEphemerides.fmtgha(ut1.gast, ra.hours, False)

        GHAAries = self.ariesGHA(tMeusure)

        lha = gha - cEphemerides.fromDegreLongitudeToAngleHoraire(dr.getLongitude().asDeg())
        while lha < 0:
            lha += 360.0
        while lha > 360.0:
            lha -= 360.0

        sha = gha - GHAAries
        while sha < 0:
            sha += 360.0
        while sha > 360.0:
            sha -= 360.0

        distanceTerreAstreenKm = (earth - planete_eph).at(ut1).distance().km
        semiDiametre = ((math.atan(planete_rayon / distanceTerreAstreenKm)) * 180.0 / math.pi)  
        
        utc = ts.from_datetime(tMeusure)
        planete_pos = (earth + maposition).at(utc).observe(planete_eph).apparent()
        alt, az, _ = planete_pos.altaz()

        HP_rad = math.asin(cEphemerides.rayon_terre_en_km / (distanceTerreAstreenKm ))
        HP = (HP_rad) * 180.0 / math.pi

        parallaxe = (math.asin(math.sin(HP_rad) * math.cos(alt.radians))) * 180.0 / math.pi

        
        print( "*******************************************************************")
        print(f"   DR:  Lat   {dr.getLatitude().toString()}")
        print(f"        Long  {dr.getLongitude().toString()}")
        print(f"   Heure:     {tMeusure.strftime("%Y/%m/%d %H:%M:%S [%Z]")}")
        print(f"   Astre:     {astre}")
        print( "--------------------------------------------------")
        print(f"   Az:        {cAngle.fromDeg(float(az.degrees)).toString()}")
        print(f"   Hc:        {cAngle.fromDeg(float(alt.degrees)).toString()}")
        print(f"   Hc + HP:   {cAngle.fromDeg(float((alt.degrees + HP))).toString()}")
        print(f"   Hc + P:    {cAngle.fromDeg(float((alt.degrees + parallaxe))).toString()}")
        print(f"   GHA Aries: {cAngle.fromDeg(float(GHAAries)).toString()}")
        print( "--------------------------------------------------")
        print(f"   GHA:       {cAngle.fromDeg(float(gha)).toString()}")
        print(f"   SHA:       {cAngle.fromDeg(float(sha)).toString()}")
        print(f"   LHA:       {cAngle.fromDeg(float(lha)).toString()}")
        print(f"   Dec:       {cAngle.fromDeg(float(dec.degrees)).toString()}")
        print(f"   SD:        {cAngle.fromDeg(float(semiDiametre)).toString()}")
        print( "--------------------------------------------------")
        print(f"   HP:        {cAngle.fromDeg(float(HP)).toString()}")
        print(f"   P:         {cAngle.fromDeg(float(parallaxe)).toString()}")
        print( "*******************************************************************")

        return {
            "gha_aries": GHAAries,
            "gha": gha,
            "lha": lha,
            "dec": dec.degrees,
            "sd": semiDiametre,
            "az": az.degrees,
            "hp": HP,
            "hauteurObservee": alt.degrees,
            "hauteurObserveeCorrigeeParallaxe": (alt.degrees + HP)
        }

    
    def getEpherideAstre(self, t : datetime, astre: str, dr: cPosition) :
        if self.isPlanete (astre):
            return self.__getEpheridePlanete (t, astre, dr)
        else :
            return self.__getEpherideEtoile (t, astre, dr)

    @staticmethod
    def fromDegreLongitudeToAngleHoraire (longitudeEnDegre : float):
        angleHoraire : float = 0.0
        if longitudeEnDegre > 0.0:
            angleHoraire = 360.0 - longitudeEnDegre
        if longitudeEnDegre < 0.0:
            angleHoraire = 180.0 - longitudeEnDegre
        
        while angleHoraire < 0:
            angleHoraire += 360.0

        while angleHoraire > 360.0:
            angleHoraire -= 360.0
        
        return angleHoraire
            

    def get_val(self) -> str:
        ts = load.timescale()
        time = ts.utc(2025, 8, 7, 20, 47, 59.99999)

        eph = load("de421.bsp")
        earth, sun = eph["earth"], eph["sun"]
        radius_km = 24764.0

        astrometric = earth.at(time).observe(sun)
        ra, dec, distance = astrometric.apparent().radec()
        apparent_diameter = Angle(radians=np.arcsin(radius_km / distance.km) * 2.0)
        print("{:.6f} arcseconds".format(apparent_diameter.arcseconds()))

        geographic = api.wgs84.latlon(latitude_degrees=0, longitude_degrees=0)
        observer = geographic.at(time)
        pos = observer.from_altaz(alt_degrees=90, az_degrees=0)

        ra, dec, distance = pos.radec()
        print(ra)
        print(dec)

    def test(self) -> str:
        maintenant: datetime = datetime.now(timezone.utc)
        print(f"maintenant {maintenant} \n")

        # Load the JPL ephemeris DE421 (covers 1900-2050).
        eph = load("de421.bsp")
        cEphemerides.planete_info(maintenant, eph)
        # earth = eph["earth"]
        # cEphemerides.stellar_info(maintenant, earth)

    @staticmethod
    def planete_info(d: datetime, eph: any):  # used in starstab
        ts = load.timescale()
        t = ts.ut1(d.year, d.month, d.day, d.hour, d.minute, d.second)

        earth = eph["earth"]
        maposition = wgs84.latlon(0 * N, 100 * E)

        for planete in cEphemerides.planete_db:
            planete_nom = planete["nom_sky_fiel"]
            planete_humannom = planete["nom"]
            planete_rayon = planete["mean_rad_in_km"]

            p = eph[planete_nom]
            position = earth.at(t).observe(p)

            ra, dec, distance = position.apparent().radec(epoch="date")

            gha = cEphemerides.fmtgha(t.gast, ra.hours)
            dec = cEphemerides.fmtdeg(dec.degrees)

            dist_km = distance.km
            semiDiametre = (
                (math.atan(planete_rayon / dist_km)) * 180.0 / math.pi
            )  # volumetric mean radius of sun = 695700 km
            print(
                f"{planete_humannom:s} - GHA: {gha} - Dec: {dec} - SD: {cAngle.toStringDebug(semiDiametre)}\n"
            )

            utc = ts.from_datetime(d)
            sun_pos = (earth + maposition).at(utc).observe(p).apparent()
            alt, az, distance = sun_pos.altaz()

            HP_rad = math.asin(cEphemerides.rayon_terre_en_km / distance.km)
            HP = (math.asin(math.sin(HP_rad) * math.cos(alt.radians))) * 180.0 / math.pi

            print(
                f"{planete_humannom:s} - azimut: {cEphemerides.fmtdeg(az.degrees)} - Dec: {cEphemerides.fmtdeg(alt.degrees)} - HP: {cEphemerides.fmtdeg(HP)} - DEC+HP: {cEphemerides.fmtdeg(alt.degrees + HP)}\n"
            )

        return

    @staticmethod
    def stellar_info(d: datetime, earth: any):  # used in starstab
        ts = load.timescale()
        t = ts.ut1(d.year, d.month, d.day, d.hour, d.minute, d.second)

        # load the Hipparcos catalog as a 118,218 row Pandas dataframe.
        path = "hipparcosCatalog.bin"
        if not os.path.isfile(path):
            with load.open(hipparcos.URL) as f:
                with open(path, "wb") as file:
                    file.write(f.read())
                file.close()

        with open(path) as f:
            df = hipparcos.load_dataframe(f)

        print(f"ARIES - GHA: {cEphemerides.ariesGHA(d)} \n")

        out = []

        for line in cEphemerides.stars_db.strip().split("\n"):
            x1 = line.index(",")
            name = line[0:x1]
            HIPnum = line[x1 + 1 :]

            star = Star.from_dataframe(df.loc[int(HIPnum)])
            astrometric = earth.at(t).observe(star).apparent()
            ra, dec, distance = astrometric.radec(epoch="date")

            sha = cEphemerides.fmtgha(0, ra.hours)
            decl = cEphemerides.fmtdeg(dec.degrees)

            print(f"{name:s} - SHA: {sha} - Dec: {decl} \n")
            # out.append([name,sha,decl])
        return out

    @staticmethod
    def ariesGHA(d: datetime) :
        ts = load.timescale()
        t = ts.ut1(d.year, d.month, d.day, d.hour, d.minute, d.second)

        gha = cEphemerides.fmtgha(t.gast, 0, False)
        return gha

    @staticmethod
    def fmtgha(gst, ra, bToString : bool = True):
        # formats angle (hours) to that used in the nautical almanac. (ddd°mm.m)
        sha = (gst - ra) * 15
        if sha < 0:
            sha += 360
        if bToString:
            return cEphemerides.fmtdeg(sha)
        return sha 

    @staticmethod
    def fmtdeg(deg):
        a : cAngle = cAngle.fromDeg(deg)
        return a.toString(0)

    @staticmethod
    def printSkyAngleAsGHA(ha: SkyAngle) -> str:
        x: float = ha.degrees
        while x < 0.0:
            x += 360
        while x > 360.0:
            x -= 360.0

        return cEphemerides.fmtdeg(x)

    @staticmethod
    def printSkyAngleAsLat(ha: SkyAngle) -> str:
        x: float = ha.degrees
        return cEphemerides.fmtdeg(x)


if __name__ == "__main__":
    # Execute when the module is not initialized from an import statement.
    x: cEphemerides = cEphemerides()
    x.test()

