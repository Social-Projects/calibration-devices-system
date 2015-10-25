package com.softserve.edu.repository;

import com.softserve.edu.entity.device.CounterType;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CounterTypeRepository extends CrudRepository<CounterType, Long> {

}
