# coding: utf-8

# https://koor.fr/Python/Tutoriel_Tkinter/tkinter_layout_pack.wp

from tkinter import * 

class myUI:
    def draw_window(self):
        w : myWindow = MyWindow()
        w.draw_window() 


class MyWindow(Tk):
    def __init__(self):
        # On appelle le constructeur parent
        super().__init__()

        # On positionne un premier label en haut
        first_label = Label(self, text="side='top'", fg="white", bg="#FF00FF")
        first_label.pack(side="top")

        # On positionne un second label en bas
        second_label = Label(self, text="side='bottom'", fg="white", bg="green")
        second_label.pack(side="bottom")

        # On positionne un troisième label à gauche
        third_label = Label(self, text="side='left'", fg="white", bg="red")
        third_label.pack(side="left")

        # On positionne un quatrième label à droite
        fourth_label = Label(self, text="side='right'", fg="white", bg="orange")
        fourth_label.pack(side="right")

        # On dimensionne la fenêtre (500 pixels de large par 200 de haut).
        self.geometry("500x200")

        # On injecte un premier label dans la fenêtre
        label = Label(self, text="Hello Tk")
        label.pack()

        # Puis, on injecte un bouton dans la fenêtre. En cas de clic, il est
        # connecté au gestionnaire d'événements do_something.
        button = Button(self, text="Push me !", command=self.do_something)
        button.pack()

        # On dimensionne la fenêtre (300 pixels de large par 200 de haut).
        self.geometry("300x200")

        # On ajoute un titre à la fenêtre
        self.title("Hello")

    # Le gestionnaire d'événement pour notre bouton.
    def do_something(self):
        print("Button clicked!")



    def draw_window(self):
        fenetre = Tk()

        # frame 1
        Frame1 = Frame(fenetre, borderwidth=2, relief=GROOVE)
        Frame1.pack(side=LEFT, padx=30, pady=30)
        Frame1.grid(row=0, column=0)

        # frame 2
        Frame2 = Frame(fenetre, borderwidth=2, relief=GROOVE)
        Frame2.pack(side=LEFT, padx=10, pady=10)
        Frame1.grid(row=1, column=0)

        label = Label(Frame1, text="Hello World", bg="white", fg="black", font="Arial 16" )
        label.pack()
        label.grid(row=0, column=0)

        bouton=Button(Frame1, text="Fermer", command=myUI.valider_cap)
        bouton.pack()
        bouton.grid(row=2, column=0)

        value = StringVar() 
        value.set("texte par défaut")
        entree = Entry(Frame1, textvariable=value, width=30)
        entree.pack()
        entree.grid(row=1, column=0)

        text = Text(Frame2, width=30, height=10)
        text.pack() 
        text.grid(row=0, column=0)

        fenetre.mainloop()

if __name__ == "__main__":
    ui = myUI()
    ui.draw_window()    


