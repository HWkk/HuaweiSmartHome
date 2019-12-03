package utils;

import java.io.*;

public class FileUtils {

    public static void saveToFile(String dataPath, Object object) {
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        long start = System.currentTimeMillis();
        try {
            fos = new FileOutputStream(dataPath);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(object);
            System.out.println("Save model to file done. Spends " + (System.currentTimeMillis() - start) + "ms");
        } catch (FileNotFoundException e) {
            System.out.println("file " + dataPath + " not found when saving model to file");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("oos error when saving model to file");
            e.printStackTrace();
        } finally {
            try {
                oos.close();
                fos.close();
                System.out.println("close fos and oos when saving model to file");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static Object readFromFile(String dataPath) {
        FileInputStream fis = null;
        ObjectInputStream dis = null;
        try {
            long start = System.currentTimeMillis();
            fis = new FileInputStream(dataPath);
            dis = new ObjectInputStream(fis);
            System.out.println("Read model done. Spends " + (System.currentTimeMillis() - start) + "ms");
            return dis.readObject();
        } catch (FileNotFoundException e) {
            System.out.println("file " + dataPath + " not found when reading model.");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.out.println("classNotFound when reading model");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("oos error when reading model");
            e.printStackTrace();
        }
        return null;
    }
}
