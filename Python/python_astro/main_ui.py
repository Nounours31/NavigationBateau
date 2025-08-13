# https://python-course.eu/tkinter/buttons-in-tkinter.php
# https://www.studytonight.com/tkinter/python-tkinter-labelframe-widget
# TOP pour le grid : https://koor.fr/Python/Tutoriel_Tkinter/tkinter_layout_grid.wp


import io
import os
import os.path

from datetime import datetime, timezone


from tools.cPosition import cPosition

BASE_DIR = os.path.dirname(os.path.abspath(__file__))

import re
import tkinter as tk
from tkinter import ttk, END
import tkinter.font as tkFont
import tkinter.messagebox as tkMessageBox


from tools.cAstroError import cAstroError
from tools.cAngle import cAngle
from tools.cLatitude import cLatitude
from tools.cLongitude import cLongitude
from tools.cEphemerides import cEphemerides
from tools.cHeureJour import cHeureJour
from tools.cHeure import cHeure
from tools.cJour import cJour
from tools.cLatitude import cLatitude

import configparser
config = configparser.ConfigParser()

__CONFIG_FILE__ : str = 'config.ini'
config : configparser 
def writeconfig():
    with open(__CONFIG_FILE__, 'w', encoding="utf-8") as configfile:
        config.write(configfile)
    

def readconfig():
    if not os.path.isfile(__CONFIG_FILE__):
        return

    with open(__CONFIG_FILE__, 'r', encoding="utf-8") as configfile:
        config.read_file(configfile)


def on_option_change(event):
    print("Save config")
    saveToConfigFile()

def on_entry_change(event):
    print("Save config")
    saveToConfigFile()

def handle_focus_in(event):
    # print("Save config")
    # saveToConfigFile()
    print("Pas de save config - que au focus out")

def handle_focus_out(event):
    global buttonCompute
    
    print("Save config")
    saveToConfigFile()

    if not buttonCompute is None:
        buttonCompute["state"] = "disabled"
        if isValideForCompute():
            buttonCompute["state"] = "normal"

def isValideForCompute ():
    isNotComputable = latitudeEntryVal.get() == "" or longitudeEntryVal.get() == "" 
    isNotComputable = isNotComputable or dateEntryVal.get() == "" or heureEntryVal.get() == "" or chronosEntryVal.get() == ""
    isNotComputable = isNotComputable or collimationEntryVal.get() == "" or hauteurInstrumentaleEntryVal.get() == "" or hauteurOeilEntryVal.get() == ""
    isNotComputable = isNotComputable or not isFormatValide(uimessage = False)
    return not isNotComputable

def isFormatValide(uimessage : bool = True):
    isOK = True
    if latitudeEntryVal.get() != "": 
        try:
            l : cLatitude = cLatitude()
            l.parse(latitudeEntryVal.get())
        except Exception as e:
            if uimessage:
                tkMessageBox.showerror(title="Latitude parsing Error", message=f"{e.args}")
            latitudeEntryVal.set("")
            isOK = False

    if longitudeEntryVal.get() != "": 
        try:
            l : cLongitude = cLongitude()
            l.parse(longitudeEntryVal.get())
        except Exception as e:
            if uimessage:
                tkMessageBox.showerror(title="Longitude parsing Error", message=f"{e.args}")
            longitudeEntryVal.set("")
            isOK = False
    
    if dateEntryVal.get() != "": 
        try:
            l : cJour = cJour()
            l.parse(dateEntryVal.get())
        except Exception as e:
            if uimessage:
                tkMessageBox.showerror(title="Date parsing Error", message=f"{e.args}")
            dateEntryVal.set("")
            isOK = False

    if heureEntryVal.get() != "": 
        try:
            l : cHeure = cHeure()
            l.parse(heureEntryVal.get())
        except Exception as e:
            if uimessage:
                tkMessageBox.showerror(title="Heure parsing Error", message=f"{e.args}")
            heureEntryVal.set("")
            isOK = False

    if chronosEntryVal.get() != "": 
        try:
            l : cHeure = cHeure()
            l.parseChrono2Seconde(chronosEntryVal.get())
        except Exception as e:
            if uimessage:
                tkMessageBox.showerror(title="Chrono parsing Error", message=f"{e.args}")
            chronosEntryVal.set("")
            isOK = False

    if collimationEntryVal.get() != "": 
        try:
            l : cAngle = cAngle()
            l.parse(collimationEntryVal.get())
        except Exception as e:
            tkMessageBox.showerror(title="Collimation parsing Error", message=f"{e.args}")
            collimationEntryVal.set("")
            isOK = False

    if hauteurInstrumentaleEntryVal.get() != "": 
        try:
            l : cAngle = cAngle()
            l.parse(hauteurInstrumentaleEntryVal.get())
        except Exception as e:
            if uimessage:
                tkMessageBox.showerror(title="hauteurInstrumentale parsing Error", message=f"{e.args}")
            hauteurInstrumentaleEntryVal.set("")
            isOK = False

    return isOK

