package net.d4rkfly3r.hourofcode.kuragigo;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

public class Animator2 {
    private final ConcurrentHashMap<String, Entity> entityList = new ConcurrentHashMap<>();
    private final JFrame jFrame;
    private final BufferedImage bufferedImage = new BufferedImage(1000, 1000, BufferedImage.TYPE_INT_ARGB);
    private final Graphics2D g = bufferedImage.createGraphics();

    public Animator2() {
        this.jFrame = new JFrame("Lingvo de Kuragigo");
        this.jFrame.setSize(1000, 1000);
        this.jFrame.setLocationRelativeTo(null);
        this.jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        final JPanel jPanel = new JPanel() {
            @Override
            public void paint(Graphics g) {
                g.clearRect(0, 0, 1000, 1000);
                g.drawImage(bufferedImage, 0, 0, 1000, 1000, null);
            }
        };

        this.jFrame.add(jPanel);
        this.jFrame.setAlwaysOnTop(true);
        this.jFrame.setVisible(true);

        this.start();
    }

    public void start() {
        new Thread(() -> {
            while (true) {
                for (final Entity entity : entityList.values()) {
                    if (entity.isVisible()) {
                        g.drawImage(entity.getBufferedImage(), entity.getX(), entity.getY(), entity.getWidth(), entity.getHeight(), null);
                    }
                }
                Animator2.this.jFrame.repaint();
            }
        }).start();
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

    public void setEntityVisibility(String name, boolean visible) {
        this.entityList.get(name).setVisible(visible);
    }

    private static class Entity implements Cloneable {
        private BufferedImage bufferedImage;
        private int x, y, rotation, width, height;
        private boolean visible = false;

        private Entity(final String asset) {
            try {
                final File input = new File(asset);
                this.bufferedImage = ImageIO.read(input);
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
