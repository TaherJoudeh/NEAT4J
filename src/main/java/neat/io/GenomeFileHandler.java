package main.java.neat.io;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import javax.imageio.ImageIO;
import main.java.neat.core.Genome;

/**
 * 
 * @author Gamer
 *
 */
public final class GenomeFileHandler {

	private final static String FILE_FORMAT = "neat";
	
	/**
	 * 
	 * @param genome
	 * @param path
	 * @param name
	 */
	public final static void saveGenome(Genome genome, String path, String name) {
		String fullPath = path+"\\"+name+"."+FILE_FORMAT;
		File file = new File(fullPath);
		try {
			
			FileOutputStream fos = new FileOutputStream(file);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			
			oos.writeObject(genome);
			fos.close();
			oos.close();
		} catch (IOException e) { e.printStackTrace(); }
	}
	
	/**
	 * 
	 * @param filePath
	 * @return
	 */
	public final static Genome loadGenome(String filePath) {
		Genome genome = null;
		File file = new File(filePath);
		try {
			
			FileInputStream fis = new FileInputStream(file);
			ObjectInputStream ois = new ObjectInputStream(fis);
			
			genome = (Genome) ois.readObject();
			fis.close();
			ois.close();
		} catch (IOException | ClassNotFoundException e) { e.printStackTrace(); }
		return genome;
	}
	
	/**
	 * 
	 * @param image
	 * @param path
	 * @param name
	 * @param format
	 */
	public final static void saveImage(BufferedImage image, String path, String name, String format) {
		String filePath = path+"\\"+name+"."+format;
		if (path == null)
			filePath = name + "." + format;
		
		File file = new File(filePath);
		try {
			ImageIO.write(image,format,file);
		} catch (IOException e) { e.printStackTrace(); }
	}
	
}
