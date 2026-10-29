package org.tinywind.server.util;

import org.junit.Test;

import java.net.NetworkInterface;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class NetworkInfoTest {
    @Test
    public void readsAnActualHardwareAddressWithoutAnExternalCommand() throws Exception {
        Set<String> addresses = new HashSet<>();
        for (NetworkInterface network : Collections.list(NetworkInterface.getNetworkInterfaces())) {
            byte[] hardware = network.getHardwareAddress();
            if (network.isUp() && !network.isLoopback() && hardware != null && hardware.length == 6) {
                StringBuilder address = new StringBuilder();
                for (byte value : hardware) {
                    if (address.length() > 0) address.append('-');
                    address.append(String.format("%02X", value & 255));
                }
                addresses.add(address.toString());
            }
        }
        String address = NetworkInfo.getMacAddress();
        if (addresses.isEmpty()) {
            assertEquals(null, address);
            assertEquals("", NetworkInfo.getShortMacAddress());
        } else {
            assertTrue(addresses.contains(address));
            assertEquals(address.replace("-", ""), NetworkInfo.getShortMacAddress());
        }
    }
}
