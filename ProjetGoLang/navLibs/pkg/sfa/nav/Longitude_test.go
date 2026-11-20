package navLibs

import (
	"testing"
)

func TestCreateLongitudeFromString_Float(t *testing.T) {
	var allCasDeTest []testCasesFromString = []testCasesFromString{
		{name: "double 01", val: "E 15.23", want: 15.23},
		{name: "double 02", val: "E15.23", want: 15.23},
		{name: "double 03", val: "15.23E", want: 15.23},
		{name: "double 04", val: "15.23 E", want: 15.23},
		{name: "double 05", val: " 15.23  ", want: 15.23},

		{name: "double 01", val: "W 15.23", want: -15.23},
		{name: "double 02", val: "W15.23", want: -15.23},
		{name: "double 03", val: "15.23W", want: -15.23},
		{name: "double 04", val: "15.23 W", want: -15.23},
		{name: "double 05", val: " -15.23  ", want: -15.23},

		{name: "double 20", val: "E 15.23°", want: 15.23},
		{name: "double 20", val: "15.23°E", want: 15.23},
		{name: "double 20", val: "15.23° E", want: 15.23},
		{name: "double 20", val: "W 15.23°", want: -15.23},
		{name: "double 20", val: "15.23°W", want: -15.23},
		{name: "double 20", val: "15.23° W", want: -15.23},
	}

	for _, unCas := range allCasDeTest {
		t.Run(unCas.name, func(t *testing.T) {
			got, _ := LongitudeFromString(unCas.val)
			if !got.equalsInDeg(unCas.want) {
				t.Errorf("FromString(%s) = %f; attendu %f", unCas.val, got.AsDeg(), unCas.want)
			}
		})
	}
}

func TestCreateLongitudeFromString_DecimalDegree(t *testing.T) {
	var allCasDeTest []testCasesFromString = []testCasesFromString{
		{name: "double dec 1", val: "E 15°23.23", want: 15.387166666666},
		{name: "double dec 1", val: "E 15°23.23'", want: 15.387166666666},
		{name: "double dec 1", val: "15°23.23E", want: 15.387166666666},
		{name: "double dec 1", val: "15°23.23'E", want: 15.387166666666},
		{name: "double dec 11", val: "W 15°23.23", want: -15.3871666666663},
		{name: "double dec 11", val: "W 15°23.23'", want: -15.387166666666},
		{name: "double dec 11", val: "15°23.23W", want: -15.387166666666},
		{name: "double dec 11", val: "15°23.23'W", want: -15.387166666666},
	}

	for _, unCas := range allCasDeTest {
		t.Run(unCas.name, func(t *testing.T) {
			got, _ := LongitudeFromString(unCas.val)
			if !got.equalsInDeg(unCas.want) {
				t.Errorf("FromString(%s) = %f; attendu %f", unCas.val, got.AsDeg(), unCas.want)
			}
		})
	}
}

func TestCreateLongitudeFromString_DecimalDegreMinute(t *testing.T) {
	var allCasDeTest []testCasesFromString = []testCasesFromString{
		{name: "double dec dec 01", val: `E 15°23'05.05`, want: 15.3847361111111},
		{name: "double dec dec 02", val: `E 15°23'05.05"`, want: 15.3847361111111},
		{name: "double dec dec 01", val: `15°23'05.05E`, want: 15.3847361111111},
		{name: "double dec dec 02", val: `15°23'05.05"E`, want: 15.3847361111111},

		{name: "double dec dec 01", val: `W 15°23'05.05`, want: -15.3847361111111},
		{name: "double dec dec 02", val: `W 15°23'05.05"`, want: -15.3847361111111},
		{name: "double dec dec 01", val: `15°23'05.05W`, want: -15.3847361111111},
		{name: "double dec dec 02", val: `15°23'05.05"W`, want: -15.3847361111111},
	}

	for _, unCas := range allCasDeTest {
		t.Run(unCas.name, func(t *testing.T) {
			got, _ := LongitudeFromString(unCas.val)
			if !got.equalsInDeg(unCas.want) {
				t.Errorf("FromString(%s) = %f; attendu %f", unCas.val, got.AsDeg(), unCas.want)
			}
		})
	}
}
