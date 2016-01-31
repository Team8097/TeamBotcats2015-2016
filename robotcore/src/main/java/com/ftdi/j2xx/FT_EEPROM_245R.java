// 
// Decompiled by Procyon v0.5.30
// 

package com.ftdi.j2xx;

public class FT_EEPROM_245R extends FT_EEPROM
{
    public boolean HighIO;
    public boolean ExternalOscillator;
    public boolean InvertTXD;
    public boolean InvertRXD;
    public boolean InvertRTS;
    public boolean InvertCTS;
    public boolean InvertDTR;
    public boolean InvertDSR;
    public boolean InvertDCD;
    public boolean InvertRI;
    public byte CBus0;
    public byte CBus1;
    public byte CBus2;
    public byte CBus3;
    public byte CBus4;
    public boolean LoadVCP;
    
    public FT_EEPROM_245R() {
        this.HighIO = false;
        this.ExternalOscillator = false;
        this.InvertTXD = false;
        this.InvertRXD = false;
        this.InvertRTS = false;
        this.InvertCTS = false;
        this.InvertDTR = false;
        this.InvertDSR = false;
        this.InvertDCD = false;
        this.InvertRI = false;
        this.CBus0 = 0;
        this.CBus1 = 0;
        this.CBus2 = 0;
        this.CBus3 = 0;
        this.CBus4 = 0;
        this.LoadVCP = false;
    }
    
    public static final class CBUS
    {
    }
}
