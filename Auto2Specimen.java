// Auto 2

package Auto;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotorEx;
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

@Autonomous
public class Auto2Specimen extends OpMode {

    private DcMotor FLMOTOR;
    private DcMotor FRMOTOR;
    private DcMotor BLMOTOR;
    private DcMotor BRMOTOR;
    
    private DcMotorEx ARMMOTOR;
    private DcMotor EXTENDMOTOR;
    private Servo GRABSERVO;

    private Blinker control_Hub;
    private Blinker expansion_Hub_2;
    private Gyroscope imu_1;
    private IMU imu;
    private int armHoldPos;
    //private double rightStickPos = -gamepad2.right_stick_y;

    @Override
    public void init() {

        FLMOTOR = hardwareMap.get(DcMotor.class, "FLMOTOR");
        FRMOTOR = hardwareMap.get(DcMotor.class, "FRMOTOR");
        BLMOTOR = hardwareMap.get(DcMotor.class, "BLMOTOR");
        BRMOTOR = hardwareMap.get(DcMotor.class, "BRMOTOR");
        
        ARMMOTOR = hardwareMap.get(DcMotorEx.class, "ARMMOTOR");
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
        
        driveforward(3);
        armlift();
        armextend();
        driveforwardshort(1);
        clawopen();
        try{
            Thread.sleep((500));
        }catch (InterruptedException e){
             // Thread.idfc();
        }
        driveback(1);
        armretract();
        armdrop();
    }


    @Override
    public void loop() {
        
    }
    public void armretract(){
        while(EXTENDMOTOR.getCurrentPosition() < 20){
        encoderMovement(EXTENDMOTOR, 0, -1);
        }
    }
    public void armdrop(){
        while(ARMMOTOR.getCurrentPosition() > 20){
        ARMMOTOR.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        ARMMOTOR.setPower(-1);
        armHoldPos = ARMMOTOR.getCurrentPosition();
        }
        encoderMovement(ARMMOTOR, armHoldPos, 0.75);
    }
    public void driveback(int time){
        FLMOTOR.setPower(-0.22);
        FRMOTOR.setPower(-0.25);
        BLMOTOR.setPower(-0.22);
        BRMOTOR.setPower(-0.25);
        try{
            Thread.sleep((time * 1000));
        }catch (InterruptedException e){
             // Thread.idfc();
        }
        stop();
    }
    public void clawopen(){
        GRABSERVO.setPosition(0);
    }
    public void armlift(){
        while(ARMMOTOR.getCurrentPosition() != 2450){
            encoderMovement(ARMMOTOR, 2450, 0.75);
            armHoldPos = ARMMOTOR.getCurrentPosition();
        }
        encoderMovement(ARMMOTOR, armHoldPos, 0.75);
    }
    public void armextend(){
        while(EXTENDMOTOR.getCurrentPosition() < 2000){
        encoderMovement(EXTENDMOTOR, 2000, 1);
        }
    }
    
    public void driveforward(int time){
            
            FLMOTOR.setPower(0.22);
            FRMOTOR.setPower(0.25);
            BLMOTOR.setPower(0.22);
            BRMOTOR.setPower(0.25);
            
            try{
                Thread.sleep(time * 1000);
            }catch (InterruptedException e){
                // Thread.idfc();
            }
            stop();
        
    }
    public void driveforwardshort(int time){
            
            FLMOTOR.setPower(0.22);
            FRMOTOR.setPower(0.25);
            BLMOTOR.setPower(0.22);
            BRMOTOR.setPower(0.25);
            
            try{
                Thread.sleep(time * 500);
            }catch (InterruptedException e){
                // Thread.idfc();
            }
            stop();
        
    }
    

    @Override
    public void stop() {

        telemetry.addData("Status", "Stopped");
        telemetry.update();
        FLMOTOR.setPower(0);
        FRMOTOR.setPower(0);
        BLMOTOR.setPower(0);
        BRMOTOR.setPower(0);
    }

    private void encoderMovement(DcMotor motor, int target, double power) {

        motor.setTargetPosition(target);    
        motor.setMode(DcMotor.RunMode.RUN_TO_POSITION); 
        motor.setPower(power);
    }
}



// encoderMovement(motor, target, power);
