import java.util.Scanner;
import java.util.Arrays;
import java.util.Comparator;

public class Main {
  
  static Scanner sc = new Scanner(System.in);
  
  public static void main(String[] args) {
    System.out.println("Welcome to Pig!");
    
    System.out.println();
    
    System.out.print("Instructions: Pig is a dice game playable for 2 to 6 players. On each turn, each player will roll a die. If they roll a 1, they will bust and not get any points for that round. If they don't roll a one, they will have the option to hold and keep the score they have, adding it on to their total score, or to roll again for a chance to add more onto their score for that round. They can keep rolling and adding onto their score for that turn as much as they want, but if they bust on any subsequent roll, they will not recieve any points for that round. The first player to 100 wins, and if two players get over 100 on the same turn, the player with the higher score wins.");
    
    System.out.println("\n");
    
    // plays and keeps playing the game if needed
    boolean keepPlaying = true;
    while (keepPlaying){
      play(findPlayers()); // method the run the game
      boolean valid = false;
      while (!valid) { // to make sure user input is valid
        System.out.println();
        System.out.println("Play Again? Yes (1) or No (0)");
        int answer = sc.nextInt();
        if (answer == 1) {
          valid = true;
        } else if (answer == 0) {
          System.out.println("Thanks for playing!");
          valid = true;
          keepPlaying = false;
        } else {
          System.out.println("Not a valid response. Please try again.");
        }
      }
    }
  }
  
  public static Player[] findPlayers() { // method to create players
  
    // finds number of players to create
    boolean valid = false;
    int numPlayers = 0;
    while (!valid) { // to make sure user input is valid 
      System.out.println("How many players? From 2 to 6.");
      numPlayers = sc.nextInt();
      if (numPlayers >= 2 && numPlayers <= 6) {
        valid = true;
      } else {
        System.out.println("Not a valid response. Please try again.");
      }
    }
    
    // makes array of player objects
    Player[] players = new Player[numPlayers];
    for (int i = 0; i < numPlayers; i++) {
      System.out.println("What is player " + (i + 1) + "'s name?");
      String name = sc.next();
      players[i] = new Player(name);
    }
    
    // creates computer players if wanted
    if (numPlayers < 6) {
      System.out.println();
      players = addComputers(players);
    }
    
    // establishes play order
    rollOrderDice(players);
    orderPlayers(players);
    
    return players;
  }
  
  public static Player[] addComputers(Player[] players) {
    
    // sees if computer players should be added
    boolean valid = false;
    int shouldAddComputers = 0;
    boolean makeComputers = false;
    System.out.println();
    while (!valid) { // to make sure user input is valid
      System.out.println("Add computers? Yes (1) or No (0)");
      shouldAddComputers = sc.nextInt();
      if (shouldAddComputers == 1) {
        makeComputers = true;
        valid = true;
      } else if (shouldAddComputers == 0) {
        makeComputers = false;
        valid = true;
      } else {
        System.out.println("Not a valid response. Please try again.");
      }
    }

    valid = !valid;
    if (makeComputers) {
      
      // sees how many xomputer players should be added
      int numComputers = 0;
      int max = 6 - players.length;
      while (!valid) { // to make sure user input is valid
        System.out.println("How many computers? Max possible is " + max);
        numComputers = sc.nextInt();
        if (numComputers <= max) {
          valid = true;
        } else {
          System.out.println("Not a valid response. Please try again.");
        }
      }
      
      // makes new array to hold all players
      Player[] aiAndPlayers = new Player[players.length + numComputers];
      for (int i = 0; i < players.length; i++) {
        aiAndPlayers[i] = players[i];
      }
      
      // adds computer player objects to new array
      for (int i = players.length; i < aiAndPlayers.length; i++) {
        aiAndPlayers[i] = new Player("computer" + ((i + 1) - players.length), true);
      }
      return aiAndPlayers;
    }
    
    // if no computer players should be added, original array is returned
    return players;
  }
  
