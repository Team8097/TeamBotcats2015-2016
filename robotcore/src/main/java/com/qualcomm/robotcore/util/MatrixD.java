// 
// Decompiled by Procyon v0.5.30
// 

package com.qualcomm.robotcore.util;

import android.util.Log;

public class MatrixD
{
    protected double[][] mData;
    protected int mRows;
    protected int mCols;
    
    public MatrixD(final int rows, final int cols) {
        this(new double[rows][cols]);
    }
    
    public MatrixD(final double[] init, final int rows, final int cols) {
        this(rows, cols);
        if (init == null) {
            throw new IllegalArgumentException("Attempted to initialize MatrixF with null array");
        }
        if (init.length != rows * cols) {
            throw new IllegalArgumentException("Attempted to initialize MatrixF with rows/cols not matching init data");
        }
        for (int i = 0; i < rows; ++i) {
            for (int j = 0; j < cols; ++j) {
                this.mData[i][j] = init[j + cols * i];
            }
        }
    }
    
    public MatrixD(final float[] init, final int rows, final int cols) {
        this(rows, cols);
        if (init == null) {
            throw new IllegalArgumentException("Attempted to initialize MatrixF with null array");
        }
        if (init.length != rows * cols) {
            throw new IllegalArgumentException("Attempted to initialize MatrixF with rows/cols not matching init data");
        }
        for (int i = 0; i < rows; ++i) {
            for (int j = 0; j < cols; ++j) {
                this.mData[i][j] = init[j + cols * i];
            }
        }
    }
    
    public MatrixD(final double[][] init) {
        this.mData = init;
        if (this.mData == null) {
            throw new IllegalArgumentException("Attempted to initialize MatrixF with null array");
        }
        this.mRows = this.mData.length;
        if (this.mRows <= 0) {
            throw new IllegalArgumentException("Attempted to initialize MatrixF with 0 rows");
        }
        this.mCols = this.mData[0].length;
        for (int i = 0; i < this.mRows; ++i) {
            if (this.mData[i].length != this.mCols) {
                throw new IllegalArgumentException("Attempted to initialize MatrixF with rows of unequal length");
            }
        }
    }
    
    public int numRows() {
        return this.mRows;
    }
    
    public int numCols() {
        return this.mCols;
    }
    
    public double[][] data() {
        return this.mData;
    }
    
    public MatrixD submatrix(final int rows, final int cols, final int rowOffset, final int colOffset) {
        if (rows > this.numRows() || cols > this.numCols()) {
            throw new IllegalArgumentException("Attempted to get submatrix with size larger than original");
        }
        if (rowOffset + rows > this.numRows() || colOffset + cols > this.numCols()) {
            throw new IllegalArgumentException("Attempted to access out of bounds data with row or col offset out of range");
        }
        final double[][] init = new double[rows][cols];
        for (int i = 0; i < rows; ++i) {
            for (int j = 0; j < cols; ++j) {
                init[i][j] = this.data()[rowOffset + i][colOffset + j];
            }
        }
        return new MatrixD(init);
    }
    
    public boolean setSubmatrix(final MatrixD inData, final int rows, final int cols, final int rowOffset, final int colOffset) {
        if (inData == null) {
            throw new IllegalArgumentException("Input data to setSubMatrix null");
        }
        if (rows > this.numRows() || cols > this.numCols()) {
            throw new IllegalArgumentException("Attempted to get submatrix with size larger than original");
        }
        if (rowOffset + rows > this.numRows() || colOffset + cols > this.numCols()) {
            throw new IllegalArgumentException("Attempted to access out of bounds data with row or col offset out of range");
        }
        if (rows > inData.numRows() || cols > inData.numCols()) {
            throw new IllegalArgumentException("Input matrix small for setSubMatrix");
        }
        if (rowOffset + rows > inData.numRows() || colOffset + cols > this.numCols()) {
            throw new IllegalArgumentException("Input matrix Attempted to access out of bounds data with row or col offset out of range");
        }
        for (int i = 0; i < rows; ++i) {
            for (int j = 0; j < cols; ++j) {
                this.data()[rowOffset + i][colOffset + j] = inData.data()[i][j];
            }
        }
        return true;
    }
    
    public MatrixD transpose() {
        final int mRows = this.mRows;
        final int mCols = this.mCols;
        final double[][] init = new double[mCols][mRows];
        for (int i = 0; i < mCols; ++i) {
            for (int j = 0; j < mRows; ++j) {
                init[i][j] = this.mData[j][i];
            }
        }
        return new MatrixD(init);
    }
    
    public MatrixD add(final MatrixD other) {
        final double[][] init = new double[this.numRows()][this.numCols()];
        final int numRows = this.numRows();
        final int numCols = this.numCols();
        for (int i = 0; i < numRows; ++i) {
            for (int j = 0; j < numCols; ++j) {
                init[i][j] = this.data()[i][j] + other.data()[i][j];
            }
        }
        return new MatrixD(init);
    }
    
