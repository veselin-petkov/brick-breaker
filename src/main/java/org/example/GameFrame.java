package org.example;

import javax.swing.*;
import java.io.IOException;

public class GameFrame extends JFrame {

    GameFrame(){
        try {
            this.add(new GamePanel());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        this.setTitle("Brick Breaker");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.pack();
        this.setVisible(true);
        this.setLocationRelativeTo(null);
        this.setBounds(10,10,700,600);
    }
}
