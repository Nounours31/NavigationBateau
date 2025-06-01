import tkinter as tk

master = tk.Tk()
master.title('NMEA Simulateur pour SimSail par socket')
tk.Label(master, text='First Name').grid(row=0)
tk.Label(master, text='Last Name').grid(row=1)
button = tk.Button (master, text="Go !", width=25, command=master.destroy)
button.grid(row=2, column=1)

e1 = tk.Entry(master)
e2 = tk.Entry(master)

e1.grid(row=0, column=1)
e2.grid(row=1, column=1)



tk.mainloop()
