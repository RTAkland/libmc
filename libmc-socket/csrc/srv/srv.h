#ifndef SRV_H
#define SRV_H

#include <stdint.h>

typedef struct {
    char target[256];
    uint16_t port;
    uint16_t priority;
    uint16_t weight;
    int success;
} SrvResult;

// query srv record
SrvResult resolve_srv(const char* domain);

#endif // SRV_H