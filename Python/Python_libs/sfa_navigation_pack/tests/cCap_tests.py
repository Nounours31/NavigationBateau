import pytest
from sfa_navigation import cAngle, eAngleFormat, cCap


class cCap_tests:
    def test_init_normalisation(self):
        assert cCap(0).valAsDeg == 0
        assert cCap(360).valAsDeg == 0
        assert cCap(370).valAsDeg == 10
        assert cCap(-10).valAsDeg == 350
    
    
    # ---------- ADDITION ----------
    
    def test_add(self):
        c = cCap(350)
        r = c + 20
        assert isinstance(r, cCap)
        assert r.valAsDeg == 10
    
    
    def test_iadd(self):
        c = cCap(350)
        c += 20
        assert c.valAsDeg == 10
    
    
    # ---------- MULTIPLICATION ----------
    
    def test_mul(self):
        c = cCap(45)
        r = c * 3
        assert isinstance(r, cCap)
        assert r.valAsDeg == 135
    
    
    def test_mul_overflow(self):
        c = cCap(45)
        r = c * 10
        assert r.valAsDeg == 90
    
    
    def test_imul(self):
        c = cCap(100)
        c *= 4
        assert c.valAsDeg == 40
    
    
    # ---------- SOUSTRACTION ----------
    
    def test_sub(self):
        c = cCap(10)
        r = c - 30
        assert r.valAsDeg == 340
    
    
    def test_isub(self):
        c = cCap(10)
        c -= 30
        assert c.valAsDeg == 340
    
    
    # ---------- DIVISION ----------
    
    def test_div(self):
        c = cCap(180)
        r = c / 2
        assert r.valAsDeg == 90
    
    
    def test_idiv(self):
        c = cCap(180)
        c /= 2
        assert c.valAsDeg == 90
    
    
    # ---------- CHAINE ----------
    
    def test_str(self):
        c = cCap(370)
        s = str(c)
        assert "10.0000" in s
    
    
    # ---------- COMPATIBILITÉ cAngle ----------
    
    def test_angle_compatibility(self):
        c = cCap(370)
        a = cAngle(10)
    
        assert c == a
        assert a == c
    
    
    # ---------- STABILITÉ ----------
    
    def test_many_operations(self):
        c = cCap(0)
        for _ in range(1000):
            c += 123.456
        assert 0 <= c.valAsDeg < 360
