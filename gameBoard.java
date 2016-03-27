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
  private boolean win;
  private boolean tie;
  private String winner; 
  private Font font;  

  public static void main (String[] args) {
    gameBoard gui = new gameBoard();
    gui.start();  
  }

  public void start() {

    frame = new JFrame();    
    font = new Font("SansSerif", Font.BOLD, 24);
    column = new Rectangle[7];
    piece = new Piece[7][6];

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

    restart();
  }     

  public void restart() {

    playerColor = Color.RED;
    win = false;
    tie = false;
      
    for (int x = 0; x < 7; x++) {
      for(int y = 0; y < 6; y++) {
        piece[x][y] = new Piece((x+1)*both, (y+1)*both);  
      }
    }
    
    repaint();
  }

  @Override
  public void paintComponent(Graphics g) {
    if (!(win || tie)) {
      g.setColor(Color.WHITE);
      g.fillRect(0, 0, frame.getWidth(), frame.getHeight());
      g.setColor(Color.BLUE);
      g.fillRect(diameter + space/2, diameter + space/2, 7*both, 6*both);
      hoverPiece(g);
    } else {
      g.setFont(font);
      changePlayer();
      g.setColor(Color.WHITE);
      g.fillRect(0, 0, frame.getWidth(), diameter + space/2);
      if (win) {
        g.setColor(playerColor);
        g.drawString(winner + " Wins!", frame.getWidth()/2 - both , both/2);
      }
      if (tie && !win) {
        g.setColor(Color.BLACK);
        g.drawString("Tie", frame.getWidth()/2 - both/2, both/2);
      }
    }       

    for (int x = 0; x < 7; x++) {
      for(int y = 0; y < 6; y++) {
        g.setColor(piece[x][y].getColor());
        g.fillOval(piece[x][y].getX(), piece[x][y].getY(), diameter, diameter);
      }
    }

  }
  
  private void hoverPiece(Graphics g) {
    g.setColor(playerColor);
    g.fillOval(mouse_x - diameter/2, space/2, diameter, diameter);  
  } 

  @Override
  public void mouseDragged(MouseEvent e) {};

  @Override
  public void mouseMoved(MouseEvent e) {
    if(!(win || tie)) {
      mouse_x = e.getX(); 
      repaint();        
    }
  }  
  
  public class ClickHandle extends MouseAdapter {
    @Override
    public void mouseClicked(MouseEvent ev) {
      if (!(win || tie)) { 
        for(int i = 0; i < column.length; i++) {
          if (column[i].contains(ev.getX(), ev.getY())) {
            //System.out.println(String.format("Clicked Column %d", i));
            dropPiece(i);
            checkBoard();
            repaint();
          } 
        }    
      } else {
        restart();
      } 
    } 
  }

  private void dropPiece(int col) {
    for (int row = piece[0].length-1; row >=0; row--) {
      if (piece[col][row].getColor().equals(Color.WHITE)) {
        piece[col][row].setColor(playerColor);
        changePlayer();
        tie = checkTie();
        return;
      }
    } 
  }

  private void changePlayer() {
    if (playerColor.equals(Color.RED)) {
      playerColor = Color.BLACK;
    } else {
      playerColor = Color.RED;
    }
  }
  
  private boolean checkTie() {
    for (int x = 0; x < piece.length; x++) {
      for (int y = 0; y < piece[0].length; y++) {
        if (piece[x][y].getValue() == 0) {
          return false;
        }
      } 
    }
    return true;
  }
    
  
  private void checkBoard() {

    for (int x = 0; x < piece.length - 3; x++) {
      for(int y = 0; y < piece[0].length - 3; y++) {
        checkForwardDiag(x,y);
        checkBackDiag(x,y); 
  
        if (x == piece.length - 4) {
          checkAllCols(x,y);
        } else { 
          checkCol(x,y);
        }

        if (y == piece[0].length - 4) {
          checkAllRows(x,y);
        } else {
          checkRow(x,y); 
        }
  
      }
    }

  }

  private void  checkWin(int result) {
    if (result == 4) {
      win = true;
      winner = "Red";
    } 
    if (result == -4) {
      win = true;
      winner = "Black";
    }
  }
  
  private void checkCol(int x, int y) {
    int result = 0; 
    for (int i = 0; i < 4; i++) {
      result += piece[x][y+i].getValue();  
    }
    checkWin(result);    
  } 

  private void checkRow(int x, int y) {
    int result = 0; 
    for (int i = 0; i < 4; i++) {
      result += piece[x+i][y].getValue();  
    }
    checkWin(result);    
  }   

  private void checkAllCols(int x, int y) {
    for (int i = 0; i < 4; i++) {
      checkCol(x+i, y);
    }
  }

  private void checkAllRows(int x, int y) {
    for (int i = 0; i < 4; i++) {
      checkRow(x, y+i);
    }
  }

  private void checkForwardDiag(int x, int y) {
    int result = 0; 
    for (int i = 0; i < 4; i++) {
      result += piece[x+i][y+3-i].getValue();  
    }
    checkWin(result);    
  }

  private void checkBackDiag(int x, int y) {
    int result = 0; 
    for (int i = 0; i < 4; i++) {
      result += piece[x+i][y+i].getValue();  
    }
    checkWin(result);    
  }
}
