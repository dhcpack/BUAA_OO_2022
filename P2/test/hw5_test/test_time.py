def test_time(filename):
    file = open(filename, "r")
    lines = file.read().split("\n")[:-1]
    file.close()
    last_time = 0
    for line in lines:
        time = float(line[1:line.index(']')])
        if not time >= last_time:
            raise Exception("Wrong time {}".format(time))
        last_time = time
    return


if __name__ == "__main__":
    # test_time("Alterego.txt")
    # test_time("Archer.txt")
    # test_time("Assassin.txt")
    # test_time("Berserker.txt")
    # test_time("Caster.txt")
    # test_time("Lancer.txt")
    # test_time("Rider.txt")
    # test_time("Saber.txt")
