package objects;

import pt.iscte.poo.game.Room;
import pt.iscte.poo.utils.Point2D;

public class Anchor extends MovebleObjects{
	
	private boolean hasMoved =false;
	
	public Anchor(Room room, Point2D position) {
		super(position, room); 
	}

	@Override
	public String getName() {
		return "anchor";
	}

	@Override
	public int getLayer() {
		return 1;
	}
	
	@Override
	public boolean isHeavy() {
		return true;
	}
	
	public boolean hasMoved() {
		return hasMoved;
	}
	public void markMoved() {
		hasMoved=true;
	}

}
