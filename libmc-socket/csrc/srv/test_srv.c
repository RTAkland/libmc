#include <stdio.h>
#include "srv.h"

int main(int argc, char* argv[]) {
    const char* domain = (argc > 1) ? argv[1] : "hypixel.net";
    SrvResult res = resolve_srv(domain);
    if (res.success) {
        printf("Target   : %s\n", res.target);
        printf("Port     : %u\n", res.port);
        printf("Priority : %u\n", res.priority);
        printf("Weight   : %u\n", res.weight);
    } else {
        printf("No SRV record found or query failed\n");
    }
    return 0;
}