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

//If we are on the blue alliance, the beacon repair zone will be on the right, and we will want to press the blue button.
public class BlueAutonomousOpMode extends CompetitionAutonomousOpMode {

    @Override
    protected double goDirectionOfOtherWall(double power, double inches, long startTime) {
        return goDistanceDiagLeft(power, inches);
    }

    @Override
    protected void moveCorrectButtonFlap() {
        if (rightLightDetected < leftLightDetected) {
            telemetry.addData("Blue on right. pressing right button (blue)", "");
            moveRightFlap();
        } else {
            telemetry.addData("Red on right. pressing left button (blue)", "");
            moveLeftFlap();
        }
    }

    protected void lookForTape() {
        if ((frontLightSensor.getLightDetected() > frontTapeLowThreshold && frontLightSensor.getLightDetected() < frontTapeHighThreshold) || (backLightSensor.getLightDetected() > backTapeLowThreshold && backLightSensor.getLightDetected() < backTapeHighThreshold)) {
            if (seesTape < 2) {
                seesTape++;
                telemetry.addData("seesTape", seesTape);
                goRight(DEFAULT_POWER);
            } else {
                if (frontLightSensor.getLightDetected() > frontTapeLowThreshold && frontLightSensor.getLightDetected() < frontTapeHighThreshold) {
                    frontTape = true;
                }
                if (backLightSensor.getLightDetected() > backTapeLowThreshold && backLightSensor.getLightDetected() < backTapeHighThreshold) {
                    backTape = true;
                }
                telemetry.addData("Found tape on the right", "");
                seesTape = 0;
                endStage();
            }
        } else {
            seesTape = 0;
            goRight(DEFAULT_POWER);
        }
    }

    @Override
    protected void alignWithTape() {
        if (!frontTape) {
            if (frontLightSensor.getLightDetected() > frontTapeLowThreshold && frontLightSensor.getLightDetected() < frontTapeHighThreshold) {
                if (seesTape < 2) {
                    seesTape++;
                    telemetry.addData("seesTape (front)", seesTape);
                    frontWheelsRight(DEFAULT_POWER);
                } else {
                    telemetry.addData("Found tape on the right (front)", "");
                    endStage();
                }
            } else {
                seesTape = 0;
                frontWheelsRight(DEFAULT_POWER);
            }
        } else if (!backTape) {
            if (backLightSensor.getLightDetected() > backTapeLowThreshold && backLightSensor.getLightDetected() < backTapeHighThreshold) {
                if (seesTape < 2) {
                    seesTape++;
                    telemetry.addData("seesTape (back)", seesTape);
                    backWheelsRight(DEFAULT_POWER);
                } else {
                    telemetry.addData("Found tape on the right (back)", "");
                    endStage();
                }
            } else {
                seesTape = 0;
                backWheelsRight(DEFAULT_POWER);
            }
        } else {
            endStage();
        }
    }

    @Override
    protected void alignWithTapePerfect() {
        if (sensorInputs < 6) {
            stopRobot();
            if (frontLightSensor.getLightDetected() > frontTapeLowThreshold && frontLightSensor.getLightDetected() < frontTapeHighThreshold) {
                seesTapeFront++;
            } else if (seesTapeFront < 3) {
                seesTapeFront = 0;
            }
            if (backLightSensor.getLightDetected() > backTapeLowThreshold && backLightSensor.getLightDetected() < backTapeHighThreshold) {
                seesTapeBack++;
            } else if (seesTapeBack < 3) {
                seesTapeBack = 0;
            }
            sensorInputs++;
            startMoveTime = System.currentTimeMillis();
        } else {
            if (seesTapeFront < 3 && seesTapeBack < 3) {
                double distanceToGo = goDistanceLeft(DEFAULT_POWER, 2 * INCHES_PER_CENT);
                if (distanceToGo == 0) {
                    sensorInputs = 0;
                }
            } else if (seesTapeFront < 3) {
                double distanceToGo = goDistanceFrontWheelsLeft(DEFAULT_POWER, 2 * INCHES_PER_CENT);
                if (distanceToGo == 0) {
                    sensorInputs = 0;
                }
            } else if (seesTapeBack < 3) {
                double distanceToGo = goDistanceBackWheelsLeft(DEFAULT_POWER, 2 * INCHES_PER_CENT);
                if (distanceToGo == 0) {
                    sensorInputs = 0;
                }
            } else {
                seesTapeFront = 0;
                seesTapeBack = 0;
                endStage();
            }
        }
    }

    @Override
    protected void moveIntoFloorGoal() {
        goRight(DEFAULT_POWER);
    }
}