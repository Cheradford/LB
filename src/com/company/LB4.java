package com.company;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Scanner;

public class LB4 {

    static Path Start() {
        System.out.print("Введите абсолютный путь каталога: ");
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        Path temp;
        try {
            temp = Paths.get(reader.readLine());
            if (!Files.exists(temp)) throw new IOException();
            return temp;
        } catch (IOException e) {
            System.out.print("Неверное указан путь каталога. ");
            System.out.println("Будет установлен каталог по умолчанию.");
        }
        return Paths.get("C:\\");
    }

    static void WorkWithFile(Path current) throws IOException {
        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss dd-MM-yyyy ", Locale.forLanguageTag("RUS"));
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        while(true) {
            System.out.println("1. распечатка свойств выбранного файла\n" +
                    "2. удаление выбранного файла\n" +
                    "3. чтение содержимого файла путем запуска программы notepad.exe\n" +
                    "4. Выход.");
            switch (Integer.parseInt(reader.readLine())) {
                case 1: {
                    System.out.println("Название файла: " + current.getFileName());
                    System.out.println("\nПоследний раз был редактрирован: " + df.format(new Date(Files.getLastModifiedTime(current).toMillis())) +
                            "\n Размер " + Files.size(current));
                    System.out.println("Обычный файл: " + (Files.isRegularFile(current) ? "Да" : "Нет") +
                            "\nИсполняемый файл: " + (Files.isExecutable(current) ? "Да" : "Нет") + "\nСкрытый файл: " + (Files.isHidden(current) ? "Да" : "Нет") +
                            "\nФайл читаемый: " + (Files.isReadable(current) ? "Да" : "Нет") + "\nФайл перезаписываемый: " + (Files.isWritable(current) ? "Да" : "Нет"));
                    WorkWithFile(current);
                }
                break;
                case 2: {
                    System.out.println("Вы точно хотите удалить файл? [Y/N]");
                    if ("Y".equals(reader.readLine().toUpperCase(Locale.ROOT))) Files.delete(current);
                    return;
                }
                case 3: {
                    System.out.println("Открытие файла в notepad.exe ...");
                    Desktop.getDesktop().edit(current.toFile());
                    WorkWithFile(current);
                }
                break;
                case 4: return;
            }
        }

    }

    static void WorkWithPath(Path current) {
        System.out.print("Введите название нового каталога: ");
        String name;
        try {
            name = new BufferedReader(new InputStreamReader(System.in)).readLine();
            if (name.isEmpty()) throw new IOException();
        } catch (IOException e) {
            System.out.println("Не было введено название нового каталога. Будет создан каталог под названием NewCatalog.");
            name = "NewCatalog";
        }
        Path temp = Paths.get(current + "\\" + name);
        try {
            Files.createDirectory(temp);
        } catch (IOException e) {
            System.out.println("Ошибка: добавление существующего каталога");
        }

    }

    static void CreateTxt(Path current) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("Введите название файла: ");
        String name;
        OutputStream file;
        try {
            name = reader.readLine();
            file = new FileOutputStream(current + "\\" + name + ".txt");
            System.out.print("Введите инфорацию в файл: ");
            file.write(reader.readLine().getBytes());
            file.close();
        } catch (FileNotFoundException e) {
            System.out.println("Файл не найден");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    static void theWay(Path current) throws IOException {

        Scanner scanner = new Scanner(System.in);
        String ans =" ";
        int i,j;

        while (!ans.equals("E")){
            i = 1; j = 1;
            System.out.println("0. ...");
            for (Path temp : Files.newDirectoryStream(current)) {

                System.out.println(i + ". " + temp.getFileName() + (Files.isDirectory(temp) ? "(Каталог)" : ""));
                i++;
            }
            System.out.println("C. Создание нового каталога\tF. Создание нового текстового файла\tE. Выход из программы");
            if (!scanner.hasNextInt()) {
                ans = scanner.next();
                if (ans.equals("C")) WorkWithPath(current);
                if (ans.equals("F")) CreateTxt(current);

            } else {
                i = scanner.nextInt();
                if (i == 0) {
                    current = current.getParent();
                } else {
                    for (Path temp : Files.newDirectoryStream(current)) {
                        if (i == j) {
                            if (Files.isDirectory(temp)) {
                                current = temp;
                                break;
                            } else {
                                WorkWithFile(temp);
                            }
                        }
                        j++;
                    }
                }
            }
        }

    }

    public static void main(String[] args) throws IOException {
        theWay(Start());
    }
}
