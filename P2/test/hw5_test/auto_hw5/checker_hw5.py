import subprocess
from sys import stdout


def RemoveTime(string):
    return string.split("]")[1]


def RemoveEnter(string):
    return string.split("\n")[0]


def ParseKind(string):  ##根据string分类 0:Arrive 1:OPEN 2:CLOSE 3:IN 4:OUT
    string_noTime = RemoveTime(string)  ## 去除时间戳
    kind = string_noTime.split("-")[0]
    if kind == "ARRIVE":
        return 0
    elif kind == "OPEN":
        return 1
    elif kind == "CLOSE":
        return 2
    elif kind == "IN":
        return 3
    else:
        return 4


def ParseTime(string):
    time = string.split("]")[0]
    time = time.split("[")[1]
    time = time.split()[0]
    time = float(time)
    return time


def ParseArriveOpenClose(string):
    time = ParseTime(string)
    string = RemoveTime(string)
    msgList = string.split("-")
    building = msgList[1]
    curPos = int(msgList[2])
    id = int(msgList[3])
    return [time, building, curPos, id]


def ParseInOut(string):
    time = ParseTime(string)
    string = RemoveTime(string)
    msgList = string.split("-")
    pId = int(msgList[1])
    building = msgList[2]
    curPos = int(msgList[3])
    eId = int(msgList[4])
    return [time, pId, building, curPos, eId]


def ParseBuilding(string):
    kind = ParseKind(string)
    if kind == 0 or kind == 1 or kind == 2:
        msgList = ParseArriveOpenClose(string)
        return msgList[1]
    if kind == 3 or kind == 4:
        msgList = ParseInOut(string)
        return msgList[2]


def ClassifyIntoBuilding(string, buildingList):  # 将输出分类到不同楼座的输出
    building = ParseBuilding(string)
    if building == "A":
        buildingList[0].append(string)
    if building == "B":
        buildingList[1].append(string)
    if building == "C":
        buildingList[2].append(string)
    if building == "D":
        buildingList[3].append(string)
    if building == "E":
        buildingList[4].append(string)


def checkLogy(outPutList):  # 检查逻辑
    i = 0
    flag = True
    if len(outPutList) == 0: return True
    firstString = outPutList[0]
    firstKind = ParseKind(firstString)
    if firstKind != 0 and firstKind != 1:  # 第一条必定是open或arrive
        print("[WrongLogy]")
        print("FirstString:" + firstString)
        return False
    while i < len(outPutList) - 1:
        curString = outPutList[i]
        nextString = outPutList[i + 1]
        curKind = ParseKind(curString)
        nextKind = ParseKind(nextString)
        if curKind == 0:
            if nextKind == 2 or nextKind == 3 or nextKind == 4:
                print("[Logical Error] Arrive之后逻辑错误")
                print("当前输出:" + curString)
                print("下一条输出:" + nextString)
                flag = False
        if curKind == 1:
            if nextKind == 0 or nextKind == 1:
                print("[Logical Error] Open之后逻辑错误")
                print("当前输出:" + curString)
                print("下一输出:" + nextString)
                flag = False
        if curKind == 2:
            if nextKind == 2 or nextKind == 3 or nextKind == 4:
                print("[Logical Error] Close之后逻辑错误")
                print("当前输出:" + curString)
                print("下一输出:" + nextString)
                flag = False
        if curKind == 3:
            if nextKind == 0 or nextKind == 1:
                print("[Logical Error] In之后逻辑错误")
                print("当前输出:" + curString)
                print("下一输出:" + nextString)
                flag = False
        if curKind == 4:
            if nextKind == 0 or nextKind == 1:
                print("[Logical Error] Out之后逻辑错误")
                print("当前输出:" + curString)
                print("下一输出:" + nextString)
                flag = False
        i = i + 1
    lastString = outPutList[-1]
    lastKind = ParseKind(lastString)
    if lastKind == 1 or lastKind == 3 or lastKind == 4:
        print("[Logical Error] 最后一条输出逻辑错误")
        print("最后输出:" + lastString)
    # print("[Logy Accepted]")
    return flag


