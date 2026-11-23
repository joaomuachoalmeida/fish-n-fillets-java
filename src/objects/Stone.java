package objects;

import pt.iscte.poo.game.Room;
import pt.iscte.poo.utils.Point2D;

public class Stone extends MovebleObjects{
	
	private boolean hasHiddenKrab =false;						//variavel para determinar se tem um krab ou nao
	
	public Stone(Room room, Point2D position) {
		super(position, room); 
		hasHiddenKrab=false;
	}
	public void setHasHiddenKrab(boolean hasHiddenKrab) {		
		this.hasHiddenKrab= hasHiddenKrab;
	}
	
	public boolean hasHiddenKrab() {
		return hasHiddenKrab;
	}

	@Override
	public String getName() {
		return "stone";
	}

	@Override
	public int getLayer() {
		return 1;
	}
	
	@Override
	public boolean isHeavy() {
		return true;
	}

}
