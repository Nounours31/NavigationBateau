import logging
import socket
from enum import Enum
from typing import List, Dict, Any

from logging import Logger
from sfa_tools.myLogger import getLogger

class eSocketConfig(Enum):
    TCP = 1
    UDP = 2

TCP_CONFIG : Dict [str, Any] = {
    "PORT" : 5006,
    "IP": "127.0.0.1"
}

UDP_CONFIG : Dict [str, Any] = {
    "PORT" : 5005,
    "IP": "127.0.0.1"
}

