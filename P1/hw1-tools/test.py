import subprocess
import compare


def generare():
    print("generating test...")
    proc_test = subprocess.Popen("java -jar source_code/test.jar", stdin=subprocess.PIPE, stdout=subprocess.PIPE,
                                 stderr=subprocess.PIPE, shell=True)
    out_test, err_test = proc_test.communicate()
    print(out_test, end="\n")

    print("getting result_zyl...")
    proc_zyl = subprocess.Popen("java -jar source_code/hw2-zyl.jar", stdin=subprocess.PIPE, stdout=subprocess.PIPE,
                                stderr=subprocess.PIPE, shell=True)
    proc_zyl.stdin.write(out_test)

    out_zyl, err_zyl = proc_zyl.communicate()

    output_zyl = str(out_zyl, 'utf-8')
    res_zyl = output_zyl.split("\r\n")[1]

    print(res_zyl, end="\n")

    print("getting result_zqy...")
    proc_zqy = subprocess.Popen("java -jar source_code/hw2-zqy.jar", stdin=subprocess.PIPE, stdout=subprocess.PIPE,
                                stderr=subprocess.PIPE, shell=True)
    proc_zqy.stdin.write(out_test)

    out_zqy, err_zqy = proc_zqy.communicate()

    output_zqy = str(out_zqy, 'utf-8')
    res_zqy = output_zqy.split("\r\n")[1]

    print(res_zqy, end="\n")

    print("comparing...")
    judge = compare.compare(res_zyl, res_zqy)

    print(judge, end="\n")


if __name__ == "__main__":
    for i in range(10):
        generare()
