import java.io.File;

public class TitelObject {

      public File file;
      public String original;
      public String w1String;
      public String w2String;

      public TitelObject(File file, String original) {

            this.file = file;
            this.original = original;
            this.w1String = original.substring(0,11); //+ original.substring(original.length()-4); -> adds ending and prevents .jpg and jgw to overlap
            this.w2String = original.substring(original.length()-12, original.length()-4);

      }

      public File getFile() {
            return file;
      }

      public void setFile(File file) {
            this.file = file;
      }

      public String getOriginal() {
            return original;
      }

      public void setOriginal(String original) {
            this.original = original;
      }

      public String getW1String() {
            return w1String;
      }

      public void setW1String(String w1String) {
            this.w1String = w1String;
      }

      public String getW2String() {
            return w2String;
      }

      public void setW2String(String w2String) {
            this.w2String = w2String;
      }

}
