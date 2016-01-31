// 
// Decompiled by Procyon v0.5.30
// 

package com.qualcomm.robotcore.util;

import java.net.Inet6Address;
import java.net.Inet4Address;
import java.util.Iterator;
import java.net.SocketException;
import java.util.Collection;
import java.util.Collections;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.net.UnknownHostException;
import java.net.InetAddress;

public class Network
{
    public static InetAddress getLoopbackAddress() {
        try {
            return InetAddress.getByAddress(new byte[] { 127, 0, 0, 1 });
        }
        catch (UnknownHostException ex) {
            return null;
        }
    }
    
    public static ArrayList<InetAddress> getLocalIpAddresses() {
        final ArrayList<InetAddress> list = new ArrayList<InetAddress>();
        try {
            final Iterator<NetworkInterface> iterator = Collections.list(NetworkInterface.getNetworkInterfaces()).iterator();
            while (iterator.hasNext()) {
                list.addAll(Collections.list(iterator.next().getInetAddresses()));
            }
        }
        catch (SocketException ex) {}
        return list;
    }
    
    public static ArrayList<InetAddress> getLocalIpAddress(final String networkInterface) {
        final ArrayList<InetAddress> list = new ArrayList<InetAddress>();
        try {
            for (final NetworkInterface networkInterface2 : Collections.list(NetworkInterface.getNetworkInterfaces())) {
                if (networkInterface2.getName() == networkInterface) {
                    list.addAll(Collections.list(networkInterface2.getInetAddresses()));
                }
            }
        }
        catch (SocketException ex) {}
        return list;
    }
    
    public static ArrayList<InetAddress> removeIPv6Addresses(final Collection<InetAddress> addresses) {
        final ArrayList<InetAddress> list = new ArrayList<InetAddress>();
        for (final InetAddress inetAddress : addresses) {
            if (inetAddress instanceof Inet4Address) {
                list.add(inetAddress);
            }
        }
        return list;
    }
    
    public static ArrayList<InetAddress> removeIPv4Addresses(final Collection<InetAddress> addresses) {
        final ArrayList<InetAddress> list = new ArrayList<InetAddress>();
        for (final InetAddress inetAddress : addresses) {
            if (inetAddress instanceof Inet6Address) {
                list.add(inetAddress);
            }
        }
        return list;
    }
    
    public static ArrayList<InetAddress> removeLoopbackAddresses(final Collection<InetAddress> addresses) {
        final ArrayList<InetAddress> list = new ArrayList<InetAddress>();
        for (final InetAddress inetAddress : addresses) {
            if (!inetAddress.isLoopbackAddress()) {
                list.add(inetAddress);
            }
        }
        return list;
    }
    
    public static ArrayList<String> getHostAddresses(final Collection<InetAddress> addresses) {
        final ArrayList<String> list = new ArrayList<String>();
        final Iterator<InetAddress> iterator = addresses.iterator();
        while (iterator.hasNext()) {
            String s = iterator.next().getHostAddress();
            if (s.contains("%")) {
                s = s.substring(0, s.indexOf(37));
            }
            list.add(s);
        }
        return list;
    }
}
