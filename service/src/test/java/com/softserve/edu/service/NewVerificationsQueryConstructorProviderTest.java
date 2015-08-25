package com.softserve.edu.service;

import com.softserve.edu.entity.Organization;
import com.softserve.edu.entity.Verification;
import com.softserve.edu.entity.user.User;
import com.softserve.edu.entity.util.Status;
import com.softserve.edu.service.utils.NewVerificationsQueryConstructorProvider;
import com.softserve.edu.service.utils.SortCriteria;
import junit.framework.TestCase;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import javax.persistence.EntityManager;
import javax.persistence.criteria.*;

import java.lang.reflect.Method;

import static org.junit.Assert.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by vova on 20.08.15.
 */
@RunWith(MockitoJUnitRunner.class)
public class NewVerificationsQueryConstructorProviderTest {

    static Logger logger = Logger.getLogger(NewVerificationsQueryConstructorProvider.class);
    final static Long providerId = 1L;

    final static String dateToSearch = "dateToSearch";
    final static String idToSearch = "idToSearch";
    final static String lastNameToSearch = "yy";
    final static String streetToSearch = "streetToSearch";
    final static  String status = "TEST_OK";
    final static String employeeSearchName = "employeeSearchName";
    final static String userName = "userName";
    final static String sortCriteria = "str";
    final static String sortOrder = "str2";

    @InjectMocks
    private NewVerificationsQueryConstructorProvider newVerificationsQueryConstructorProvider;

    @Mock
    private Root<Verification> root;

    @Mock
    CriteriaQuery<Long> countQuery;

    @Mock
    private CriteriaBuilder cb;

    @Mock
    private Join<Object, Object> joinSearch;

    @Mock
    private User providerEmployee;

    @Mock
    private CriteriaQuery<Verification> criteriaQuery;

    @Mock
    private EntityManager em;

    @Mock
    private Predicate predicate;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testBuildSearchQuery() throws Exception {
        when(em.getCriteriaBuilder()).thenReturn(cb);
        when(cb.createQuery(Verification.class)).thenReturn(criteriaQuery);
        when(cb.createQuery(Verification.class)).thenReturn(criteriaQuery);
        when(criteriaQuery.from(Verification.class)).thenReturn(root);
        when(root.join("provider")).thenReturn(joinSearch);
        when(cb.conjunction()).thenReturn(predicate);
        CriteriaQuery<Verification> query = newVerificationsQueryConstructorProvider.buildSearchQuery( providerId,  dateToSearch, idToSearch, lastNameToSearch,
                streetToSearch, status, providerEmployee, sortCriteria, sortOrder, employeeSearchName, em);
        verify(criteriaQuery.orderBy(SortCriteria.valueOf(sortCriteria.toUpperCase()).getSortOrder(root, cb, sortOrder)));
/*
        when(em.getCriteriaBuilder()).thenReturn(cb);
        when(cb.createQuery(Verification.class)).thenReturn(criteriaQuery);
        when(criteriaQuery.from(Verification.class)).thenReturn(root);
        when(root.join("provider")).thenReturn(joinSearch);
        when(cb.conjunction()).thenReturn(predicate);
        when(cb.like(root.get("clientData").get("lastName"), "%" + lastNameToSearch + "%")).thenReturn(predicate);
       // when(cb.and(cb.like(root.get("clientData").get("lastName"), "%" + lastNameToSearch + "%"),
        //        predicate)).thenReturn(predicate);

*/
    }

    @Test
    public void testBuildCountQuery() throws Exception {
        when(em.getCriteriaBuilder()).thenReturn(cb);
        when(cb.createQuery(Long.class)).thenReturn(countQuery);
        when(criteriaQuery.from(Verification.class)).thenReturn(root);
        when(root.join("provider")).thenReturn(joinSearch);

        Method buildPredicate = newVerificationsQueryConstructorProvider.getClass().getDeclaredMethod("buildPredicate", Root.class, CriteriaBuilder.class, Join.class, Long.class,
                String.class, String.class, String.class, String.class, String.class, User.class, String.class
        );
        buildPredicate.setAccessible(true);
        when(buildPredicate.invoke(newVerificationsQueryConstructorProvider, root, cb, joinSearch, providerId,
                        dateToSearch, idToSearch, lastNameToSearch, streetToSearch, status, providerEmployee, employeeSearchName)
        ).thenReturn(predicate);

       doNothing().when(countQuery.select(cb.count(root)));
    }
}