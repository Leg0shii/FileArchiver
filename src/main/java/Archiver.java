import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;

public class Archiver {

      public double dirCount;
      public double dirChecked;
      public JProgressBar progressBar;
      public JLabel label;

      public Archiver() {
            dirCount = 0;
            dirChecked = 1;
      }

      public void startCheck(File startFile, JProgressBar jprogressBar, JLabel jlabel) throws IOException, InterruptedException {
            this.progressBar = jprogressBar;
            this.label = jlabel;
            Thread.sleep(1000);
            progressBar.setValue(10);
            File[] files = startFile.listFiles();
            assert files != null;
            ArrayList<File> directories = getDirectories(files);
            for (File dir : directories) {
                  ArrayList<TitelObject> titelObjectArrayList = getTitelObjects(dir);
                  if (!titelObjectArrayList.isEmpty()) {
                        File archiv = new File(dir.getAbsolutePath() + "/Archiv");
                        if (!archiv.exists()) archiv.mkdir();
                        checkForDuplicates(titelObjectArrayList, archiv.toPath());
                  }
            }
            this.label.setText("DONE");
      }

      //returns a list of all sub folders
      public ArrayList<File> getDirectories(File[] files) {
            ArrayList<File> fileArrayList = new ArrayList<>();
            for (File file : files) if (file.isDirectory()) fileArrayList.add(file);
            this.dirCount = fileArrayList.size();
            return fileArrayList;
      }

      //returns all files with the endings of .ecw .jgw .jpg .asc .las .dgn
      public ArrayList<TitelObject> getTitelObjects(File directory) {
            File[] files = directory.listFiles();
            ArrayList<TitelObject> titelObjectArrayList = new ArrayList<>();
            for (File file : files) {
                  if (file.getName().length() > 3) {
                        String endingName = file.getName().substring(file.getName().length() - 4);
                        if (endingName.equals(".ecw") || endingName.equals(".jgw") || endingName.equals(".jpg")
                                || endingName.equals(".asc") || endingName.equals(".las") || endingName.equals(".dgn")) {
                              titelObjectArrayList.add(new TitelObject(file, file.getName()));
                              this.label.setText("Adding Files: " + file.getName());
                        }
                  }
            }
            return titelObjectArrayList;
      }

      //checks for duplicates
      public void checkForDuplicates(ArrayList<TitelObject> titelObjectArrayList, Path archivpath) throws IOException {
            titelObjectArrayList.sort(Comparator.comparing(TitelObject::getW1String).thenComparing(TitelObject::getW2String));
            String first, second;
            Logger logger = new Logger();
            logger.createLogFile(archivpath.toString());
            for (int i = 0; i < titelObjectArrayList.size() - 1; i++) {
                  TitelObject t1 = titelObjectArrayList.get(i);
                  TitelObject t2 = titelObjectArrayList.get(i + 1);
                  first = t1.getW1String();
                  second = t2.getW1String();
                  if (first.equals(second)) moveFile(t1, t2, archivpath);
            }
            this.dirChecked = 1 + this.dirChecked;
            this.progressBar.setValue((int)((this.dirChecked/this.dirCount)*100));
      }

      public void moveFile(TitelObject t1, TitelObject t2, Path archivpath) throws IOException {
            int date1 = Integer.parseInt(t1.getW2String());
            int date2 = Integer.parseInt(t2.getW2String());
            Logger logger = new Logger();
            if (date1 < date2) {
                  Path newLocation = Path.of(archivpath+"/"+t1.getOriginal());
                  Files.move(t1.getFile().toPath(), newLocation);
                  logger.writeToLogFile("Moved " + t1.getFile().toString() + " to " + newLocation, archivpath.toString());

            } else {
                  Path newLocation = Path.of(archivpath+"/"+t2.getOriginal());
                  Files.move(t2.getFile().toPath(), newLocation);
                  logger.writeToLogFile("Moved " + t2.getFile().toString() + " to " + newLocation, archivpath.toString());
            }
      }
}
