file = open("exp", "r")
res = open("instant_stdin.txt", "w")

lines = file.read().split("\n")[:-1]
for line in lines:
    if ']' not in line:
        continue
    res.write(line[(line.index(']') + 1):] + "\n")

file.close()
res.close()
