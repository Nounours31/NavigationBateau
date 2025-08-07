from libs.fibo import fib3
from pytest import approx

def test_answer():
    assert fib3(3) == approx(3, abs = 0.01)