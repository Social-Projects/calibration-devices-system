package com.softserve.edu.service.calibrator.impl;

import com.softserve.edu.device.test.data.DeviceTestData;
import com.softserve.edu.repository.UploadBbiRepository;
import com.softserve.edu.repository.VerificationRepository;
import com.softserve.edu.service.calibrator.BbiFileService;
import com.softserve.edu.service.parser.DeviceTestDataParser;
import com.softserve.edu.service.parser.DeviceTestDataParserFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@Service
public class BbiFileServiceImpl implements BbiFileService {

    @Autowired
    private UploadBbiRepository uploadBbiRepository;

    @Autowired
    private VerificationRepository verificationRepository;

    private DeviceTestDataParserFactory testDataParserFactory = new DeviceTestDataParserFactory();

    @Override
    @Transactional
    public File findBbiFileByFileName(String fileName) {
        String absolutePath = uploadBbiRepository.findFileAbsolutePathByFileName(fileName);
        File file = new File(absolutePath);
        return file;
    }

    @Override
    public DeviceTestData findBbiFileContentByFileName(String fileName) throws IOException {
        DeviceTestDataParser parser = testDataParserFactory.getParser(fileName);
        File bbiFile = findBbiFileByFileName(fileName);
        InputStream inputStream = new FileInputStream(bbiFile);
        DeviceTestData parsedData = parser.parse(inputStream);
        inputStream.close();
        return parsedData;
    }

    @Override
    public DeviceTestData parseBbiFile(InputStream fileStream, String fileName) throws IOException {
        DeviceTestDataParser parser = testDataParserFactory.getParser(fileName);
        DeviceTestData parsedData = parser.parse(fileStream);
        return parsedData;
    }

}
