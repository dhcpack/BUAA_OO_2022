import random

personIdPool = set()
buildingList = ['A', 'B', 'C', 'D', 'E']

current_time = 0.0
file = open("stdin.txt", "w")

for i in range(10):
    personId = random.randint(0, 1000)
    while personId in personIdPool:
        personId = random.randint(0, 1000)
    personIdPool.add(personId)
    building = random.choice(buildingList)
    start = random.randint(1, 10)
    end = random.randint(1, 10)
    while end == start:
        end = random.randint(1, 10)
    current_time = current_time + float(random.randint(0, 20) / 100)
    request = "[" + str(current_time) + "]" + str(
        personId) + "-" + "FROM" + "-" + building + "-" + str(start) + "-TO-" + str(building) + "-" + str(end) + "\n"
    # request = str(personId) + "-" + "FROM" + "-" + building + "-" + str(start) + "-TO-" + str(building) + "-" + str(
    #     end) + "\n"
    file.write(request)

file.close()