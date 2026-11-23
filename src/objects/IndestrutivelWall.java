package objects;

import pt.iscte.poo.game.Room;
import pt.iscte.poo.utils.Point2D;

public class IndestrutivelWall extends ImmovableObjects {

	public IndestrutivelWall(Room room, Point2D position) {
		super(position, room);
	}

	@Override
	public String getName() {
		return "wall";					// wallReinforced
	}									//podemos alterar a imagem das paredes reforçadas

	@Override
	public int getLayer() {
		return 1;
	}
}
