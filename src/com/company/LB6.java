package com.company;


import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Scanner;

public class LB6 {




    static void ThrowIndexOutofBounds(){
        try {
            int[] temp = new int[10];
            System.out.println(temp[10]);
        } catch (ArrayIndexOutOfBoundsException ex){
            System.out.println("Пример переполнения массива: ");
            System.out.println(ex);
        }
    }

    static void ThrowIOException(){
        try{
            System.out.println(Files.size(Paths.get("\\dom")));
        }catch (Exception e){
            System.out.println("Пример ошибка ввода-вывода : ");
            System.out.println(e);
        }
    }

    static void ThrowFileNotFoundException(){

        try {
            FileInputStream temp = new FileInputStream("well.txt");

        }catch (FileNotFoundException e){
            System.out.println("Пример отсутствие файла : ");
            System.out.println(e);
        }
    }

    static void ThrowArithmeticException(){

        try {
            System.out.println(12/0);
        }catch (ArithmeticException e){
            System.out.println("Пример деления на ноль: ");
            System.out.println(e);
        }
    }

    static void ThrowAnyException(){

        try {
            Thread.currentThread().setPriority(-1000);
        }catch (IllegalArgumentException e){
            System.out.println("Cобственное исключение произвольного вида: ");
            System.out.println(e);
        }
    }
    public static void main(String[] args){
        String temp;
        do {
            Scanner scn = new Scanner(System.in);
            System.out.println("1.\tОшибка ввода-вывода.\n" +
                    "2.\tПереполнение массива.\n" +
                    "3.\tОтсутствие файла.\n" +
                    "4.\tДеление на ноль.\n" +
                    "5.\tСобственное исключение произвольного вида.\n" +
                    "6.\tВыход.");
            temp = scn.next();
            switch (temp){
                case "1": ThrowIOException(); break;
                case "2": ThrowIndexOutofBounds(); break;
                case "3": ThrowFileNotFoundException(); break;
                case "4": ThrowArithmeticException(); break;
                case "5": ThrowAnyException(); break;
            }
        }while (!temp.equals("6"));


    }
}
