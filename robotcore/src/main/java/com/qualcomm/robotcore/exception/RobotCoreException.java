// 
// Decompiled by Procyon v0.5.30
// 

package com.qualcomm.robotcore.exception;

public class RobotCoreException extends Exception
{
    private Exception a;
    
    public RobotCoreException(final String message) {
        super(message);
        this.a = null;
    }
    
    public RobotCoreException(final String message, final Exception e) {
        super(message);
        this.a = null;
        this.a = e;
    }
    
    public boolean isChainedException() {
        return this.a != null;
    }
    
    public Exception getChainedException() {
        return this.a;
    }
}