  public static void rollOrderDice(Player players[]) { // method that has each player roll a die to determine the order
  
    System.out.println("\n");
    
    // rolls a die for each player and makes sure they're all different
    System.out.println("Each player rolls a die to determine the order.");
    int[] dice = new int[players.length];
    for (int i = 0; i < players.length; i++) {
      dice[i] = (int) Math.ceil(Math.random() * 6);
      if (i > 0) {
        boolean noTies = false;
        while(!noTies) {
          boolean noTie = true;
          for (int j = 1; j <= i; j++) {
            if (noTie) {
              if (dice[i] == dice[i - j]) {
                noTie = false;
              } 
            }
          }
          if (noTie) {
            noTies = true;
          } else {
            dice[i] = (int) Math.ceil(Math.random() * 6);
          }
        }
      } 
    }
    
    // sets and prints the die rolled for each player
    for(int i = 0; i < players.length; i++) {
      players[i].setOrderDie(dice[i]);
      System.out.println(players[i].getName() + " rolled a " + dice[i]);
    }
  }
  
  public static void orderPlayers(Player[] players) { // puts the players in order according to what die they rolled
  
    Comparator<Player> diceComparator = Comparator.comparing(Player::getOrderDie); // makes it so that the array sorter can sort the players based on the dice they rolled
    Arrays.parallelSort(players, diceComparator); // sorts the players from lowest to highest based on the dice they rolled
    reverseOrder(players); // makes the order from highest to lowest
    
    System.out.println();
    
    // prints the order of players
    System.out.println("Order:");
    for(int i = 0; i < players.length; i++) {
      System.out.println(players[i].getName());
    }
  }
  
  public static void reverseOrder(Player players[]) { // makes the player order from highest to lowest
    int i = 0;
    int j = players.length - 1;
    Player temp;
    while (j > i) {
      temp = players[j];
      players[j] = players[i];
      players[i] = temp;
      j--;
      i++;
    }
  }
  
  public static void play(Player[] players) { // method to take the turns of the game and determine a winner
  
    int highscore = 0;
    int highscorePlayer = -1;
    while (highscore < 100) {
      // this is a turn
      for (int i = 0; i < players.length; i++) {
        System.out.println("\n");
        if (players[i].getIsComputer()) {
          players[i].setScore(ai(players[i].getScore(), players[i].getName()));
        } else {
          players[i].setScore(turn(players[i].getScore(), players[i].getName()));
        }
      }
      
      // scoreboard at end of turn
      System.out.println("\n*************************");
      for (int i = 0; i < players.length; i++) {
        System.out.println(players[i].getName() + "'s score: " + players[i].getScore());
        if (players[i].getScore() > highscore) {
          highscore = players[i].getScore();
          highscorePlayer = i;
        }
      }
      System.out.println("*************************\n");
    }
    
    // the player with the highest score over 100 wins
    System.out.println(players[highscorePlayer].getName() + " won!");
  }
  
  public static int turn(int totalScore, String name) { // method that takes the turn for each player
  
    int die = (int) Math.ceil(Math.random() * 6);
    int turnScore = 0;
    boolean bust = false;
    while (!bust) { // gets out of turn once they bust or hold
      System.out.println(name + " rolled a " + die);
      if (die == 1) {
        System.out.println(name + " busted");
        turnScore = 0;
        bust = true;
      } else { // if they don't bust, ask if they want to roll again or hold 
        turnScore += die;
        boolean valid = false;
        while (!valid) { // to make sure user input is valid
          System.out.println(name + "'s turn score is " + turnScore);
          System.out.println("Roll again (1)? Hold (2)?");
          int response  = sc.nextInt();
          if (response == 1) {
            die = (int) Math.ceil(Math.random() * 6);
            valid = true;
          } else if (response == 2) {
            bust = true; // to get out of loop even if they didn't bust
            valid = true;
          } else {
            System.out.println("Not a valid response. Please try again.");
          }
        }
      }
    }
    
    // returns new total score 
    return totalScore + turnScore;
  }
  
  public static int ai(int totalScore, String name) { // method that takes the turn for each computer player
  
    int die = (int) Math.ceil(Math.random() * 6);
    int turnScore = 0;
    boolean bust = false;
    while (!bust) { // gets out of turn once they bust or hold
      System.out.println(name + " rolled a " + die);
      if (die == 1) {
        System.out.println(name + " busted");
        turnScore = 0;
        bust = true;
      } else { // only holds if their turn score is at least 18
        turnScore += die;
        System.out.println(name + "'s turn score is " + turnScore);
        if (turnScore < 18) {
          System.out.println(name + " rolled again");
          die = (int) Math.ceil(Math.random() * 6);
        } else {
          System.out.println(name + " said hold");
          bust = true; // to get out of loop even if they didn't bust
        }
      }
    }
    
    // returns new total score 
    return totalScore + turnScore;
  }
} 