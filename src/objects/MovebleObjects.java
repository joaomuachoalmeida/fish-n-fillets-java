package objects;

import pt.iscte.poo.game.Room;
import pt.iscte.poo.utils.*;


public abstract class MovebleObjects extends GameObject {

	public MovebleObjects(Point2D position,Room room) {
		super(position,room);
	}
	

		
	public void gravity() {					//gravidade normal
	    Vector2D down = Direction.DOWN.asVector();
	    Point2D nextPosition = getPosition().plus(down); 
	    GameObject objAbaixo = getObjectAt(nextPosition);

	    if((objAbaixo instanceof Trunk) && isHeavy()) {					//tronco ser partido se lhe cair um objeto pesado em cima
	    	getRoom().removeObject(objAbaixo);
	    	setPosition(nextPosition);
	    }

		if(canFallInto(objAbaixo)){
			setPosition(nextPosition);
		}
	}

	protected boolean canFallInto(GameObject below) {
		return below == null;   // por defeito: só cai se estiver vazio
	}

	
	public boolean isHeavy() {
		return false;
	}
}
