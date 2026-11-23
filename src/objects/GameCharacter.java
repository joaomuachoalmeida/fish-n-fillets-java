package objects;

import pt.iscte.poo.game.Room;
import pt.iscte.poo.utils.Direction;
import pt.iscte.poo.utils.Point2D;
import pt.iscte.poo.utils.Vector2D;

import java.util.ArrayList;
import java.util.List;

import pt.iscte.poo.game.*;

public abstract class GameCharacter extends GameObject {
	
	public GameCharacter(Room room) {
		super(room);
	}
	public abstract boolean isCrushedBy(int light, int heavy);
		
	public void move(Vector2D dir) {
	  
	}


	
	public boolean pushObj (Point2D destination, GameObject target, Vector2D dir) {
		Point2D objDest = destination.plus(dir);
    	
    	GameObject behind = getObjectAt(objDest);
    	
 //   	if(target instanceof Trap)										caso a trap nao seja para ser movida em cadeia basta adicionar esta condiçao
 //   		return false;
    	
    	if(target instanceof Anchor) {								
    		if(!((Anchor)target).hasMoved()) {											//empurrar a ancora caso ainda nao tivesse sido movida
    			if(behind==null) {
    				target.setPosition(objDest);
    				((Anchor)target).markMoved();										//marcar como movida
    				return true;
    			}
    			if(behind instanceof MovebleObjects) {
    				if(pushObj(objDest, behind, dir)) {									//caso a ancora seja a primeira da cadeia de objetos marcar como movida
    					target.setPosition(objDest);
    					((Anchor)target).markMoved();
    					return true;
    				}
    			}
    		}
    			return false;
    	}
    	
    	if(behind==null) {
    		if(GameEngine.getInstance().FishSelect() && dir.getY()!=0) {						//caso o smallfish tente empurrar na vertical
    			return false;
    		}
    		
    		  if (target instanceof Stone && ((Stone) target).hasHiddenKrab() && dir.getY() == 0) {					// se a pedra for movida na horizontal aparece um krab e mete a false o spawn de krabs para essa pedra
    		        SpawnKrab(target);
					((Stone) target).setHasHiddenKrab(false);

    		    }
    		target.setPosition(objDest);
    		return true;
    	}
    	if(behind instanceof MovebleObjects && !GameEngine.getInstance().FishSelect()) {			//se tentar empurrar em cadeia e for o peixe grande a jogar
    		if(dir.getY()==-1) {			//nao é possivel empurar mais do que um objeto verticalmente
    			return false;
    		}
    		if(pushObj(objDest, behind, dir)) {
    			if(target instanceof Stone) {						//dar spawn ao Krab caso a pedra esteja a ser empurrada em cadeia
    				SpawnKrab(target);
    			}
    			target.setPosition(objDest);
    			return true;
    		}   		
    	}
    	return false;
    	
	}
	
	
	public boolean SpawnKrab(GameObject obj) {
		
		Point2D KrabPos = obj.getPosition().plus(Direction.UP.asVector());						//posiçao onde o krab vai aparecer
		if (getObjectAt(KrabPos) == null && ((Stone)obj).hasHiddenKrab()) {						//se essa posiçao tiver vazia e a pedra tiver um krab ainda vai adicionar um krab
			Krab newKrab = new Krab(getRoom(), KrabPos);
            getRoom().addObject(newKrab);
            ((Stone) obj).setHasHiddenKrab(false);
           return true;
        	}
			return false;
	}
	
	public void triggerKrabMove() {
		Room currentRoom = getRoom();
		if (currentRoom != null) {
			
			List<GameObject> snapshot = new ArrayList<>(currentRoom.getObjects()); 				//lista de objetos copiados para poder remover (experiencia propria: se tentar remover da original jogo buga)
			
			for (GameObject obj : snapshot) {
				if (obj instanceof Krab) {									//fazer com que o krab se mova
					((Krab) obj).autoMove();
				}
			}
		}
	}
	
	@Override
	public int getLayer() {
		return 2;
	}
	
}