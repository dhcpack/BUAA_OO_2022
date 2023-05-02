import os
import random
import re
from fractions import Fraction

buildings = ['A', 'B', 'C', 'D', 'E']


class Person:
    def __init__(self, arrive_time, id, start_floor, end_floor, start_building, end_building):
        self.arrive_time = Fraction(arrive_time)
        self.id = id
        self.start_floor = int(start_floor)
        self.end_floor = int(end_floor)
        self.start_building = ord(start_building) - ord('A')
        self.end_building = ord(end_building) - ord('A')
        self.current_floor = int(start_floor)
        self.current_building = ord(start_building) - ord('A')
        self.current_time = Fraction(arrive_time)

    def check_end(self):
        return self.current_building == self.end_building and self.current_floor == self.end_floor


class Elevator:
    def __init__(self, id, capacity, speed, stop_place, type, current_floor, current_building, time):
        self.id = id
        self.capacity = Fraction(capacity)
        self.wait_time = Fraction(speed)
        self.stop_place = []
        self.ops = []
        self.type = type
        self.current_floor = int(current_floor)
        self.current_building = ord(current_building) - ord('A')
        self.time = Fraction(time)
        self.is_open = False
        self.person_on_elevator = dict()
        index = 0
        stop_place = int(stop_place)
        while stop_place != 0:
            if stop_place & 1 == 1:
                self.stop_place.append(index)
            index += 1
            stop_place = stop_place >> 1

    def add_op(self, op):
        self.ops.append(op)

    def get_ops(self):
        return self.ops

    def check_arrive(self, arrive_time, arrive_floor, arrive_building):
        arrive_time = Fraction(arrive_time)
        arrive_floor = int(arrive_floor)
        arrive_building = ord(arrive_building) - ord('A')
        if not arrive_time - self.time >= self.wait_time:
            raise Exception(
                "电梯 elevator: {} 运行时间错误: arrive_time = {}, former_time = {}".format(self.id, arrive_time, self.time))
        if self.type == "building":
            if not (self.current_building == arrive_building and abs(self.current_floor - arrive_floor) == 1):
                raise Exception(
                    "电梯运行楼层或楼座错误 elevator: {}: current_floor = {}, former_floor = {}, current_building = {},"
                    " former_building = {}".format(self.id, arrive_floor, self.current_floor, arrive_building,
                                                   self.current_building))
        else:
            if not (self.current_floor == arrive_floor and (
                    abs(self.current_building - arrive_building) == 1 or self.current_building * 10 + arrive_building in
                    [4, 40])):
                raise Exception(
                    "电梯运行楼层或楼座错误 elevator: {}: current_floor = {}, former_floor = {}, current_building = {},"
                    " former_building = {}".format(self.id, arrive_floor, self.current_floor, arrive_building,
                                                   self.current_building))
        self.time = arrive_time
        self.current_building = arrive_building
        self.current_floor = arrive_floor

    def check_open(self, open_time, open_floor, open_building):
        open_time = Fraction(open_time)
        open_floor = int(open_floor)
        open_building = ord(open_building) - ord('A')
        if open_time < self.time:
            raise Exception(
                "电梯开门时间错误 elevator: {}: arrive_time = {}, open_time = {}".format(self.id, self.time, open_time))
        if self.current_floor != open_floor or self.current_building != open_building:
            raise Exception("楼座或楼层错误 elevator: {}: time = {}".format(self.id, open_time))
        if self.is_open:
            raise Exception("重复开门 elevator: {}: time = {}".format(self.id, open_time))
        self.time = open_time
        self.is_open = True

    def check_close(self, close_time, close_floor, close_building):
        close_time = Fraction(close_time)
        close_floor = int(close_floor)
        close_building = ord(close_building) - ord('A')
        if close_time < self.time:
            raise Exception(
                "电梯关门时间错误 elevator: {}: arrive_time = {}, close_time = {}".format(self.id, self.time, close_time))
        if self.current_floor != close_floor or self.current_building != close_building:
            raise Exception("楼座或楼层错误 elevator: {}: time = {}".format(self.id, close_time))
        if not self.is_open:
            raise Exception("重复关门 elevator: {}: time = {}".format(self.id, close_time))
        self.time = close_time
        self.is_open = False

    def check_in(self, in_time, in_floor, in_building, in_person: Person):
        in_time = Fraction(in_time)
        in_floor = int(in_floor)
        in_building = ord(in_building) - ord('A')
        if not self.is_open:
            raise Exception("电梯门关，人不能进入电梯 elevator: {}: person {}".format(self.id, in_person.id))
        if in_building != self.current_building or in_floor != self.current_floor:
            raise Exception("楼座或楼层错误 elevator: {}".format(self.id))
        if in_floor != in_person.current_floor or in_building != in_person.current_building:
            raise Exception("人不在该层or座，不能进入电梯 elevator: {} person: {}: person_floor = {}, elevator_floor = {}，"
                            "person_building = {}, elevator_building = {}"
                            .format(self.id, in_person.id, in_person.current_floor, self.current_floor,
                                    in_person.current_building, self.current_building))
        if in_time < in_person.arrive_time:
            raise Exception("人进入电梯时间错误 elevator: {}: arrive_time = {}, person_arrive_time = {}"
                            .format(self.id, in_time, in_person.arrive_time))
        self.person_on_elevator[in_person.id] = in_person
        if len(self.person_on_elevator) > self.capacity:
            raise Exception("超载 elevator: {}: after person {} get in".format(self.id, in_person.id))
        in_person.arrive_time = in_time
        self.time = in_time

    def check_out(self, out_time, out_floor, out_building, out_person_id: str):
        out_time = Fraction(out_time)
        out_floor = int(out_floor)
        out_building = ord(out_building) - ord('A')
        if not self.is_open:
            raise Exception("电梯门关，人不能走出电梯 elevator: {}: person {}".format(self.id, out_person_id))
        if out_building != self.current_building or out_floor != self.current_floor:
            raise Exception("楼座或楼层错误 elevator: {}".format(self.id))
        if out_person_id not in self.person_on_elevator.keys():
            raise Exception("人不在电梯上 elevator: {}: person_id = {}".format(self.id, out_person_id))
        out_person = self.person_on_elevator[out_person_id]
        self.person_on_elevator.pop(out_person_id)
        if out_time < out_person.arrive_time:
            raise Exception("人走出电梯时间错误 elevator: {}: arrive_time = {}, person_arrive_time = {}"
                            .format(self.id, out_time, out_person.arrive_time))
        out_person.current_floor = self.current_floor
        out_person.current_building = self.current_building
        out_person.current_time = out_time
        self.time = out_time
        if out_person.check_end():
            return None
        else:
            return out_person

    def check_finished(self):
        if len(self.person_on_elevator) != 0:
            for person_id in self.person_on_elevator.keys():
                print(person_id)
            raise Exception("人在电梯中未出来 elevator: {}".format(self.id))
        if self.is_open:
            raise Exception("电梯未关门 elevator: {}".format(self.id))


