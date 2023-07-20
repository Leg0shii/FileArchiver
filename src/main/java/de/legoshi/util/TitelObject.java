package de.legoshi.util;

import lombok.Getter;
import lombok.Setter;

import java.io.File;

@Getter
@Setter
public class TitelObject {
    
    public File file;
    public String original;
    public String w1String;
    public String w2String;
    
    public TitelObject(File file, String original) {
        this.file = file;
        this.original = original;
        this.w1String = original.substring(0, 11); //+ original.substring(original.length()-4); -> adds ending and prevents .jpg and jgw to overlap
        this.w2String = original.substring(original.length() - 12, original.length() - 4);
    }
    
}
