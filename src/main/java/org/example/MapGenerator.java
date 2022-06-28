package org.example;

import java.awt.*;

public class MapGenerator {
    int map[][];
    private int brickWidth;
    private int brickHeight;

    private int difficulty = GamePanel.getDifficulty();

    public int getBrickWidth() {
        return brickWidth;
    }

    public int getBrickHeight() {
        return brickHeight;
    }

    public MapGenerator(int row, int col) {
        map = new int[row][col];
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                map[i][j] =difficulty;
            }
        }
        brickHeight=150/row;
        brickWidth=540/col;
    }

    public void draw(Graphics2D g) {

        switch (difficulty) {
            case 3:
            for (int i = 0; i < map.length; i++) {
                for (int j = 0; j < map[0].length; j++) {
                    if (map[i][j] ==3) {
                        g.setColor(Color.RED);
                        g.fillRect(j * brickWidth + 80, i * brickHeight + 50, brickWidth, brickHeight);

                        g.setStroke(new BasicStroke(3));
                        g.setColor(Color.BLACK);
                        g.drawRect(j * brickWidth + 80, i * brickHeight + 50, brickWidth, brickHeight);
                    }
                }
            }
            case 2:
                for (int i = 0; i < map.length; i++) {
                    for (int j = 0; j < map[0].length; j++) {
                        if (map[i][j] == 2) {
                            g.setColor(Color.BLUE);
                            g.fillRect(j * brickWidth + 80, i * brickHeight + 50, brickWidth, brickHeight);

                            g.setStroke(new BasicStroke(3));
                            g.setColor(Color.BLACK);
                            g.drawRect(j * brickWidth + 80, i * brickHeight + 50, brickWidth, brickHeight);
                        }
                    }
                }
                case 1:
                for (int i = 0; i < map.length; i++) {
                    for (int j = 0; j < map[0].length; j++) {
                        if (map[i][j] ==1) {
                            g.setColor(Color.WHITE);
                            g.fillRect(j * brickWidth + 80, i * brickHeight + 50, brickWidth, brickHeight);

                            g.setStroke(new BasicStroke(3));
                            g.setColor(Color.BLACK);
                            g.drawRect(j * brickWidth + 80, i * brickHeight + 50, brickWidth, brickHeight);
                        }
                    }
                }
            break;
        }
    }
    public void setBrickCollision(int row,int col){
        map[row][col]--;
    }
}
