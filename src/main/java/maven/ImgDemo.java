package maven;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Iterator;

public class ImgDemo {
	public static String createThumbnailBase64(String base64Image, int thumbWidth, int thumbHeight,
			String outputFormat) throws Exception {

		// Remove data URI prefix if present
		if (base64Image.contains(",")) {
			base64Image = base64Image.split(",")[1];
		}

		byte[] imageBytes = Base64.getDecoder().decode(base64Image);

		BufferedImage originalImage;

		// Handle GIF separately
		if ("gif".equalsIgnoreCase(outputFormat)) {

			try (ImageInputStream stream = ImageIO.createImageInputStream(new ByteArrayInputStream(imageBytes))) {

				Iterator<ImageReader> readers = ImageIO.getImageReadersByFormatName("gif");

				if (!readers.hasNext()) {
					throw new RuntimeException("No GIF reader found");
				}

				ImageReader reader = readers.next();
				reader.setInput(stream);

				// Read first frame of GIF
				originalImage = reader.read(0);
			}

		} else {
			originalImage = ImageIO.read(new ByteArrayInputStream(imageBytes));
		}

		if (originalImage == null) {
			throw new RuntimeException("Invalid image");
		}

		// Create thumbnail
		BufferedImage thumbnail = new BufferedImage(thumbWidth, thumbHeight, BufferedImage.TYPE_INT_RGB);

		Graphics2D g2d = thumbnail.createGraphics();

		g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

		g2d.drawImage(originalImage, 0, 0, thumbWidth, thumbHeight, null);

		g2d.dispose();

		// Convert thumbnail to base64
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write(thumbnail, outputFormat, baos);

		return Base64.getEncoder().encodeToString(baos.toByteArray());
	}

	public static void main(String[] args) throws Exception {

		String inputBase64 = getImageBase64("image.png");

		String thumbnailBase64 = createThumbnailBase64(inputBase64, 256, 256, "jpg");

		System.out.println(thumbnailBase64);
		System.out.println(inputBase64.length());
		System.out.println(thumbnailBase64.length());
		System.out.println((thumbnailBase64.length()/inputBase64.length())*100);
	}

	private static String getImageBase64(String fileName) {
		try {
			byte[] fileContent = Files.readAllBytes(Paths.get(fileName));
			return Base64.getEncoder().encodeToString(fileContent);
		} catch (Exception e) {
			throw new RuntimeException("Failed to convert image to Base64: " + fileName, e);
		}
	}
}