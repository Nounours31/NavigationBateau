package navLibs

import (
	"testing"
)

type iTestCas1 struct {
	name string
	val  string
	want float64
}

func TestCreateLatitudeFromString(t *testing.T) {
	var casDeTest []iTestCas1 = []iTestCas1{
		{name: "double", val: "-15.23°", want: -15.23},
		{name: "double", val: " - 15.23 ° ", want: -15.23},
		{name: "double", val: "15.23°", want: 15.23},
		{name: "double", val: " 15.23 ° ", want: 15.23},
	}

	for _, tc := range casDeTest {
		t.Run(tc.name, func(t *testing.T) {
			got, _ := FromString(tc.val)
			if got.AsDeg() != tc.want {
				t.Errorf("FromString(%s) = %f; attendu %f", tc.val, got.AsDeg(), tc.want)
			}
		})
	}
}
