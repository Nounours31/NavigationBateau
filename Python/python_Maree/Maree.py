import sys

import signal
import argparse

from MareeScrapperFactory import MareeScrapperFactory
import MareeScrapper

import logging
import logging.config
import os
import json

from urllib import request
from urllib.error import URLError, HTTPError

from datetime import datetime

import xml.etree.ElementTree as ET

loggingInitPath = os.path.join(os.path.dirname(__file__), ".\\logging.json")
with open(loggingInitPath, 'r') as f:
    CONFIG = f.read()
logging.config.dictConfig(json.loads(CONFIG))
logger = logging.getLogger("MareeScrapper")

# --------------------------------------------------------------
# gestion du CtrlC / pas de coverage
# --------------------------------------------------------------
def exit_gracefully(signum, frame):
    # restore the original signal handler as otherwise evil things will happen
    # in raw_input when CTRL+C is pressed, and our signal handler is not re-entrant
    signal.signal(signal.SIGINT, original_sigint)

    try:
        if input("\nReally quit? (y/n)> ").lower().startswith('y'):
            sys.exit(1)

    except KeyboardInterrupt:
        print("Ok ok, quitting")
        sys.exit(1)

    # restore the exit gracefully handler here    
    signal.signal(signal.SIGINT, exit_gracefully)

# --------------------------------------------------------------
# Le main 
# --------------------------------------------------------------
if __name__ == '__main__':
    logger.info ("Start ...")
    
    global original_sigint
    original_sigint = signal.getsignal(signal.SIGINT)
    signal.signal(signal.SIGINT, exit_gracefully)

    parser = argparse.ArgumentParser(
        prog="Maree",
        description="Extraction des info maree du site maree.info"
    )
    parser.add_argument('-l',   '--listPortPrincipaux',  dest="arg_listp", action="store_true",  default=False,  help='Liste des ports principaux')
    parser.add_argument('-q',   '--portPrincipal',       dest="arg_pp",                      default=None,  help='definit un port principal')
    parser.add_argument('-m',   '--listPortRattaches',   dest="arg_lists", action="store_true", default=False,  help='Liste des ports rataches')
    parser.add_argument('-p',   '--port',                dest="arg_port",type=str, default=None,                             help='Nom du port ou chercher les infos maree Ex: Paimpol')
    parser.add_argument('-d',   '--date',                dest="arg_date",type=str, default=None,                             help='definit la date de calcul au format yyyymmdd. Ex: 20151003 = 3 oct. 2015')
    parser.add_argument('-x',   '--duree',               dest="arg_duree",type=int, default=7,                             help='definit la duree de calcul (reponse en multiple de 7)')
    args = parser.parse_args()

    isOK : bool = False

    x : MareeScrapper = MareeScrapperFactory.getScrapper(0)
    if args.arg_listp:
        x.getPortPrincipaux()
        isOK = True

    elif args.arg_lists and args.arg_pp != None:
        x.getPortSecondaire(args.arg_pp)
        isOK = True

    elif args.arg_port != None and args.arg_date != None:
        depuis = args.arg_date
        if not isDateValide(depuis):
            print ("Format date debut INVALIDE")
            parser.print_help()
            sys.exit (1)

        
        portId : int = getPortIdFromName (args.arg_port)
        if portId == -1:
            print ("Nom port INVALIDE - n'existe pas faire une recherche pport principaux/ratachés")
            parser.print_help()
            sys.exit (1)

        x: tuple = x.getPortMareeUTC (portId, args.arg_date, args.arg_duree)
        s: str = convertTupleForXLS(x, ".\\maree.csv")
        with open(".\\maree.csv") as f:
            print (f.read())
        isOK = True

    else:
        parser.print_help()
        sys.exit (1)

    logger.info ("The end ...")
