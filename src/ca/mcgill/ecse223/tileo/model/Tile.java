/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.25.0-9e8af9e modeling language!*/

package ca.mcgill.ecse223.tileo.model;
import java.io.Serializable;
import ca.mcgill.ecse223.tileo.util.Node;
import java.util.*;

// line 21 "../../../../../TileOPersistence.ump"
// line 270 "../../../../../TileO.ump"
public abstract class Tile implements Serializable
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Tile Attributes
  private int x;
  private int y;
  private boolean hasBeenVisited;

  //Tile Associations
  private List<Connection> connections;
  private Game game;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Tile(int aX, int aY, Game aGame)
  {
    x = aX;
    y = aY;
    hasBeenVisited = false;
    connections = new ArrayList<Connection>();
    boolean didAddGame = setGame(aGame);
    if (!didAddGame)
    {
      throw new RuntimeException("Unable to create tile due to game");
    }
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setX(int aX)
  {
    boolean wasSet = false;
    x = aX;
    wasSet = true;
    return wasSet;
  }

  public boolean setY(int aY)
  {
    boolean wasSet = false;
    y = aY;
    wasSet = true;
    return wasSet;
  }

  public boolean setHasBeenVisited(boolean aHasBeenVisited)
  {
    boolean wasSet = false;
    hasBeenVisited = aHasBeenVisited;
    wasSet = true;
    return wasSet;
  }

  public int getX()
  {
    return x;
  }

  public int getY()
  {
    return y;
  }

  public boolean getHasBeenVisited()
  {
    return hasBeenVisited;
  }

  public boolean isHasBeenVisited()
  {
    return hasBeenVisited;
  }

  public Connection getConnection(int index)
  {
    Connection aConnection = connections.get(index);
    return aConnection;
  }

  public List<Connection> getConnections()
  {
    List<Connection> newConnections = Collections.unmodifiableList(connections);
    return newConnections;
  }

  public int numberOfConnections()
  {
    int number = connections.size();
    return number;
  }

  public boolean hasConnections()
  {
    boolean has = connections.size() > 0;
    return has;
  }

  public int indexOfConnection(Connection aConnection)
  {
    int index = connections.indexOf(aConnection);
    return index;
  }

  public Game getGame()
  {
    return game;
  }

  public static int minimumNumberOfConnections()
  {
    return 0;
  }

  public static int maximumNumberOfConnections()
  {
    return 4;
  }

  public boolean addConnection(Connection aConnection)
  {
    boolean wasAdded = false;
    if (connections.contains(aConnection)) { return false; }
    if (numberOfConnections() >= maximumNumberOfConnections())
    {
      return wasAdded;
    }

    connections.add(aConnection);
    if (aConnection.indexOfTile(this) != -1)
    {
      wasAdded = true;
    }
    else
    {
      wasAdded = aConnection.addTile(this);
      if (!wasAdded)
      {
        connections.remove(aConnection);
      }
    }
    return wasAdded;
  }

  public boolean removeConnection(Connection aConnection)
  {
    boolean wasRemoved = false;
    if (!connections.contains(aConnection))
    {
      return wasRemoved;
    }

    int oldIndex = connections.indexOf(aConnection);
    connections.remove(oldIndex);
    if (aConnection.indexOfTile(this) == -1)
    {
      wasRemoved = true;
    }
    else
    {
      wasRemoved = aConnection.removeTile(this);
      if (!wasRemoved)
      {
        connections.add(oldIndex,aConnection);
      }
    }
    return wasRemoved;
  }

  public boolean addConnectionAt(Connection aConnection, int index)
  {  
    boolean wasAdded = false;
    if(addConnection(aConnection))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfConnections()) { index = numberOfConnections() - 1; }
      connections.remove(aConnection);
      connections.add(index, aConnection);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveConnectionAt(Connection aConnection, int index)
  {
    boolean wasAdded = false;
    if(connections.contains(aConnection))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfConnections()) { index = numberOfConnections() - 1; }
      connections.remove(aConnection);
      connections.add(index, aConnection);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addConnectionAt(aConnection, index);
    }
    return wasAdded;
  }

  public boolean setGame(Game aGame)
  {
    boolean wasSet = false;
    if (aGame == null)
    {
      return wasSet;
    }

    Game existingGame = game;
    game = aGame;
    if (existingGame != null && !existingGame.equals(aGame))
    {
      existingGame.removeTile(this);
    }
    game.addTile(this);
    wasSet = true;
    return wasSet;
  }

  public void delete()
  {
    ArrayList<Connection> copyOfConnections = new ArrayList<Connection>(connections);
    connections.clear();
    for(Connection aConnection : copyOfConnections)
    {
      if (aConnection.numberOfTiles() <= Connection.minimumNumberOfTiles())
      {
        aConnection.delete();
      }
      else
      {
        aConnection.removeTile(this);
      }
    }
    Game placeholderGame = game;
    this.game = null;
    placeholderGame.removeTile(this);
  }

   public abstract void land();
  // line 282 "../../../../../TileO.ump"
   public boolean isConnectedWith(Tile t){
    boolean isConnected = false;
	  for (Connection conn: getConnections()) {
		  if (conn.getTile(0)==t || conn.getTile(1)==t) {
			  isConnected = true;
			  break;
		  }
	  }
	  return isConnected;
  }

  // line 293 "../../../../../TileO.ump"
   public ArrayList<Tile> getDisconnectedNeighbors(){
    ArrayList<Tile> neigbors = new ArrayList<Tile>();
    Tile t;
    int nx;
    int ny;

    //up
    nx = getX();
    ny = getY()-1;
    if (ny > 0) {
        t = getGame().getTileAtXY(nx,ny);
        if (t!=null)
            if (!isConnectedWith(t))
                neigbors.add(t);
    }
    //down
    nx = getX();
    ny = getY()+1;
    t = getGame().getTileAtXY(nx,ny);
    if (t!=null)
        if (!isConnectedWith(t))
            neigbors.add(t);
    //left
    nx = getX()-1;
    ny = getY();
    if (nx > 0) {
        t = getGame().getTileAtXY(nx,ny);
        if (t!=null)
            if (!isConnectedWith(t))
                neigbors.add(t);
    }
    //right
    nx = getX()+1;
    ny = getY();
    t = getGame().getTileAtXY(nx,ny);
    if (t!=null)
        if (!isConnectedWith(t))
            neigbors.add(t);

    return neigbors;
  }

  // line 336 "../../../../../TileO.ump"
   public ArrayList<Tile> getNeighbours(int boardsize){
    ArrayList<Tile> neigbours = new ArrayList<Tile>();

    if(!(this.getX() == boardsize )) {
      if (game.getTileAtXY(this.getX() + 1, this.getY()) != null)
        neigbours.add(game.getTileAtXY(this.getX() + 1, this.getY()));
    }

    if(!(this.getX() == 0)) {
      if (game.getTileAtXY(this.getX() - 1, this.getY()) != null)
        neigbours.add(game.getTileAtXY(this.getX() - 1, this.getY()));
    }

    if(!(this.getY() == boardsize)) {
      if (game.getTileAtXY(this.getX(), this.getY() - 1) != null)
        neigbours.add(game.getTileAtXY(this.getX(), this.getY() - 1));
    }

    if(!(this.getY() == 0)) {
      if (game.getTileAtXY(this.getX(), this.getY() + 1) != null)
        neigbours.add(game.getTileAtXY(this.getX(), this.getY() + 1));
    }


    return neigbours;
  }

  // line 363 "../../../../../TileO.ump"
   public ArrayList<Tile> getPossibleMovesFrom(int depth){
    // Depth first search with limited depth, Iterate over the possible children
	      // Cannot go back but loops are allowed
	      Stack<Node> fringe = new Stack<Node>();
	      List<Connection> connections;
	      List<Tile> connectedTiles;
	      HashSet<Tile> possibleTiles = new HashSet<Tile>();
	      int tIdx;
	      Tile t;
	      
	      Node current = new Node(this, null, 0);
	      fringe.push(current);

	      while (!fringe.isEmpty()) {
	          current = fringe.pop();
	    	  t = current.getTile();
	    	  
	          if (current.getDepth() == depth){
	        	  possibleTiles.add(t);
	              continue;
	          }
	          
	          connections = t.getConnections();
	          for (Connection aConnection : connections){
	        	  connectedTiles = aConnection.getTiles();
	        	  tIdx = connectedTiles.get(0)==t ? 1:0; // select the other tile
	              if (current.getParent()==null  || connectedTiles.get(tIdx) != current.getParent().getTile())
	            	  fringe.push(new Node(connectedTiles.get(tIdx), current, current.getDepth()+1));
	          }
	      }
	      return new ArrayList<Tile>(possibleTiles);
  }

  // line 396 "../../../../../TileO.ump"
   public ArrayList<Tile> getShortestPathToWin(){
    Deque<Tile> path = new ArrayDeque<Tile>(); 
	   
	   Queue<Node> fringe = new LinkedList<Node>();
	   Node current = new Node(this, null, 0);
	   HashSet<Tile> visited = new HashSet<Tile>();
	   fringe.add(current);
	   
	   while(!fringe.isEmpty()) {
		   current = fringe.remove();
		   if (current.getTile() instanceof WinTile){
			   while (current != null) {
				   path.addFirst(current.getTile()); // build the path
				   current = current.getParent();
			   }
		   }
		   else {
			   visited.add(current.getTile());
			   for (int i=1; i<=6; ++i) {
				   for (Tile t: current.getTile().getPossibleMovesFrom(i)){
					   if (!visited.contains(t)){
						   visited.add(t);
						   fringe.add(new Node(t, current, current.getDepth()+1));
					   }
				    }
			   }
		   }
	   }
	   return new ArrayList<Tile>(path);
  }

  // line 427 "../../../../../TileO.ump"
   public int manhattanToWin(){
    int dx = getX() - getGame().getWinTile().getX();
	   int dy = getY() - getGame().getWinTile().getY();
	   
	   return Math.abs(dx) + Math.abs(dy);
  }


  public String toString()
  {
    String outputString = "";
    return super.toString() + "["+
            "x" + ":" + getX()+ "," +
            "y" + ":" + getY()+ "," +
            "hasBeenVisited" + ":" + getHasBeenVisited()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "game = "+(getGame()!=null?Integer.toHexString(System.identityHashCode(getGame())):"null")
     + outputString;
  }  
  //------------------------
  // DEVELOPER CODE - PROVIDED AS-IS
  //------------------------
  
  // line 24 ../../../../../TileOPersistence.ump
  private static final long serialVersionUID = 4853757344933261749L ;

  
}