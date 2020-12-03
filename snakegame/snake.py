from tkinter import *
import time
import random
from PIL import Image, ImageTk

win = Tk()
win.title("Snake")
win.wm_attributes("-topmost",1)
global starter
starter = False
global cont
cont = False
global width
global height
width = 40
height = 30
global gridsize
gridsize = 20
global wide
global high
wide = width*gridsize + 1
high =height*gridsize +1
canvas = Canvas(win, width=wide, height=high)
global dead
dead = False
global offsetx
global offsety
global length
length = 0
offsetx = gridsize
offsety = 0
length = 1
global snek
snek = [[10],[10]]
global tail
tail = [[0],[0]]
global food
food = [random.randint(0,width-1), random.randint(0,height-1)]

path = "snakehome.png"
img = ImageTk.PhotoImage(Image.open(path).resize((wide, high)))
canvas.create_image(0,0, anchor=NW, image=img)
canvas.create_text(wide - wide/6.7, high/3 + 10,fill="black",font="rumpelstiltskin 40 bold", text=" Press \n Enter \n  to  \n Start")
canvas.create_text(wide - wide/7, high/2 + 20 ,fill="brown",font="rumpelstiltskin 15 italic bold", text="created by Lars Fyhr")

def addpiece():
    global length
    snek[0].append(snek[0][length-1])
    snek[1].append(snek[1][length-1])
    length = length + 1
    canvas.create_rectangle(wide - 50, high - 50, wide, high, fill='white', outline='white')
    canvas.create_text(wide - 10, high - 10, fill="black", font="rumpelstiltskin 14 italic bold",
                       text=length)

def snakeread(length):
    firstx = (snek[0][0] * gridsize) + 3
    firsty = (snek[1][0] * gridsize) + 3
    lastx = (snek[0][length - 1] * gridsize) + 3
    lasty = (snek[1][length - 1] * gridsize) + 3
    canvas.create_rectangle(firstx, firsty, firstx + gridsize, firsty + gridsize, fill='green')
    canvas.create_rectangle(lastx, lasty, lastx + gridsize, lasty + gridsize, fill= 'green')
    global tail
    tailx = (tail[0][0] * gridsize) + 3
    taily = (tail[1][0] * gridsize) + 3
    canvas.create_rectangle(tailx, taily, tailx + gridsize, taily + gridsize, fill='white', outline='white')

def updatepieces():
    for q in range(length - 1, 0, -1):
        snek[0][q] = snek[0][q-1]
        snek[1][q] = snek[1][q-1]

def collided():
    for q in range(2, length):
        if q > 1:
            if snek[0][q] == snek[0][0]:
                if snek[1][q] == snek[1][0]:
                    global dead
                    dead = True
    if snek[0][0] > width-1 or snek[0][0] < 0:
        dead = True
    if snek[1][0] > height-1 or snek[1][0] < 0:
        dead = True

def atefood():
    if snek[0][0] == food[0]:
        if snek[1][0] == food[1]:
            addpiece()
            badpiece = True
            while badpiece:
                food[0] = random.randint(0,width-1)
                food[1] = random.randint(0,height-1)
                badpiece = False
                for p in range(length):
                    if food[0] == snek[0][p]:
                         if food[1] == snek[1][p]:
                             badpiece = True
            canvas.create_rectangle(food[0]*gridsize + 3, food[1]*gridsize + 3, food[0]*gridsize + gridsize+3, food[1]*gridsize + gridsize+3, fill='red', outline='red')

def restart():
    global cont
    global length
    canvas.create_text(wide / 2, high / 4, fill="red", font="rumpelstiltskin 50 bold",
                       text="Final Score: " + str(length))
    canvas.create_text(wide / 2, high / 2, fill="black", font="rumpelstiltskin 50 bold",
                       text="        Try Again? \n Press Return to Restart")
    canvas.pack()
    while not cont:
        canvas.update()
    cont = False
    canvas.delete(ALL)
    global dead
    dead = False
    global snek
    global tail
    global score
    snek = [[10], [10]]
    tail = [[0], [0]]
    length = 1
    score = 0
    global food
    food = [random.randint(0, width - 1), random.randint(0, height - 1)]
    canvas.create_rectangle(food[0] * gridsize + 3, food[1] * gridsize + 3, food[0] * gridsize + gridsize + 3,
                            food[1] * gridsize + gridsize + 3, fill='red', outline='red')
    canvas.create_text(wide - 10, high - 10, fill="black", font="rumpelstiltskin 10 italic bold",
                       text=length)
    gameloop()

def start(event):
    global starter
    global cont
    if starter == False:
        canvas.delete(ALL)
        starter = True
        canvas.create_rectangle(food[0] * gridsize + 3, food[1] * gridsize + 3, food[0] * gridsize + gridsize + 3,
                                food[1] * gridsize + gridsize + 3, fill='red', outline='red')
        gameloop()
    if starter == True:
        cont = True

def moveright(event):
    global offsetx
    if offsetx != -gridsize:
        global offsety
        offsetx = gridsize
        offsety = 0

def moveleft(event):
    global offsetx
    if offsetx != gridsize:
        global offsety
        offsetx = -gridsize
        offsety = 0

def moveup(event):
    global offsety
    if offsety != gridsize:
        global offsetx
        offsetx = 0
        offsety = -gridsize

def movedown(event):
    global offsety
    if offsety != -gridsize:
        global offsetx
        offsetx = 0
        offsety = gridsize

#def setgrid():
#    for i in range(20):
#        x = (i * 50) + 3
#        for z in range (14):
#            y = (z * 50) + 3
#            canvas.create_rectangle(x, y, x+50, y+50, fill='white')

win.bind("<Right>", moveright)
win.bind("<Left>", moveleft)
win.bind("<Up>", moveup)
win.bind("<Down>", movedown)
win.bind("<Return>", start)

def gameloop():
    #win.update_idletasks()
    canvas.create_text(wide - 10, high - 10, fill="black", font="rumpelstiltskin 14 italic bold",
                       text=length)
    while dead == False:
        canvas.pack()
        win.update()
        snakeread(length)
        tail[0][0] = snek[0][length-1]
        tail[1][0] = snek[1][length-1]
        atefood()
        collided()
        updatepieces()
        snek[0][0] = snek[0][0] + offsetx/gridsize
        snek[1][0] = snek[1][0] + offsety/gridsize
        time.sleep(0.05)
    if dead == True:
        restart()

while starter == False:
    canvas.pack()
    win.update()

