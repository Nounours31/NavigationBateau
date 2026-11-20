package navLibs

// ------------------------------------------
// Package prive de gestion des angles
// ------------------------------------------
import (
	"fmt"
	"log/slog"
	"math"
	"regexp"
	"strconv"
	"strings"
)

const (
	DEG2SEC       = 3600.0
	MIN2SEC       = 60.0
	DEG2RAD       = math.Pi / 180.0
	ANGLE_EPSILON = 1e-3
)

type angle struct {
	_asSecondes float64
}

func (this *angle) asDeg() float64 {
	return this._asSecondes / DEG2SEC
}

func (this *angle) asSec() float64 {
	return this._asSecondes
}

func (this *angle) deg(deg float64) angle {
	this._asSecondes = deg * 3600.0
	return *this
}

func (this *angle) asRad() float64 {
	return this.asDeg() * DEG2RAD
}

func (this *angle) normalize() angle {

	for this._asSecondes < 0 {
		this._asSecondes += (360.0 * DEG2SEC)
	}
	for this._asSecondes > (360.0 * DEG2SEC) {
		this._asSecondes -= (360.0 * DEG2SEC)
	}
	return *this
}

func (this *angle) equals(o *angle) bool {
	if math.Abs(this._asSecondes-o._asSecondes) < ANGLE_EPSILON {
		return true
	}
	return false
}
func (this *angle) equalsInDeg(o float64) bool {
	if math.Abs(this._asSecondes-o*DEG2SEC) < ANGLE_EPSILON {
		return true
	}
	slog.Info(fmt.Sprintf("Delta: %f - this: %f VS o: %f", math.Abs(this._asSecondes-o*DEG2SEC), this._asSecondes, o*DEG2SEC))
	return false
}

func (this angle) String() string {
	return fmt.Sprintf("%f", this.asDeg())
}

func newAngle(valAsSec float64) angle {
	var a angle = angle{
		_asSecondes: valAsSec,
	}
	return a
}

func angleFromString(val string) (angle, error) {

	var a angle
	var err error
	// -------------------------------------------------------------------------------
	// cas 1
	//                            +/-     1.23        e        -        10.23       °
	// -------------------------------------------------------------------------------
	regExpFloat, _ := regexp.Compile(`^\s*([+-]?\s*[0-9.,]+)\s*°?\s*$`)
	regExpDegDecimal, _ := regexp.Compile(`^\s*([+-])?\s*([0-9]{1,4})\s*°\s*([0-9]{1,2}[.,0-9]*)\s*'?\s*$`)
	regExpDegMinDecimal, _ := regexp.Compile(`^\s*([+-])?\s*([0-9]{1,4})\s*°\s*([0-9]{1,2})\s*'\s*([0-9]{1,2}[.,0-9]*)\s*"?\s*$`)

	val = strings.ReplaceAll(val, ",", ".")

	if regExpFloat.MatchString(val) {
		matches := regExpFloat.FindAllStringSubmatch(val, -1)
		a, err = angleFromString_anglefloatant(matches)
	} else if regExpDegDecimal.MatchString(val) {
		matches := regExpDegDecimal.FindAllStringSubmatch(val, -1)
		a, err = angleFromString_angledecimal(matches)
	} else if regExpDegMinDecimal.MatchString(val) {
		matches := regExpDegMinDecimal.FindAllStringSubmatch(val, -1)
		a, err = angleFromString_angledecimaldegmin(matches)
	} else {
		a = newAngle(0)
		err = fmt.Errorf("Impossible de decder %s", val)
	}

	return a, err
}

func angleFromString_angledecimaldegmin(matches [][]string) (angle, error) {
	var err error = nil

	for i, match := range matches {
		coef := 1.0
		// suppression des ' ' dans le chiffre
		value := strings.ReplaceAll(match[1], " ", "")
		if value == "-" {
			coef = -1.0
		}

		value = strings.ReplaceAll(match[2], " ", "")
		valasDeg, errConv := strconv.ParseFloat(value, 64)
		if errConv != nil {
			err = errConv
			return newAngle(0), err
		}

		value = strings.ReplaceAll(match[3], " ", "")
		valasminute, errMin := strconv.ParseFloat(value, 64)
		if errMin != nil {
			err = errMin
			return newAngle(0), err
		}

		value = strings.ReplaceAll(match[4], " ", "")
		valasseconde, errsec := strconv.ParseFloat(value, 64)
		if errsec != nil {
			err = errsec
			return newAngle(0), err
		}

		if (i + 1) == len(matches) {
			return newAngle(coef * (valasDeg*DEG2SEC + valasminute*MIN2SEC + valasseconde)), nil
		}
	}
	return newAngle(0), fmt.Errorf("impossible de decoder l'angle")
}

func angleFromString_angledecimal(matches [][]string) (angle, error) {
	var err error = nil

	for _, match := range matches {
		coef := 1.0
		// suppression des ' ' dans le chiffre
		value := strings.ReplaceAll(match[1], " ", "")
		if value == "-" {
			coef = -1.0
		}

		value = strings.ReplaceAll(match[2], " ", "")
		valasDeg, errConv := strconv.ParseFloat(value, 64)
		if errConv != nil {
			err = errConv
			return newAngle(0), err
		}

		value = strings.ReplaceAll(match[3], " ", "")
		valasminute, errMin := strconv.ParseFloat(value, 64)
		if errMin != nil {
			err = errMin
			return newAngle(0), err
		}

		return newAngle(coef * (valasDeg*DEG2SEC + valasminute*MIN2SEC)), nil
	}

	return newAngle(0), fmt.Errorf("impossible de decoder l'angle")
}

func angleFromString_anglefloatant(matches [][]string) (angle, error) {
	var err error = nil

	// -------------------------------------------------------------------------------
	// cas 1
	//                            +/-     1.23                 °
	// -------------------------------------------------------------------------------
	for _, match := range matches {
		// suppression des ' ' dans le chiffre
		value := strings.ReplaceAll(match[1], " ", "")
		valasDeg, errConv := strconv.ParseFloat(value, 64)

		if errConv != nil {
			err = errConv
			return newAngle(0), err
		}
		return newAngle(valasDeg * DEG2SEC), nil
	}
	return newAngle(0), fmt.Errorf("impossible de decoder l'angle")
}
