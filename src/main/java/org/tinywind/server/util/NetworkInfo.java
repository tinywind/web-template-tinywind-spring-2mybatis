package org.tinywind.server.util;

import java.io.IOException;
import java.net.NetworkInterface;
import java.util.Collections;

/**
 * Reads hardware addresses through the JDK network interface API.
 *
 * @author haneulnoon
 * @since 2009-06-16
 */
public final class NetworkInfo {
    /**
     * 현재 컴퓨터의 맥 주소를 리턴한다.(맥 주소 중에 '-'를 제거한다.)
     */
    public static String getShortMacAddress() {
        String value = "";

        try {
            value = getMacAddress();
        } catch (IOException e) {
            e.printStackTrace();
        }
        value = value == null ? "" : value.replace("-", "");

        return value;
    }

    public static String getMacAddress() throws IOException {
        for (NetworkInterface network : Collections.list(NetworkInterface.getNetworkInterfaces())) {
            byte[] hardware = network.getHardwareAddress();
            if (!network.isUp() || network.isLoopback() || hardware == null || hardware.length != 6) {
                continue;
            }
            StringBuilder address = new StringBuilder();
            for (byte value : hardware) {
                if (address.length() > 0) address.append('-');
                address.append(String.format("%02X", value & 255));
            }
            return address.toString();
        }
        return null;
    }

    /**
     * 문자열에서  패턴에 맞는 문자열 즉 맥주소를 뽑아낸다.
     *
     * @param text 검사할 문자열
     * @return 맥 주소
     */
    public static String ParseMacAddress(String text) {
        String result = null;
        String[] list = text.split("\\p{XDigit}{2}(-\\p{XDigit}{2}){5}");
        int index = 0;
        for (String str : list) {
            if (str.length() < text.length()) {
                index = str.length();
                result = text.substring(index, index + 17);
                if (!result.equals("00-00-00-00-00-00")) {
                    break;
                }
                text = text.substring(index + 17);

            }
        }
        return result;
    }
}


