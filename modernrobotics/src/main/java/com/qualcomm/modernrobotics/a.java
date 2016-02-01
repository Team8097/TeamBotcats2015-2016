// 
// Decompiled by Procyon v0.5.30
// 

package com.qualcomm.modernrobotics;

import com.qualcomm.robotcore.util.TypeConversion;
import com.qualcomm.robotcore.util.RobotLog;

class a
{
    static boolean a(final byte[] array, final int n) {
        if (array.length < 5) {
            RobotLog.w("Modern Robotics USB header length is too short");
            return false;
        }
        if (array[0] != 51 || array[1] != -52) {
            RobotLog.w("Modern Robotics USB header sync bytes are incorrect");
            return false;
        }
        if (TypeConversion.unsignedByteToInt(array[4]) != n) {
            RobotLog.w("Modern Robotics USB header reported unexpected packet size");
            return false;
        }
        return true;
    }
}
