package com.company;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;



public class LB7 {
    static class MyListener implements ActionListener {

        Commander Window;
        Path toChange;
        TextField tf;
        TextField tf2;
        MyListener(Commander var1, Path var2) { Window = var1; toChange = var2; }
        MyListener(Commander var1, Path var2, TextField var3) { Window = var1; toChange = var2; tf = var3; }
        MyListener(Commander var1, Path var2, TextField var3,TextField var4) { Window = var1; toChange = var2; tf = var3; tf2 = var4; }


        @Override
        public void actionPerformed(ActionEvent e) {
            Commander previous= Window;
            try {

                if(e.getActionCommand().equals("Начать")) {
                    Window = new Commander(e.getActionCommand(),Window,Paths.get(tf.getText()),0);
                    previous.dispose();
                    return;
                }
                if(e.getActionCommand().equals("Создание каталога")) {
                    Window = new Commander(e.getActionCommand(),Window,toChange,2);
                    return;
                }

                if(e.getActionCommand().equals("Создать каталог")){
                    Path temp = Paths.get(toChange + "\\" + tf.getText());
                    try {
                        Files.createDirectory(temp);
                        Window.dispose();
                        Window.prev.SetPath(Window.prev.now);

                    } catch (IOException ex) {
                        System.out.println("Ошибка: добавление существующего каталога");
                    }
                    return;
                }

                if(e.getActionCommand().equals("Создание файла")){
                    Window = new Commander(e.getActionCommand(),Window,toChange,3);

                    return;
                }

                if(e.getActionCommand().equals("Создать файл")){
                    OutputStream file;

                    try {
                        file = new FileOutputStream(toChange + "\\" + tf.getText());
                        file.write(tf2.getText().getBytes());
                        file.close();
                        Window.dispose();
                        Window.prev.SetPath(Window.prev.now);
                    } catch (FileNotFoundException ex) {
                        System.out.println("Файл не найден");
                    }
                    return;
                }

                if(Files.isDirectory(toChange)) Window.SetPath(toChange);
                else {
                    if(!e.getActionCommand().equals("Удалить") && !e.getActionCommand().equals("Открыть в notepad.exe") && !e.getActionCommand().equals("Выход")){
                        Window = new Commander(toChange.getFileName().toString(),Window,toChange,1);

                    }

                    if (e.getActionCommand().equals("Удалить")) {
                        Files.delete(toChange);
                        Window.dispose();
                        Window.prev.SetPath(Window.prev.now);
                        return;
                    }
                    if (e.getActionCommand().equals("Открыть в notepad.exe")) {
                        Desktop.getDesktop().edit(toChange.toFile());
                        return;
                    }
                    if (e.getActionCommand().equals("Выход")) {
                        Window.dispose();

                    }


                }
            }
            catch (IOException ex){
                System.out.println("Путь ненайден.");
                try {
                    Window.SetPath(toChange.getParent());
                } catch (IOException exc) {
                    exc.printStackTrace();
                }
            }

        }

    }



    static class Commander extends Frame{

        Path now;
        Commander prev;
        ArrayList<Button> buttons;



        public void SetPath(Path current) throws IOException{
            this.removeAll();
            for (Button temp: buttons) {
                temp.setVisible(false);
            }
            buttons = new ArrayList<>();
            setLayout(null);

            setVisible(true);
            int y = 30;
            int x = 20;
            int max_X = 0, max_Y = 0;
            long amount = 1 , count = 1;
            now = current;
            for (Path ignored :Files.newDirectoryStream(now)) {
                amount++;
            }
            amount = Math.round(Math.sqrt(amount));
            Button button = new Button("...");
            button.setBounds(x,y,150,50);
            x+= 160;
            button.addActionListener(new MyListener(this,now.getParent()));
            add(button);
            buttons.add(button);
            for (Path temp: Files.newDirectoryStream(now)) {
                button = new Button(temp.getFileName().toString());
                button.setBounds(x ,y ,150,50 );
                x+=160;
                max_X = Math.max(max_X,x);
                count++;
                if (count>=amount){
                    y += 55;
                    x = 20;
                    count = 0;
                }
                max_Y = Math.max(max_Y,y);
                button.addActionListener(new MyListener(this, temp));
                add(button);
                buttons.add(button);
            }
            x = 20;
            y+= 55;
            button = new Button("Создание каталога");
            button.setBounds(x,y,150,50);
            button.addActionListener(new MyListener(this, now));
            add(button);
            buttons.add(button);
            x+=160;
            button = new Button("Создание файла");
            button.setBounds(x,y,150,50);
            button.addActionListener(new MyListener(this, now));
            add(button);
            buttons.add(button);
            if(getWidth()<max_X + 100 || getHeight() < max_Y + 100) setSize(max_X + 120,max_Y + 120);
        }

