import os
import random
import re
from fractions import Fraction

from validator import validator

buildings = ['A', 'B', 'C', 'D', 'E']
TIME_MOVE = Fraction(2, 10)
TIME_DOOR_OPENCLOSE = Fraction(4, 10)
MIN_FLOOR = 1
MAX_FLOOR = 10
MAX_PEOPLE = 6


def create(num):
    personIdPool = set()
    elevatorIdPool = set()
    for i in range(5):
        elevatorIdPool.add(i)
    elevators = [1] * 5 + [0] * 10
    exist_time = [-1] * 5 + [float('inf')] * 10
    current_time = 0.0
    file = open("stdin.txt", "w")

    for i in range(num):
        current_time = current_time + random.randint(0, 10) / 100
        building = random.choice(buildings)
        coin = random.randint(1, 10)
        if coin == 1:
            flag = False
            for elevator in elevators:
                if elevator != 3:
                    flag = True
                    break
            if not flag:
                continue
            elevatorId = random.randint(1, 10000)
            while elevatorId in elevatorIdPool:
                elevatorId = random.randint(1, 10000)
            elevatorIdPool.add(elevatorId)
            elevatorType = random.randint(0, 14)
            while elevators[elevatorType] == 3:
                elevatorType = random.randint(0, 14)
            elevators[elevatorType] += 1
            if elevatorType < 5:
                request = "[{}]ADD-building-{}-{}\n".format(current_time, elevatorId, building)
            else:
                request = "[{}]ADD-floor-{}-{}\n".format(current_time, elevatorId, elevatorType - 4)
            exist_time[elevatorType] = min(exist_time[elevatorType], current_time)
        else:
            personId = random.randint(1, 10000)
            while personId in personIdPool:
                personId = random.randint(1, 10000)
            personIdPool.add(personId)
            elevatorType = random.randint(0, 14)
            while elevators[elevatorType] == 0 or current_time - exist_time[elevatorType] < 1:
                elevatorType = random.randint(0, 14)
            if elevatorType < 5:
                building = chr(ord('A') + elevatorType)
                start = random.randint(1, 10)
                end = random.randint(1, 10)
                while end == start:
                    end = random.randint(1, 10)
                request = "[{}]{}-FROM-{}-{}-TO-{}-{}\n".format(current_time, personId, building, start, building, end)
            else:
                floor = elevatorType - 4
                start = random.choice(buildings)
                end = random.choice(buildings)
                while end == start:
                    end = random.choice(buildings)
                request = "[{}]{}-FROM-{}-{}-TO-{}-{}\n".format(current_time, personId, start, floor, end, floor)
        file.write(request)
    file.close()


class Person:
    def __init__(self, arrive_time, id, start_floor, end_floor, start_building, end_building):
        self.arrive_time = float(arrive_time)
        self.id = id
        self.start_floor = int(start_floor)
        self.end_floor = int(end_floor)
        self.start_building = start_building
        self.end_building = end_building


def get_person():
    file = open("stdin.txt", "r")
    lineStdin = file.read().split("\n")[:-1]
    file.close()
    person_dict = dict()
    # 分离所有人
    for line in lineStdin:
        if 'ADD' in line:
            continue
        # [时间戳]乘客ID-FROM-起点座-起点层-TO-终点座-终点层
        linePattern = "\[(?P<time>.*)](?P<id>\\d+)-FROM-(?P<start_building>[A-E]+)-(?P<start_floor>(10|[1-9]))-TO-(?P<end_building>[A-E]+)-(?P<end_floor>(10|[1-9]))"
        pattern = re.compile(linePattern)
        match = re.match(pattern, line)
        person = Person(match.group("time"), match.group("id"), match.group("start_floor"), match.group("end_floor"),
                        match.group("start_building"), match.group("end_building"))
        # if match.group("start_floor") == match.group("end_floor"):
        #     person_dict.setdefault(match.group("start_floor"), set()).add(person)
        # else:
        #     person_dict.setdefault(match.group("start_building"), set()).add(person)
        person_dict[person.id] = person
    return person_dict


def get_elevator(filename):
    file = open(filename, "r")
    lines = file.read().split('\n')[:-1]
    file.close()
    former_time = Fraction(lines[0][1:lines[0].index(']')])
    for line in lines:
        current_time = Fraction(line[1:line.index(']')])
        if not current_time >= former_time:
            raise Exception("时间戳非递增，at time {}".format(current_time))
        former_time = current_time
    print("时间戳检查完成")
    time = float(lines[-1][lines[-1].index("[") + 1:lines[-1].index("]")])
    elevator_dict = dict()
    # 分离各个电梯
    for line in lines:
        new_line = line.split("-")
        elevator_dict.setdefault(new_line[-1], []).append(line)
    return time, elevator_dict


