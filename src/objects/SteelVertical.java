package objects;

import pt.iscte.poo.game.Room;
import pt.iscte.poo.utils.Point2D;

public class SteelVertical extends ImmovableObjects {

	public SteelVertical(Room room, Point2D position) {
		super(position, room);
	}

	@Override
	public String getName() {
		return "steelVertical";
	}

	@Override
	public int getLayer() {
		return 1;
	}


}