def ParseReq(Req):
    string = RemoveTime(Req)
    msg = string.split("-")
    id = int(msg[0])
    building = msg[2]
    fromFloor = int(msg[3])
    toFloor = int(msg[6])
    return (id, building, fromFloor, toFloor)


def getReqNameList(testCase):  # 将输入分类到不同楼座的输入
    nameList = [[], [], [], [], []]
    for req in testCase:
        msg = ParseReq(req)
        building = msg[1]
        if building == "A":
            nameList[0].append(msg)
        elif building == "B":
            nameList[1].append(msg)
        elif building == "C":
            nameList[2].append(msg)
        elif building == "D":
            nameList[3].append(msg)
        else:
            nameList[4].append(msg)
    return nameList


def checkPerson(nameList, outPutList, building):
    flag = True
    inList = []
    outList = []
    floors = [[] for i in range(0, 10)]
    for string in outPutList:
        kind = ParseKind(string)
        if kind == 3:  # in
            msg = ParseInOut(string)
            pId = msg[1]
            find = False
            for req in nameList:
                if req[0] == pId:
                    find = True
                    break
            if not find:  ## 确认是有这个请求的人近来
                print("[Person Error] 进入了一个没有请求的人")
                print("当前输出:" + string)
                flag = False
            fromFloor = msg[3]
            Req = (pId, building, fromFloor)
            inList.append(Req)  ##进入电梯
            if len(inList) > 6:
                print("[Person Error] 电梯超载")
                print("当前输出:" + string)
                flag = False
        if kind == 4:  # Out
            msg = ParseInOut(string)
            pId = msg[1]
            find = False
            for i in range(0, len(inList)):
                if inList[i][0] == pId:
                    Req = inList[i]
                    inList.remove(Req)
                    toFloor = msg[3]
                    Req = Req + tuple([toFloor])
                    outList.append(Req)  # 离开电梯
                    find = True
                    break
            if not find:
                print("[Person Error] 一个本不在电梯中的人出去了")
                print("当前输出:" + string)
                flag = False
    outSet = set(outList)
    nameSet = set(nameList)
    if len(inList) != 0:
        print("[Person Error] 运行结束时还有人在电梯内")
        for req in inList:
            print("PersonID:" + str(req[0]))
        flag = False
    if outSet != nameSet:
        print("[Person Error] 运行结束时有人没到其目的楼层")
        diffSet = outSet ^ nameSet
        for req in diffSet:
            print("PersonID:" + str(req[0]))
        flag = False
    # print("[Person Accepted]")
    return flag


def checkPosition(outPutList):
    flag = True
    pos = 1
    for string in outPutList:
        kind = ParseKind(string)
        if kind == 0:
            msg = ParseArriveOpenClose(string)
            curPos = msg[2]
            if (curPos - pos != 1) and (curPos - pos != -1):
                print("[Position Error] 电梯一次移动超过一层或移到相同楼层")
                print("当前输出:" + string)
                flag = False
            pos = curPos
        if kind == 1 or kind == 2:
            msg = ParseArriveOpenClose(string)
            curPos = msg[2]
            if curPos != pos:
                print("[Position Error] 在一个未到达楼层开关门")
                print("当前输出:" + string)
                flag = False
        if kind == 3 or kind == 4:
            msg = ParseInOut(string)
            curPos = msg[3]
            if curPos != pos:
                print("[Position Error] 在一个未到达楼层进出人员")
                print("当前输出:" + string)
                flag = False
        if pos < 1 or pos > 10:
            print("[Position Error] 电梯运行到一个错误位置")
            print("当前输出:" + string)
            flag = False
    # print("[Position Accepted]")
    return flag