def create(num):
    personIdPool = set()
    elevatorIdPool = set()
    for i in range(5):
        elevatorIdPool.add(i)
    elevators = [1] * 5 + [0] * 10
    current_time = 1.0
    file = open("stdin.txt", "w")

    for i in range(num):
        current_time = 1.0
        building = random.choice(buildings)
        coin = random.randint(1, 10)
        if coin == 1:
            if sum(elevators) >= 20:
                continue
            flag = False
            for elevator in elevators:
                if elevator != 3:
                    flag = True
                    break
            if not flag:
                continue
            v1 = [4, 6, 8]
            v2 = [0.2, 0.4, 0.6]
            capacity = random.choice(v1)
            speed = random.choice(v2)
            reach_place = random.randint(3, 31)
            while reach_place in [4, 8, 16]:
                reach_place = random.randint(3, 31)
            elevatorId = random.randint(1, 1000000)
            while elevatorId in elevatorIdPool:
                elevatorId = random.randint(1, 1000000)
            elevatorIdPool.add(elevatorId)
            elevatorType = random.randint(0, 14)
            while elevators[elevatorType] == 3:
                elevatorType = random.randint(0, 14)
            elevators[elevatorType] += 1
            if elevatorType < 5:
                request = "[{:.1f}]ADD-building-{}-{}-{}-{}\n".format(current_time, elevatorId, building, capacity, speed)
            else:
                request = "[{:.1f}]ADD-floor-{}-{}-{}-{}-{}\n".format(current_time, elevatorId, elevatorType - 4, capacity,
                                                                  speed, reach_place)
        else:
            personId = random.randint(1, 1000000)
            while personId in personIdPool:
                personId = random.randint(1, 1000000)
            personIdPool.add(personId)
            start_floor = random.randint(1, 10)
            end_floor = random.randint(1, 10)
            start_building = random.choice(buildings)
            end_building = random.choice(buildings)
            while start_floor == end_floor and start_building == end_building:
                end_floor = random.randint(1, 10)
                end_building = random.choice(buildings)
            request = "[{:.1f}]{}-FROM-{}-{}-TO-{}-{}\n".format(current_time, personId, start_building, start_floor,
                                                            end_building, end_floor)
        file.write(request)
    file.close()


