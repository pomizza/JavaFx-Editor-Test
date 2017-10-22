package com.pku.model;

import java.io.File;

public class FileItem {
    File file;

    public FileItem(File file){
        this.file = file;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String toString(){
        return file.getName();
    }
}
