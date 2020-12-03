from datetime import datetime

import pyautogui as pyg
import time
import datetime
from tkinter import *

pyg.FAILSAFE = True

minetime = 12
points = []
t1 = datetime.datetime.now().replace(microsecond=0)

window = Tk()
window.title("CP Auto-Miner")
window.geometry('350x200')
window.resizable(0, 0)
window.pack_propagate(0)
window.wm_attributes("-topmost", 1)


def clicknmovendance(x, y, t):
    pyg.moveTo(x, y, duration=0.5)
    pyg.click()
    time.sleep(3)
    pyg.typewrite('d')
    time.sleep(t)


def getclicklocation(event):
    if len(points) < 6:
        points.append((pyg.position().x, pyg.position().y))
        T.config(state=NORMAL)
        T.insert(END, "Point " + str(len(points)) + ": " + str(points[len(points) - 1][0]) + ", " + str(points[len(points) - 1][1]) + "\n")
        T.config(state=DISABLED)


def automine():
    exitted = False
    pyg.moveRel(60, 0, duration=0.5)
    pyg.click()
    global t1
    t1 = gettime()
    if len(points) > 0:
        while not exitted:
            for x in points:
                clicknmovendance(x[0], x[1], minetime)


def calctime():
    t2 = gettime()
    T.insert(END, "\nTime Elapsed: " + str(t2 - t1))


def gettime():
    return datetime.datetime.now().replace(microsecond=0)


B = Button(window, fg='red', padx=10, pady=10, text="Quit", command=quit).pack(side=LEFT, fill=Y)
R = Button(window, fg='green', padx=10, pady=10, text="Execute", command=automine).pack(side=RIGHT, fill=Y)
C = Button(window, fg='gray', padx=10, pady=10, text="Calculate Time", command=calctime).pack(side=BOTTOM)
T = Text(window)
T.insert(END, "Please select six mining points by pressing P... "
              "after executing, move mouse to top left corner to stop automation\n\n")
T.tag_add("here", "1.0", "1.114")
T.tag_config("here", foreground="BLUE")
T.pack(side=TOP)
T.config(state=DISABLED)
window.bind('<Escape>', quit)
window.bind('<p>', getclicklocation)

window.mainloop()


