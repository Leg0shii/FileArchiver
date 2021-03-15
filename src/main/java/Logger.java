import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.time.LocalDate;

public class Logger {

      public void createLogFile(String path) {
            String date = LocalDate.now().toString();
            File logfile = new File(path + "/" + date + ".txt");
            if (!logfile.mkdir()) System.out.println(path + "/" + date + ".txt couldn't be created!");
      }

      public void writeToLogFile(String message, String path) throws FileNotFoundException, UnsupportedEncodingException {
            PrintWriter writer = new PrintWriter(path, "UTF-8");
            String date = LocalDate.now().toString();
            writer.println(date + ": " +message);
            writer.close();
      }

}
