package com.softserve.edu.service.calibrator.impl;

import com.softserve.edu.common.Constants;
import com.softserve.edu.device.test.data.DeviceTestData;
import com.softserve.edu.entity.Address;
import com.softserve.edu.entity.device.Counter;
import com.softserve.edu.entity.device.CounterType;
import com.softserve.edu.entity.enumeration.verification.Status;
import com.softserve.edu.entity.organization.Organization;
import com.softserve.edu.entity.user.User;
import com.softserve.edu.entity.verification.ClientData;
import com.softserve.edu.entity.verification.Verification;
import com.softserve.edu.service.admin.CounterTypeService;
import com.softserve.edu.service.admin.OrganizationService;
import com.softserve.edu.service.calibrator.BBIFileServiceFacade;
import com.softserve.edu.service.calibrator.BbiFileService;
import com.softserve.edu.service.calibrator.CalibratorService;
import com.softserve.edu.service.calibrator.data.test.CalibrationTestService;
import com.softserve.edu.service.utils.BBIOutcomeDTO;
import com.softserve.edu.service.verification.VerificationService;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

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
    @Autowired
    private VerificationService verificationService;
    @Autowired
    private CounterTypeService counterTypeService;
    @Autowired
    private OrganizationService organizationService;


    @Override
    public DeviceTestData parseAndSaveBBIFile(File BBIfile, String verificationID, String originalFileName) throws IOException, NoSuchElementException, DecoderException {
        DeviceTestData deviceTestData;
        try (InputStream inputStream = FileUtils.openInputStream(BBIfile)) {
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

    public List<BBIOutcomeDTO> parseAndSaveArchiveOfBBIfiles(File archive, String originalFileName, User calibratorEmployee)
            throws IOException, ZipException, SQLException, ClassNotFoundException, ParseException {
        try (InputStream inputStream = FileUtils.openInputStream(archive)) {
            return parseAndSaveArchiveOfBBIfiles(inputStream, originalFileName, calibratorEmployee);
        }
    }


    public List<BBIOutcomeDTO> parseAndSaveArchiveOfBBIfiles(MultipartFile archiveFile, String originalFileName,
                                                             User calibratorEmployee) throws IOException, ZipException,
            SQLException, ClassNotFoundException, ParseException {
        List<BBIOutcomeDTO> resultsOfBBIProcessing = parseAndSaveArchiveOfBBIfiles(archiveFile.getInputStream(), originalFileName, calibratorEmployee);
        return resultsOfBBIProcessing;
    }

    @Transactional
    public List<BBIOutcomeDTO> parseAndSaveArchiveOfBBIfiles(InputStream archiveStream, String originalFileName,
                                                             User calibratorEmployee) throws IOException,
            ZipException, SQLException, ClassNotFoundException, ParseException {
        File directoryWithUnpackedFiles = unpackArchive(archiveStream, originalFileName);
        Map<String, Map<String, String>> bbiFileNamesToVerificationMap = getBBIfileNamesToVerificationMap(directoryWithUnpackedFiles);
        List<File> listOfBBIfiles = new ArrayList<>(FileUtils.listFiles(directoryWithUnpackedFiles, bbiExtensions, true));
        List<BBIOutcomeDTO> resultsOfBBIProcessing = processListOfBBIFiles(bbiFileNamesToVerificationMap, listOfBBIfiles,
                calibratorEmployee);
        FileUtils.forceDelete(directoryWithUnpackedFiles);
        return resultsOfBBIProcessing;
    }

    /**
     * @param bbiFileNamesToVerificationMap Map of BBI files names to their corresponding verifications
     * @param listOfBBIfiles                List with BBI files extracted from the archive
     * @return List of DTOs containing BBI filename, verification id, outcome of parsing (true/false), and reason of rejection (if the bbi file was rejected)
     */
    private List<BBIOutcomeDTO> processListOfBBIFiles(Map<String, Map<String, String>> bbiFileNamesToVerificationMap,
                                                      List<File> listOfBBIfiles, User calibratorEmployee) throws ParseException {
        List<BBIOutcomeDTO> resultsOfBBIProcessing = new ArrayList<>();

        for (File bbiFile : listOfBBIfiles) {
            Map<String, String> correspondingVerificationMap = bbiFileNamesToVerificationMap.get(bbiFile.getName());
            String correspondingVerification = correspondingVerificationMap.get(Constants.VERIFICATION_ID);
            if (correspondingVerification == null) {
                correspondingVerification = createNewVerificationFromMap(correspondingVerificationMap, calibratorEmployee);
            } else {
                updateVerificationFromMap(correspondingVerificationMap, correspondingVerification);
            }
            try {
                parseAndSaveBBIFile(bbiFile, correspondingVerification, bbiFile.getName());
            } catch (NoSuchElementException e) {
                resultsOfBBIProcessing.add(BBIOutcomeDTO.reject(bbiFile.getName(), correspondingVerification, BBIOutcomeDTO.ReasonOfRejection.INVALID_VERIFICATION_CODE));
                logger.info(e); // for prevent critical issue "Either log or rethrow this exception"
                continue;
            } catch (Exception e) {
                resultsOfBBIProcessing.add(BBIOutcomeDTO.reject(bbiFile.getName(), correspondingVerification,
                        BBIOutcomeDTO.ReasonOfRejection.BBI_IS_NOT_VALID));
                logger.info(e); // for prevent critical issue "Either log or rethrow this exception"
                continue;
            }
            resultsOfBBIProcessing.add(BBIOutcomeDTO.accept(bbiFile.getName(), correspondingVerification));
        }

        return resultsOfBBIProcessing;
    }


    /**
     * Unpacks file into temporary directory
     *
     * @param inputStream      InputStream representing archive file
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
     * @param directoryWithUnpackedFiles Directory with unpacked files (should include BBIs and DBF)
     * @return Map of BBI files names to their corresponding verifications
     * @throws SQLException
     * @throws ClassNotFoundException
     * @throws FileNotFoundException
     * @implNote Uses sqlite to open DBF
     */
    private Map<String, Map<String, String>> getBBIfileNamesToVerificationMap(File directoryWithUnpackedFiles) throws SQLException, ClassNotFoundException, FileNotFoundException {
        Map<String, Map<String, String>> bbiFilesToVerification = new LinkedHashMap<>();
        Map<String, String> verificationMap;
        Optional<File> foundDBFile = FileUtils.listFiles(directoryWithUnpackedFiles, dbfExtensions, true).stream().findFirst();
        File dbFile = foundDBFile.orElseThrow(() -> new FileNotFoundException("DBF not found"));

        Class.forName("org.sqlite.JDBC");

        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:" + dbFile)) {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM Results");
            while (rs.next()) {
                verificationMap = new LinkedHashMap<>();
                String fileNumber = rs.getString("FileNumber");
                String verificationID = rs.getString("Id_pc");
                String lastName = rs.getString("Surname");
                String firstName = rs.getString("Name");
                String middleName = rs.getString("Middlename");
                String city = rs.getString("City");
                String district = rs.getString("District");
                String street = rs.getString("Street");
                String building = rs.getString("Building");
                String flat = rs.getString("Apartment");
                String phone = rs.getString("TelNumber");
                String provider = rs.getString("Customer");
                String date = rs.getString("Date");
                String counterNumber = rs.getString("CounterNumber");
                String type = rs.getString("Type");
                String year = rs.getString("Year");
                String account = rs.getString("Account");

                verificationMap.put(Constants.VERIFICATION_ID, verificationID);
                verificationMap.put(Constants.PROVIDER, provider);
                verificationMap.put(Constants.DATE, date);
                verificationMap.put(Constants.COUNTER_NUMBER, counterNumber);
                verificationMap.put(Constants.COUNTER_SIZE_AND_SYMBOL, type);
                verificationMap.put(Constants.YEAR, year);
                verificationMap.put(Constants.STAMP, account);
                verificationMap.put(Constants.LAST_NAME, lastName);
                verificationMap.put(Constants.FIRST_NAME, firstName);
                verificationMap.put(Constants.MIDDLE_NAME, middleName);
                verificationMap.put(Constants.PHONE_NUMBER, phone);
                verificationMap.put(Constants.REGION, district);
                verificationMap.put(Constants.CITY, city);
                verificationMap.put(Constants.STREET, street);
                verificationMap.put(Constants.BUILDING, building);
                verificationMap.put(Constants.FLAT, flat);
                bbiFilesToVerification.put(fileNumber, verificationMap);
            }
        }
        return bbiFilesToVerification;
    }

    private String createNewVerificationFromMap(Map<String, String> verificationData, User calibratorEmployee) throws ParseException {

        Address address = new Address(verificationData.get(Constants.REGION), verificationData.get(Constants.CITY),
                verificationData.get(Constants.STREET), verificationData.get(Constants.BUILDING),
                verificationData.get(Constants.FLAT));
        ClientData clientData = new ClientData(verificationData.get(Constants.FIRST_NAME),
                verificationData.get(Constants.LAST_NAME), verificationData.get(Constants.MIDDLE_NAME),
                verificationData.get(Constants.PHONE_NUMBER), address);

        Long calibratorOrganisationId = calibratorEmployee.getOrganization().getId();
        Organization calibrator = organizationService.getOrganizationById(calibratorOrganisationId);
        Counter counter = getCounterFromVerificationData(verificationData);
        Date date = new SimpleDateFormat(Constants.FULL_DATE).parse(verificationData.get(Constants.DATE));
        String verId = verificationService.getNewVerificationDailyId(date);

        Verification verification = new Verification(date, clientData,
                Status.CREATED_BY_CALIBRATOR, Verification.ReadStatus.UNREAD, calibrator, calibratorEmployee,
                counter, verId);
        String verificationId = verification.getId();
        verificationService.saveVerification(verification);
        return verificationId;
    }

    private void updateVerificationFromMap(Map<String, String> verificationData, String verificationId) {
        Verification verification = verificationService.findById(verificationId);
        Counter counter = getCounterFromVerificationData(verificationData);
        verification.setCounter(counter);
    }
    private Counter getCounterFromVerificationData(Map<String, String> verificationData){
        String sizeAndSymbol = verificationData.get(Constants.COUNTER_SIZE_AND_SYMBOL);
        String[] parts = sizeAndSymbol.split(" ");
        String standardSize = parts[0] + " " + parts[1];
        String symbol = parts[2];
        if (parts.length > 3) {
            for (int i = 3; i < parts.length; i++) {
                symbol += " " + parts[i];
            }
        }
        CounterType counterType = counterTypeService.findOneBySymbolAndStandardSize(symbol, standardSize);
        Counter counter = new Counter(verificationData.get(Constants.YEAR),
                verificationData.get(Constants.COUNTER_NUMBER), counterType);
        return counter;
    }
}