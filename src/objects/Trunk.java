package objects;

import pt.iscte.poo.game.Room;
import pt.iscte.poo.utils.Point2D;

public class Trunk extends ImmovableObjects{

private static Trunk Y = new Trunk(null, null);

    
    public Trunk(Room room, Point2D position) { 
        super(position, room);
    }
    

    @Override
    public String getName() {
        return "trunk";
    }

    @Override
    public int getLayer() {
        return 2;
    }
}
