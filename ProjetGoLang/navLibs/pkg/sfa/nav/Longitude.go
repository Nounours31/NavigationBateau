package navLibs

import (
	"fmt"
	"log/slog"
	"math"
	"regexp"
	"strings"
)

// Contrat d'entrée : ce que VOTRE package exige pour fonctionner
type ILongitude interface {
	GetVal() (float64, error)
}

type sensLongitude struct { // unexported type users can't construct their own.
	sens
}

type Longitude struct {
	_val angle
	sensLongitude
}

func (this *Longitude) AsDeg() float64 {
	var retour float64 = 0.0
	retour = this.sensLongitude.value() * this._val.asDeg()
	return retour
}

func (this *Longitude) asSec() float64 {
	var retour float64 = 0.0
	retour = this.sensLongitude.value() * this._val.asSec()
	return retour
}

func (this Longitude) equalsInDeg(o float64) bool {
	if math.Abs(this.asSec()-o*DEG2SEC) < ANGLE_EPSILON {
		return true
	}
	slog.Info(fmt.Sprintf("Delta: %f - this: %f VS o: %f", math.Abs(this._val._asSecondes-o*DEG2SEC), this._val._asSecondes, o*DEG2SEC))
	return false
}

func (this Longitude) String() string {
	return fmt.Sprintf("%s", this._val.String())
}

func NewLongitude(a angle, sens sensLongitude) Longitude {
	var retour Longitude = Longitude{
		_val:          newAngle(a.asSec()),
		sensLongitude: sens,
	}
	return retour
}

func LongitudeFromString(val string) (Longitude, error) {
	var longitude Longitude = NewLongitude(newAngle(0), sensLongitude{sens: E})
	var err error = nil

	regExpAngle, _ := regexp.Compile(`^\s*([EeWwOo])?\s*([\+\-0-9°'".,\s]+)\s*([EeWwOo])?\s*$`)
	if regExpAngle.MatchString(val) {
		matches := regExpAngle.FindAllStringSubmatch(val, -1)

		for _, match := range matches {
			var coef float64 = 1.0
			var hasSens bool = false
			// suppression des ' ' dans le chiffre
			value := strings.ReplaceAll(match[1], " ", "")
			if strings.ToLower(value) == "e" || strings.ToLower(value) == "o" || strings.ToLower(value) == "w" {
				if strings.ToLower(value) == "w" || strings.ToLower(value) == "o" {
					coef = -1.0
				}
				hasSens = true
			}

			// suppression des ' ' dans le chiffre
			value = strings.ReplaceAll(match[2], " ", "")
			a, errConv := angleFromString(value)
			if errConv != nil {
				err = errConv
				return longitude, err
			}
			if math.Abs(a.asDeg()) > 180.0 {
				err = fmt.Errorf("Angle > 90° (%s)- KO ", val)
				return longitude, err
			}
			if (a.asDeg() < 0.0) && (hasSens) {
				err = fmt.Errorf("On ne peut pas avoir sens et signe : %s", val)
				return longitude, err
			} else if a.asDeg() < 0.0 {
				coef = -1.0
				a.deg(-1.0 * a.asDeg())
				hasSens = true
			}

			value = strings.ReplaceAll(match[3], " ", "")
			if (hasSens) && (strings.ToLower(value) == "e" || strings.ToLower(value) == "w" || strings.ToLower(value) == "o") {
				err = fmt.Errorf("On ne peut pas avoir sens et signe : %s", val)
				return longitude, err
			}

			if strings.ToLower(value) == "w" || strings.ToLower(value) == "o" {
				coef = -1.0
			}

			var sens sensLongitude = sensLongitude{sens: E}
			if coef < 0 {
				sens = sensLongitude{sens: W}
			}
			longitude = NewLongitude(a, sens)
		}
	}
	return longitude, err
}
