// 
// Decompiled by Procyon v0.5.30
// 

package com.qualcomm.robotcore.util;

import java.util.LinkedList;
import java.util.Queue;

public class RollingAverage
{
    public static final int DEFAULT_SIZE = 100;
    private final Queue<Integer> a;
    private long b;
    private int c;
    
    public RollingAverage() {
        this.a = new LinkedList<Integer>();
        this.resize(100);
    }
    
    public RollingAverage(final int size) {
        this.a = new LinkedList<Integer>();
        this.resize(size);
    }
    
    public int size() {
        return this.c;
    }
    
    public void resize(final int size) {
        this.c = size;
        this.a.clear();
    }
    
    public void addNumber(final int number) {
        if (this.a.size() >= this.c) {
            this.b -= this.a.remove();
        }
        this.a.add(number);
        this.b += number;
    }
    
    public int getAverage() {
        if (this.a.isEmpty()) {
            return 0;
        }
        return (int)(this.b / this.a.size());
    }
    
    public void reset() {
        this.a.clear();
    }
}
