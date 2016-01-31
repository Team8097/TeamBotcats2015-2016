// 
// Decompiled by Procyon v0.5.30
// 

package com.qualcomm.robotcore.util;

public class ElapsedTime {
    private long a;
    private double b;

    public ElapsedTime() {
        this.a = 0L;
        this.b = 1.0E9;
        this.reset();
    }

    public ElapsedTime(final long startTime) {
        this.a = 0L;
        this.b = 1.0E9;
        this.a = startTime;
    }

    public ElapsedTime(final Resolution resolution) {
        this.a = 0L;
        this.b = 1.0E9;
        this.reset();
        switch (resolution) {
            case SECONDS: {
                this.b = 1.0E9;
                break;
            }
            case MILLISECONDS: {
                this.b = 1000000.0;
                break;
            }
        }
    }

    public void reset() {
        this.a = System.nanoTime();
    }

    public double startTime() {
        return this.a / this.b;
    }

    public double time() {
        return (System.nanoTime() - this.a) / this.b;
    }

    private String a() {
        if (this.b == 1.0E9) {
            return "seconds";
        }
        if (this.b == 1000000.0) {
            return "milliseconds";
        }
        return "Unknown units";
    }

    public void log(final String label) {
        RobotLog.v(String.format("TIMER: %20s - %1.3f %s", label, this.time(), this.a()));
    }

    @Override
    public String toString() {
        return String.format("%1.4f %s", this.time(), this.a());
    }

    public enum Resolution {
        SECONDS,
        MILLISECONDS;
    }
}
