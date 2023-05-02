#include <stdio.h>
#include <time.h>

#ifdef WIN32
#include <windows.h>
#define SLEEP_MS(x) Sleep((x))
#else
#include <unistd.h>
#define SLEEP_MS(x) usleep(1000*(x))
#endif

char buf[1005];
long curMillis;

int main()
{
    long millis;
    while (scanf("%ld:", &millis) != EOF) {
        gets(buf);
        SLEEP_MS(millis - curMillis);
        puts(buf);
        fflush(stdout);
        curMillis = millis;
    }

    return 0;
}
