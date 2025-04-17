import argparse
import signal
import sys
from pathlib import Path
from types import FrameType
from typing import Optional, Union, Any

from TrameNMEANav.CApp import CApp
from TrameNMEANav.CEnv import CEnv
from TrameNMEANav.CLogger import CLogger

original_sigint = None
app = None


# ----------------------------------------------------
# Call back du signal ^c
# ----------------------------------------------------
def exit_gracefully(signum: int, frame: Optional[FrameType]) -> Union[Any, int, signal.Handlers, None]:
    # restore the original signal handler as otherwise evil things will happen
    # in raw_input when CTRL+C is pressed, and our signal handler is not re-entrant
    if signum == signal.SIGINT and not original_sigint is None:
        signal.signal(signal.SIGINT, original_sigint)

    try:
        if input("\nReally quit? (y/n)> ").lower().startswith('y'):
            sys.exit(1)

    except KeyboardInterrupt:
        print("Ok ok, quitting")
        if not app is None:
            app.close()
        sys.exit(1)

    # restore the exit gracefully handler here
    signal.signal(signal.SIGINT, exit_gracefully)


# ----------------------------------------------------------------------------------
# main function
# ----------------------------------------------------------------------------------
def main():
    # gestion du ^C dnas l'App
    original_sigint = signal.getsignal(signal.SIGINT)
    signal.signal(signal.SIGINT, exit_gracefully)

    # recup des parametre
    parser = argparse.ArgumentParser(
        prog=CEnv.getAppName(),
        description='Simulation NMEA de nav entre 2 points',
        epilog='en cours ...')
    '''
    parser.add_argument('filename', nargs='*', default=[1, 2, 3], help='BAR!')       # positional argument
    parser.add_argument('-c', '--count', type=int, default=42)      # option that takes a value
    parser.add_argument('-v', '--verbose', action='store_true')
    '''
    requiredNamed = parser.add_argument_group('Arg necessaires')
    requiredNamed.add_argument('-logback', type=str, nargs=1, help='Logback file', required=True)
    requiredNamed.add_argument('-logdir', type=str, nargs=1, help='Log directory', required=True)

    args = parser.parse_args()
    if args.logback is None or args.logdir is None:
        parser.print_usage()
        sys.exit(1)

    logbackFile = args.logback[0]
    logDir = args.logdir[0]
    my_file = Path(logbackFile)
    if not my_file.is_file():
        print("invalid logback file - not exist :" + logbackFile)
        parser.print_usage()
        sys.exit(1)

    my_file = Path(logDir)
    if not my_file.is_dir():
        print("invalid logdir file - not exist :" + logDir)
        parser.print_usage()
        sys.exit(1)

    CLogger.init(CEnv.getAppName(), logbackFile, logDir)
    app = CApp()
    app.start()


if __name__ == '__main__':
    print(f"Start {CEnv.getAppName()}")
    main()
    print("The end ...")
