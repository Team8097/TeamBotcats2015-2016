// 
// Decompiled by Procyon v0.5.30
// 

package com.qualcomm.hardware;

import com.qualcomm.robotcore.util.RobotLog;

public class MatrixI2cTransaction
{
    public byte motor;
    public byte servo;
    public a property;
    public int value;
    public boolean write;
    public byte speed;
    public int target;
    public byte mode;
    public b state;
    
    MatrixI2cTransaction(final byte motor, final a property) {
        this.motor = motor;
        this.property = property;
        this.state = b.a;
        this.write = false;
    }
    
    MatrixI2cTransaction(final byte motor, final a property, final int value) {
        this.motor = motor;
        this.value = value;
        this.property = property;
        this.state = b.a;
        this.write = true;
    }
    
    MatrixI2cTransaction(final byte motor, final byte speed, final int target, final byte mode) {
        this.motor = motor;
        this.speed = speed;
        this.target = target;
        this.mode = mode;
        this.property = a.f;
        this.state = b.a;
        this.write = true;
    }
    
    MatrixI2cTransaction(final byte servo, final byte target, final byte speed) {
        this.servo = servo;
        this.speed = speed;
        this.target = target;
        this.property = a.g;
        this.state = b.a;
        this.write = true;
    }
    
    public boolean isEqual(final MatrixI2cTransaction transaction) {
        if (this.property != transaction.property) {
            return false;
        }
        switch (this.property) {
            case a:
            case i:
            case j:
            case b:
            case c:
            case d:
            case e: {
                return this.write == transaction.write && this.motor == transaction.motor && this.value == transaction.value;
            }
            case f: {
                return this.write == transaction.write && this.motor == transaction.motor && this.speed == transaction.speed && this.target == transaction.target && this.mode == transaction.mode;
            }
            case g: {
                return this.write == transaction.write && this.servo == transaction.servo && this.speed == transaction.speed && this.target == transaction.target;
            }
            case h: {
                return this.write == transaction.write && this.value == transaction.value;
            }
            default: {
                RobotLog.e("Can not compare against unknown transaction property " + transaction.toString());
                return false;
            }
        }
    }
    
    @Override
    public String toString() {
        if (this.property == a.f) {
            return "Matrix motor transaction: " + this.property + " motor " + this.motor + " write " + this.write + " speed " + this.speed + " target " + this.target + " mode " + this.mode;
        }
        if (this.property == a.g) {
            return "Matrix servo transaction: " + this.property + " servo " + this.servo + " write " + this.write + " change rate " + this.speed + " target " + this.target;
        }
        if (this.property == a.h) {
            return "Matrix servo transaction: " + this.property + " servo " + this.servo + " write " + this.write + " value " + this.value;
        }
        return "Matrix motor transaction: " + this.property + " motor " + this.motor + " write " + this.write + " value " + this.value;
    }
    
    enum b
    {
        a, 
        b, 
        c, 
        d, 
        e;
    }
    
    enum a
    {
        a, 
        b, 
        c, 
        d, 
        e, 
        f, 
        g, 
        h, 
        i, 
        j;
    }
}
