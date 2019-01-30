public class Player {
  
  private String name;
  private boolean isComputer; // variable used to know which kind of turn to take
  private int orderDie; // variable used to order the players in the array based on the initial did they roll
  private int score; 
  
  public Player(String name) {
    this.name = name;
    isComputer = false;
    orderDie = -1;
    score = 0;
  }
  
  public Player(String name, boolean isComputer) {
    this.name = name;
    this.isComputer = isComputer;
    orderDie = -1;
    score = 0;
  }
  
  public String getName() {
    return name;
  }
  
  public boolean getIsComputer() {
    return isComputer;
  }
  
  public void setOrderDie(int newOrderDie) {
    orderDie = newOrderDie;
  }
  
  public int getOrderDie() {
    return orderDie;
  }
  
  public int getScore() {
    return score;
  }
  
  public void setScore(int newScore) {
    score = newScore;
  }
}