package de.legoshi.util;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;

public class DirectoryPaths {
    private static final List<String> BASE_FOLDERS = Arrays.asList(
        "/01174_Laserdaten/",
        "/01173_Orthofotos/",
        "/01177_Hoehenlinien/"
    );

    private static final Map<String, List<String>> SUBFOLDERS_MAPPING;
    static {
        SUBFOLDERS_MAPPING = new HashMap<>();
        SUBFOLDERS_MAPPING.put("/01174_Laserdaten/", Arrays.asList("ASC/", "LAS/"));
        SUBFOLDERS_MAPPING.put("/01173_Orthofotos/", Arrays.asList("ECW/", "Geo_tiff/", "jpg/", "jgw/"));
    }

    public static final List<String> SEARCH_FILE_NAMES;
    static {
        SEARCH_FILE_NAMES = new ArrayList<>();
        for (String base : BASE_FOLDERS) {
            List<String> subs = SUBFOLDERS_MAPPING.get(base);
            if (subs != null) {
                for (String sub : subs) {
                    SEARCH_FILE_NAMES.add(base + sub);
                }
            }
        }
    }
    
    public static final List<String> FOLDER_NAMES = BASE_FOLDERS;
}