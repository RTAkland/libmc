#include "srv.h"
#include <stdio.h>
#include <string.h>

#ifdef _WIN32
#include <windows.h>
#include <windns.h>
#pragma comment(lib, "dnsapi.lib")
#else
#include <sys/types.h>
#include <netinet/in.h>
#include <arpa/nameser.h>
#include <resolv.h>
#if defined(__APPLE__) || defined(__MACH__)
#define RES_CLOSE(state) res_ndestroy(state)
#else
#define RES_CLOSE(state) res_nclose(state)
#endif
#endif

SrvResult resolve_srv(const char * domain) {
    SrvResult result;
    memset(&result, 0, sizeof(SrvResult));
    if (!domain || strlen(domain) == 0) return result;
    char srv_domain[300];
    if (strncmp(domain, "_minecraft._tcp.", 16) == 0) {
        snprintf(srv_domain, sizeof(srv_domain), "%s", domain);
    } else {
        snprintf(srv_domain, sizeof(srv_domain), "_minecraft._tcp.%s", domain);
    }

#ifdef _WIN32
    PDNS_RECORD pDnsRecord = NULL;
    DNS_STATUS status = DnsQuery_A(srv_domain, DNS_TYPE_SRV, DNS_QUERY_STANDARD, NULL, &pDnsRecord, NULL);
    if (status == ERROR_SUCCESS && pDnsRecord != NULL) {
        PDNS_RECORD pCurr = pDnsRecord;
        while (pCurr != NULL) {
            if (pCurr->wType == DNS_TYPE_SRV) {
                result.priority = (uint16_t)pCurr->Data.Srv.wPriority;
                result.weight   = (uint16_t)pCurr->Data.Srv.wWeight;
                result.port     = (uint16_t)pCurr->Data.Srv.wPort;
                snprintf(result.target, sizeof(result.target), "%s", pCurr->Data.Srv.pNameTarget);
                size_t len = strlen(result.target);
                if (len > 0 && result.target[len - 1] == '.') {
                    result.target[len - 1] = '\0';
                }

                result.success = 1;
                break;
            }
            pCurr = pCurr->pNext;
        }
        DnsRecordListFree(pDnsRecord, DnsFreeRecordListDeep);
    }
#else
    struct __res_state state;
    if (res_ninit(&state) != 0) return result;
    unsigned char response[1024];
    int len = res_nquery(&state, srv_domain, 1, 33, response, sizeof(response));
    if (len > 0) {
        ns_msg handle;
        if (ns_initparse(response, len, &handle) == 0) {
            int count = ns_msg_count(handle, ns_s_an);
            for (int i = 0; i < count; i++) {
                ns_rr rr;
                if (ns_parserr(&handle, ns_s_an, i, &rr) == 0) {
                    if (ns_rr_type(rr) == 33 && ns_rr_rdlen(rr) >= 6) {
                        const unsigned char * rdata = ns_rr_rdata(rr);
                        result.priority = (uint16_t)((rdata[0] << 8) | rdata[1]);
                        result.weight   = (uint16_t)((rdata[2] << 8) | rdata[3]);
                        result.port     = (uint16_t)((rdata[4] << 8) | rdata[5]);
                        if (ns_name_uncompress(ns_msg_base(handle), ns_msg_end(handle),
                                               rdata + 6, result.target, sizeof(result.target)) >= 0) {

                            size_t tlen = strlen(result.target);
                            if (tlen > 0 && result.target[tlen - 1] == '.') {
                                result.target[tlen - 1] = '\0';
                            }
                            result.success = 1;
                            break;
                        }
                    }
                }
            }
        }
    }
    RES_CLOSE(&state);
#endif
    return result;
}