def toString():
    retour = ""

    latitude = "Undef"
    if latitudeEntryVal.get() != "": 
        try:
            l : cLatitude = cLatitude()
            latitude = l.parse(latitudeEntryVal.get()).toString()
        except Exception as e:
            tkMessageBox.showerror(title="Latitude parsing Error", message=f"{e.args}")
    
    longitude = "Undef"
    if longitudeEntryVal.get() != "": 
        try:
            l : cLongitude = cLongitude()
            longitude = l.parse(longitudeEntryVal.get()).toString()
        except Exception as e:
            tkMessageBox.showerror(title="Longitude parsing Error", message=f"{e.args}")
    
    decodeHeureJour : cHeureJour = cHeureJour()
    decodeJour : cJour = cJour()
    decodeHeure : cHeure = cHeure()
    decodeChronoEnSec : float = 0.0
    decodeFuseauAsHeure : int = 0
    if dateEntryVal.get() != "": 
        try:
            decodeJour.parse(dateEntryVal.get())
        except Exception as e:
            tkMessageBox.showerror(title="Date parsing Error", message=f"{e.args}")
    else:
        decodeJour = decodeJour.parse ("1900/01/01")
    decodeHeureJour.jour(decodeJour)
    print (f"{decodeHeureJour.toString()}")

    if heureEntryVal.get() != "": 
        try:
            decodeHeure = decodeHeure.parse(heureEntryVal.get())
        except Exception as e:
            tkMessageBox.showerror(title="Heure parsing Error", message=f"{e.args}")
    else:
        decodeHeure = decodeHeure.parse ("00:00:00")
    decodeHeureJour.heure(decodeHeure.duplicate())
    print (f"{decodeHeureJour.toString()}")
    print (f"{decodeHeure.toString()}")

    if chronosEntryVal.get() != "": 
        try:
            decodeChronoEnSec = cHeure.parseChrono2Seconde(chronosEntryVal.get())
        except Exception as e:
            tkMessageBox.showerror(title="Chrono parsing Error", message=f"{e.args}")
    else:
        decodeChronoEnSec = 0.0
    pipo = decodeHeureJour.getHeure()
    print (f"{pipo.toString()}")
    pipo.addSeconde (decodeChronoEnSec)
    print (f"{pipo.toString()}")

    if decalageUTCEntryVal.get() != "":
        valeur = decalageUTCEntryVal.get()
        valeur = int(re.sub(r"[^0-9\-\+]",'', valeur))
        decodeFuseauAsHeure = valeur
    pipo.addSeconde (-1.0 * 3600 * decodeFuseauAsHeure)
    print (f"{pipo.toString()}")
    decodeHeureJour.heure(pipo)
    print (f"{decodeHeureJour.toString()}")




    hauteurOeil = "Undef"
    if hauteurOeilEntryVal.get() != "":
        hauteurOeil = hauteurOeilEntryVal.get()

    astre = "Undef"
    if astreEntryVal.get() != "":
        astre = astreEntryVal.get()

    collimation = "Undef"    
    if collimationEntryVal.get() != "": 
        try:
            l : cAngle = cAngle()
            collimation = l.parse(collimationEntryVal.get()).toString()
        except Exception as e:
            tkMessageBox.showerror(title="Collimation parsing Error", message=f"{e.args}")

    visee = "Undef"
    if typeViseeEntryVal.get() != "": 
        visee = typeViseeEntryVal.get()

    hauteurInstrument = "Undef"
    if hauteurInstrumentaleEntryVal.get() != "": 
        try:
            l : cAngle = cAngle()
            hauteurInstrument = l.parse(hauteurInstrumentaleEntryVal.get()).toString()
        except Exception as e:
            tkMessageBox.showerror(title="hauteurInstrumentale parsing Error", message=f"{e.args}")

    retour =   "=====================================================\n"
    retour += f" Now: {datetime.now(tz=timezone.utc).strftime("%Y/%m/%d %H:%M:%S [%Z]")}\n"
    retour +=   " ----------- \n"
    retour += f" Latitude:  {latitude}\n"
    retour += f" Longitude: {longitude}\n"
    retour += f" Heure:     {decodeHeureJour.toString()}\n"
    retour += f"         Date:       {decodeJour.toString()}\n"
    retour += f"         Heure:      {decodeHeure.toString()}\n"
    retour += f"         Chrono (s): {decodeChronoEnSec}\n"
    retour += f"         Fuseau(h):  {decodeFuseauAsHeure}\n"
    retour += f" Astre:       {astre}\n"
    retour += f" Oeil (m):    {hauteurOeil}\n"
    retour += f" collimation: {collimation}\n"
    retour += f" visee:       {visee}\n"
    retour += f" Hi:          {hauteurInstrument}\n"
    retour += "=====================================================\n"
    
    return retour
    
