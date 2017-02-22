package kuragigo;


import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;

public class Util {
    private static final ConcurrentHashMap<String, Entity> entityList = new ConcurrentHashMap<>();
    private static final JFrame jFrame = new JFrame("Lingvo de Kuragigo");
    private static final BufferedImage bufferedImage = new BufferedImage(1000, 1000, BufferedImage.TYPE_INT_ARGB);
    private static final Graphics2D g = bufferedImage.createGraphics();

    public static void moveEntity(String name, Double x, Double y) {
        _updateEntityX(name, x.intValue());
        _updateEntityY(name, y.intValue());
        System.out.println("moveEntity: " + name + " | " + x + " | " + y);
    }

    public static void createEntity(String path, String name) {
        createEntity(path, name);
        System.out.println("createEntity: " + path + " | " + name);
    }

    public static void resizeEntity(String name, Double width, Double height) {
        _resizeEntity(name, width.intValue(), height.intValue());
        System.out.println("resizeEntity: " + name + " | " + width + " | " + height);
    }

    public static void rotateEntity(String name, Double degrees) {
        _updateEntityRotation(name, degrees.intValue());
        System.out.println("rotateEntity: " + name + " | " + degrees);
    }

    public static void wait(Double time) {
        try {
            Thread.sleep(time.intValue());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("wait: " + time);
    }

    public static void main(String[] args) {
        jFrame.setSize(1000, 1000);
        jFrame.setLocationRelativeTo(null);
        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        final JPanel jPanel = new JPanel() {
            @Override
            public void paint(Graphics g) {
                g.clearRect(0, 0, 1000, 1000);
                g.drawImage(bufferedImage, 0, 0, 1000, 1000, null);
            }
        };

        jFrame.add(jPanel);
        jFrame.setAlwaysOnTop(true);
        jFrame.setVisible(true);
        start();
        try {
            Class<?> clazz = Class.forName(args[0]);
            Method method = clazz.getDeclaredMethod("main");
            method.invoke(clazz.newInstance());
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void start() {
        new Thread(() -> {
            while (true) {
                for (final Entity entity : entityList.values()) {
                    if (entity.isVisible()) {
                        g.drawImage(entity.getBufferedImage(), entity.getX(), entity.getY(), entity.getWidth(), entity.getHeight(), null);
                    }
                }
                jFrame.repaint();
            }
        }).start();

    }

    public static void _updateEntityX(String name, int x) {
        entityList.get(name).setX(x);
        jFrame.repaint();
    }

    public static void _updateEntityY(String name, int y) {
        entityList.get(name).setY(y);
        jFrame.repaint();
    }

    public static void _updateEntityRotation(String name, int r) {
        entityList.get(name).setRotation(r);
        jFrame.repaint();
    }

    public static void _resizeEntity(String name, int width, int height) {
        entityList.get(name).setWidth(width);
        entityList.get(name).setHeight(height);
        jFrame.repaint();
    }

    public void _createEntity(String asset, String name) {
        final Entity value = new Entity(asset);
        entityList.put(name, value);
        jFrame.repaint();
    }

    public void _setEntityVisibility(String name, boolean visible) {
        entityList.get(name).setVisible(visible);
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