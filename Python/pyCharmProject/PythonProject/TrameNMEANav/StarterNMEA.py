import argparse
import signal
import sys
import logging


from math import floor, pi, cos, sin
from pathlib import Path
from time import sleep
from datetime import datetime, timezone
import calendar
from types import FrameType
from typing import Optional, Union, Any

from TrameNMEANav.CAngle import CAngle
from TrameNMEANav.CLatitude import CLatitude
from TrameNMEANav.CLogger import CLogger
from TrameNMEANav.CLongitude import CLongitude
from TrameNMEANav.CNMEA import CNMEA
from TrameNMEANav.CSocketNetWorkForNMEA import CSocketNetWorkForNMEA
from TrameNMEANav.CUI import CUI
from TrameNMEANav.Cnavigation import CNavigation


parser = argparse.ArgumentParser(
                    prog='ProgramName',
                    description='What the program does',
                    epilog='Text at the bottom of help')
'''
parser.add_argument('filename', nargs='*', default=[1, 2, 3], help='BAR!')       # positional argument
parser.add_argument('-c', '--count', type=int, default=42)      # option that takes a value
parser.add_argument('-v', '--verbose', action='store_true')
'''
requiredNamed = parser.add_argument_group('required named arguments')
requiredNamed.add_argument('-logback', type=str, nargs=1,help='Logback file', required=True)
requiredNamed.add_argument('-logdir', type=str, nargs=1, help='Log directory', required=True)


args = parser.parse_args()
print(args.logback, args.logdir)
if  args.logback is None or args.logdir is None:
    parser.print_usage()
    sys.exit(1)

my_file = Path(args.logback[0])
if not my_file.is_file():
    print("invalid logback file - not exist :" + args.logback)
    parser.print_usage()
    sys.exit(1)

my_file = Path(args.logdir[0])
if not my_file.is_dir():
    print("invalid logdir file - not exist :" + args.logdir)
    parser.print_usage()
    sys.exit(1)

logger : logging.Logger = CLogger.getLogger('NMEA', args.logback[0], args.logdir[0])
myNMEANetwork : CSocketNetWorkForNMEA = CSocketNetWorkForNMEA()
original_sigint = None



MAX_TIME = 3000



def exit_gracefully(signum:int, frame:  Optional[FrameType]) -> Union[Any, int, signal.Handlers, None]:
    # restore the original signal handler as otherwise evil things will happen
    # in raw_input when CTRL+C is pressed, and our signal handler is not re-entrant
    if signum == signal.SIGINT:
        signal.signal(signal.SIGINT, original_sigint)

    if not frame is None:
        localFrame: FrameType = frame
        logger.debug("Frame: {}".format(localFrame))

    try:
        if input("\nReally quit? (y/n)> ").lower().startswith('y'):
            sys.exit(1)

    except KeyboardInterrupt:
        print("Ok ok, quitting")
        myNMEANetwork.close()
        sys.exit(1)

    # restore the exit gracefully handler here
    signal.signal(signal.SIGINT, exit_gracefully)


