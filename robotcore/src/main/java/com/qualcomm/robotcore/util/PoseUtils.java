// 
// Decompiled by Procyon v0.5.30
// 

package com.qualcomm.robotcore.util;

import android.util.Log;

public class PoseUtils
{
    public static double[] getAnglesAroundZ(final Pose inputPose) {
        double[] anglesAroundZ = null;
        if (inputPose != null && inputPose.poseMatrix != null) {
            anglesAroundZ = getAnglesAroundZ(inputPose.poseMatrix.submatrix(3, 3, 0, 0));
        }
        else {
            Log.e("PoseUtils", "null input");
        }
        return anglesAroundZ;
    }
    
    public static double[] getAnglesAroundZ(final MatrixD rotMat) {
        if (rotMat.numRows() != 3 || rotMat.numCols() != 3) {
            throw new IllegalArgumentException("Invalid Matrix Dimension: Expected (3,3) got (" + rotMat.numRows() + "," + rotMat.numCols() + ")");
        }
        final MatrixD times = rotMat.times(new MatrixD(new double[][] { { 0.0 }, { 0.0 }, { 1.0 } }));
        return new double[] { Math.toDegrees(Math.atan2(times.data()[1][0], times.data()[0][0])), Math.toDegrees(Math.atan2(times.data()[0][0], times.data()[1][0])), Math.toDegrees(Math.asin(times.data()[2][0] / times.length())) };
    }
    
    public static double smallestAngularDifferenceDegrees(final double firstAngleDeg, final double secondAngleDeg) {
        final double n = (firstAngleDeg - secondAngleDeg) * 3.141592653589793 / 180.0;
        return Math.atan2(Math.sin(n), Math.cos(n)) * 180.0 / 3.141592653589793;
    }
}
