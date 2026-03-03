from sfa_navigation import cHeure


class cHeure_tests:
    def test_infra(self):
        h : cHeure = cHeure()
        assert h.uid != ""
        assert h.ts > 0

        h : cHeure = cHeure("toto", 10)
        assert h.uid == "toto"
        assert h.ts == 10

    def test_1(self):
        h : cHeure = cHeure(uid="a33298dd-d13a-4909-bb18-e83d389ad36c", timestampUTC=1478542)
        assert h.heure is not None
        assert h.toString() == ("[cHeure uid: a33298dd-d13a-4909-bb18-e83d389ad36c - timeStamp: 1970-01-18 02:42:22+00:00]")

    def test_2(self):
        h : cHeure = cHeure.fromTimeStamp(timestampUTC=1478542)

        assert h.heure is not None
        assert "timeStamp: 1970-01-18 02:42:22+00:00]" in h.toString()

    def test_3(self):
        h : cHeure = cHeure.fromTimeStamp(timestampUTC=1478542)
        assert h.toHeure2GPSDecimale() == 24222.0

