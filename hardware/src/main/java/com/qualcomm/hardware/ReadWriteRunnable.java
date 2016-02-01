// 
// Decompiled by Procyon v0.5.30
// 

package com.qualcomm.hardware;

import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.eventloop.SyncdDevice;

interface ReadWriteRunnable extends SyncdDevice, Runnable
{
    void setCallback(final Callback p0);
    
    void blockUntilReady() throws RobotCoreException, InterruptedException;
    
    void write(final int p0, final byte[] p1);
    
    byte[] readFromWriteCache(final int p0, final int p1);
    
    byte[] read(final int p0, final int p1);
    
    void close();
    
    ReadWriteRunnableSegment createSegment(final int p0, final int p1, final int p2);
    
    void queueSegmentRead(final int p0);
    
    void queueSegmentWrite(final int p0);
    
    public enum BlockingState
    {
        BLOCKING, 
        WAITING;
    }
    
    public static class EmptyCallback implements Callback
    {
        @Override
        public void readComplete() throws InterruptedException {
        }
        
        @Override
        public void writeComplete() throws InterruptedException {
        }
    }
    
    public interface Callback
    {
        void readComplete() throws InterruptedException;
        
        void writeComplete() throws InterruptedException;
    }
}
