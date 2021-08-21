package panes;

import javafx.concurrent.Task;
import util.Dir;
import util.TitelObject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;

public class Archiver extends Task<Void> {

      public double dirCount;
      public double dirChecked;
      public File startFile;

      public Archiver(File startFile) {
            dirCount = 0;
            dirChecked = 1;
            this.startFile = startFile;
      }

      @Override
      protected Void call() throws Exception {

            File[] files = startFile.listFiles();
            if(files != null) {
                  ArrayList<File> directories = Dir.getDirectories(files);
                  this.dirCount = directories.size();
                  for (File dir : directories) {
                        ArrayList<TitelObject> titelObjectArrayList = getTitelObjects(dir);
                        if (!titelObjectArrayList.isEmpty()) {
                              addArchive(dir);
                              checkForDuplicates(titelObjectArrayList, Paths.get(dir.getAbsolutePath() + "/Archiv"));
                        }
                  }
                  updateMessage("DONE");
                  cancel();
            }
            return null;
      }

      private void addArchive(File file) {

            File archiv = new File(file.getAbsolutePath() + "/Archiv");
            if (!archiv.exists()) archiv.mkdir();
      }

      //returns all files with the endings of .ecw .jgw .jpg .asc .las .dgn
      public ArrayList<TitelObject> getTitelObjects(File directory) {

            File[] files = directory.listFiles();
            ArrayList<TitelObject> titelObjectArrayList = new ArrayList<>();
            System.out.println("Adding Files: " + files.length);
            for (File file : files) {
                  if (file.getName().length() > 3) {
                        String endingName = file.getName().substring(file.getName().length() - 4);
                        if (endingName.equals(".ecw") || endingName.equals(".jgw") || endingName.equals(".jpg")
                                || endingName.equals(".asc") || endingName.equals(".las") || endingName.equals(".dgn")) {
                              titelObjectArrayList.add(new TitelObject(file, file.getName()));
                              updateMessage("Adding Files: " + file.getName());
                        }
                  }
            }
            return titelObjectArrayList;
      }

      //checks for duplicates
      public void checkForDuplicates(ArrayList<TitelObject> titelObjectArrayList, Path archivpath) throws IOException {

            titelObjectArrayList.sort(Comparator.comparing(TitelObject::getW1String).thenComparing(TitelObject::getW2String));
            String first, second;
            //logger.Logger logger = new logger.Logger();
            //logger.createLogFile(archivpath.toString());
            for (int i = 0; i < titelObjectArrayList.size() - 1; i++) {
                  TitelObject t1 = titelObjectArrayList.get(i);
                  TitelObject t2 = titelObjectArrayList.get(i + 1);
                  first = t1.getW1String();
                  second = t2.getW1String();
                  if (first.equals(second)) moveFile(t1, t2, archivpath);
            }
            this.dirChecked = 1 + this.dirChecked;
            updateProgress((int)((this.dirChecked/this.dirCount)*100),100);
      }

      public void moveFile(TitelObject t1, TitelObject t2, Path archivpath) throws IOException {

            int date1 = Integer.parseInt(t1.getW2String());
            int date2 = Integer.parseInt(t2.getW2String());
            if (date1 < date2) {
                  Path newLocation = Paths.get(archivpath+"/"+t1.getOriginal());
                  Files.move(t1.getFile().toPath(), newLocation);
                  //logger.writeToLogFile("Moved " + t1.getFile().toString() + " to " + newLocation);

            } else {
                  Path newLocation = Paths.get(archivpath+"/"+t2.getOriginal());
                  Files.move(t2.getFile().toPath(), newLocation);
                  //logger.writeToLogFile("Moved " + t2.getFile().toString() + " to " + newLocation);
            }
      }
}