        void SetFile(Path current){

            setSize(800,500);
            setLayout(null);
            setVisible(true);
            int x = 10;
            int y = 250;

            String[] func = new String[] {"Удалить", "Открыть в notepad.exe", "Выход"};
            for (String temp:func) {
                Button button = new Button(temp);
                button.setBounds(x, y+=50, 150, 50);
                button.addActionListener(new MyListener(this, current));
                buttons.add(button);
                add(button);
            }
            toProperties();





        }

        void toProperties(){


            SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss dd-MM-yyyy ", Locale.forLanguageTag("RUS"));

            setLayout(null);
            setVisible(true);
            int x = 20;
            int y = 10;

            try {
                Label l1 = new Label();
                String[] properties = new String[]{"Название файла: " + now.getFileName(), "Последний раз был редактрирован: " + df.format(new Date(Files.getLastModifiedTime(now).toMillis())),
                        "Размер " + Files.size(now), "Обычный файл: " + (Files.isRegularFile(now) ? "Да" : "Нет"), "Исполняемый файл: " + (Files.isExecutable(now) ? "Да" : "Нет"),
                        "Скрытый файл: " + (Files.isHidden(now) ? "Да" : "Нет"), "Файл читаемый:" + (Files.isReadable(now) ? "Да" : "Нет"),
                        "\nФайл перезаписываемый: " + (Files.isWritable(now) ? "Да" : "Нет")};

                l1.setFont(new Font("Serif", Font.PLAIN, 28));
                for (String temp : properties) {

                    l1 = new Label(temp,Label.LEFT);
                    l1.setBounds(x,y+=20,getWidth(),20);
                    add(l1);

                }

            } catch (IOException e) {
                System.out.println("Файл не найден");
            }
        }

        void toAddNewCatalog(){
            setLayout(null);
            setVisible(true);
            setSize(300,160);
            setResizable(false);

            Label l1 = new Label("Введите название каталога",Label.CENTER);
            l1.setSize(40,40);
            l1.setBounds(10,30,getWidth()-20,15);
            add(l1);

            TextField tf1 = new TextField(30);
            tf1.setBounds(20,50,getWidth()-40,40);
            add(tf1);

            Button button = new Button("Создать каталог");
            button.setBounds(10,100,getWidth()-20,50);
            button.addActionListener(new MyListener(this,now,tf1));
            add(button);
            buttons.add(button);


        }
        void toAddNewFile() {
            setLayout(null);
            setVisible(true);
            setSize(300, 500);
            setResizable(false);

            Label l1 = new Label("Введите название файла",Label.CENTER);
            l1.setSize(40,40);
            l1.setBounds(10,30,getWidth()-20,15);
            add(l1);

            TextField tf1 = new TextField(30);
            tf1.setBounds(20,50,getWidth()-40,40);
            add(tf1);

            Label l2= new Label("Введите текст",Label.CENTER);
            l2.setSize(40,40);
            l2.setBounds(10,150,getWidth()-20,15);
            add(l2);

            TextField tf2 = new TextField(100);
            tf2.setBounds(20,175,getWidth()-40,100);
            add(tf2);

            Button button = new Button("Создать файл");
            button.setBounds(10,300,getWidth()-20,50);
            button.addActionListener(new MyListener(this,now,tf1,tf2));
            add(button);
            buttons.add(button);

        }

        void setStart(){
            setLayout(null);
            setVisible(true);
            setSize(300,160);
            setResizable(false);
            prev = null;

            Label l1 = new Label("Введите абсолютный путь каталога",Label.CENTER);
            l1.setSize(40,40);
            l1.setBounds(10,30,getWidth()-20,15);
            add(l1);

            TextField tf1 = new TextField(30);
            tf1.setBounds(20,50,getWidth()-40,40);
            add(tf1);

            Button button = new Button("Начать");
            button.setBounds(10,100,getWidth()-20,50);
            button.addActionListener(new MyListener(this,now,tf1));
            add(button);
            buttons.add(button);
        }

        public Commander(String name,Commander prev ,Path current, int mode) throws IOException {
            super(name);
            setLayout(null);
            setVisible(true);
            buttons = new ArrayList<>();
            now = current;
            this.prev = prev;
            if(mode == -1){
                setStart();
            }
            if(mode == 0) {
                SetPath(current);
            }
            if(mode == 1) {
                SetFile(current);

            }
            if(mode == 2) {
                toAddNewCatalog();
            }
            if(mode == 3) {
                toAddNewFile();
            }

            addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent ev) { ev.getWindow().dispose(); }

            });
        }
    }

    public static void main(String[] args) throws IOException {
        new Commander("Commander",null,null,-1);
    }
}