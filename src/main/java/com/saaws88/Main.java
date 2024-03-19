package com.saaws88;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.IOException;
import java.util.List;

public class Main {

  public static void main(String[] args) throws IOException {

    XlsParser p = new XlsParser();

    long startTime = System.nanoTime();
    HSSFWorkbook wbIn = p.readWorkbook(args[0]);
    List<HSSFRow> wrongNumbers = p.getWrongNumberRowsList(wbIn);
    HSSFWorkbook wbOut = p.writeToWorkBook(wrongNumbers);
    p.writeToFile(wbOut);
    long endTime = System.nanoTime();
    double elapsedTime = (endTime - startTime) / 1e+9;
    System.out.printf("Выполнено за %.2f сек. \n", elapsedTime);

  }

}