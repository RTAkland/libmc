/*
 * Copyright © 2026 RTAkland
 * Author: RTAkland
 * Date: 2026/9/3
 */


package test;

import cn.rtast.libmc.mcping.McPing;
import cn.rtast.libmc.mcping.PingResponse;
import cn.rtast.libmc.mcping.ServerType;
import org.junit.Test;


public class TestPingInJava {

    @Test
    public void testPingJava() {
        String testJavaHost = "org.mc-complex.com";
        PingResponse resp = McPing.mcping(testJavaHost, 25565);
        System.out.println(resp);
    }

    @Test
    public void testPingBedrock() {
        String testBedrockHost = "play.wildnetwork.net";
        PingResponse resp = McPing.mcping(testBedrockHost, 19132, ServerType.Bedrock);
        System.out.println(resp.getContent());
        System.out.println(resp.getLatency());
        System.out.println(resp.toBedrockResponse());
    }
}