def initFromConfigFile(main):
    readconfig()    
#    latitudeEntryVal.trace_add(["write", "unset"], on_entry_change)

    main.bind("<FocusIn>", handle_focus_in)
    main.bind("<FocusOut>", handle_focus_out)

    key = "position"
    if key in config:
        if 'latitude' in config[key]:
            latitudeEntryVal.set(config[key]['latitude'])
        if 'longitude' in config[key]:
            longitudeEntryVal.set(config[key]['longitude'])

    key = "horaire"
    if key in config:
        if 'date' in config[key]:
            dateEntryVal.set(config[key]['date'])
        if 'heure' in config[key]:
            heureEntryVal.set(config[key]['heure'])
        if 'chrono' in config[key]:
            chronosEntryVal.set(config[key]['chrono'])
        if 'decalageUTC' in config[key]:
            decalageUTCEntryVal.set(config[key]['decalageUTC'])

    key = "visee"
    if key in config:
        if 'astre' in config[key]:
            astreEntryVal.set(config[key]['astre'])
        if 'collimation' in config[key]:
            collimationEntryVal.set(config[key]['collimation'])
        if 'type' in config[key]:
            typeViseeEntryVal.set(config[key]['type'])
        if 'Hi' in config[key]:
            hauteurInstrumentaleEntryVal.set(config[key]['Hi'])
        if 'Oeil' in config[key]:
            hauteurOeilEntryVal.set(config[key]['Oeil'])
          
def saveToConfigFile():
    global zoneOutput
    if not isFormatValide():
        return
    
    status = toString()
    zoneOutput.delete('1.0', END)
    zoneOutput.insert (END, status)


    keys = ["position", "horaire", "visee", "calculs"]
    for key in keys:
        if not key in config:
            config[key] = {}

    key = "position"
    if latitudeEntryVal.get() != "": 
        config[key]['latitude'] = latitudeEntryVal.get()
    if longitudeEntryVal.get() != "": 
        config[key]['longitude'] = longitudeEntryVal.get()
    

    key = "horaire"
    if dateEntryVal.get() != "": 
        config[key]['date'] = dateEntryVal.get()
    if heureEntryVal.get() != "": 
        config[key]['heure'] = heureEntryVal.get()
    if chronosEntryVal.get() != "": 
        config[key]['chrono'] = chronosEntryVal.get()
    if decalageUTCEntryVal.get() != "": 
        config[key]['decalageUTC'] = decalageUTCEntryVal.get()


    key = "visee"
    if astreEntryVal.get() != "": 
        config[key]['astre'] = astreEntryVal.get()
    if collimationEntryVal.get() != "": 
        config[key]['collimation'] = collimationEntryVal.get()
    if typeViseeEntryVal.get() != "": 
        config[key]['type'] = typeViseeEntryVal.get()
    if hauteurInstrumentaleEntryVal.get() != "": 
        config[key]['Hi'] = hauteurInstrumentaleEntryVal.get()

    if hauteurOeilEntryVal.get() != "": 
        config[key]['Oeil'] = hauteurOeilEntryVal.get()
    
    writeconfig()

