// 
// Decompiled by Procyon v0.5.30
// 

package com.qualcomm.robotcore.sensor;

public class TrackedTargetInfo
{
    public TargetInfo mTargetInfo;
    public double mConfidence;
    public long mTimeTracked;
    
    public TrackedTargetInfo(final TargetInfo targetInfo, final double reProjectionError, final long timeTracked) {
        this.mTargetInfo = targetInfo;
        this.mConfidence = reProjectionError;
        this.mTimeTracked = timeTracked;
    }
}
