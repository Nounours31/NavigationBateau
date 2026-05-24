from concurrent.futures import thread
from logging import Logger
import threading
from typing import Any, Dict
from pSfaTools.mLogger import getLogger



import argparse
import signal
import sys
import time

import mApp
from mGUI import cMyGUI


logger: Logger = getLogger("main.py")
app : mApp.cApp = mApp.cApp()

def exit_gracefully(signum, frame):
    signal.signal(signal.SIGINT, original_sigint)

    try:
        if input("\nReally quit? (y/n)> ").lower().startswith('y'):
            app.closeAll()
            sys.exit(0)

    except KeyboardInterrupt:
        print("Ok ok, quitting")
        app.closeAll()
        sys.exit(0)

    # restore the exit gracefully handler here    
    signal.signal(signal.SIGINT, exit_gracefully)



TCP_CONFIG : Dict [str, Any] = {
    "PORT" : 5006,
    "IP": "127.0.0.1"
}

UDP_CONFIG : Dict [str, Any] = {
    "PORT" : 5005,
    "IP": "127.0.0.1"
}


if __name__ == '__main__':
    logger.info("Start __main__")
    parser = argparse.ArgumentParser(description="Simulateur de nav pour tz boat via trame NMEA183")

    parser.add_argument("-o", "--protocol", help="protocol de la socket", type=str, choices=['TCP', 'UDP'], default='TCP')

    parser.add_argument("-s", "--server", action="store_true", help="Creation du server NMEA - sans lui pas de socket")
    parser.add_argument("-p", "--port", help="TCP and UDP port de la socket", type=int, default=5006)
    parser.add_argument("-t", "--TCP_IPhost", help="TCP - Host de la socket", type=str, default='127.0.0.1')
    parser.add_argument("-u", "--UDP_IPClient", help="IP Ipad a va recevoir les trames", type=str, default='127.0.0.1')
 
    parser.add_argument("-c", "--client", action="store_true", help="Creation d'un client NMEA - si pas d'IPad pour permettre la comm sur la socket")
    parser.add_argument("-r", "--receiver", action="store_true", help="Creation d'un receiver NMEA - pour ecouter l'IPad")
    parser.add_argument("-d", "--debug", action="store_true", help="Debug")
    parser.add_argument("-f", "--force", action="store_true", help="Force - forcer le lancement de l'app sans iPad - forcer le lancement du server sans client")

    app_args = parser.parse_args()
    if app_args.server:
        logger.debug("Create a server")
    if app_args.client:
        if not app_args.server :
            logger.error("Pas de client sans server")
            parser.print_help()
            sys.exit(1)

        logger.debug("Create a client")

    if app_args.debug:
        logger.debug("Debug mode")

    if app_args.port:
        logger.debug("port: " + str(app_args.port))

    if app_args.receiver:
        logger.debug("receiver: " + str(app_args.receiver))

    global original_sigint
    original_sigint = signal.getsignal(signal.SIGINT)
    signal.signal(signal.SIGINT, exit_gracefully)


    # ------------------------------------
    # demarre le server PC qui emet les messages
    # ------------------------------------
    
    if app_args.server:
        if app_args.protocol == "UDP":
            app.startServer (app_args.protocol, app_args.UDP_IPClient, app_args.port, receiver = app_args.receiver)
        if app_args.protocol == "TCP":
            app.startServer (app_args.protocol, app_args.TCP_IPhost, app_args.port, receiver = app_args.receiver)
    else:
        if app_args.force:
            logger.warning("Force mode - starting server without iPad")
        else:
            logger.error("No server - no socket - no communication with iPad possible")
            parser.print_help()
            sys.exit(1)

    # ------------------------------------
    # En debug sans iPad lis les trames NMEA et les drops - permet juste d'ouvrir la socket server 
    # ------------------------------------
    if app_args.client:
        app.startClient (app_args.protocol, app_args.TCP_IPhost, app_args.port)
    else:
        logger.warning("No client - no socket - no communication with iPad possible = NO PILOT INFO for example")

    # ------------------------------------
    # En attente de la jointure iPad <-> PC
    # ------------------------------------
    while not app.isReady():
        logger.info("Wait for app to be ready")
        if app_args.force:
            logger.warning("Force mode - starting app without iPad")
            break   
        time.sleep(10)

    # ------------------------------------
    # Start de l'app
    # ------------------------------------
    thread = threading.Thread(target=app.startNavigation, daemon=True)
    thread.start()

    cUI : cMyGUI = cMyGUI.getInstance()
    cUI.draw()
          
    logger.info ("The end ...")