def onClick(e: tk.EventType):
    x: str = entry.get()
    y: str = value_inside.get()
    z: cAngle = cAngle()
    z.parse(x)
    print(f"Single Click, Button-l - >{x:s}< >{y:s}< >{z.toString():s}<")
    isOk, data = collectInfo()

def onClickButtonE(e: tk.EventType):
    dr: cPosition = cPosition()
    latitude : cLatitude = cLatitude()
    latitude = latitude.parse(latitudeEntryVal.get())
    longitude : cLongitude = cLongitude()
    longitude = longitude.parse(longitudeEntryVal.get())
    dr.setLatitude(latitude)
    dr.setLongitude(longitude)

    # -------
    decodeHeureJour : cHeureJour = cHeureJour()
    decodeJour : cJour = cJour()   
    decodeJour.parse(dateEntryVal.get())
    decodeHeure : cHeure = cHeure()
    decodeHeure = decodeHeure.parse(heureEntryVal.get())

    decodeHeureJour.jour(decodeJour)
    decodeHeureJour.heure(decodeHeure.duplicate())

    decodeChronoEnSec : float = 0.0
    decodeChronoEnSec = cHeure.parseChrono2Seconde(chronosEntryVal.get())
    decodeHeureJour.getHeure().addSeconde (decodeChronoEnSec)

    decodeFuseauAsHeure : int = 0 
    valeur = decalageUTCEntryVal.get()
    decodeFuseauAsHeure = int(re.sub(r"[^0-9\-\+]",'', valeur))
    decodeHeureJour.getHeure().addSeconde (-1.0 * 3600 * decodeFuseauAsHeure)

    t: datetime = datetime.fromtimestamp(decodeHeureJour.asTS(), tz = timezone.utc)

    # -------
    hauteurOeilEnMetre: float = float(hauteurOeilEntryVal.get())

    # -------
    collimation : cAngle = cAngle()
    collimation = collimation.parse(collimationEntryVal.get())

    # -------
    astre = astreEntryVal.get()

    # -------
    viseeAsTxt = typeViseeEntryVal.get()
    visee = 0.0
    for i in cEphemerides.getViseeInfo():
        if viseeAsTxt == i["nom"]:
            visee = i["val"]

    # -------
    hauteurInstrument : cAngle = cAngle()
    hauteurInstrument = hauteurInstrument.parse(hauteurInstrumentaleEntryVal.get())

    buffer = io.StringIO()
    buffer.write ("\n------------------------------------------------------------------------\n")
    buffer.write ("-- Resume --\n")
    buffer.write ("------------------------------------------------------------------------\n")
    buffer.write (f"-- heure:              {t}\n")
    buffer.write (f"-- collimation:        {collimation.toString()}\n")
    buffer.write (f"-- hauteurOeilEnMetre: {hauteurOeilEnMetre}\n")
    buffer.write (f"-- Position:           {dr.toString()}\n")
    buffer.write (f"-- astre:              {astre}\n")
    buffer.write (f"-- visee:              {visee}\n")
    buffer.write (f"-- hauteurAstreDeg:    {hauteurInstrument.toString()}\n")

    result = buffer.getvalue()
    zoneOutput.insert (END, result)

    x : cEphemerides = cEphemerides()
    xx = x.getEpherideAstre(t, astre, dr)
    """
            "gha_aries": GHAAries,
            "gha": gha,
            "lha": lha,
            "sha": sha,
            "dec": dec.degrees,
            "sd": semiDiametre,
            "az": az.degrees,
            "hp": HP,
            "parallaxe": parallaxe,
            "hauteurObservee": alt.degrees,
            "hauteurObserveeCorrigeeParallaxe": (alt.degrees + HP) """
    buffer = io.StringIO()
    buffer.write ("------------------------------------------------------------------------\n")
    buffer.write ("-- calcul --\n")
    buffer.write ("------------------------------------------------------------------------\n")
    buffer.write (f"-- Az:               {xx["az"]}\n")
    buffer.write (f"-- Hauteur:          {xx["hauteurObserveeCorrigeeParallaxe"]}\n")
    buffer.write (f"-- Aries:            {xx["gha_aries"]}\n")
    buffer.write (f"-- Gha:              {xx["gha"]}\n")
    buffer.write (f"-- Sha:              {xx["sha"]}\n")
    buffer.write (f"-- Lha:              {xx["lha"]}\n")
    buffer.write (f"-- Dec:              {xx["dec"]}\n")
    buffer.write (f"-- Semi Diam('):     {float(xx["sd"]) * 60.0}\n")
    buffer.write (f"-- HP ('):           {float(xx["hp"]) * 60.0}\n")
    buffer.write (f"-- P ('):            {float(xx["parallaxe"]) * 60.0}\n")
    buffer.write ("------------------------------------------------------------------------\n")
    result = buffer.getvalue()
    zoneOutput.insert (END, result)

    yy = cEphemerides.pointAstroCalculCorrection(hauteurOeil=hauteurOeilEnMetre,
                                                 Hi=hauteurInstrument.asDeg(),
                                                 collimation=collimation.asDeg(),
                                                 hp=float(xx["hp"]),
                                                 visee=visee,
                                                 diametre=float(xx["sd"]),
                                                 pressionAtm=1010,
                                                 Temperature=10)
    """
            "dip": dip,
            "Ha" : Ha,
            "parallaxeDeg" : parallaxeDeg,
            "refractionDeg": refractionDeg,
            "SD" : sd,
            "correctionTotaleSurHi": correctionTotaleSurHi,
            "correctionTotaleSurHo": correctionTotaleSurHo,
            "Ho" : Ho """
    buffer = io.StringIO()
    buffer.write (f"-- Dip:                    {yy["dip"]}\n")
    buffer.write (f"-- Ha (Hi - coll - dip):   {yy["Ha"]}\n")
    buffer.write (f"-- parallaxeDeg:           {yy["parallaxeDeg"]}\n")
    buffer.write (f"-- refractionDeg:          {yy["refractionDeg"]}\n")
    buffer.write (f"-- correctionTotaleSurHi:  {yy["correctionTotaleSurHi"]}\n")
    buffer.write (f"-- correctionTotaleSurHo:  {yy["correctionTotaleSurHo"]}\n")
    buffer.write (f"-- Ho (Ha -R + P +/-SD):   {yy["Ho"]}\n")
    buffer.write ("------------------------------------------------------------------------\n")
    result = buffer.getvalue()
    zoneOutput.insert (END, result)

    zz = cEphemerides.pointAstroPoint(dec=float(xx["dec"]),
                                      lat=dr.getLatitude().asDeg(),
                                      lha=float(xx["lha"]))
    buffer = io.StringIO()
    buffer.write (f"-- Hc:                    {zz["Hc"]}\n")
    buffer.write (f"-- Az:                    {zz["Az"]}\n")
    buffer.write (f"-- Az2:                   {zz["Az2"]}\n")
    buffer.write ("------------------------------------------------------------------------\n")
    result = buffer.getvalue()
    zoneOutput.insert (END, result)



