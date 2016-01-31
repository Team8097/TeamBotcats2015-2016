// 
// Decompiled by Procyon v0.5.30
// 

package com.qualcomm.robotcore.eventloop.opmode;

import com.qualcomm.robotcore.util.RobotLog;
import com.qualcomm.robotcore.util.ElapsedTime;

public abstract class LinearOpMode extends OpMode
{
    private a a;
    private Thread b;
    private ElapsedTime c;
    private volatile boolean d;
    
    public LinearOpMode() {
        this.a = null;
        this.b = null;
        this.c = new ElapsedTime();
        this.d = false;
    }
    
    public abstract void runOpMode() throws InterruptedException;
    
    public synchronized void waitForStart() throws InterruptedException {
        while (!this.d) {
            synchronized (this) {
                this.wait();
            }
        }
    }
    
    public void waitOneFullHardwareCycle() throws InterruptedException {
        this.waitForNextHardwareCycle();
        Thread.sleep(1L);
        this.waitForNextHardwareCycle();
    }
    
    public void waitForNextHardwareCycle() throws InterruptedException {
        synchronized (this) {
            this.wait();
        }
    }
    
    public void sleep(final long milliseconds) throws InterruptedException {
        Thread.sleep(milliseconds);
    }
    
    public boolean opModeIsActive() {
        return this.d;
    }
    
    @Override
    public final void init() {
        this.a = new a(this);
        (this.b = new Thread(this.a, "Linear OpMode Helper")).start();
    }
    
    @Override
    public final void init_loop() {
        this.a();
    }
    
    @Override
    public final void start() {
        this.d = true;
        synchronized (this) {
            this.notifyAll();
        }
    }
    
    @Override
    public final void loop() {
        this.a();
    }
    
    @Override
    public final void stop() {
        this.d = false;
        if (!this.a.c()) {
            this.b.interrupt();
        }
        this.c.reset();
        while (!this.a.c() && this.c.time() < 0.5) {
            Thread.yield();
        }
        if (!this.a.c()) {
            RobotLog.e("*****************************************************************");
            RobotLog.e("User Linear Op Mode took too long to exit; emergency killing app.");
            RobotLog.e("Possible infinite loop in user code?");
            RobotLog.e("*****************************************************************");
            System.exit(-1);
        }
    }
    
    private void a() {
        if (this.a.a()) {
            throw this.a.b();
        }
        synchronized (this) {
            this.notifyAll();
        }
    }
    
    private static class a implements Runnable
    {
        private RuntimeException a;
        private boolean b;
        private final LinearOpMode c;
        
        public a(final LinearOpMode c) {
            this.a = null;
            this.b = false;
            this.c = c;
        }
        
        @Override
        public void run() {
            this.a = null;
            this.b = false;
            try {
                this.c.runOpMode();
            }
            catch (InterruptedException ex) {
                RobotLog.d("LinearOpMode received an Interrupted Exception; shutting down this linear op mode");
            }
            catch (RuntimeException a) {
                this.a = a;
            }
            finally {
                this.b = true;
            }
        }
        
        public boolean a() {
            return this.a != null;
        }
        
        public RuntimeException b() {
            return this.a;
        }
        
        public boolean c() {
            return this.b;
        }
    }
}
