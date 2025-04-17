# import the library
from appJar import gui


class CUI:
    def __init__(self):
        pass

    def press(self, button):
        if button == "Cancel":
            self.app.stop()
        else:
            usr = self.app.getEntry("Username")
            pwd = self.app.getEntry("Password")
            print("User:", usr, "Pass:", pwd)

    def init(self):
        # create a GUI variable called app
        self.app = gui("Login Window", "400x200")
        self.app.setBg("orange")
        self.app.setFont(18)

        # add & configure widgets - widgets get a name, to help referencing them later
        self.app.addLabel("title", "Welcome to appJar")
        self.app.setLabelBg("title", "blue")
        self.app.setLabelFg("title", "orange")

        self.app.addLabelEntry("Username")
        self.app.addLabelSecretEntry("Password")

        # link the buttons to the function called press
        self.app.addButtons(["Submit", "Cancel"], self.press)

        self.app.setFocus("Username")

        # start the GUI
        self.app.go()
