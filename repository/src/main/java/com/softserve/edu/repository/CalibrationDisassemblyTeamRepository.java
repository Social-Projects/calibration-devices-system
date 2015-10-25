package com.softserve.edu.repository;

import com.softserve.edu.entity.catalogue.Team.DisassemblyTeam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;


public interface CalibrationDisassemblyTeamRepository extends
        PagingAndSortingRepository<DisassemblyTeam, String>, JpaSpecificationExecutor {

        Page<DisassemblyTeam> findAll(Pageable pageable);

        //Page<DisassemblyTeam> findByCalibratorId(Long calibratorId, Pageable pageable);

        Page<DisassemblyTeam> findByNameLikeIgnoreCase(String name, Pageable pageable);
}
