package objects;

import pt.iscte.poo.game.Room;
import pt.iscte.poo.utils.Point2D;

public class Debris extends GameObject {
    private int ticksRemaining;

    public Debris(Room room, Point2D position, int durationTicks) {
        super(position, room);
        this.ticksRemaining = durationTicks;
    }

    @Override
    public String getName() {
        return "debris";  // nome da imagem em images/debris.png
    }

    @Override
    public int getLayer() {
        return 1;  // desenha por baixo
    }

    public void tick() {
        ticksRemaining--;
        if (ticksRemaining <= 0) {
            getRoom().removeObject(this);
        }
    }
}
