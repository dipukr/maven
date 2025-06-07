package maven;

import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.io.File;
import javax.imageio.ImageIO;

public class ImageUtils {

	public static void blur() throws Exception {
		File file = new File("apple.jpg");
		BufferedImage input = ImageIO.read(file);

		// Create a Gaussian kernel (5x5 kernel)
		float[] matrix = {
			1f/273, 4f/273, 7f/273,  4f/273, 1f/273,
			4f/273, 16f/273, 26f/273,16f/273, 4f/273,
			7f/273, 26f/273, 41f/273,26f/273, 7f/273,
			4f/273, 16f/273, 26f/273,16f/273, 4f/273,
			1f/273, 4f/273, 7f/273, 4f/273, 1f/273
		};

		Kernel kernel = new Kernel(5, 5, matrix);
		ConvolveOp op = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);

		// Apply the blur
		BufferedImage blurred = op.filter(input, null);

		// Save the output image
		ImageIO.write(blurred, "jpg", new File("output.jpg"));

		System.out.println("Blur applied and saved to output.jpg");
	}
	
	public static void main(final String[] args) throws Exception {
		//for (int i = 0; i < 100; i++)
				blur();
	}
}
