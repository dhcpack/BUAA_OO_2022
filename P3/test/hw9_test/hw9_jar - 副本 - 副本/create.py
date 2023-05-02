import random
import subprocess
from threading import Timer
from contextlib import contextmanager
import threading
import _thread
import time


class TimeoutException(Exception):
    def __init__(self, msg=''):
        self.msg = msg


@contextmanager
def time_limit(seconds, msg=''):
    timer = threading.Timer(seconds, lambda: _thread.interrupt_main())
    timer.start()
    try:
        yield
    except KeyboardInterrupt:
        raise TimeoutException("Timed out for operation {}".format(msg))
    finally:
        # if the action ends in specified time, timer is canceled
        timer.cancel()


op = ['ap', 'ar', 'qv', 'qps', 'qci', 'qbs', 'ag', 'atg', 'dfg']
person_id_pool = set()
group_id_pool = set()
timeout_sec = 20000


def create(type, num, ratea, rateq):
    file = open("stdin_create.txt", "w")
    if type == 1:
        for i in range(int(num * 0.1)):
            a = random.uniform(0, 1)
            if a < 0.7:
                person_id = gen_person_id(0.1)
                age = random.randint(0, 200)
                request = "ap {} {} {}".format(person_id, person_id, age)
            else:
                person_id1 = gen_person_id(0.9)
                person_id2 = gen_person_id(0.9)
                value = random.randint(0, 1000)
                request = "ar {} {} {}".format(person_id1, person_id2, value)
            file.write(request + "\n")

        for i in range(int(num * 0.9)):
            a = random.uniform(0, 1)
            if a < 0.15:
                person_id = gen_person_id(0.1)
                age = random.randint(0, 200)
                request = "ap {} {} {}".format(person_id, person_id, age)
            elif 0.15 <= a < 0.3:
                person_id1 = gen_person_id(0.9)
                person_id2 = gen_person_id(0.9)
                value = random.randint(0, 1000)
                request = "ar {} {} {}".format(person_id1, person_id2, value)
            elif 0.3 <= a < 0.4:
                person_id1 = gen_person_id(0.9)
                person_id2 = gen_person_id(0.9)
                request = "qv {} {}".format(person_id1, person_id2)
            elif 0.4 <= a < 0.5:
                request = "qps"
            elif 0.5 <= a < 0.6:
                person_id1 = gen_person_id(0.9)
                person_id2 = gen_person_id(0.9)
                request = "qci {} {}".format(person_id1, person_id2)
            elif 0.6 <= a < 0.7:
                request = "qbs"
            elif 0.7 <= a < 0.8:
                group_id = gen_group_id(0.1)
                request = "ag {}".format(group_id)
            elif 0.8 <= a < 0.9:
                group_id = gen_group_id(0.9)
                person_id = gen_person_id(0.9)
                request = "atg {} {}".format(person_id, group_id)
            else:
                group_id = gen_group_id(1)
                person_id = gen_person_id(1)
                request = "dfg {} {}".format(person_id, group_id)
            file.write(request + "\n")
    else:
        for i in range(num):
            a = random.uniform(0, 1)
            if a < ratea:
                person_id = gen_person_id(0.1)
                age = random.randint(0, 200)
                request = "ap {} {} {}".format(person_id, person_id, age)
            else:
                person_id1 = gen_person_id(0.9)
                person_id2 = gen_person_id(0.9)
                value = random.randint(0, 1000)
                request = "ar {} {} {}".format(person_id1, person_id2, value)
            file.write(request + '\n')
        for i in range(1000 - num):
            a = random.uniform(0, 1)
            if a < rateq:
                request = "qbs"
            else:
                person_id1 = gen_person_id(0.9)
                person_id2 = gen_person_id(0.9)
                request = "qci {} {}".format(person_id1, person_id2)
            file.write(request + '\n')

    file.close()


def gen_person_id(exist_rate):
    rate = random.uniform(0, 1)
    if rate < exist_rate and len(person_id_pool) != 0:
        person_id = random.choice(list(person_id_pool))
    else:
        person_id = random.randint(0, 100000000)
    person_id_pool.add(person_id)
    return person_id


