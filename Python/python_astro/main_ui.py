# This code is generated using PyUIbuilder: https://pyuibuilder.com

import os
import tkinter as tk
from tkinter import ttk

from tools.cAstroError import cAstroError
from tools.cAngle import cAngle
from tools.cEphemerides import cEphemerides
from tools.cAstroError import cAstroError

# https://python-course.eu/tkinter/buttons-in-tkinter.php
# https://www.studytonight.com/tkinter/python-tkinter-labelframe-widget

def onClick(e: tk.EventType):
    x: str = entry.get()
    y: str = value_inside.get()
    z: cAngle = cAngle()
    z.parse(x)
    print(f"Single Click, Button-l - >{x:s}< >{y:s}< >{z.toString():s}<")


def onClickButtonE(e: tk.EventType):
    print("Single Click, Button-l")
    z: cEphemerides = cEphemerides()
    z.get_val()


def onClickButtonQ(e: tk.EventType):
    print("Single Click, Button-l")
    main.destroy
    import sys

    sys.exit()
    print("destroy ?")


def create_text(master):
        textbox = tk.Text(master, height = 10, width = 79, wrap = 'word')
        vertscroll = ttk.Scrollbar(master)
        vertscroll.config(command=textbox.yview)
        textbox.config(yscrollcommand=vertscroll.set)
        textbox.grid(column=0, row=0)
        vertscroll.grid(column=1, row=0, sticky='NS')

BASE_DIR = os.path.dirname(os.path.abspath(__file__))

main = tk.Tk()
main.title("Nav astro by Nounours")
main.config(bg="#F7F8F8")
#main.geometry("700x400")


for i in range(1,5):
    for j in range(1,3):
        frame = tk.Frame(
            master = main,
            relief = tk.RAISED,
            borderwidth = 1
        )
        frame.grid(row=i, column=j)
        label = tk.Label(master=frame, text=f"Row {i}\nColumn {j}")
        label.pack()

create_text (main)

frm = tk.Frame(master = main)
frm.grid(row=6,column=1)

labelframe1 = tk.LabelFrame(master=main, text="Happy Thoughts!!!")  
toplabel = tk.Label(master=labelframe1, text="You can put your happy thoughts here")  
w = tk.Entry(master=labelframe1, text="toto")


labelframe2 = tk.LabelFrame(main, text = "Changes You want!!", bg="#9AE8FC")  
bottomlabel = tk.Label(labelframe2, text = "You can put here the changes you want,If any!")  
bottomlabel.grid(column=1, row=12)
labelframe2.grid(column=0, row=12)

label = tk.Label(master=main, text="Heure")
label.config(bg="#E4E2E2", fg="#000")
#label.place(x=17, y=19, height=40)
label.grid(column=0, row=11)

entry = tk.Entry(master=main)
entry.config(bg="#fff", fg="#000")
#entry.place(x=131, y=27, width=164, height=34)
entry.grid(column=1, row=11)

options_list = ["Option 1", "Option 2", "Option 3", "Option 4"]
value_inside = tk.StringVar(main)
value_inside.set("Select an Option")
question_menu = tk.OptionMenu(main, value_inside, *options_list)
question_menu.config(bg="#E4E2E2", fg="#000")
# question_menu.place(x=159, y=149)
# question_menu.pack()
question_menu.grid(column=0, row=10)

button = tk.Button(master=main, text="Button")
button.config(bg="#E4E2E2", fg="#000")
# button.place(x=12, y=324, height=40)
button.bind("<Button-1>", onClick)
button.grid(column=0, row=9)

buttonQ = tk.Button(master=main, text="Exit")
buttonQ.config(bg="#E4E2E2", fg="#000")
# buttonQ.place(x=62, y=324, height=40)
buttonQ.bind("<Button-1>", onClickButtonQ)
buttonQ.grid(column=0, row=7)

buttonE = tk.Button(master=main, text="Ephemeride")
buttonE.config(bg="#E4E2E2", fg="#000")
# buttonE.place(x=122, y=324, height=40)
buttonE.bind("<Button-1>", onClickButtonE)
buttonE.grid(column=0, row=8)

main.mainloop()
