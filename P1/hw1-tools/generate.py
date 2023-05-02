import subprocess
import compare


def generare(origin):
    proc_test = subprocess.Popen("java -jar source_code/hw2-test.jar", stdin=subprocess.PIPE, stdout=subprocess.PIPE,
                                 stderr=subprocess.PIPE, shell=True)
    proc_test.stdin.write(origin.encode())
    proc_test.stdin.write(b'\n^D\n')

    proc_zyl = subprocess.Popen("java -jar source_code/hw2-zyl.jar", stdin=subprocess.PIPE, stdout=subprocess.PIPE,
                                stderr=subprocess.PIPE, shell=True)
    proc_zyl.stdin.write(origin.encode())
    proc_zyl.stdin.write(b'\n^D\n')

    out_zyl, err_zyl = proc_zyl.communicate()

    output_zyl = str(out_zyl, 'utf-8')
    res_zyl = output_zyl.split("\r\n")[1]

    proc_zqy = subprocess.Popen("java -jar source_code/hw2-zqy.jar", stdin=subprocess.PIPE, stdout=subprocess.PIPE,
                                stderr=subprocess.PIPE, shell=True)
    proc_zqy.stdin.write(origin.encode())
    proc_zqy.stdin.write(b'\n^D\n')

    out_zqy, err_zqy = proc_zqy.communicate()

    output_zqy = str(out_zqy, 'utf-8')
    res_zqy = output_zqy.split("\r\n")[1]



    out_test, err_test = proc_test.communicate()

    output_test = str(out_test, 'utf-8')
    res_test = output_test.split("\r\n")[1]

    # print(origin)
    # print(res_zyl)
    # print(res_zqy)

    judge = compare.compare(origin, res_test, res_zyl, res_zqy)

    return judge
