package renue.fts.gateway.admin.autotest.loader;

import renue.fts.gateway.admin.autotest.container.XMLContainer;

import java.io.*;
import java.util.Map;

/**
 * Created by Danil on 07.07.2017.
 */
public class XMLFileLoader implements IFileLoader {

    public static final File XML_DIR = new File("C:\\Users\\Danil\\Desktop\\xmls");

    private Map<String,File> xmlContainer = XMLContainer.getXmlFilesContainer();

    @Override
    public void loadFileFromDir(final File dir) throws FileNotFoundException {

        File[] xmlFiles = dir.listFiles();


    }
}
