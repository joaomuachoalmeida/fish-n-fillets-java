package pt.iscte.poo.game;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import objects.SmallFish;
import objects.BigFish;
import objects.Buoy;
import objects.Debris;
import objects.GameCharacter;
import objects.GameObject;
import objects.HoledWall;
import objects.ImmovableObjects;
import objects.MovebleObjects;
import pt.iscte.poo.gui.ImageGUI;
import pt.iscte.poo.observer.Observed;
import pt.iscte.poo.observer.Observer;
import pt.iscte.poo.utils.Direction;
import pt.iscte.poo.utils.Point2D;
import java.awt.event.KeyEvent;

public class GameEngine implements Observer {
	
	private Map<String,Room> rooms;
	private Room currentRoom;
	private int lastTickProcessed = 0;
	public  Boolean FishSelect = true;
	private static GameEngine INSTANCE;
	public boolean gameEnded;
	public int moves=0;
	private long startTimeMillis;					//variavel para tratar do tempo 


	public GameEngine() {
		INSTANCE = this;
		rooms = new HashMap<String,Room>();
		loadGame();
		currentRoom = rooms.get("room0.txt");
		gameEnded = false;
		SmallFish.getInstance().setRoom(currentRoom);
		SmallFish.getInstance().setPosition(currentRoom.getSmallFishStartingPosition());  
		BigFish.getInstance().setRoom(currentRoom);
		BigFish.getInstance().setPosition(currentRoom.getBigFishStartingPosition());   
		
		updateGUI();
		FishSelect = true;
		startTimeMillis = System.currentTimeMillis();
	}
	
	public static GameEngine getInstance() {
		return INSTANCE;
	}
	
	public boolean FishSelect() {
		return FishSelect;
	}
	
	

	private void loadGame() {
		File[] files = new File("./rooms").listFiles();
		for(File f : files) {
			rooms.put(f.getName(),Room.readRoom(f,this));
		}
	}
	public void nextLevel() {
	    
	    String name = currentRoom.getName();								//saca o nome do nivel atual exemplo room0.txt
	    int currentLevelNum = Integer.parseInt(name.replaceAll("[^0-9]", ""));			//separa o numero do resto
	    int nextLevelNum = currentLevelNum + 1;												

	    File nextFile = new File("./rooms/room" + nextLevelNum + ".txt");			//ficheiro do nivel seguinte

	    if (!nextFile.exists()) {
	        
	        ImageGUI.getInstance().showMessage("Fim do Jogo", "Parabéns, completou o jogo!");			//acabar o jogo caso nao haja proximo nivel
	        long tempoPassado =System.currentTimeMillis() - startTimeMillis;							
	        
	        saveScore(tempoPassado,moves);						//salvar a pontuçao
	        showHighscores();									//motrar tabela top10
	        
	        gameEnded=true;
	        ImageGUI.getInstance().dispose();
	        return;
	    }

	    
	    currentRoom = Room.readRoom(nextFile, this);
	    updateGUI();
	}

	
	public void restartLevel() {
	    	
		ImageGUI.getInstance().showMessage("Reiniciar", "A reiniciar o nivel");
		
		if( "room0.txt".equals(currentRoom.getName())) {						//caso seja reiniciado no primeiro nivel, começar tanto com os moves como o tempo de inicio
		    moves=0;
		    startTimeMillis =System.currentTimeMillis();
		}

		
		File file = new File("rooms/" + currentRoom.getName());
		currentRoom = Room.readRoom(file, this);					//ler novamento o ficheiro do nivel atual
		
		SmallFish.getInstance().setRoom(currentRoom);  
	    SmallFish.getInstance().setPosition(currentRoom.getSmallFishStartingPosition());  		//colocar os peixes de volta nas posiçoes

	    BigFish.getInstance().setRoom(currentRoom);  
	    BigFish.getInstance().setPosition(currentRoom.getBigFishStartingPosition()); 			
	    
	    FishSelect=true;																	//joga o smallfish como no inicio
		updateGUI();
		
	}
	

