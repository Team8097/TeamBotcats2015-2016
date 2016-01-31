// 
// Decompiled by Procyon v0.5.30
// 

package com.qualcomm.robotcore.sensor;

import java.util.Iterator;
import java.util.List;

public abstract class SensorBase<T>
{
    protected List<SensorListener<T>> mListeners;
    
    public SensorBase(final List<SensorListener<T>> listeners) {
        this.mListeners = listeners;
    }
    
    public abstract boolean initialize();
    
    public abstract boolean shutdown();
    
    public abstract boolean resume();
    
    public abstract boolean pause();
    
    public final void update(final T data) {
        synchronized (this.mListeners) {
            if (this.mListeners == null) {
                return;
            }
            final Iterator<SensorListener<T>> iterator = this.mListeners.iterator();
            while (iterator.hasNext()) {
                iterator.next().onUpdate(data);
            }
        }
    }
}
