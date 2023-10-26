package org.firstinspires.ftc.teamcode;

import static com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior.BRAKE;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;

@TeleOp
public class RoByteOpMode extends OpMode {
    DcMotor frontright;
    DcMotor frontleft;
    DcMotor armlift;
    DcMotor armmain;

    @Override
    public void init() {
        frontright = hardwareMap.dcMotor.get("frontright");
        frontleft = hardwareMap.dcMotor.get("frontleft");
        armlift = hardwareMap.dcMotor.get("armlift");
        armmain = hardwareMap.dcMotor.get("armmain");
    }

    @Override
    public void loop() {
        double powerY = gamepad1.left_stick_y;
        double powerX = -gamepad1.left_stick_x;
        double turnPower = gamepad1.right_stick_x;

        double powerFR = (powerY - powerX) / 1.5 + turnPower / 2;
        double powerFL = (powerY + powerX) / 1.5 - turnPower / 2;

        // Arm Down, Left Trigger
        double lefttriggerValue = gamepad1.left_trigger;
        double armdownmotorPower = -lefttriggerValue * 0.7;
        armlift.setPower(armdownmotorPower);

        // Arm Up, Left Bumper
        boolean leftbumperValue = gamepad1.left_bumper;
        double armupmotorPower = leftbumperValue ? 0.6 : 0;
        armlift.setPower(armupmotorPower);

        frontright.setPower(powerFR);
        frontleft.setPower(powerFL);
    }
}