import random
import subprocess
import time

# 支持的26个操作
op_add = ['ap', 'ar', 'ag', 'atg', 'dfg', 'am', 'arem', 'anm', 'aem', 'sei', 'dce']
op_query1 = ['qv', 'qps', 'qgps', 'qgvs', 'qsv', 'qrm', 'sm', 'cn', 'qp', 'qm']
op_query2 = ['qci', 'qbs', 'qgav', 'qlc', 'sim']
person_id_pool = set()
group_id_pool = set()
message_id_pool = set()
emoji_id_pool = set()

timeout_sec = 10


# add_person_max = 5000
# add_group_max = 25
# query_circle_max = 333
# query_least_connection_max = 100
# send_indirect_message_max = 1000


def create(num, rate_add, rate_q1, rate_q2):
    # add_person_cnt = 0
    # add_group_cnt = 0
    # query_circle_cnt = 0
    # query_least_connection_cnt = 0
    file = open("stdin.txt", "w")
    # op_add = ['ap', 'ar', 'ag', 'atg', 'dfg', 'am', 'arem', 'anm', 'aem', 'sei', 'dce']
    for i in range(int(0.05 * num)):
        person_id = gen_person_id(0.1)
        age = random.randint(0, 200)
        request = "ap {} {} {}".format(person_id, person_id, age)
        file.write(request + '\n')
    for i in range(max(int(rate_add * num) - int(0.05 * num), 0)):
        a = random.uniform(0, 1)
        if a < 0.15:
            person_id = gen_person_id(0.1)
            age = random.randint(0, 200)
            request = "ap {} {} {}".format(person_id, person_id, age)
        elif a < 0.3:
            person_id1 = gen_person_id(0.95)
            person_id2 = gen_person_id(0.95)
            value = random.randint(0, 1000)
            request = "ar {} {} {}".format(person_id1, person_id2, value)
        elif a < 0.4:
            group_id = gen_group_id(0.1)
            request = "ag {}".format(group_id)
        elif a < 0.5:
            group_id = gen_group_id(0.95)
            person_id = gen_person_id(0.95)
            request = "atg {} {}".format(person_id, group_id)
        elif a < 0.55:
            group_id = gen_group_id(1)
            person_id = gen_person_id(1)
            request = "dfg {} {}".format(person_id, group_id)
        elif a < 0.65:
            message_id = gen_message_id(0.95)
            message_id_pool.remove(message_id)
            request = "sm {}".format(message_id)
        elif a < 0.7:
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
        elif a < 0.8:
            message_id = gen_message_id(0.95)
            money = random.randint(0, 200)
            person_id1 = gen_person_id(1)
            type = random.randint(0, 1)
            if type == 0:
                person_id2 = gen_person_id(1)
                request = "arem {} {} {} {} {}".format(message_id, money, type, person_id1, person_id2)
            else:
                group_id = gen_group_id(1)
                request = "arem {} {} {} {} {}".format(message_id, money, type, person_id1, group_id)
        elif a < 0.85:
            message_id = gen_message_id(0.95)
            string = "This_is_No.{}_notice_message".format(message_id)
            person_id1 = gen_person_id(1)
            type = random.randint(0, 1)
            if type == 0:
                person_id2 = gen_person_id(1)
                request = "anm {} {} {} {} {}".format(message_id, string, type, person_id1, person_id2)
            else:
                group_id = gen_group_id(1)
                request = "anm {} {} {} {} {}".format(message_id, string, type, person_id1, group_id)
        elif a < 0.9:
            message_id = gen_message_id(0.95)
            emoji_id = gen_emoji_id(0.95)
            person_id1 = gen_person_id(1)
            type = random.randint(0, 1)
            if type == 0:
                person_id2 = gen_person_id(1)
                request = "aem {} {} {} {} {}".format(message_id, emoji_id, type, person_id1, person_id2)
            else:
                group_id = gen_group_id(1)
                request = "aem {} {} {} {} {}".format(message_id, emoji_id, type, person_id1, group_id)
        elif a < 0.95:
            emoji_id = gen_emoji_id(0.95)
            request = "sei {}".format(emoji_id)
        else:
            if len(emoji_id_pool) == 0:
                limit = 0
            else:
                limit = int((len(message_id_pool) / len(emoji_id_pool)))
            request = "dce {}".format(limit)
        file.write(request + "\n")

    # op_query1 = ['qv', 'qps', 'qgps', 'qgvs', 'qsv', 'qrm', 'sm', 'cn', 'qp', 'qm']
    for i in range(int(num * rate_q1)):
        a = random.uniform(0, 1)
        if a < 0.1:
            person_id1 = gen_person_id(0.95)
            person_id2 = gen_person_id(0.95)
            request = "qv {} {}".format(person_id1, person_id2)
        elif a < 0.2:
            request = "qps"
        elif a < 0.3:
            group_id = gen_group_id(0.95)
            request = "qgps {}".format(group_id)
        elif a < 0.4:
            group_id = gen_group_id(0.95)
            request = "qgvs {}".format(group_id)
        elif a < 0.5:
            person_id = gen_person_id(0.95)
            request = "qsv {}".format(person_id)
        elif a < 0.6:
            person_id = gen_person_id(0.95)
            request = "qrm {}".format(person_id)
        elif a < 0.7:
            message_id = gen_message_id(0.95)
            request = "sm {}".format(message_id)
        elif a < 0.8:
            person_id = gen_person_id(0.95)
            request = "cn {}".format(person_id)
        elif a < 0.9:
            emoji_id = gen_emoji_id(0.95)
            request = "qp {}".format(emoji_id)
        else:
            person_id = gen_person_id(0.95)
            request = "qm {}".format(person_id)
        file.write(request + '\n')

    # op_query2 = ['qci', 'qbs', 'qgav', 'qlc', 'sim']
    for i in range(int(num * rate_q2)):
        a = random.uniform(0, 1)
        if a < 0.2:
            person_id1 = gen_person_id(0.99)
            person_id2 = gen_person_id(0.99)
            request = "qci {} {}".format(person_id1, person_id2)
        elif a < 0.4:
            request = "qbs"
        elif a < 0.6:
            person_id = gen_person_id(0.99)
            request = "qlc {}".format(person_id)
        elif a < 0.8:
            group_id = gen_group_id(0.99)
            request = "qgav {}".format(group_id)
        else:
            message_id = gen_message_id(1)
            request = "sim {}".format(message_id)
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


