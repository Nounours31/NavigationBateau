package navLibs

import (
	"testing"
)

type testCasesFromString struct {
	name string
	val  string
	want float64
}

func TestCreateAngleFromString_Float(t *testing.T) {
	var allCasDeTest []testCasesFromString = []testCasesFromString{
		{name: "double 1", val: "15.23", want: 15.23},
		{name: "double 2", val: " 15.23  ", want: 15.23},
		{name: "double 3", val: "-15.23", want: -15.23},
		{name: "double 4", val: " - 15.23  ", want: -15.23},

		{name: "double 10", val: "15.", want: 15.0},
		{name: "double 11", val: " 15.  ", want: 15.0},
		{name: "double 12", val: " 15  ", want: 15.0},
		{name: "double 13", val: "15", want: 15.0},
		{name: "double 14", val: "-15.", want: -15.0},
		{name: "double 15", val: " - 15.  ", want: -15.0},
		{name: "double 16", val: "-15", want: -15.0},
		{name: "double 17", val: " - 15  ", want: -15.0},

		{name: "double 20", val: "15.23°", want: 15.23},
		{name: "double 21", val: " 15.23  °", want: 15.23},
		{name: "double 22", val: "-15.23°", want: -15.23},
		{name: "double 23", val: " - 15.23 ° ", want: -15.23},

		{name: "double 30", val: "15.°", want: 15.0},
		{name: "double 31", val: " 15.  °", want: 15.0},
		{name: "double 32", val: " 15  °", want: 15.0},
		{name: "double 33", val: "15°", want: 15.0},
		{name: "double 34", val: "-15.°", want: -15.0},
		{name: "double 35", val: " - 15. ° ", want: -15.0},
		{name: "double 36", val: "-15°", want: -15.0},
		{name: "double 37", val: " - 15 ° ", want: -15.0},

		{name: "double 40", val: "  400° ", want: 400.0},
		{name: "double 41", val: "  -400° ", want: -400.0},
		{name: "double 42", val: "-400°", want: -400.0},
	}

	for _, unCas := range allCasDeTest {
		t.Run(unCas.name, func(t *testing.T) {
			got, _ := angleFromString(unCas.val)
			if !got.equalsInDeg(unCas.want) {
				t.Errorf("FromString(%s) = %f; attendu %f", unCas.val, got.asDeg(), unCas.want)
			}
		})
	}
}

func TestCreateAngleFromString_DecimalDegree(t *testing.T) {
	var allCasDeTest []testCasesFromString = []testCasesFromString{
		{name: "double dec 1", val: "15°23.23", want: 15.387166666666},
		{name: "double dec 2", val: " 15°23.23 ", want: 15.387166666666},
		{name: "double dec 3", val: " 15 ° 23.23 ", want: 15.387166666666},

		{name: "double dec 11", val: "-15°23.23", want: 344.61283333333 - 360},
		{name: "double dec 12", val: "- 15°23.23 ", want: 344.61283333333 - 360},
		{name: "double dec 13", val: " - 15 ° 23.23 ", want: 344.61283333333 - 360},

		{name: "double dec 21", val: "15°23.23'", want: 15.387166666666},
		{name: "double dec 22", val: " 15°23.23 ' ", want: 15.387166666666},
		{name: "double dec 23", val: " 15 ° 23.23 ' ", want: 15.387166666666},

		{name: "double dec 31", val: "-15°23.23'", want: 344.61283333333 - 360},
		{name: "double dec 32", val: "- 15°23.23 ' ", want: 344.61283333333 - 360},
		{name: "double dec 33", val: " - 15 ° 23.23 ' ", want: 344.61283333333 - 360},

		{name: "double dec 41", val: "15°23'", want: 15.3833333333},
		{name: "double dec 42", val: " 15 ° 23 ' ", want: 15.3833333333},
		{name: "double dec 43", val: "-15°23'", want: 344.6166666666 - 360},
		{name: "double dec 44", val: "- 15°23 ' ", want: 344.6166666666 - 360},
	}

	for _, unCas := range allCasDeTest {
		t.Run(unCas.name, func(t *testing.T) {
			got, _ := angleFromString(unCas.val)
			if !got.equalsInDeg(unCas.want) {
				t.Errorf("FromString(%s) = %f; attendu %f", unCas.val, got.asDeg(), unCas.want)
			}
		})
	}
}

func TestCreateAngleFromString_DecimalDegreMinute(t *testing.T) {
	var allCasDeTest []testCasesFromString = []testCasesFromString{
		{name: "double dec dec 01", val: `15°23'05.05`, want: 15.3847361111111},
		{name: "double dec dec 02", val: `15°23'05.05"`, want: 15.3847361111111},
		{name: "double dec dec 03", val: ` 15 °  23 ' 05.05 `, want: 15.3847361111111},
		{name: "double dec dec 04", val: ` 15 ° 23 ' 05.05" `, want: 15.3847361111111},
		{name: "double dec dec 05", val: ` 15°23' 05.05 " `, want: 15.3847361111111},

		{name: "double dec dec 11", val: `-15°23'05.05`, want: -15.3847361111111},
		{name: "double dec dec 12", val: `-15°23'05.05"`, want: -15.3847361111111},
		{name: "double dec dec 13", val: ` - 15°23'05.05 `, want: -15.3847361111111},
		{name: "double dec dec 14", val: `-  15°23'05.05" `, want: -15.3847361111111},
		{name: "double dec dec 15", val: ` - 15°23'05.05 " `, want: -15.3847361111111},

		{name: "double dec dec 21", val: `-15°23'05`, want: -15.3847222222222},
		{name: "double dec dec 22", val: `-15°23'05"`, want: -15.3847222222222},
		{name: "double dec dec 23", val: ` - 15°23'05 `, want: -15.3847222222222},
		{name: "double dec dec 24", val: `-  15°23'05" `, want: -15.3847222222222},
		{name: "double dec dec 25", val: ` - 15°23'05 " `, want: -15.3847222222222},

		{name: "double dec dec 31", val: `15°23'05`, want: 15.3847222222222},
		{name: "double dec dec 32", val: `15°23'05"`, want: 15.3847222222222},
		{name: "double dec dec 33", val: `  15°23'05 `, want: 15.3847222222222},
		{name: "double dec dec 34", val: `  15°23'05" `, want: 15.3847222222222},
		{name: "double dec dec 35", val: `  15°23'05 " `, want: 15.3847222222222},
	}

	for _, unCas := range allCasDeTest {
		t.Run(unCas.name, func(t *testing.T) {
			got, _ := angleFromString(unCas.val)
			if !got.equalsInDeg(unCas.want) {
				t.Errorf("FromString(%s) = %f; attendu %f", unCas.val, got.asDeg(), unCas.want)
			}
		})
	}
}
