package com.softserve.edu.repository;

import com.softserve.edu.entity.device.CalibrationModule;
import com.softserve.edu.entity.device.Device;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;

/**
 * Created by roman on 08.10.2015.
 *
 */

@Repository
 public interface CalibrationModuleRepository
        extends PagingAndSortingRepository<CalibrationModule, Long>, JpaSpecificationExecutor,
            CalibrationModuleRepositoryCustom {

    CalibrationModule findBySerialNumber(String serialNumber);

    @Query("SELECT MIN(C.workDate) FROM CalibrationModule C")
    Date findEarliestDateAvailableCalibrationModule();

}
