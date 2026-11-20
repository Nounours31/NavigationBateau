package navLibs

type sens string

func (this *sens) value() float64 {
	if *this == Nord {
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
	Nord sens = "N"
	S    sens = "S"
	E    sens = "E"
	W    sens = "W"
)
