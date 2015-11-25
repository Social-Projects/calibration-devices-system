package com.softserve.edu.service.calibrator.impl;

import com.softserve.edu.device.test.data.DeviceTestData;
import com.softserve.edu.repository.VerificationRepository;
import com.softserve.edu.service.calibrator.BBIFileServiceFacade;
import com.softserve.edu.service.calibrator.BbiFileService;
import com.softserve.edu.service.calibrator.CalibratorService;
import com.softserve.edu.service.calibrator.data.test.CalibrationTestService;
import com.softserve.edu.service.utils.BBIOutcomeDTO;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.CloseShieldInputStream;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.sql.*;
import java.util.*;

@Service
public class BBIFileServiceFacadeImpl implements BBIFileServiceFacade {
    private static final String[] bbiExtensions = {"bbi", "BBI"};
    private static final String[] dbfExtensions = {"db", "dbf", "DB", "DBF"};

    private final Logger logger = Logger.getLogger(BBIFileServiceFacadeImpl.class);


    @Autowired
    private BbiFileService bbiFileService;

    @Autowired
    private CalibratorService calibratorService;

    @Autowired
    private CalibrationTestService calibrationTestService;

    @Override
    public DeviceTestData parseAndSaveBBIFile(File BBIfile, String verificationID, String originalFileName) throws IOException, NoSuchElementException, DecoderException {
        DeviceTestData deviceTestData;
        try (InputStream inputStream = FileUtils.openInputStream(BBIfile)){
            deviceTestData = parseAndSaveBBIFile(inputStream, verificationID, originalFileName);
            calibrationTestService.createNewTest(deviceTestData, verificationID);
        } catch (DecoderException e) {
            throw e;
        }
        return deviceTestData;
    }


    public DeviceTestData parseAndSaveBBIFile(MultipartFile BBIfile, String verificationID, String originalFileName) throws IOException, NoSuchElementException, DecoderException {
        DeviceTestData deviceTestData = parseAndSaveBBIFile(BBIfile.getInputStream(), verificationID, originalFileName);
        return deviceTestData;
    }

    @Transactional
    public DeviceTestData parseAndSaveBBIFile(InputStream inputStream, String verificationID, String originalFileName) throws IOException, DecoderException {
        BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
        bufferedInputStream.mark(inputStream.available());
        DeviceTestData deviceTestData = bbiFileService.parseBbiFile(bufferedInputStream, originalFileName);
        bufferedInputStream.reset();
        calibratorService.uploadBbi(bufferedInputStream, verificationID, originalFileName);
        return deviceTestData;
    }

    public List<BBIOutcomeDTO> parseAndSaveArchiveOfBBIfiles(File archive, String originalFileName) throws IOException, ZipException, SQLException, ClassNotFoundException {
        try(InputStream inputStream = FileUtils.openInputStream(archive)){
            return parseAndSaveArchiveOfBBIfiles(inputStream, originalFileName);
        }
    }


    public List<BBIOutcomeDTO> parseAndSaveArchiveOfBBIfiles(MultipartFile archiveFile, String originalFileName) throws IOException, ZipException, SQLException, ClassNotFoundException {
        List<BBIOutcomeDTO> resultsOfBBIProcessing = parseAndSaveArchiveOfBBIfiles(archiveFile.getInputStream(), originalFileName);
        return resultsOfBBIProcessing;
    }

    @Transactional
    public List<BBIOutcomeDTO> parseAndSaveArchiveOfBBIfiles(InputStream archiveStream, String originalFileName) throws IOException, ZipException, SQLException, ClassNotFoundException {
        File directoryWithUnpackedFiles = unpackArchive(archiveStream, originalFileName);
        Map<String, String> bbiFileNamesToVerificationMap = getBBIfileNamesToVerificationMap(directoryWithUnpackedFiles);
        List<File> listOfBBIfiles = new ArrayList<>(FileUtils.listFiles(directoryWithUnpackedFiles, bbiExtensions, true));
        List<BBIOutcomeDTO> resultsOfBBIProcessing = processListOfBBIFiles(bbiFileNamesToVerificationMap, listOfBBIfiles);
        FileUtils.forceDelete(directoryWithUnpackedFiles);
        return resultsOfBBIProcessing;
    }