def writefile(id, ops: list):
    print("wa in elevator " + str(id))
    file = open("elevator" + str(id), "w")
    for op in ops:
        file.write(op + "\n")
    file.close()


def check(filename):
    person_dict = get_person()
    time, elevator_dict = get_elevator(filename)

    # logic check
    for elevator_id in elevator_dict.keys():
        elevator_ops = elevator_dict.setdefault(elevator_id, None)
        personOnElevator = dict()
        last_time = Fraction(0)
        last_floor = None
        last_building = None
        closed = True
        index = 0

        while index < len(elevator_ops):
            if not (elevator_ops[index].find("ARRIVE") == -1 and index == 0):
                # [时间戳]ARRIVE-所在座-所在层-电梯ID
                arrive_pattern = "\[(?P<time>.*)]ARRIVE-(?P<building>[A-E]+)-(?P<floor>(10|[1-9]))-(?P<elevator_id>\\d+)"
                pattern = re.compile(arrive_pattern)
                match = re.match(pattern, elevator_ops[index])
                arrive_time, current_building, current_floor = Fraction(match.group("time")), match.group(
                    "building"), int(match.group("floor"))
                if not arrive_time - last_time >= TIME_MOVE:
                    writefile(elevator_id, elevator_ops)
                    raise Exception(
                        "电梯运行时间错误(line: {})：arrive_time = {}, former_time = {}".format(index, arrive_time, last_time))
                if not ((last_floor is None or (abs(current_floor - last_floor) == 1) and (
                        last_building is None or current_building == last_building)) or (
                                (last_floor is None or current_floor == last_floor) and (
                                last_building is None or (abs(
                            ord(current_building) - ord(last_building)) == 1) or (
                                        (str(current_building) + str(last_building)) in ["AE", "EA"])))):
                    writefile(elevator_id, elevator_ops)
                    raise Exception(
                        "电梯运行楼层或楼座错误(line: {})：current_floor = {}, former_floor = {}, current_building = {}, "
                        "former_building = {}".format(index, current_floor, last_floor, current_building,
                                                      last_building))
                last_time = arrive_time
                last_floor = current_floor
                last_building = current_building
                index += 1
            else:  # 第一层直接开门
                # [时间戳]OPEN-所在座-所在层-电梯ID
                open_pattern = "\[(?P<time>.*)]OPEN-(?P<building>[A-E]+)-(?P<floor>(10|[1-9]))-(?P<elevator_id>\\d+)"
                pattern = re.compile(open_pattern)
                match = re.match(pattern, elevator_ops[index])
                arrive_time, current_building, current_floor = Fraction(0), match.group("building"), int(match.group(
                    "floor"))
            current_floor_op = []
            while index < len(elevator_ops) and elevator_ops[index].find("ARRIVE") == -1:
                current_floor_op.append(elevator_ops[index])
                index += 1
            open_time, close_time, in_time, out_time = 0, 0, 0, 0

            while current_floor_op:
                line = current_floor_op.pop(0)
                if "OPEN" in line:
                    open_pattern = "\[(?P<time>.*)]OPEN-(?P<building>[A-E]+)-(?P<floor>(10|[1-9]))-(?P<elevator_id>\\d+)"
                    pattern = re.compile(open_pattern)
                    match = re.match(pattern, line)
                    if match.group("building") != current_building or int(match.group("floor")) != current_floor:
                        writefile(elevator_id, elevator_ops)
                        raise Exception("楼座或楼层错误(near line: {})".format(index))
                    if not closed:
                        writefile(elevator_id, elevator_ops)
                        raise Exception("重复开门(near line: {})".format(index))
                    open_time = Fraction(match.group("time"))
                    closed = False
                    if open_time < arrive_time:
                        writefile(elevator_id, elevator_ops)
                        raise Exception(
                            "电梯开门时间错误(near line: {})：arrive_time = {}, open_time = {}".format(index, arrive_time,
                                                                                              open_time))
                if "CLOSE" in line:
                    # [时间戳]CLOSE-所在座-所在层-电梯ID
                    close_pattern = "\[(?P<time>.*)]CLOSE-(?P<building>[A-E]+)-(?P<floor>(10|[1-9]))-(?P<elevator_id>\\d+)"
                    pattern = re.compile(close_pattern)
                    match = re.match(pattern, line)
                    if match.group("building") != current_building or int(match.group("floor")) != current_floor:
                        writefile(elevator_id, elevator_ops)
                        raise Exception("楼座或楼层错误(near line: {})".format(index))
                    if closed:
                        writefile(elevator_id, elevator_ops)
                        raise Exception("重复关门(near line: {})".format(index))
                    close_time = Fraction(match.group("time"))
                    closed = True
                    if close_time - open_time < TIME_DOOR_OPENCLOSE:
                        writefile(elevator_id, elevator_ops)
                        raise Exception(
                            "电梯关门时间错误(near line: {})：open_time = {}, close_time = {}".format(index, open_time,
                                                                                             close_time))
                    last_time, last_floor, last_building = close_time, int(match.group("floor")), match.group(
                        "building")
                if "IN" in line:
                    # [时间戳]IN-乘客ID-所在座-所在层-电梯ID
                    in_pattern = "\[(?P<time>.*)]IN-(?P<person_id>\\d+)-(?P<building>[A-E]+)-(?P<floor>(10|[1-9]))-(?P<elevator_id>\\d+)"
                    pattern = re.compile(in_pattern)
                    match = re.match(pattern, line)
                    if match.group("building") != current_building or int(match.group("floor")) != current_floor:
                        writefile(elevator_id, elevator_ops)
                        raise Exception("楼座或楼层错误(near line: {})".format(index))
                    person_id, in_time, in_building, in_floor = match.group("person_id"), Fraction(
                        match.group("time")), match.group("building"), int(match.group("floor"))
                    flag = False
                    for a, person in person_dict.items():
                        if person_id == a:
                            flag = True
                            if in_time < person.arrive_time:
                                writefile(elevator_id, elevator_ops)
                                raise Exception(
                                    "人进入电梯时间错误(near line: {})：person_start_time = {}, person_go_into_elevator_time = {}".format(
                                        index, person.arrive_time, in_time))
                            if closed:
                                writefile(elevator_id, elevator_ops)
                                raise Exception("elevator closed, person cannot go into(near line: {})".format(index))
                            if in_floor != person.start_floor or in_building != person.start_building:
                                writefile(elevator_id, elevator_ops)
                                raise Exception(
                                    "人不在该层or座，不能进入电梯(near line: {})：person_start_floor = {}, "
                                    "current_floor = {}，person_start_building = {}, current_building = {}".format(
                                        index, person.start_floor, current_floor, person.start_building,
                                        current_building))
                            personOnElevator[person_id] = person
                    if not flag:
                        writefile(elevator_id, elevator_ops)
                        raise Exception("person ID({}) not Found(near line: {})".format(person_id, index))
                    del person_dict[person_id]
                    if len(personOnElevator) > 6:
                        writefile(elevator_id, elevator_ops)
                        raise Exception("超载(near line: {})".format(index))
                if "OUT" in line:
                    # [时间戳]OUT-乘客ID-所在座-所在层-电梯ID
                    out_pattern = "\[(?P<time>.*)]OUT-(?P<person_id>\\d+)-(?P<building>[A-E]+)-(?P<floor>(10|[1-9]))-(?P<elevator_id>\\d+)"
                    pattern = re.compile(out_pattern)
                    match = re.match(pattern, line)
                    if match.group("building") != current_building or int(match.group("floor")) != current_floor:
                        writefile(elevator_id, elevator_ops)
                        raise Exception("楼座或楼层错误(near line: {})".format(index))
                    person_id, out_time, out_building, out_floor = match.group("person_id"), Fraction(
                        match.group("time")), match.group("building"), int(match.group("floor"))
                    if person_id not in personOnElevator:
                        writefile(elevator_id, elevator_ops)
                        raise Exception("人不在电梯上(near line: {})：person_id = {}".format(index, person_id))
                    if closed:
                        writefile(elevator_id, elevator_ops)
                        raise Exception("elevator closed, person cannot go out(near line: {})".format(index))
                    p = personOnElevator[person_id]
                    if p.end_floor != current_floor or p.end_building != current_building:
                        writefile(elevator_id, elevator_ops)
                        raise Exception(
                            "人下电梯位置错误(near line: {})：person_end_floor = {}, current_floor = {}, "
                            "person_end_bulding = {}, current_building = {}".format(index, p.end_floor, current_floor,
                                                                                    p.end_building, current_building))
                    personOnElevator.pop(person_id)
        if not closed:
            writefile(elevator_id, elevator_ops)
            raise Exception("elevator not closed")
        if len(personOnElevator) != 0:
            writefile(elevator_id, elevator_ops)
            raise Exception("person remain on elevator")
    if len(person_dict) != 0:
        for index in person_dict.keys():
            print(index.id)
        writefile(elevator_id, elevator_ops)
        raise Exception("请求未完成")
    return time


if __name__ == "__main__":
    for i in range(1):
        print("checking test point No.{}".format(i + 1))
        # refresh = input("refresh(y or n): ")
        refresh = "n"
        if refresh == 'y':
            # num = int(input("指令条数："))
            num = 70
            create(num)
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
