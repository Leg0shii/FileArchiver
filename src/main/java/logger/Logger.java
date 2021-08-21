package logger;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;

public class Logger {

      String path;

      public void createLogFile(String path) throws IOException {

            String date = 2021 + "_" + LocalTime.now().toString();
            this.path = path + "/" + date + ".txt";
            File logfile = new File(path + "/" + date + ".txt");
            if (!logfile.createNewFile()) System.out.println(path + "/" + date + ".txt couldn't be created!");
      }

      public void writeToLogFile(String message) throws FileNotFoundException, UnsupportedEncodingException {

            PrintWriter writer = new PrintWriter(path, "UTF-8");
            String date = LocalDate.now().toString();
            writer.println(date + ": " +message);
            writer.close();
      }

}
