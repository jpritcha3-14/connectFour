import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

class gameBoard extends JPanel implements MouseMotionListener {
    
  JFrame frame; 
  private int mouse_x = 20;
  private int mouse_y = 50;
  private int diameter = 50;
  private int space = 10;
  private int both = diameter + space;
  public Rectangle[] column; 
  private Piece[][] piece;
  private Color playerColor;   


  public static void main (String[] args) {
    gameBoard gui = new gameBoard();
    gui.go();  
  }

  public void go() {
    frame = new JFrame();
    column = new Rectangle[7];
    piece = new Piece[7][6];
    playerColor = Color.RED;

    for (int x = 0; x < 7; x++) {
      for(int y = 0; y < 6; y++) {
        piece[x][y] = new Piece((x+1)*both, (y+1)*both);  
      }
    }
 
    for (int i = 0; i < column.length; i++) {
      column[i] = new Rectangle(diameter + space/2 + i*(both), diameter + space/2, both-1, 6*both);
    }
    
    this.setFocusable(true);
    frame.getContentPane().add(this);
    frame.addMouseMotionListener(this);
    frame.addMouseListener(new ClickHandle());

    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);     
    frame.setSize(9*both, 8*both);
    frame.setVisible(true);
  }

  @Override
  public void paintComponent(Graphics g) {
    g.setColor(Color.WHITE);
    g.fillRect(0, 0, frame.getWidth(), frame.getHeight());
    g.setColor(Color.BLUE);
    g.fillRect(diameter + space/2, diameter + space/2, 7*both, 6*both);
    for (int x = 0; x < 7; x++) {
      for(int y = 0; y < 6; y++) {
        g.setColor(piece[x][y].getColor());
        g.fillOval(piece[x][y].getX(), piece[x][y].getY(), diameter, diameter);
      }
    }
    hoverPiece(g);
  }
  
  private void hoverPiece(Graphics g) {
    g.setColor(playerColor);
    g.fillOval(mouse_x - diameter/2, space/2, diameter, diameter);  
  } 

  @Override
  public void mouseDragged(MouseEvent e) {};

  @Override
  public void mouseMoved(MouseEvent e) {
    mouse_x = e.getX(); 
    repaint();        
  }  
  
  public class ClickHandle extends MouseAdapter {
    @Override
    public void mouseClicked(MouseEvent ev) {
      for(int i = 0; i < column.length; i++) {
        if (column[i].contains(ev.getX(), ev.getY())) {
          System.out.println(String.format("Clicked Column %d", i));
          dropPiece(i);
          repaint();
        } 
      }    
    } 
  }

  public void dropPiece(int col) {
    for (int row = piece[0].length-1; row >=0; row--) {
      if (piece[col][row].getColor().equals(Color.WHITE)) {
        piece[col][row].setColor(playerColor);
        changePlayer();
        return;
      }
    } 
  }

  public void changePlayer() {
    if (playerColor.equals(Color.RED)) {
      playerColor = Color.BLACK;
    } else {
      playerColor = Color.RED;
    }
  }
    
}
