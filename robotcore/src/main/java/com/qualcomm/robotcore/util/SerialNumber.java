// 
// Decompiled by Procyon v0.5.30
// 

package com.qualcomm.robotcore.util;

import java.io.Serializable;

public class SerialNumber implements Serializable
{
    private String a;
    
    public SerialNumber() {
        this.a = "N/A";
    }
    
    public SerialNumber(final String serialNumber) {
        this.a = serialNumber;
    }
    
    public String getSerialNumber() {
        return this.a;
    }
    
    public void setSerialNumber(final String serialNumber) {
        this.a = serialNumber;
    }
    
    @Override
    public boolean equals(final Object object) {
        if (object == null) {
            return false;
        }
        if (object == this) {
            return true;
        }
        if (object instanceof SerialNumber) {
            return this.a.equals(((SerialNumber)object).getSerialNumber());
        }
        return object instanceof String && this.a.equals(object);
    }
    
    @Override
    public int hashCode() {
        return this.a.hashCode();
    }
    
    @Override
    public String toString() {
        return this.a;
    }
}
