// 
// Decompiled by Procyon v0.5.30
// 

package com.qualcomm.robotcore.wifi;

import android.net.NetworkInfo;
import android.content.Intent;
import android.net.wifi.p2p.WifiP2pGroup;
import android.net.wifi.p2p.WifiP2pInfo;
import java.util.Iterator;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pConfig;
import java.util.Collection;
import android.content.BroadcastReceiver;
import com.qualcomm.robotcore.util.RobotLog;
import android.os.Looper;
import java.util.ArrayList;
import java.net.InetAddress;
import android.net.wifi.p2p.WifiP2pManager;
import android.content.IntentFilter;
import android.content.Context;
import android.net.wifi.p2p.WifiP2pDevice;
import java.util.List;

public class WifiDirectAssistant
{
    private static WifiDirectAssistant a;
    private final List<WifiP2pDevice> b;
    private Context c;
    private boolean d;
    private final IntentFilter e;
    private final WifiP2pManager.Channel f;
    private final WifiP2pManager g;
    private d h;
    private final a i;
    private final c j;
    private final b k;
    private int l;
    private ConnectStatus m;
    private Event n;
    private String o;
    private String p;
    private InetAddress q;
    private String r;
    private String s;
    private String t;
    private boolean u;
    private int v;
    private WifiDirectAssistantCallback w;
    
    public static synchronized WifiDirectAssistant getWifiDirectAssistant(final Context context) {
        if (WifiDirectAssistant.a == null) {
            WifiDirectAssistant.a = new WifiDirectAssistant(context);
        }
        return WifiDirectAssistant.a;
    }
    
    private WifiDirectAssistant(final Context context) {
        this.b = new ArrayList<WifiP2pDevice>();
        this.c = null;
        this.d = false;
        this.l = 0;
        this.m = ConnectStatus.NOT_CONNECTED;
        this.n = null;
        this.o = "";
        this.p = "";
        this.q = null;
        this.r = "";
        this.s = "";
        this.t = "";
        this.u = false;
        this.v = 0;
        this.w = null;
        this.c = context;
        (this.e = new IntentFilter()).addAction("android.net.wifi.p2p.STATE_CHANGED");
        this.e.addAction("android.net.wifi.p2p.PEERS_CHANGED");
        this.e.addAction("android.net.wifi.p2p.CONNECTION_STATE_CHANGE");
        this.e.addAction("android.net.wifi.p2p.THIS_DEVICE_CHANGED");
        this.g = (WifiP2pManager)context.getSystemService("wifip2p");
        this.f = this.g.initialize(context, Looper.getMainLooper(), (WifiP2pManager.ChannelListener)null);
        this.h = new d();
        this.i = new a();
        this.j = new c();
        this.k = new b();
    }
    
    public synchronized void enable() {
        ++this.v;
        RobotLog.v("There are " + this.v + " Wifi Direct Assistant Clients (+)");
        if (this.v == 1) {
            RobotLog.v("Enabling Wifi Direct Assistant");
            if (this.h == null) {
                this.h = new d();
            }
            this.c.registerReceiver((BroadcastReceiver)this.h, this.e);
        }
    }
    
    public synchronized void disable() {
        --this.v;
        RobotLog.v("There are " + this.v + " Wifi Direct Assistant Clients (-)");
        if (this.v == 0) {
            RobotLog.v("Disabling Wifi Direct Assistant");
            this.g.stopPeerDiscovery(this.f, (WifiP2pManager.ActionListener)null);
            this.g.cancelConnect(this.f, (WifiP2pManager.ActionListener)null);
            try {
                this.c.unregisterReceiver((BroadcastReceiver)this.h);
            }
            catch (IllegalArgumentException ex) {}
            this.n = null;
        }
    }
    
    public synchronized boolean isEnabled() {
        return this.v > 0;
    }
    
    public ConnectStatus getConnectStatus() {
        return this.m;
    }
    
