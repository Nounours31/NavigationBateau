from sfa_navigation.cPosition import cPosition

sleepTimeInSec=2

# Port de la trinitee
latTrinitee = 47.565102  # angleSexaToDecimal(degre = 2, minute = 56.23)
longTrinitee = -3.011115  # angleSexaToDecimal(degre = 2, minute = 56.23)
postionTrinitee: cPosition = cPosition.fromString(f"{latTrinitee:.6f},{longTrinitee:.6f}")

# Port de St Quay
latPortStQuay = 48.649665  # angleSexaToDecimal(degre = 2, minute = 56.23)
longPortStQuay = -2.813217  # angleSexaToDecimal(degre = 2, minute = 56.23)
postionStQuay: cPosition = cPosition.fromString(f"{latPortStQuay:.6f},{longPortStQuay:.6f}")

# Port de
latSamoa = -14.2456  # angleSexaToDecimal(degre = 2, minute = 56.23)
longSamoa = -169.6100  # angleSexaToDecimal(degre = 2, minute = 56.23)
