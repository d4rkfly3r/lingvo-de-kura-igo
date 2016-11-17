package net.d4rkfly3r.hourofcode.kuragigo;

import javax.imageio.*;
import javax.imageio.metadata.IIOInvalidTreeException;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataNode;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Random;

public abstract class OutputAnimation {

    public static void generate(final String output, final int delayInMilliseconds, final boolean loop, final BufferedImage... images) throws IOException {
        final ImageWriter gifWriter = getWriter();
        try (final ImageOutputStream ios = createImageOutputStream(output)) {
            final IIOMetadata metadata = getMetadata(gifWriter, delayInMilliseconds, loop);

            gifWriter.setOutput(ios);
            gifWriter.prepareWriteSequence(null);
            for (final BufferedImage img : images) {
                IIOImage temp = new IIOImage(img, null, metadata);
                gifWriter.writeToSequence(temp, null);
            }
            gifWriter.endWriteSequence();
        }
    }

    private static ImageWriter getWriter() throws IIOException {
        final Iterator<ImageWriter> itr = ImageIO.getImageWritersByFormatName("gif");
        if (itr.hasNext())
            return itr.next();

        throw new IIOException("GIF writer doesn't exist on this JVM!");
    }

    private static ImageOutputStream createImageOutputStream(final String output) throws IOException {
        final File outfile = new File(output);
        return ImageIO.createImageOutputStream(outfile);
    }

    private static IIOMetadata getMetadata(final ImageWriter writer, final int delayInMilliseconds, final boolean loop) throws IIOInvalidTreeException {
        final ImageTypeSpecifier img_type = ImageTypeSpecifier.createFromBufferedImageType(BufferedImage.TYPE_INT_RGB);
        final IIOMetadata metadata = writer.getDefaultImageMetadata(img_type, null);
        final String native_format = metadata.getNativeMetadataFormatName();
        final IIOMetadataNode node_tree = (IIOMetadataNode) metadata.getAsTree(native_format);

        final IIOMetadataNode graphics_node = getOrCreateNode("GraphicControlExtension", node_tree);
        graphics_node.setAttribute("delayTime", String.valueOf(delayInMilliseconds));
        graphics_node.setAttribute("disposalMethod", "none");
        graphics_node.setAttribute("userInputFlag", "FALSE");

        if (loop) {
            makeLoopable(node_tree);
        }

        metadata.setFromTree(native_format, node_tree);

        return metadata;
    }

    private static void makeLoopable(final IIOMetadataNode root) {
        final IIOMetadataNode app_extensions = getOrCreateNode("ApplicationExtensions", root);
        final IIOMetadataNode app_node = getOrCreateNode("ApplicationExtension", app_extensions);

        app_node.setAttribute("applicationID", "NETSCAPE");
        app_node.setAttribute("authenticationCode", "2.0");
        app_node.setUserObject(new byte[]{1, 0, 0});

        app_extensions.appendChild(app_node);
        root.appendChild(app_extensions);
    }

    private static IIOMetadataNode getOrCreateNode(final String nodeName, final IIOMetadataNode root) {

        for (int i = 0; i < root.getLength(); i++) {
            if (root.item(i).getNodeName().compareToIgnoreCase(nodeName) == 0) {
                return (IIOMetadataNode) root.item(i);
            }
        }

        final IIOMetadataNode node = new IIOMetadataNode(nodeName);
        root.appendChild(node);

        return node;
    }

    //HOLY BEEP IT WORKS!!!
    public static void main(String[] args) throws IOException {
        final int length = 10;
        final int imageSize = 1000;
        final Random random = new Random();
        final BufferedImage[] bufferedImages = new BufferedImage[length];
        for (int i = 0; i < length; i++) {
            System.out.println("Generating image #" + i);
            bufferedImages[i] = new BufferedImage(imageSize, imageSize, BufferedImage.TYPE_INT_RGB);
            for (int x = 0; x < imageSize; x++) {
                for (int y = 0; y < imageSize; y++) {
                    bufferedImages[i].setRGB(x, y, random.nextInt(0xFF0000));
                }
            }
        }

        generate("test.gif", 100, false, bufferedImages);
    }
}