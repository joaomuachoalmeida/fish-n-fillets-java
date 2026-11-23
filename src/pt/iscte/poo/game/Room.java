package pt.iscte.poo.game;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import objects.*;
import objects.Water;
import objects.BigFish;
import objects.Cup;
import objects.GameObject;
import objects.SmallFish;
import objects.SteelHorizontal;
import objects.Wall;
import pt.iscte.poo.utils.Point2D;

public class Room {

	public static final int GRID_SIZE = 10;
	private List<GameObject> objects;
	private String roomName;
	private  GameEngine engine;
	private Point2D smallFishStartingPosition;
	private Point2D bigFishStartingPosition;

	public Room() {
		objects = new ArrayList<GameObject>();
	}

	private void setName(String name) {
		roomName = name;
	}
	
	public String getName() {
		return roomName;
	}
	
	private void setEngine(GameEngine engine) {
		this.engine = engine;
	}

	public void addObject(GameObject obj) {
		objects.add(obj);
		engine.updateGUI();
	}
	
	public void removeObject(GameObject obj) {
		objects.remove(obj);
		engine.updateGUI();
	}
	
	public List<GameObject> getObjects() {
		return objects;
	}

	public void setSmallFishStartingPosition(Point2D heroStartingPosition) {
		this.smallFishStartingPosition = heroStartingPosition;
	}
	
	public Point2D getSmallFishStartingPosition() {
		return smallFishStartingPosition;
	}
	
	public void setBigFishStartingPosition(Point2D heroStartingPosition) {
		this.bigFishStartingPosition = heroStartingPosition;
	}
	
	public Point2D getBigFishStartingPosition() {
		return bigFishStartingPosition;
	}
	
	public static Room readRoom(File file, GameEngine engine) {
		Room r = new Room();
        
		r.objects = new ArrayList<>(); 
        r.engine = engine;
        r.roomName = file.getName();

		r.createWaterBackground();
		
		r.readLevelFile(file); 
		
		return r;
	}

	private void createWaterBackground() {									//coloca agua em todas as posiçoes 
		
		for (int x = 0; x < GRID_SIZE; x++) {
			for (int y = 0; y < GRID_SIZE; y++) {
				Point2D position = new Point2D(x, y); 
				objects.add(new Water(this, position)); 
			}
		}
	}
	

	private void readLevelFile(File file) {										// ler ficheiro do nivel
	    
	    try (Scanner scanner = new Scanner(file)) {
            int y = 0;
            
            while (scanner.hasNextLine() && y < GRID_SIZE) {
                String line = scanner.nextLine();
                
                for (int x = 0; x < line.length() && x < GRID_SIZE; x++) {
                    
                    char c = line.charAt(x);
                    Point2D p = new Point2D(x, y);
                    
                    switch (c) {													//caso de Letras que indicam objetos 
                        case 'W':
                            objects.add(new Wall(this, p)); 
                            break;
                        case 'H':
                            objects.add(new SteelHorizontal(this, p)); 
                            break;
                        case 'B':
                            BigFish.getInstance().setRoom(this);
                            BigFish.getInstance().setPosition(p);
                            this.setBigFishStartingPosition(p);  
                            objects.add(BigFish.getInstance());
                            break;
                        case 'S':
                            SmallFish.getInstance().setRoom(this);
                            SmallFish.getInstance().setPosition(p);
                            this.setSmallFishStartingPosition(p); 
                            objects.add(SmallFish.getInstance());
                            break;
                        case 'C':
                            objects.add(new Cup(this, p)); 
                            break;
                        case 'R':
                        	Stone st=new Stone(this,p);
                        	st.setHasHiddenKrab(true);
                        	objects.add(st);
                        	break;
                        case 'A':
                        	objects.add(new Anchor(this,p));
                        	break;
                        case 'X':
                        	objects.add(new HoledWall(this,p));
                        	break;
                        case 'T':
                        	objects.add(new Trap(this,p));
                        	break;
                        case 'Y':
                        	objects.add(new Trunk(this,p));
                        	break;
                        case 'b':
                        	objects.add(new Bomb(this,p));
                        	break;
                        case 'F':
                        	objects.add(new Buoy(this,p));
                        	break;
                        case 'V':
                        	objects.add(new SteelVertical(this,p));
                        	break;
                        case 'I':
                        	objects.add(new IndestrutivelWall(this,p));
                        	break;
                        case 'K':
                        	objects.add(new Krab(this,p));
                        	break;
                            
                        case ' ':
                            
                            break;
                    }
                }
                y++;
            }
        } catch (FileNotFoundException e) {
            System.err.println("Erro: Ficheiro de nível não encontrado: " + file.getName());
            
        }
	}

}