    public List<WifiP2pDevice> getPeers() {
        return new ArrayList<WifiP2pDevice>(this.b);
    }
    
    public WifiDirectAssistantCallback getCallback() {
        return this.w;
    }
    
    public void setCallback(final WifiDirectAssistantCallback callback) {
        this.w = callback;
    }
    
    public String getDeviceMacAddress() {
        return this.o;
    }
    
    public String getDeviceName() {
        return this.p;
    }
    
    public InetAddress getGroupOwnerAddress() {
        return this.q;
    }
    
    public String getGroupOwnerMacAddress() {
        return this.r;
    }
    
    public String getGroupOwnerName() {
        return this.s;
    }
    
    public String getPassphrase() {
        return this.t;
    }
    
    public boolean isWifiP2pEnabled() {
        return this.d;
    }
    
    public boolean isConnected() {
        return this.m == ConnectStatus.CONNECTED || this.m == ConnectStatus.GROUP_OWNER;
    }
    
    public boolean isGroupOwner() {
        return this.m == ConnectStatus.GROUP_OWNER;
    }
    
    public void discoverPeers() {
        this.g.discoverPeers(this.f, (WifiP2pManager.ActionListener)new WifiP2pManager.ActionListener() {
            public void onSuccess() {
                WifiDirectAssistant.this.a(Event.DISCOVERING_PEERS);
                RobotLog.d("Wifi Direct discovering peers");
            }
            
            public void onFailure(final int reason) {
                final String failureReasonToString = WifiDirectAssistant.failureReasonToString(reason);
                WifiDirectAssistant.this.l = reason;
                RobotLog.w("Wifi Direct failure while trying to discover peers - reason: " + failureReasonToString);
                WifiDirectAssistant.this.a(Event.ERROR);
            }
        });
    }
    
    public void cancelDiscoverPeers() {
        RobotLog.d("Wifi Direct stop discovering peers");
        this.g.stopPeerDiscovery(this.f, (WifiP2pManager.ActionListener)null);
    }
    
    public void createGroup() {
        this.g.createGroup(this.f, (WifiP2pManager.ActionListener)new WifiP2pManager.ActionListener() {
            public void onSuccess() {
                WifiDirectAssistant.this.m = ConnectStatus.GROUP_OWNER;
                WifiDirectAssistant.this.a(Event.GROUP_CREATED);
                RobotLog.d("Wifi Direct created group");
            }
            
            public void onFailure(final int reason) {
                if (reason == 2) {
                    RobotLog.d("Wifi Direct cannot create group, does group already exist?");
                }
                else {
                    final String failureReasonToString = WifiDirectAssistant.failureReasonToString(reason);
                    WifiDirectAssistant.this.l = reason;
                    RobotLog.w("Wifi Direct failure while trying to create group - reason: " + failureReasonToString);
                    WifiDirectAssistant.this.m = ConnectStatus.ERROR;
                    WifiDirectAssistant.this.a(Event.ERROR);
                }
            }
        });
    }
    
    public void removeGroup() {
        this.g.removeGroup(this.f, (WifiP2pManager.ActionListener)null);
    }
    
    public void connect(final WifiP2pDevice peer) {
        if (this.m == ConnectStatus.CONNECTING || this.m == ConnectStatus.CONNECTED) {
            RobotLog.d("WifiDirect connection request to " + peer.deviceAddress + " ignored, already connected");
            return;
        }
        RobotLog.d("WifiDirect connecting to " + peer.deviceAddress);
        this.m = ConnectStatus.CONNECTING;
        final WifiP2pConfig wifiP2pConfig = new WifiP2pConfig();
        wifiP2pConfig.deviceAddress = peer.deviceAddress;
        wifiP2pConfig.wps.setup = 0;
        wifiP2pConfig.groupOwnerIntent = 1;
        this.g.connect(this.f, wifiP2pConfig, (WifiP2pManager.ActionListener)new WifiP2pManager.ActionListener() {
            public void onSuccess() {
                RobotLog.d("WifiDirect connect started");
                WifiDirectAssistant.this.a(Event.CONNECTING);
            }
            
            public void onFailure(final int reason) {
                final String failureReasonToString = WifiDirectAssistant.failureReasonToString(reason);
                WifiDirectAssistant.this.l = reason;
                RobotLog.d("WifiDirect connect cannot start - reason: " + failureReasonToString);
                WifiDirectAssistant.this.a(Event.ERROR);
            }
        });
    }
    
