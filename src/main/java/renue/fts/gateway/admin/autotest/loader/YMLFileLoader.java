package renue.fts.gateway.admin.autotest.loader;


import java.io.*;

/**
 * Created by Danil on 07.07.2017.
 */
public class YMLFileLoader implements IFileLoader {

    public static final File XML_DIR = new File("C:\\Users\\Danil\\IdeaProjects\\auto-tester\\src\\main\\resources");

    @Override
    public String loadFileFromDir() throws FileNotFoundException {
        String[] ymlFiles = XML_DIR.list((dir, name) -> name.contains(".yml"));
        if(ymlFiles.length == 0)
            return "File not found";
        return "C:\\Users\\Danil\\IdeaProjects\\auto-tester\\src\\main\\resources\\"+ymlFiles[0];
    }
}