    public MatrixD add(final double val) {
        final double[][] init = new double[this.numRows()][this.numCols()];
        final int numRows = this.numRows();
        final int numCols = this.numCols();
        for (int i = 0; i < numRows; ++i) {
            for (int j = 0; j < numCols; ++j) {
                init[i][j] = this.data()[i][j] + val;
            }
        }
        return new MatrixD(init);
    }
    
    public MatrixD subtract(final MatrixD other) {
        final double[][] init = new double[this.numRows()][this.numCols()];
        final int numRows = this.numRows();
        final int numCols = this.numCols();
        for (int i = 0; i < numRows; ++i) {
            for (int j = 0; j < numCols; ++j) {
                init[i][j] = this.data()[i][j] - other.data()[i][j];
            }
        }
        return new MatrixD(init);
    }
    
    public MatrixD subtract(final double val) {
        final double[][] init = new double[this.numRows()][this.numCols()];
        final int numRows = this.numRows();
        final int numCols = this.numCols();
        for (int i = 0; i < numRows; ++i) {
            for (int j = 0; j < numCols; ++j) {
                init[i][j] = this.data()[i][j] - val;
            }
        }
        return new MatrixD(init);
    }
    
    public MatrixD times(final MatrixD other) {
        if (this.numCols() != other.numRows()) {
            throw new IllegalArgumentException("Attempted to multiply matrices of invalid dimensions (AB) where A is " + this.numRows() + "x" + this.numCols() + ", B is " + other.numRows() + "x" + other.numCols());
        }
        final int numCols = this.numCols();
        final int numRows = this.numRows();
        final int numCols2 = other.numCols();
        final double[][] init = new double[numRows][numCols2];
        for (int i = 0; i < numRows; ++i) {
            for (int j = 0; j < numCols2; ++j) {
                for (int k = 0; k < numCols; ++k) {
                    final double[] array = init[i];
                    final int n = j;
                    array[n] += this.data()[i][k] * other.data()[k][j];
                }
            }
        }
        return new MatrixD(init);
    }
    
    public MatrixD times(final double f) {
        final double[][] init = new double[this.numRows()][this.numCols()];
        for (int i = 0; i < this.numRows(); ++i) {
            for (int j = 0; j < this.numCols(); ++j) {
                init[i][j] = this.data()[i][j] * f;
            }
        }
        return new MatrixD(init);
    }
    
    public double length() {
        if (this.numRows() != 1 && this.numCols() != 1) {
            throw new IndexOutOfBoundsException("Not a 1D matrix ( " + this.numRows() + ", " + this.numCols() + " )");
        }
        double n = 0.0;
        for (int i = 0; i < this.numRows(); ++i) {
            for (int j = 0; j < this.numCols(); ++j) {
                n += this.mData[i][j] * this.mData[i][j];
            }
        }
        return Math.sqrt(n);
    }
    
    @Override
    public String toString() {
        String s = new String();
        for (int i = 0; i < this.numRows(); ++i) {
            String s2 = new String();
            for (int j = 0; j < this.numCols(); ++j) {
                s2 += String.format("%.4f", this.data()[i][j]);
                if (j < this.numCols() - 1) {
                    s2 += ", ";
                }
            }
            s += s2;
            if (i < this.numRows() - 1) {
                s += "\n";
            }
        }
        return s + "\n";
    }
    
    public static void test() {
        Log.e("MatrixD", "Hello2 matrix");
        final MatrixD other = new MatrixD(new double[][] { { 1.0, 0.0, -2.0 }, { 0.0, 3.0, -1.0 } });
        Log.e("MatrixD", "Hello3 matrix");
        Log.e("MatrixD", "A = \n" + other.toString());
        final MatrixD other2 = new MatrixD(new double[][] { { 0.0, 3.0 }, { -2.0, -1.0 }, { 0.0, 4.0 } });
        Log.e("MatrixD", "B = \n" + other2.toString());
        Log.e("MatrixD", "A transpose = " + other.transpose().toString());
        Log.e("MatrixD", "B transpose = " + other2.transpose().toString());
        Log.e("MatrixD", "AB = \n" + other.times(other2).toString());
        final MatrixD times = other2.times(other);
        Log.e("MatrixD", "BA = \n" + times.toString());
        Log.e("MatrixD", "BA*2 = " + times.times(2.0).toString());
        Log.e("MatrixD", "BA submatrix 3,2,0,1 = " + times.submatrix(3, 2, 0, 1));
        Log.e("MatrixD", "BA submatrix 2,1,1,2 = " + times.submatrix(2, 1, 1, 2));
    }
}
