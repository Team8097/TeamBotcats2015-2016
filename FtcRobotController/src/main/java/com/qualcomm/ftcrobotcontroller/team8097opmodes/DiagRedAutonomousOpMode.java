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

//If we are on the red alliance, the beacon repair zone will be on the left.
public class DiagRedAutonomousOpMode extends CompetitionAutonomousOpMode {

    @Override
    protected void goToBeacon() {
        if (frontUltra.getUltrasonicLevel() > 30) {
            seesInFront = 0;
            stoppedForObstacle = false;
            distanceToGo.set(distanceToGoIndex, goDistanceDiagRight(DEFAULT_POWER, distanceToGo.get(distanceToGoIndex - 1), startMoveTime));
        } else if (seesInFront < 20) {
            seesInFront++;
            telemetry.addData("seesInFront", seesInFront);
            stoppedForObstacle = false;
            distanceToGo.set(distanceToGoIndex, goDistanceDiagRight(DEFAULT_POWER, distanceToGo.get(distanceToGoIndex - 1), startMoveTime));
        } else if (distanceToGo.get(distanceToGoIndex) < 36) {
            endStage();
        } else {
            if (!stoppedForObstacle) {
                stopRobot();
                stoppedForObstacle = true;
                distanceToGoIndex++;
                startStoppedTime = System.currentTimeMillis();
            }
            if (System.currentTimeMillis() - startStoppedTime > 5000) {
                endStage();
            }
            startMoveTime = System.currentTimeMillis();
        }
//        if (frontUltra.getUltrasonicLevel() > 20) {
//            seesInFront = 0;
//            goDiagRight(DEFAULT_POWER);
//        } else if (seesInFront < 3) {
//            seesInFront++;
//            goDiagRight(DEFAULT_POWER);
//        } else {
//            endStage();
//        }
    }

    @Override
    protected void moveCorrectButtonFlap() {
        if (blueLightDetected > BLUE_THRESHOLD) {
            telemetry.addData("Red on right. pressing right button (red)", "");
            moveRightFlap();
        } else {
            telemetry.addData("Blue on right. pressing left button (red)", "");
            moveLeftFlap();
        }
    }

    @Override
    protected void turnToButton() {
        endStage();
    }

    @Override
    protected void lookForTape() {
        if (frontLightSensor.getLightDetected() > TAPE_THRESHOLD || backLightSensor.getLightDetected() > TAPE_THRESHOLD) {
            if (seesTape < 2) {
                seesTape++;
                telemetry.addData("seesTape", seesTape);
                goLeft(DEFAULT_POWER / 2.0);
            } else {
                if (frontLightSensor.getLightDetected() > TAPE_THRESHOLD) {
                    frontTape = true;
                }
                if (backLightSensor.getLightDetected() > TAPE_THRESHOLD) {
                    backTape = true;
                }
                telemetry.addData("Found tape on the left", "");
                seesTape = 0;
                endStage();
            }
        } else {
            seesTape = 0;
            goLeft(DEFAULT_POWER / 2.0);
        }
    }

    @Override
    protected void alignWithTape() {
        if (!frontTape) {
            if (frontLightSensor.getLightDetected() > TAPE_THRESHOLD) {
                if (seesTape < 2) {
                    seesTape++;
                    telemetry.addData("seesTape (front)", seesTape);
                    frontWheelsLeft(DEFAULT_POWER / 2.0);
                } else {
                    telemetry.addData("Found tape on the left (front)", "");
                    endStage();
                }
            } else {
                seesTape = 0;
                frontWheelsLeft(DEFAULT_POWER / 2.0);
            }
        } else if (!backTape) {
            if (backLightSensor.getLightDetected() > TAPE_THRESHOLD) {
                if (seesTape < 2) {
                    seesTape++;
                    telemetry.addData("seesTape (back)", seesTape);
                    backWheelsLeft(DEFAULT_POWER / 2.0);
                } else {
                    telemetry.addData("Found tape on the left (back)", "");
                    endStage();
                }
            } else {
                seesTape = 0;
                backWheelsLeft(DEFAULT_POWER / 2.0);
            }
        }
    }
}