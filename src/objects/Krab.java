package objects;


import pt.iscte.poo.game.GameEngine;
import pt.iscte.poo.game.Room;
import pt.iscte.poo.gui.ImageGUI;
import pt.iscte.poo.utils.Direction;
import pt.iscte.poo.utils.Point2D;
import pt.iscte.poo.utils.Vector2D;

public class Krab extends MovebleObjects {

	public Krab(Room room, Point2D position) {
		super(position, room); 
	}

	@Override
	public String getName() {
		return "krab";
	}

	@Override
	public int getLayer() {
		return 1;
	}

	@Override
	protected boolean canFallInto(GameObject below){
		return below ==null || below instanceof HoledWall;
	}

	@Override
	public void gravity() {

		Vector2D down = Direction.DOWN.asVector();
		Point2D nextPosition = getPosition().plus(down);
		GameObject objAbaixo = getObjectAt(nextPosition);

		if (objAbaixo instanceof Trap) {
			getRoom().removeObject(this);
			return;
		}
		if(objAbaixo instanceof SmallFish) {								//krab matar o smallfish quando lhe cai em cima
			ImageGUI.getInstance().showMessage("Morte", "SmallFish foi picado pelo Krab");
			GameEngine.getInstance().restartLevel();
			return;
		}

		super.gravity();
	}
	
	
	public void autoMove() {
		Direction horizontalDir = Math.random() < 0.5 ? Direction.LEFT : Direction.RIGHT;			// 50/50 de ir para um dos lados direita ou esquerda
		Vector2D dir = horizontalDir.asVector();												
		Point2D currentPosition = getPosition();
		Point2D destination = currentPosition.plus(dir);
		GameObject target = getObjectAt(destination);

		if (target == null) {
			setPosition(destination);							//mover o krab
			return;
		}

		if (target instanceof SmallFish ) {					//caso va contra o smallfish mata-o
			ImageGUI.getInstance().showMessage("Morte", "SmallFish Morreu beliscado pelo Krab!");
	        GameEngine.getInstance().restartLevel();
			return;
		}
		if(target instanceof BigFish) {					//caso va contra o bigfish, o krab morre
			getRoom().removeObject(this);
			return;

		}
		if(target instanceof Trap) {						//caso va contra a trap, o krab morre
			getRoom().removeObject(this);
		}

	}

}
