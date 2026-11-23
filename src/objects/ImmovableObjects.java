package objects;

import pt.iscte.poo.game.Room;
import pt.iscte.poo.utils.Point2D;

public abstract class ImmovableObjects extends GameObject {

	public ImmovableObjects(Point2D position,Room room) {
		super(position,room);
	}
	

}
