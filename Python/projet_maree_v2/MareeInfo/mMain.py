
import argparse

import logging
from logging import Logger, getLogger

import datetime
from datetime import datetime

import sys
print(sys.path)

from pMareeInfo import mListPortConnu, mCExtract

logging.basicConfig(
    level=logging.INFO,  # Set minimum log level
    format="%(asctime)s - %(name)s - %(levelname)s - %(message)s",
    datefmt="%Y-%m-%d %H:%M:%S"
)

logger: Logger = getLogger("main.py")
logger.info("Start pMareeInfo")




if __name__ == '__main__':
    logger.info("Start __main__")
    parser = argparse.ArgumentParser(description="pMareeInfo - Marée Information")
    parser.add_argument("-p", "--port", help="Port", type=str, default="Cancale") 
    parser.add_argument("-l", "--latitude", help="Latitude Port", type=float, default=48.676) 
    parser.add_argument("-L", "--longitude", help="Longitude Port", type=float, default=-1.852) 
    parser.add_argument("-s", "--start", help="heure de debut [format iso. Ex: 2024-06-01T00:00:00Z]", required=True, type=str, default="2024-06-01T00:00:00Z") 
    parser.add_argument("-e", "--end", help="heure de fin [format iso. Ex: 2024-06-01T23:59:59Z]", required=True, type=str, default="2024-06-01T23:59:59Z") 

    args = parser.parse_args()
    if (args is not None) and ((args.port is not None) and ((args.longitude is not None) or (args.latitude is not None))) :
        logger.info("Extracting tide data...") 
        
        indicePort : int = -1
        for i, p in enumerate(mListPortConnu.listPortConnu):
            if args.port.lower() == p["nom"].lower():
                indicePort = i

        start: datetime = datetime.fromisoformat(args.start.replace("Z", "+00:00"))
        end: datetime = datetime.fromisoformat(args.end.replace("Z", "+00:00"))
        
        extractor = mCExtract.CExtract(indicePort, start.timestamp(), end.timestamp())
        extractor.extract_tide_data_arrow()

    else:
        logger.error("Invalid arguments provided.") 
        parser.print_help()
        
