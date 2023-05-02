import os
import random

from validator import validator
from checker_hw5 import check_hw5

buildings = ['A', 'B']


def create(i, num):
    personIdPool = set()
    current_time = 0.0
    file = open("stdin.txt", "w")

    for i in range(num):
        personId = random.randint(0, 1000)
        while personId in personIdPool:
            personId = random.randint(0, 1000)
        personIdPool.add(personId)
        building = random.choice(buildings)
        start = random.randint(1, 10)
        end = random.randint(1, 10)
        while end == start:
            end = random.randint(1, 10)
        current_time = current_time + random.randint(0, 20) / 100
        request = "[" + str(current_time) + "]" + str(
            personId) + "-" + "FROM" + "-" + building + "-" + str(start) + "-TO-" + str(building) + "-" + str(
            end) + "\n"
        file.write(request)
    file.close()


class Person:
    def __init__(self, arrive_time, id, start, end):
        self.arrive_time = float(arrive_time)
        self.id = id
        self.start = int(start)
        self.end = int(end)


def getStdin():
    file = open("stdin.txt", "r")
    lineStdin = file.read().split("\n")[:-1]
    file.close()
    stdinDict = dict()
    # 分离五座楼中的人
    for line in lineStdin:
        new_line = line.split("-")
        person = Person(new_line[0][1:new_line[0].index("]")], new_line[0][new_line[0].index("]") + 1:], new_line[3],
                        new_line[6])
        stdinDict.setdefault(new_line[2], set()).add(person)
    return stdinDict


def getRes(filename):
    file = open(filename, "r")
    lineMine = file.read().split('\n')[:-1]
    file.close()
    timeMine = float(lineMine[-1][lineMine[-1].index("[") + 1:lineMine[-1].index("]")])
    mineDict = dict()
    # 分离五个电梯
    for line in lineMine:
        new_line = line.split("-")
        mineDict.setdefault(new_line[-3], []).append(line)
    return timeMine, mineDict


def check(filename):
    stdinDict = getStdin()
    timeMine, mineDict = getRes(filename)

    # logic check
    for b in buildings:
        # print("testing {}".format(b))
        b_mine = mineDict.setdefault(b, None)
        b_person = stdinDict.setdefault(b, None)
        if (not b_mine) and (not b_person):
            continue
        personOnElevator = dict()
        lastTime = 0
        lastFloor = 1
        closed = True
        i = 0
        while i < len(b_mine):
            if not (b_mine[i].find("ARRIVE") == -1 and i == 0):
                new_line = b_mine[i].split("-")
                arrive_time, currentfloor = float(new_line[0][1:10]), int(new_line[-2])
                if not arrive_time - lastTime >= 0.35:
                    raise Exception(
                        "电梯运行时间错误(line: {})：arrive_time = {}, former_time = {}".format(i, arrive_time, lastTime))
                if not abs(currentfloor - lastFloor) == 1:
                    raise Exception(
                        "电梯运行楼层错误(line: {})：current_floor = {}, former_floor = {}".format(i, currentfloor, lastFloor))
                lastTime = arrive_time
                lastFloor = currentfloor
                i += 1
            else:  # 第一层直接开门
                arrive_time, currentfloor = 0, 0
            current_floor_op = []
            while i < len(b_mine) and b_mine[i].find("ARRIVE") == -1:
                current_floor_op.append(b_mine[i])
                i += 1
            open_time, close_time, in_time, out_time = 0, 0, 0, 0

            while current_floor_op:
                new_line = current_floor_op.pop(0).split("-")
                time, op, currentfloor = float(new_line[0][1:10]), new_line[0][11:], int(new_line[-2])
                if op == "OPEN":
                    if not closed:
                        raise Exception("重复开门(near line: {})".format(i))
                    open_time = time
                    closed = False
                    if open_time < arrive_time:
                        raise Exception(
                            "电梯开门时间错误(near line: {})：arrive_time = {}, open_time = {}".format(i, arrive_time,
                                                                                              open_time))
                if op == "CLOSE":
                    if closed:
                        raise Exception("重复关门(near line: {})".format(i))
                    close_time = time
                    closed = True
                    if close_time - open_time < 0.35:
                        raise Exception(
                            "电梯关门时间错误(near line: {})：open_time = {}, close_time = {}".format(i, open_time, close_time))
                if op == "IN":
                    person_id = new_line[1]
                    flag = False
                    for person in b_person:
                        if person.id == person_id:
                            flag = True
                            if time < person.arrive_time:
                                raise Exception(
                                    "人进入电梯时间错误(near line: {})：person_start_time = {}, person_go_into_elevator_time = {}".format(
                                        i, person.arrive_time, time))
                            if closed:
                                raise Exception("elevator closed, person cannot go into(near line: {})".format(i))
                            if currentfloor != person.start:
                                raise Exception(
                                    "人不在该层，不能进入电梯(near line: {})：person_start_floor = {}, current_floor = {}".format(i,
                                                                                                                     person.start,
                                                                                                                     currentfloor))
                            personOnElevator[person_id] = person
                    b_person.remove(personOnElevator[person_id])
                    if not flag:
                        raise Exception("person ID({}) not Found(near line: {})".format(person_id, i))
                    if len(personOnElevator) > 6:
                        raise Exception("超载(near line: {})".format(i))
                if op == "OUT":
                    person_id = new_line[1]
                    if person_id not in personOnElevator:
                        raise Exception("人不在电梯上(near line: {})：person_id = {}".format(i, person_id))
                    if closed:
                        raise Exception("elevator closed, person cannot go out(near line: {})".format(i))
                    p = personOnElevator[person_id]
                    if p.end != currentfloor:
                        raise Exception("人下电梯位置错误(near line: {})：person_end = {}, current_floor = {}".format(i, p.end,
                                                                                                             currentfloor))
                    personOnElevator.pop(person_id)
        if not closed:
            raise Exception("elevator not closed")
        if len(personOnElevator) != 0:
            raise Exception("person remain on elevator")
        if len(b_person) != 0:
            for i in b_person:
                print(i.id)
            raise Exception("请求未完成")
    return timeMine