def get_person_elevator(filename):
    file = open(filename, "r")
    lineStdin = file.read().split("\n")[:-1]
    file.close()
    person_dict = dict()
    elevator_dict = dict()

    for i in range(1, 6):
        elevator_dict[str(i)] = Elevator(str(i), 8, 0.6, 0, "building", 1, chr(ord('A') + i - 1), 0)
    elevator_dict['6'] = Elevator('6', 8, 0.6, 31, "floor", 1, 'A', 0)

    for line in lineStdin:
        person_pattern = "\[(?P<time>.*)](?P<id>\\d+)-FROM-(?P<start_building>[A-E]+)-(?P<start_floor>(10|[1-9]))-TO-(?P<end_building>[A-E]+)-(?P<end_floor>(10|[1-9]))"
        building_elevator_pattern = "\[(?P<time>.*)]ADD-building-(?P<elevator_id>\\d+)-(?P<building>[A-E]+)-(?P<capacity>\\d+)-(?P<speed>.*)"
        floor_elevator_pattern = "\[(?P<time>.*)]ADD-floor-(?P<elevator_id>\\d+)-(?P<floor>(10|[1-9]))-(?P<capacity>\\d+)-(?P<speed>.*)-(?P<stop_place>\\d+)"
        pattern = re.compile(person_pattern)
        match = re.match(pattern, line)
        if match:
            person = Person(match.group("time"), match.group("id"), match.group("start_floor"),
                            match.group("end_floor"),
                            match.group("start_building"), match.group("end_building"))
            person_dict[person.id] = person

        pattern = re.compile(building_elevator_pattern)
        match = re.match(pattern, line)
        if match:
            building_elevator = Elevator(match.group("elevator_id"), match.group("capacity"),
                                         match.group("speed"), 0, "building", 1, match.group("building"),
                                         match.group("time"))
            elevator_dict[building_elevator.id] = building_elevator

        pattern = re.compile(floor_elevator_pattern)
        match = re.match(pattern, line)
        if match:
            floor_elevator = Elevator(match.group("elevator_id"), match.group("capacity"), match.group("speed"),
                                      match.group("stop_place"), "floor", match.group("floor"), "A",
                                      match.group("time"))
            elevator_dict[floor_elevator.id] = floor_elevator

    return person_dict, elevator_dict


