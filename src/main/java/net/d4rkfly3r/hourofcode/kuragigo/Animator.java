package net.d4rkfly3r.hourofcode.kuragigo;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class Animator {
    private final HashMap<String, Entity> entityList = new HashMap<>();
    private final JFrame jFrame;

    public Animator() {
        this.jFrame = new JFrame("Lingvo de Kuragigo");
        this.jFrame.setSize(1000, 1000);
        this.jFrame.setLocationRelativeTo(null);
        this.jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        final JPanel jPanel = new JPanel() {
            @Override
            public void paint(Graphics g) {
                super.paint(g);
                Animator.this.entityList.values().stream().filter(Entity::isVisible).forEach(entity -> g.drawImage(entity.getBufferedImage(), entity.getX(), entity.getY(), entity.getWidth(), entity.getHeight(), null));
            }
        };

        this.jFrame.add(jPanel);
        this.jFrame.setAlwaysOnTop(true);
        this.jFrame.setVisible(true);
    }

    public void close() {
        this.jFrame.setVisible(false);
        this.jFrame.dispose();
    }

    public void createEntity(String asset, String name) {
        final Entity value = new Entity(asset);
        this.entityList.put(name, value);
        this.jFrame.repaint();
    }

    public void updateEntityX(String name, int x) {
        this.entityList.get(name).setX(x);
        this.jFrame.repaint();
    }

    public void updateEntityY(String name, int y) {
        this.entityList.get(name).setY(y);
        this.jFrame.repaint();
    }

    public void updateEntityRotation(String name, int r) {
        this.entityList.get(name).setRotation(r);
        this.jFrame.repaint();
    }

    public void resizeEntity(String name, int width, int height) {
        this.entityList.get(name).setWidth(width);
        this.entityList.get(name).setHeight(height);
        this.jFrame.repaint();
    }

    private static class Entity {
        private BufferedImage bufferedImage;
        private int x, y, rotation, width, height;
        private boolean visible = true;

        private Entity(final String asset) {
            try {
                this.bufferedImage = ImageIO.read(new File(asset));
                this.width = this.bufferedImage.getWidth();
                this.height = this.bufferedImage.getHeight();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        public int getWidth() {
            return this.width;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public int getHeight() {
            return this.height;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public boolean isVisible() {
            return this.visible;
        }

        public void setVisible(boolean visible) {
            this.visible = visible;
        }

        public BufferedImage getBufferedImage() {
            return this.bufferedImage;
        }

        public void setBufferedImage(BufferedImage bufferedImage) {
            this.bufferedImage = bufferedImage;
        }

        public int getX() {
            return this.x;
        }

        public void setX(int x) {
            this.x = x;
        }

        public int getY() {
            return this.y;
        }

        public void setY(int y) {
            this.y = y;
        }

        public int getRotation() {
            return this.rotation;
        }

        public void setRotation(int rotation) {
            this.rotation = rotation;
        }
    }
}
