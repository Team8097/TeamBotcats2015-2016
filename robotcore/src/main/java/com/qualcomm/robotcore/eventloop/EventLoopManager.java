// 
// Decompiled by Procyon v0.5.30
// 

package com.qualcomm.robotcore.eventloop;

import java.util.Collection;
import java.util.HashSet;
import com.qualcomm.robotcore.robocol.RobocolParsable;
import com.qualcomm.robotcore.robocol.PeerDiscovery;
import java.net.SocketException;
import java.util.Iterator;
import com.qualcomm.robotcore.robocol.RobocolDatagram;
import com.qualcomm.robotcore.robocol.Telemetry;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.exception.RobotCoreException;
import java.util.concurrent.CopyOnWriteArraySet;
import com.qualcomm.robotcore.eventloop.opmode.OpModeManager;
import com.qualcomm.robotcore.util.RobotLog;
import java.net.InetAddress;
import com.qualcomm.robotcore.robocol.Command;
import java.util.Set;
import com.qualcomm.robotcore.robocol.Heartbeat;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.robocol.RobocolDatagramSocket;
import com.qualcomm.robotcore.robot.RobotState;

public class EventLoopManager
{
    public static final String SYSTEM_TELEMETRY = "SYSTEM_TELEMETRY";
    public static final String ROBOT_BATTERY_LEVEL_KEY = "Robot Battery Level";
    public static final String RC_BATTERY_LEVEL_KEY = "RobotController Battery Level";
    private static final EventLoop a;
    public RobotState state;
    private Thread b;
    private Thread c;
    private final RobocolDatagramSocket d;
    private boolean e;
    private ElapsedTime f;
    private EventLoop g;
    private final Gamepad[] h;
    private Heartbeat i;
    private EventLoopMonitor j;
    private final Set<SyncdDevice> k;
    private final Command[] l;
    private int m;
    private final Set<Command> n;
    private InetAddress o;
    
    public void handleDroppedConnection() {
        final OpModeManager opModeManager = this.g.getOpModeManager();
        final String string = "Lost connection while running op mode: " + opModeManager.getActiveOpModeName();
        this.resetGamepads();
        opModeManager.initActiveOpMode("Stop Robot");
        this.a(RobotState.DROPPED_CONNECTION);
        RobotLog.i(string);
    }
    
    public EventLoopManager(final RobocolDatagramSocket socket) {
        this.state = RobotState.NOT_STARTED;
        this.b = new Thread();
        this.c = new Thread();
        this.e = false;
        this.f = new ElapsedTime();
        this.g = EventLoopManager.a;
        this.h = new Gamepad[] { new Gamepad(), new Gamepad() };
        this.i = new Heartbeat(Heartbeat.Token.EMPTY);
        this.j = null;
        this.k = new CopyOnWriteArraySet<SyncdDevice>();
        this.l = new Command[8];
        this.m = 0;
        this.n = new CopyOnWriteArraySet<Command>();
        this.d = socket;
        this.a(RobotState.NOT_STARTED);
    }
    
    public void setMonitor(final EventLoopMonitor monitor) {
        this.j = monitor;
    }
    
    public void start(final EventLoop eventLoop) throws RobotCoreException {
        this.e = false;
        this.setEventLoop(eventLoop);
        (this.c = new Thread(new d(), "Scheduled Sends")).start();
        new Thread(new c()).start();
    }
    
    public void shutdown() {
        this.d.close();
        this.c.interrupt();
        this.e = true;
        this.b();
    }
    
    public void registerSyncdDevice(final SyncdDevice device) {
        this.k.add(device);
    }
    
    public void unregisterSyncdDevice(final SyncdDevice device) {
        this.k.remove(device);
    }
    
    public void setEventLoop(EventLoop eventLoop) throws RobotCoreException {
        if (eventLoop == null) {
            eventLoop = EventLoopManager.a;
            RobotLog.d("Event loop cannot be null, using empty event loop");
        }
        this.b();
        this.g = eventLoop;
        this.a();
    }
    
    public EventLoop getEventLoop() {
        return this.g;
    }
    
