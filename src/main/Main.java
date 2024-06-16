package main;
import javax.swing.JFrame;

public class Main {
    public static void main(String[] args) {
        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // exits program when user hits the x button
        window.setResizable(false); // user cannot resize the window
        window.setTitle("my 2D adventure"); // game title

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
