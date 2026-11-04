import os
import sys


import signal
import argparse



import logging
import logging.config
import json

from maree.MareeScrapperFactory import MareeScrapperFactory
from maree.MareeScrapper import MareeScrapper

loggingInitPath = os.path.join(os.path.dirname(__file__), ".\\logging.json")
with open(loggingInitPath, 'r') as f:
    CONFIG = f.read()
logging.config.dictConfig(json.loads(CONFIG))
logger = logging.getLogger("Maree")
logger.info("Configured the logger!")



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
    parser.add_argument('-l',   '--listPort',  dest="arg_listp", action="store_true",  default=False,  help='Liste des ports principaux')
    parser.add_argument('-p',   '--port',      dest="arg_port",type=str, default=None,              help='Nom du port ou chercher les infos maree Ex: Paimpol')
    parser.add_argument('-f',   '--date',      dest="arg_date",type=str, default=None,              help='definit la date de calcul au format yyyymmdd. Ex: 20151003 = 3 oct. 2015')
    parser.add_argument('-d',   '--duree',     dest="arg_duree",type=int, default=7,                help='definit la duree de calcul (reponse en multiple de 7)')
    parser.add_argument('-o',   '--origine',   dest="arg_from",type=str,  default="Shom",           help='Shom ou MareeInfo ?')
    args = parser.parse_args()
    
    allOrigines : dict = MareeScrapperFactory.getOrigines()
    if not args.arg_from in allOrigines:
        print ("On ne sait scrappe que les donnees issues de :")
        for k in allOrigines:
            print ("\t - ", k)

        parser.print_help()
        sys.exit (1)

    logger.info ("source: " + args.arg_from)
    iOrigine : int = allOrigines[args.arg_from]
    source : MareeScrapper = MareeScrapperFactory.getScrapper(iOrigine)

    isOK : bool = False
    if args.arg_listp:
        source.getListPort()
        isOK = True

    elif args.arg_port != None and args.arg_date != None:
        if (not source.isDateValide(args.arg_date)):
            print ("Format de date invalide. Date:>>" + args.arg_date + "<< attendu yyyymmdd")
            parser.print_help()
            sys.exit (1)

        duree : int = -1
        try: 
            duree = int(args.arg_duree)
        except:
            print ("Format de duree invalide")
            duree = -1
        
        if (duree < 2) or (duree > 14):
            print ("Format de duree invalide ((> 1) et (<14))")
            parser.print_help()
            sys.exit (1)

        outputName : str = os.path.join (os.path.dirname(__file__), ".\\out\\" + source.getType() + "_maree.csv")
        if (not os.path.exists (os.path.dirname(outputName))):
            os.makedirs (name=os.path.dirname(outputName),mode=777)
            
        source.getPortMaree (args.arg_port, args.arg_date, duree, outputName=outputName)
    else:
        parser.print_help()
        sys.exit (1)

    logger.info ("The end ...")
