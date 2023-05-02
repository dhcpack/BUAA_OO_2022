stdin = open("stdin.txt", "r")
resMine = open("resLOOK.txt", "r")
res = open("res.txt", "r")

lineStdin = stdin.read().split('\n')[:-1]
lineRes = res.read().split('\n')[:-1]
lineMine = resMine.read().split('\n')[:-1]

b = input("input building: ")

b_stdin = open(b + "stdin.txt", "w")
b_resMine = open(b + "resLOOK.txt", "w")
b_res = open(b + "res.txt", "w")

for line in lineStdin:
    new_line = line.split("-")
    if new_line[-2] == b:
        b_stdin.write(line + "\n")

for line in lineRes:
    new_line = line.split("-")
    if new_line[-3] == b:
        b_res.write(line + "\n")

for line in lineMine:
    new_line = line.split("-")
    if new_line[-3] == b:
        b_resMine.write(line + "\n")

stdin.close()
resMine.close()
res.close()
b_stdin.close()
b_resMine.close()
b_res.close()
