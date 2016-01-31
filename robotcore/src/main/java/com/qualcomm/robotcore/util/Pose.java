// 
// Decompiled by Procyon v0.5.30
// 

package com.qualcomm.robotcore.util;

public class Pose
{
    public double transX;
    public double transY;
    public double transZ;
    public MatrixD poseMatrix;
    
    public Pose(final MatrixD poseMatrix) {
        if (poseMatrix == null) {
            throw new IllegalArgumentException("Attempted to construct Pose from null matrix");
        }
        if (poseMatrix.numRows() != 3 || poseMatrix.numCols() != 4) {
            throw new IllegalArgumentException("Invalid matrix size ( " + poseMatrix.numRows() + ", " + poseMatrix.numCols() + " )");
        }
        this.poseMatrix = poseMatrix;
        this.transX = poseMatrix.data()[0][3];
        this.transY = poseMatrix.data()[1][3];
        this.transZ = poseMatrix.data()[2][3];
    }
    
    public Pose(final double transX, final double transY, final double transZ) {
        this.transX = transX;
        this.transY = transY;
        this.transZ = transZ;
        this.poseMatrix = new MatrixD(3, 4);
        final double[] array = this.poseMatrix.data()[0];
        final int n = 0;
        final double[] array2 = this.poseMatrix.data()[1];
        final int n2 = 1;
        final double[] array3 = this.poseMatrix.data()[2];
        final int n3 = 2;
        final double n4 = 1.0;
        array3[n3] = n4;
        array[n] = (array2[n2] = n4);
        this.poseMatrix.data()[0][3] = transX;
        this.poseMatrix.data()[1][3] = transY;
        this.poseMatrix.data()[2][3] = transZ;
    }
    
    public Pose() {
        this.transX = 0.0;
        this.transY = 0.0;
        this.transZ = 0.0;
    }
    
    public MatrixD getTranslationMatrix() {
        return new MatrixD(new double[][] { { this.transX }, { this.transY }, { this.transZ } });
    }
    
    public static MatrixD makeRotationX(final double angle) {
        final double[][] init = new double[3][3];
        final double cos = Math.cos(angle);
        final double sin = Math.sin(angle);
        init[0][0] = 1.0;
        final double[] array = init[0];
        final int n = 1;
        final double[] array2 = init[0];
        final int n2 = 2;
        final double[] array3 = init[1];
        final int n3 = 0;
        final double[] array4 = init[2];
        final int n4 = 0;
        final double n5 = 0.0;
        array3[n3] = (array4[n4] = n5);
        array[n] = (array2[n2] = n5);
        init[1][1] = (init[2][2] = cos);
        init[1][2] = -sin;
        init[2][1] = sin;
        return new MatrixD(init);
    }
    
    public static MatrixD makeRotationY(final double angle) {
        final double[][] init = new double[3][3];
        final double cos = Math.cos(angle);
        final double sin = Math.sin(angle);
        final double[] array = init[0];
        final int n = 1;
        final double[] array2 = init[1];
        final int n2 = 0;
        final double[] array3 = init[1];
        final int n3 = 2;
        final double[] array4 = init[2];
        final int n4 = 1;
        final double n5 = 0.0;
        array3[n3] = (array4[n4] = n5);
        array[n] = (array2[n2] = n5);
        init[1][1] = 1.0;
        init[0][0] = (init[2][2] = cos);
        init[0][2] = sin;
        init[2][0] = -sin;
        return new MatrixD(init);
    }
    
    public static MatrixD makeRotationZ(final double angle) {
        final double[][] init = new double[3][3];
        final double cos = Math.cos(angle);
        final double sin = Math.sin(angle);
        init[2][2] = 1.0;
        final double[] array = init[2];
        final int n = 0;
        final double[] array2 = init[2];
        final int n2 = 1;
        final double[] array3 = init[0];
        final int n3 = 2;
        final double[] array4 = init[1];
        final int n4 = 2;
        final double n5 = 0.0;
        array3[n3] = (array4[n4] = n5);
        array[n] = (array2[n2] = n5);
        init[0][0] = (init[1][1] = cos);
        init[0][1] = -sin;
        init[1][0] = sin;
        return new MatrixD(init);
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        final double[] anglesAroundZ = PoseUtils.getAnglesAroundZ(this);
        sb.append(String.format("(XYZ %1$,.2f ", this.transX));
        sb.append(String.format(" %1$,.2f ", this.transY));
        sb.append(String.format(" %1$,.2f mm)", this.transZ));
        sb.append(String.format("(Angles %1$,.2f, ", anglesAroundZ[0]));
        sb.append(String.format(" %1$,.2f, ", anglesAroundZ[1]));
        sb.append(String.format(" %1$,.2f ", anglesAroundZ[2]));
        sb.append('°');
        sb.append(")");
        return sb.toString();
    }
    
    public double getDistanceInMm() {
        return Math.sqrt(Math.pow(this.transX, 2.0) + Math.pow(this.transY, 2.0) + Math.pow(this.transZ, 2.0));
    }
}
