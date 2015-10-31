package com.softserve.edu.service.calibrator;

import com.softserve.edu.entity.device.CalibrationModule;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Vasyl on 08.10.2015.
 */
public interface CalibrationModuleService {

    List<String> findAllCalibrationModulsNumbers (String moduleType, Date workDate, String applicationFiled, String userName);

}
