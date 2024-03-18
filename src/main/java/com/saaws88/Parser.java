package com.saaws88;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Parser {

  HashMap<String, List<String>> getWrongNums(String filename) throws IOException {

    BufferedReader br = new BufferedReader(new FileReader(filename));
    Validator v = new Validator();

    HashMap<String, List<String>> wrongNumberOrders = new HashMap<>();

    String line;
    int count = 0;

    skipHeader(br);
    while ((line = br.readLine())!= null) {

      count++;
      List<String> wrongNums = new ArrayList<>();
      String[] columns = separateLine(line);

      for(int i = 1; i < columns.length; i++) {

        if(v.isMobilePhone(columns[i])) {
          break;
        }

        wrongNums.add(columns[i]);
        wrongNumberOrders.put(columns[0], wrongNums);

      }

    }

    System.out.printf("Кол-во заказов %s\n", count);
    return wrongNumberOrders;

  }

  public void writeWrongNumbersToFile(HashMap<String, List<String>> wrongNumberOrders) throws IOException{

    if (wrongNumberOrders != null) {

      Path OUT_FOLDER_PATH = Path.of(System.getenv("OUT_FOLDER_PATH"));
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm");
      String fileName =  LocalDateTime.now().format(formatter) + ".csv";
      File outFile = new File(OUT_FOLDER_PATH + fileName);
      PrintWriter writer = new PrintWriter(outFile);

      writer.println("Номер накладной, номера телефонов");

      int count = 0;

      for (Map.Entry<String, List<String>> entry : wrongNumberOrders.entrySet()) {
        String key = entry.getKey();
        String valueToPrint = entry.getValue().toString()
            .replace("[", "")
            .replace("]", "");

        count++;
        writer.printf("%s, \"%s\"\n", key, valueToPrint);

      }

      writer.close();

      System.out.printf(
          "Создан файл %s\nКоличество заказов без мобильного номера телефона: %d\n", outFile.getName(), count
      );

    }


  }

  private String[] separateLine(String line) {
    return line
        .replace("\"", "")
        .replace(" ", "")
        .split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)", -1);

  }

  private void skipHeader (BufferedReader br) throws IOException {
    for(int i = 0; i < 1; i++) {
      br.readLine();
    }
  }



}





