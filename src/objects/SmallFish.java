package objects;

import pt.iscte.poo.game.GameEngine;
import pt.iscte.poo.game.Room;
import pt.iscte.poo.gui.ImageGUI;
import pt.iscte.poo.utils.Point2D;
import pt.iscte.poo.utils.Vector2D;

public class SmallFish extends GameCharacter {

	private static SmallFish sf = new SmallFish(null);
	private String currentDirection = "Left";  // começa virado para esquerda
	
	private SmallFish(Room room) {
		super(room);
	}

	public static SmallFish getInstance() {
		return sf;
	}
	
	@Override
	public String getName() {
		return "smallFish" + currentDirection;
	}

	@Override
	public int getLayer() {
		return 1;
	}

	@Override
	public boolean isCrushedBy(int light, int heavy) {
		return heavy >= 1 || light > 1;   // morre com um pesado, ou com mais do que um leve
	}

	@Override
	public void move(Vector2D dir) {
		 Point2D currentPosition = getPosition();
		    Point2D destination = currentPosition.plus(dir);
		    GameObject target = getObjectAt(destination);
		    
		    if (dir.getX() > 0) {
	            currentDirection = "Right";						//muda a direçao consoante o input
	        } else if (dir.getX() < 0) {
	            currentDirection = "Left";
	        }
		    
		    if (target ==null || target instanceof HoledWall || target instanceof Trap) {
		        setPosition(destination);															//smallfish pode atravessar a parede com buraco e a trap
		        triggerKrabMove();
		        GameEngine.getInstance().moves++;
		        return;
		    } 
		    if(target instanceof Krab) {
		    	ImageGUI.getInstance().showMessage("Morte", "SmallFish morreu picado pelo Krab");			//morre caso va contra o krab
		    	GameEngine.getInstance().restartLevel();
		    }
		    
		    if (target instanceof MovebleObjects && !((MovebleObjects) target).isHeavy()) {					//tenta empurrar caso o objeto nao seja pesado
		    	if(pushObj(destination,target, dir)) {
		    		setPosition(destination);
			        GameEngine.getInstance().moves++;
		    		triggerKrabMove();
		    	}
		    	
		    }
	}
}
