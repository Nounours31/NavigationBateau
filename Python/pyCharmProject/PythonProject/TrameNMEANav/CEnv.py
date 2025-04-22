

class CEnv:
    @staticmethod
    def getProjectName():
        return 'NMEA'

    @staticmethod
    def getDefaultLogbackFile():
        return './logging.ini'

    @staticmethod
    def getAppName():
        return CEnv.getProjectName()


