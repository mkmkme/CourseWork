package mtj;

import java.util.LinkedList;
import java.util.Random;

public class City {

    public int size; // the number of roads
    public final static int COUNT = 48;
    public Road[][] city;

    public City(int size) {
        this.size = size;
        this.city = new Road[size][size];
    }

    public City() {
        this.size = COUNT;
        this.city = new Road[size][size];
    }

    public void newCityStatic() {
        Random rand = new Random();
        for (int i = 0, j = 1; i < size && j < size; i++, j++) {
            int ln = rand.nextInt(900) + 100;
            city[i][j] = city[j][i] = new Road(ln - ln % 10, 0);
        }
        for (int i = 0, j = 8; i < size && j < size; i++, j++) {
            int ln = rand.nextInt(900) + 100;
            city[i][j] = city[j][i] = new Road(ln - ln % 10, 0);
        }
        for (int i = 7; i < size - 1; i += 8) {
            city[i][ i + 1] = city[i + 1][i] = null;
        }
        newSpeed();
    }

    public void newCity() {
        Random rand = new Random();
        for (int i = 0; i < city.length; i++) {
            for (int j = 0; j < city.length; j++) {
                if (rand.nextBoolean() && i != j) {
                    int ln = rand.nextInt(900) + 100;
                    int th = rand.nextInt(100) + 20;
                    th = th / 2 >= 60 ? 60 : th / 2;
                    city[i][j] = new Road(ln - ln % 10, th - th % 5);
                }
            }
        }
        analyze();
        optimally();
        if (!isConnected()) {
            newCity();
        }
    }

    public void analyze() {
        for (int i = 0; i < city.length; i++) {
            for (int j = 0; j < city.length; j++) {
                if (i != j) {
                    if (city[i][j] != null && city[j][i] == null) {
                        city[j][i] = city[i][j];
                    } else if (city[i][j] != null && city[j][i] != null) {
                        city[i][j].lengthRoad = city[j][i].lengthRoad =
                                (city[i][j].lengthRoad + city[j][i].lengthRoad) / 2;
                    }
                }
            }
        }
    }

    public void optimally() {
        int temp;
        Random rand = new Random();
        for (int i = 0; i < city.length; i++) {
            temp = 0;
            for (int j = 0; j < city.length; j++) {
                if (city[i][j] != null) {
                    temp++;
                }
            }
            if (temp > 3) {
                for (int j = 0; j < temp - 3; j++) {
                    int t = rand.nextInt(size);
                    while (city[i][t] == null || city[t][i] == null) {
                        t = rand.nextInt(size);
                    }
                    city[i][t] = city[t][i] = null;
                }
            }
        }
    }

    public void newSpeed() {
        Random rand = new Random();
        for (int i = 0; i < city.length; i++) {
            for (int j = 0; j < city.length; j++) {
                if (city[i][j] != null) {
                    int th = rand.nextInt(51) + 10;
                    th = th >= 60 ? 60 : th;
                    city[i][j] = new Road(city[i][j].lengthRoad, th - th % 5);
                }
            }
        }
    }

    public void newSpeedReally() {
        Random rand = new Random();
        for (int i = 0; i < city.length; i++) {
            for (int j = 0; j < city.length; j++) {
                if (city[i][j] != null) {
                    int th;
                    if (rand.nextBoolean()) {
                        th = city[i][j].speed + 10;
                    } else {
                        th = city[i][j].speed - 10;
                    }
                    th = th >= 60 ? 60 : th;
                    th = th <= 0 ? 5 : th;
                    city[i][j] = new Road(city[i][j].lengthRoad, th);
                }
            }
        }
    }

    public boolean isConnected() {
        boolean[] temp = new boolean[size];
        int cur = 0;
        temp[cur] = true;
        LinkedList<Integer> list = new LinkedList();
        list.addFirst(cur);
        while (!list.isEmpty()) {
            for (int i = 0; i < city.length; i++) {
                if (city[cur][i] != null && !temp[i]) {
                    temp[i] = true;
                    list.addLast(i);
                }
            }
            cur = list.pollFirst();
        }
        for (boolean aTemp : temp) {
            if (!aTemp) {
                return false;
            }
        }
        return true;
    }

    public void delete() {
        for (int i = 0; i < city.length; i++) {
            for (int j = 0; j < city.length; j++) {
                if (new Random().nextBoolean() && city[i][j] != null) {
                    city[i][j] = city[j][i] = null;
                }
            }
        }
    }

    public void printInfo() {
        System.out.println("Length: ");
        for (Road[] aCity : city) {
            for (int j = 0; j < city.length; j++) {
                if (aCity[j] == null) {
                    System.out.print("---     ");
                } else {
                    System.out.print(aCity[j].lengthRoad + "     ");
                }
            }
            System.out.println("");
        }
        System.out.println("Throughput: ");
        for (Road[] aCity : city) {
            for (int j = 0; j < city.length; j++) {
                if (aCity[j] == null) {
                    System.out.print("--      ");
                } else {
                    System.out.print(aCity[j].speed + "      ");
                }
            }
            System.out.println("");
        }
    }

    public int getSpeed(int i, int j) {
        return city[i][j].speed;
    }

    public String getLengthString(int i, int j) {
        if (city[i][j] != null) {
            return String.valueOf(city[i][j].lengthRoad);
        }
        return "null";
    }

    public String getSpeedString(int i, int j) {
        if (city[i][j] != null) {
            return String.valueOf(city[i][j].speed);// + "-" + city[i][j].length;
        }
        return "null";
    }

    public City test(int count){
        City city = new City(count);
        city.newCity();
        city.printInfo();
        return city;
    }

    public City testStatic() {
        City city = new City();
        city.newCityStatic();
        return city;
    }
}