def onClickButtonQ(e: tk.EventType):
    print("Single Click, Button-l")
    main.destroy
    import sys

    sys.exit()
    print("destroy ?")


default_label_style = {
            "bg": "#bfffbc", "fg": "black", "highlightthickness": 0, "font": ("Arial", 8, "bold")
}

default_Entry_style = {
            "bg": "#bfffbc", "fg": "black", "highlightthickness": 0, "font": ("Arial", 10, "normal")
}

default_txtbox_style = {
            "bg": "#f6f877", "fg": "black", "highlightthickness": 0, "font": ("Arial", 8, "normal")
}

def create_position_ui(main):
    frame = tk.Frame(
        master = main,
        relief = tk.RAISED,
        borderwidth = 1
    )
    frame.grid(row=0, column=0, sticky="w", padx=2, pady=2)
    labelframe = tk.LabelFrame(frame, text = "DR - Position", bg="#9AE8FC")  

    labelLat = tk.Label(labelframe, text = "Latitude:", **default_label_style)  
    labelLat.grid(column=1, row=1, sticky="e")
    
    entryLat = tk.Entry(master=labelframe, textvariable=latitudeEntryVal, **default_Entry_style)
    entryLat.config(bg="#fff", fg="#000", width=9)
    entryLat.grid(column=2, row=1, sticky="w")

    labelLong = tk.Label(labelframe, text = "Longitude:", **default_label_style)  
    labelLong.grid(column=1, row=2, sticky="e")
    
    entryLong = tk.Entry(master=labelframe, textvariable=longitudeEntryVal, **default_Entry_style)
    entryLong.config(bg="#fff", fg="#000", width=9)
    entryLong.grid(column=2, row=2, sticky="w")

    labelframe.grid(column=0, row=0)
    labelframe.pack()

