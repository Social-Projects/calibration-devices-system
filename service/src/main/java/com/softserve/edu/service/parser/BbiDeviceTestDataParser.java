package com.softserve.edu.service.parser;

import javax.xml.bind.DatatypeConverter;

import com.softserve.edu.device.test.data.BbiDeviceTestData;
import com.softserve.edu.device.test.data.DeviceTestData;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.binary.Base64;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class BbiDeviceTestDataParser implements DeviceTestDataParser {
    private InputStream reader;
    private Map<String, Object> resultMap;

    @Override
    public DeviceTestData parse(InputStream deviceTestDataStream) {
        final int EMPTY_BYTES_BETWEEN_TESTS = 180;
        resultMap = new HashMap<>();
        reader = new BufferedInputStream(deviceTestDataStream);
        try {
            resultMap.put("day", readLongValueReversed(1)); //0x800000
            resultMap.put("month", readLongValueReversed(1)); //0x800001
            resultMap.put("year", readLongValueReversed(2)); //0x800002 + 0x02
            resultMap.put("hour", readLongValueReversed(1)); //0x800004
            resultMap.put("minute", readLongValueReversed(1)); //0x800005
            resultMap.put("second" ,readLongValueReversed(1)); //0x800006
            resultMap.put("dayOfWeek", readLongValueReversed(1)); //0x800007
            resultMap.put("unixTime", readLongValueReversed(4) * 1000); //0x800008 + 0x04
            resultMap.put("regStart", readConsecutiveBytesReversed(4)); //0x80000c + 0x04
            reader.skip(4); //0x800010 + 0x04
            resultMap.put("temperature", readLongValueReversed(4)); //0x800014 + 0x04
            resultMap.put("batteryCharge", readLongValueReversed(4)); //0x800018 + 0x04
            resultMap.put("bbiWritten", readLongValueReversed(4)); //0x80001c + 0x04
            resultMap.put("bbiAvailableToWrite", readLongValueReversed(4)); //0x800020 + 0x04
            resultMap.put("fileName", readLongValueReversed(4)); //0x800024 + 0x04
            reader.skip(12); //0x800028 + 0x0c
            resultMap.put("integrationTime", readLongValueReversed(4)); //0x800034 + 0x04
            resultMap.put("testCounter", readLongValueReversed(4)); //0x800038 + 0x04
            resultMap.put("confirmationRegister", readLongValueReversed(4)); //0x80003c + 0x04
            resultMap.put("regControl", readConsecutiveBytesReversed(4)); //0x800040 + 0x04
            resultMap.put("installmentNumber", readLongValueReversed(4)); //0x800044 + 0x04
            resultMap.put("currentCounterNumber", readConsecutiveBytesAsUTF8(12));
            resultMap.put("latitude", readLongValueReversed(4) / 100000.0);  //0x800054+0x04
            resultMap.put("longitude", readLongValueReversed(4) / 100000.0); //0x800058+0x04
            resultMap.put("impulsePricePerLitre", readLongValueReversed(4)); //0x80005c+0x04
            resultMap.put("initialCapacity", readLongValueReversed(8)); //0x800060+0x08
            resultMap.put("counterType1", readConsecutiveBytesAsUTF8(16)); //0x800068+0x10
            resultMap.put("testimony", readLongValueReversed(4)); //0x800078+0x04
            resultMap.put("counterProductionYear", readLongValueReversed(4)); //0x80007c+0x04
            resultMap.put("counterType2", readConsecutiveBytesAsUTF8(16)); //0x800080+0x10
            resultMap.put("fileOpened", readLongValueReversed(4)); //0x800090+0x04
            reader.skip(108); //0x800100 now
            for (int i = 1; i <= 6; ++i) {
                readTest(i);
                if (i != 6) {
                    reader.skip(EMPTY_BYTES_BETWEEN_TESTS);
                }
            }
            resultMap.put("fullInstallmentNumber", readConsecutiveBytesAsUTF8(32)); //0x0x80064c+32
            reader.skip(2452); //go to images

            resultMap.put("testPhoto", readImageBase64());
            for (int i = 0; i < 12; ++i) {
                String imageKey = "test" + (i / 2 + 1) + (i % 2 == 0 ? "begin" : "end") + "Photo";
                resultMap.put(imageKey, readImageBase64());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }  catch (DecoderException e) {
            e.printStackTrace();
        }
        return new BbiDeviceTestData(resultMap);
    }

    /**
     * Adds to string representation of byte leading "0" character if the byte's value has only one digit
     * @param stringByte
     * @return
     */
    private String normalizeByte(String stringByte) {
        return stringByte.length() == 2 ? stringByte : "0" + stringByte;
    }

    /**
     * Reads specified amount of bytes from InputStream reader and
     * concatenates them in reversed order.
     * @param amount
     *          amount of bytes to read.
     * @return
     *          string which contains concatenated bytes in reverse order.
     * @throws IOException
     */
    private String readConsecutiveBytesReversed(long amount) throws IOException {
        String result = "";
        for (long i = 0; i < amount; ++i) {
            result = normalizeByte(Integer.toHexString(reader.read())) + result;
        }
        return result;
    }

    /**
     * Reads specified amount of bytes from InputStream reader and
     * concatenates them.
     * @param amount
     *          amount of bytes to read.
     * @return
     *          string which contains concatenated bytes.
     * @throws IOException
     */
    private String readConsecutiveBytes(long amount) throws IOException {
        String result = "";
        for (long i = 0; i < amount; ++i) {
            result = result + normalizeByte(Integer.toHexString(reader.read()));
        }
        return result;
    }

    /**
     * Reads specified amount of bytes from InputStream reader, concatenates them
     * and converts hex string into UTF8 string.
     * @param amount
     *          amount of bytes to read.
     * @return
     *          string which contains UTF8 symbols.
     * @throws IOException
     */
    private String readConsecutiveBytesAsUTF8(long amount) throws IOException {
        return new String(DatatypeConverter.parseHexBinary(readConsecutiveBytes(amount))).trim();
    }

    /**
     * Reads specified amount of bytes from InputStream reader, concatenates them
     * and converts hex string into long value.
     * @param amount
     *          amount of bytes to read.
     * @return
     *          long value.
     * @throws IOException
     */
    private Long readLongValue(long bytesAmount) throws  IOException {
        long result = 0;
        for (long i = 0; i < bytesAmount; ++i) {
            result <<= 8;
            result += reader.read();
        }
        return result;
//        return Long.parseLong(readConsecutiveBytes(bytesAmount), 16);
    }

    /**
     * Reads specified amount of bytes from InputStream reader, concatenates them
     * in reverse order and converts hex string into long value.
     * @param amount
     *          amount of bytes to read.
     * @return
     *          long value.
     * @throws IOException
     */
    private Long readLongValueReversed(long bytesAmount) throws IOException {
        long result = 0;
        for (int i = 0; i < bytesAmount; ++i) {
            result += reader.read() << 8 * i;
        }
        return result;
//        return Long.parseLong(readConsecutiveBytesReversed(bytesAmount), 16);
    }

    private void readTest(int testIndex) throws IOException {
        resultMap.put("test" + testIndex + "specifiedConsumption", readLongValueReversed(4)); //0x800100+0x04
        resultMap.put("test" + testIndex + "lowerConsumptionLimit", readLongValueReversed(4)); //0x800104+0x04
        resultMap.put("test" + testIndex + "upperConsumptionBound", readLongValueReversed(4)); //0x800108+0x04
        resultMap.put("test" + testIndex + "allowableError", readLongValueReversed(4) / 10); //0x80010с+0x04
        resultMap.put("test" + testIndex + "specifiedImpulsesAmount", readLongValueReversed(4) / 10000); //0x800110+0x04
        resultMap.put("test" + testIndex + "correctedCumulativeImpulsesValue", readLongValueReversed(4) / 1000.0); //0x800114+0x04
        resultMap.put("test" + testIndex + "correctedCurrentConsumption", readLongValueReversed(4) / 1000.0); //0x800118+0x04
        resultMap.put("test" + testIndex + "cumulativeImpulsesValueWithoutCorrection", readLongValueReversed(4) / 1000.0); //0x80011с+0x04
        resultMap.put("test" + testIndex + "currentConsumptionWithoutCorrection", readLongValueReversed(4) / 1000.0); //0x800120+0x04
        resultMap.put("test" + testIndex + "estimatedError", readLongValueReversed(4)); //0x800124+0x04
        resultMap.put("test" + testIndex + "initialCounterValue", readLongValueReversed(4) / 10000.0); //0x800128+0x04
        resultMap.put("test" + testIndex + "terminalCounterValue", readLongValueReversed(4) / 10000.0); //0x80012с+0x04
        resultMap.put("test" + testIndex + "unixTestBeginTime", readLongValueReversed(4)); //0x800130+0x04
        resultMap.put("test" + testIndex + "unixTestEndTime", readLongValueReversed(4)); //0x800134+0x04
        resultMap.put("test" + testIndex + "testDuration", readLongValueReversed(4) / 1000.0); //0x800138+0x04
        resultMap.put("test" + testIndex + "correctionFactor", readLongValueReversed(4)); //0x80013с+0x04
        resultMap.put("test" + testIndex + "minConsumptionLimit", readLongValueReversed(4)); //0x800140+0x04
        resultMap.put("test" + testIndex + "maxConsumptionLimit", readLongValueReversed(4)); //0x800144+0x04
        resultMap.put("test" + testIndex + "testNumber", readLongValueReversed(4)); //0x800148+0x04
    }

    /**
     * Reads image size. Then reads next imageSize bytes and
     * stores them in string variable.
     * Finally, skips all blank bytes reserved for image.
     * @return Image written in base64 string.
     * @throws IOException
     */
    private String readImageBase64() throws IOException, DecoderException {
        final int ALLOCATED_IMAGE_SIZE = 16380;

        Long imageSize = readLongValue(4);
        String imageHex = readConsecutiveBytes(imageSize);

        byte[] decodedHex = Hex.decodeHex(imageHex.toCharArray());
        String encodedHexB64 = Base64.encodeBase64String(decodedHex);

        // skips all empty bytes till the next image beginning.
        reader.skip(ALLOCATED_IMAGE_SIZE - imageSize);
        return encodedHexB64;
    }
}
