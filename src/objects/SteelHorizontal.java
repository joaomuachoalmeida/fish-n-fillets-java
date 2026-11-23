package objects;

import pt.iscte.poo.game.Room;
import pt.iscte.poo.utils.Point2D;

public class SteelHorizontal extends ImmovableObjects {

	public SteelHorizontal(Room room, Point2D position) {
		super(position, room);
	}

	@Override
	public String getName() {
		return "steelHorizontal";
	}

	@Override
	public int getLayer() {
		return 1;
	}


}
