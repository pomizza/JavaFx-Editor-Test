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
            Runtime.getRuntime().exec("D:\\Program Files\\gradle-4.3-rc-1\\bin\\gradle -q run "+path);
        } catch (IOException e) {
            System.out.println("命令运行出错 path"+path);
            e.printStackTrace();
        }
    }
}
