// 
// Decompiled by Procyon v0.5.30
// 

package com.qualcomm.robotcore.util;

public class CurvedWheelMotion
{
    public static int velocityForRotationMmPerSec(final int rotateAroundXInMM, final int rotateAroundYInMM, final double rotationalVelocityInDegPerSec, final int wheelOffsetXInMm, final int wheelOffsetYInMm) {
        final int n = (int)(rotationalVelocityInDegPerSec * (6.283185307179586 * (int)Math.sqrt(Math.pow(wheelOffsetXInMm - rotateAroundXInMM, 2.0) + Math.pow(wheelOffsetYInMm - rotateAroundYInMM, 2.0)) / 360.0));
        RobotLog.d("CurvedWheelMotion rX " + rotateAroundXInMM + ", theta " + rotationalVelocityInDegPerSec + ", velocity " + n);
        return n;
    }
    
    public static int getDiffDriveRobotWheelVelocity(final int linearVelocityInMmPerSec, final double rotationalVelocityInDegPerSec, final int wheelRadiusInMm, final int axleLengthInMm, final boolean leftWheel) {
        final double radians = Math.toRadians(rotationalVelocityInDegPerSec);
        double n;
        if (leftWheel) {
            n = (2 * linearVelocityInMmPerSec - radians * axleLengthInMm) / (2 * wheelRadiusInMm);
        }
        else {
            n = (2 * linearVelocityInMmPerSec + radians * axleLengthInMm) / (2 * wheelRadiusInMm);
        }
        return (int)(n * wheelRadiusInMm);
    }
    
    public static int getDiffDriveRobotTransVelocity(final int leftVelocityInMmPerSec, final int rightVelocityInMmPerSec) {
        return (leftVelocityInMmPerSec + rightVelocityInMmPerSec) / 2;
    }
    
    public static double getDiffDriveRobotRotVelocity(final int leftVelocityInMmPerSec, final int rightVelocityInMmPerSec, final int axleLengthInMm) {
        return Math.toDegrees((rightVelocityInMmPerSec - leftVelocityInMmPerSec) / axleLengthInMm);
    }
}
