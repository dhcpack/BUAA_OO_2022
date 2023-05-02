#!/bin/env python3

from fractions import Fraction

from typing import List


class Operation:
    def __init__(self, op, time, args, text):
        self.oper = op
        self.time = time
        if op in ('ARRIVE', 'OPEN', 'CLOSE'):
            building, floor, e_id = args
            self.building = building
            self.floor = int(floor)
            self.e_id = int(e_id)
        elif op in ('IN', 'OUT'):
            p_id, building, floor, e_id = args
            self.p_id = int(p_id)
            self.building = building
            self.floor = int(floor)
            self.e_id = int(e_id)
        else:
            raise Exception('未知操作')
        self.text = text

    def __repr__(self):
        return self.text


def parse_line(line: str):
    time, body = line[1:].split(']')
    time = Fraction(time)
    op, _, body = body.partition('-')
    args = body.split('-')
    return Operation(op, time, args, line)


TIME_MOVE = Fraction(4, 10)
TIME_DOOR_OPEN = Fraction(2, 10)
TIME_DOOR_CLOSE = Fraction(2, 10)
MIN_FLOOR = 1
MAX_FLOOR = 10
MAX_PEOPLE = 6


def validate_per_elevator(e_id: int, ops: List[Operation]):
    # all from the same building
    building = None
    prev_time = None
    for op in ops:
        if building == None:
            building = op.building
        elif op.building != building:
            raise Exception('同一电梯在不同座间移动')
        if prev_time == None:
            prev_time = op.time
        elif op.time < prev_time:
            raise Exception('时间倒流')

    # states
    curr_floor = 1
    door_opened = False
    last_floor_time = None
    last_door_open_time = None
    passengers = []
    #
    for op in ops:
        # print(op)
        oper = op.oper
        if oper == 'ARRIVE':
            # 忽略到达与当前层相同层(没有移动)
            if op.floor == curr_floor:
                raise Exception('重复到达')
            # 移动了, 不能开着门移动
            if door_opened:
                raise Exception('开门移动')
            # 楼层间移动耗时
            if last_floor_time != None:
                if op.time - last_floor_time < TIME_MOVE:
                    raise Exception('楼层间移动时间过短')
            # 楼层范围
            if op.floor < MIN_FLOOR or op.floor > MAX_FLOOR:
                raise Exception('超出有效楼层范围')
            if abs(op.floor - curr_floor) > 1:
                raise Exception('一次移动了超过一层')
            # OP: ARRIVE
            curr_floor = op.floor
            last_floor_time = op.time
            continue
        if op.floor != curr_floor:
            raise Exception('楼层与状态不一致')

        if oper == 'OPEN':
            if door_opened:
                raise Exception('重复开门')
            door_opened = True
            last_door_open_time = op.time + TIME_DOOR_OPEN  # 完全打开的时间
            last_floor_time = op.time + TIME_DOOR_OPEN  # 开门过程中不能动
        elif oper == 'CLOSE':
            if not door_opened:
                raise Exception('重复关门')
            if last_door_open_time != None:
                if op.time - last_door_open_time < TIME_DOOR_CLOSE:
                    raise Exception('开关门时间过短')
            door_opened = False
            last_floor_time = op.time  # 关门完成
        else:
            if not door_opened:
                raise Exception('乘客撞穿电梯门')
            if oper == 'IN':
                if len(passengers) >= MAX_PEOPLE:
                    raise Exception('超载')
                passengers.append(op.p_id)
            elif oper == 'OUT':
                if op.p_id not in passengers:
                    raise Exception('幽灵出门')
                passengers.remove(op.p_id)


def validator(filename):
    ops = []
    with open(filename, 'r') as file:
        for line in map(lambda l: l.strip(), file):
            ops.append(parse_line(line))
    ops_by_id = dict()
    for op in ops:
        ops_by_id.setdefault(op.e_id, []).append(op)
    for e_id, ops in ops_by_id.items():
        # print('ELEVATOR', e_id)
        validate_per_elevator(e_id, ops)


if __name__ == '__main__':
    validator('resMine.txt')
    validator('resALS.txt')
