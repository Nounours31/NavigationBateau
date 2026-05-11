from logging import root
import tkinter as tk
from tkinter import ttk, Tk



class cMyGUI:
    _instance : cMyGUI
    _initialized : bool = False

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

        self.cap : float | None = None
        self.capAsEntry : ttk.Entry 
        self.outputAsText : tk.Text 
        self.output = ""

    def valideCap(self):
        self.output = "rien fait \n"
        try:
            if self.capAsEntry is not None:
                self.cap = float(self.capAsEntry.get())
                self.output += f"Cap valide: {self.cap}\n"
        except ValueError:
            self.cap = None
        
        if self.cap is None:
            self.output += f"Cap invalide: {self.capAsEntry.get() if self.capAsEntry is not None else 'Inconnu'} - keep: {self.cap}\n"

        self.addOutputInfo(self.output, True) # insert new text

    def getCap(self) -> float | None:
        return self.cap

    def setCap(self, value: float | None) :
        self.cap = value
    
    def addOutputInfo(self, txt: str|None, clear: bool = False):
        if txt is not None:
            self.output += txt + "\n"
            if clear:
                self.output = txt + "\n"
        try:  
            if clear:
                self.outputAsText.delete("1.0", tk.END) # clear text
            self.outputAsText.insert(tk.END, self.output) # insert new text
        except Exception:
            pass


    def drawframe1(self, frame: ttk.Frame):
        frame['padding'] = (1,1,1,1) # frame['padding'] = (left, top, right, bottom)
        frame['borderwidth'] = 1
        frame['relief'] = 'solid' # flat, groove, raised, ridge, solid, or sunken
        
        frame.rowconfigure(0, weight=1) # label / input
        frame.rowconfigure(1, weight=1)
        frame.columnconfigure(0, weight=10)
        frame.columnconfigure(1, weight=30)
        frame.columnconfigure(2, weight=10)

        username_label = ttk.Label(frame, text="Cap (9.99°):")
        username_label.grid(column=0, row=0, sticky=tk.EW, padx=1, pady=1)

        self.capAsEntry = ttk.Entry(frame)
        self.capAsEntry.grid(column=1, row=0, sticky=tk.EW, padx=1, pady=1)

        # password
        password_label = ttk.Label(frame, text="Test later:")
        password_label.grid(column=0, row=1, sticky=tk.EW, padx=1, pady=1)

        password_entry = ttk.Entry(frame)
        password_entry.grid(column=1, row=1, sticky=tk.EW, padx=1, pady=1)

        # login button
        login_button = ttk.Button(frame, text="Valide", command=self.valideCap)
        login_button.grid(column=2, row=1, sticky=tk.E, padx=1, pady=1)

    def drawframe2(self, frame: ttk.Frame):
        frame['padding'] = (1,1,1,1) # frame['padding'] = (left, top, right, bottom)
        frame['borderwidth'] = 1
        frame['relief'] = 'solid' # flat, groove, raised, ridge, solid, or sunken
        
        frame.rowconfigure(0, weight=100) # label / input
        frame.rowconfigure(1, weight=1) # label / input
        frame.columnconfigure(0, weight=100)
        frame.columnconfigure(1, weight=1)

        # Horizontal scrollbar
        x_scroll = ttk.Scrollbar(frame, orient="horizontal")
        x_scroll.grid(column=0, row=1, sticky=tk.EW)

        # Vertical scrollbar
        y_scroll = ttk.Scrollbar(frame, orient="vertical")
        y_scroll.grid(column=1, row=0, sticky=tk.NS)

        # Text widget
        self.outputAsText = tk.Text(
            frame,
            wrap="none",  # disables line wrapping
            width=50,
            xscrollcommand=x_scroll.set,
            yscrollcommand=y_scroll.set
        )

        self.outputAsText.grid(column=0, row=0, sticky=tk.NSEW)

        # Connect scrollbars
        x_scroll.config(command=self.outputAsText.xview)
        y_scroll.config(command=self.outputAsText.yview)

        # Example long line
        cMyGUI.__outputText = self.outputAsText



    def draw(self):
        # main window
        root = tk.Tk()
        root.title('iPad simulator')


        # grid 3x2
        root.rowconfigure(0, weight=1)  # label / input
        root.rowconfigure(1, weight=50) # output
        root.columnconfigure(0, weight=1)


        # username
        frame1 = ttk.Frame(root)
        frame1.grid(column=0, row=0, sticky=tk.NSEW)
        self.drawframe1(frame1)

        frame2 = ttk.Frame(root)
        frame2.grid(column=0, row=1, sticky=tk.NSEW)
        self.drawframe2(frame2)


        root.mainloop()

