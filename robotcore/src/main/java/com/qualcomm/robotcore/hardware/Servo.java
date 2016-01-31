// 
// Decompiled by Procyon v0.5.30
// 

package com.qualcomm.robotcore.hardware;

import com.qualcomm.robotcore.util.Range;

public class Servo implements HardwareDevice
{
    public static final double MIN_POSITION = 0.0;
    public static final double MAX_POSITION = 1.0;
    protected ServoController controller;
    protected int portNumber;
    protected Direction direction;
    protected double minPosition;
    protected double maxPosition;
    
    public Servo(final ServoController controller, final int portNumber) {
        this(controller, portNumber, Direction.FORWARD);
    }
    
    public Servo(final ServoController controller, final int portNumber, final Direction direction) {
        this.controller = null;
        this.portNumber = -1;
        this.direction = Direction.FORWARD;
        this.minPosition = 0.0;
        this.maxPosition = 1.0;
        this.direction = direction;
        this.controller = controller;
        this.portNumber = portNumber;
    }
    
    @Override
    public String getDeviceName() {
        return "Servo";
    }
    
    @Override
    public String getConnectionInfo() {
        return this.controller.getConnectionInfo() + "; port " + this.portNumber;
    }
    
    @Override
    public int getVersion() {
        return 1;
    }
    
    @Override
    public void close() {
    }
    
    public ServoController getController() {
        return this.controller;
    }
    
    public void setDirection(final Direction direction) {
        this.direction = direction;
    }
    
    public Direction getDirection() {
        return this.direction;
    }
    
    public int getPortNumber() {
        return this.portNumber;
    }
    
    public void setPosition(double position) {
        if (this.direction == Direction.REVERSE) {
            position = this.a(position);
        }
        this.controller.setServoPosition(this.portNumber, Range.scale(position, 0.0, 1.0, this.minPosition, this.maxPosition));
    }
    
    public double getPosition() {
        double n = this.controller.getServoPosition(this.portNumber);
        if (this.direction == Direction.REVERSE) {
            n = this.a(n);
        }
        return Range.clip(Range.scale(n, this.minPosition, this.maxPosition, 0.0, 1.0), 0.0, 1.0);
    }
    
    public void scaleRange(final double min, final double max) throws IllegalArgumentException {
        Range.throwIfRangeIsInvalid(min, 0.0, 1.0);
        Range.throwIfRangeIsInvalid(max, 0.0, 1.0);
        if (min >= max) {
            throw new IllegalArgumentException("min must be less than max");
        }
        this.minPosition = min;
        this.maxPosition = max;
    }
    
    private double a(final double n) {
        return 1.0 - n + 0.0;
    }
    
    public enum Direction
    {
        FORWARD, 
        REVERSE;
    }
}
