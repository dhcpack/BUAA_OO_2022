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
    add_person_cnt = 0
    add_group_cnt = 0
    query_circle_cnt = 0
    query_least_connection_cnt = 0
    file = open("stdin.txt", "w")
    # op_add = ['ap', 'ar', 'ag', 'atg', 'dfg', 'am', 'sm']
    for i in range(min(int(0.05 * num), add_person_max)):
        person_id = gen_person_id(0.1)
        age = random.randint(0, 200)
        add_person_cnt += 1
        request = "ap {} {} {}".format(person_id, person_id, age)
        file.write(request + '\n')
    for i in range(max(int(num * rate_add) - min(int(0.05 * num), add_person_max), 0)):
        a = random.uniform(0, 1)
        if a < 0.25 and add_person_cnt < add_person_max:
            person_id = gen_person_id(0.1)
            age = random.randint(0, 200)
            add_person_cnt += 1
            request = "ap {} {} {}".format(person_id, person_id, age)
        elif a < 0.5:
            person_id1 = gen_person_id(0.95)
            person_id2 = gen_person_id(0.95)
            value = random.randint(0, 1000)
            request = "ar {} {} {}".format(person_id1, person_id2, value)
        elif a < 0.6 and add_group_cnt < add_group_max:
            group_id = gen_group_id(0.1)
            request = "ag {}".format(group_id)
        elif a < 0.85:
            group_id = gen_group_id(0.95)
            person_id = gen_person_id(0.95)
            request = "atg {} {}".format(person_id, group_id)
        elif a < 0.9:
            group_id = gen_group_id(1)
            person_id = gen_person_id(1)
            request = "dfg {} {}".format(person_id, group_id)
        elif a < 0.95:
            message_id = gen_message_id(0.95)
            message_id_pool.remove(message_id)
            request = "sm {}".format(message_id)
        else:
            message_id = gen_message_id(0.95)
            social_value = random.randint(-1000, 1000)
            person_id1 = gen_person_id(1)
            type = random.randint(0, 1)
            if type == 0:
                person_id2 = gen_person_id(1)
                request = "am {} {} {} {} {}".format(message_id, social_value, type, person_id1, person_id2)
            else:
                group_id = gen_group_id(1)
                request = "am {} {} {} {} {}".format(message_id, social_value, type, person_id1, group_id)
        file.write(request + "\n")

    # op_query1 = ['qv', 'qps', 'qgps', 'qgvs', 'qsv', 'qrm']
    for i in range(int(num * rate_q1)):
        a = random.uniform(0, 1)
        if a < 0.2:
            person_id1 = gen_person_id(0.95)
            person_id2 = gen_person_id(0.95)
            request = "qv {} {}".format(person_id1, person_id2)
        elif a < 0.35:
            request = "qps"
        elif a < 0.5:
            group_id = gen_group_id(0.95)
            request = "qgps {}".format(group_id)
        elif a < 0.65:
            group_id = gen_group_id(0.95)
            request = "qgvs {}".format(group_id)
        elif a < 0.85:
            person_id = gen_person_id(0.95)
            request = "qsv {}".format(person_id)
        else:
            person_id = gen_person_id(0.95)
            request = "qrm {}".format(person_id)
        file.write(request + '\n')

    # op_query2 = ['qci', 'qbs', 'qgav', 'qlc']
    for i in range(int(num * rate_q2)):
        a = random.uniform(0, 1)
        if a < 0.25 and query_circle_cnt < query_circle_max:
            person_id1 = gen_person_id(0.99)
            person_id2 = gen_person_id(0.99)
            query_circle_cnt += 1
            request = "qci {} {}".format(person_id1, person_id2)
        elif a < 0.5:
            request = "qbs"
        elif a < 0.75 and query_least_connection_cnt < query_least_connection_max:
            person_id = gen_person_id(0.99)
            query_least_connection_cnt += 1
            request = "qlc {}".format(person_id)
        else:
            group_id = gen_group_id(0.99)
            request = "qgav {}".format(group_id)
        file.write(request + "\n")
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
    file = open("stdin.txt", "r")
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
    for i in range(1000):
        print("creating test point {}".format(i + 1))
        create(5000, 0.2, 0.1, 0.7)
        print("test point create succeed!")
        res_Alterego, time_Alterego = generare("Alterego.jar")
        res_Archer, time_Archer = generare("Archer.jar")
        res_Assassin, time_Assassin = generare("Assassin.jar")
        res_Berserker, time_Berserker = generare("Berserker.jar")
        res_Caster, time_Caster = generare("Caster.jar")
        res_Lancer, time_Lancer = generare("Lancer.jar")
        res_Rider, time_Rider = generare("Rider.jar")
        res_Saber, time_Saber = generare("Saber.jar")
        time_list = [time_Alterego, time_Archer, time_Assassin, time_Berserker, time_Caster, time_Lancer, time_Rider,
                     time_Saber]
        res_list = [res_Alterego, res_Archer, res_Assassin, res_Berserker, res_Caster, res_Lancer, res_Rider, res_Saber]
        res_set = {res_Lancer}
        for j in range(len(res_list)):
            res_set.add(res_list[j])
            if len(res_set) != 1:
                write_stdout("Alterego.txt", res_Alterego)
                write_stdout("Archer.txt", res_Archer)
                write_stdout("Assassin.txt", res_Assassin)
                write_stdout("Berserker.txt", res_Berserker)
                write_stdout("Caster.txt", res_Caster)
                write_stdout("Lancer.txt", res_Lancer)
                write_stdout("Rider.txt", res_Rider)
                write_stdout("Saber.txt", res_Saber)
                print(j)
                exit(-1)
        print(time_list)
        if max(time_list) > 10:
            print("exist TLE")
            exit(-1)
        print("Accepted!\n")
