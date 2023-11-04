package org.firstinspires.ftc.teamcode.Payload;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.PoseVelocity2d;
import com.acmerobotics.roadrunner.Vector2d;

public final class HighLevel {
    private final Payload payload;

    private final Payload.GameState gameState;

    private final boolean disabled;

    HighLevel(Payload payload){
        this.payload = payload;
        disabled = payload.drive == null;
        gameState = payload.gameState;
    }

    public Action align(Camera.AlignmentController controller){
        return telemetryPacket -> {
            if(disabled)return false;

            PoseVelocity2d errors = controller.obtainErrors();
            switch (controller.getState()){
                case ACTIVE:
                    payload.drive.setDrivePowers(errors);
                    return true;
                case LOST_TARGET:
                case WITHIN_TOLERANCE:
                default: // << why is this case necessary
                    payload.drive.setDrivePowers(new PoseVelocity2d
                            (new Vector2d(0, 0), 0));
                    return false;
            }
        };
    }

    public Camera.AlignmentController alignWithTag(int tag, PixelArm.Gripper.Side side){
        return payload.camera.alignmentController(
                tag, new Vector2d(6, side.offset), 1);
    }

    public Action alignWithBackboard(){
        int id = 0;

        switch (gameState.teamColor){
            case BLUE: id += 0;break;
            case RED: id += 3;break;
        }

        switch (gameState.signalState){
            case LEFT: id += 0;break;
            case MIDDLE: id += 1;break;
            case RIGHT: id += 2;break;
        }

        return align(alignWithTag(id, PixelArm.Gripper.Side.A));
    }
}
