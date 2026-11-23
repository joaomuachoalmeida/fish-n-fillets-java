package objects;

import java.util.ArrayList;

import pt.iscte.poo.game.GameEngine;
import pt.iscte.poo.game.Room;
import pt.iscte.poo.gui.ImageGUI;
import pt.iscte.poo.utils.Direction;
import pt.iscte.poo.utils.Point2D;
import pt.iscte.poo.utils.Vector2D;

public class Bomb extends MovebleObjects {

	private boolean hasFallen=false;
    
    public Bomb(Room room, Point2D position) { 
        super(position, room);
    }

    @Override
    public String getName() {
        return "bomb";
    }
    
    public boolean hasFallen() {
    	return hasFallen;
    }
    public void setmarkFallen(boolean hasFallen) {
    	this.hasFallen=hasFallen;
    }
    
    public void explodeAt(Point2D center) {						//funçao para explodir numa determinada posiçao
    	Room room = getRoom();
    	
    	Point2D[] adj= new Point2D[]{
    			center,
    			center.plus(Direction.UP.asVector()),				//posiçoes para arrebentar
    			center.plus(Direction.LEFT.asVector()),
    			center.plus(Direction.RIGHT.asVector()),
    			center.plus(Direction.DOWN.asVector())
    	};
    	
    	for (Point2D p: adj) {
    		for(GameObject obj: new ArrayList<>(room.getObjects())) {
    			if(obj.getPosition()!=null && obj.getPosition().equals(p)) {
    				if(obj instanceof GameCharacter) {
    					ImageGUI.getInstance().showMessage("Morte", obj.getClass().getSimpleName() + "Explodiu");      //arrebentar o peixe e reiniciar o nivel
    					GameEngine.getInstance().restartLevel();
    				}
    				if(obj instanceof Water || obj instanceof IndestrutivelWall) {				//nao arrebentar a agua nem as paredes indestrutiveis (bordas do mapa)
    					continue;
    				}
    				room.removeObject(obj);							//remover o resto dos objetos
    				room.addObject(new Debris(room, p, 2));					//adicionar os pedaços de objetos arrebentados duranre 2 ticks
    			}
    			
    		}
    	}
    }

    @Override
    public int getLayer() {
        return 1;
    }

	@Override
	public void gravity(){

		Vector2D down = Direction.DOWN.asVector();
		Point2D nextPosition = getPosition().plus(down);
		GameObject objAbaixo = getObjectAt(nextPosition);

			if(objAbaixo instanceof GameCharacter) {					//caso a bomba caia em cima de um dos peixes e seja empurrada na horizontal nao exploda automaticamente
				setmarkFallen(false);
				return;
			}
			if(objAbaixo==null) {					//caso esteja em queda meter o markFallen como true para quando chegar ao chão arrebentar
				setmarkFallen(true);
				setPosition(nextPosition);
				return;
			}
			if(!hasFallen()) {
				return;
			}else {
				explodeAt(getPosition());				//quando entra em contacto com qualque dos objetos sem ser os peixes e se ja tiver caido arrebenta

			}

		}
}