package cm.dasp.antibotultra.utils;

import cm.dasp.antibotultra.AntiBotUltra;
import org.bukkit.Bukkit;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ZipUtils {
    private static final AntiBotUltra ANTI_BOT_ULTRA;

    /*
     * Using injection to get the instance of the plugin
     */
    static {
        ANTI_BOT_ULTRA = (AntiBotUltra) Bukkit.getServer().getPluginManager().getPlugin("AntiBot-Ultra");
    }

    /**
     * @param zipFilePath name & path of the zip
     * @param destDir     destination directory
     * @throws IOException
     */
    public static void unzipFile(String zipFilePath, String destDir) throws IOException {
        File dir = new File(destDir);
        /*
          create output directory if it doesn't exist
          it's not really necessary as the plugin itself
          creates the directory for it but i'm implementing
          it any ways, for future usages such like
          separating files and folders by hierarchy
         */
        if (!dir.exists()) {
            dir.mkdirs();
        }
        FileInputStream fileInputStream;
        /*
            buffer for read and write data to file
         */
        byte[] buffer = new byte[1024];
        fileInputStream = new FileInputStream(zipFilePath);
        ZipInputStream zipInputStream = new ZipInputStream(fileInputStream);
        ZipEntry zipEntry = zipInputStream.getNextEntry();
        while (zipEntry != null) {
            String fileName = zipEntry.getName();
            File newFile = new File(destDir + File.separator + fileName);
            ANTI_BOT_ULTRA.getLogger().log(Level.INFO, "Unzipping to " + newFile.getAbsolutePath());
            /*
                create directories for sub directories in zip
             */
            new File(newFile.getParent()).mkdirs();
            FileOutputStream fileOutputStream = new FileOutputStream(newFile);
            int len;
            while ((len = zipInputStream.read(buffer)) > 0) {
                fileOutputStream.write(buffer, 0, len);
            }
            fileOutputStream.close();
            /*
            close this zipEntry
             */
            zipInputStream.closeEntry();
            zipEntry = zipInputStream.getNextEntry();
        }
        /*
        close last zipEntry
         */
        zipInputStream.closeEntry();
        zipInputStream.close();
        fileInputStream.close();
    }

}
