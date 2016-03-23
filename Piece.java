import java.awt.Color;

public class Piece {

  private int x_pos;
  private int y_pos;
  private Color color;

  public Piece(int x, int y) {
    x_pos = x;
    y_pos = y;
    color = Color.WHITE;
  }
  
  public int getX() {
    return x_pos;
  }
    
  public int getY() {
    return y_pos;
  }

  public Color getColor() {
    return color;
  }

  public void setColor(Color c) {
    color = c;
  }
}
