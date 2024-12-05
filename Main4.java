// Main 4

package TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.Blinker;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.Gyroscope;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;
import java.text.SimpleDateFormat;
import java.util.Date;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp
public class Main4 extends OpMode {

    private DcMotor FLMOTOR;
    private DcMotor FRMOTOR;
    private DcMotor BLMOTOR;
    private DcMotor BRMOTOR;
    
    private DcMotor ARMMOTOR;
    private DcMotor EXTENDMOTOR;
    private Servo GRABSERVO;

    private Blinker control_Hub;
    private Blinker expansion_Hub_2;
    private Gyroscope imu_1;
    private IMU imu;

    @Override
    public void init() {

        FLMOTOR = hardwareMap.get(DcMotor.class, "FLMOTOR");
        FRMOTOR = hardwareMap.get(DcMotor.class, "FRMOTOR");
        BLMOTOR = hardwareMap.get(DcMotor.class, "BLMOTOR");
        BRMOTOR = hardwareMap.get(DcMotor.class, "BRMOTOR");
        
        ARMMOTOR = hardwareMap.get(DcMotor.class, "ARMMOTOR");
        EXTENDMOTOR = hardwareMap.get(DcMotor.class, "EXTENDMOTOR");
        GRABSERVO = hardwareMap.get(Servo.class, "GRABSERVO");
        
        control_Hub = hardwareMap.get(Blinker.class, "Control Hub");
        expansion_Hub_2 = hardwareMap.get(Blinker.class, "Expansion Hub 2");
        imu_1 = hardwareMap.get(Gyroscope.class, "imu 1");
        imu = hardwareMap.get(IMU.class, "imu");
        
        FRMOTOR.setDirection(DcMotor.Direction.REVERSE);
        BRMOTOR.setDirection(DcMotor.Direction.REVERSE);
        EXTENDMOTOR.setDirection(DcMotor.Direction.REVERSE);
        ARMMOTOR.setDirection(DcMotor.Direction.REVERSE);
        
        ARMMOTOR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        EXTENDMOTOR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        ARMMOTOR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        EXTENDMOTOR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);


        telemetry.addData("Status", "Initialized");
        telemetry.update();
    }


    @Override
    public void init_loop() {
    
    }


    @Override
    public void start() {

        telemetry.addData("Status", "Started");
        telemetry.update();

        GRABSERVO.setPosition(0);
    }


    @Override
    public void loop() {
        
    
        // Driving 

        double vertical = -gamepad1.left_stick_y;
        double horizontal = gamepad1.left_stick_x;
        double pivot = gamepad1.right_stick_x;

        if (gamepad1.right_trigger > 0.1) {

            FLMOTOR.setPower(vertical + horizontal + pivot);
            FRMOTOR.setPower(vertical - horizontal - pivot);
            BLMOTOR.setPower(vertical - horizontal + pivot);
            BRMOTOR.setPower(vertical + horizontal - pivot);

        } else if (gamepad1.left_trigger > 0.1) {

            FLMOTOR.setPower((vertical + horizontal + pivot) / 5);
            FRMOTOR.setPower((vertical - horizontal - pivot) / 5);
            BLMOTOR.setPower((vertical - horizontal + pivot) / 5);
            BRMOTOR.setPower((vertical + horizontal - pivot) / 5);
  
  
        } else {

            FLMOTOR.setPower((vertical + horizontal + pivot) / 2);
            FRMOTOR.setPower((vertical - horizontal - pivot) / 2);
            BLMOTOR.setPower((vertical - horizontal + pivot) / 2);
            BRMOTOR.setPower((vertical + horizontal - pivot) / 2);
        }


        // Arm Up / Down

        if (gamepad2.right_stick_y < 0) {     
    
            encoderMovement(ARMMOTOR, 2450, -gamepad2.right_stick_y); 
        
        } else if (gamepad2.right_stick_y > 0) {
            
            encoderMovement(ARMMOTOR, 50, gamepad2.right_stick_y / 2); 
        
        } else {

            encoderMovement(ARMMOTOR, ARMMOTOR.getCurrentPosition(), 1); 
        }


        // Arm Out / In

        if (gamepad2.right_trigger > 0) {     
            
            encoderMovement(EXTENDMOTOR, 2000, gamepad2.right_trigger);

        } else if (gamepad2.left_trigger > 0) {

            encoderMovement(EXTENDMOTOR, 0, gamepad2.left_trigger);

        } else {

            encoderMovement(EXTENDMOTOR, EXTENDMOTOR.getCurrentPosition(), 1); 
        }


        // Claw Grab / Release

        if (gamepad2.dpad_down) {
                
            GRABSERVO.setPosition(0);

        } else if (gamepad2.dpad_up) {
    
            GRABSERVO.setPosition(0.5);
        }




        telemetry.addData("Status", "Running");
        telemetry.update();

    }


    @Override
    public void stop() {

        telemetry.addData("Status", "Stopped");
        telemetry.update();
    }

    private void encoderMovement(DcMotor motor, int target, double power) {

        motor.setTargetPosition(target);    
        motor.setMode(DcMotor.RunMode.RUN_TO_POSITION); 
        motor.setPower(power);
    }
}



// encoderMovement(motor, target, power);