# ----------------------------------------------------------------------------------
# main function
# ----------------------------------------------------------------------------------
def main():
    myNMEANetwork.openSocket()


    logger.debug("UDP target IP: %s" % CSocketNetWorkForNMEA.getIP())
    logger.debug("UDP target port: %s" % CSocketNetWorkForNMEA.getPort())

    sleepTimeInSec = 2
    now = datetime.now(tz=timezone.utc)
    vitesseEnNoeudMoyenne = 5.0
    capMoyen = 45.0
    variationMagnetique = -1.2
    # Port de St Quay
    #latPortStQuay = 48.649665  # angleSexaToDecimal(degre = 2, minute = 56.23)
    #longPortStQuay = -2.813217  # angleSexaToDecimal(degre = 2, minute = 56.23)

    # Port de
    latSamoa = -14.2456  # angleSexaToDecimal(degre = 2, minute = 56.23)
    longSamoa = -169.6100  # angleSexaToDecimal(degre = 2, minute = 56.23)

    a = CAngle(angleDegreDouble = latSamoa)

    positionDepartLatitudeDecimale = latSamoa
    positionDepartLongitudeDecimale = longSamoa

    positionLatitudeDecimale, positionLongitudeDecimale = positionDepartLatitudeDecimale, positionDepartLongitudeDecimale

    nmeaTool : CNMEA = CNMEA("x")
    while True:
        prev = now
        now = datetime.now(tz=timezone.utc)
        nbSecondes = calendar.timegm(now.timetuple()) - calendar.timegm(prev.timetuple())

        cap = capMoyen + 5.0 * cos(10 * nbSecondes)  # +/- 5 degre
        vitesseEnNoeud = vitesseEnNoeudMoyenne + (vitesseEnNoeudMoyenne / 10) * cos(100 * nbSecondes)  # +/- 10%

        prevpositionLatitudeDecimale, prevpositionLongitudeDecimale = positionLatitudeDecimale, positionLongitudeDecimale
        positionLatitudeDecimale, positionLongitudeDecimale = CNavigation.nav(nbSecondes, vitesseEnNoeud, cap,
                                                                  prevpositionLatitudeDecimale,
                                                                  prevpositionLatitudeDecimale,
                                                                  prevpositionLongitudeDecimale)

        gga = nmeaTool.getGGA(now, positionLatitudeDecimale, positionLongitudeDecimale)
        rmc = nmeaTool.getRMC(now, positionLatitudeDecimale, positionLongitudeDecimale, vitesseEnNoeud, cap)
        vtg = nmeaTool.getVTG(vitesseEnNoeud, cap)
        gsa = nmeaTool.getGSA()
        gsv = nmeaTool.getGSV()
        depth = nmeaTool.getDepth(nbSecondes)
        hdg = nmeaTool.getHDG(cap, variationMagnetique)
        trueWind = nmeaTool.getWindInfo("True", 150.0, 12.2)
        relativeWind = nmeaTool.getWindInfo("Relative", 150.0, 12.2)
        bwr = nmeaTool.getWayPointInfoBWR(now, positionDepartLatitudeDecimale + 2.0, positionDepartLongitudeDecimale + 2.0, 45.0,
                               variationMagnetique, 2.98, "WP1")
        bwc = nmeaTool.getWayPointInfoBWC(now, positionDepartLatitudeDecimale + 2.0, positionDepartLongitudeDecimale + 2.0, 45.0,
                               variationMagnetique, 2.98, "WP1")
        logger.debug(gga)
        logger.debug(gsv)
        logger.debug(gsa)
        logger.debug(vtg)
        logger.debug(rmc)
        logger.debug(depth)
        logger.debug(hdg)
        logger.debug(trueWind)
        logger.debug(relativeWind)
        logger.debug(bwr)
        logger.debug(bwc)

        logger.info("Position duree:{duree:d} lat:{lat} long:{long}".format(
            duree=nbSecondes,
            lat=CLatitude.fromLatDecimalToStr(positionLatitudeDecimale),
            long=CLongitude.fromLongDecimalToStr(positionLongitudeDecimale)))

        myNMEANetwork.sendto(gga)

        for x in gsv:
            myNMEANetwork.sendto(x)

        myNMEANetwork.sendto(gsa)
        myNMEANetwork.sendto(vtg)
        myNMEANetwork.sendto(rmc)
        myNMEANetwork.sendto(depth)
        myNMEANetwork.sendto(hdg)
        myNMEANetwork.sendto(relativeWind)
        myNMEANetwork.sendto(trueWind)
        myNMEANetwork.sendto(bwr)
        myNMEANetwork.sendto(bwc)


        sleep(sleepTimeInSec)
        nbSecondes = nbSecondes + sleepTimeInSec

        if nbSecondes > MAX_TIME:
            myNMEANetwork.close()
            break



if __name__ == '__main__':
    original_sigint = signal.getsignal(signal.SIGINT)
    signal.signal(signal.SIGINT, exit_gracefully)
    main()
#    cUI : CUI = CUI()
#    cUI.init()

    print("The end ...")