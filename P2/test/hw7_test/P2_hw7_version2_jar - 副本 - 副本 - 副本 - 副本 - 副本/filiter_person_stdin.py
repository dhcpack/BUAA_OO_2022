import re

# [3.7100000000000004]448-FROM-B-5-TO-B-4
# [3.8500000000000005]180-FROM-B-8-TO-C-8
file = open("stdin.txt", "r")
lines = file.read().split("\n")[:-1]
file.close()

type = input("building or floor: ")
if type == "building":
    building = input("input building: ")
    RequestPattern = "\[(?P<time>.*)](?P<id>\\d+)-FROM-" + building + "-(?P<start_floor>(10|[1-9]))-TO-" + building + "-(?P<end_floor>(10|[1-9]))"
    pattern = re.compile(RequestPattern)
    res = open("building requests " + building + ".txt", "w")
else:
    floor = input("input floor: ")
    RequestPattern = "\[(?P<time>.*)](?P<id>\\d+)-FROM-(?P<start_building>[A-E]+)-" + floor + "-TO-(?P<end_building>[A-E]+)-" + floor
    pattern = re.compile(RequestPattern)
    res = open("floor requests " + floor + ".txt", "w")
for line in lines:
    if 'ADD' in line and type in line:
        if type == "building" and building == line.split('-')[-1]:
            res.write(line + '\n')
        elif type == "floor" and floor == line.split('-')[-1]:
            res.write(line + '\n')
        continue
    match = re.match(pattern, line)
    if match is not None:
        res.write(line + '\n')
res.close()