	@Override
	public void update(Observed source) {
		if(gameEnded)
			return;

		if (ImageGUI.getInstance().wasKeyPressed()) {
			int k = ImageGUI.getInstance().keyPressed();
			
			if(k==KeyEvent.VK_R) {
				restartLevel();
				return;
			}
			
			if (k == KeyEvent.VK_SPACE) {
				FishSelect = !FishSelect;
				return;
			}
			try {
				if (FishSelect) {												//fishSelect Variavel para saber qual peixe joga (false=bigFish)
					SmallFish.getInstance().move(Direction.directionFor(k).asVector());
				} else {
					BigFish.getInstance().move(Direction.directionFor(k).asVector());
				}
				

			} catch(IllegalArgumentException e){
				System.out.println("Tecla não permitida.");
			}
		}
		int t = ImageGUI.getInstance().getTicks();
		while (lastTickProcessed < t) {
			processTick();
		}
		ImageGUI.getInstance().update();
	}
	
	private void updateStatusBar() {
	    String name = currentRoom.getName();
	    int currentLevelNum = Integer.parseInt(name.replaceAll("[^0-9]", "")) + 1;

	    ImageGUI.getInstance().setStatusMessage(
	        "Nível: " + currentLevelNum + " | Moves: "+ moves 					//mostrar nivel atual e movimentos
	    );
	}

	
	private void applyGravity() {
		List<GameObject> snapshot = new ArrayList<>(currentRoom.getObjects());
		
	    for (GameObject obj : snapshot) {
	        if (obj instanceof MovebleObjects) {
	            ((MovebleObjects) obj).gravity();
	        }
	    }
	}
	private void checkPeixesOut() {

	    Point2D smallPos = SmallFish.getInstance().getPosition();
	    Point2D bigPos = BigFish.getInstance().getPosition();

	   
	    if (smallPos.getX() < 0 || smallPos.getX() >= Room.GRID_SIZE ||
	        smallPos.getY() < 0 || smallPos.getY() >= Room.GRID_SIZE) {
	        
	        currentRoom.removeObject(SmallFish.getInstance());				// remove o smallfish se saiu do tabuleiro
	        FishSelect = false; 
	    }

	    if (bigPos.getX() < 0 || bigPos.getX() >= Room.GRID_SIZE ||
	        bigPos.getY() < 0 || bigPos.getY() >= Room.GRID_SIZE) {
	        
	        currentRoom.removeObject(BigFish.getInstance());					//remove o bigfish se saiu do tabuleiro
	        FishSelect = true;
	    }
	}
	
	private boolean checkLevelComplete() {											//se os dois peixes tiveren fora da como nivel completo
	    return !currentRoom.getObjects().contains(SmallFish.getInstance()) &&
	           !currentRoom.getObjects().contains(BigFish.getInstance());
	}
	
	private boolean isCrushed(GameCharacter fish) {							//verificar se peixes estao esmagados
		Point2D pos = fish.getPosition();
		int light=0;
		int heavy=0;
		int x=pos.getX();
		
		for (GameObject obj : currentRoom.getObjects()) {
		    if (obj.getPosition() != null && obj.getPosition().equals(pos) &&
		        obj instanceof HoledWall) {												//caso o smallfish esteja dentro de uma parede com buraco, os objetos estao suportados na parede
		        return false;
		       
		    }
		    
		    
		}
		
		for(int y=pos.getY()-1; y>=0; y--) {								//correr todas as posiçoes verticalmente acima de um peixe
			Point2D p = new Point2D(x,y);
			GameObject target= fish.getObjectAt(p);
			
			if(target ==null) {							//se nao tiver nada sai do for
				break;
			}
			if(target instanceof ImmovableObjects || target instanceof GameCharacter) {		//se tiver umas parede ou algo do genero ou outro peixe sai do for
				break;
			}
			
			if(target instanceof MovebleObjects && !((MovebleObjects)target).isHeavy()) {				
				light++;
			}
			if(target instanceof MovebleObjects && ((MovebleObjects)target).isHeavy()) {
				heavy++;
			}
		}

	    return fish.isCrushedBy(light, heavy);
	}
	
	

