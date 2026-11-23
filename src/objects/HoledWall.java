package objects;

import pt.iscte.poo.game.Room;
import pt.iscte.poo.utils.Point2D;

public class HoledWall extends ImmovableObjects{

private static HoledWall X = new HoledWall(null, null);

    
    public HoledWall(Room room, Point2D position) { 
        super(position, room);
    }
    

    @Override
    public String getName() {
        return "holedWall";
    }

    @Override
    public int getLayer() {
        return 2;
    }
}
