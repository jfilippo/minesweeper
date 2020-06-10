=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=
CIS 120 Game Project README
PennKey: jfilippo
=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=

===================
=: Core Concepts :=
===================

- List the three core concepts, the features they implement, and why each feature
  is an appropriate use of the concept. Incorporate the feedback you got after
  submitting your proposal.

  1. 2D-Arrays
 	  I used 2D arrays to model the board grid storing the Tile objects in a 2D,
 	  Tile[][] array. This way I had to use nested for loops and could evaluate
 	  each Tile's surroundings. This also will be useful for the recursion, as my
 	  recursive algorithm can use the 2D array structure to help guide its way
 	  through the tiles.

  2. Recursion
  	  I used this concept when handling with what I called "floor opening". I defined
  	  a function called openFloor, defined on the board class, that recursively calls
  	  itself on surrounding blocks, granted that some conditions are, or are not true.
  	  This allows the board to appropriately open up once the player makes a few moves.

  3. Collections
  	  I used the Collections library for the purpose of keeping track of a leaderboard.
  	  Everytime a user wins the game, he/she is prompted in a pop-up for the name that
  	  he/she would like to add to the leaderboard. I then store that information in a 
  	  TreeMap, with the key being the username, and the value being the total number of
  	  moves needed to clear the board. This way, whenever the user clicks on the
  	  Leaderboard button, a String is constructed by iterating through the map's entrySet
  	  and concatenating the names of the winners.

=========================
=: Your Implementation :=
=========================

- Provide an overview of each of the classes in your code, and what their
  function is in the overall game.
  
  Game.java
  This is the main class of the game, implementing the run() function.
  In this class, I define the visual design using the BorderLayout,
  as well as add components such as labels, a timer, buttons, pop-up windows,
  etc...
  This class contains mostly aspects of visual design, with logic consisting
  mostly in the addition of ActionListeners, which interact with the clickable
  components of my game.
  
  Board.java
  This class is responsible for most of the logic in the minesweeper game.
  When the board is initialized, the user is prompted in a pop-up for the
  size of the board he/she wishes to play in. After that, the constructor
  creates a board, assigns it a GridLayout, and populates each grid slot with
  a brand new Tile.
  It is responsible for handling Tile clicks and MouseListeners. As well as that,
  it has some control over the timer, and most importantly, deals with the logic
  of winning, losing, and floor opening.
  It implements a recursive function called openFloor(), which recursively looks
  for bombs and clears nearby tiles after every click.
  
  Tile.java
  This class extends the JPanel object and is used to define what a Tile is, 
  how it is drawn and all of its functionality.
  It keeps track of internal state variables, such as if that Tile contains a bomb,
  if it is flagged, if it is hidden or not, etc...



- Were there any significant stumbling blocks while you were implementing your
  game (related to your design, or otherwise)?
	The main one relates to resetting the board. I currently have a system implemented,
	in which the board resets properly, but it is not a board with a new distribution of bombs.
	A new distribution of bombs only occurs upon restarting the game. The reason I was not able
	to figure this out is because of the way I chose to use the swing frames, and the fact that
	if I created new Tile classes everytime, their MouseListeners would be lost. Of course, this
	is less important for larger grids, but if the player chooses to play small grids, he/she can
	easily memorize the layout after a few games.
	
	Another stumbling block was the timer. I found this JComponent especially hard to deal with,
	and I was not able to add the time to the leaderboard. Actually I was able to do so, but as
	it was always reset, a 00:00 time got added every time. This is why I used total moves as a
	point parameter in my leaderboard.

- Evaluate your design. Is there a good separation of functionality? How well is
  private state encapsulated? What would you refactor, if given the chance?
	I did a lot of things well, but I would definitely do some refactoring if I had the time.
	First of all, I'd use Swing differently, allowing me to resize my grid after every reset,
	and also allowing a new random mine distribution to be placed without having to 
	reinitalize the game.
	In terms of encapsulation, I had to make more variables public that I originally wanted to,
	but it was the way I found to make the different components of the game communicate with each
	other for some functionality.


========================
=: External Resources :=
========================

- Cite any external resources (libraries, images, tutorials, etc.) that you may
  have used while implementing your game.
  
  I didn't use images. I used a red square to represent flagged tiles
  and a cross to represent a bomb.
  I used the Collections Library as well as the Swing library (JLabel, Timer, etc).
  
  
