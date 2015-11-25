package com.softserve.edu.device.test.data;

import java.util.Map;

/**
 * Created by Taras on 15.09.2015.
 */
public class BbiDeviceTestData implements DeviceTestData {
    private Map<String, Object> testData;

    public BbiDeviceTestData(Map<String, Object> bbiTestData)
    {
        this.testData = bbiTestData;
    }

    @Override
    public String getStringValue(String key) {
        return testData.get(key).toString();
    }

    @Override
    public long getLongValue(String key) {
        return (long) testData.get(key);
    }

    @Override
    public int getIntegerValue(String key) {
        return (int) getLongValue(key);
    }

    @Override
    public double getDoubleValue(String key) {
        return (double) testData.get(key);
    }

    @Override
    public int getDay() {
        return getIntegerValue("day");
    }

    @Override
    public int getMonth() {
        return getIntegerValue("month");
    }

    @Override
    public int getYear() {
        return getIntegerValue("year");
    }

    @Override
    public int getMinute() {
        return getIntegerValue("minute");
    }

    @Override
    public int getSecond() {
        return getIntegerValue("second");
    }

    @Override
    public int getDayOfWeek() {
        return getIntegerValue("dayOfWeek");
    }

    @Override
    public long getUnixTime() {
        return getLongValue("unixTime");
    }

    @Override
    public int getTemperature() {
        return getIntegerValue("temperature");
    }

    @Override
    public long getBatteryCharge() {
        return getLongValue("batteryCharge");
    }

    @Override
    public String getFileName() {
        return getStringValue("fileName").concat(".bbi");
    }

    @Override
    public int getIntegrationTime() {
        return getIntegerValue("integrationTime");
    }

    @Override
    public int getTestCounter() {
        return getIntegerValue("testCounter");
    }

    @Override
    public int getInstallmentNumber() {
        return (int) getLongValue("installmentNumber");
    }

    @Override
    public String getCurrentCounterNumber() {
        return getStringValue("currentCounterNumber");
    }

    @Override
    public double getLatitude() {
        return getDoubleValue("latitude");
    }

    @Override
    public double getLongitude() {
        return getDoubleValue("longitude");
    }

    @Override
    public long getImpulsePricePerLitre() {
        return getLongValue("impulsePricePerLitre");
    }

    @Override
    public String getInitialCapacity() {
        return getStringValue("initialCapacity");
    }

    @Override
    public String getCounterType1() {
        return getStringValue("counterType1");
    }

    @Override
    public long getTestimony() {
        return getLongValue("testimony");
    }

    @Override
    public int getCounterProductionYear() {
        return getIntegerValue("counterProductionYear");
    }

    @Override
    public String getCounterType2() {
        return getStringValue("counterType2");
    }

    @Override
    public long getTestSpecifiedConsumption(int testIndex) {
        return getLongValue("test" + testIndex + "specifiedConsumption");
    }

    @Override
    public long getTestLowerConsumptionLimit(int testIndex) {
        return getLongValue("test" + testIndex + "lowerConsumptionLimit");
    }

    @Override
    public long getTestUpperConsumptionLimit(int testIndex) {
        return getLongValue("test" + testIndex + "upperConsumptionLimit");
    }

    @Override
    public long getTestAllowableError(int testIndex) {
        return getLongValue("test" + testIndex + "allowableError");
    }

    @Override
    public double getTestSpecifiedImpulsesAmount(int testIndex) {
        return  getDoubleValue("test" + testIndex + "specifiedImpulsesAmount");
    }

    @Override
    public double getTestCorrectedCumulativeImpulsesValue(int testIndex) {
        return getDoubleValue("test" + testIndex + "correctedCumulativeImpulsesValue");
    }

    @Override
    public double getTestCorrectedCurrentConsumption(int testIndex) {
        return getDoubleValue("test" + testIndex + "correctedCurrentConsumption");
    }

    @Override
    public double getTestCumulativeImpulsesValueWithoutCorrection(int testIndex) {
        return getDoubleValue("test" + testIndex + "cumulativeImpulsesValueWithoutCorrection");
    }

    @Override
    public double getTestCurrentConsumptionWithoutCorrection(int testIndex) {
        return getDoubleValue("test" + testIndex + "currentConsumptionWithoutCorrection");
    }

    @Override
    public double getTestEstimatedError(int testIndex) {
        return getLongValue("test" + testIndex + "estimatedError")/100.0;
    }

    @Override
    public double getTestInitialCounterValue(int testIndex) {
        return getDoubleValue("test" + testIndex + "initialCounterValue");
    }

    @Override
    public double getTestTerminalCounterValue(int testIndex) {
        return getDoubleValue("test" + testIndex + "terminalCounterValue");
    }

    @Override
    public long getTestUnixBeginTime(int testIndex) {
        return getLongValue("test" + testIndex + "unixTestBeginTime");
    }

    @Override
    public long getTestUnixEndTime(int testIndex) {
        return getLongValue("test" + testIndex + "unixTestEndTime");
    }

    @Override
    public double getTestDuration(int testIndex) {
        return getDoubleValue("test" + testIndex + "testDuration");
    }

    @Override
    public long getTestCorrectionFactor(int testIndex) {
        return getLongValue("test" + testIndex + "correctionFactor");
    }

    @Override
    public long getTestMinConsumptionLimit(int testIndex) {
        return getLongValue("test" + testIndex + "minConsumptionLimit");
    }

    @Override
    public long getTestMaxConsumptionLimit(int testIndex) {
        return getLongValue("test" + testIndex + "maxConsumptionLimit");
    }

    @Override
    public int getTestNumber(int testIndex) {
        return getIntegerValue("test" + testIndex + "testNumber");
    }

    /**
     *
     * @return Test photo in hex.
     */
    @Override
    public String getTestPhoto() {
        return getStringValue("testPhoto");
    }

    /**
     *
     * @param testIndex Index of test.
     * @return Test begin photo in base64.
     */
    @Override
    public String getBeginPhoto(int testIndex) {
        return getStringValue("test" + testIndex + "beginPhoto");
    }

    /**
     *
     * @param testIndex Index of test.
     * @return Test end photo in base64.
     */
    @Override
    public String getEndPhoto(int testIndex) {
        return getStringValue("test" + testIndex + "endPhoto");
    }


}
