=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=
CIS 120 Game Project README
PennKey: cdf
=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=

===================
=: Core Concepts :=
===================

- List the four core concepts, the features they implement, and why each feature
  is an appropriate use of the concept. Incorporate the feedback you got after
  submitting your proposal.

  1. Collections: The concept implements how the level configurations are stored. The map file 
  simply contains rows of numbers, each of which specify a different object that needs to be 
  placed in a certain location for that map. When the map file is parsed some logic determines
  the right object (for example a tile object or coin object) to create and then stores that new 
  instance in my collection. The collection that I choose to hold these objects was a 
  Map<Integer, Tuple<Set<Tile>, Set<Coin>>>. While it may look like an overly complex 
  representation, it serves its function simply and efficiently. Tuple is a class that I created 
  that functions identically to a Tuple in OCaml (stores two different objects and allows you to
  call first or second). Every map has a specific configuration of coins and tiles, which I 
  stored in sets because there should be no repeat elements(every tile and coin should have a 
  different location) and the order in which they are rendered or interacted with does not matter.
  I am able to access both sets via the Tuple quickly just by calling that level's number, which 
  is stored along with the Tuple in the Map. To get the right configuration, I simply pass the 
  Map an integer 0-9.

  2. I/O: This concept is used in two different places, high scores and the map file. As 
  discussed above, my map configurations are stored in a map file which is read and parsed 
  when the game is first opened. This allows the maps the be the same between games, which 
  means high scores are comparable between games. To store those high scores, I offer the user 
  a text box to enter a username and then store that data (along with their score, # of rooms 
  they survived, and the time they survived) in my own format in highscore.txt. Whenever the 
  user lands on the starting page (either at the start of a game or after a game), the file 
  is read, parsed, and displayed in order of the highest score. Using an external file is 
  key to being able to maintain a list of scores between games.

  3. Inheritance: I used this concept to set up a structure for creating different enemy types.
  At the start of each level, my map will be randomly populated with a certain number of mobs,
  all of which will be a subclass of the Mob abstract class. This sub-typing allows for me to
  call all of the mob's tick() method in one statement, even though they are instances of 
  different classes. It makes handling the mobs in a given level much, much easier. The Mob 
  abstract class extends the GameObj class shared by all rendered objects in the GameCourt. 
  However, to make a mob, extra fields must be passed into its "super" constructor such as
  MAX_HEALTH, POWER, and POINTS_WORTH. These fields would be static final fields in each of 
  the mob subclasses. The Mob abstract class has several methods that it provides every subclass
  with. getCurrHealth(), reduceHealth(), getHitPower(), and getPointsWorth() are all shared 
  methods that are not overridden in the subclasses and are used to handle player health and the 
  score. Mob also overrides GameObj's abstract method tick() to include dealing damage to the 
  player when they intersect (another shared method between subclasses). An abstract method that
  subclasses must provide a body for is how the mob moves. This is different between walking, 
  flying, or following mobs. The use of dynamic dispatch here again simplifies the process
  of making new types of mobs and handling the ones spawned at each level.

  4. JUnit Testing: The specific component that JUnit testing was done for is Mob interaction
  with the player. The system that is in place to detect collisions, update player health and
  score, and update the running list of mobs with non zero health, all are key components of 
  the game that must function correctly. Concurrently, there are many edge cases where, for
  example multiple things may be happening within the same tick() call. Thus, it is essential
  that the order in which events occur are (1) consistent and (2) logical. The cases that were
  written all aim to test such things. Some examples are when a mob intersects with a bullet
  and does/doesn't die. In each case, there are many actions that are taken that update score, 
  the MobSpawner, the Mob's health, the and Bullet's velocity. Another example is where a 
  bullet intersects a mob which is on top of a player (a case which is actually a common 
  occurrence in real gameplay). The test case makes sure that if the bullet removes all of the 
  mobs health, it will not remove any of the player's health within the same tick, ensuring
  proper ordering. Testing allows for a controlled, isolated situation which is not possible
  to achieve in simply playing the game to debug, especially where there are random elements
  that the coder cannot control. Testing allows for proper debugging in essential parts of 
  the program.


=========================
=: Your Implementation :=
=========================

