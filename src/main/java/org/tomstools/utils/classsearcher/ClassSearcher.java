package org.tomstools.utils.classsearcher;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;


public class ClassSearcher {
    private static ClassSearcher instance = null;


    public List<String> search(String directory, String className) {
        List<String> result = new ArrayList<String>();
        File dir = new File(directory);
        doSearch(result, dir, className);

        return result;
    }


    private void doSearch(List<String> result, File path, String className) {
        if (path.isDirectory()) {


            if (isContained(path.getAbsolutePath(), className)) {
                result.add(path.getAbsolutePath());
            }


            File[] files = path.listFiles(new FileFilter() {
                public boolean accept(File pathname) {
                    return (-1 != ClassSearcher.this.checkFile(pathname));
                }
            });

            if (null == files) {
                return;
            }


            for (File file : files) {
                doSearch(result, file, className);
            }
        } else if (path.isFile()) {

            int sign = checkFile(path);
            if (1 == sign) {

                doSearchFromZip(result, path, className);
            } else if (2 == sign) {

                doSearchFromPath(result, path, className);
            }
        }
    }


    private void doSearchFromPath(List<String> result, File path, String className) {
        if (isContained(path.getAbsolutePath(), className)) {
            result.add(path.getAbsolutePath());
        }
    }


    private void doSearchFromZip(List<String> result, File file, String className) {
        try {
            ZipFile zipFile = new ZipFile(file);
            Enumeration<? extends ZipEntry> entries = zipFile.entries();
            ZipEntry entry = null;
            while (entries.hasMoreElements()) {

                entry = (ZipEntry) entries.nextElement();
                if (isContained(entry.getName(), className)) {

                    result.add(file.getAbsolutePath());

                    break;
                }
            }
        } catch (ZipException e) {


        } catch (IOException e) {
        }
    }


    boolean isContained(String pathname, String className) {
        String tmpPathName = pathname.replace("\\", "/").toLowerCase();
        String tmpClassName = className.replace(".", "/").toLowerCase();

        return tmpPathName.contains(tmpClassName);
    }


    private int checkFile(File file) {
        if (file.isDirectory() && !file.isHidden()) {
            return 0;
        }

        String fileName = file.getName().toLowerCase();
        if (fileName.endsWith(".jar") || fileName.endsWith(".zip")) {
            return 1;
        }
        if (fileName.endsWith(".class") || fileName.endsWith(".java")) {
            return 2;
        }


        return -1;
    }


    public static ClassSearcher getInstance() {
        if (null == instance) {
            instance = new ClassSearcher();
        }

        return instance;
    }
}
