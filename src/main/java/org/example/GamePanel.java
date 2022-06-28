package org.example;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Random;
import java.util.TimerTask;


public class GamePanel extends JPanel implements ActionListener, KeyListener, MouseMotionListener, MouseListener {
    static final int SCREEN_WIDTH = 700;
    static final int SCREEN_HEIGHT = 600;
    static final int UNIT_SIZE = 20;
    private boolean running = false;
    private int score = 0;
    private int totalBricks = 21;
    private Timer timer;
    private final int delay = 8;
    private static int difficulty = 1;
    private int ballposX;
    private int ballposY;
    private int ballXdir = -3;
    private int ballYdir = -4;
    private int x = 320;
    private int y = 350;
    private int paddleWidth = 100;
    Random random;
    private boolean keyboard = false;
    private boolean mainMenu = true;
    private MapGenerator map;
    private PowerUpGenerator[] powerUp;
    private final Image backgroundImage;
    JButton playButton, exitButton;
    JToggleButton btnEasy, btnMedium, btnHard, btnMouse, btnKeyboard;
    String select = "Choose difficulty and controls:";
    String info = "Press SPACE or LEFT MB to start";
    public boolean isDraw = false;
    private boolean onFire = false;
    private boolean startingPoint = true;
    Path currentRelativePath = Paths.get("");
    String s = currentRelativePath.toAbsolutePath().toString() + "\\src\\main\\resources\\";
    Font pixelFont;
    Font pixelFont30, pixelFont38, pixelFont20;


    GamePanel() throws IOException {


        try {
            InputStream stream = ClassLoader.getSystemClassLoader().getResourceAsStream("Minecraft.ttf");
            pixelFont = Font.createFont(Font.TRUETYPE_FONT, Objects.requireNonNull(stream)).deriveFont(14f);

        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }
        pixelFont30 = pixelFont.deriveFont(30f);
        pixelFont38 = pixelFont.deriveFont(38f);
        pixelFont20 = pixelFont.deriveFont(20f);

        random = new Random();
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.addMouseMotionListener(new MyMouseAdapter());
        this.setLayout(null);
        this.addMouseListener(this);

        backgroundImage = ImageIO.read(new File(s + "vnAYc.jpg"));
        timer = new Timer(8, this);
        mainMenu();
    }

