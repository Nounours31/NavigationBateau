# coding: utf-8

import signal
import re
import package_angle_sfa.src.package_angle_sfa.angle as angle


def handler(signum, frame):
    msg = "Ctrl-c was pressed. Do you really want to exit? y/n "
    print(msg, end="\n", flush=True)
    print("-" * len(msg), end="\n", flush=True) # clear the printed line
    exit(1)


def myAngleReader() :
    myParser = angle.Convertisseur()
    while True:
        inputVal = input("[Saisir un angle / vide (ou) Ctrl C]==> ")
        if inputVal == "":
            return
        x = myParser.parseAngle(inputVal)
        s = myParser.printAngle(x)
        print("The input value is {inputVal} : {s}".format(inputVal=inputVal, s = s))

def myHeureReader() :
    myParser = angle.Convertisseur()
    while True:
        inputVal = input("[Saisir une heure / vide (ou) Ctrl C]==> ")
        if inputVal == "":
            return
        x = myParser.parseHeure(inputVal)
        if x >= 0.0:
            s = myParser.printHeure(x)
            print("The input value is {inputVal} : {s}".format(inputVal=inputVal, s = s))
        else:
            print("The input value is {inputVal} : invalide")

def myInterpolationReader() :
    regex2 = r"^\s*(\d+(\.\d*)?)\s*,\s*(\d+(\.\d*)?)\s*$"
    regex1 = r"^\s*(\d+(\.\d*)?)\s*$"
    while True:
        print ("Saisir \n\t 1. [x0=temps, y0=valeur] pour debut \n\t 2. [x1=temps, y1=valeur] pour fin \n\t 3. x entre x0 et x1 pout la valeur extrapolee.\n\t ou x pour sortir")
        inputVal = input("debut [format: x0, y0] => ")
        if inputVal == "x":
            break
        matches = re.match (regex2, inputVal)
        if matches == None:
            print ("format invalide - ex: 0.3, 53.2")
            continue
        else:
            x0 = float(matches.group(1))
            y0 = float(matches.group(3))

        inputVal = input("fin   [format: x1, y1] => ")
        if inputVal == "x":
            break
        matches = re.match (regex2, inputVal)
        if matches == None:
            print ("format invalide - ex: 0.3, 53.2")
            continue
        else:
            x1 = float(matches.group(1))
            y1 = float(matches.group(3))

        inputVal = input("x     [format: x]      => ")
        if inputVal == "x":
            break
        matches = re.match (regex1, inputVal)
        if matches == None:
            print ("format invalide - ex: 0.3")
            continue
        else:
            x = float(matches.group(1))


        val = y0
        if x1 != x0:
            val = y0 + (y1 - y0) * (x - x0) /  (x1 - x0) 
            
        print(f"Entre [x0={x0},val0={y0}] <-- x= {x} => val={val} --> [x1={x1},val1={y1}]")

def myInterpolationAngleReader() :
    myParser = angle.Convertisseur()
    regexAngle = r"\s*(\d+°(\d{1,2}(\.\d+)?)?)\s*$"
    regexHoraire = r"^\s*((\d{1,2})([:h](\d{1,2})([:m](\d{1,2}(\.\d*)?)s?)?)?)\s*"
    fullregexp =  regexHoraire + "," +  regexAngle

    while True:
        print ("Saisir \n\t 1. [x0=temps(00:00:00), y0=GHA(2°59.99)] pour debut \n\t \
               2. [x1=temps, y1=GHA] pour fin \n\t \
               3. x entre x0 et x1 pout la valeur extrapolee. \n\t \
               ou x pour sortir")
        inputVal = input("debut [format:  00:00:00, 2°59.99] => ")
        if inputVal == "x":
            break
        matches = re.match (fullregexp, inputVal)
        if matches == None:
            print (f"format invalide - ex: 00:01, 2°59.99    = {fullregexp}")
            continue
        else:
            x0 = myParser.parseHeure(matches.group(1))
            y0 = myParser.parseAngle(matches.group(8)+"'")

        inputVal = input("Fin [format:  00:00:00, 2°59.99] => ")
        if inputVal == "x":
            break

        matches = re.match (fullregexp, inputVal)
        if matches == None:
            print (f"format invalide - ex: 00:01, 2°59.99    = {fullregexp}")
            continue
        else:
            x1 = myParser.parseHeure(matches.group(1))
            y1 = myParser.parseAngle(matches.group(8)+"'")

        inputVal = input("temps intermediaire [format: 00:00:00] => ")
        if inputVal == "x":
            break
        matches = re.match (regexHoraire, inputVal)
        if matches == None:
            print ("format invalide - ex: 0.3")
            continue
        else:
            x =myParser.parseHeure(matches.group(1))


        val = y0
        if x1 != x0:
            val = y0 + (y1 - y0) * (x - x0) /  (x1 - x0) 
        
        
        print(f"Entre [x0={x0},val0={y0}] <-- x= {x} --> [x1={x1},val1={y1}]")
        print(myParser.printAngle(val))


def myMainReader() :
    while True:
        print ("======================================================")
        print ("== Menu")
        print ("======================================================")
        print (" 1. Conversion angulaire ")
        print (" 2. Conversion horaire ")
        print (" 3. Interpolation ")
        print (" 4. Interpolation angle minute sexa [ex: 5°59.99]")
        print (" ------------------------------------------------ ")
        print (" x. Sortie ")
        inputVal = input(" ? ==> ")

        if inputVal == "1":
            myAngleReader()

        if inputVal == "2":
            myHeureReader()

        if inputVal == "3":
            myInterpolationReader()

        if inputVal == "4":
            myInterpolationAngleReader()

        if inputVal == "x":
            return
        

signal.signal(signal.SIGINT, handler)
myMainReader()