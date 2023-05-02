res = open("res.txt", "r")
resMine = open("resMine.txt", "r")

lineRes = res.read().split('\n')[:-1]
lineMine = resMine.read().split('\n')[:-1]
res.close()
resMine.close()
buildings = ["A", "B", "C", "D", "E"]
resDict = dict.fromkeys(buildings)
mineDict = dict.fromkeys(buildings)

for line in lineRes:
    new_line = line.split("-")
    if resDict.get(new_line[-3]) is not None:
        resDict[new_line[-3]].append(line)
    else:
        resDict[new_line[-3]] = [line]

for line in lineMine:
    new_line = line.split("-")
    if mineDict.get(new_line[-3]) is not None:
        mineDict[new_line[-3]].append(line)
    else:
        mineDict[new_line[-3]] = [line]

if len(lineRes) != len(lineMine):
    print("different length: lengthRes = {}, lengthMine = {}".format(len(lineRes), len(lineMine)))

for b in buildings:
    b_mine = mineDict.get(b)
    b_res = resDict.get(b)
    if not b_res and not b_mine:
        continue
    if len(b_mine) != len(b_res):
        print("different length{}: lengthRes = {}, lengthMine = {}".format(b, len(b_res), len(b_mine)))

    for i in range(min(len(b_res), len(b_mine))):
        if b_res[i][10:] != b_mine[i][10:]:
            print(i)
            print("res: " + b_res[i])
            print("mine: " + b_mine[i])
            print()
