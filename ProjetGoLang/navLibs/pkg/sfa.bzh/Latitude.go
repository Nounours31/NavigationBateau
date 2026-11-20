package navLibs

import (
	"fmt"
	"math"
	"regexp"
	"strconv"
)

// Contrat d'entrée : ce que VOTRE package exige pour fonctionner
type ILatitude interface {
	GetVal() (float64, error)
}

// Type concret exporté (contrat de sortie)
type sens string

func (this *sens) value() float64 {
	if *this == N {
		return +1.0
	}
	if *this == S {
		return -1.0
	}
	if *this == E {
		return +1.0
	}
	if *this == W {
		return -1.0
	}
	return 0.0
}

const (
	// These are exported values so they can use these sens to construct a latitude with.
	N sens = "N"
	S sens = "S"
	E sens = "E"
	W sens = "W"
)

type sensLat struct { // unexported type users can't construct their own.
	sens
}

type sensLong struct { // unexported type users can't construct their own.
	sens
}

type Latitude struct {
	deg float64
	sensLat
}

func (this *Latitude) AsDeg() float64 {
	var retour float64 = 0.0
	retour = this.sensLat.value() * this.deg
	return retour
}

func New(valAsDeg float64, sens sensLat) *Latitude {
	var retour Latitude = Latitude{
		deg:     valAsDeg,
		sensLat: sens,
	}
	return &retour
}

func FromString(val string) (*Latitude, error) {
	// cas 1
	//   +/- 1.23 °
	r, _ := regexp.Compile("^\\s*(.*)\\s*°?\\s*$")
	if r.MatchString(val) {
		matches := r.FindAllStringSubmatchIndex(val, -1)

		for _, match := range matches {
			// Process this match here. Each pair in match contains start/end byte offsets.
			for group := 1; group < len(match)/2; group++ {
				value := "<unmatched>"

				if match[2*group] >= 0 {
					value = val[match[2*group]:match[2*group+1]]
				}

				valasDeg, _ := strconv.ParseFloat(value, 64)
				var sens sensLat = sensLat{sens: N}
				if valasDeg < 0 {
					valasDeg *= -1.0
					sens = sensLat{sens: S}
				}
				return New(math.Abs(valasDeg), sens), nil
			}
		}
	}
	if r.MatchString(val) {
		matches := r.FindAllStringSubmatchIndex(val, -1)

		for index, match := range matches {
			// Process this match here. Each pair in match contains start/end byte offsets.
			fmt.Printf("Match %d: %s\n", index+1, val[match[0]:match[1]])
			fmt.Printf("  Range: %d-%d (bytes)\n", match[0], match[1])

			for group := 1; group < len(match)/2; group++ {
				value := "<unmatched>"

				if match[2*group] >= 0 {
					value = val[match[2*group]:match[2*group+1]]
				}

				fmt.Printf("  Group %d: %s\n", group, value)

				if name := r.SubexpNames()[group]; name != "" {
					fmt.Printf("  Group %s: %s\n", name, value)
				}
			}
		}
	}

	return nil, nil
}
