package com.softserve.edu.service.utils;

import com.softserve.edu.entity.enumeration.user.UserRole;
import com.softserve.edu.entity.organization.Organization;
import com.softserve.edu.entity.user.User;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.criteria.*;

/**
 * @deprecated this class have a lot of repeated code <br/>
 * {need to be replaced and removed}<br/>
 * use {@link com.softserve.edu.specification.SpecificationBuilder} instead
 */
@Deprecated
public class ProviderEmployeeQuery {

    static Logger logger = Logger.getLogger(ArchivalVerificationsQueryConstructorProvider.class);

    public static CriteriaQuery<User> buildSearchQuery(String userName, String role, String firstName,
                                                       String lastName, String organization, String telephone, String secondTelephone,
                                                       EntityManager em, Long idOrganization, String fieldToSort) {


        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<User> criteriaQuery = cb.createQuery(User.class);
        Root<User> root = criteriaQuery.from(User.class);
        Join<User, Organization> joinSearch = root.join("organization");

        Predicate predicate = ProviderEmployeeQuery.buildPredicate(root, cb, joinSearch, userName,
                role, firstName, lastName, organization, telephone, secondTelephone, idOrganization);
        if (fieldToSort.length() > 0) {
            if (fieldToSort.substring(0, 1).equals("-")) {
                if (fieldToSort.substring(1, fieldToSort.length()).equals("userRoles")) {
                    //todo fix or delete ordering by user roles
                    //criteriaQuery.orderBy(cb.desc(joinRole.get(fieldToSort.substring(1, fieldToSort.length()))));
                    //criteriaQuery.orderBy(cb.desc(root.get(fieldToSort.substring(1, fieldToSort.length()))));
                    //criteriaQuery.orderBy(cb.desc(root.<Set<UserRole>>get(fieldToSort.substring(1, fieldToSort.length()))));
                } else {
                    criteriaQuery.orderBy(cb.desc(root.get(fieldToSort.substring(1, fieldToSort.length()))));
                }
            } else {
                if (fieldToSort.equals("userRoles")) {
                    //criteriaQuery.orderBy(cb.asc(joinRole.get(fieldToSort)));
                    //criteriaQuery.orderBy(cb.asc(root.get(fieldToSort)));
                    //criteriaQuery.orderBy(cb.asc(root.<Set<UserRole>>get(fieldToSort)));

                } else {
                    criteriaQuery.orderBy(cb.asc(root.get(fieldToSort)));
                }
            }
        }
        criteriaQuery.select(root).distinct(true);
        criteriaQuery.where(predicate);
        return criteriaQuery;
    }


    private static Predicate buildPredicate(Root<User> root, CriteriaBuilder cb,
                                            Join<User, Organization> joinSearch, String userName, String role,
                                            String firstName, String lastName, String organization, String telephone, String secondTelephone,
                                            Long idOrganization) {
        Predicate queryPredicate = cb.conjunction();
        if (idOrganization != null) {
            queryPredicate = cb.and(cb.equal(joinSearch.get("id"), idOrganization), queryPredicate);
        }
        if ((userName != null) && !userName.isEmpty()) {
            queryPredicate = cb.and(cb.like(root.get("username"), "%" + userName + "%"), queryPredicate);
        }
        if ((role != null) && !role.isEmpty()) {
            UserRole uRole = UserRole.valueOf(role.trim());
            queryPredicate = cb.and(cb.isMember(uRole, root.get("userRoles")), queryPredicate);
        }
        if ((firstName != null) && !firstName.isEmpty()) {
            queryPredicate = cb.and(cb.like(root.get("firstName"), "%" + firstName + "%"), queryPredicate);
        }
        if ((lastName != null) && !lastName.isEmpty()) {
            queryPredicate = cb.and(cb.like(root.get("lastName"), "%" + lastName + "%"), queryPredicate);
        }
        if ((organization != null) && !organization.isEmpty()) {
            queryPredicate = cb.and(cb.like(root.get("organization").get("name"), "%" + organization + "%"), queryPredicate);
        }
        if ((telephone != null) && !telephone.isEmpty()) {
            queryPredicate = cb.and(cb.like(root.get("phone"), "%" + telephone + "%"), queryPredicate);
        }
        if ((secondTelephone != null) && !secondTelephone.isEmpty()) {
            queryPredicate = cb.and(cb.like(root.get("secondPhone"), "%" + secondTelephone + "%"), queryPredicate);
        }
        return queryPredicate;
    }

    public static CriteriaQuery<Long> buildCountQuery(String userName, String role, String firstName,
                                                      String lastName, String organization, String telephone, String secondTelephone,
                                                      Long idOrganization, EntityManager em) {

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<User> root = countQuery.from(User.class);
        Join<User, Organization> joinSearch = root.join("organization");
        Predicate predicate = ProviderEmployeeQuery.buildPredicate(root, cb, joinSearch, userName, role,
                firstName, lastName, organization, telephone, secondTelephone, idOrganization);

        countQuery.select(cb.countDistinct(root));
        countQuery.where(predicate);
        return countQuery;
    }
}
