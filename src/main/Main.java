package main;
import javax.swing.JFrame;

public class Main {
    public static void main(String[] args) {
        JFrame window = new JFrame();//CREATE NEW J-FRAME
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // EXITS PROGRAM WHEN USER HITS X
        window.setResizable(false);  // USER CANNOT RESIZE THE WINDOW
        window.setTitle("my 2D adventure");  // GAME TITLE

        GamePanel gamePanel = new GamePanel(); // CREATE PANEL
        window.add(gamePanel);//ADD GAME PANEL TO THE WINDOW
        window.pack(); // FORCE THE WINDOW TO BE SIZED TO FIT THE DIMENSIONS

        window.setLocationRelativeTo(null); // SETS THE WINDOW SO THAT IT HAS NO SPECIFIC LOCATION ON THE SCREEN
        window.setVisible(true); // DISPLAY NEWLY GENERATED WINDOW

//        START GAME PANEL
        gamePanel.setupGame();
        gamePanel.startGameThread();

    }
}
