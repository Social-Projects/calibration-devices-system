package com.softserve.edu.service.calibrator.impl;

import com.softserve.edu.entity.enumeration.verification.Status;
import com.softserve.edu.entity.user.User;
import com.softserve.edu.entity.verification.Verification;
import com.softserve.edu.repository.VerificationRepository;
import com.softserve.edu.service.calibrator.CalibratorDigitalProtocolsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * @author Veronika 11.11.2015
 */
@Service
public class CalibratorDigitalProtocolsServiceImpl implements CalibratorDigitalProtocolsService {

    @Autowired
    private VerificationRepository verificationRepository;

    @PersistenceContext
    private EntityManager em;

    public Long countByCalibratorEmployee_usernameAndStatus(User calibratorEmployee, Status status) {
        return verificationRepository.countByCalibratorEmployee_usernameAndStatus(calibratorEmployee.getUsername(), status);
    }

    /**
     * Find and return from database Verifications by user and status
     * is used for table with protocols
     *
     * @param calibratorEmployee - user
     * @param pageNumber
     * @param itemsPerPage
     * @param status
     * @return verifications
     */
    @Transactional(readOnly = true)
    public List<Verification> findPageOfVerificationsByCalibratorIdAndStatus(
            User calibratorEmployee, int pageNumber, int itemsPerPage, Status status) {

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Verification> cq = cb.createQuery(Verification.class);
        Root<Verification> verifications = cq.from(Verification.class);

        cq.where(cb.and(cb.equal(verifications.get("calibratorEmployee"), calibratorEmployee),
                cb.equal(verifications.get("status"), status)));

        TypedQuery<Verification> typedQuery = em.createQuery(cq);
        typedQuery.setFirstResult((pageNumber - 1) * itemsPerPage);
        typedQuery.setMaxResults(itemsPerPage);

        return typedQuery.getResultList();
    }
}
