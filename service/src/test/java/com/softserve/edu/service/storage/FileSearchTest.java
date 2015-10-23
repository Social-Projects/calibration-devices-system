package com.softserve.edu.service.storage;

import com.softserve.edu.service.storage.impl.FileSearch;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;

import static org.junit.Assert.*;

/**
 * Created by Yurko on 20.10.2015.
 */
public class FileSearchTest {
    /*@Rule
    public TemporaryFolder testFolder = new TemporaryFolder();

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testGetResult() throws Exception {

    }

    @Test
    public void testFind() throws Exception {
        File tempFile = testFolder.newFile("file.txt");
        tempFile.createNewFile();
        String result = FileSearch.find(testFolder.getRoot(), "file.txt");
        assertEquals(tempFile.getAbsolutePath(), result);
    }

    @Test
    public void test1Find() throws Exception {
        File tempFolder = testFolder.newFolder("folder");
        File tempFile = new File(tempFolder.getAbsolutePath() + File.separator + "file.txt");
        tempFile.createNewFile();
        String result = FileSearch.find(testFolder.getRoot(), "file.txt");
        assertEquals(tempFile.getAbsolutePath(), result);
    }

    *//**
     * try to find file that doesn't exist
     * @throws Exception
     *//*
    @Test
    public void test2Find() throws Exception {
        File tempFolder = testFolder.newFolder("a");
        String result = FileSearch.find(tempFolder.getAbsoluteFile(), "foo");
        assertEquals("", result);
    }

    @Test
    public void testSearchDirectory() throws Exception {
    }*/
}