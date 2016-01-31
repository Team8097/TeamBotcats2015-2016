// 
// Decompiled by Procyon v0.5.30
// 

package com.qualcomm.robotcore.hardware;

public abstract class IrSeekerSensor implements HardwareDevice
{
    public static final int MAX_NEW_I2C_ADDRESS = 126;
    public static final int MIN_NEW_I2C_ADDRESS = 16;
    
    public abstract void setSignalDetectedThreshold(final double p0);
    
    public abstract double getSignalDetectedThreshold();
    
    public abstract void setMode(final Mode p0);
    
    public abstract Mode getMode();
    
    public abstract boolean signalDetected();
    
    public abstract double getAngle();
    
    public abstract double getStrength();
    
    public abstract IrSeekerIndividualSensor[] getIndividualSensors();
    
    public abstract void setI2cAddress(final int p0);
    
    public abstract int getI2cAddress();
    
    @Override
    public String toString() {
        if (this.signalDetected()) {
            return String.format("IR Seeker: %3.0f%% signal at %6.1f degrees", this.getStrength() * 100.0, this.getAngle());
        }
        return "IR Seeker:  --% signal at  ---.- degrees";
    }
    
    public static void throwIfModernRoboticsI2cAddressIsInvalid(final int newAddress) {
        if (newAddress < 16 || newAddress > 126) {
            throw new IllegalArgumentException(String.format("New I2C address %d is invalid; valid range is: %d..%d", newAddress, 16, 126));
        }
        if (newAddress % 2 != 0) {
            throw new IllegalArgumentException(String.format("New I2C address %d is invalid; the address must be even.", newAddress));
        }
    }
    
    public enum Mode
    {
        MODE_600HZ, 
        MODE_1200HZ;
    }
    
    public static class IrSeekerIndividualSensor
    {
        private double a;
        private double b;
        
        public IrSeekerIndividualSensor() {
            this(0.0, 0.0);
        }
        
        public IrSeekerIndividualSensor(final double angle, final double strength) {
            this.a = 0.0;
            this.b = 0.0;
            this.a = angle;
            this.b = strength;
        }
        
        public double getSensorAngle() {
            return this.a;
        }
        
        public double getSensorStrength() {
            return this.b;
        }
        
        @Override
        public String toString() {
            return String.format("IR Sensor: %3.1f degrees at %3.1f%% power", this.a, this.b * 100.0);
        }
    }
}
