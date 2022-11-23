package com.company;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class LB2 {

    static StringBuilder XOR(String txt){
        char[] key = {'s','e','k','r','e','t'};
        StringBuilder temp = new StringBuilder();
        for (int i = 0; i <txt.length(); i++) {

            temp.append((char) (txt.charAt(i)^key[i% key.length]));

        }

        return temp;
    }

    private static char rigth(char a, int s) {
        s %= 16;
        return (char) ((a >> s) | (a << (16 - s)));
    }
    private static char left(char a, int s) {
        s %= 16;
        return (char) ((a << s) | (a >> (16 - s)));
    }

    static String Cicle(String txt, int key){

        StringBuilder temp = new StringBuilder();
        for (char symb: txt.toCharArray()) {
            temp.append(key>=0 ? rigth(symb,key): left(symb,-key));
        }
        return String.valueOf(temp);
    }

    public static void main(String[] args) throws IOException {

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String text = reader.readLine();
        int key = 1;
        System.out.println("1.\tШифрование с помощью операции \"XOR\"\n" +
                "2.\tШифрование с помощью циклического сдвига\n" +
                "3.\tВвести число позиций для шифрования путем циклического сдвига\n");
        while (true){
            switch (Integer.parseInt(reader.readLine())){
                case 1:{
                    System.out.println("Исходный текст: " +text);
                    text = XOR(text).toString();
                    System.out.println("Новый текст: " +text);
                }break;

                case 2:{
                    System.out.println("Исходный текст: " +text);
                    text = Cicle(text,key);
                    System.out.println("Новый текст: " +text);

                }break;

                case 3:{
                        System.out.print("Введите новый ключ сдвига: ");
                        key = Integer.parseInt(reader.readLine());
                }break;

                case 4:{
                    System.exit(0);
                }break;
            }
        }

    }
}