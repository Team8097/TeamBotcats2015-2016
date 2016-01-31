// 
// Decompiled by Procyon v0.5.30
// 

package com.qualcomm.robotcore.robocol;

import com.qualcomm.robotcore.exception.RobotCoreException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.Executors;
import com.qualcomm.robotcore.util.RobotLog;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledExecutorService;
import java.net.InetAddress;

public class PeerDiscoveryManager
{
    private InetAddress a;
    private final RobocolDatagramSocket b;
    private ScheduledExecutorService c;
    private ScheduledFuture<?> d;
    private final PeerDiscovery e;
    
    public PeerDiscoveryManager(final RobocolDatagramSocket socket) {
        this.e = new PeerDiscovery(PeerDiscovery.PeerType.PEER);
        this.b = socket;
    }
    
    public InetAddress getPeerDiscoveryDevice() {
        return this.a;
    }
    
    public void start(final InetAddress peerDiscoveryDevice) {
        RobotLog.v("Starting peer discovery");
        if (peerDiscoveryDevice == this.b.getLocalAddress()) {
            RobotLog.v("No need for peer discovery, we are the peer discovery device");
            return;
        }
        if (this.d != null) {
            this.d.cancel(true);
        }
        this.a = peerDiscoveryDevice;
        this.c = Executors.newSingleThreadScheduledExecutor();
        this.d = this.c.scheduleAtFixedRate(new a(), 1L, 1L, TimeUnit.SECONDS);
    }
    
    public void stop() {
        RobotLog.v("Stopping peer discovery");
        if (this.d != null) {
            this.d.cancel(true);
        }
    }
    
    private class a implements Runnable
    {
        @Override
        public void run() {
            try {
                RobotLog.v("Sending peer discovery packet");
                final RobocolDatagram message = new RobocolDatagram(PeerDiscoveryManager.this.e);
                if (PeerDiscoveryManager.this.b.getInetAddress() == null) {
                    message.setAddress(PeerDiscoveryManager.this.a);
                }
                PeerDiscoveryManager.this.b.send(message);
            }
            catch (RobotCoreException ex) {
                RobotLog.d("Unable to send peer discovery packet: " + ex.toString());
            }
        }
    }
}