def gen_group_id(exist_rate):
    rate = random.uniform(0, 1)
    if rate < exist_rate and len(group_id_pool) != 0:
        group_id = random.choice(list(group_id_pool))
    else:
        group_id = random.randint(0, 100000000)
    group_id_pool.add(group_id)
    return group_id


def signal_handler():
    raise RuntimeError


def generare(program):
    file = open("stdin_create.txt", "r")
    stdin = file.read()

    proc = subprocess.Popen("java -jar " + program, stdin=subprocess.PIPE, stdout=subprocess.PIPE,
                            stderr=subprocess.PIPE, shell=True)
    start_time = time.time()
    stdout, stderr = proc.communicate(stdin.encode())
    end_time = time.time()
    print(program, end=" ")
    print(end_time - start_time)
    return str(stdout, encoding="utf-8").replace("\r", ""), end_time - start_time


def write_stdout(filename, text):
    file = open(filename, "w")
    file.write(text)
    file.close()


if __name__ == "__main__":
    # rate_a = [0, 0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9, 1]
    # rate_q = [1, 0.9, 0.8, 0.7, 0.6, 0.5, 0.4, 0.3, 0.2, 0.1, 0]
    # for type in range(0, 2):
    #     for num in range(0, 1001, 100):
    #         for i in range(11):
    #             print("creating point type = {}, num = {}, ratea = {}, rateq = {}".format(type, num, rate_a[i],
    #                                                                                       rate_q[i]))
    #             create(type, num, rate_a[i], rate_q[i])
    #             print("test point create succeed!")
    #             res_zyl, time_zyl = generare("hw9.jar")
    #             res_zqy, time_zqy = generare("zqy-hw9.jar")
    #             write_stdout("zyl_stdout_1.txt", res_zyl)
    #             write_stdout("zqy_stdout_1.txt", res_zqy)
    #             if not res_zyl == res_zqy:
    #                 break
    #             print("Accepted!\n")
    file = open("res.txt", "w")

    rate_a = [0, 0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9, 1]
    rate_q = [1, 0.9, 0.8, 0.7, 0.6, 0.5, 0.4, 0.3, 0.2, 0.1, 0]
    for a in range(1):
        for type in range(0, 2):
            for num in range(0, 1001, 100):
                for i in range(11):
                    print("creating point type = {}, num = {}, ratea = {}, rateq = {}".format(type, num, rate_a[i],
                                                                                              rate_q[i]))
                    create(type, num, rate_a[i], rate_q[i])
                    print("test point create succeed!")

                    res_Alterego, time_Alterego = generare("Alterego.jar")
                    res_Archer, time_Archer = generare("Archer.jar")
                    res_Assassin, time_Assassin = generare("Assassin.jar")
                    res_Berserker, time_Berserker = generare("Berserker.jar")
                    res_Caster, time_Caster = generare("Caster.jar")
                    res_Lancer, time_Lancer = generare("Lancer.jar")
                    res_Rider, time_Rider = generare("Rider.jar")
                    res_Saber, time_Saber = generare("Saber.jar")
                    time_list = [time_Saber, time_Rider, time_Lancer, time_Caster, time_Berserker, time_Assassin,
                                 time_Archer, time_Alterego]
                    res_list = [res_Rider, res_Saber, res_Lancer, res_Caster, res_Berserker, res_Assassin, res_Archer,
                                res_Alterego]
                    res_set = {res_Alterego}
                    for i in range(len(res_list)):
                        res_set.add(res_list[i])
                        if len(res_set) != 1:
                            write_stdout("Alterego.txt", res_Alterego)
                            write_stdout("Archer.txt", res_Archer)
                            write_stdout("Assassin.txt", res_Assassin)
                            write_stdout("Berserker.txt", res_Berserker)
                            write_stdout("Caster.txt", res_Caster)
                            write_stdout("Lancer.txt", res_Lancer)
                            write_stdout("Rider.txt", res_Rider)
                            write_stdout("Saber.txt", res_Saber)
                            print(i)
                            exit(-1)
                    if max(time_list) > 2:
                        print(time_list)
                        file.write(
                            "type = {}, num = {}, ratea = {}, rateq = {}\n".format(type, num, rate_a[i], rate_q[i]))
                        file.write((str(time_list) + "\n"))
                        for _ in time_list:
                            if _ > 2:
                                file.write(str(_))
                    print("Accepted!\n")
        file.write("\n\n\n")
    file.close()
