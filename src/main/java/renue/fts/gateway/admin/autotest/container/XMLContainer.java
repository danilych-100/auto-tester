package renue.fts.gateway.admin.autotest.container;


import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Danil on 07.07.2017.
 */
public final class XMLContainer {

    private static final Map<String,File> XML_FILES = new HashMap<>();

    private XMLContainer(){}

    public static Map<String, File> getXmlFilesContainer() {
        return XML_FILES;
    }
}
