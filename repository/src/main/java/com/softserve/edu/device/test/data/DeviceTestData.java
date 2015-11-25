package com.softserve.edu.device.test.data;

/**
 * Created by Taras on 15.09.2015.
 */
public interface DeviceTestData {
    String getStringValue(String key);
    long getLongValue(String key);
    int getIntegerValue(String key);
    double getDoubleValue(String key);

    int getDay();
    int getMonth();
    int getYear();
    int getMinute();
    int getSecond();
    int getDayOfWeek();
    long getUnixTime();
    int getTemperature();
    long getBatteryCharge();
    String getFileName();
    int getIntegrationTime();
    int getTestCounter();
    int getInstallmentNumber();
    String getCurrentCounterNumber();
    double getLatitude();
    double getLongitude();
    long getImpulsePricePerLitre();
    String getInitialCapacity();
    String getCounterType1();
    long getTestimony();
    int getCounterProductionYear();
    String getCounterType2();

    long getTestSpecifiedConsumption(int testIndex);
    long getTestLowerConsumptionLimit(int testIndex);
    long getTestUpperConsumptionLimit(int testIndex);
    long getTestAllowableError(int testIndex);
    double getTestSpecifiedImpulsesAmount(int testIndex);
    double getTestCorrectedCumulativeImpulsesValue(int testIndex);
    double getTestCorrectedCurrentConsumption(int testIndex);
    double getTestCumulativeImpulsesValueWithoutCorrection(int testIndex);
    double getTestCurrentConsumptionWithoutCorrection(int testIndex);
    double getTestEstimatedError(int testIndex);
    double getTestInitialCounterValue(int testIndex);
    double getTestTerminalCounterValue(int testIndex);
    long getTestUnixBeginTime(int testIndex);
    long getTestUnixEndTime(int testIndex);

    /**
     * Returns test duration in seconds
     * @param testIndex
     * @return
     */
    double getTestDuration(int testIndex);
    long getTestCorrectionFactor(int testIndex);
    long getTestMinConsumptionLimit(int testIndex);
    long getTestMaxConsumptionLimit(int testIndex);
    int getTestNumber(int testIndex);

    String getTestPhoto();
    String getBeginPhoto(int testIndex);
    String getEndPhoto(int testIndex);
}
