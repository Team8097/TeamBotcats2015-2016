// 
// Decompiled by Procyon v0.5.30
// 

package com.qualcomm.robotcore.robocol;

import java.io.IOException;
import com.qualcomm.robotcore.util.TypeConversion;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.ArrayList;
import java.net.SocketException;
import com.qualcomm.robotcore.util.RobotLog;
import java.net.NetworkInterface;
import java.util.Collection;
import com.qualcomm.robotcore.util.Network;
import java.net.InetAddress;

public class RobocolConfig
{
    public static final int MAX_PACKET_SIZE = 4098;
    public static final int PORT_NUMBER = 20884;
    public static final int TTL = 3;
    public static final int TIMEOUT = 1000;
    public static final int WIFI_P2P_SUBNET_MASK = -256;
    
    public static InetAddress determineBindAddress(final InetAddress destAddress) {
        final ArrayList<InetAddress> removeIPv6Addresses = Network.removeIPv6Addresses(Network.removeLoopbackAddresses(Network.getLocalIpAddresses()));
        for (final InetAddress inetAddress : removeIPv6Addresses) {
            try {
                final Enumeration<InetAddress> inetAddresses = NetworkInterface.getByInetAddress(inetAddress).getInetAddresses();
                while (inetAddresses.hasMoreElements()) {
                    final InetAddress inetAddress2 = inetAddresses.nextElement();
                    if (inetAddress2.equals(destAddress)) {
                        return inetAddress2;
                    }
                }
            }
            catch (SocketException ex) {
                RobotLog.v(String.format("socket exception while trying to get network interface of %s", inetAddress.getHostAddress()));
            }
        }
        return determineBindAddressBasedOnWifiP2pSubnet(removeIPv6Addresses, destAddress);
    }
    
    public static InetAddress determineBindAddressBasedOnWifiP2pSubnet(final ArrayList<InetAddress> localIpAddresses, final InetAddress destAddress) {
        final int byteArrayToInt = TypeConversion.byteArrayToInt(destAddress.getAddress());
        for (final InetAddress inetAddress : localIpAddresses) {
            if ((TypeConversion.byteArrayToInt(inetAddress.getAddress()) & 0xFFFFFF00) == (byteArrayToInt & 0xFFFFFF00)) {
                return inetAddress;
            }
        }
        return Network.getLoopbackAddress();
    }
    
    public static InetAddress determineBindAddressBasedOnIsReachable(final ArrayList<InetAddress> localIpAddresses, final InetAddress destAddress) {
        for (final InetAddress inetAddress : localIpAddresses) {
            try {
                if (inetAddress.isReachable(NetworkInterface.getByInetAddress(inetAddress), 3, 1000)) {
                    return inetAddress;
                }
                continue;
            }
            catch (SocketException ex) {
                RobotLog.v(String.format("socket exception while trying to get network interface of %s", inetAddress.getHostAddress()));
            }
            catch (IOException ex2) {
                RobotLog.v(String.format("IO exception while trying to determine if %s is reachable via %s", destAddress.getHostAddress(), inetAddress.getHostAddress()));
            }
        }
        return Network.getLoopbackAddress();
    }
}
