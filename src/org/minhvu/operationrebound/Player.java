package org.minhvu.operationrebound;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;

public class Player extends Entity {
    private Sprite sprite;

    private boolean uppressed;
    private boolean downpressed;
    private boolean leftpressed;
    private boolean rightpressed;

//    private boolean mousePressed;

    private double damage;
    private int ammo;
    private int maxAmmo;
    private int reloadTime;
    private boolean reloading;

    private long regenTimer;
    private int regenTime;
    private int regen;

    public Player() {
        sprite = new Sprites().getRandomSprite();

        image = sprite.getPistolImage();

        location = new Position((Game.getInstance().getWidth() - image.getWidth(Game.getInstance())) / 2, Game.getInstance().getHeight() - 200);
        speed = 3;

        uppressed = false;
        downpressed = false;
        leftpressed = false;
        rightpressed = false;

//        mousePressed = false;

        maxHealth = 100;
        health = maxHealth;
        damage = 10;
        maxAmmo = 16;
        ammo = maxAmmo;
        reloadTime = 886;
        reloading = false;

        regenTimer = System.currentTimeMillis();
        regenTime = 500;
        regen = 1;
    }

    public void paint(Graphics2D g2d) {
        AffineTransform transform = g2d.getTransform();

        HealthBar.paint(g2d, this);

        Point mousePostion = MouseInfo.getPointerInfo().getLocation();
        double angle = Math.atan2(mousePostion.y - getCenter().y, mousePostion.x - getCenter().x);

        g2d.rotate(angle, getCenter().x, getCenter().y);
        g2d.drawImage(image, location.getX(), location.getY(), Game.getInstance());
        g2d.setTransform(transform);
    }

    public void update() {
        if (uppressed) {
            if (location.y - speed > 0) {
                location.y -= speed;
            }

            if (hasCollision()) {
                location.y += speed;
            }
        }

        if (downpressed) {
            if (location.y + speed < Game.getInstance().getHeight() - image.getHeight(Game.getInstance())) {
                location.y += speed;
            }

            if (hasCollision()) {
                location.y -= speed;
            }
        }

        if (leftpressed) {
            if (location.x - speed > 0) {
                location.x -= speed;
            }

            if (hasCollision()) {
                location.x += speed;
            }
        }

        if (rightpressed) {
            if (location.x + speed < Game.getInstance().getWidth() - image.getWidth(Game.getInstance())) {
                location.x += speed;
            }

            if (hasCollision()) {
                location.x -= speed;
            }
        }

        if (reloading) {
            image = sprite.getReloadImage();
        } else {
            image = sprite.getMachineImage();
        }

        // Update Power Ups
        for (Object object : Game.getInstance().getPowerUps()) {
            PowerUp powerup = (PowerUp) object;

            if (powerup.isAlive() && powerup.getBounds().contains(getCenter())) {
                powerup.consume(this);
            }
        }

        // Update Health
        if (health < maxHealth && System.currentTimeMillis() - regenTimer > regenTime) {
            health += regen;
            regenTimer = System.currentTimeMillis();
        }

        if (health > maxHealth) {
            health = maxHealth;
        }
    }

//    public void mousePressed(MouseEvent e) {
//        if (e.getButton() == MouseEvent.BUTTON1) {
//            mousePressed = true;
//        }
//    }

    public void mouseReleased(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1 && ammo > 0 && !reloading) {
            Point mousePostion = MouseInfo.getPointerInfo().getLocation();
            double angle = Math.atan2(mousePostion.y - getCenter().y, mousePostion.x - getCenter().x);
            Game.getInstance().getBullets().add(new Bullet(new Point((int) (getCenter().x + 24 * Math.cos(angle) + 5 * -Math.sin(angle)),
                    (int) (getCenter().y + 24 * Math.sin(angle) + 5 * Math.cos(angle))), damage, 10, 500, angle));

            if (--ammo == 0) {
                reload();
            }
        }
    }

    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_W) {
            uppressed = false;
        }

        if (e.getKeyCode() == KeyEvent.VK_S) {
            downpressed = false;
        }

        if (e.getKeyCode() == KeyEvent.VK_A) {
            leftpressed = false;
        }

        if (e.getKeyCode() == KeyEvent.VK_D) {
            rightpressed = false;
        }
    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_W) {
            uppressed = true;
        }

        if (e.getKeyCode() == KeyEvent.VK_S) {
            downpressed = true;
        }

        if (e.getKeyCode() == KeyEvent.VK_A) {
            leftpressed = true;
        }

        if (e.getKeyCode() == KeyEvent.VK_D) {
            rightpressed = true;
        }

        if (e.getKeyCode() == KeyEvent.VK_R) {
            reload();
        }
    }

    private void reload() {
        if (!reloading && ammo != maxAmmo) {
            reloading = true;
            new java.util.Timer().schedule(
                    new java.util.TimerTask() {
                        @Override
                        public void run() {
                            ammo = maxAmmo;
                            reloading = false;
                            Scoreboard.reloads++;
                        }
                    }, reloadTime
            );
        }
    }

    public void setAmmo(int ammo) {
        this.ammo = ammo;
    }

    public int getMaxAmmo() {
        return maxAmmo;
    }

    public void setMaxAmmo(int maxAmmo) {
        this.maxAmmo = maxAmmo;
    }

    public double getDamage() {
        return damage;
    }

    public void setDamage(double damage) {
        this.damage = damage;
    }

    @Override
    public void damage(double damage) {
        health -= damage;
        Scoreboard.damageRecieved += damage;

        if (health <= 0) {
            Scoreboard.output();
            System.exit(1);
        }
    }

    @Override
    public Point getCenter() {
        return new Point(location.getX() + sprite.getCenter().x, location.getY() + sprite.getCenter().y);
    }

    @Override
    protected Dimension getDimensions() {
        return new Dimension(sprite.getMachineImage().getWidth(Game.getInstance()), sprite.getMachineImage().getHeight(Game.getInstance()));
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle(location.getX(), location.getY(), getDimensions().width - 16, getDimensions().height);
    }
}

