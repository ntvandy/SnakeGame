import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;

import javax.swing.JPanel;

/*
* Created by Nathan Vandegrift
*
*/

public class GamePanel extends JPanel implements ActionListener {

    static final int width = 500;   //width
    static final int height = 500;   //height
    static final int unitSize = 20;
    static final int numOfUnits = (width * height) / (unitSize * unitSize);

    //holds x/y coordinates for body parts of the snake
    final int x[] = new int[numOfUnits];
    final int y[]= new int[numOfUnits];

    //starting length of snake
    int length = 5;
    int foodEaten;
    int foodX, foodY;
    char direction = 'D';
    boolean running = false;
    Random random;
    Timer timer;

    GamePanel() {
        random = new Random();
        this.setPreferredSize(new Dimension(width, height));
        this.setBackground(new Color(75, 128, 75));
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        play();
    }

    public void play() {
        addFood();
        running = true;

        timer = new Timer(80, this);
        timer.start();
    }

    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        draw(graphics);
    }

    public void move() {
        for (int i = length; i > 0; i--) {
            //shifts snake one unit to the desired direction
            x[i] = x[i-1];
            y[i] = y[i-1];
        }

        if (direction == 'L') {         //Left
            x[0] = x[0] - unitSize;
        } else if (direction == 'R') {  //Right
            x[0] = x[0] + unitSize;
        } else if (direction == 'U') {  //Up
            y[0] = y[0] - unitSize;
        } else {                        //Down
            y[0] = y[0] + unitSize;
        }
    }

    public void checkFood() {
        if(x[0] == foodX && y[0] == foodY) {
            length++;
            foodEaten++;
            addFood();
        }
    }

    public void draw(Graphics graphics) {
        if (running) {
            graphics.setColor(Color.red); //Food
            graphics.fillOval(foodX, foodY, unitSize, unitSize);

            graphics.setColor(new Color(29, 63, 29)); //Snake Head
            graphics.fillRect(x[0], y[0], unitSize, unitSize);

            for (int i = 1; i < length; i++) {
                graphics.setColor(new Color(29, 63, 29)); //body
                graphics.fillRect(x[i], y[i], unitSize, unitSize);
            }

            graphics.setColor(Color.white);
            graphics.setFont(new Font ("Sans serif", Font.ROMAN_BASELINE, 22));
            FontMetrics metrics = getFontMetrics(graphics.getFont());
            graphics.drawString("Score: " + foodEaten, (width - metrics.stringWidth("Score: " + foodEaten)) / 2, graphics.getFont().getSize());
        } else {
            gameOver(graphics);
        }
    }

    public void addFood() {
        foodX = random.nextInt((int)(width / unitSize)) * unitSize;
        foodY = random.nextInt((int)(height / unitSize)) * unitSize;
    }

    public void checkHit() {
        //checks if head hits snake body
        for (int i = length; i > 0; i--) {
            if (x[0] == x[i] && y[0] == y[i]) {
                running = false;
            }
        }

        //checks if head hits border
        if (x[0] < 0 || x[0] > width || y[0] < 0 || y[0] > height) {
            running = false;
        }

        if(!running) {
            timer.stop();
        }
    }

    public void gameOver(Graphics graphics) {
        graphics.setColor(Color.red);
        graphics.setFont(new Font("Sans serif", Font.ROMAN_BASELINE, 50));
        FontMetrics metrics = getFontMetrics(graphics.getFont());
        graphics.drawString("GAME OVER", (width - metrics.stringWidth("GAME OVER")) / 2, height / 2);

        graphics.setColor(Color.white);
        graphics.setFont(new Font("Sans serif", Font.ROMAN_BASELINE, 25));
        metrics = getFontMetrics(graphics.getFont());
        graphics.drawString("Score: " + foodEaten, (width - metrics.stringWidth("Score: " + foodEaten)) / 2, graphics.getFont().getSize());
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
        if (running) {
            move();
            checkFood();
            checkHit();
        }
        repaint();
    }

    public class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            switch(e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    if (direction != 'R')  {
                        direction = 'L';
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if (direction != 'L')  {
                        direction = 'R';
                    }
                    break;
                case KeyEvent.VK_UP:
                    if (direction != 'D')  {
                        direction = 'U';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if (direction != 'U')  {
                        direction = 'D';
                    }
                    break;
            }
        }
    }
}