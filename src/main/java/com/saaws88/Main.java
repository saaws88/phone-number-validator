package com.saaws88;

import java.io.IOException;

public class Main {

  public static void main(String[] args) throws IOException {

    Parser p = new Parser();

    for (String arg : args) {
        long startTime = System.nanoTime();
        p.writeWrongNumbersToFile(p.getWrongNums(arg));
        long endTime = System.nanoTime();
        double elapsedTime = (endTime - startTime) / 1e+9;

        System.out.printf("Выполнено за %.2f сек. \n", elapsedTime);
    }
  }

}