    private void a(final WifiP2pDevice wifiP2pDevice) {
        this.p = wifiP2pDevice.deviceName;
        this.o = wifiP2pDevice.deviceAddress;
        RobotLog.v("Wifi Direct device information: " + this.p + " " + this.o);
    }
    
    public String getFailureReason() {
        return failureReasonToString(this.l);
    }
    
    public static String failureReasonToString(final int reason) {
        switch (reason) {
            case 1: {
                return "P2P_UNSUPPORTED";
            }
            case 0: {
                return "ERROR";
            }
            case 2: {
                return "BUSY";
            }
            default: {
                return "UNKNOWN (reason " + reason + ")";
            }
        }
    }
    
    private void a(final Event n) {
        if (this.n == n && this.n != Event.PEERS_AVAILABLE) {
            return;
        }
        this.n = n;
        if (this.w != null) {
            this.w.onWifiDirectEvent(n);
        }
    }
    
    static {
        WifiDirectAssistant.a = null;
    }
    
    public enum Event
    {
        DISCOVERING_PEERS, 
        PEERS_AVAILABLE, 
        GROUP_CREATED, 
        CONNECTING, 
        CONNECTED_AS_PEER, 
        CONNECTED_AS_GROUP_OWNER, 
        DISCONNECTED, 
        CONNECTION_INFO_AVAILABLE, 
        ERROR;
    }
    
    public enum ConnectStatus
    {
        NOT_CONNECTED, 
        CONNECTING, 
        CONNECTED, 
        GROUP_OWNER, 
        ERROR;
    }
    
    private class c implements WifiP2pManager.PeerListListener
    {
        public void onPeersAvailable(final WifiP2pDeviceList peerList) {
            WifiDirectAssistant.this.b.clear();
            WifiDirectAssistant.this.b.addAll(peerList.getDeviceList());
            RobotLog.v("Wifi Direct peers found: " + WifiDirectAssistant.this.b.size());
            for (final WifiP2pDevice wifiP2pDevice : WifiDirectAssistant.this.b) {
                RobotLog.v("    peer: " + wifiP2pDevice.deviceAddress + " " + wifiP2pDevice.deviceName);
            }
            WifiDirectAssistant.this.a(Event.PEERS_AVAILABLE);
        }
    }
    
    private class a implements WifiP2pManager.ConnectionInfoListener
    {
        public void onConnectionInfoAvailable(final WifiP2pInfo info) {
            WifiDirectAssistant.this.g.requestGroupInfo(WifiDirectAssistant.this.f, (WifiP2pManager.GroupInfoListener)WifiDirectAssistant.this.k);
            WifiDirectAssistant.this.q = info.groupOwnerAddress;
            RobotLog.d("Group owners address: " + WifiDirectAssistant.this.q.toString());
            if (info.groupFormed && info.isGroupOwner) {
                RobotLog.d("Wifi Direct group formed, this device is the group owner (GO)");
                WifiDirectAssistant.this.m = ConnectStatus.GROUP_OWNER;
                WifiDirectAssistant.this.a(Event.CONNECTED_AS_GROUP_OWNER);
            }
            else if (info.groupFormed) {
                RobotLog.d("Wifi Direct group formed, this device is a client");
                WifiDirectAssistant.this.m = ConnectStatus.CONNECTED;
                WifiDirectAssistant.this.a(Event.CONNECTED_AS_PEER);
            }
            else {
                RobotLog.d("Wifi Direct group NOT formed, ERROR: " + info.toString());
                WifiDirectAssistant.this.l = 0;
                WifiDirectAssistant.this.m = ConnectStatus.ERROR;
                WifiDirectAssistant.this.a(Event.ERROR);
            }
        }
    }
    