def create_Heure_ui(main):
    frame = tk.Frame(
        master = main,
        relief = tk.RAISED,
        borderwidth = 1
    )
    frame.grid(row=1, column=0, sticky="w", padx=2, pady=2)
    labelframe = tk.LabelFrame(frame, text = "Date & heure (UTC)", bg="#A5FC9A")  

    labelDate = tk.Label(labelframe, text = "Date:", **default_label_style)  
    labelDate.grid(column=1, row=1, sticky="e")
    
    entryDate = tk.Entry(master=labelframe, textvariable=dateEntryVal, **default_Entry_style)
    entryDate.config(bg="#fff", fg="#000", width=12)
    entryDate.grid(column=2, row=1, sticky="w")

    labelHeure = tk.Label(labelframe, text = "Heure (start chrono):", **default_label_style)  
    labelHeure.grid(column=1, row=2, sticky="e")
    
    entryHeure = tk.Entry(master=labelframe, textvariable=heureEntryVal, **default_Entry_style)
    entryHeure.config(bg="#fff", fg="#000", width=8)
    entryHeure.grid(column=2, row=2, sticky="w")

    labelChrono = tk.Label(labelframe, text = "Valeur chrono:", **default_label_style)  
    labelChrono.grid(column=4, row=2, sticky="e")
    
    entryChronos = tk.Entry(master=labelframe, textvariable=chronosEntryVal,**default_Entry_style)
    entryChronos.config(bg="#fff", fg="#000", width=8)
    entryChronos.grid(column=5, row=2, sticky="w")

    labelDecalageUTC = tk.Label(labelframe, text = "Decalage UTC:", **default_label_style)  
    labelDecalageUTC.grid(column=7, row=2, sticky="e")
    
    options_list = []
    defaut = ""
    for i in range(-12,+13, 1):
        signe = "-" if i < 0 else "+"
        suffixe = ""
        isdefaut = False
        if i == 0:
            suffixe=" [UTC]"
            isdefaut = True
        elif i == 1:
            suffixe=" [Hiver]"
        elif i == 2:
            suffixe=" [Ete]"
        
        options_list.append(f"{signe:1s}{abs(i):1d}{suffixe}")
        if isdefaut:
            defaut = f"{signe:1s}{abs(i):1d}{suffixe}"

    # rien dand la config je l'init
    if decalageUTCEntryVal.get() == "":
        decalageUTCEntryVal.set(defaut)
    question_menu = tk.OptionMenu(labelframe, decalageUTCEntryVal, *options_list, command=on_option_change)
    question_menu.config(bg="#E4E2E2", fg="#000")
    question_menu.grid(column=8, row=2, sticky="w")

    labelframe.grid(column=0, row=0)
    labelframe.pack()

