# coding: utf-8

import re
import signal

class Convertisseur:
    __allRegExp = [ r"^(-)?(\d+)°(\d{1,2})'(\d{1,2})(\.(\d+))?\"?$", r"^(-)?(\d+)°(\d{1,2})(\.(\d+))?\'$", r"^(-)?(\d+)°(\d{1,2})(\.(\d+))?$",r"^(-)?(\d+)(\.(\d+))?\°?$"]
    regexHoraire = r"^\s*(\d{1,2})([:h](\d{1,2})([:m](\d{1,2}(\.\d*)?)s?)?)?\s*$"
    __isdebug = False

    def __init__(self) -> None:
        pass

    def parseAngle(self, x) -> float :
        retour = 0.0
        i=0
        hasASolution=False
        hasSigne = False
        
        for regexp in self.__allRegExp:
            if hasASolution: 
                break

            i = i+1
            matches = re.finditer(regexp, x, re.MULTILINE)
            allSols = list(matches)
            if len(allSols) > 0 :
                for match in allSols:
                    if self.__isdebug:
                        for groupNum in range(0, len(match.groups())):
                            groupNum = groupNum + 1                    
                            print ("Case {i} Group {groupNum} found : {group}".format(i=i, groupNum = groupNum, group = match.group(groupNum)))
                    
                    if match.group(0).startswith(r"-"):
                        hasSigne = True

                    if i == 1:
                        print ("Parse as Hexadecimal angle",end = "\n")
                        hasASolution=True
                        retour = float(match.group(2)) + float(match.group(3))/60.0 + float(match.group(4))/3600.0
                        if match.group(5) != None:
                            retour +=  float(match.group(5))/3600.0
                        break

                    elif i == 2:
                        print ("Parse as Hexadecimal Minutes ",end = "\n")
                        hasASolution=True
                        retour = float(match.group(2)) + float(match.group(3))/60.0 
                        if match.group(4) != None:
                            retour +=  float(match.group(4))/60.0
                        break

                    elif i == 3:
                        print ("Parse as Decimal Minutes ",end = "\n")
                        hasASolution=True
                        retour = float(match.group(2)) + float(match.group(3))/100.0 
                        if match.group(4) != None:
                            retour +=  float(match.group(4))/100.0
                        break

                    elif i == 4:
                        print ("Parse as Decimal angle ",end = "\n")
                        hasASolution=True
                        retour = float(match.group(2))  
                        if match.group(3) != None:
                            retour +=  float(match.group(3))
                        break
        if hasSigne:
            retour = retour * (-1)
        return retour
    
    def parseHeure(self, x) -> float :
        retour = 0.0
        i=0
        
        matches = re.match(self.regexHoraire, x)
        if matches == None:
            return -1.0
        
        if matches.group(1) != None:
            heure = float (matches.group(1))

        minute = 0.0
        if matches.group(3) != None:
            minute = float (matches.group(3))

        seconde = 0.0
        if matches.group(5) != None:
            seconde = float (matches.group(5))

        heure = heure + (minute + seconde / 60.0) / 60.0
        return heure

    def printAngle(self, x) -> str :
        retour = ""
        signe = "+"
        if x < 0.0:
            signe = "-"
        angle = abs(x)
        
        # hexa
        nbSecondeTotale = angle * 3600.0
        degre = int(angle)
        minuteHexa = int((nbSecondeTotale - 3600 * degre) / 60.0)
        secondesHexa = nbSecondeTotale - 3600 * degre - 60 * minuteHexa

        # decimal
        minuteDecimale = (angle - int(angle)) * 100.0
        retour = "lu: >>>" + str(x) + "<<<" + \
            "\n\t [angle decimale:   "+ signe + str(angle) + "°" + "]"  + \
            "\n\t [minute decimale:  "+ signe + str(int(angle)) + "°" + str(minuteDecimale) + "]"  + \
            "\n\t [-> minute hexa:   "+ signe + str(int(angle)) + "°" + str((nbSecondeTotale - 3600 * degre) / 60.0) + "']"  + \
            "\n\t [seconde decimale: "+ signe + str(int(angle)) + "°" + str(int(minuteHexa)) + "'" + str(secondesHexa * 100.0 / 60.0) + "]" + \
            "\n\t [seconde hexa:     "+ signe + str(int(angle)) + "°" + str(int(minuteHexa)) + "'" + str(secondesHexa) + "\"]"  
        return retour

    def printHeure(self, x) -> str :
        retour = ""
        
        # hexa
        nbSecondeTotale = x * 3600.0
        heure = int(x)
        minute = int((nbSecondeTotale - 3600 * heure) / 60.0)
        secondes = nbSecondeTotale - 3600 * heure - 60 * minute

        # decimal
        retour = "lu: >>>%s<<<   [heure:  %02d:%02d:%02d ]" % (str(x), heure, minute, secondes) 
        return retour