def checkTime(outPutList, maxTime, realTime):
    curTime = 0
    nextTime = 0
    flag = True
    for string in outPutList:
        nextTime = ParseTime(string)
        if nextTime < curTime:
            print("[Time Error] 时间戳非递增")
            print("当前输出:" + string)
            flag = False
        curTime = nextTime
    # print("[Time Sequence Accepted]")
    if curTime > maxTime:
        print("[Time Error] 电梯运行时间finalTime超过限制maxTime")
        print("Your finaltime:" + str(curTime))
        print("Max time:" + str(maxTime))
        flag = False
    # print("[Final Time Accepted]")
    if realTime > maxTime:
        print("[Time Error] 程序运行时间realTime超过限制maxTime")
        print("Your realtime:" + str(realTime))
        print("Max time:" + str(maxTime))
        flag = False
    for i in range(0, len(outPutList)):  # 检查Arrive的0.4s
        if i == 0: continue
        curString = outPutList[i]
        lastString = outPutList[i - 1]
        curKind = ParseKind(curString)
        if curKind == 0:
            lastTime = ParseTime(lastString)
            curTime = ParseTime(curString)
            if round(curTime - lastTime, 4) < 0.4000:
                print("[Time Error] 电梯移动过快")
                print("上一输出:" + lastString)
                print("当前输出:" + curString)
                flag = False
    # print("[Arrive Time Accepted]")
    openTime = 0
    closeTime = 0
    for string in outPutList:
        kind = ParseKind(string)
        if kind == 1:
            openTime = ParseTime(string)
        if kind == 2:
            closeTime = ParseTime(string)
            if round(closeTime - openTime, 4) < 0.4000:
                print("[Time Error] 电梯开关门过快")
                print("开门时间戳:" + str(openTime))
                print("关门时间戳:" + str(closeTime))
                flag = False
    # print("[Open and Close Time Accepted]")
    return flag


def check(realTime):
    dataCheckLoc = "./"
    dataCheckCommand = "./datacheck1.exe"
    dataCheckSp = subprocess.Popen(dataCheckCommand, cwd=dataCheckLoc, stdin=subprocess.PIPE, stdout=subprocess.PIPE,
                                   stderr=subprocess.PIPE)
    stdout = dataCheckSp.communicate()
    timeString = bytes.decode(stdout[0])
    base = float(timeString.split(" ")[6].split(",")[0])
    max = float(timeString.split(" ")[9])
    fout = open("stdout.txt", "r")
    totalOutPut = []
    for string in fout.readlines():
        if string[0] != "[": break
        totalOutPut.append(RemoveEnter(string))
    buildingList = [[], [], [], [], []]
    for string in totalOutPut:
        ClassifyIntoBuilding(string, buildingList)
    fout.close()
    fin = open("stdin.txt", "r")
    testCase = []
    for string in fin.readlines():
        testCase.append(RemoveEnter(string))
    totalNameList = getReqNameList(testCase=testCase)
    totalFlag = True
    for i in range(0, 5):
        subFlag = True
        if i == 0:
            building = "A"
        elif i == 1:
            building = "B"
        elif i == 2:
            building = "C"
        elif i == 3:
            building = "D"
        else:
            building = "E"
        outPutList = buildingList[i]
        if not checkLogy(outPutList=outPutList):
            totalFlag = False
            subFlag = False
        if not checkPerson(totalNameList[i], outPutList, building=building):
            totalFlag = False
            subFlag = False
        if not checkPosition(outPutList):
            totalFlag = False
            subFlag = False
        if not checkTime(outPutList=outPutList, maxTime=max, realTime=realTime):
            totalFlag = False
            subFlag = False
        if subFlag:
            print("Elevetor " + building + ": Accepted")
        else:
            print("Elevetor " + building + ": Wrong Answer")
        print()
    if totalFlag:
        print("Result: Accepted")
    else:
        print("Result: Wrong Answer")
    return totalFlag

# check()