def create_Astre_ui(main):
    frame = tk.Frame(
        master = main,
        relief = tk.RAISED,
        borderwidth = 1
    )
    frame.grid(row=2, column=0,sticky="w", padx=2, pady=2)
    labelframe = tk.LabelFrame(frame, text = "Astre", bg="#A5FC9A")  

    labelAstre = tk.Label(labelframe, text = "Astre:", **default_label_style)  
    labelAstre.grid(column=0, row=0, sticky="e")
    
    options_list = []
    defaut = ""
    options_list_values = cEphemerides.getPlaneteStarNom()
    max = len(options_list_values)
    i = 1
    for x in range (0, max-1):
        print (f"Init astre : {i:03d}/{max:03d} - {options_list_values[x]}", end = "\r")
        if options_list_values[x].startswith("sun"):
            defaut = options_list_values[x]
        options_list.append(options_list_values[x])
        i +=1    
    print ("\n")

    # rien dand la config je l'init
    if  astreEntryVal.get() == "" :
        astreEntryVal.set(defaut)
    question_menu = tk.OptionMenu(labelframe, astreEntryVal, *options_list, command=on_option_change )
    question_menu.config(bg="#E4E2E2", fg="#000")
    question_menu.grid(column=1, row=0, sticky="w")

    labelframe.grid(column=0, row=0)
    labelframe.pack()

def create_Visee_ui(main):
    frame = tk.Frame(
        master = main,
        relief = tk.RAISED,
        borderwidth = 1
    )
    frame.grid(row=3, column=0, sticky="w", padx=2, pady=2)
    labelframe = tk.LabelFrame(frame, text = "Visée", bg="#FC9ADB")  

    labelCollimation = tk.Label(labelframe, text = "Collimation:", **default_label_style)  
    labelCollimation.grid(column=0, row=0, sticky="e")

    entryCollimation = tk.Entry(master=labelframe, textvariable=collimationEntryVal,**default_Entry_style)
    entryCollimation.config(bg="#fff", fg="#000", width=8)
    entryCollimation.grid(column=1, row=0, sticky="w")

    labelOeil = tk.Label(labelframe, text = "Hauteur oeil (m):", **default_label_style)  
    labelOeil.grid(column=0, row=1, sticky="e")

    entryOeil = tk.Entry(master=labelframe, textvariable=hauteurOeilEntryVal,**default_Entry_style)
    entryOeil.config(bg="#fff", fg="#000", width=8)
    entryOeil.grid(column=1, row=1, sticky="w")


    labelTypeVisee = tk.Label(labelframe, text = "Type:", **default_label_style)  
    labelTypeVisee.grid(column=0, row=2, sticky="e")
    
    options_list = []
    defaut = ""
    options_list_values = cEphemerides.getViseeInfo()
    max = len(options_list)
    i = 1
    for x in options_list_values:
        print (f"Init visee : {i:03d}/{max:03d} - {x["nom"]}", end = "\r")
        if x["nom"].startswith("inf"):
            defaut = x["nom"]
        options_list.append(x["nom"])
        i +=1
    print ("\n")

    # rien dand la config je l'init
    if typeViseeEntryVal.get() == "":
        typeViseeEntryVal.set(defaut)
    question_menu = tk.OptionMenu(labelframe, typeViseeEntryVal, *options_list, command=on_option_change)
    question_menu.config(bg="#E4E2E2", fg="#000")
    question_menu.grid(column=1, row=2, sticky="w")

    labelHi = tk.Label(labelframe, text = "Hauteur Hi:", **default_label_style)  
    labelHi.grid(column=0, row=3, sticky="e")

    entryHi = tk.Entry(master=labelframe, textvariable=hauteurInstrumentaleEntryVal, **default_Entry_style)
    entryHi.config(bg="#fff", fg="#000", width=8)
    entryHi.grid(column=1, row=3, sticky="w")

    labelframe.grid(column=0, row=0)
    labelframe.pack()