    public Gamepad getGamepad() {
        return this.getGamepad(0);
    }
    
    public Gamepad getGamepad(final int port) {
        Range.throwIfRangeIsInvalid(port, 0.0, 1.0);
        return this.h[port];
    }
    
    public Gamepad[] getGamepads() {
        return this.h;
    }
    
    public void resetGamepads() {
        final Gamepad[] h = this.h;
        for (int length = h.length, i = 0; i < length; ++i) {
            h[i].reset();
        }
    }
    
    public Heartbeat getHeartbeat() {
        return this.i;
    }
    
    public void sendTelemetryData(final Telemetry telemetry) {
        try {
            this.d.send(new RobocolDatagram(telemetry.toByteArray()));
        }
        catch (RobotCoreException e) {
            RobotLog.w("Failed to send telemetry data");
            RobotLog.logStacktrace(e);
        }
        telemetry.clearData();
    }
    
    public void sendCommand(final Command command) {
        this.n.add(command);
    }
    
    private void a() throws RobotCoreException {
        try {
            this.a(RobotState.INIT);
            this.g.init(this);
            final Iterator<SyncdDevice> iterator = this.k.iterator();
            while (iterator.hasNext()) {
                iterator.next().startBlockingWork();
            }
        }
        catch (Exception e) {
            RobotLog.w("Caught exception during looper init: " + e.toString());
            RobotLog.logStacktrace(e);
            this.a(RobotState.EMERGENCY_STOP);
            if (RobotLog.hasGlobalErrorMsg()) {
                this.buildAndSendTelemetry("SYSTEM_TELEMETRY", RobotLog.getGlobalErrorMsg());
            }
            throw new RobotCoreException("Robot failed to start: " + e.getMessage());
        }
        this.f = new ElapsedTime(0L);
        this.a(RobotState.RUNNING);
        (this.b = new Thread(new b(), "Event Loop")).start();
    }
    
    private void b() {
        this.b.interrupt();
        try {
            Thread.sleep(200L);
        }
        catch (InterruptedException ex) {}
        this.a(RobotState.STOPPED);
        this.g = EventLoopManager.a;
        this.k.clear();
    }
    
    private void a(final RobotState state) {
        this.state = state;
        RobotLog.v("EventLoopManager state is " + state.toString());
        if (this.j != null) {
            this.j.onStateChange(state);
        }
    }
    
    private void a(final RobocolDatagram robocolDatagram) throws RobotCoreException {
        final Gamepad gamepad = new Gamepad();
        gamepad.fromByteArray(robocolDatagram.getData());
        if (gamepad.user < 1 || gamepad.user > 2) {
            RobotLog.d("Gamepad with user %d received. Only users 1 and 2 are valid");
            return;
        }
        final byte b = (byte)(gamepad.user - 1);
        this.h[b].copy(gamepad);
        if (this.h[0].id == this.h[1].id) {
            RobotLog.v("Gamepad moved position, removing stale gamepad");
            if (b == 0) {
                this.h[1].copy(new Gamepad());
            }
            if (b == 1) {
                this.h[0].copy(new Gamepad());
            }
        }
    }
    
    private void b(final RobocolDatagram message) throws RobotCoreException {
        final Heartbeat i = new Heartbeat(Heartbeat.Token.EMPTY);
        i.fromByteArray(message.getData());
        i.setRobotState(this.state);
        message.setData(i.toByteArray());
        this.d.send(message);
        this.f.reset();
        this.i = i;
    }
    
    private void c(final RobocolDatagram robocolDatagram) throws RobotCoreException {
        if (robocolDatagram.getAddress().equals(this.o)) {
            return;
        }
        if (this.state == RobotState.DROPPED_CONNECTION) {
            this.a(RobotState.RUNNING);
        }
        if (this.g == EventLoopManager.a) {
            return;
        }
        this.o = robocolDatagram.getAddress();
        RobotLog.i("new remote peer discovered: " + this.o.getHostAddress());
        try {
            this.d.connect(this.o);
        }
        catch (SocketException ex) {
            RobotLog.e("Unable to connect to peer:" + ex.toString());
        }
        final PeerDiscovery message = new PeerDiscovery(PeerDiscovery.PeerType.PEER);
        RobotLog.v("Sending peer discovery packet");
        final RobocolDatagram message2 = new RobocolDatagram(message);
        if (this.d.getInetAddress() == null) {
            message2.setAddress(this.o);
        }
        this.d.send(message2);
    }
    
