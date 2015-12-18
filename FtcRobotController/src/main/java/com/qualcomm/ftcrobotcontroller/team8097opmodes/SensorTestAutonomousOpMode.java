/* Copyright (c) 2014 Qualcomm Technologies Inc

All rights reserved.

Redistribution and use in source and binary forms, with or without modification,
are permitted (subject to the limitations in the disclaimer below) provided that
the following conditions are met:

Redistributions of source code must retain the above copyright notice, this list
of conditions and the following disclaimer.

Redistributions in binary form must reproduce the above copyright notice, this
list of conditions and the following disclaimer in the documentation and/or
other materials provided with the distribution.

Neither the name of Qualcomm Technologies Inc nor the names of its contributors
may be used to endorse or promote products derived from this software without
specific prior written permission.

NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
"AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. */

package com.qualcomm.ftcrobotcontroller.team8097opmodes;

//used for testing sensors
public class SensorTestAutonomousOpMode extends BaseOpMode {

    @Override
    public void init() {
        frontLightSensor = hardwareMap.lightSensor.get("frontLight");
        backLightSensor = hardwareMap.lightSensor.get("backLight");
        colorLightSensor = hardwareMap.lightSensor.get("colorLight");
        frontOds = hardwareMap.opticalDistanceSensor.get("frontOds");
        frontUltra = hardwareMap.ultrasonicSensor.get("frontUltra");
        rightUltra = hardwareMap.ultrasonicSensor.get("rightUltra");
        backUltra = hardwareMap.ultrasonicSensor.get("backUltra");
        leftUltra = hardwareMap.ultrasonicSensor.get("leftUltra");
    }

    @Override
    public void loop() {
        colorLightSensor.enableLed(false);
        telemetry.addData("Color Light", colorLightSensor.getLightDetected());
        frontLightSensor.enableLed(true);
        telemetry.addData("Front Light", frontLightSensor.getLightDetected());
        backLightSensor.enableLed(true);
        telemetry.addData("Back Light", backLightSensor.getLightDetected());
        telemetry.addData("frontUltra", frontUltra.getUltrasonicLevel());
        telemetry.addData("rightUltra", rightUltra.getUltrasonicLevel());
        telemetry.addData("backUltra", backUltra.getUltrasonicLevel());
        telemetry.addData("leftUltra", leftUltra.getUltrasonicLevel());
        telemetry.addData("frontOds", frontOds.getLightDetected());
    }

}
