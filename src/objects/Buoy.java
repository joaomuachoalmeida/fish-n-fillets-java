package objects;

import pt.iscte.poo.game.Room;
import pt.iscte.poo.utils.Direction;
import pt.iscte.poo.utils.Point2D;

public class Buoy extends MovebleObjects {

    public Buoy(Room room, Point2D position) { 
        super(position, room);
    }

    @Override
    public String getName() {
        return "buoy";
    }
    
    @Override
    public int getLayer() {
        return 1;
    }

    @Override
    public void gravity(){
        Point2D nextPosition = getPosition().plus(Direction.UP.asVector());
        GameObject objAcima= getObjectAt(nextPosition);

        if(objAcima ==null) {
            setPosition(nextPosition);
            return;
        }

        if(objAcima instanceof GameCharacter || objAcima instanceof ImmovableObjects) {
            return;
        }else {
            super.gravity();									//caso tenho um objeto em cima aplica se a gravidade normal
        }



    }
 
}