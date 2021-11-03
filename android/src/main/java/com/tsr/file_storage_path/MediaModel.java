package com.tsr.file_storage_path;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class MediaModel {
    @SerializedName("folderName")
    @Expose
    String folder;
    @SerializedName("files")
    @Expose
    ArrayList<MetaData> files;


    public String getFolder() {
        return folder;
    }

    public void setFolder(String folder) {
        this.folder = folder;
    }

    public ArrayList<MetaData> getFiles() {
        return files;
    }

    public void setFiles(ArrayList<MetaData> files) {
        this.files = files;
    }

}
