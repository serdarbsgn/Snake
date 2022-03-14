package com.serdarbsgn;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Arrays;
import java.util.Stack;

public class Game extends JPanel implements ActionListener,KeyListener {

    int resolutionX,resolutionY;
    int[] snakeCoordinates;
    boolean startGame = false;
    int baitX,baitY;
    int snakeSize = 1;
    Stack<int[]> snakeStack = new Stack<>();
    Direction direction;
    private final Timer timer;

    Game(int width, int height) {
        resolutionX=width;
        resolutionY=height;
        snakeCoordinates = new int[]{resolutionX/2, resolutionY/2};
        snakeStack.addElement(new int[]{snakeCoordinates[0] ,snakeCoordinates[1] });
        setBaitCoordinates();
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        int delay = 5;
        timer = new Timer(delay,this);
        timer.start();
    }

    public void setBaitCoordinates()
    {
        baitX = ((int)(Math.random() * (resolutionX-40)) + 20)/10*10;
        baitY = ((int)(Math.random() * (resolutionY-40)) + 20)/10*10;
    }

    public int[] normalizeCoordinates(int normX, int normY){
        if(normX%10>6)
        {
            normX= ((normX/10)+1)*10;
        }
        else normX = normX/10*10;
        if(normX%10>6)
        {
            normY= ((normY/10)+1)*10;
        }
        else normY = normY/10*10;

        return new int[]{normX, normY};
    }

    public void paint(Graphics g){
        requestFocus(true);

        g.setColor(Color.RED);
        g.fillRect(0,0,resolutionX,resolutionY);
        g.setColor(Color.GREEN);
        g.fillRect(20,20,resolutionX-40,resolutionY-40);
        g.setColor(Color.BLUE);
        snakeStack.forEach(e-> g.fillOval(e[0],e[1],10,10));
        g.setColor(new Color(128,0,128));
        g.fillRect(baitX,baitY,10,10);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        timer.start();
        if(startGame)
        {
            if(baitX==snakeCoordinates[0] && baitY == snakeCoordinates[1])
            {
                snakeSize+=1;
                setBaitCoordinates();
            }
            if(direction==Direction.UP && snakeCoordinates[1] > 20)
            {
                snakeCoordinates[1]-=2;
            }
            if(direction==Direction.DOWN && snakeCoordinates[1] < resolutionY-30)
            {
                snakeCoordinates[1]+=2;
            }
            if(direction==Direction.LEFT && snakeCoordinates[0] > 20)
            {
                snakeCoordinates[0]-=2;
            }
            if(direction==Direction.RIGHT && snakeCoordinates[0] < resolutionX-30 )
            {
                snakeCoordinates[0]+=2;
            }

            if(snakeStack.stream().anyMatch(c-> (Arrays.equals(c, new int[]{snakeCoordinates[0], snakeCoordinates[1]}))))
            {
                System.out.println("You died");
            }

            if(snakeStack.size()>snakeSize*10)
            {
                snakeStack.removeElementAt(0);
            }
            snakeStack.addElement(new int[]{snakeCoordinates[0] ,snakeCoordinates[1] });
            repaint();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

        if ((e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W) && direction != Direction.DOWN && direction != Direction.UP) {
            direction = Direction.UP;
            snakeCoordinates = normalizeCoordinates(snakeCoordinates[0], snakeCoordinates[1]);
        }
        if ((e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S) && direction != Direction.DOWN && direction != Direction.UP) {
            direction = Direction.DOWN;
            snakeCoordinates = normalizeCoordinates(snakeCoordinates[0], snakeCoordinates[1]);
        }
        if ((e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) && direction != Direction.LEFT && direction != Direction.RIGHT) {
            direction = Direction.RIGHT;
            snakeCoordinates = normalizeCoordinates(snakeCoordinates[0], snakeCoordinates[1]);
        }
        if ((e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) && direction != Direction.LEFT && direction != Direction.RIGHT) {
            direction = Direction.LEFT;
            snakeCoordinates = normalizeCoordinates(snakeCoordinates[0], snakeCoordinates[1]);
        }

        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            startGame = true;
            direction = Direction.RIGHT;
            snakeCoordinates = normalizeCoordinates(snakeCoordinates[0], snakeCoordinates[1]);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}

enum Direction {
    UP, DOWN, LEFT, RIGHT
}

