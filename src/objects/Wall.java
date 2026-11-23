package objects;

import pt.iscte.poo.game.Room;
import pt.iscte.poo.utils.Point2D;

public class Wall extends ImmovableObjects {

	public Wall(Room room, Point2D position) {
		super(position, room);
	}

	@Override
	public String getName() {
		return "wall";
	}	

	@Override
	public int getLayer() {
		return 1;
	}
}
