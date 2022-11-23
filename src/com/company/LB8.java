package com.company;

import javafx.scene.image.Image;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.*;


class Paint extends JFrame{
    static Color current;
    static BasicStroke cur_str;
    static String shape;
    static JMenuBar Bar;

    HashMap<Shape, Color> shapes = new HashMap<>();
    HashMap<Shape, Stroke> pens = new HashMap<>();

    static class ML implements MouseMotionListener,MouseListener,MouseWheelListener {
        static boolean moving = false;
        static int[]x = new int[3];
        static int[]y = new int[3];
        int i =0;
        static Paint wind;
        static float width = 2.0f;

        public ML(Paint wind){
            ML.wind = wind;
            for (int i = 0; i < 3; i++) {
                x[i] = -1;
                y[i] = -1;
            }
        }
        @Override
        public void mouseDragged(MouseEvent mouseEvent) {

            if(!shape.equals("Треугольник")) {
                if (!moving) {
                    x[0] = mouseEvent.getX();
                    y[0] = mouseEvent.getY();
                    moving = true;
                }

                x[1] = mouseEvent.getX();
                y[1] = mouseEvent.getY();
            }

        }

        @Override
        public void mouseWheelMoved(MouseWheelEvent mouseWheelEvent) {
            BasicStroke oldster = (BasicStroke)((Graphics2D)wind.getGraphics()).getStroke();
            Graphics2D grp = (Graphics2D) wind.getGraphics();

            if(mouseWheelEvent.getWheelRotation()<0) width++;
            if(mouseWheelEvent.getWheelRotation()>0 && width!=0) width--;

            cur_str = new BasicStroke(width, oldster.getEndCap(), oldster.getLineJoin(),oldster.getMiterLimit(),oldster.getDashArray(),oldster.getDashPhase());
            grp.clearRect(0,0,wind.getWidth(),wind.getHeight());
            wind.paint(grp);
        }

        @Override
        public void mouseReleased(MouseEvent mouseEvent) {
            if(!shape.equals("Треугольник")) {
                moving = false;
                wind.drawShape(x,y);
            }

        }

        @Override
        public void mouseMoved(MouseEvent mouseEvent) {}
        @Override
        public void mouseClicked(MouseEvent mouseEvent) {
            if(shape.equals("Треугольник")){

                while (i!=3){

                    x[i] = mouseEvent.getX();
                    y[i] = mouseEvent.getY();
                    System.out.println(x[i]+" "+y[i]);
                    i++;
                    if( i==3 ) {
                        i = 0;
                        wind.drawShape(x,y);
                    }
                    return;
                }
            }
        }
        @Override
        public void mousePressed(MouseEvent mouseEvent) {}
        @Override
        public void mouseEntered(MouseEvent mouseEvent) {}
        @Override
        public void mouseExited(MouseEvent mouseEvent) {}


    }
    static class MyListener implements ActionListener{
        Paint wind;
        MyListener(Paint wind){
            this.wind = wind;
        }
        @Override
        public void actionPerformed(ActionEvent actionEvent) {

            switch (actionEvent.getActionCommand()){
                case "Черный":{
                    current = Color.BLACK;
                    return;
                }
                case "Серый":{
                    current = Color.GRAY;
                    return;
                }
                case "Белый":{
                    current = Color.WHITE;
                    return;
                }
                case "Красный":{
                    current = Color.RED;
                    return;
                }
                case "Зеленый":{
                    current = Color.GREEN;
                    return;
                }
                case "Голубой":{
                    current = Color.BLUE;
                    return;
                }
                case "Циановый":{
                    current = Color.CYAN;
                    return;
                }
                case "Пурпурный":{
                    current = Color.MAGENTA;
                    return;
                }
                case "Желтый":{
                    current = Color.YELLOW;

                    return;
                }
                case "Квадрат":{
                    shape = "Квадрат";
                    return;
                }
                case "Круг":{
                    shape = "Круг";
                    return;
                }
                case "Треугольник":{
                    shape = "Треугольник";
                    return;
                }
                case "Выход":{
                    wind.dispose();
                    return;
                }
                case "Создать новый":{
                    wind.NewBack();
                }

            }

        }
    }

    public void drawShape(int[] x,int[] y){
        Graphics2D g =(Graphics2D) this.getGraphics();

        Shape temp = null;
        switch (shape){
            case "Круг":{
                temp = new Ellipse2D.Double((x[0]+x[1])/2.0,(y[0]+y[1])/2.0,Math.abs(x[0]-x[1])/2.0,Math.abs(y[0]-y[1])/2.0);
            }break;
            case "Квадрат":{
                temp = new Rectangle2D.Double(x[0],y[0],Math.abs(x[0]-x[1]),Math.abs(y[0]-y[1]));
            } break;
            case "Треугольник":{
                temp = new Polygon(x,y,y.length);
            }break;
        }
        shapes.put(temp, current);
        pens.put(temp,cur_str);
        this.revalidate();
        this.repaint();


    }
    @Override
    public void paint(Graphics gr){
        Graphics2D g = (Graphics2D) gr;
        for (Map.Entry<Shape, Color> temp: shapes.entrySet()) {
            System.out.println(temp.getKey());
            System.out.println(temp.getValue());
            System.out.println(pens.get(temp.getKey()));
            g.setColor(temp.getValue());
            g.setStroke(pens.get(temp.getKey()));
            g.draw(temp.getKey());

        }
        Bar.revalidate();
        Bar.repaint();

    }

    public void NewBack(){
        this.shapes.clear();
        this.pens.clear();
        this.removeAll();
        Graphics temp = this.getGraphics();
        temp.dispose();
        this.repaint();
        this.revalidate();
    }

    public Paint(String s){
        current = Color.BLACK;
        shape = "Круг";

        Bar = new JMenuBar();
        String[][] names = {
                            {"Форма", "Квадрат", "Треугольник", "Круг"},
                            {"Цвет", "Черный", "Серый", "Белый", "Красный","Зеленый", "Голубой", "Циановый", "Пурпурный", "Желтый"}};
        for (String[] name:names) {
            Bar.add(CreateMenus(name));
        }


        setVisible(true);
        setSize(500,500);
        setFocusable(false);
        setName(s);
        cur_str = (BasicStroke) ((Graphics2D) getGraphics()).getStroke();
        setJMenuBar(Bar);
        addMouseListener(new ML(this));
        addMouseMotionListener(new ML(this));
        addMouseWheelListener(new ML(this));
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent ev) { ev.getWindow().dispose(); }

        });

    }

    JMenu CreateMenus(String[] names){
        JMenu temp = new JMenu(names[0]);

        for (int i = 1; i <names.length ; i++) {

            JMenuItem item = new JMenuItem(names[i]);
            item.addActionListener(new MyListener(this));
            temp.add(item);
            if((i)%3 == 0 && i != (names.length-1)) temp.addSeparator();

        }

        return temp;
    }


}
public class LB8 {


    public static void main(String[] args){
        new Paint("Paint");

    }
}