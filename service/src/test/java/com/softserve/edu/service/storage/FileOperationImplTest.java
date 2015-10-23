package com.softserve.edu.service.storage;

import com.softserve.edu.service.storage.impl.FileOperationImpl;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.operations.FileOperation;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

/**
 * Created by Yurko on 21.10.2015.
 */
public class FileOperationImplTest {
    /*
    FileOperationImpl fileOperationImpl;

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    private String localStorage;

    private String photoPathPref;

    @Before
    public void setUp() {
        //localStorage = mock(String.class);
        photoPathPref = "";

        fileOperationImpl = new FileOperationImpl();
    }

    @Test
    public void testPutResourse() throws Exception {
    }

    @Test
    public void testGetResourseURI() throws Exception {
        photoPathPref = folder.newFolder("dir").getAbsolutePath();
        String relativeFilePath = File.separator + "folder" + File.separator + "file.txt";
        String fullPath = path + relativeFilePath;
        assertEquals(fullPath, fileOperationImpl.getResourseURI(relativeFilePath));
    }*/
}