    private void d(final RobocolDatagram robocolDatagram) throws RobotCoreException {
        final Command command = new Command(robocolDatagram.getData());
        if (command.isAcknowledged()) {
            this.n.remove(command);
            return;
        }
        command.acknowledge();
        this.d.send(new RobocolDatagram(command));
        for (final Command command2 : this.l) {
            if (command2 != null && command2.equals(command)) {
                return;
            }
        }
        this.l[this.m++ % this.l.length] = command;
        try {
            this.g.processCommand(command);
        }
        catch (Exception e) {
            RobotLog.e("Event loop threw an exception while processing a command");
            RobotLog.logStacktrace(e);
        }
    }
    
    private void c() {
    }
    
    private void e(final RobocolDatagram robocolDatagram) {
        RobotLog.w("RobotCore event loop received unknown event type: " + robocolDatagram.getMsgType().name());
    }
    
    public void buildAndSendTelemetry(final String tag, final String msg) {
        final Telemetry telemetry = new Telemetry();
        telemetry.setTag(tag);
        telemetry.addData(tag, msg);
        this.sendTelemetryData(telemetry);
    }
    
    static {
        a = new a();
    }
    
    private class d implements Runnable
    {
        private Set<Command> b;
        
        private d() {
            this.b = new HashSet<Command>();
        }
        
        @Override
        public void run() {
            while (!Thread.interrupted()) {
                for (final Command command : EventLoopManager.this.n) {
                    if (command.getAttempts() > 10) {
                        RobotLog.w("Failed to send command, too many attempts: " + command.toString());
                        this.b.add(command);
                    }
                    else if (command.isAcknowledged()) {
                        RobotLog.v("Command " + command.getName() + " has been acknowledged by remote device");
                        this.b.add(command);
                    }
                    else {
                        try {
                            RobotLog.v("Sending command: " + command.getName() + ", attempt " + command.getAttempts());
                            EventLoopManager.this.d.send(new RobocolDatagram(command.toByteArray()));
                        }
                        catch (RobotCoreException e) {
                            RobotLog.w("Failed to send command " + command.getName());
                            RobotLog.logStacktrace(e);
                        }
                    }
                }
                EventLoopManager.this.n.removeAll(this.b);
                this.b.clear();
                try {
                    Thread.sleep(100L);
                    continue;
                }
                catch (InterruptedException ex) {
                    return;
                }
//                break;
            }
        }
    }
    
    private class c implements Runnable
    {
        ElapsedTime a;
        
        private c() {
            this.a = new ElapsedTime();
        }
        
        @Override
        public void run() {
            while (true) {
                final RobocolDatagram recv = EventLoopManager.this.d.recv();
                if (EventLoopManager.this.e || EventLoopManager.this.d.isClosed()) {
                    break;
                }
                if (recv == null) {
                    Thread.yield();
                }
                else {
                    if (RobotLog.hasGlobalErrorMsg()) {
                        EventLoopManager.this.buildAndSendTelemetry("SYSTEM_TELEMETRY", RobotLog.getGlobalErrorMsg());
                    }
                    try {
                        switch (recv.getMsgType()) {
                            case GAMEPAD: {
                                EventLoopManager.this.a(recv);
                                continue;
                            }
                            case HEARTBEAT: {
                                EventLoopManager.this.b(recv);
                                continue;
                            }
                            case PEER_DISCOVERY: {
                                EventLoopManager.this.c(recv);
                                continue;
                            }
                            case COMMAND: {
                                EventLoopManager.this.d(recv);
                                continue;
                            }
                            case EMPTY: {
                                EventLoopManager.this.c();
                                continue;
                            }
                            default: {
                                EventLoopManager.this.e(recv);
                                continue;
                            }
                        }
                    }
                    catch (RobotCoreException ex) {
                        RobotLog.w("RobotCore event loop cannot process event: " + ex.toString());
                    }
                }
            }
        }
    }
    
