package com.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class ParkourGame extends JPanel implements ActionListener, KeyListener {
    private final Timer timer;
    private final int groundY = 250;
    private Rectangle player;
    private ArrayList<Rectangle> obstacles;
    private boolean jump;
    private int velocityY;
    private final int gravity = 1;
    private Random random;

    public ParkourGame() {
        setPreferredSize(new Dimension(400, 300));
        setBackground(Color.WHITE);
        timer = new Timer(20, this);
        timer.start();
        addKeyListener(this);
        setFocusable(true);
        initGame();
    }

    private void initGame() {
        player = new Rectangle(50, groundY - 30, 30, 30);
        obstacles = new ArrayList<>();
        random = new Random();
        jump = false;
        velocityY = 0;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (random.nextInt(100) < 2) {
            obstacles.add(new Rectangle(400, groundY - 20, 20, 20));
        }

        Iterator<Rectangle> it = obstacles.iterator();
        while (it.hasNext()) {
            Rectangle ob = it.next();
            ob.x -= 5;
            if (ob.x + ob.width < 0) it.remove();
            if (player.intersects(ob)) {
                timer.stop();
            }
        }

        if (jump) {
            velocityY -= gravity;
            player.y -= velocityY;
            if (player.y >= groundY - player.height) {
                player.y = groundY - player.height;
                jump = false;
                velocityY = 0;
            }
        }

        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.BLACK);
        g.fillRect(0, groundY, getWidth(), 5);
        g.setColor(Color.BLUE);
        g.fillRect(player.x, player.y, player.width, player.height);
        g.setColor(Color.RED);
        for (Rectangle ob : obstacles) {
            g.fillRect(ob.x, ob.y, ob.width, ob.height);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE && !jump) {
            jump = true;
            velocityY = 15;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}

    public static void main(String[] args) {
        JFrame frame = new JFrame("Parkour Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(new ParkourGame());
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
