// 
// Decompiled by Procyon v0.5.30
// 

package com.qualcomm.robotcore.hardware.configuration;

import com.qualcomm.robotcore.exception.RobotCoreException;
import java.io.FileOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.io.Writer;
import java.io.StringWriter;
import android.util.Xml;
import android.content.Context;
import java.util.ArrayList;
import java.util.HashSet;
import org.xmlpull.v1.XmlSerializer;

public class WriteXMLFileHandler
{
    private XmlSerializer a;
    private HashSet<String> b;
    private ArrayList<String> c;
    private String[] d;
    private int e;
    
    public WriteXMLFileHandler(final Context context) {
        this.b = new HashSet<String>();
        this.c = new ArrayList<String>();
        this.d = new String[] { "    ", "        ", "            " };
        this.e = 0;
        this.a = Xml.newSerializer();
    }
    
    public String writeXml(final ArrayList<ControllerConfiguration> deviceControllerConfigurations) {
        this.c = new ArrayList<String>();
        this.b = new HashSet<String>();
        final StringWriter output = new StringWriter();
        try {
            this.a.setOutput((Writer)output);
            this.a.startDocument("UTF-8", true);
            this.a.ignorableWhitespace("\n");
            this.a.startTag("", "Robot");
            this.a.ignorableWhitespace("\n");
            for (final ControllerConfiguration controllerConfiguration : deviceControllerConfigurations) {
                final String string = controllerConfiguration.getType().toString();
                if (string.equalsIgnoreCase(DeviceConfiguration.ConfigurationType.MOTOR_CONTROLLER.toString()) || string.equalsIgnoreCase(DeviceConfiguration.ConfigurationType.SERVO_CONTROLLER.toString())) {
                    this.a(controllerConfiguration, true);
                }
                if (string.equalsIgnoreCase(DeviceConfiguration.ConfigurationType.LEGACY_MODULE_CONTROLLER.toString())) {
                    this.b(controllerConfiguration);
                }
                if (string.equalsIgnoreCase(DeviceConfiguration.ConfigurationType.DEVICE_INTERFACE_MODULE.toString())) {
                    this.a(controllerConfiguration);
                }
            }
            this.a.endTag("", "Robot");
            this.a.ignorableWhitespace("\n");
            this.a.endDocument();
            return output.toString();
        }
        catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
    
    private void a(final String s) {
        if (s.equalsIgnoreCase("NO DEVICE ATTACHED")) {
            return;
        }
        if (this.b.contains(s)) {
            this.c.add(s);
        }
        else {
            this.b.add(s);
        }
    }
    
    private void a(final ControllerConfiguration controllerConfiguration) throws IOException {
        this.a.ignorableWhitespace(this.d[this.e]);
        this.a.startTag("", this.b(controllerConfiguration.getType().toString()));
        this.a(controllerConfiguration.getName());
        this.a.attribute("", "name", controllerConfiguration.getName());
        this.a.attribute("", "serialNumber", controllerConfiguration.getSerialNumber().toString());
        this.a.ignorableWhitespace("\n");
        ++this.e;
        final DeviceInterfaceModuleConfiguration deviceInterfaceModuleConfiguration = (DeviceInterfaceModuleConfiguration)controllerConfiguration;
        final Iterator iterator = ((ArrayList)deviceInterfaceModuleConfiguration.getPwmDevices()).iterator();
        while (iterator.hasNext()) {
            this.a((DeviceConfiguration)iterator.next());
        }
        final Iterator iterator2 = ((ArrayList)deviceInterfaceModuleConfiguration.getI2cDevices()).iterator();
        while (iterator2.hasNext()) {
            this.a((DeviceConfiguration)iterator2.next());
        }
        final Iterator iterator3 = ((ArrayList)deviceInterfaceModuleConfiguration.getAnalogInputDevices()).iterator();
        while (iterator3.hasNext()) {
            this.a((DeviceConfiguration)iterator3.next());
        }
        final Iterator iterator4 = ((ArrayList)deviceInterfaceModuleConfiguration.getDigitalDevices()).iterator();
        while (iterator4.hasNext()) {
            this.a((DeviceConfiguration)iterator4.next());
        }
        final Iterator iterator5 = ((ArrayList)deviceInterfaceModuleConfiguration.getAnalogOutputDevices()).iterator();
        while (iterator5.hasNext()) {
            this.a((DeviceConfiguration)iterator5.next());
        }
        --this.e;
        this.a.ignorableWhitespace(this.d[this.e]);
        this.a.endTag("", this.b(controllerConfiguration.getType().toString()));
        this.a.ignorableWhitespace("\n");
    }
    
    private void b(final ControllerConfiguration controllerConfiguration) throws IOException {
        this.a.ignorableWhitespace(this.d[this.e]);
        this.a.startTag("", this.b(controllerConfiguration.getType().toString()));
        this.a(controllerConfiguration.getName());
        this.a.attribute("", "name", controllerConfiguration.getName());
        this.a.attribute("", "serialNumber", controllerConfiguration.getSerialNumber().toString());
        this.a.ignorableWhitespace("\n");
        ++this.e;
        for (final DeviceConfiguration deviceConfiguration : controllerConfiguration.getDevices()) {
            final String string = deviceConfiguration.getType().toString();
            if (string.equalsIgnoreCase(DeviceConfiguration.ConfigurationType.MOTOR_CONTROLLER.toString()) || string.equalsIgnoreCase(DeviceConfiguration.ConfigurationType.SERVO_CONTROLLER.toString()) || string.equalsIgnoreCase(DeviceConfiguration.ConfigurationType.MATRIX_CONTROLLER.toString())) {
                this.a((ControllerConfiguration)deviceConfiguration, false);
            }
            else {
                if (!deviceConfiguration.isEnabled()) {
                    continue;
                }
                this.a(deviceConfiguration);
            }
        }
        --this.e;
        this.a.ignorableWhitespace(this.d[this.e]);
        this.a.endTag("", this.b(controllerConfiguration.getType().toString()));
        this.a.ignorableWhitespace("\n");
    }
    
    private void a(final ControllerConfiguration controllerConfiguration, final boolean b) throws IOException {
        this.a.ignorableWhitespace(this.d[this.e]);
        this.a.startTag("", this.b(controllerConfiguration.getType().toString()));
        this.a(controllerConfiguration.getName());
        this.a.attribute("", "name", controllerConfiguration.getName());
        if (b) {
            this.a.attribute("", "serialNumber", controllerConfiguration.getSerialNumber().toString());
        }
        else {
            this.a.attribute("", "port", String.valueOf(controllerConfiguration.getPort()));
        }
        this.a.ignorableWhitespace("\n");
        ++this.e;
        for (final DeviceConfiguration deviceConfiguration : controllerConfiguration.getDevices()) {
            if (deviceConfiguration.isEnabled()) {
                this.a(deviceConfiguration);
            }
        }
        if (controllerConfiguration.getType() == DeviceConfiguration.ConfigurationType.MATRIX_CONTROLLER) {
            for (final DeviceConfiguration deviceConfiguration2 : ((MatrixControllerConfiguration)controllerConfiguration).getMotors()) {
                if (deviceConfiguration2.isEnabled()) {
                    this.a(deviceConfiguration2);
                }
            }
            for (final DeviceConfiguration deviceConfiguration3 : ((MatrixControllerConfiguration)controllerConfiguration).getServos()) {
                if (deviceConfiguration3.isEnabled()) {
                    this.a(deviceConfiguration3);
                }
            }
        }
        --this.e;
        this.a.ignorableWhitespace(this.d[this.e]);
        this.a.endTag("", this.b(controllerConfiguration.getType().toString()));
        this.a.ignorableWhitespace("\n");
    }
    
    private void a(final DeviceConfiguration deviceConfiguration) {
        if (!deviceConfiguration.isEnabled()) {
            return;
        }
        try {
            this.a.ignorableWhitespace(this.d[this.e]);
            this.a.startTag("", this.b(deviceConfiguration.getType().toString()));
            this.a(deviceConfiguration.getName());
            this.a.attribute("", "name", deviceConfiguration.getName());
            this.a.attribute("", "port", String.valueOf(deviceConfiguration.getPort()));
            this.a.endTag("", this.b(deviceConfiguration.getType().toString()));
            this.a.ignorableWhitespace("\n");
        }
        catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
    
    public void writeToFile(final String data, final String folderName, String filename) throws RobotCoreException, IOException {
        if (this.c.size() > 0) {
            throw new IOException("Duplicate names: " + this.c);
        }
        filename = filename.replaceFirst("[.][^.]+$", "");
        final File file = new File(folderName);
        boolean mkdir = true;
        if (!file.exists()) {
            mkdir = file.mkdir();
        }
        if (mkdir) {
            final File file2 = new File(folderName + filename + ".xml");
            FileOutputStream fileOutputStream = null;
            try {
                fileOutputStream = new FileOutputStream(file2);
                fileOutputStream.write(data.getBytes());
            }
            catch (Exception ex) {
                ex.printStackTrace();
                try {
                    fileOutputStream.close();
                }
                catch (IOException ex2) {
                    ex2.printStackTrace();
                }
            }
            finally {
                try {
                    fileOutputStream.close();
                }
                catch (IOException ex3) {
                    ex3.printStackTrace();
                }
            }
            return;
        }
        throw new RobotCoreException("Unable to create directory");
    }
    
    private String b(final String s) {
        String s2 = s.substring(0, 1) + s.substring(1).toLowerCase();
        for (int i = s.lastIndexOf("_"); i > 0; i = s2.lastIndexOf("_")) {
            final int n = i + 1;
            s2 = s2.substring(0, i) + s2.substring(n, n + 1).toUpperCase() + s2.substring(n + 1);
        }
        return s2;
    }
}