if __name__ == "__main__":
    for i in range(1000):
        print("checking test point No.{}".format(i + 1))
        # refresh = input("refresh(y or n): ")
        refresh = "y"
        if refresh == 'y':
            # num = int(input("指令条数："))
            num = 50
            create(i, num)
            print("test point create succeed!")
        else:
            print("using testpoint before.")

        os.system("datainput_student_win64.exe | java -jar Alterego.jar > Alterego.txt")
        print("Alterego get!")
        os.system("datainput_student_win64.exe | java -jar Archer.jar > Archer.txt")
        print("Archer get!")
        os.system("datainput_student_win64.exe | java -jar Assassin.jar > Assassin.txt")
        print("Assassin get!")
        os.system("datainput_student_win64.exe | java -jar Berserker.jar > Berserker.txt")
        print("Berserker get!")
        os.system("datainput_student_win64.exe | java -jar Caster.jar > Caster.txt")
        print("Caster get!")
        os.system("datainput_student_win64.exe | java -jar Lancer.jar > Lancer.txt")
        print("Lancer get!")
        os.system("datainput_student_win64.exe | java -jar Rider.jar > Rider.txt")
        print("Rider get!")
        os.system("datainput_student_win64.exe | java -jar Saber.jar > Saber.txt")
        print("Saber get!")

        print("discussion checker_hw5 .......")
        check_hw5("Alterego.txt")
        check_hw5("Archer.txt")
        check_hw5("Assassin.txt")
        check_hw5("Berserker.txt")
        check_hw5("Caster.txt")
        check_hw5("Lancer.txt")
        check_hw5("Rider.txt")
        check_hw5("Saber.txt")
        print("discussion checker_hw5 succeed")

        print("valid check .......")
        validator('Alterego.txt')
        validator('Archer.txt')
        validator('Assassin.txt')
        validator('Berserker.txt')
        validator('Caster.txt')
        validator('Lancer.txt')
        validator('Rider.txt')
        validator('Saber.txt')
        print("valid check succeed")

        print("logical check .......")
        check("Alterego.txt")
        check("Archer.txt")
        check("Assassin.txt")
        check("Berserker.txt")
        check("Caster.txt")
        check("Lancer.txt")
        check("Rider.txt")
        check("Saber.txt")
        print("logical check succeed")

        print()
