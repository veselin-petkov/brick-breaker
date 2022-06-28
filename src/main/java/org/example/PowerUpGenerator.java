package org.example;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class PowerUpGenerator {
    public Rectangle bounds;
    public int x=0,y=0;
    private int width = 25;
    private int height = 25;
    public int powerUpType;
    public boolean remove = false;
    Path currentRelativePath = Paths.get("");
    String s = currentRelativePath.toAbsolutePath().toString() + "\\src\\main\\resources\\";
    public Image growthImg =ImageIO.read(new File(s+"PUGrowth.png"));
    //public Image multiBall = ImageIO.read(new File("src/main/resources/PUMulitball.png"));
    public Image fireBall = ImageIO.read(new File(s+"PUFireball.png"));
    private Image image;
    public void setGrowthImg(Image growthImg) {
        this.growthImg = growthImg;
    }

//    public PowerUpGenerator() throws IOException {
//        growthImg =ImageIO.read(new File("src/main/resources/PUGrowth.png"));
//        //multiBall = ImageIO.read(new File(s+"PUMultiball.png"));
//        fireBall = ImageIO.read(new File("src/main/resources/PUFireball.png"));
//        bounds = new Rectangle(x, y, width, height);
//    }

    public Image getImage() {
        return image;
    }

    public PowerUpGenerator(int x, int y, int type) throws IOException {
        this.x = x;
        this.y = y;
        powerUpType=type;
        if(powerUpType == 1) {
            image = growthImg;
        }
        //if(powerUpType == 2) {  image = multiBall;}
        if(powerUpType == 3) {
            image = fireBall;
        }
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getX() {
        return x;
    }


    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
    public void tick() {
        if (y < Frame.HEIGHT) {
            y += 3;
            bounds = new Rectangle(x, y, width, height);
        } else {
            remove = true;
        }
    }
}
