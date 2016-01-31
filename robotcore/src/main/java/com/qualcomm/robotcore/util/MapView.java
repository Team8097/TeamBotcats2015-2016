// 
// Decompiled by Procyon v0.5.30
// 

package com.qualcomm.robotcore.util;

import android.graphics.drawable.Drawable;
import android.graphics.Matrix;
import java.util.Iterator;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.content.Context;
import android.util.Log;
import java.util.HashMap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

public class MapView extends View
{
    private int b;
    private int c;
    private int d;
    private int e;
    private Paint f;
    private Canvas g;
    private Bitmap h;
    private boolean i;
    private boolean j;
    MapView a;
    private int k;
    private float l;
    private float m;
    private BitmapDrawable n;
    private int o;
    private int p;
    private int q;
    private boolean r;
    private HashMap<Integer, a> s;
    private Bitmap t;
    
    protected void onSizeChanged(final int x, final int y, final int oldx, final int oldy) {
        this.l = this.getWidth() / this.b;
        this.m = this.getHeight() / this.c;
        this.j = true;
        this.redraw();
        Log.e("MapView", "Size changed");
    }
    
    public MapView(final Context context) {
        super(context);
        this.i = false;
        this.j = false;
        this.k = 1;
        this.r = false;
        this.a();
    }
    
    public MapView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        this.i = false;
        this.j = false;
        this.k = 1;
        this.r = false;
        this.a();
    }
    
    public MapView(final Context context, final AttributeSet attrs, final int defStyle) {
        super(context, attrs, defStyle);
        this.i = false;
        this.j = false;
        this.k = 1;
        this.r = false;
        this.a();
    }
    
    private void a() {
        (this.f = new Paint()).setColor(-16777216);
        this.f.setStrokeWidth(1.0f);
        this.f.setAntiAlias(true);
        this.a = this;
        this.s = new HashMap<Integer, a>();
    }
    
    private int a(final int n) {
        if (n % 2 == 0) {
            return n;
        }
        return n + 1;
    }
    
    public void setup(final int xMax, final int yMax, final int numLinesX, final int numLinesY, final Bitmap robotIcon) {
        this.b = xMax * 2;
        this.c = yMax * 2;
        this.d = this.b / this.a(numLinesX);
        this.e = this.c / this.a(numLinesY);
        this.t = robotIcon;
        this.i = true;
    }
    
    private void b() {
        this.h = Bitmap.createBitmap(this.getWidth(), this.getHeight(), Bitmap.Config.ARGB_8888);
        this.g = new Canvas(this.h);
        final Paint paint = new Paint();
        paint.setColor(-1);
        paint.setAntiAlias(true);
        this.g.drawRect(0.0f, 0.0f, (float)this.g.getWidth(), (float)this.g.getHeight(), paint);
        for (int i = 0; i < this.c; i += this.e) {
            final float n = i * this.m;
            this.g.drawLine(0.0f, n, (float)this.g.getWidth(), n, this.f);
        }
        for (int j = 0; j < this.b; j += this.d) {
            final float n2 = j * this.l;
            this.g.drawLine(n2, 0.0f, n2, (float)this.g.getHeight(), this.f);
        }
    }
    
    private float b(final int n) {
        return n * this.l + this.getWidth() / 2;
    }
    
    private float c(final int n) {
        return this.getHeight() / 2 - n * this.m;
    }
    
    private int d(final int n) {
        return 360 - n;
    }
    
    public void setRobotLocation(final int x, final int y, final int angle) {
        this.o = -x;
        this.p = y;
        this.q = angle;
        this.r = true;
    }
    
    public int addMarker(final int x, final int y, final int color) {
        final int n = this.k++;
        this.s.put(n, new a(n, -x, y, color, true));
        return n;
    }
    
    public boolean removeMarker(final int id) {
        return this.s.remove(id) != null;
    }
    
    public int addDrawable(final int x, final int y, final int resource) {
        final int n = this.k++;
        this.s.put(n, new a(n, -x, y, resource, false));
        return n;
    }
    
    private void c() {
        for (final a a : this.s.values()) {
            final float b = this.b(a.b);
            final float c = this.c(a.c);
            if (a.e) {
                final Paint paint = new Paint();
                paint.setColor(a.d);
                this.g.drawCircle(b, c, 5.0f, paint);
            }
            else {
                final Bitmap decodeResource = BitmapFactory.decodeResource(this.getResources(), a.d);
                this.g.drawBitmap(decodeResource, b - decodeResource.getWidth() / 2, c - decodeResource.getHeight() / 2, new Paint());
            }
        }
    }
    
    private void d() {
        final float b = this.b(this.o);
        final float c = this.c(this.p);
        final int d = this.d(this.q);
        final Matrix matrix = new Matrix();
        matrix.postRotate((float)d);
        matrix.postScale(0.2f, 0.2f);
        final Bitmap t = this.t;
        final Bitmap bitmap = Bitmap.createBitmap(t, 0, 0, t.getWidth(), t.getHeight(), matrix, true);
        this.g.drawBitmap(bitmap, b - bitmap.getWidth() / 2, c - bitmap.getHeight() / 2, new Paint());
    }
    
    public void redraw() {
        if (this.i && this.j) {
            this.b();
            this.c();
            if (this.r) {
                this.d();
            }
        }
        this.n = new BitmapDrawable(this.getResources(), this.h);
        this.a.setBackgroundDrawable((Drawable)this.n);
    }
    
    private class a
    {
        public int a;
        public int b;
        public int c;
        public int d;
        public boolean e;
        
        public a(final int a, final int b, final int c, final int d, final boolean e) {
            this.a = a;
            this.b = b;
            this.c = c;
            this.d = d;
            this.e = e;
        }
    }
}
