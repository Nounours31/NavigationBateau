from logging import Logger
from typing import Any, Dict
from sfa_tools import myLogger

from colorist import green, Color

import argparse
import signal
import sys

import mApp


logger: Logger = myLogger.getLogger("main.py")
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
    parser.add_argument("-s", "--server", action="store_true", help="Creation du server NMEA - sans lui pas de socket")
    parser.add_argument("-c", "--client", action="store_true", help="Creation d'un client NMEA - si pas d'IPad pour permettre la comm sur la socket")
    parser.add_argument("-d", "--debug", action="store_true", help="Debug")
    parser.add_argument("-p", "--port", help="port de la socket", type=int, default=5006)
    parser.add_argument("-o", "--protocol", help="protocol de la socket", type=str, choices=['TCP', 'UDP'], default='TCP')
    parser.add_argument("-t", "--host", help="IP Host de la socket", type=str, default='127.0.0.1')

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

    global original_sigint
    original_sigint = signal.getsignal(signal.SIGINT)
    signal.signal(signal.SIGINT, exit_gracefully)


    if app_args.server:
        app.startServer (app_args.protocol, app_args.host, app_args.port)

    if app_args.client:
        app.startClient (app_args.protocol, app_args.host, app_args.port)

    app.startNavigation()    
    
    logger.info ("The end ...")




