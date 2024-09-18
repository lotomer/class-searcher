package org.tomstools.utils.classsearcher.shell;

import org.tomstools.utils.classsearcher.ClassSearcher;

import java.io.IOException;
import java.util.List;


public class ConsoleClassSearcher {
    public static final int MAX_CLASS_NAME_LENGTH = 2048;
    public static final int MAX_DIRECTORY_LENGTH = 4096;

    public static void main(String[] args) throws IOException {
        System.out.println("输入待搜索的完整类名：如org.junit.Before或org/junit/Before");
        byte[] btClassName = new byte[2048];
        int classNameLength = System.in.read(btClassName);
        System.out.println("请输入完整目录名：如D:\\");
        byte[] btDirectory = new byte[4096];
        int dirLength = System.in.read(btDirectory);

        String className = new String(btClassName, classNameLength);
        className = trimRight(className);

        String directory = new String(btDirectory, dirLength);
        directory = trimRight(directory);

        List<String> fileNames = ClassSearcher.getInstance().search(directory, className);

        System.out.println("===========================");
        System.out.println("搜索结果：");
        for (String fileName : fileNames) {
            System.out.println(fileName);
        }
    }


    private static String trimRight(String value) {
        int index = value.indexOf("\r");
        if (-1 == index) {

            index = value.indexOf("\n");
            if (-1 == index) {
                index = 2048;
            }
        }
        return value.substring(0, index);
    }
}
