package org.minhvu.operationrebound;

import java.awt.*;
import java.awt.geom.AffineTransform;

public class Enemy extends Entity {
    public static Enemy recent;

    private Point center;
    private double damage;
    private long damageTimer;
    private int damageTime;
    private boolean alive;

    public Enemy() {
        image = Game.getInstance().getChararcters().getSprite(424, 0, 35, 43);
        location = new Position(0.0, 0.0);
        speed = 2;
        maxHealth = 100;
        health = maxHealth;

        center = new Point(15, 22);
        damage = 10;
        damageTimer = System.currentTimeMillis();
        damageTime = 500;
        alive = true;
    }

    public void update() {
        Player player = Game.getInstance().getPlayer();
        double angle = Math.atan2(player.getCenter().y - getCenter().y, player.getCenter().x - getCenter().x);

        if (location.x + speed * Math.cos(angle) < Game.getInstance().getWidth() - image.getWidth(Game.getInstance()) && location.x + speed * Math.cos(angle) > 0) {
            location.x += speed * Math.cos(angle);

            if (hasCollision()) {
                location.x -= speed * Math.cos(angle);
            }
        }

        if (location.y + speed * Math.sin(angle) < Game.getInstance().getHeight() - image.getHeight(Game.getInstance()) && location.y + speed * Math.sin(angle) > 0) {
            location.y += speed * Math.sin(angle);

            if (hasCollision()) {
                location.y -= speed * Math.sin(angle);
            }
        }

        if (getBounds().intersects(player.getBounds())) {
            if (System.currentTimeMillis() - damageTimer > damageTime) {
                damageTimer = System.currentTimeMillis();
                player.damage(damage);
            }
        }
    }

    public void paint(Graphics2D g2d, Player player) {
        if (recent != null) {
            Healthbar.paint(g2d, recent);
        }

        AffineTransform transform = g2d.getTransform();

        double angle = Math.atan2(player.getCenter().y - getCenter().y, player.getCenter().x - getCenter().x);

        g2d.rotate(angle, getCenter().x, getCenter().y);
        g2d.drawImage(image, location.getX(), location.getY(), Game.getInstance());
        g2d.setTransform(transform);
    }

    @Override
    public void damage(double damage) {
        health -= damage;

        if (health <= 0) {
            alive = false;
        }
    }

    @Override
    public Point getCenter() {
        return new Point(location.getX() + center.x, location.getY() + center.y);
    }

    public boolean isAlive() {
        return alive;
    }
}
