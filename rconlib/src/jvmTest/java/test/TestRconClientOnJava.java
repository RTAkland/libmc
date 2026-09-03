/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/4
 */


package test;

import cn.rtast.mcping.rconlib.RCONClient;
import cn.rtast.mcping.rconlib.Rconlib;
import org.junit.Test;

public class TestRconClientOnJava {

    @Test
    public void testRconClient() {
        String host = "127.0.0.1";
        int port = 25575;
        String password = "123456";
        RCONClient rconClient = Rconlib.rconClient(host, port);
        boolean authed = rconClient.connect(password);
        if (authed) {
            System.out.println(rconClient.command("list"));
        } else {
            throw new IllegalStateException("incorrect password");
        }
    }
}
