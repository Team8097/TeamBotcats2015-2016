// 
// Decompiled by Procyon v0.5.30
// 

package com.ftdi.j2xx;

public class FT_EEPROM_X_Series extends FT_EEPROM
{
    public short A_DeviceTypeValue;
    public boolean A_LoadVCP;
    public boolean A_LoadD2XX;
    public boolean BCDEnable;
    public boolean BCDForceCBusPWREN;
    public boolean BCDDisableSleep;
    public byte CBus0;
    public byte CBus1;
    public byte CBus2;
    public byte CBus3;
    public byte CBus4;
    public byte CBus5;
    public byte CBus6;
    public boolean FT1248ClockPolarity;
    public boolean FT1248LSB;
    public boolean FT1248FlowControl;
    public boolean InvertTXD;
    public boolean InvertRXD;
    public boolean InvertRTS;
    public boolean InvertCTS;
    public boolean InvertDTR;
    public boolean InvertDSR;
    public boolean InvertDCD;
    public boolean InvertRI;
    public int I2CSlaveAddress;
    public int I2CDeviceID;
    public boolean I2CDisableSchmitt;
    public boolean AD_SlowSlew;
    public boolean AD_SchmittInput;
    public byte AD_DriveCurrent;
    public boolean AC_SlowSlew;
    public boolean AC_SchmittInput;
    public byte AC_DriveCurrent;
    public boolean RS485EchoSuppress;
    public boolean PowerSaveEnable;
    
    public FT_EEPROM_X_Series() {
        this.A_DeviceTypeValue = 0;
        this.A_LoadVCP = false;
        this.A_LoadD2XX = false;
        this.BCDEnable = false;
        this.BCDForceCBusPWREN = false;
        this.BCDDisableSleep = false;
        this.CBus0 = 0;
        this.CBus1 = 0;
        this.CBus2 = 0;
        this.CBus3 = 0;
        this.CBus4 = 0;
        this.CBus5 = 0;
        this.CBus6 = 0;
        this.FT1248ClockPolarity = false;
        this.FT1248LSB = false;
        this.FT1248FlowControl = false;
        this.InvertTXD = false;
        this.InvertRXD = false;
        this.InvertRTS = false;
        this.InvertCTS = false;
        this.InvertDTR = false;
        this.InvertDSR = false;
        this.InvertDCD = false;
        this.InvertRI = false;
        this.I2CSlaveAddress = 0;
        this.I2CDeviceID = 0;
        this.I2CDisableSchmitt = false;
        this.AD_SlowSlew = false;
        this.AD_SchmittInput = false;
        this.AD_DriveCurrent = 0;
        this.AC_SlowSlew = false;
        this.AC_SchmittInput = false;
        this.AC_DriveCurrent = 0;
        this.RS485EchoSuppress = false;
        this.PowerSaveEnable = false;
    }
    
    public static final class DRIVE_STRENGTH
    {
    }
    
    public static final class CBUS
    {
    }
}
