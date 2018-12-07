package org.minhvu.operationrebound.essentials;

import org.minhvu.operationrebound.entity.Entity;

import java.awt.*;

public abstract class HealthBar {
    private static final int WIDTH = 32;
    private static final int HEIGHT = 5;
    private static final int BORDER = 0;

    public static void paint(Graphics2D g2d, Entity entity) {
        g2d.setColor(Color.GRAY);
        g2d.fillRect(entity.getCenter().x - WIDTH / 2 - BORDER / 2, entity.getLocation().y + (int) entity.getBounds().getHeight() - BORDER / 2, WIDTH + BORDER, HEIGHT + BORDER);

        if (entity.getHealth() > (int) (entity.getMaxHealth() * 0.75)) {
            g2d.setColor(Color.GREEN);
        } else if (entity.getHealth() > (int) (entity.getMaxHealth() * 0.5)) {
            g2d.setColor(Color.YELLOW);
        } else if (entity.getHealth() > (int) (entity.getMaxHealth() * 0.25)) {
            g2d.setColor(Color.ORANGE);
        } else {
            g2d.setColor(Color.RED);
        }

        g2d.fillRect(entity.getCenter().x - WIDTH / 2, entity.getLocation().y + (int) entity.getBounds().getHeight(), (int) (entity.getHealth() / entity.getMaxHealth() * WIDTH), HEIGHT);
    }
}