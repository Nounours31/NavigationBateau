from __future__ import annotations
import tkinter as tk
from tkinter import ttk



class cMyGUI:
    FRAME_4_IPAD_MESSAGE : int = 1
    FRAME_4_VECTEUR_ETAT : int = 2

    _instance : cMyGUI
    _initialized : bool = False
    _isDrawned : bool = False

    @staticmethod
    def getInstance() -> cMyGUI:
        if not hasattr(cMyGUI, "_instance"):
            cMyGUI._instance = cMyGUI()
        return cMyGUI._instance
    
    def __new__(cls):
        if not hasattr(cls, "_instance"):
            cls._instance = super().__new__(cls)
        return cls._instance

    def __init__(self, value=None):
        if not self._initialized:
            self.value = value
            cMyGUI._initialized = True
            cMyGUI._isDrawned = False

        # info as float
        self.cap : float | None = None
        self.vitesse : float | None = None

        # info as ttk.Entry
        self.capAsEntry : ttk.Entry 
        self.vitesseAsEntry : ttk.Entry 

        # zone de text output
        self.outputAsTextForVecteurEtat : tk.Text 
        self.outputAsTextForVecteurEtatTxt = ""
        self.outputAsTextForIpadMessage : tk.Text 
        self.outputAsTextForIpadMessageTxt = ""

        

    def valideCap(self):
        outputTxt : str = ""
        try:
            if self.capAsEntry is not None:
                if len(self.capAsEntry.get()) > 0:
                    self.cap = float(self.capAsEntry.get())
            if self.vitesseAsEntry is not None:
                if len(self.vitesseAsEntry.get()) > 0:
                    self.vitesse = float(self.vitesseAsEntry.get())
            outputTxt += f"Cap lu:      {self.cap}\n"
            outputTxt += f"Vitesse lue: {self.vitesse}\n"
        except ValueError:
            self.cap = None
            self.vitesse = None
        
        if self.cap is None:
            outputTxt += f"Cap invalide: {self.capAsEntry.get() if self.capAsEntry is not None else 'Inconnu'} - keep: {self.cap}\n"
        if self.vitesse is None:
            outputTxt += f"Vitesse invalide: {self.vitesseAsEntry.get() if self.vitesseAsEntry is not None else 'Inconnu'} - keep: {self.vitesse}\n"

        self.addVecteurEtatInfo(outputTxt, True) # insert new text
        
        self.capAsEntry.delete(0, tk.END) # clear entry
        self.vitesseAsEntry.delete(0, tk.END) # clear entry


    def resetAll(self):
        self.output = "rien fait \n"
        self.cap = None
        self.vitesse = None
        self.capAsEntry.delete(0, tk.END) # clear entry
        self.vitesseAsEntry.delete(0, tk.END) # clear entry


    def getCap(self) -> float | None:
        return self.cap

    def getVitesse(self) -> float | None:
        return self.vitesse

    def setCap(self, value: float | None) :
        self.cap = value

    def setVitesse(self, value: float | None) :
        self.vitesse = value

    def addIPadInfo(self, txt: str|None, clear: bool = False):
        self.addTxtInfo(txt, clear, qui = cMyGUI.FRAME_4_IPAD_MESSAGE)

    def addVecteurEtatInfo(self, txt: str|None, clear: bool = False):
        self.addTxtInfo(txt, clear, qui = cMyGUI.FRAME_4_VECTEUR_ETAT)

    def addTxtInfo(self, txt: str|None, clear: bool = False, qui: int = -1):
        if not cMyGUI._isDrawned:
            return
        
        output : str = ""
        if txt is None:
            return
        
        pipo : tk.Text | None = None
        if qui == cMyGUI.FRAME_4_IPAD_MESSAGE:
            pipo = self.outputAsTextForIpadMessage
        elif qui == cMyGUI.FRAME_4_VECTEUR_ETAT:
            pipo = self.outputAsTextForVecteurEtat
        else:
            return
        
        if clear:
            output = txt
        else:
            output = pipo.get("1.0", tk.END) + txt
        try:  
            pipo.delete("1.0", tk.END) # clear text
            pipo.insert(tk.END, output) # insert new text
        except Exception:
            pass


    def drawframe1(self, frame: ttk.Frame):
        frame['padding'] = (1,1,1,1) # frame['padding'] = (left, top, right, bottom)
        frame['borderwidth'] = 1
        frame['relief'] = 'solid' # flat, groove, raised, ridge, solid, or sunken
        
        frame.rowconfigure(0, weight=1) # label / input
        frame.rowconfigure(1, weight=1)
        frame.rowconfigure(2, weight=1)
        frame.columnconfigure(0, weight=10)
        frame.columnconfigure(1, weight=30)
        frame.columnconfigure(2, weight=10)

        # le cap a envoyer a l'iPad
        cap_label = ttk.Label(frame, text="Cap (9.99°):")
        cap_label.grid(column=0, row=0, sticky=tk.EW, padx=1, pady=1)
        self.capAsEntry = ttk.Entry(frame)
        self.capAsEntry.grid(column=1, row=0, sticky=tk.EW, padx=1, pady=1)

        # la vitesse a envoyer a l'iPad
        vitesse_label = ttk.Label(frame, text="Vitesse (9.99 nœuds):")
        vitesse_label.grid(column=0, row=1, sticky=tk.EW, padx=1, pady=1)
        self.vitesseAsEntry = ttk.Entry(frame)
        self.vitesseAsEntry.grid(column=1, row=1, sticky=tk.EW, padx=1, pady=1)

        # pour test posterieur
        test_label = ttk.Label(frame, text="Test later:")
        test_label.grid(column=0, row=2, sticky=tk.EW, padx=1, pady=1)
        test_entry = ttk.Entry(frame)
        test_entry.grid(column=1, row=2, sticky=tk.EW, padx=1, pady=1)

        # login button
        login_button = ttk.Button(frame, text="Valide", command=self.valideCap)
        login_button.grid(column=2, row=1, sticky=tk.E, padx=1, pady=1)

        # login button
        login_button = ttk.Button(frame, text="Reset Cap/Vitesse", command=self.resetAll)
        login_button.grid(column=2, row=0, sticky=tk.E, padx=1, pady=1)

    def drawframe2(self, frame: ttk.Frame, qui: int = -1):
        frame['padding'] = (1,1,1,1) # frame['padding'] = (left, top, right, bottom)
        frame['borderwidth'] = 1
        frame['relief'] = 'solid' # flat, groove, raised, ridge, solid, or sunken
        
        frame.rowconfigure(0, weight=150) # label / input
        frame.rowconfigure(1, weight=50) # label / input
        frame.columnconfigure(0, weight=100)
        frame.columnconfigure(1, weight=1)

        # Horizontal scrollbar
        x_scroll = ttk.Scrollbar(frame, orient="horizontal")
        x_scroll.grid(column=0, row=1, sticky=tk.EW)

        # Vertical scrollbar
        y_scroll = ttk.Scrollbar(frame, orient="vertical")
        y_scroll.grid(column=1, row=0, sticky=tk.NS)

        # Text widget
        pipo : tk.Text | None = None
        if qui == cMyGUI.FRAME_4_IPAD_MESSAGE:
            self.outputAsTextForIpadMessage = tk.Text(
                frame,
                wrap="none",  # disables line wrapping
                width=50,
                height=100,
                font=("Arial", 8),
                xscrollcommand=x_scroll.set,
                yscrollcommand=y_scroll.set
            )
            pipo = self.outputAsTextForIpadMessage

        elif qui == cMyGUI.FRAME_4_VECTEUR_ETAT:
            self.outputAsTextForVecteurEtat = tk.Text(
                frame,
                wrap="none",  # disables line wrapping
                width=50,
                height=50,
                xscrollcommand=x_scroll.set,
                yscrollcommand=y_scroll.set
            )
            pipo = self.outputAsTextForVecteurEtat

        else:
            raise ValueError("Unknown frame type: " + str(qui))
        
        pipo.grid(column=0, row=0, sticky=tk.NSEW)

        # Connect scrollbars
        x_scroll.config(command=pipo.xview)
        y_scroll.config(command=pipo.yview)


    def draw(self):
        # main window
        root = tk.Tk()
        root.title('iPad simulator')


        # grid 3x2
        root.rowconfigure(0, weight=1)  # label / input
        root.rowconfigure(1, weight=50) # output
        root.rowconfigure(2, weight=50) # output
        root.columnconfigure(0, weight=1)


        # username
        frame1 = ttk.Frame(root)
        frame1.grid(column=0, row=0, sticky=tk.NSEW)
        self.drawframe1(frame1)

        frame2 = ttk.Frame(root)
        frame2.grid(column=0, row=1, sticky=tk.NSEW)
        self.drawframe2(frame2, qui = cMyGUI.FRAME_4_IPAD_MESSAGE)

        frame3 = ttk.Frame(root)
        frame3.grid(column=0, row=2, sticky=tk.NSEW)
        self.drawframe2(frame3, qui = cMyGUI.FRAME_4_VECTEUR_ETAT)

        cMyGUI._isDrawned = True
        root.mainloop()

