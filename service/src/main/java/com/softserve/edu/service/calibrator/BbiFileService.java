package com.softserve.edu.service.calibrator;

import com.softserve.edu.device.test.data.DeviceTestData;

/**
 * Created by Taras on 07.10.2015.
 */
// TODO Add description of the service
public interface BbiFileService {

    byte[] findBbiFileBytesByFileName(String fileName);

    public DeviceTestData findBbiFileContentByFileName(String fileName);
}
