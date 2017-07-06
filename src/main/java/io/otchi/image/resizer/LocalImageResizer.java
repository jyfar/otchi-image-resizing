package io.otchi.image.resizer;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;

import javax.imageio.ImageIO;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;

public class LocalImageResizer {

    private static final float MAX_WIDTH = 526;
    private static final float MAX_HEIGHT = 526;
    
	public static void main(String[] args) throws IOException {
		// Read the source image
		String imagePath = Paths.get(args[0]).toString();
		ImageValidator imageValidator = new ImageValidator();
		if(imageValidator.validate(imagePath) ==  false ){
			throw new InvalidFormatException("file extension is not an image", imagePath, File.class);
		}
		
		File imageFile  = new File(imagePath);
		String imageType =  imageValidator.getImageType(imagePath);
        BufferedImage srcImage = ImageIO.read(imageFile);
        int srcHeight = srcImage.getHeight();
        int srcWidth = srcImage.getWidth();
        // Infer the scaling factor to avoid stretching the image
        // unnaturally
        float scalingFactor = Math.min(MAX_WIDTH / srcWidth, MAX_HEIGHT
                / srcHeight);
        int width = (int) (scalingFactor * srcWidth);
        int height = (int) (scalingFactor * srcHeight);

        BufferedImage resizedImage = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_RGB);
        Graphics2D g = resizedImage.createGraphics();
        // Fill with white before applying semi-transparent (alpha) images
        g.setPaint(Color.white);
        g.fillRect(0, 0, width, height);
        // Simple bilinear resize
        // If you want higher quality algorithms, check this link:
        // https://today.java.net/pub/a/today/2007/04/03/perils-of-image-getscaledinstance.html
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.drawImage(srcImage, 0, 0, width, height, null);
        g.dispose();

        // Re-encode image to target format
        String targetImagePath = imagePath.substring(0, imagePath.lastIndexOf('.')) + "-resized" + imagePath.substring(imagePath.lastIndexOf('.'), imagePath.length());
        FileOutputStream os = new FileOutputStream(new File(targetImagePath));
        ImageIO.write(resizedImage, imageType, os);
        

	}

}
