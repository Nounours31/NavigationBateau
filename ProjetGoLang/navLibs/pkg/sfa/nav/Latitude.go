package navLibs

import (
	"fmt"
	"log/slog"
	"math"
	"regexp"
	_ "sfa/navLibs/internal/logger"
	"strings"
)

// Contrat d'entrée : ce que VOTRE package exige pour fonctionner
type ILatitude interface {
	GetVal() (float64, error)
}

type sensLat struct { // unexported type users can't construct their own.
	sens
}

var (
	N sensLat = sensLat{sens: Nord}
)

type Latitude struct {
	_val angle
	sensLat
}

func (this *Latitude) AsDeg() float64 {
	var retour float64 = 0.0
	retour = this.sensLat.value() * this._val.asDeg()
	return retour
}

func (this *Latitude) asSec() float64 {
	var retour float64 = 0.0
	retour = this.sensLat.value() * this._val.asSec()
	return retour
}

func (this Latitude) equalsInDeg(o float64) bool {
	if math.Abs(this.asSec()-o*DEG2SEC) < ANGLE_EPSILON {
		return true
	}
	slog.Info(fmt.Sprintf("Delta: %f - this: %f VS o: %f", math.Abs(this._val._asSecondes-o*DEG2SEC), this._val._asSecondes, o*DEG2SEC))
	return false
}

func (this Latitude) String() string {
	return fmt.Sprintf("%s", this._val.String())
}

func NewLatitude() Latitude {
	var retour Latitude = Latitude{
		_val:    newAngle(0),
		sensLat: N,
	}
	return retour
}

func LatitudeFromString(val string) (Latitude, error) {
	slog.Info("Toto")
	var lat Latitude = NewLatitude()
	var err error = nil

	regExpAngle, _ := regexp.Compile(`^\s*([NnSs])?\s*([\+\-0-9°'".,\s]+)\s*([NnSs])?\s*$`)
	if regExpAngle.MatchString(val) {
		matches := regExpAngle.FindAllStringSubmatch(val, -1)

		for _, match := range matches {
			var coef float64 = 1.0
			var hasSens bool = false
			// suppression des ' ' dans le chiffre
			value := strings.ReplaceAll(match[1], " ", "")
			if strings.ToLower(value) == "n" || strings.ToLower(value) == "s" {
				if strings.ToLower(value) == "s" {
					coef = -1.0
				}
				hasSens = true
			}

			// suppression des ' ' dans le chiffre
			value = strings.ReplaceAll(match[2], " ", "")
			a, errConv := angleFromString(value)
			if errConv != nil {
				err = errConv
				return lat, err
			}
			if math.Abs(a.asDeg()) > 90.0 {
				err = fmt.Errorf("Angle > 90° (%s)- KO ", val)
				return lat, err
			}
			if (a.asDeg() < 0.0) && (hasSens) {
				err = fmt.Errorf("On ne peut pas avoir sens et signe : %s", val)
				return lat, err
			} else if a.asDeg() < 0.0 {
				coef = -1.0
				a.deg(-1.0 * a.asDeg())
				hasSens = true
			}

			value = strings.ReplaceAll(match[3], " ", "")
			if (hasSens) && (strings.ToLower(value) == "n" || strings.ToLower(value) == "s") {
				err = fmt.Errorf("On ne peut pas avoir sens et signe : %s", val)
				return lat, err
			}

			if strings.ToLower(value) == "s" {
				coef = -1.0
			}

			var sens sensLat = N
			if coef < 0 {
				sens = sensLat{sens: S}
			}
			lat = NewLatitude()
			lat._val = a
			lat.sensLat = sens
		}
	}
	return lat, err
}
