// 
// Decompiled by Procyon v0.5.30
// 

package com.qualcomm.robotcore.hardware;

public class DcMotor implements HardwareDevice
{
    protected Direction direction;
    protected DcMotorController controller;
    protected int portNumber;
    protected DcMotorController.RunMode mode;
    protected DcMotorController.DeviceMode devMode;
    
    public DcMotor(final DcMotorController controller, final int portNumber) {
        this(controller, portNumber, Direction.FORWARD);
    }
    
    public DcMotor(final DcMotorController controller, final int portNumber, final Direction direction) {
        this.direction = Direction.FORWARD;
        this.controller = null;
        this.portNumber = -1;
        this.mode = DcMotorController.RunMode.RUN_WITHOUT_ENCODERS;
        this.devMode = DcMotorController.DeviceMode.WRITE_ONLY;
        this.controller = controller;
        this.portNumber = portNumber;
        this.direction = direction;
    }
    
    @Override
    public String getDeviceName() {
        return "DC Motor";
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
        this.setPowerFloat();
    }
    
    public DcMotorController getController() {
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
    
    public void setPower(double power) {
        if (this.direction == Direction.REVERSE) {
            power *= -1.0;
        }
        if (this.mode == DcMotorController.RunMode.RUN_TO_POSITION) {
            power = Math.abs(power);
        }
        this.controller.setMotorPower(this.portNumber, power);
    }
    
    public double getPower() {
        double motorPower = this.controller.getMotorPower(this.portNumber);
        if (this.direction == Direction.REVERSE && motorPower != 0.0) {
            motorPower *= -1.0;
        }
        return motorPower;
    }
    
    public boolean isBusy() {
        return this.controller.isBusy(this.portNumber);
    }
    
    public void setPowerFloat() {
        this.controller.setMotorPowerFloat(this.portNumber);
    }
    
    public boolean getPowerFloat() {
        return this.controller.getMotorPowerFloat(this.portNumber);
    }
    
    public void setTargetPosition(int position) {
        if (this.direction == Direction.REVERSE) {
            position *= -1;
        }
        this.controller.setMotorTargetPosition(this.portNumber, position);
    }
    
    public int getTargetPosition() {
        int motorTargetPosition = this.controller.getMotorTargetPosition(this.portNumber);
        if (this.direction == Direction.REVERSE) {
            motorTargetPosition *= -1;
        }
        return motorTargetPosition;
    }
    
    public int getCurrentPosition() {
        int motorCurrentPosition = this.controller.getMotorCurrentPosition(this.portNumber);
        if (this.direction == Direction.REVERSE) {
            motorCurrentPosition *= -1;
        }
        return motorCurrentPosition;
    }
    
    public void setMode(final DcMotorController.RunMode mode) {
        this.mode = mode;
        this.controller.setMotorChannelMode(this.portNumber, mode);
    }
    
    public DcMotorController.RunMode getMode() {
        return this.controller.getMotorChannelMode(this.portNumber);
    }
    
    @Deprecated
    public void setChannelMode(final DcMotorController.RunMode mode) {
        this.setMode(mode);
    }
    
    @Deprecated
    public DcMotorController.RunMode getChannelMode() {
        return this.getMode();
    }
    
    public enum Direction
    {
        FORWARD, 
        REVERSE;
    }
}
