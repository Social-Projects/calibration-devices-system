package com.softserve.edu.service.calibrator;


import com.softserve.edu.entity.catalogue.Team.DisassemblyTeam;
import com.softserve.edu.entity.device.Device;
import com.softserve.edu.entity.organization.Organization;
import com.softserve.edu.service.exceptions.DuplicateRecordException;
import org.springframework.data.domain.Page;

import java.util.Date;
import java.util.List;

public interface CalibratorDisassemblyTeamService {

    List<DisassemblyTeam> getAll();

    List<DisassemblyTeam> getByOrganization(Organization organization);

    Page<DisassemblyTeam> getByOrganization(Organization organization, int pageNumber, int itemsPerPage);

    Page<DisassemblyTeam> findByOrganizationAndSearchAndPagination(int pageNumber, int itemsPerPage,
                                                                   Organization organization, String search);

    void add(DisassemblyTeam disassemblyTeam) throws DuplicateRecordException;

    DisassemblyTeam findById(String teamId);

    void edit(String id, String name, Date effectiveTo, Device.DeviceType specialization, String leaderFullName,
              String leaderPhone, String leaderEmail);

    void delete(String teamId);

}
