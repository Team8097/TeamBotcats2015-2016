// 
// Decompiled by Procyon v0.5.30
// 

package com.ftdi.j2xx;

public class FT_EEPROM
{
    public short DeviceType;
    public String Manufacturer;
    public String Product;
    public String SerialNumber;
    public short VendorId;
    public short ProductId;
    public boolean SerNumEnable;
    public short MaxPower;
    public boolean SelfPowered;
    public boolean RemoteWakeup;
    public boolean PullDownEnable;
    
    public FT_EEPROM() {
        this.DeviceType = 0;
        this.Manufacturer = "FTDI";
        this.Product = "USB <-> Serial Converter";
        this.SerialNumber = "FT123456";
        this.VendorId = 1027;
        this.ProductId = 24577;
        this.SerNumEnable = true;
        this.MaxPower = 90;
        this.SelfPowered = false;
        this.RemoteWakeup = false;
        this.PullDownEnable = false;
    }
}
