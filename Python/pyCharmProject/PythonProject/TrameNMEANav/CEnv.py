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


"""
    latPortStQuay : CLa= 48.649665  # angleSexaToDecimal(degre = 2, minute = 56.23)
    # longPortStQuay = -2.813217  # angleSexaToDecimal(degre = 2, minute = 56.23)

    # Port de
    latSamoa = -14.2456  # angleSexaToDecimal(degre = 2, minute = 56.23)
    longSamoa = -169.6100  # angleSexaToDecimal(degre = 2, minute = 56.23)
"""
