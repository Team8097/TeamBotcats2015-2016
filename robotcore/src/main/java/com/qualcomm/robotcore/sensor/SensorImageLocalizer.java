// 
// Decompiled by Procyon v0.5.30
// 

package com.qualcomm.robotcore.sensor;

import java.util.Iterator;
import com.qualcomm.robotcore.util.PoseUtils;
import android.util.Log;
import com.qualcomm.robotcore.util.MatrixD;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import com.qualcomm.robotcore.util.Pose;

public class SensorImageLocalizer extends SensorBase<Pose> implements SensorListener<List<TrackedTargetInfo>>
{
    private final boolean a = false;
    private final String b = "SensorImageLocalizer";
    private final Map<String, TargetInfo> c;
    private Pose d;
    private final HashMap<String, a> e;
    private a f;
    
    public SensorImageLocalizer(final List<SensorListener<Pose>> l) {
        super(l);
        this.e = new HashMap<String, a>();
        this.c = new HashMap<String, TargetInfo>();
    }
    
    @Override
    public boolean initialize() {
        return true;
    }
    
    @Override
    public boolean shutdown() {
        return true;
    }
    
    @Override
    public boolean resume() {
        return true;
    }
    
    @Override
    public boolean pause() {
        return true;
    }
    
    public void AddListener(final SensorListener<Pose> l) {
        synchronized (this.mListeners) {
            if (!this.mListeners.contains(l)) {
                this.mListeners.add((SensorListener<Pose>)l);
            }
        }
    }
    
    public void RemoveListener(final SensorListener<Pose> l) {
        synchronized (this.mListeners) {
            if (this.mListeners.contains(l)) {
                this.mListeners.remove(l);
            }
        }
    }
    
    public boolean addTargetReference(final String targetName, final double xTrans, final double yTrans, final double zTrans, final double angle, final double longSideTransFromCenterToVertex, final double shortSideTransFromCenterToVertex) {
        if (targetName == null) {
            throw new IllegalArgumentException("Null targetInfoWorldRef");
        }
        if (this.c.containsKey(targetName)) {
            return false;
        }
        final MatrixD rotationY = Pose.makeRotationY(Math.toRadians(angle));
        final MatrixD poseMatrix = new MatrixD(3, 4);
        poseMatrix.setSubmatrix(rotationY, 3, 3, 0, 0);
        poseMatrix.data()[0][3] = yTrans;
        poseMatrix.data()[1][3] = zTrans;
        poseMatrix.data()[2][3] = xTrans;
        final Pose targetPose = new Pose(poseMatrix);
        Log.d("SensorImageLocalizer", "Target Pose \n" + poseMatrix);
        this.c.put(targetName, new TargetInfo(targetName, targetPose, new TargetSize(targetName, longSideTransFromCenterToVertex, shortSideTransFromCenterToVertex)));
        return true;
    }
    
    public boolean addRobotToCameraRef(final double length, final double width, final double height, final double angle) {
        final MatrixD matrixD = new MatrixD(3, 3);
        final MatrixD rotationY = Pose.makeRotationY(-angle);
        final MatrixD poseMatrix = new MatrixD(3, 4);
        poseMatrix.setSubmatrix(rotationY, 3, 3, 0, 0);
        poseMatrix.data()[0][3] = width;
        poseMatrix.data()[1][3] = -height;
        poseMatrix.data()[2][3] = length;
        this.d = new Pose(poseMatrix);
        return true;
    }
    
    public boolean removeTargetReference(final String targetName) {
        if (targetName == null) {
            throw new IllegalArgumentException("Null targetName");
        }
        if (this.c.containsKey(targetName)) {
            this.c.remove(targetName);
            return true;
        }
        return false;
    }
    
    private boolean a(final TrackedTargetInfo trackedTargetInfo) {
        final long n = System.currentTimeMillis() / 1000L;
        a a;
        if (this.e.containsKey(trackedTargetInfo.mTargetInfo.mTargetName)) {
            a = this.e.get(trackedTargetInfo.mTargetInfo.mTargetName);
            a.b = trackedTargetInfo.mTimeTracked;
            a.e = trackedTargetInfo.mConfidence;
            if (n - a.b > 120L) {
                a.c = 1;
            }
            else {
                final a a2 = a;
                ++a2.c;
            }
        }
        else {
            a = new a();
            a.e = trackedTargetInfo.mConfidence;
            a.d = trackedTargetInfo.mTargetInfo.mTargetName;
            a.b = trackedTargetInfo.mTimeTracked;
            a.c = 1;
            this.e.put(trackedTargetInfo.mTargetInfo.mTargetName, a);
        }
        if (this.f != null && this.f.d != a.d && n - this.f.a < 10L) {
            Log.d("SensorImageLocalizer", "Ignoring target " + trackedTargetInfo.mTargetInfo.mTargetName + " Time diff " + (n - this.f.a));
            return false;
        }
        return true;
    }
    
