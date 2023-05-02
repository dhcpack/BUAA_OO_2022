import creater_hw5
import checker_hw5
import subprocess
import random
import time

print("请输入测试样例数:", end=" ")
testCaseNum = int(input())
for i in range(0, testCaseNum):
    print("[TestCase" + str(i) + "]")
    num = random.randint(1, 90)
    f = open("stdin.txt", "w")
    testCase = creater_hw5.getTestCase(num)
    for string in testCase:
        f.write(string + "\n")
    f.close()
    dataCheckLoc = "./"
    dataCheckCommand = "./datacheck1.exe"
    dataCheckSp = subprocess.Popen(dataCheckCommand, cwd=dataCheckLoc, stdin=subprocess.PIPE, stdout=subprocess.PIPE,
                                   stderr=subprocess.PIPE)
    stdout = dataCheckSp.communicate()
    timeString = bytes.decode(stdout[0])
    base = float(timeString.split(" ")[6].split(",")[0])
    max = float(timeString.split(" ")[9])
    print("Official MaxTime:" + str(max))
    print("正在运行java程序...(如果时间超过Max过长，可能是出现死锁，请退出本程序后查看stdin.txt的测试数据并进行手动测试)")
    start = time.time()
    subprocess.run("mybatWrite.bat")
    end = time.time()
    realTime = round(end - start, 4)
    flag = checker_hw5.check(realTime)
    if flag == False: break
if flag:
    print("All Accpeted")
print("请输入任意键退出程序", end=" ")
input()
