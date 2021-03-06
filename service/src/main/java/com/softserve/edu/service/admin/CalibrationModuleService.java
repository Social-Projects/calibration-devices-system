package com.softserve.edu.service.admin;

import com.softserve.edu.entity.device.CalibrationModule;
import com.softserve.edu.entity.device.Device;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Pavlo on 02.11.2015.
 */
public interface CalibrationModuleService {
    CalibrationModule addCalibrationModule(CalibrationModule calibrationModule);

    CalibrationModule findModuleById(Long calibrationModuleId);

    void deleteCalibrationModule(Long moduleId);

    void disableCalibrationModule(Long calibrationModuleId);

    void enableCalibrationModule(Long calibrationModuleId);

    Page<CalibrationModule> findAllModules(Pageable pageable);

    List<CalibrationModule> findAllModules();

    Page<CalibrationModule> getFilteredPageOfCalibrationModule(Map<String, Object> searchKeys, Pageable pageable);

    List<String> findAllSerialNumbers(CalibrationModule.ModuleType moduleType, Date workDate,
                                      Device.DeviceType applicationField, String userName);

    void updateCalibrationModule(Long moduleId, CalibrationModule calibrationModule);

    Date getEarliestDate();

    List<CalibrationModule> findAllActing();

}