    private class b implements Runnable
    {
        @Override
        public void run() {
            RobotLog.v("EventLoopRunnable has started");
            try {
                final ElapsedTime elapsedTime = new ElapsedTime();
                while (!Thread.interrupted()) {
                    while (elapsedTime.time() < 0.001) {
                        Thread.sleep(5L);
                    }
                    elapsedTime.reset();
                    if (RobotLog.hasGlobalErrorMsg()) {
                        EventLoopManager.this.buildAndSendTelemetry("SYSTEM_TELEMETRY", RobotLog.getGlobalErrorMsg());
                    }
                    if (EventLoopManager.this.f.startTime() == 0.0) {
                        Thread.sleep(500L);
                    }
                    else if (EventLoopManager.this.f.time() > 2.0) {
                        EventLoopManager.this.handleDroppedConnection();
                        EventLoopManager.this.o = null;
                        EventLoopManager.this.f = new ElapsedTime(0L);
                    }
                    final Iterator<SyncdDevice> iterator = EventLoopManager.this.k.iterator();
                    while (iterator.hasNext()) {
                        iterator.next().blockUntilReady();
                    }
                    try {
                        EventLoopManager.this.g.loop();
                    }
                    catch (Exception e) {
                        RobotLog.e("Event loop threw an exception");
                        RobotLog.logStacktrace(e);
                        RobotLog.setGlobalErrorMsg("User code threw an uncaught exception: " + (e.getClass().getSimpleName() + ((e.getMessage() != null) ? (" - " + e.getMessage()) : "")));
                        EventLoopManager.this.buildAndSendTelemetry("SYSTEM_TELEMETRY", RobotLog.getGlobalErrorMsg());
                        throw new RobotCoreException("EventLoop Exception in loop()");
                    }
                    finally {
                        final Iterator<SyncdDevice> iterator2 = EventLoopManager.this.k.iterator();
                        while (iterator2.hasNext()) {
                            iterator2.next().startBlockingWork();
                        }
                    }
                }
            }
            catch (InterruptedException ex2) {
                RobotLog.v("EventLoopRunnable interrupted");
                EventLoopManager.this.a(RobotState.STOPPED);
            }
            catch (RobotCoreException ex) {
                RobotLog.v("RobotCoreException in EventLoopManager: " + ex.getMessage());
                EventLoopManager.this.a(RobotState.EMERGENCY_STOP);
                EventLoopManager.this.buildAndSendTelemetry("SYSTEM_TELEMETRY", RobotLog.getGlobalErrorMsg());
            }
            try {
                EventLoopManager.this.g.teardown();
            }
            catch (Exception e2) {
                RobotLog.w("Caught exception during looper teardown: " + e2.toString());
                RobotLog.logStacktrace(e2);
                if (RobotLog.hasGlobalErrorMsg()) {
                    EventLoopManager.this.buildAndSendTelemetry("SYSTEM_TELEMETRY", RobotLog.getGlobalErrorMsg());
                }
            }
            RobotLog.v("EventLoopRunnable has exited");
        }
    }
    
    private static class a implements EventLoop
    {
        @Override
        public void init(final EventLoopManager eventProcessor) {
        }
        
        @Override
        public void loop() {
        }
        
        @Override
        public void teardown() {
        }
        
        @Override
        public void processCommand(final Command command) {
            RobotLog.w("Dropping command " + command.getName() + ", no active event loop");
        }
        
        @Override
        public OpModeManager getOpModeManager() {
            return null;
        }
    }
    
    public enum State
    {
        NOT_STARTED, 
        INIT, 
        RUNNING, 
        STOPPED, 
        EMERGENCY_STOP, 
        DROPPED_CONNECTION;
    }
    
    public interface EventLoopMonitor
    {
        void onStateChange(final RobotState p0);
    }
}
