package com.company;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Objects;

public class LB5 {

    static class Coordinates {

        int x, y;

        public Coordinates(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public String toString() {
            return "Coordinates{" +
                    "x=" + x +
                    ", y=" + y +
                    '}';
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Coordinates that = (Coordinates) o;
            return x == that.x && y == that.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }
    }

    static class Car {
        Coordinates loc;


        public Car(Coordinates var1) {
            loc = var1;
        }

        public Coordinates getLoc() {
            return loc;
        }

        public void toMove(Coordinates dest) {

            while (!loc.equals(dest)) {

                if (loc.x < dest.x) {
                    loc.x++;
                }
                if (loc.x > dest.x) {
                    loc.x--;
                }

                if (loc.y < dest.y) {
                    loc.y++;
                }

                if (loc.y > dest.y) {
                    loc.y--;
                }

                //System.out.println(loc);
            }

        }
    }


    static class Point {
        boolean moveable = true;

        public void setMoveable(boolean moveable) {
            this.moveable = moveable;
        }

        public boolean isMoveable() {
            return moveable;
        }

        @Override
        public String toString() {
            return moveable ? "0" : "1";
        }
    }

    static class GameMap extends Frame {

        Point[][] map;
        Coordinates exit;
        Car player;
        ArrayList<Coordinates> way = new ArrayList<>();

        public GameMap() {
            int length = 5;
            map = new Point[length][length];
            exit = new Coordinates((int) (Math.random() * length), (int) (Math.random() * length));
            player = new Car(new Coordinates((int) (Math.random() * length), (int) (Math.random() * length)));
            way.add(new Coordinates(player.getLoc().getX(), player.getLoc().getY()));

            for (int i = 0; i < map.length; i++) {
                for (int j = 0; j < map[i].length; j++) {
                    map[i][j] = new Point();
                }
            }

            for (int i = 0; i < 5; i++) {
                Coordinates temp;
                int x, y;
                do {
                    x = (int) (Math.random() * length);
                    y = (int) (Math.random() * length);
                    temp = new Coordinates(x, y);

                } while (temp.equals(player.getLoc()) || temp.equals(exit));

                way.add(temp);
                map[x][y].setMoveable(false);
            }

            for (int i = 0; i < map.length; i++) {
                for (int j = 0; j < map[i].length; j++) {
                    if (player.getLoc().getX() == i && player.getLoc().getY() == j) {
                        System.out.print("P");
                        continue;
                    }
                    if (exit.getX() == i && exit.getY() == j) {
                        System.out.print("E");
                        continue;
                    }
                    System.out.print(map[i][j]);
                }
                System.out.println();
            }
            Move();
            way.add(exit);
            setSize(500, 500);
            setVisible(true);
            setLayout(null);
            setResizable(false);

            System.out.println(way);
        }

        public void paint(Graphics gr) {
            Graphics2D g = (Graphics2D) gr;

            BasicStroke penl = new BasicStroke(5, BasicStroke.CAP_BUTT,
                    BasicStroke.JOIN_MITER, 30);
            g.setStroke(penl);
            g.draw(new Line2D.Double(way.get(0).getX() * 100, way.get(0).getY() * 100, way.get(6).getX() * 100, way.get(6).getY() * 100));
            for (int i = 6; i < way.size() - 1; i++) {
                g.draw(new Line2D.Double(way.get(i).getX() * 100, way.get(i).getY() * 100, way.get(i + 1).getX() * 100, way.get(i + 1).getY() * 100));
                //System.out.println(way.get(i).getX() + " " + way.get(i).getY());
            }
            for (int i = 1; i < 6; i++) {
                g.draw(new Ellipse2D.Double(way.get(i).getX() * 100, way.get(i).getY() * 100, 100, 100));
                System.out.println(way.get(i).getX() + " " + way.get(i).getY());
            }
        }

        public void Move() {

            try {
                OutputStream file = new FileOutputStream("log.txt");
                file.write(("Player: " + player.getLoc().toString() + "\n").getBytes());
                file.write(("Exit: " + exit.toString() + "\n").getBytes());
                file.write("Координаты преград: \n".getBytes());
                for (int i = 1; i < way.size(); i++) {

                    file.write(("\t" + i + " " + way.get(i).toString() + "\n").getBytes());
                }


                while (!player.getLoc().equals(exit)) {
                    int i;
                    int j;
                    Coordinates point1 = player.getLoc();

                    System.out.println(point1);
                    if (point1.getX() > exit.getX()) i = -1;
                    else if (point1.getX() < exit.getX()) i = 1;
                    else i = 0;
                    if (point1.getY() > exit.getY()) j = -1;
                    else if (point1.getY() < exit.getY()) j = 1;
                    else j = 0;

                    if (map[point1.getX() + i][point1.getY() + j].isMoveable()) {
                        point1.x += i;
                        point1.y += j;
                    } else if (((exit.getX() - point1.getX()) * i) > ((exit.getY() - point1.getY()) * j) && map[point1.getX() + i][point1.getY()].isMoveable()) {
                        point1.x += i;
                    } else if (map[point1.getX()][point1.getY() + j].isMoveable()) {
                        point1.y += i;
                    } else if (map[point1.getX() - j][point1.getX() + j].isMoveable()) {
                        point1.x -= j;
                        point1.y += j;
                    } else if (map[point1.getX() + j][point1.getX() + j].isMoveable()) {
                        point1.x += j;
                        point1.y += j;
                    } else break;

                    player.toMove(point1);
                    way.add(new Coordinates(point1.getX(),point1.getY()));
                    file.write((point1 + "\n").getBytes());

                }
                //way.add(exit);


            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public static void main(String[] args) {
        new GameMap();

    }
}
