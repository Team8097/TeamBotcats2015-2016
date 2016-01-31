// 
// Decompiled by Procyon v0.5.30
// 

package com.qualcomm.robotcore.sensor;

import com.qualcomm.robotcore.util.Pose;

public class TargetInfo
{
    public String mTargetName;
    public Pose mTargetPose;
    public TargetSize mTargetSize;
    
    public TargetInfo() {
    }
    
    public TargetInfo(final String targetName, final Pose targetPose, final TargetSize targetSize) {
        this.mTargetName = targetName;
        this.mTargetPose = targetPose;
        this.mTargetSize = targetSize;
    }
}