    private class b implements WifiP2pManager.GroupInfoListener
    {
        public void onGroupInfoAvailable(final WifiP2pGroup group) {
            if (group == null) {
                return;
            }
            if (group.isGroupOwner()) {
                WifiDirectAssistant.this.r = WifiDirectAssistant.this.o;
                WifiDirectAssistant.this.s = WifiDirectAssistant.this.p;
            }
            else {
                final WifiP2pDevice owner = group.getOwner();
                WifiDirectAssistant.this.r = owner.deviceAddress;
                WifiDirectAssistant.this.s = owner.deviceName;
            }
            WifiDirectAssistant.this.t = group.getPassphrase();
            WifiDirectAssistant.this.t = ((WifiDirectAssistant.this.t != null) ? WifiDirectAssistant.this.t : "");
            RobotLog.v("Wifi Direct connection information available");
            WifiDirectAssistant.this.a(Event.CONNECTION_INFO_AVAILABLE);
        }
    }
    
    private class d extends BroadcastReceiver
    {
        public void onReceive(final Context context, final Intent intent) {
            final String action = intent.getAction();
            if ("android.net.wifi.p2p.STATE_CHANGED".equals(action)) {
                WifiDirectAssistant.this.d = (intent.getIntExtra("wifi_p2p_state", -1) == 2);
                RobotLog.d("Wifi Direct state - enabled: " + WifiDirectAssistant.this.d);
            }
            else if ("android.net.wifi.p2p.PEERS_CHANGED".equals(action)) {
                RobotLog.d("Wifi Direct peers changed");
                WifiDirectAssistant.this.g.requestPeers(WifiDirectAssistant.this.f, (WifiP2pManager.PeerListListener)WifiDirectAssistant.this.j);
            }
            else if ("android.net.wifi.p2p.CONNECTION_STATE_CHANGE".equals(action)) {
                final NetworkInfo networkInfo = (NetworkInfo)intent.getParcelableExtra("networkInfo");
                final WifiP2pInfo wifiP2pInfo = (WifiP2pInfo)intent.getParcelableExtra("wifiP2pInfo");
                RobotLog.d("Wifi Direct connection changed - connected: " + networkInfo.isConnected());
                if (networkInfo.isConnected()) {
                    WifiDirectAssistant.this.g.requestConnectionInfo(WifiDirectAssistant.this.f, (WifiP2pManager.ConnectionInfoListener)WifiDirectAssistant.this.i);
                    WifiDirectAssistant.this.g.stopPeerDiscovery(WifiDirectAssistant.this.f, (WifiP2pManager.ActionListener)null);
                }
                else {
                    WifiDirectAssistant.this.m = ConnectStatus.NOT_CONNECTED;
                    if (!WifiDirectAssistant.this.u) {
                        WifiDirectAssistant.this.discoverPeers();
                    }
                    if (WifiDirectAssistant.this.isConnected()) {
                        WifiDirectAssistant.this.a(Event.DISCONNECTED);
                    }
                    WifiDirectAssistant.this.u = wifiP2pInfo.groupFormed;
                }
            }
            else if ("android.net.wifi.p2p.THIS_DEVICE_CHANGED".equals(action)) {
                RobotLog.d("Wifi Direct this device changed");
                WifiDirectAssistant.this.a((WifiP2pDevice)intent.getParcelableExtra("wifiP2pDevice"));
            }
        }
    }
    
    public interface WifiDirectAssistantCallback
    {
        void onWifiDirectEvent(final Event p0);
    }
}