def check(filename):
    person_dict, elevator_dict = get_person_elevator("stdin.txt")

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

    for line in lines:
        arrive_pattern = "\[(?P<time>.*)]ARRIVE-(?P<building>[A-E]+)-(?P<floor>(10|[1-9]))-(?P<elevator_id>\\d+)"
        open_pattern = "\[(?P<time>.*)]OPEN-(?P<building>[A-E]+)-(?P<floor>(10|[1-9]))-(?P<elevator_id>\\d+)"
        close_pattern = "\[(?P<time>.*)]CLOSE-(?P<building>[A-E]+)-(?P<floor>(10|[1-9]))-(?P<elevator_id>\\d+)"
        in_pattern = "\[(?P<time>.*)]IN-(?P<person_id>\\d+)-(?P<building>[A-E]+)-(?P<floor>(10|[1-9]))-(?P<elevator_id>\\d+)"
        out_pattern = "\[(?P<time>.*)]OUT-(?P<person_id>\\d+)-(?P<building>[A-E]+)-(?P<floor>(10|[1-9]))-(?P<elevator_id>\\d+)"

        pattern = re.compile(arrive_pattern)
        match = re.match(pattern, line)
        if match:
            if match.group("elevator_id") not in elevator_dict.keys():
                raise Exception("电梯(ID: {})不存在".format(match.group("elevator_id")))
            elevator = elevator_dict[match.group("elevator_id")]
            elevator.check_arrive(match.group("time"), match.group("floor"), match.group("building"))

        pattern = re.compile(open_pattern)
        match = re.match(pattern, line)
        if match:
            if match.group("elevator_id") not in elevator_dict.keys():
                raise Exception("电梯(ID: {})不存在".format(match.group("elevator_id")))
            elevator = elevator_dict[match.group("elevator_id")]
            elevator.check_open(match.group("time"), match.group("floor"), match.group("building"))

        pattern = re.compile(close_pattern)
        match = re.match(pattern, line)
        if match:
            if match.group("elevator_id") not in elevator_dict.keys():
                raise Exception("电梯(ID: {})不存在".format(match.group("elevator_id")))
            elevator = elevator_dict[match.group("elevator_id")]
            elevator.check_close(match.group("time"), match.group("floor"), match.group("building"))

        pattern = re.compile(in_pattern)
        match = re.match(pattern, line)
        if match:
            if match.group("elevator_id") not in elevator_dict.keys():
                raise Exception("电梯(ID: {})不存在".format(match.group("elevator_id")))
            elevator = elevator_dict[match.group("elevator_id")]
            in_person_id = match.group("person_id")
            if in_person_id not in person_dict.keys():
                raise Exception("person ID({}) not Found".format(in_person_id))
            in_person = person_dict.pop(in_person_id)
            elevator.check_in(match.group("time"), match.group("floor"), match.group("building"), in_person)

        pattern = re.compile(out_pattern)
        match = re.match(pattern, line)
        if match:
            if match.group("elevator_id") not in elevator_dict.keys():
                raise Exception("电梯(ID: {})不存在".format(match.group("elevator_id")))
            elevator = elevator_dict[match.group("elevator_id")]
            out_person = elevator.check_out(match.group("time"), match.group("floor"), match.group("building"),
                                            match.group("person_id"))
            if out_person is not None:
                person_dict[match.group("person_id")] = out_person
    for elevator in elevator_dict.values():
        elevator.check_finished()
    if len(person_dict) != 0:
        for person_id in person_dict.keys():
            print(person_id)
        raise Exception("请求未完成")
    return time


if __name__ == "__main__":
    for i in range(1000):
        print("checking test point No.{}".format(i + 1))
        # refresh = input("refresh(y or n): ")
        refresh = "y"
        if refresh == 'y':
            # num = int(input("指令条数："))
            num = 30
            create(num)
            print("test point create succeed!")
        else:
            print("using testpoint before.")

        os.system("datainput_student_win64.exe | java -jar Archer.jar > Archer.txt")
        print("Archer get!")
        os.system("datainput_student_win64.exe | java -jar Assassin.jar > Assassin.txt")
        print("Assassin get!")
        os.system("datainput_student_win64.exe | java -jar Berserker.jar > Berserker.txt")
        print("Berserker get!")
        # os.system("datainput_student_win64.exe | java -jar Caster.jar > Caster.txt")
        # print("Caster get!")
        os.system("datainput_student_win64.exe | java -jar Lancer.jar > Lancer.txt")
        print("Lancer get!")
        # os.system("datainput_student_win64.exe | java -jar Rider.jar > Rider.txt")
        # print("Rider get!")
        os.system("datainput_student_win64.exe | java -jar Saber.jar > Saber.txt")
        print("Saber get!")

        print("logical check .......")
        check("Archer.txt")
        check("Assassin.txt")
        check("Berserker.txt")
        # check("Caster.txt")
        check("Lancer.txt")
        # check("Rider.txt")
        check("Saber.txt")
        print("logical check succeed")

        print()