def create_Output(main):
    global zoneOutput

    frame = tk.Frame(
        master = main,
        relief = tk.RAISED,
        borderwidth = 1
    )
    frame.grid(row=4, column=0, sticky="we", padx=2, pady=2)
    labelframe = tk.LabelFrame(frame, text = "Résultats", bg="#FC9ADB")  

    vertscroll = tk.Scrollbar(labelframe)
    textbox = tk.Text(labelframe, wrap = 'word',width=100, height=40, **default_txtbox_style)
    zoneOutput = textbox
    zoneOutput.insert(END, "test")

    textbox.config(yscrollcommand=vertscroll.set)
    vertscroll.config(command=textbox.yview)

    textbox.grid(column=0, row=0, sticky="we")
    vertscroll.grid(column=1, row=0,sticky="wens")

    labelframe.grid(column=0, row=0)
    labelframe.pack()

def create_buttons(main):
    global buttonCompute
    frame = tk.Frame(
        master = main,
        relief = tk.RAISED,
        borderwidth = 1
    )
    frame.grid(row=5, column=0)
    labelframe = tk.LabelFrame(frame, text = "Management", bg="#FC9ADB")  

    button = tk.Button(master=labelframe, text="Pour test")
    button.config(bg="#E4E2E2", fg="#000")
    button.bind("<Button-1>", onClick)
    button.grid(column=0, row=0)

    buttonQ = tk.Button(master=labelframe, text="Exit")
    buttonQ.config(bg="#E4E2E2", fg="#000")
    buttonQ.bind("<Button-1>", onClickButtonQ)
    buttonQ.grid(column=1, row=0)

    buttonCompute = tk.Button(master=labelframe, text="Calcul !")
    buttonCompute.config(bg="#E4E2E2", fg="#000")
    buttonCompute.bind("<Button-1>", onClickButtonE)
    buttonCompute.grid(column=2, row=0)
    buttonCompute["state"] = "disabled"
    if isValideForCompute() :
        buttonCompute["state"] = "normal"

    labelframe.grid(column=0, row=0)
    labelframe.pack()




main = tk.Tk()
main.title("Nav astro by Nounours")
main.config(bg="#F7F8F8")

# Pour que la grille occupe 100% de la fenêtre, il faut que tkinter
# connaisse à l'avance le nombre de lignes et le nombre de colonnes.
# Le paramètre weight indique que chaque colonne (et chaque ligne)
# à le même poids. L'une ne prendra pas l'avantage sur l'autre.
main.grid_rowconfigure(0, weight=1)
main.grid_rowconfigure(1, weight=1)
main.grid_columnconfigure(0, weight=1)
main.grid_columnconfigure(1, weight=1)
main.grid_columnconfigure(2, weight=1)
main.grid_columnconfigure(3, weight=1)


#default_font = tkFont.nametofont("Terminal", main)
#default_font.configure(size=12)
#main.option_add("*Font", default_font)



# ensemble des inputs du dialogue
latitudeEntryVal : tk.StringVar = tk.StringVar(main, value="")
longitudeEntryVal : tk.StringVar = tk.StringVar(main, value="")
dateEntryVal : tk.StringVar = tk.StringVar(main, value="")
heureEntryVal : tk.StringVar = tk.StringVar(main, value="")
chronosEntryVal : tk.StringVar = tk.StringVar(main, value="")
decalageUTCEntryVal : tk.StringVar = tk.StringVar(main, value="")

hauteurOeilEntryVal : tk.StringVar = tk.StringVar(main, value="")
astreEntryVal : tk.StringVar = tk.StringVar(main, value="")
collimationEntryVal : tk.StringVar = tk.StringVar(main, value="")
typeViseeEntryVal : tk.StringVar = tk.StringVar(main, value="")
hauteurInstrumentaleEntryVal : tk.StringVar = tk.StringVar(main, value="")

zoneOutput : tk.Text = None
buttonCompute : tk.Button = None

initFromConfigFile(main)

create_position_ui(main)
create_Heure_ui(main)
create_Astre_ui(main)
create_Visee_ui(main)
create_Output(main)
create_buttons(main)

main.mainloop()
