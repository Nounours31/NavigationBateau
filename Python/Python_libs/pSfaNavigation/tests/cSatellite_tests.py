from pSfaNavigation.mSatellite import cSatellite


class cSatellite_tests:
    def test_infra(self):
        h : cSatellite = cSatellite()

    def test_1(self):
        h : cSatellite = cSatellite.fromDict({"a":"a"})

    def test_2(self):
        h : cSatellite = cSatellite.fromString("a")

    def test_3(self):
        h : cSatellite = cSatellite.fromObject(cSatellite())

