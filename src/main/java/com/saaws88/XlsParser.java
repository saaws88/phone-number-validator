package com.saaws88;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class XlsParser {

  private final ColumnMapper indices = new ColumnMapper();
  private final Validator v = new Validator();
  int HEADER_SIZE = 1;


  public List<HSSFRow> getWrongNumberRowsList(HSSFWorkbook wb) {

    int WORKING_SHEET_INDEX = 0;

    HSSFSheet sheet = wb.getSheetAt(WORKING_SHEET_INDEX);
    Iterator<Row> rowIter = sheet.rowIterator();
    List<HSSFRow> wrongNumbersRowsList = new ArrayList<>();
    int count = 0;

    while (rowIter.hasNext()) {

      count++;
      HSSFRow row = (HSSFRow) rowIter.next();
      HSSFCell phoneNumbersCell = row.getCell(indices.RECEIVER_PHONES);
      String[] phoneNumbers = getPhoneNumbers(phoneNumbersCell);

      if (!v.hasMobilePhone(phoneNumbers)) {

        wrongNumbersRowsList.add(row);

      }

    }

    System.out.printf("Обработано %d строк\n", count - HEADER_SIZE);
    System.out.printf("Нет мобильных номеров: %d\n", wrongNumbersRowsList.size() - HEADER_SIZE);
    return wrongNumbersRowsList;

  }

  public HSSFWorkbook writeToWorkBook(List<HSSFRow> rows) {

    HSSFWorkbook wb = new HSSFWorkbook();
    Sheet sheet = wb.createSheet();

    int i = 0;

    for (HSSFRow sourceRow : rows) {

      Row newRow = sheet.createRow(i);
      int j = 0;

      for (Cell cell : sourceRow) {

        newRow.createCell(j).setCellValue(cell.getRichStringCellValue());
        j++;

      }

      i++;

    }
    return wb;
  }

  public void writeToFile(HSSFWorkbook wb, String outPath) throws IOException {

    Path OUT_DIR_PATH = Path.of(outPath);
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm");
    String filePath =  OUT_DIR_PATH + LocalDateTime.now().format(formatter) + ".xlsx";
    File outFile = new File(filePath);

    FileOutputStream outputStream = new FileOutputStream(filePath);
    wb.write(outputStream);
    wb.close();

  }

  public HSSFWorkbook readWorkbook(String filePath) throws IOException {

    POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(filePath));

    return new HSSFWorkbook(fs);

  }

  private String[] getPhoneNumbers(HSSFCell cell) {

    String line = cell
        .getRichStringCellValue()
        .getString()
        .replace(" ", "");

    return line.split(",");

  }

}
