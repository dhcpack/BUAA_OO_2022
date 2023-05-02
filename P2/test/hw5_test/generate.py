import os
import random


def create(num):
    personIdPool = set()
    buildingList = ['A', 'B', 'C', 'D', 'E']

    current_time = 0.0
    file = open("stdin.txt", "w")

    for i in range(num):
        personId = random.randint(0, 1000)
        while personId in personIdPool:
            personId = random.randint(0, 1000)
        personIdPool.add(personId)
        building = random.choice(buildingList)
        start = random.randint(1, 10)
        end = random.randint(1, 10)
        while end == start:
            end = random.randint(1, 10)
        current_time = current_time + random.randint(0, 10) / 10
        request = "[" + str(current_time)[:str(current_time).index(".") + 2] + "]" + str(
            personId) + "-" + "FROM" + "-" + building + "-" + str(start) + "-TO-" + str(building) + "-" + str(
            end) + "\n"
        # request = str(personId) + "-" + "FROM" + "-" + building + "-" + str(start) + "-TO-" + str(building) + "-" + str(
        #     end) + "\n"
        file.write(request)

    file.close()


def filiter(b: str):
    stdin = open("stdin.txt", "r")
    resMine = open("resMine.txt", "r")
    res = open("res.txt", "r")

    lineStdin = stdin.read().split('\n')[:-1]
    lineRes = res.read().split('\n')[:-1]
    lineMine = resMine.read().split('\n')[:-1]

    b_stdin = open(b + "stdin.txt", "w")
    b_resMine = open(b + "resMine.txt", "w")
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


def compare():
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
        flag = True
        if not b_res and not b_mine:
            continue
        if len(b_mine) != len(b_res):
            print("\ndifferent length{}: lengthRes = {}, lengthMine = {}\n".format(b, len(b_res), len(b_mine)))
        for i in range(min(len(b_res), len(b_mine))):
            if b_res[i][10:] != b_mine[i][10:]:
                print(i)
                print("res: " + b_res[i])
                print("mine: " + b_mine[i])
                print()
                flag = False
        if not flag:
            filiter(b)
            print(b + " different. filiter succeed!")


if __name__ == "__main__":
    refresh = input("refresh(y or n): ")
    if refresh == 'y':
        num = int(input("指令条数："))
        create(num)
        print("test point create succeed!")
    else:
        print("using testpoint before.")
    os.system("datainput_student_win64.exe | java -jar P2_version2.jar > resMine.txt")
    print("resMine get!")
    os.system("datainput_student_win64.exe | java -jar code.jar > res.txt")
    print("res get!")
    compare()
    print("compare succeed!")
