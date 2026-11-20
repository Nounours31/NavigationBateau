package navLibs

import (
	"fmt"
)

// Contrat d'entrée : ce que VOTRE package exige pour fonctionner

type Position struct {
	Lat  Latitude
	Long Longitude
}

func NewPosition(lat Latitude, long Longitude) Position {
	var retour Position = Position{
		Lat:  lat,
		Long: long,
	}
	return retour
}

func (this Position) String() string {
	return fmt.Sprintf("%s, %s", this.Lat, this.Long)
}

/*
func (c Position) MarshalJSON() ([]byte, error) {
	return json.Marshal(struct {
		Lat float64 `json:"lat"`
		Lon float64 `json:"lon"`
	}{
		Lat: c.lat,
		Lon: c.lon,
	})
}

// UnmarshalJSON valide les données lors de la lecture d'un JSON.
func (c *Position) UnmarshalJSON(data []byte) error {
	var aux struct {
		Lat float64 `json:"lat"`
		Lon float64 `json:"lon"`
	}
	if err := json.Unmarshal(data, &aux); err != nil {
		return err
	}
	valid, err := NewCoordinates(aux.Lat, aux.Lon)
	if err != nil {
		return err
	}
	*c = valid
	return nil
}
*/
