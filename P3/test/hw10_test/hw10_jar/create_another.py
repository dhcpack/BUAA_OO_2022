import random
import subprocess
import time

# 支持的17个操作
op_add = ['ap', 'ar', 'ag', 'atg', 'dfg', 'am', 'sm']
op_query1 = ['qv', 'qps', 'qgps', 'qgvs', 'qsv', 'qrm']
op_query2 = ['qci', 'qbs', 'qgav', 'qlc']
person_id_pool = set()
group_id_pool = set()
message_id_pool = set()
timeout_sec = 10
add_person_max = 2500
add_group_max = 25
query_circle_max = 333
query_least_connection_max = 100


def create(num, rate_add, rate_q1, rate_q2):
    person1_relation_pool = {1}
    add_person_cnt = 0
    add_group_cnt = 0
    query_circle_cnt = 0
    query_least_connection_cnt = 0
    file = open("stdin_another.txt", "w")
    # op_add = ['ap', 'ar', 'ag', 'atg', 'dfg', 'am', 'sm']
    person_id = 1
    age = 200
    add_person_cnt += 1
    request = "ap {} {} {}".format(person_id, person_id, age)
    file.write(request + '\n')
    nums = [2499, len(person_id_pool), 20, 100]
    for i in range(2499):
        person_id = gen_person_id(0)
        age = 200
        add_person_cnt += 1
        request = "ap {} {} {}".format(person_id, person_id, age)
        file.write(request + '\n')
    for person_id in range(len(person_id_pool)):
        # person_id1 = gen_person_id(1)
        # person_id2 = gen_person_id(1)
        value = 1000
        request = "ar {} {} {}".format(1, person_id, value)
        file.write(request + "\n")
    for i in range(10000 - sum(nums)):
        person_id1 = gen_person_id(1)
        person_id2 = gen_person_id(1)
        value = 1000
        request = "ar {} {} {}".format(person_id1, person_id2, value)
        file.write(request + "\n")
    for i in range(200000):
        person_id = 1
        query_least_connection_cnt += 1
        request = "qlc {}".format(person_id)
        file.write(request + "\n")
    for i in range(100):
        person_id1 = gen_person_id(1)
        person_id2 = gen_person_id(1)
        query_circle_cnt += 1
        request = "qci {} {}".format(person_id1, person_id2)
        file.write(request + "\n")
    # for i in range(10000 - sum(nums)):
    #     request = "qbs"
    #     file.write(request + "\n")
    file.close()


def gen_person_id(exist_rate):
    rate = random.uniform(0, 1)
    if rate < exist_rate and len(person_id_pool) != 0:
        person_id = random.choice(list(person_id_pool))
    else:
        person_id = random.randint(0, 100000000)
        while person_id in person_id_pool:
            person_id = random.randint(0, 100000000)
    person_id_pool.add(person_id)
    return person_id


def gen_group_id(exist_rate):
    rate = random.uniform(0, 1)
    if rate < exist_rate and len(group_id_pool) != 0:
        group_id = random.choice(list(group_id_pool))
    else:
        group_id = random.randint(0, 100000000)
        while group_id in group_id_pool:
            group_id = random.randint(0, 100000000)
    group_id_pool.add(group_id)
    return group_id


def gen_message_id(exist_rate):
    rate = random.uniform(0, 1)
    if rate < exist_rate and len(message_id_pool) != 0:
        message_id = random.choice(list(message_id_pool))
    else:
        message_id = random.randint(0, 100000000)
        while message_id in message_id_pool:
            message_id = random.randint(0, 100000000)
    message_id_pool.add(message_id)
    return message_id


def generare(program):
    file = open("stdin_another.txt", "r")
    stdin = file.read()

    proc = subprocess.Popen("java -jar " + program, stdin=subprocess.PIPE, stdout=subprocess.PIPE,
                            stderr=subprocess.PIPE, shell=True)
    start_time = time.time()
    stdout, stderr = proc.communicate(stdin.encode())
    end_time = time.time()
    # print(end_time - start_time)
    return str(stdout, encoding="utf-8").replace("\r", ""), end_time - start_time


def write_stdout(filename, text):
    file = open(filename, "w")
    file.write(text)
    file.close()


if __name__ == "__main__":
    for i in range(1):
        print("checking test point No.{}".format(i + 1))
        # refresh = input("refresh(y or n): ")
        refresh = "y"
        if refresh == 'y':
            # num = int(input("指令条数: "))
            num = 500000
            create(num, 0.3, 0.1, 0.6)
            print("test point create succeed!")
        else:
            print("using testpoint before.")

        res_zyl, time_zyl = generare("hw10.jar")
        res_zqy = generare("hw10-zqy.jar")
        write_stdout("zyl_stdout_another.txt", res_zyl)
        # write_stdout("zqy_stdout.txt", res_zqy)
        # if not res_zyl == res_zqy:
        #     break
        print(time_zyl)
        print("Accepted!\n")
