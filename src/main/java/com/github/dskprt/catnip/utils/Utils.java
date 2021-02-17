package com.github.dskprt.catnip.utils;

import java.lang.management.ManagementFactory;

public class Utils {

    //TODO a more universal solution?
    public static String getCurrentPid() {
        return ManagementFactory.getRuntimeMXBean().getName().split("@")[0];
    }
}
