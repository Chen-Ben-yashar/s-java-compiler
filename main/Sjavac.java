package oop.ex6.main;

import jdk.nashorn.internal.runtime.regexp.joni.Regex;
import oop.ex6.RegexStatements;
import oop.ex6.exceptions.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;


public class Sjavac {

    private static final int FILE_PATH_ARG_INDEX = 0;
    private static final int VALID_CODE = 0;
    private static String sjavaFilePath;
    private static String[] sjavaFileLinesArray;
    private static JavaParser sjavaParser;

    /**
     * Function receives a file path and creates a Java Parser Object, that verifies the code.
     *
     * @param args File destination.
     */
    public static void main(String[] args) {
        try {
            try {
                sjavaFilePath = args[FILE_PATH_ARG_INDEX];

            }
            catch (ArrayIndexOutOfBoundsException ex) {
                throw new SjavaIOException(SjavaIOException.NO_FILE_PATH);
            }

            createSjavaLinesArray();
            sjavaParser = new JavaParser(sjavaFileLinesArray);
            sjavaParser.javaCodeCheck();
            System.out.println(VALID_CODE);
        }

        catch (SjavaException exception) {

            System.out.println(exception.getMessage());

        }


    }


    /**
     * Function creates an array of all lines of a given file.
     *
     * @throws SjavaIOException IO problem Exception.
     */
    private static void createSjavaLinesArray() throws SjavaIOException {
        File sjavaFile = new File(sjavaFilePath);
        LinkedList<String> sjavaFileStringList = new LinkedList<String>();
        FileReader input;
        BufferedReader bufRead;
        String myLine;
        RegexStatements regexStatement = new RegexStatements();

        try {
            input = new FileReader(sjavaFile);
            bufRead = new BufferedReader(input);
            while ((myLine = bufRead.readLine()) != null) {

                if (!(regexStatement.checkCommentLine(myLine) || regexStatement.checkEmptyLine(myLine))) {
                    sjavaFileStringList.add(myLine);
                }

            }
            sjavaFileLinesArray = sjavaFileStringList.toArray(new String[sjavaFileStringList.size()]);
        }
        catch (IOException ioExceptionThrown) {
            throw new SjavaIOException();

        }


    }
}
