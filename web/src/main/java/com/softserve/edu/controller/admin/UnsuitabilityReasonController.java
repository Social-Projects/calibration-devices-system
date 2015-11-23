package com.softserve.edu.controller.admin;

import com.softserve.edu.dto.PageDTO;
import com.softserve.edu.dto.admin.OrganizationDTO;
import com.softserve.edu.dto.admin.UnsuitabilityReasonDTO;
import com.softserve.edu.entity.device.UnsuitabilityReason;
import com.softserve.edu.service.admin.CounterTypeService;
import com.softserve.edu.service.admin.UnsuitabilityReasonService;
import com.softserve.edu.service.user.SecurityUserDetailsService;
import com.softserve.edu.service.utils.ListToPageTransformer;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by Sonka on 23.11.2015.
 */
@RestController
@RequestMapping(value = "/admin/unsuitability-reasons/")
public class UnsuitabilityReasonController {
       private final Logger logger = Logger.getLogger(DeviceController.class);
       @Autowired
       private UnsuitabilityReasonService unsuitabilityReasonService;
    @Autowired
    private CounterTypeService counterTypeService;
    /**
     * Saves unsuitability reason  in database
     * @param unsuitabilityReasonDTO object with unsuitability reason data
     * @return a response body with http status {@literal CREATED} if everything
     * device category successfully created or else http status {@literal CONFLICT}
     */
    @RequestMapping(value = "add", method = RequestMethod.POST)
    public ResponseEntity addUnsuitabilityReason(@RequestBody UnsuitabilityReasonDTO unsuitabilityReasonDTO) {
        HttpStatus httpStatus = HttpStatus.CREATED;
        try {
            unsuitabilityReasonService.addUnsuitabilityReason(
                    unsuitabilityReasonDTO.getName(),
                    unsuitabilityReasonDTO.getCounterId()
            );
        } catch (Exception e) {
            logger.error("Got exeption while add unsuitability reason", e);
            httpStatus = HttpStatus.CONFLICT;
        }
        return new ResponseEntity(httpStatus);
    }
    /**
     * Delete counter type
     * @param reasonId Long id of unsuitability reason
     * @return a response body with http status {@literal OK} if unsuitability reason
     * successfully deleted or else http status {@literal CONFLICT}
     */
    @RequestMapping(value = "delete/{reasonId}", method = RequestMethod.DELETE)
    public ResponseEntity removeUnsuitabilityReason(@PathVariable Long reasonId) {
        HttpStatus httpStatus = HttpStatus.OK;
        try {
            unsuitabilityReasonService.removeUnsuitabilityReason(reasonId);
        } catch (Exception e) {
            logger.error("Got exeption while remove unsuitability reason",e);
            httpStatus = HttpStatus.CONFLICT;
        }
        return new ResponseEntity(httpStatus);
    }
    @RequestMapping(value = "{pageNumber}/{itemsPerPage}/{sortCriteria}/{sortOrder}", method = RequestMethod.GET)
    public PageDTO<UnsuitabilityReasonDTO> pageUnsuitabilityReasonsWithSearch(@PathVariable Integer pageNumber, @PathVariable Integer itemsPerPage,
                                                           @PathVariable String sortCriteria, @PathVariable String sortOrder,
                                                           UnsuitabilityReasonDTO searchData) {
        ListToPageTransformer<UnsuitabilityReason> queryResult = unsuitabilityReasonService.getUnsuitabilityReasonBySearchAndPagination(
                pageNumber,
                itemsPerPage,
                searchData.getId(),
                searchData.getCounterTypeName(),
                searchData.getName(),
                sortCriteria,
                sortOrder
        );
        List<UnsuitabilityReasonDTO> content = toUnsuitabilityReasonDTOFromList(queryResult.getContent());
        return new PageDTO(queryResult.getTotalItems(), content);
    }
    /**
     * Build page without sorting, ordering and searching data
     * @param pageNumber
     * @param itemsPerPage
     * @return
     */
    @RequestMapping(value = "{pageNumber}/{itemsPerPage}", method = RequestMethod.GET)
    public PageDTO<UnsuitabilityReasonDTO> getUnsuitabilityReasonsPage(@PathVariable Integer pageNumber, @PathVariable Integer itemsPerPage) {
        return pageUnsuitabilityReasonsWithSearch(pageNumber, itemsPerPage, null, null, null);
    }
    /**
     * Get verificators that has agreement with this calibrator
     * @param user
     * @return
     */
    @RequestMapping(value = "counterTypes", method = RequestMethod.GET)
    public Set<OrganizationDTO> getVerification(@AuthenticationPrincipal SecurityUserDetailsService.CustomUserDetails user) {
        return counterTypeService.findAll().stream().map(organization ->
                new OrganizationDTO(organization.getId(), organization.getName())).collect(Collectors.toSet());
    }

    public static List<UnsuitabilityReasonDTO> toUnsuitabilityReasonDTOFromList(List<UnsuitabilityReason> list){
        List<UnsuitabilityReasonDTO> resultList = new ArrayList<>();
        for (UnsuitabilityReason unsuitabilityReason : list) {
            resultList.add(new UnsuitabilityReasonDTO(
                    unsuitabilityReason.getId(),
                    unsuitabilityReason.getName(),
                    unsuitabilityReason.getCounterType().getId(),
                    unsuitabilityReason.getCounterType().getName()
                 ));
        }
        return resultList;
    }
}
