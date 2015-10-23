package com.softserve.edu.service.utils;

import com.softserve.edu.service.verification.VerificationService;
import junit.framework.TestCase;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by lyubomyr on 22.10.2015.
 */
public class ProcessTimeCheckerTest extends TestCase {

    @Mock
    private VerificationService verificationService;

    @InjectMocks
    ProcessTimeChecker processTimeChecker;

    public void setUp() throws Exception {
        processTimeChecker = new ProcessTimeChecker();

        MockitoAnnotations.initMocks(this);
    }

    public void tearDown() throws Exception {
        processTimeChecker = null;
    }

    public void testRunProcessTimeCheck() throws Exception {
        processTimeChecker.runProcessTimeCheck();
        List<Object[]> providerList = mock(List.class);
        List<Object[]> calibratorList = mock(List.class);
        List<Object[]> verificatorList = mock(List.class);
        when(verificationService.getProcessTimeProvider()).thenReturn(calibratorList);
        when(verificationService.getProcessTimeCalibrator()).thenReturn(providerList);
        when(verificationService.getProcessTimeVerificator()).thenReturn(verificatorList);
        verify(verificationService).getProcessTimeProvider();
        verify(verificationService).getProcessTimeCalibrator();
        verify(verificationService).getProcessTimeVerificator();
    }
}