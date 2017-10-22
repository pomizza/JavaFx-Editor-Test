package com.pku.concurrency;

import java.io.IOException;

public class RunProgram implements Runnable{
    String path;

    public RunProgram(String path) {
        this.path = path;
    }

    @Override
    public void run() {
        try {
            Runtime.getRuntime().exec("cd "+path);
            Runtime.getRuntime().exec("gradle -q run");
        } catch (IOException e) {
            System.out.println("命令运行出错");
            e.printStackTrace();
        }
    }
}