	private void processTick() {
		if(gameEnded)
			return;
		applyGravity();				//aplica gravidade
		lastTickProcessed++;
		checkPeixesOut();			//verifica se os peixes sairam
		updateStatusBar();			// atuliza o titulo
		
		if (currentRoom.getObjects().contains(SmallFish.getInstance()) &&
		        isCrushed(SmallFish.getInstance())) {
				ImageGUI.getInstance().showMessage("Morte", "O peixe pequeno foi esmagado!");		//esmagamento do peixe pequeno
		        restartLevel();
		        return;
		    }

		    if (currentRoom.getObjects().contains(BigFish.getInstance()) &&
		        isCrushed(BigFish.getInstance())) {
		    	ImageGUI.getInstance().showMessage("Morte", "O peixe grande foi esmagado!");  			//esmagamnto do peixe grande
		    	restartLevel();
		        return;
		    }
		
		
		 for (GameObject obj : new ArrayList<>(currentRoom.getObjects())) {
		        if (obj instanceof Debris) {											//remove os ticks restantes para remover os debris
		            ((Debris) obj).tick();
		        }
		    }
		
		if(checkLevelComplete()) {
			ImageGUI.getInstance().showMessage("Parabens!", "Passaste de nivel!");					//ve se o nivel foi completo
			nextLevel();
		}
		
	}

	public void updateGUI() {
		if(currentRoom!=null) {
			ImageGUI.getInstance().clearImages();
			ImageGUI.getInstance().addImages(currentRoom.getObjects());
		}
	}
	
	private void saveScore(long elapsedMillis, int moves) {
	    try {
	        File file = new java.io.File("scores.txt");
	        FileWriter fw = new java.io.FileWriter(file, true);
	        PrintWriter out = new java.io.PrintWriter(fw);

	        String name = ImageGUI.getInstance().showInputDialog("Highscore", "Introduz o teu nome:");				//pergunta o nome ao jogador, caso nada seja inserido mete anonimo
	        if (name == null || name.trim().isEmpty()) {
	            name = "Anonimo";
	        }

	        out.println(name + ";" + elapsedMillis + ";" + moves);				//escvreve no ficheiro nome;milisegundos;moves
	        out.close();
	    } catch (java.io.IOException e) {
	        System.err.println("Erro a gravar scores: " + e.getMessage());
	    }
	}
	
	private void showHighscores() {
	    java.util.List<String> lines = new java.util.ArrayList<>();

	    try (java.util.Scanner sc = new java.util.Scanner(new java.io.File("scores.txt"))) {
	        while (sc.hasNextLine()) {
	            lines.add(sc.nextLine());
	        }
	    } catch (java.io.FileNotFoundException e) {
	        ImageGUI.getInstance().showMessage("Highscores", "Ainda não há scores.");
	        return;
	    }

	    																		// ordenar primeiro por movimentos, em caso de empate organiza por tempo
	    lines.sort((a, b) -> {
	        String[] pa = a.split(";");
	        String[] pb = b.split(";");
	        int movesA = Integer.parseInt(pa[2]);
	        int movesB = Integer.parseInt(pb[2]);
	        if (movesA != movesB)
	            return Integer.compare(movesA, movesB);				//compara moves
	        long timeA = Long.parseLong(pa[1]);
	        long timeB = Long.parseLong(pb[1]);
	        return Long.compare(timeA, timeB);							//compara o tempo
	    });

	    StringBuilder sb = new StringBuilder();
	    sb.append("Top 10 highscores:\n");
	    int limit = Math.min(10, lines.size());
	    for (int i = 0; i < limit; i++) {
	        String[] p = lines.get(i).split(";");
	        String name = p[0];
	        long timeMs = Long.parseLong(p[1]);
	        int moves = Integer.parseInt(p[2]);
	        int seconds = (int)(timeMs / 1000);												//divide os milisegundos para obter segundos
	        sb.append((i+1) + ". " + name + " - " + moves + " mov, " + seconds + "s\n");
	    }

	    ImageGUI.getInstance().showMessage("Highscores", sb.toString());
	}


	
}
