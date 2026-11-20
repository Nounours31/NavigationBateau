package navLibs

import (
	"testing"
)

func TestCreateLatitudeFromString_Float(t *testing.T) {
	var allCasDeTest []testCasesFromString = []testCasesFromString{
		{name: "double 01", val: "N 15.23", want: 15.23},
		{name: "double 02", val: "N15.23", want: 15.23},
		{name: "double 03", val: "15.23N", want: 15.23},
		{name: "double 04", val: "15.23 N", want: 15.23},
		{name: "double 05", val: " 15.23  ", want: 15.23},

		{name: "double 01", val: "S 15.23", want: -15.23},
		{name: "double 02", val: "S15.23", want: -15.23},
		{name: "double 03", val: "15.23S", want: -15.23},
		{name: "double 04", val: "15.23 S", want: -15.23},
		{name: "double 05", val: " -15.23  ", want: -15.23},

		{name: "double 20", val: "N 15.23°", want: 15.23},
		{name: "double 20", val: "15.23°N", want: 15.23},
		{name: "double 20", val: "15.23° N", want: 15.23},
		{name: "double 20", val: "S 15.23°", want: -15.23},
		{name: "double 20", val: "15.23°S", want: -15.23},
		{name: "double 20", val: "15.23° S", want: -15.23},
	}

	for _, unCas := range allCasDeTest {
		t.Run(unCas.name, func(t *testing.T) {
			got, _ := LatitudeFromString(unCas.val)
			if !got.equalsInDeg(unCas.want) {
				t.Errorf("FromString(%s) = %f; attendu %f", unCas.val, got.AsDeg(), unCas.want)
			}
		})
	}
}

func TestCreateLatitudeFromString_DecimalDegree(t *testing.T) {
	var allCasDeTest []testCasesFromString = []testCasesFromString{
		{name: "double dec 1", val: "N 15°23.23", want: 15.387166666666},
		{name: "double dec 1", val: "N 15°23.23'", want: 15.387166666666},
		{name: "double dec 1", val: "15°23.23N", want: 15.387166666666},
		{name: "double dec 1", val: "15°23.23'N", want: 15.387166666666},
		{name: "double dec 11", val: "S 15°23.23", want: -15.3871666666663},
		{name: "double dec 11", val: "S 15°23.23'", want: -15.387166666666},
		{name: "double dec 11", val: "15°23.23S", want: -15.387166666666},
		{name: "double dec 11", val: "15°23.23'S", want: -15.387166666666},
	}

	for _, unCas := range allCasDeTest {
		t.Run(unCas.name, func(t *testing.T) {
			got, _ := LatitudeFromString(unCas.val)
			if !got.equalsInDeg(unCas.want) {
				t.Errorf("FromString(%s) = %f; attendu %f", unCas.val, got.AsDeg(), unCas.want)
			}
		})
	}
}

func TestCreateLatitudeFromString_DecimalDegreMinute(t *testing.T) {
	var allCasDeTest []testCasesFromString = []testCasesFromString{
		{name: "double dec dec 01", val: `N 15°23'05.05`, want: 15.3847361111111},
		{name: "double dec dec 02", val: `N 15°23'05.05"`, want: 15.3847361111111},
		{name: "double dec dec 01", val: `15°23'05.05N`, want: 15.3847361111111},
		{name: "double dec dec 02", val: `15°23'05.05"N`, want: 15.3847361111111},

		{name: "double dec dec 01", val: `S 15°23'05.05`, want: -15.3847361111111},
		{name: "double dec dec 02", val: `S 15°23'05.05"`, want: -15.3847361111111},
		{name: "double dec dec 01", val: `15°23'05.05S`, want: -15.3847361111111},
		{name: "double dec dec 02", val: `15°23'05.05"S`, want: -15.3847361111111},
	}

	for _, unCas := range allCasDeTest {
		t.Run(unCas.name, func(t *testing.T) {
			got, _ := LatitudeFromString(unCas.val)
			if !got.equalsInDeg(unCas.want) {
				t.Errorf("FromString(%s) = %f; attendu %f", unCas.val, got.AsDeg(), unCas.want)
			}
		})
	}
}