    @Override
    public void onUpdate(final List<TrackedTargetInfo> targetPoses) {
        Log.d("SensorImageLocalizer", "SensorImageLocalizer onUpdate");
        if (targetPoses == null || targetPoses.size() < 1) {
            Log.d("SensorImageLocalizer", "SensorImageLocalizer onUpdate NULL");
            this.update(null);
            return;
        }
        boolean b = false;
        double mConfidence = Double.MIN_VALUE;
        final long a = System.currentTimeMillis() / 1000L;
        TrackedTargetInfo trackedTargetInfo = null;
        a f = null;
        for (final TrackedTargetInfo trackedTargetInfo2 : targetPoses) {
            if (this.c.containsKey(trackedTargetInfo2.mTargetInfo.mTargetName)) {
                if (this.a(trackedTargetInfo2) && trackedTargetInfo2.mConfidence > mConfidence) {
                    f = this.e.get(trackedTargetInfo2.mTargetInfo.mTargetName);
                    trackedTargetInfo = trackedTargetInfo2;
                    mConfidence = trackedTargetInfo2.mConfidence;
                    b = true;
                    Log.d("SensorImageLocalizer", "Potential target " + trackedTargetInfo2.mTargetInfo.mTargetName + " Confidence " + trackedTargetInfo2.mConfidence);
                }
                else {
                    Log.d("SensorImageLocalizer", "Ignoring target " + trackedTargetInfo2.mTargetInfo.mTargetName + " Confidence " + trackedTargetInfo2.mConfidence);
                }
            }
        }
        if (!b) {
            this.update(null);
            return;
        }
        final TargetInfo targetInfo = this.c.get(trackedTargetInfo.mTargetInfo.mTargetName);
        f.a = a;
        this.f = f;
        Log.d("SensorImageLocalizer", "Selected target " + trackedTargetInfo.mTargetInfo.mTargetName + " time " + a);
        MatrixD submatrix = null;
        if (this.d != null) {
            submatrix = this.d.poseMatrix.submatrix(3, 3, 0, 0);
        }
        final MatrixD transpose = trackedTargetInfo.mTargetInfo.mTargetPose.poseMatrix.submatrix(3, 3, 0, 0).transpose();
        final MatrixD submatrix2 = targetInfo.mTargetPose.poseMatrix.submatrix(3, 3, 0, 0);
        final MatrixD times = Pose.makeRotationX(Math.toRadians(90.0)).times(Pose.makeRotationY(Math.toRadians(90.0)));
        MatrixD inData = times.times(submatrix2).times(transpose);
        if (submatrix != null) {
            inData = inData.times(submatrix);
        }
        final MatrixD other = new MatrixD(3, 1);
        other.data()[0][0] = targetInfo.mTargetSize.mLongSide;
        other.data()[1][0] = targetInfo.mTargetSize.mShortSide;
        other.data()[2][0] = 0.0;
        final MatrixD times2 = transpose.times(trackedTargetInfo.mTargetInfo.mTargetPose.getTranslationMatrix());
        MatrixD translationMatrix = new MatrixD(3, 1);
        if (this.d != null) {
            translationMatrix = this.d.getTranslationMatrix();
        }
        final MatrixD times3 = times.times(targetInfo.mTargetPose.getTranslationMatrix().subtract(submatrix2.times(times2.add(transpose.times(translationMatrix)).add(other))));
        final MatrixD poseMatrix = new MatrixD(3, 4);
        poseMatrix.setSubmatrix(inData, 3, 3, 0, 0);
        poseMatrix.setSubmatrix(times3, 3, 1, 0, 3);
        final Pose pose = new Pose(poseMatrix);
        final double[] anglesAroundZ = PoseUtils.getAnglesAroundZ(pose);
        Log.d("SensorImageLocalizer", String.format("POSE_HEADING: x %8.4f z %8.4f up %8.4f", anglesAroundZ[0], anglesAroundZ[1], anglesAroundZ[2]));
        final MatrixD translationMatrix2 = pose.getTranslationMatrix();
        Log.d("SensorImageLocalizer", String.format("POSE_TRANS: x %8.4f y %8.4f z %8.4f", translationMatrix2.data()[0][0], translationMatrix2.data()[1][0], translationMatrix2.data()[2][0]));
        this.update(pose);
    }
    
    private class a
    {
        public long a;
        public long b;
        public int c;
        public String d;
        public double e;
    }
}