    public void mainMenu() {
        if (mainMenu) {
            this.setLayout(null);

            playButton = new JButton(new ImageIcon(s + "start_button2.png"));
            exitButton = new JButton(new ImageIcon(s + "exit.png"));
            exitButton.setBounds(SCREEN_WIDTH / 2 - 125, SCREEN_HEIGHT - 200, 250, 77);
            playButton.setBounds(SCREEN_WIDTH / 2 - 125, SCREEN_HEIGHT / 8, 250, 77);
            this.add(playButton);
            this.add(exitButton);
            gameControlsAndDifficulty();

            playButton.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent evt) {
                    mainMenu = false;
                    startGame();
                }
            });
            exitButton.addActionListener(new CloseListener());
        }
    }

    public void startGame() {
        this.setFocusable(true);
        requestFocus();
        this.remove(playButton);
        this.remove(exitButton);
        playButton.setVisible(false);
        this.remove(btnEasy);
        this.remove(btnMedium);
        this.remove(btnHard);
        this.remove(btnMouse);
        this.remove(btnKeyboard);
        map = new MapGenerator(3, 7);
        running = true;
        timer.start();
        powerUp = new PowerUpGenerator[5];
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImage, 0, 0, this);
        if (mainMenu) {
            g.setColor(Color.WHITE);
            g.setFont(pixelFont30);
            g.drawString(select, 130, SCREEN_HEIGHT / 2 - 100);
        }
        if (!mainMenu && startingPoint) {
            g.setColor(Color.WHITE);
            g.setFont(pixelFont30);
            g.drawString(info, 105, SCREEN_HEIGHT / 2);
        }

        if (running) {
            draw(g);
        } else {
            if (totalBricks <= 0) {
                running = false;
                g.setColor(Color.WHITE);
                g.setFont(pixelFont38);
                g.drawString("YOU WON!", 240, SCREEN_HEIGHT / 2 - 100);
                g.setFont(pixelFont20);
                g.drawString("Press ENTER to play again", 210, SCREEN_HEIGHT / 2 + 100);
            }
        }
        if (!running && totalBricks >= 1 && !mainMenu) {
            g.setColor(Color.RED);
            g.setFont(pixelFont38);
            g.drawString("Game Over", 230, SCREEN_HEIGHT / 2 - 100);
            g.setFont(pixelFont20);
            g.setColor(Color.GREEN);
            g.drawString("Your score:", 260, SCREEN_HEIGHT / 2);
            g.drawString(String.valueOf(score), 380, SCREEN_HEIGHT / 2);
            g.drawString("Press Enter to Restart", 230, SCREEN_HEIGHT / 2 + 100);
        }
    }

    public void draw(Graphics g) {
        //drawing the ball and plank
        if (keyboard) {
            //blue plank
            g.setColor(Color.CYAN);
            g.fillRect(x, 550, getPaddleWidth(), 20);
            //ball
            g.setColor(Color.CYAN);
            g.fillOval(ballposX, ballposY, 18, 18);
            if (onFire) {
                g.setColor(Color.RED);
                g.fillOval(ballposX, ballposY, 18, 18);
            }

        } else {
            //red plank
            g.setColor(Color.CYAN);
            g.fillRect(y, 550, getPaddleWidth(), 20);
            //ball
            g.setColor(Color.CYAN);
            g.fillOval(ballposX, ballposY, 18, 18);
            if (onFire) {
                g.setColor(Color.RED);
                g.fillOval(ballposX, ballposY, 18, 18);
            }
        }
        map.draw((Graphics2D) g);
        for (int i = 0; i < powerUp.length; i++) {
            if (powerUp[i] != null) {
                g.drawImage(powerUp[i].getImage(), powerUp[i].getX(), powerUp[i].getY(), powerUp[i].getWidth(), powerUp[i].getWidth(), null);
            }
        }
        if (totalBricks <= 0) {
            running = false;
        }
    }

    public void moveWithMouse(int y) {
        if (y + getPaddleWidth() < 685) {
            this.y = y;
            repaint();
        }
    }

    public void moveWithArrows(int x) {
        this.x += x;
        repaint();
    }

    public void ballMove() throws IOException, InterruptedException {
        int tmpX;
        if (startingPoint) {
            if (keyboard) {
                ballposY = 530;
                ballposX = x + 45;
            } else {
                ballposY = 530;
                ballposX = y + 45;
            }

        }
        if (keyboard) {
            if (new Rectangle(ballposX, ballposY, 20, 20).intersects(new Rectangle(x, 550, getPaddleWidth(), 20))) {
                ballYdir = -ballYdir;
            }
        } else {
            if (new Rectangle(ballposX, ballposY, 20, 20).intersects(new Rectangle(y, 550, getPaddleWidth(), 20))) {
                ballYdir = -ballYdir;
            }
        }

        if (ballposY > 560) running = false;

        //ball collision with bricks
        A:
        for (int i = 0; i < map.map.length; i++) {
            for (int j = 0; j < map.map[0].length; j++) {
                if (map.map[i][j] > 0) {
                    int brickX = j * map.getBrickWidth() + 80;
                    int brickY = i * map.getBrickHeight() + 50;

                    Rectangle rectangle = new Rectangle(brickX, brickY, map.getBrickWidth() + 1, map.getBrickHeight());
                    Rectangle ballRectangle = new Rectangle(ballposX, ballposY, 20, 20);
                    Rectangle brickRectangle = rectangle;

                    if (ballRectangle.intersects(brickRectangle)) {
                        map.setBrickCollision(i, j);
                        if (random.nextInt(10 - 1) + 1 == 1) {
                            System.out.println("powerup");
                            isDraw = true;
                            //spawnPowerUp()
                            for (int l = 0; l < powerUp.length; l++) {
                                if (powerUp[l] == null) {
                                    powerUp[l] = new PowerUpGenerator(brickRectangle.x, brickRectangle.y, 1);
                                    isDraw = false;
                                    break;
                                }
                            }
                        } else if (random.nextInt(20 - 1) + 1 == 2) {
                            isDraw = true;
                            //spawnPowerUp()
                            for (int l = 0; l < powerUp.length; l++) {
                                if (powerUp[l] == null) {
                                    powerUp[l] = new PowerUpGenerator(brickRectangle.x, brickRectangle.y, 3);
                                    isDraw = false;
                                    break;
                                }
                            }
                        }

                        totalBricks--;
                        score += 10;
                        if (onFire) {

                        } else if (ballRectangle.x + 17 <= brickRectangle.x || ballRectangle.x + 3 >= brickRectangle.x + brickRectangle.width) {
                            ballXdir = -ballXdir;

                        } else {
                            ballYdir = -ballYdir;
                        }
                        break A;
                    }
                }
            }
        }
        for (int i = 0; i < powerUp.length; i++) {
            if (powerUp[i] != null) {
                powerUp[i].setY(powerUp[i].getY() + 1);
                if (powerUp[i].remove) {
                    powerUp[i] = null;
                }
                if (keyboard) {
                    tmpX = x;
                } else tmpX = y;
                if (new Rectangle(powerUp[i].getX(), powerUp[i].getY(), powerUp[i].getWidth(), powerUp[i].getHeight()).intersects(new Rectangle(tmpX, 550, getPaddleWidth(), 20))) {
                    if (powerUp[i].powerUpType == 1) {
                        setPaddleWidth(getPaddleWidth() + 10);//grow paddle
                    }
                    if (powerUp[i].powerUpType == 3) {
                        if (!onFire) {
                            onFire = true;
                            new java.util.Timer().schedule(new RemindTask(), 3 * 1000);
                        }
                    }
                    powerUp[i] = null;
                }
            }
        }
        if (onFire) {

        }
        ballposX += ballXdir;
        ballposY += ballYdir;
        if (ballposX < 0) {
            ballXdir = -ballXdir;
        }
        if (ballposY < 0) {
            ballYdir = -ballYdir;
        }
        if (ballposX > SCREEN_WIDTH - 30) {
            ballXdir = -ballXdir;
        }
    }

    class RemindTask extends TimerTask {
        public void run() {
            onFire = false;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            try {
                ballMove();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        }
        repaint();
    }

    public int getPaddleWidth() {
        return paddleWidth;
    }

    public void setPaddleWidth(int paddleWidth) {
        this.paddleWidth = paddleWidth;
    }

    public class MyKeyAdapter extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();
            if (key == KeyEvent.VK_LEFT) {
                if (x < 10) {
                    x = 0;
                } else {
                    moveWithArrows(-UNIT_SIZE);
                }
            }
            if (key == KeyEvent.VK_RIGHT) {
                if (x + getPaddleWidth() > 660) {
                    x = 570 + getPaddleWidth();
                } else {
                    moveWithArrows(UNIT_SIZE);
                }
            }
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                if (!running) {
                    totalBricks = 21;
                    startingPoint = true;
                    mainMenu = true;
                    ballposY = 530;
                    ballposX = y + 40;
                    ballXdir = -3;
                    ballYdir = -4;
                    mainMenu();
                    score = 0;
                    setPaddleWidth(100);
                    setDifficulty(1);
                    //map = new MapGenerator(3,7);
                }
            }
            if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                if (running) {
                    startingPoint = false;
                }
            }
            if (key == KeyEvent.VK_ESCAPE) {
                running = false;
                startingPoint = true;
                mainMenu = true;
                ballXdir = -3;
                ballYdir = -4;
                setPaddleWidth(100);
                totalBricks = 21;
                setDifficulty(1);

                mainMenu();
            }
        }

        public void keyReleased(KeyEvent e) {
            int key = e.getKeyCode();
            if (key == KeyEvent.VK_RIGHT) {
                moveWithArrows(0);
            }
            if (key == KeyEvent.VK_LEFT) {
                moveWithArrows(0);
            }
        }
    }

    public class MyMouseAdapter extends MouseMotionAdapter {
        @Override
        public void mouseMoved(MouseEvent e) {
            moveWithMouse(e.getX());
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (running) {
            startingPoint = false;
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    ActionListener listener = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
        }
    };

    private class CloseListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.exit(0);
        }
    }

    public void setTotalBricks(int totalBricks) {
        this.totalBricks = totalBricks;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public static int getDifficulty() {
        return difficulty;
    }

    public void toggleDifficulty() {
        ButtonGroup btnGroup = new ButtonGroup();
        String text = "Easy";
        String text1 = "Medium";
        String text2 = "Hard";
        btnEasy = new JToggleButton(text);
        btnMedium = new JToggleButton(text1);
        btnHard = new JToggleButton(text2);
        btnEasy.addActionListener(listener);
        btnGroup.add(btnEasy);
        btnEasy.setBackground(Color.WHITE);
        btnEasy.setSelected(true);
        add(btnEasy);
        btnEasy.setBounds(SCREEN_WIDTH / 2 - 120, SCREEN_HEIGHT / 2 - 60, 80, 30);
        btnMedium.addActionListener(listener);
        btnGroup.add(btnMedium);
        btnMedium.setOpaque(true);
        btnMedium.setBorderPainted(false);
        btnMedium.setBackground(Color.ORANGE);
        add(btnMedium);
        btnMedium.setBounds(SCREEN_WIDTH / 2 - 40, SCREEN_HEIGHT / 2 - 60, 80, 30);
        btnHard.addActionListener(listener);
        btnGroup.add(btnHard);
        btnHard.setBackground(Color.RED);
        add(btnHard);
        btnHard.setBounds(SCREEN_WIDTH / 2 + 40, SCREEN_HEIGHT / 2 - 60, 80, 30);


        btnEasy.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                setTotalBricks(21);
                setDifficulty(1);
                setPaddleWidth(100);
            }
        });
        btnMedium.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                setTotalBricks(42);
                setDifficulty(2);
                setPaddleWidth(80);
            }
        });
        btnHard.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                setTotalBricks(63);
                setDifficulty(3);
                setPaddleWidth(60);
            }
        });
    }

    public void gameControlsAndDifficulty() {
        toggleDifficulty();
        ButtonGroup btnGroup = new ButtonGroup();
        String text1 = "Mouse";
        String text2 = "Keyboard";
        btnMouse = new JToggleButton(text1);
        btnKeyboard = new JToggleButton(text2);
        btnMouse.addActionListener(listener);
        btnGroup.add(btnMouse);
        btnMouse.setOpaque(true);
        btnMouse.setBorderPainted(false);
        btnMouse.setBackground(Color.ORANGE);
        btnMouse.setSelected(true);
        add(btnMouse);
        btnMouse.setBounds(SCREEN_WIDTH / 2 - 120, SCREEN_HEIGHT / 2, 120, 30);
        btnKeyboard.addActionListener(listener);
        btnGroup.add(btnKeyboard);
        btnKeyboard.setBackground(Color.RED);
        add(btnKeyboard);
        btnKeyboard.setBounds(SCREEN_WIDTH / 2, SCREEN_HEIGHT / 2, 120, 30);

        btnMouse.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                keyboard = false;
            }
        });
        btnKeyboard.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                keyboard = true;
            }
        });
    }
}