- Provide an overview of each of the classes in your code, and what their
  function is in the overall game.

  Game - contains the run() method and the main method. Creates the JFrame and all of the JPanels
  that populate it. It's function is to create the user interface that leads the user to the
  GameCourt and to hold all of the different JComponents.

  StartPanel - creates a JPanel that is populated with all JLabels and JButtons needed
  for the start screen. I also adds the ActionListeners to the buttons. It also handles reading 
  the high score file. This class contains a private class that is used in formatting the high 
  scores and arranging them in order.

  EndPanel - creates a JPanel that is populated with all the JLabels, JButtons, and JTextFields
  for the end game screen. It is responsible for writing to the high score file.

  GameCourt - extends JPanel and represents the playable area. In it, there is a timer that
  calls the tick method. It is responsible for ending the game when health = 0 and updating the
  score and roomCounter JLabels's text, the Hearts display of current player health, and the 
  WeaponBag display of the current weapon. It's tick method calls all of the other objects' tick
  methods. In GameCourt, all of the objects needed for the game are added to the object controller.

  GameObj - abstract class for an object that needs to be rendered. Provides methods to detect
  collisions with another GameObj, mobs, and tiles of the map. Has two abstract methods, draw()
  and tick(ObjControler oc) which allow subclasses to specify their appearance and movement/input.

  ObjController - class that makes it easy to add new objects to the GameCourt. Its draw and tick
  methods simply calls all draw or tick methods of the GameObj's that were added.

  Direction - enumerator that specifies NORTH, SOUTH, EAST, and WEST

  Tuple - groups two Objects together and allows one access with getFirst() and getSecond()

  Input - mostly has static fields which handle all input related things. Contains boolean arrays
  for all keys and mouse buttons. Makes it easy to add keyboard and mouse interaction with 
  GameObj's. Because these arrays are static, input related changes to the game state can be 
  added nearly anywhere simply.

  FileManager - reads all files and puts them into static fields. These objects can be then 
  anywhere within the game. Also responsible for parsing the map file and creating the tile
  and coin objects for each level. 

  Mob - abstract class for all enemies (described in more detail in the sub-typing concept).
  Provides some methods to aid in the interaction between the player the enemies. For example, 
  this class handles reducing the player's health when a mob intersects the player. Extends
  GameObj. A mob is killed when it's health is zero and at that point, the player's score will
  increase by the mob's POINT_VALUE.

  MobSpawner - class for the addition of mobs into the GameCourt. Maintains a List<Mob> which 
  is all mobs that have been spawned for a level. Takes in a ObjController in the constructor to 
  allow for special mobs to be spawned on only certain levels. Has a reset method that is called 
  whenever the player moves on to the next level. It's draw method calls the draw method of all
  Mobs in the List. It's tick method removes mobs that have zero health and calls all of the Mob
  in the List's tick(). 

  Walker - subclass of Mob. Has a mover method that specifies how this type of mob moves (it 
  paces until it reaches a wall or Tile and then turns around). Its draw method uses the proper 
  image from FileManager. Also has a static spawn method which creates a List<Mob> populated with 
  Walkers.

  Flyer - subclass of Mob. Has a mover method that specifies how this type of mob moves (it "flies"
  by only bouncing off walls and ignoring Tiles). Also has a static spawn method which creates a 
  List<Mob> populated with Flyers.

  Follower - subclass of Mob. Has a mover method that specifies how this type of mob moves (it 
  follows the player ignoring Tiles like a Flyer). Also has a static spawn method which creates 
  a List<Mob> populated with Followers. The Follower mob only shows up on map 6.

  Square - this object represents the player. In its tick method, uses the Input class to change
  its velocity depending on key inputs. Also has collision dection for walls, Tiles, and Coins.
  Calls TileMap's reset function when a new level is in order. The square class is passed into
  various other classes (like Follower or Light) that depend on it's position. Also has a field
  currDirection which is maintained according to the velocities. Square also maintains much 
  of the games state. Because nearly all of the game state's changes originate from a change
  in Square's position, velocity, etc., it is easiest to keep fields like score and roomCount
  in fields inside of Square.

  TileMap - is responsible for choosing randomly one of the 10 maps to give render when its 
  reset function is called. Has a method for removing Tiles (used for the pickaxe) and removing 
  a coin (when the player picks up a coin). TileMap has a private Set<Tile> and Set<Coin> which 
  are deleted and repopulated from FileManager's copy of the maps whenever reset is called. This
  makes sure that even when a player removes a Tile with the pickaxe, it that Tile will reappear
  when the player is in that map again.

  Tile - represents a wall object. Takes in a bool isDestroyable and an image from FileManager
  to render out in its location. Also implements Comparable to make sure no two tiles have the
  same location (when added to FileManager's Set<Tile> it needs this). Subclass of GameObj.

  Coin - similar to tile in that it is created and stored in the FileManager and TileMap classes.
  Has a tick method that makes it appear to be spinning and a draw method that uses the color
  taken in by its constructer. A coin object can be picked up by the player to add points. 
  Subclass of GameObj.

  Light - subclass of GameObj that covers the entire GameCourt area with a transparent dark
  rectangle. The tick method of Light uses the Square's current position to create a light 
  area which when renders looks like the square is radiating light. The purpose of this class
  is solely to make the game more difficult and pretty.

  Bullet - object that is created whenever a Weapon's dealDamage method is called. Takes in only
  a direction and using its static final MAX_VEL field determines what v_x and v_y should be. 
  Using this direction, it also changes it's draw method to have the right width and height. 
  Also has collision detection for walls, the TileMap, and Mobs. A collision with any will set
  the bullet's velicty to zero (resulting in its removal in the Weapon's class). 

  Weapon - maintains parameters (such as power, rateOfFire) and also a List<Bullet>. Whenever
  the method dealDamage(Direction d) is called, it appends a new Bullet to this list. Each
  instance of the Weapon class is responsible for removing Bullets that have a zero velocity and
  calling the tick methods of the rest.

  WeaponBag - this class contains all of the weapon objects that a player has access to. It 
  contains a private Map<String, Weapon> which is iterated over to call the draw and tick methods
  for each weapon. Also handles changing the current weapon depending on key input and calling
  the dealDamage method of the current weapon when SPACE is pressed. To call dealDamage, 
  WeaponBag uses the ObjController's square currDirection field, which ensures the bullets are 
  fired in the right direction relative to the player's movements.

  WeaponBagFrame - class that extends JPanel and lives outside of the GameCourt. Displays the 
  current Weapon and is updated by the GameCourt (GameCourt takes in a WeaponBagFrame in
  its constructor). It's reset method returns the current Weapon back to originalTool. 
  Similar to HeartsFrame.

  HeartsFrame - class that extends JPanel and lives outside of the GameCourt. Is a visual
  representation of the player's current health. Is updated by the GameCourt (GameCourt takes in 
  a HeartsFrame in its constructor). Its reset method re-displays all four heart images. Similar 
  to WeaponBagFrame in that it shows GameCourt's state in a separate JPanel.


