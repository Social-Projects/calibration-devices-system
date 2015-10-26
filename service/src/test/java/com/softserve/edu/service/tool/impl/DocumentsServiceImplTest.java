package com.softserve.edu.service.tool.impl;

import com.softserve.edu.documents.DocumentFactory;
import com.softserve.edu.documents.FileFactory;
import com.softserve.edu.documents.document.Document;
import com.softserve.edu.documents.parameter.FileFormat;
import com.softserve.edu.documents.parameter.FileParameters;
import com.softserve.edu.documents.parameter.FileSystem;
import com.softserve.edu.documents.resources.DocumentType;
import com.softserve.edu.entity.enumeration.verification.CalibrationTestResult;
import com.softserve.edu.entity.verification.Verification;
import com.softserve.edu.entity.verification.calibration.CalibrationTest;
import com.softserve.edu.repository.CalibrationTestRepository;
import com.softserve.edu.repository.VerificationRepository;
import org.apache.commons.vfs2.FileObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.LinkedHashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;


/**
 * Created by Pavlo on 21.10.2015.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({FileParameters.class, FileFactory.class, DocumentFactory.class, DocumentsServiceImpl.class})

public class DocumentsServiceImplTest {
    @Mock
    private VerificationRepository verificationRepository;
    @Mock
    private CalibrationTestRepository calibrationTestRepository;
    @Mock
    private FileParameters fileParameters;
    @Mock
    private FileObject fileObject;
    @Mock
    private Verification verification;
    @Mock
    private Verification verification2;
    @Mock
    private CalibrationTest calibrationTest;
    @Mock
    private CalibrationTest calibrationTest2;
    @Mock
    private Document document;
    private DocumentType documentType;
    private FileFormat fileFormat;
    private String verificationCode = "id";
    private Set<CalibrationTest> calibrationTestSet;
    private Long calibrationTestID;
    @InjectMocks
    private DocumentsServiceImpl documentsService;

    @Before
    public void setUp() throws Exception {
        calibrationTestID = 1L;
        documentType = DocumentType.VERIFICATION_CERTIFICATE;
        fileFormat = FileFormat.DOCX;
        fileParameters.setFileSystem(FileSystem.RAM);
        fileParameters.setFileName(documentType.toString());
        PowerMockito.mockStatic(FileFactory.class);
        PowerMockito.mockStatic(DocumentFactory.class);
        when(verificationRepository.findOne(verificationCode)).thenReturn(verification);
        when(FileFactory.buildFile(fileParameters)).thenReturn(fileObject);
        when(DocumentFactory.build(documentType, verification, calibrationTest))
                .thenReturn(document);
        calibrationTestSet = new LinkedHashSet<>();
        calibrationTestSet.add(calibrationTest);
        when(verification.getCalibrationTests()).thenReturn(calibrationTestSet);

    }

    @Test
    public void testMainBuildFile() throws Exception {
        documentType = DocumentType.VERIFICATION_CERTIFICATE;
        PowerMockito.whenNew(FileParameters.class).withArguments(document, documentType, fileFormat)
                .thenReturn(fileParameters);
        FileObject expected = FileFactory.buildFile(fileParameters);
        FileObject actual = documentsService.buildFile(documentType, verification, calibrationTest, fileFormat);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testBuildFileWithoutTestResultWithSuccessResult() throws Exception {
        documentType = DocumentType.VERIFICATION_CERTIFICATE;
        when(calibrationTest.getTestResult()).thenReturn(CalibrationTestResult.SUCCESS);
        PowerMockito.whenNew(FileParameters.class).withArguments(document, documentType, fileFormat)
                .thenReturn(fileParameters);
        FileObject expected = FileFactory.buildFile(fileParameters);
        FileObject actual = documentsService.buildFile(verificationCode, fileFormat);
        assertEquals(expected, actual);
    }

    @Test
    public void testBuildFileWithoutTestResultWithFailedResult() throws Exception {
        documentType = DocumentType.UNFITNESS_CERTIFICATE;
        when(calibrationTest.getTestResult()).thenReturn(CalibrationTestResult.FAILED);
        when(DocumentFactory.build(documentType, verification, calibrationTest))
                .thenReturn(document);
        PowerMockito.whenNew(FileParameters.class).withArguments(document, documentType, fileFormat)
                .thenReturn(fileParameters);
        FileObject expected = FileFactory.buildFile(fileParameters);
        FileObject actual = documentsService.buildFile(verificationCode, fileFormat);
        assertEquals(expected, actual);
    }

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testBuildFileWithoutTestResultWithIncorrectResult() throws Exception {
        CalibrationTestResult wrongTestResult = CalibrationTestResult.valueOf("wrong");
        documentType = DocumentType.UNFITNESS_CERTIFICATE;
        when(calibrationTest.getTestResult()).thenReturn(wrongTestResult);
        when(DocumentFactory.build(documentType, verification, calibrationTest))
                .thenReturn(document);
        PowerMockito.whenNew(FileParameters.class).withArguments(document, documentType, fileFormat)
                .thenReturn(fileParameters);
        documentsService.buildFile(verificationCode, fileFormat);
    }

    @Test
    public void testBuildFileWithoutCalibrationTestWithWrongVerificationCode() throws Exception {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("can't find verification with id not verification code");
        documentsService.buildFile("not verification code", documentType, fileFormat);
    }

    @Test
    public void testBuildFileWithoutCalibrationTestWithoutAssignedCalibrationTests() throws Exception {
        calibrationTestSet = new LinkedHashSet<>();
        //calibrationTestSet.add(calibrationTest);
        when(verification.getCalibrationTests()).thenReturn(calibrationTestSet);
        thrown.expect(IllegalStateException.class);
        thrown.expectMessage(Verification.class.getSimpleName() + " with id " +
                verificationCode + " doesn't have any tests assigned to it");
        documentsService.buildFile(verificationCode, documentType, fileFormat);
    }

    @Test
    public void testBuildFileWithoutCalibrationTestWithTooMuchCalibrationTests() throws Exception {
        calibrationTestSet = new LinkedHashSet<>();
        calibrationTestSet.add(calibrationTest);
        calibrationTestSet.add(calibrationTest2);
        when(verification.getCalibrationTests()).thenReturn(calibrationTestSet);
        thrown.expect(IllegalStateException.class);
        thrown.expectMessage(Verification.class.getSimpleName() + " with id " +
                verificationCode + " have more than one test assigned to it. " +
                "Specify the id of a concrete test");
        documentsService.buildFile(verificationCode, documentType, fileFormat);
    }

    @Test
    public void testBuildFileWithoutCalibrationTest() throws Exception {
        PowerMockito.whenNew(FileParameters.class).withArguments(document, documentType, fileFormat)
                .thenReturn(fileParameters);
        FileObject expected = FileFactory.buildFile(fileParameters);

        FileObject actual = documentsService.buildFile(verificationCode, documentType, fileFormat);
        assertEquals(expected, actual);
    }

    @Test
    public void testBuildFileWithoutCalibrationTestAndVerification() throws Exception {
        when(calibrationTestRepository.findOne(calibrationTestID)).thenReturn(calibrationTest);
        when(calibrationTest.getVerification()).thenReturn(verification);
        PowerMockito.whenNew(FileParameters.class).withArguments(document, documentType, fileFormat)
                .thenReturn(fileParameters);
        FileObject expected = FileFactory.buildFile(fileParameters);
        FileObject actual = documentsService.buildFile(verificationCode, calibrationTestID, documentType, fileFormat);
        assertEquals(expected, actual);
    }

    @Test
    public void testBuildFileWithoutCalibrationTestAndVerificationWithWrongVerificationCode() throws Exception {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("can't find a " +
                Verification.class.getSimpleName() + " with id not verification code");
        documentsService.buildFile("not verification code", calibrationTestID, documentType, fileFormat);

    }

    @Test
    public void testBuildFileWithoutCalibrationTestAndVerificationWithWrongCalibrationTestID() throws Exception {
        Long wrongCalibrationID = 2L;
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("can't find a " +
                CalibrationTest.class.getSimpleName() + " with id2");
        documentsService.buildFile(verificationCode, wrongCalibrationID, documentType, fileFormat);
    }

    @Test
    public void testBuildWhereVerificationIsNotAssignedToCalibration() throws Exception {
        when(calibrationTestRepository.findOne(calibrationTestID)).thenReturn(calibrationTest);
        when(calibrationTest.getVerification()).thenReturn(verification);
        when(verificationRepository.findOne(verificationCode)).thenReturn(verification2);
        thrown.expect(IllegalStateException.class);
        thrown.expectMessage(calibrationTest.getClass() + " with id:" +
                CalibrationTest.class.getSimpleName() + " is not assigned to " +
                verification.getClass() + " with id: " + verification.getId());
        documentsService.buildFile(verificationCode, calibrationTestID, documentType, fileFormat);
    }

    @Test
    public void testBuildInfoFile() throws Exception {
        documentType = DocumentType.INFO_DOCUMENT;
        fileParameters.setDocumentType(documentType);
        when(DocumentFactory.buildInfoDoc(documentType, verification)).thenReturn(document);
        PowerMockito.whenNew(FileParameters.class)
                .withArguments(document, documentType, fileFormat).thenReturn(fileParameters);
        when(FileFactory.buildInfoFile(fileParameters)).thenReturn(fileObject);
        FileObject expected = FileFactory.buildInfoFile(fileParameters);
        FileObject actual = documentsService.buildInfoFile(verificationCode, fileFormat);
        assertEquals(expected, actual);


    }
}