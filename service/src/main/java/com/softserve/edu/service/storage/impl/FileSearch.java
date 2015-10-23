package com.softserve.edu.service.storage.impl;

import java.io.File;

public class FileSearch {

    private static String fileNameToSearch;
    private static String result = "";

    public static String getFileNameToSearch() {
        return fileNameToSearch;
    }

    public static void setFileNameToSearch(String fileNameToSearch) {
        FileSearch.fileNameToSearch = fileNameToSearch;
    }

    public static String getResult() {
        return result;
    }

    public static String find(File directory, String fileName) {

        FileSearch.searchDirectory(directory, fileName);
        return getResult();
    }

    public static void searchDirectory(File directory, String fileNameToSearch) {

        setFileNameToSearch(fileNameToSearch);

        if (directory.isDirectory()) {
            searchFile(directory);
        } else {
            System.out.println(directory.getAbsoluteFile() + " is not a directory!");
        }
    }

    private static void searchFile(File file) {
        if (!file.isDirectory() || !file.canRead())
            return;

        for (File temp : file.listFiles()) {
            if (temp.isDirectory()) {
                searchFile(temp);
                continue;
            }
            if (getFileNameToSearch().equalsIgnoreCase(temp.getName())) {
                result = (temp.getAbsoluteFile().toString());
            }

        }

    }

}