def gen_emoji_id(exist_rate):
    rate = random.uniform(0, 1)
    if rate < exist_rate and len(emoji_id_pool) != 0:
        emoji_id = random.choice(list(emoji_id_pool))
    else:
        emoji_id = random.randint(0, 100000000)
        while emoji_id in emoji_id_pool:
            emoji_id = random.randint(0, 100000000)
    emoji_id_pool.add(emoji_id)
    return emoji_id


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
    for i in range(1):
        print("checking test point No.{}".format(i + 1))
        # refresh = input("refresh(y or n): ")
        refresh = "y"
        if refresh == 'y':
            # num = int(input("指令条数: "))
            num = 4000000
            create(num, 0.3, 0.1, 0.6)
            print("test point create succeed!")
        else:
            print("using testpoint before.")

        res_zyl, time_zyl = generare("hw11.jar")
        print("res_zyl get")
        # res_lcy, time_lcy = generare("hw11-lcy.jar")
        # print("res_lcy get")
        res_zqy, time_zqy = generare("hw11-zqy.jar")
        print("res_zqy get")
        write_stdout("zyl_stdout.txt", res_zyl)
        # write_stdout("lcy_stdout.txt", res_lcy)
        write_stdout("zqy_stdout.txt", res_zqy)
        # if not res_zyl == res_lcy:
        #     print("diff 1")
        #     break
        if not res_zyl == res_zqy:
            print("diff 2")
            break
        print("time zyl is {}".format(time_zyl))
        # print("time lcy is {}".format(time_lcy))
        print("time zqy is {}".format(time_zqy))
        print("Accepted!\n")
