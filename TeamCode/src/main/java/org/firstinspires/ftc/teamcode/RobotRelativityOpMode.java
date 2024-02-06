package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp
public class RobotRelativityOpMode extends OpMode {
    DcMotor frontRight;
    DcMotor frontLeft;
    DcMotor backLeft;
    DcMotor backRight;
    DcMotor armlift;
    DcMotor armmain;

    boolean armUp = false;

    @Override
    public void init() {
        frontRight = hardwareMap.dcMotor.get("frontRight");
        frontLeft = hardwareMap.dcMotor.get("frontLeft");
        backLeft = hardwareMap.dcMotor.get("backLeft");
        backRight = hardwareMap.dcMotor.get("backRight");
        armlift = hardwareMap.dcMotor.get("armlift");
        armmain = hardwareMap.dcMotor.get("armmain");

        // Reset the motor encoder so that it reads zero ticks
        armlift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);


        // Turn the motor back on, required if you use STOP_AND_RESET_ENCODER
        armlift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        // frontRight 0
        // frontLeft 1
        // backRight 3
        // backLeft 2
    }

    @Override
    public void loop() {
        double powerY = gamepad1.left_stick_y;
        double powerX = -gamepad1.left_stick_x;
        double turnPower = gamepad1.right_stick_x;

        double powerFR = (powerY - powerX) / 1.5 + turnPower * 0.8;
        double powerFL = (powerY + powerX) / 1.5 - turnPower * 0.8;
        double powerBR = (powerY + powerX) / 1.5 + turnPower * 0.8;
        double powerBL = (powerY - powerX) / 1.5 - turnPower * 0.8;

        frontRight.setPower(-powerFR);
        frontLeft.setPower(-powerFL);
        backRight.setPower(-powerBR);
        backLeft.setPower(-powerBL);

        // Get the current position of the arm lift
        int position = armlift.getCurrentPosition();
        // Show the position of the motor on telemetry
        telemetry.addData("arm ticks: ", position);
        telemetry.update();
        // Telemetry for Left Trigger

// arm up is 488
// down is 0

        if(gamepad1.left_bumper) {
            int armDownPosition = -488;// The position (in ticks) that you want the motor to move to
            armlift.setTargetPosition(armDownPosition);// Tells the motor that the position it should go to is armDownPosition
            armlift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            armlift.setPower(-1);
        }
        // Arm Down, Right Button
        if(gamepad1.right_bumper) {
            int armUpPosition = 0;
            armlift.setTargetPosition(armUpPosition);
            armlift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            armlift.setPower(1);
            armlift.setPower(0);
        }
    }
}