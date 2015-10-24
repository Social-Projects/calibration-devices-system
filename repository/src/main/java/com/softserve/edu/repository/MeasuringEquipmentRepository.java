package com.softserve.edu.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.softserve.edu.entity.device.MeasuringEquipment;

@Repository
public interface MeasuringEquipmentRepository extends CrudRepository<MeasuringEquipment, Long> {
	
	Page<MeasuringEquipment> findAll(Pageable pageable);
	Page<MeasuringEquipment> findByNameLikeIgnoreCase(String name, Pageable pageable);

}
