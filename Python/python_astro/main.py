# This code is generated using PyUIbuilder: https://pyuibuilder.com

import os
import tkinter as tk
from tools.cAngle import cAngle
from tools.cEphemerides import cEphemerides

# https://python-course.eu/tkinter/buttons-in-tkinter.php


def onClick(e: tk.EventType) :
    x : str = entry.get()
    y : str = value_inside.get()
    z : cAngle = cAngle()
    z.parse(x)
    print(f"Single Click, Button-l - >{x:s}< >{y:s}< >{z.toString():s}<") 

def onClickButtonE(e: tk.EventType) :
    print(f"Single Click, Button-l") 
    z : cEphemerides = cEphemerides()
    z.get_val()


def onClickButtonQ(e: tk.EventType) :
    print(f"Single Click, Button-l") 
    main.destroy
    import sys
    sys.exit()
    print(f"destroy ?") 

BASE_DIR = os.path.dirname(os.path.abspath(__file__))

main = tk.Tk()
main.title("Nav astro by Nounours")
main.config(bg="#E4E2E2")
main.geometry("700x400")

label = tk.Label(master=main, text="Heure")
label.config(bg="#E4E2E2", fg="#000")
label.place(x=17, y=19, height=40)

entry = tk.Entry(master=main)
entry.config(bg="#fff", fg="#000")
entry.place(x=131, y=27, width=164, height=34)

options_list = ["Option 1", "Option 2", "Option 3", "Option 4"]
value_inside = tk.StringVar(main)
value_inside.set("Select an Option")
question_menu = tk.OptionMenu(main, value_inside, *options_list)
question_menu.config(bg="#E4E2E2", fg="#000")
question_menu.place(x=159, y=149)
# question_menu.pack()

button = tk.Button(master=main, text="Button")
button.config(bg="#E4E2E2", fg="#000")
button.place(x=12, y=324, height=40)
button.bind('<Button-1>', onClick)

buttonQ = tk.Button(master=main, text="Exit")
buttonQ.config(bg="#E4E2E2", fg="#000")
buttonQ.place(x=62, y=324, height=40)
buttonQ.bind('<Button-1>', onClickButtonQ)

buttonE = tk.Button(master=main, text="Ephemeride")
buttonE.config(bg="#E4E2E2", fg="#000")
buttonE.place(x=122, y=324, height=40)
buttonE.bind('<Button-1>', onClickButtonE)

main.mainloop()