    /**
     *
     * @param bbiFileNamesToVerificationMap Map of BBI files names to their corresponding verifications
     * @param listOfBBIfiles List with BBI files extracted from the archive
     * @return List of DTOs containing BBI filename, verification id, outcome of parsing (true/false), and reason of rejection (if the bbi file was rejected)
     */
    private List<BBIOutcomeDTO> processListOfBBIFiles(Map<String, String> bbiFileNamesToVerificationMap, List<File> listOfBBIfiles){
        List<BBIOutcomeDTO> resultsOfBBIProcessing = new ArrayList<>();
        for (File bbiFile : listOfBBIfiles) {
            String correspondingVerification = bbiFileNamesToVerificationMap.getOrDefault(bbiFile.getName(), null);
            if(correspondingVerification == null){
                resultsOfBBIProcessing.add(BBIOutcomeDTO.reject(bbiFile.getName(), correspondingVerification, BBIOutcomeDTO.ReasonOfRejection.NO_CORRESPONDING_VERIFICATION));
                continue;
            }
            try {
                parseAndSaveBBIFile(bbiFile, correspondingVerification, bbiFile.getName());
            } catch (NoSuchElementException e) {
                resultsOfBBIProcessing.add(BBIOutcomeDTO.reject(bbiFile.getName(), correspondingVerification, BBIOutcomeDTO.ReasonOfRejection.INVALID_VERIFICATION_CODE));
                logger.info(e); // for prevent critical issue "Either log or rethrow this exception"
                continue;
            } catch (Exception e) {
                resultsOfBBIProcessing.add(BBIOutcomeDTO.reject(bbiFile.getName(), correspondingVerification, BBIOutcomeDTO.ReasonOfRejection.BBI_IS_NOT_VALID));
                logger.info(e); // for prevent critical issue "Either log or rethrow this exception"
                continue;
            }
            resultsOfBBIProcessing.add(BBIOutcomeDTO.accept(bbiFile.getName(), correspondingVerification));
        }
        return resultsOfBBIProcessing;
    }

    /**
     * Unpacks file into temporary directory
     * @param inputStream InputStream representing archive file
     * @param originalFileName Name of the archive
     * @return Directory to which the archive was unpacked
     * @throws IOException
     * @throws ZipException
     */
    private File unpackArchive(InputStream inputStream, String originalFileName) throws IOException, ZipException {
        String randomDirectoryName = RandomStringUtils.randomAlphanumeric(8);
        File directoryForUnpacking = FileUtils.getFile(FileUtils.getTempDirectoryPath(), randomDirectoryName);
        FileUtils.forceMkdir(directoryForUnpacking);
        File zipFileDownloaded = FileUtils.getFile(FileUtils.getTempDirectoryPath(), originalFileName);

        try (OutputStream os = new FileOutputStream(zipFileDownloaded)) {
            IOUtils.copy(inputStream, os);
        }

        ZipFile zipFile = new ZipFile(zipFileDownloaded);
        zipFile.extractAll(directoryForUnpacking.toString());
        FileUtils.forceDelete(zipFileDownloaded);
        return directoryForUnpacking;
    }

    /**
     *
     * @param directoryWithUnpackedFiles Directory with unpacked files (should include BBIs and DBF)
     * @return Map of BBI files names to their corresponding verifications
     * @throws SQLException
     * @throws ClassNotFoundException
     * @throws FileNotFoundException
     * @implNote Uses sqlite to open DBF
     */
    private Map<String, String> getBBIfileNamesToVerificationMap(File directoryWithUnpackedFiles) throws SQLException, ClassNotFoundException, FileNotFoundException {
        Map<String, String> bbiFilesToVerification = new LinkedHashMap<>();
        Optional<File> foundDBFile = FileUtils.listFiles(directoryWithUnpackedFiles, dbfExtensions, true).stream().findFirst();
        File dbFile = foundDBFile.orElseThrow(() -> new FileNotFoundException("DBF not found"));

        Class.forName("org.sqlite.JDBC");

        try(Connection connection = DriverManager.getConnection("jdbc:sqlite:" + dbFile)) {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT FileNumber, Id_pc FROM Results");
            while (rs.next()) {
                String fileNumber = rs.getString("FileNumber");
                String verificationID = rs.getString("Id_pc");
                bbiFilesToVerification.put(fileNumber, verificationID);
            }
        }
        return bbiFilesToVerification;
    }
}