- Were there any significant stumbling blocks while you were implementing your
  game (related to your design, or otherwise)?
  
  Yes. One of the largest issues I faced was how to update fields outside of the main 
  GameCourt instance while the game was still being played. I tried to keep the actual
  game implementation as separate as possible from the game engine parts, but it is 
  impossible to change the state of external labels or panels due to changes in state 
  of the game court. Secondly, in order to create JUnit tests, I had to refractor my 
  code significantly. In a previous version, the ObjController was every GameObj's tick 
  method took in an ObjController. This was my way of allowing every object access to 
  other GameObj's parameters. However, this rigid implementation make it impossible to
  test isolated cases, so I had to restructure the way that the objects interacted with
  each other, which was a large stumbling block.


- Evaluate your design. Is there a good separation of functionality? How well is
  private state encapsulated? What would you refactor, if given the chance?
  
  I believe that there is a pretty good separation of funcionality. Every object is only
  passed the parameters that it absolutely needs to function (defined by how it must interact
  with other objects and its purpose). I made sure to encapsulate state as much as
  possible, really only making static final fields public and using getters and setters
  to control how other classes interacted with such fields. In cases where I exposed a
  a field, such as with the getMobs() method in MobSpawner, I purposefully did not return
  a clone of the List<Mob> because the purpose of that method is to allow an outsider
  to alter the field. What I would refractor if given the chance is the actual Game class
  and the system that I use to update JPanels other than the GameCourt. It currently seems 
  messy and cumbersome. However, much of the things that I wanted to refractor, I have.


========================
=: External Resources :=
========================

- Cite any external resources (libraries, images, tutorials, etc.) that you may
  have used while implementing your game.
  Images:
  http://vignette2.wikia.nocookie.net/minecraftpocketedition/images/b/b7/Pickaxe.png
  https://s-media-cache-ak0.pinimg.com/originals/32/41/be/3241bedbdd6626c4eccdda210df375f6.gif
  http://orig02.deviantart.net/8baa/f/2012/058/0/5/8_bit_heart_stock_by_xquatrox-d4r844m.png
  http://i40.tinypic.com/14l7k8l.png
  http://previewcf.turbosquid.com/Preview/2014/08/01__23_00_40/StoneWallTexture.jpg84701617-622a-4550-bec7-1fa5e2e18b0dLarge.jpg
  Font:
  http://www.dafont.com/8bit-wonder.font


