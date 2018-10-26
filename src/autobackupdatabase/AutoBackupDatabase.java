package autobackupdatabase;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 *
 * @author Đỗ Trung Đức
 */
public class AutoBackupDatabase {

    public static void main(String[] args) {

        final String username = "duc010298";
        final String password = "123456";
        final String database = "Clinic";
        final String backupSqlPath = "D:\\backup\\backup.sql";
        final String logFilePath = "D:/backup/log.log";

        final String command = "mysqldump.exe –e –u" + username + " -p" + password + " " + database + " > " + backupSqlPath;

        File f = new File(logFilePath);
        //if f is directory, using default path
        if(f.isDirectory()) {
            f = new File("D:/backup/log.log");
        }
        if (!f.exists()) {
            try {
                f.getParentFile().mkdirs(); 
                f.createNewFile();
            } catch (IOException ex) {
                Logger.getLogger(AutoBackupDatabase.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        Logger logger = Logger.getLogger(AutoBackupDatabase.class.getName());
        FileHandler fh;
        try {
            fh = new FileHandler(logFilePath);
            logger.addHandler(fh);
            SimpleFormatter formatter = new SimpleFormatter();
            fh.setFormatter(formatter);

            logger.info("Program stated");
        } catch (IOException | SecurityException ex) {
            Logger.getLogger(AutoBackupDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                logger.info("Run backup");
                ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", command);
                try {
                    Process p = builder.start();
                } catch (IOException ex) {
                    logger.log(Level.SEVERE, null, ex);
                }
            }
        };
        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
        service.scheduleWithFixedDelay(runnable, 0, 3, TimeUnit.HOURS);
    }

}
