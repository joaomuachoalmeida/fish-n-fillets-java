package objects;

import pt.iscte.poo.game.*;
import pt.iscte.poo.gui.ImageGUI;
import pt.iscte.poo.utils.Point2D;
import pt.iscte.poo.utils.Vector2D;

public class BigFish extends GameCharacter {

	private static BigFish bf = new BigFish(null);
	private String currentDirection = "Left";  // começa virado para esquerda

	
	private BigFish(Room room) {
		super(room);
	}

	public static BigFish getInstance() {
		return bf;
	}
	
	@Override
	public String getName() {
		return "bigFish" + currentDirection;
	}

	@Override
	public int getLayer() {
		return 1;
	}

	@Override
	public boolean isCrushedBy(int light, int heavy) {
		return heavy > 1 || (light >= 1 && heavy >= 1);
	}
	
	@Override
	public void move(Vector2D dir) {
		 Point2D currentPosition = getPosition();
		    Point2D destination = currentPosition.plus(dir);
		    GameObject target =getObjectAt(destination);
		    
		    if (dir.getX() > 0) {
	            currentDirection = "Right";
	        } else if (dir.getX() < 0) {					//mudar a direçao consoante o input
	            currentDirection = "Left";
	        }
		    
		    
		    if (target == null) {
		        setPosition(destination);
		        triggerKrabMove();						 //movimento para uma celula vazia, movimento do krab
		        GameEngine.getInstance().moves++;     
		        return;
		    }
		    
		    if(target instanceof Krab) {
		    	getRoom().removeObject(target);
		    	setPosition(destination);
		    	GameEngine.getInstance().moves++;				//matar o krab
		    	return;
		    }
		    if(target instanceof Trap) {
		    	ImageGUI.getInstance().showMessage("Morte", "BigFish Morreu!!");
		    	GameEngine.getInstance().restartLevel();							//morte caso entre numa trap
		    	return;
		    }

		    if (target instanceof MovebleObjects) {
		    	if (pushObj(destination, target, dir)) {
		            setPosition(destination);								//tentar empurrar objetos moveis
		            triggerKrabMove();
			        GameEngine.getInstance().moves++;
		            return;
		    	}
		    }
		    
		    return;
	}
}
