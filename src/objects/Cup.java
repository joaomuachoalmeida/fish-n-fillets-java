package objects;

import pt.iscte.poo.game.Room;
import pt.iscte.poo.utils.Point2D;

public class Cup extends MovebleObjects {

    public Cup(Room room, Point2D position) { 
        super(position, room);
    }

    @Override
    public String getName() {
        return "cup";
    }

    @Override
    public int getLayer() {
        return 1;
    }

    @Override
    protected boolean canFallInto(GameObject below){
        return below==null || below instanceof HoledWall;
    }
 
}