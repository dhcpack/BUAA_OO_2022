file = open("Astdin.txt", "r")
res = open("instant.txt", "w")

lines = file.read().split("\n")[:-1]
for line in lines:
    res.write(line[(line.index(']') + 1):] + "\n")

file.close()
res.close()
