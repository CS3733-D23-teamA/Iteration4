package edu.wpi.teamA.database.Singletons;

public enum IdleSingleton {
  INSTANCE;
  // example of how attributes are added to the Enum
  Double x = 0.00;
  Double y = 0.00;

  public Double getX() {
    return x;
  }

  public Double getY() {
    return y;
  }

  public void setX(Double x1) {
    this.x = x1;
  }

  public void setY(Double y1) {
    this.y = y1;
  }
}
