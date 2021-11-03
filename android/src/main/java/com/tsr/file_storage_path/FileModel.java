package com.tsr.file_storage_path;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class FileModel {
    @SerializedName("folderName")
    @Expose
    String folder;

    @SerializedName("files")
    @Expose
    ArrayList<String> files;

    public String getFolder() {
        return folder;
    }

    public void setFolder(String folder) {
        this.folder = folder;
    }

    public ArrayList<String> getFiles() {
        return files;
    }

    public void setFiles(ArrayList<String> files) {
        this.files = files;
    }
}
