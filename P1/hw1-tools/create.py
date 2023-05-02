import random
OP = ['', '+', '-']
NUM_MAX_LEN = 12            # 常数最大长度
TERM_MAX_SCALE = 3          # 一个项最多含有多少个因子
EXPR_MAX_SCALE = 8          # 一个表达式最多含有多少个项
SUB_EXPR_MAX_SCALE = 3      # 一个括号里的表达式最多含有多少个项


# 指数
def new_power():
    power_str = ""
    if random.randint(0, 1) > 0:
        power_str += "**"
        power_str += OP[random.randint(0, 1)]
        power_str += str(random.randint(0, 8))
    # print(power_str)
    return power_str


# 常数因子
def new_num():
    num_str = ""

    num_str += OP[random.randint(0, 2)]

    num_len = random.randint(1, NUM_MAX_LEN)
    for i in range(num_len):
        num_str += str(random.randint(0, 9))

    # print(num_str)
    return num_str


# 变量因子
def new_var():
    var_str = "x"

    var_str += new_power()

    # print(var_str)
    return var_str


# 因子
def new_factor(in_bracket=False):
    if in_bracket:
        factor_type = random.randint(1, 2)
    else:
        factor_type = random.randint(0, 2)

    if factor_type == 0:
        return "(" + new_expr(random.randint(1, SUB_EXPR_MAX_SCALE), True) + ")" + new_power()
    elif factor_type == 1:
        return new_num()
    else:
        return new_var()


# 项
def new_term(factor_cnt=1, in_bracket=False):
    term_str = ""

    if factor_cnt == 1:
        term_str += OP[random.randint(0, 2)]
        term_str += new_factor(in_bracket)
    else:
        factor_cnt -= 1
        term_str += new_term(factor_cnt, in_bracket)
        term_str += "*"
        term_str += new_factor(in_bracket)

    # print(term_str)
    return term_str


# 表达式
def new_expr(term_cnt=1, in_bracket=False):
    expr_str = ""

    if term_cnt == 1:
        expr_str += OP[random.randint(0, 2)]
    else:
        term_cnt -= 1
        expr_str += new_expr(term_cnt, in_bracket)
        expr_str += OP[random.randint(1, 2)]

    expr_str += new_term(random.randint(1, TERM_MAX_SCALE), in_bracket)

    # print(expr_str)
    return expr_str


def create():
    expr = new_expr(random.randint(1, EXPR_MAX_SCALE))
    while len(expr) >= 50:
        expr = new_expr(random.randint(1, EXPR_MAX_SCALE))
    return expr