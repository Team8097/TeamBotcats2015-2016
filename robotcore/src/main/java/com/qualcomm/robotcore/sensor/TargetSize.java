// 
// Decompiled by Procyon v0.5.30
// 

package com.qualcomm.robotcore.sensor;

public class TargetSize
{
    public String mTargetName;
    public double mLongSide;
    public double mShortSide;
    
    public TargetSize() {
        this("", 0.0, 0.0);
    }
    
    public TargetSize(final String targetName, final double longSide, final double shortSide) {
        this.mTargetName = targetName;
        this.mLongSide = longSide;
        this.mShortSide = shortSide;
    }
}
