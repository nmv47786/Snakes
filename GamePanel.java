import java.awt.*;
import java.awt.event.*;
import java.security.KeyException;

import javax.swing.*;

import java.util.Random;


// I need to add another graphic for snake 2. and allow the sizes to be different and let the snakes interact
public class GamePanel extends JPanel implements ActionListener {
    
    //screen width and length
    static final int SCREEN_WIDTH = 600;
    static final int SCREEN_HEIGHT = 600;
    static final int UNIT_SIZE = 25; //determines size of grid spaces
    static final int GAME_UNITS = (SCREEN_HEIGHT*SCREEN_WIDTH) / UNIT_SIZE;
    static final int DELAY = 75;
    //snake 1
    final int x1[] = new int[GAME_UNITS];
    final int y1[] = new int[GAME_UNITS];
    //snake 2
    final int x2[] = new int[GAME_UNITS];
    final int y2[] = new int[GAME_UNITS];
    int snakeSize1 = 3;
    int snakeSize2 = 3;
    int applesEaten;
    int apples1;
    int apples2;
    int appleX;
    int appleY;
    char direction1 = 'R'; //snake 1 starting direction. 
    char direction2 = 'D'; //snake 2 starting direction.
    boolean running = false;
    Timer timer;
    Random random;

    GamePanel() {
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH,SCREEN_HEIGHT));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startGame();
    }

    public void startGame() {
        newApple();
        running = true;
        timer = new Timer(DELAY, this);
        timer.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        
        if (running) {
            //drawing grid
            for (int i = 0; i < SCREEN_HEIGHT/UNIT_SIZE; i++) {
                g.drawLine(i*UNIT_SIZE, 0, i*UNIT_SIZE, SCREEN_HEIGHT);
                g.drawLine(0, i*UNIT_SIZE, SCREEN_WIDTH, i*UNIT_SIZE);
            }

            //drawing apple
            g.setColor(Color.green);
            g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);

            //Drawing Snake 1
            for (int i = 0; i < snakeSize1; i++) {
                if (i == 0) {
                    g.setColor(Color.GRAY);
                    g.fillRect(x1[i], y1[i], UNIT_SIZE, UNIT_SIZE);
                }
                else {
                    g.setColor(Color.red);
                    g.fillRect(x1[i], y1[i], UNIT_SIZE, UNIT_SIZE);
                }
            }

            //Drawing Snake 2
            for (int i = 0; i < snakeSize2; i++) {
                if (i == 0) {
                    g.setColor(Color.GRAY);
                    g.fillRect(x2[i], y2[i], UNIT_SIZE, UNIT_SIZE);
                }
                else {
                    g.setColor(Color.blue);
                    g.fillRect(x2[i], y2[i], UNIT_SIZE, UNIT_SIZE);
                }
            }

        }
        else {
            gameOver(g);
        }
    }

    public void move() {
        for (int i = snakeSize1; i > 0; i--) {
            x1[i] = x1[i-1];
            y1[i] = y1[i-1];
        }

        for (int i = snakeSize2; i > 0; i--) {
            x2[i] = x2[i-1];
            y2[i] = y2[i-1];
        }

        switch(direction1) {
            case 'U':
                y1[0] = y1[0] - UNIT_SIZE;
                break;
            case 'D':
                y1[0] = y1[0] + UNIT_SIZE;
                break;
            case 'R':
                x1[0] = x1[0] + UNIT_SIZE;
                break;
            case 'L':
                x1[0] = x1[0] - UNIT_SIZE;
                break;
        }
        switch(direction2) {
            case 'U':
                y2[0] = y2[0] - UNIT_SIZE;
                break;
            case 'D':
                y2[0] = y2[0] + UNIT_SIZE;
                break;
            case 'R':
                x2[0] = x2[0] + UNIT_SIZE;
                break;
            case 'L':
                x2[0] = x2[0] - UNIT_SIZE;
                break;
        }
        

    }

    public void newApple() {
        appleX = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE))*UNIT_SIZE;
        appleY = random.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE))*UNIT_SIZE;
    }

    public void checkApple() {
        if ((x1[0] == appleX) && (y1[0] == appleY)) {
            snakeSize1 ++;
            applesEaten ++;
            apples1 ++;
            newApple();
        }
        else if ((x2[0] == appleX) && (y2[0] == appleY)) {
            snakeSize2 ++;
            applesEaten ++;
            apples2 ++;
            newApple();
        }
    }

    public void checkCollisions() {
        for (int i = snakeSize1; i > 0; i--) {
            //self collision snake 1
            if ((x1[0] == x1[i]) && (y1[0] == y1[i])) {
                running = false;
            }
        }
        for (int i = snakeSize2; i > 0; i--) {
            //self collision snake 2
            if ((x2[0] == x2[i]) && (y2[0] == y2[i])) {
                running = false;
            }
            
        }
        //check if head touches borders. Later, I want to let the snake wrap around the map
        
        if (x1[0] < 0) {
            running = false; //snake 1 touches left border
        }
        if (x1[0] > SCREEN_WIDTH) {
            running = false; //snake 1 touches right border
        }
        if (y1[0] < 0) {
            running = false; //snake 1 touches top border
        }
        if (y1[0] > SCREEN_HEIGHT) {
            running = false; //snake 1 touches bottom border
        }
        
        if (x2[0] < 0) {
            running = false; //snake 2 touches left border
        }       
        if (x2[0] > SCREEN_WIDTH) {
            running = false; //snake 2 touches right border
        }        
        if (y2[0] < 0) {
            running = false; //snake 2 touches top border
        }
        if (y2[0] > SCREEN_HEIGHT) {
            running = false; //snake 2 touches bottom border
        }
        

        if (!running) {
            timer.stop();
        }
        
    }

    public void gameOver(Graphics g) {
        g.setColor(Color.red);
        g.setFont(new Font ("Ink Free",Font.BOLD, 75));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("Game Over", (SCREEN_WIDTH - metrics.stringWidth("Game Over")) / 2, SCREEN_HEIGHT / 2);
    }

    //@Override
    public void actionPerformed(ActionEvent e) {

        if (running) {
            move();
            checkApple();
            checkCollisions();
        }
        repaint();
    }

    public class MyKeyAdapter extends KeyAdapter {
        //@Override
        public void keyPressed(KeyEvent e) {
            switch(e.getKeyCode()) {
                //Snake 1 uses WASD
                case KeyEvent.VK_A:
                    if (direction1 != 'R') {
                        direction1 = 'L';
                    }
                    break;
                case KeyEvent.VK_D:
                    if (direction1 != 'L') {
                        direction1 = 'R';
                    }
                    break;
                case KeyEvent.VK_W:
                    if (direction1 != 'D') {
                        direction1 = 'U';
                    }
                    break;
                case KeyEvent.VK_S:
                    if (direction1 != 'U') {
                        direction1 = 'D';
                    }
                    break;
                //Snake 2 uses Arrow keys
                case KeyEvent.VK_LEFT:
                    if (direction2 != 'R') {
                        direction2 = 'L';
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if (direction2 != 'L') {
                        direction2 = 'R';
                    }
                    break;
                case KeyEvent.VK_UP:
                    if (direction2 != 'D') {
                        direction2 = 'U';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if (direction2 != 'U') {
                        direction2 = 'D';
                    }
                    break;
            }
        }
    }
}
