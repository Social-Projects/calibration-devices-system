package com.softserve.edu.repository.impl;

import com.softserve.edu.entity.device.CalibrationModule;
import com.softserve.edu.entity.device.Device;
import com.softserve.edu.repository.CalibrationModuleRepository;
import com.softserve.edu.repository.CalibrationModuleRepositoryCustom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by roman on 13.11.15.
 *
 */

@Repository
public class CalibrationModuleRepositoryImpl implements CalibrationModuleRepositoryCustom {

    @Autowired
    CalibrationModuleRepository calibrationModuleRepository;

    public CalibrationModule saveWithGenerating(CalibrationModule calibrationModule) {
        calibrationModuleRepository.save(calibrationModule);
        generateModuleNumber(calibrationModule);
        return calibrationModuleRepository.save(calibrationModule);
    }

    private void generateModuleNumber(CalibrationModule cm) {
        Map<Device.DeviceType, Integer> moduleNumbers = new HashMap<Device.DeviceType, Integer>();
        Device.DeviceType[] deviceTypes = Device.DeviceType.values();
        String prefix = ""; // Added
        for (int i = 0; i < deviceTypes.length; i++) {
            moduleNumbers.put(deviceTypes[i], i + 1);
            // Added:
            if (cm.getDeviceType().contains(deviceTypes[i])) {
                prefix += i + 1;
            }
        }
        // 1%03d -> %%03d
        cm.setModuleNumber(String.format("%%03d", prefix, cm.getModuleId()));
    }

}
