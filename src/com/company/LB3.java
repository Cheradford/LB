package com.company;

import java.util.Scanner;

public class LB3 {

    public static void main(String[] args) {

        Scanner scn = new Scanner(System.in);
        String[] lines = new String[100];
        System.out.print("Введите размер слов: ");
        int size = scn.nextInt();
        for (int i = 0; i < lines.length; i++) {
            StringBuilder temp = new StringBuilder();
            for (int z = 0; z <(int) (Math.random()*99) + 1; z++) {
                for (int j = 0; j < size; j++) {
                    temp.append((char) (Math.random() * 31 + 1040));
                }
                temp.append(" ");
            }
            lines[i] = temp.toString();
        }
        System.out.println("Все строки: ");
        for (String line :lines) {
            System.out.println(line);
        }


        System.out.println("Палиндромы: ");
        for (String line : lines) {
            scn = new Scanner(line);
            scn.useDelimiter(" ");
            loop:
            while (scn.hasNext()) {
                String temp = scn.next();
                for (int i = 0; i < size / 2; i++) {
                    if (temp.charAt(i) != temp.charAt(temp.length() - 1 - i)) continue loop;
                }
                System.out.print(temp+" ");
            }
        }
    }
}
