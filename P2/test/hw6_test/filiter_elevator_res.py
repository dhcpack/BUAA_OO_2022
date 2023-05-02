def get_elevator(filename):
    file = open(filename, "r")
    lines = file.read().split('\n')[:-1]
    file.close()
    time = float(lines[-1][lines[-1].index("[") + 1:lines[-1].index("]")])
    elevator_dict = dict()
    # 分离各个电梯
    for line in lines:
        new_line = line.split("-")
        elevator_dict.setdefault(new_line[-1], []).append(line)
    return time, elevator_dict


def writefile(id, ops: list):
    file = open("elevator" + str(id), "w")
    for op in ops:
        file.write(op + "\n")
    file.close()


if __name__ == "__main__":
    time ,elevators = get_elevator("mutual_2022_04_17_15_34_26_0a3b49968c02d8bca6e7ec57985fd705_stdin.txt")
    id = input("input elevator: ")
    if id not in elevators.keys():
        print("elevator not found")
    else:
        writefile(id, elevators[id])
