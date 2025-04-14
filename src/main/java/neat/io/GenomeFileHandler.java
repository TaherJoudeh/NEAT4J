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
 * Utility class for handling genome serialization/deserialization and image saving.
 * Provides static methods to save/load NEAT genomes and export network visualizations.
 * <p>
 * This class cannot be instantiated and uses Java object serialization for genome persistence.
 * 
 * @author Taher Joudeh
 */
public final class GenomeFileHandler {

	private final static String FILE_FORMAT = "neat";
	
    /**
     * Serializes and saves a Genome object to a file in NEAT format.
     * @param genome The genome to serialize.
     * @param path Directory path for saving (can be null for current directory).
     * @param name Filename without extension.
     */
	public final static void saveGenome(Genome genome, String path, String name) {
		String filePath = path+"\\"+name+"."+FILE_FORMAT;
		if (path == null)
			filePath = name + "." + FILE_FORMAT;
		File file = new File(filePath);
		try {
			
			FileOutputStream fos = new FileOutputStream(file);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			
			oos.writeObject(genome);
			fos.close();
			oos.close();
		} catch (IOException e) { e.printStackTrace(); }
	}
	
    /**
     * Loads and deserializes a Genome from file.
     * @param filePath Full path to the genome file (including extension).
     * @return Deserialized Genome object, or null if loading fails.
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
     * Saves a network visualization image to disk.
     * @param image BufferedImage containing network visualization.
     * @param path Directory path for saving (can be null for current directory).
     * @param name Filename without extension.
     * @param format Image format (e.g., "png", "jpg") as supported by ImageIO.
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
