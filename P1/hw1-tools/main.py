# import generate
# import create
#
# inputfile = open("input.txt", "r")
# index = 0
# for line in inputfile.readlines():
#     line = line.strip("\n")
#     judge = generate.generare(line[6:])
#     if not judge:
#         print(line)
#     index += 1
#     print(index)
import create
import generate

for i in range(3000):
    if i % 100 == 0:
        print(i)
    origin = create.create()
    judge = generate.generare(origin.strip("\n"))
    if not judge:
        print(origin)

