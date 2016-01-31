// 
// Decompiled by Procyon v0.5.30
// 

package com.qualcomm.robotcore.util;

public class DifferentialControlLoopCoefficients
{
    public double p;
    public double i;
    public double d;
    
    public DifferentialControlLoopCoefficients() {
        this.p = 0.0;
        this.i = 0.0;
        this.d = 0.0;
    }
    
    public DifferentialControlLoopCoefficients(final double p, final double i, final double d) {
        this.p = 0.0;
        this.i = 0.0;
        this.d = 0.0;
        this.p = p;
        this.i = i;
        this.d = d;
    }
}
