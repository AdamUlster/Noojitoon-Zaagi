package main;
import javax.swing.JFrame;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//exits program when user hits the x buton
        window.setResizable(false);//user cannot resize the window
        window.setTitle("my 2D adventure");//game title

        GamePanel gamePanel = new GamePanel();//create gamepanel
        window.add(gamePanel);
        window.pack();//cause the window to be size to fit the dimensions

        window.setLocationRelativeTo(null);//sets the window so that it has no specific location
        window.setVisible(true);//displays window now that it's generated
        gamePanel.startGameThread();

    }
}
