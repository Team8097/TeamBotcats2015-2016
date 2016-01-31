// 
// Decompiled by Procyon v0.5.30
// 

package com.qualcomm.robotcore.sensor;

import java.util.List;

public abstract class SensorTargetPose extends SensorBase<List<TrackedTargetInfo>>
{
    public SensorTargetPose(final List<SensorListener<List<TrackedTargetInfo>>> listeners) {
        super(listeners);
    }
}
