package objects;

import pt.iscte.poo.game.GameEngine;
import pt.iscte.poo.game.Room;
import pt.iscte.poo.gui.ImageGUI;
import pt.iscte.poo.utils.Direction;
import pt.iscte.poo.utils.Point2D;
import pt.iscte.poo.utils.Vector2D;

public class Trap extends MovebleObjects {

    public Trap(Room room, Point2D position) { 
        super(position, room);
    }

    @Override
    public String getName() {
        return "trap";
    }

    @Override
    public int getLayer() {
        return 0;
    }

    @Override
    protected boolean canFallInto(GameObject below) {
        return below ==null || below instanceof GameCharacter;
    }

    @Override
    public void gravity(){
        Vector2D down = Direction.DOWN.asVector();
        Point2D nextPosition = getPosition().plus(down);
        GameObject objAbaixo = getObjectAt(nextPosition);

        if(objAbaixo instanceof BigFish) {							//matar bigfish caso trap caia em cima dele
            ImageGUI.getInstance().showMessage("Morte", "BigFIsh morreu atrapado");
            GameEngine.getInstance().restartLevel();
            return;
        }
        if(objAbaixo instanceof Krab) {
            getRoom().removeObject(objAbaixo);
            return;
        }
        super.gravity();
    }
 

}
