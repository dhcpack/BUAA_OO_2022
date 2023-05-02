from cgi import test
import random
from unittest import TestCase
def getTimeStamps(num):
    times = []
    curtime = 0.0
    for i in range(0,num):
        curtime = curtime + random.uniform(0.0,0.5)
        curtime = round(curtime,2)
        times.append(curtime)
    return times

def getPersonReq(id,building,fromFloor,toFloor):
    string = str(id) + "-FROM-"+building+"-"+str(fromFloor)+"-TO-"+building+"-"+str(toFloor)
    return string

def mergeTimeAndReq(time,req):
    string = "["+str(time)+"]"+req
    return string

def getTestCase(num):
    times = getTimeStamps(num)
    testcase = []
    buildings = ["A","B","C","D","E"]
    ids = set()
    while len(ids) < num:
        ids.add(random.randint(1,9999))
    ids = list(ids)
    for i in range(0,num):
        building = buildings[random.randint(0,len(buildings)-1)]
        fromFloor = random.randint(1,10)
        toFloor = random.randint(1,10)
        while toFloor==fromFloor:
            toFloor = random.randint(1,10) ## 避免to和from相等
        id = ids[i]
        req = getPersonReq(id,building,fromFloor,toFloor)
        time = times[i]
        testcase.append(mergeTimeAndReq(time,req))
    return testcase



