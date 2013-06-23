package mtj;

import java.util.Arrays;

public class Time {

    public static float[][] time;

    public Time(Road[][] city, int size) {
        time = new float[size][size];
        for (int i = 0; i < time.length; i++) {
            for (int j = 0; j < time.length; j++) {
                if (city[i][j] == null) {
                    time[i][j] = Integer.MAX_VALUE / 2;
                } else {
                    time[i][j] = (float) ((city[i][j].lengthRoad / city[i][j].speed) * 3.6);
                }
            }
        }
    }

    public float getTimeEdge(int i, int j) {
        return (time[i][j] == Integer.MAX_VALUE / 2) ? 0 : time[i][j];
    }

    public int[] dijkstraLen(int current, int last) {
        float[][] dist = new float[time.length][2];
        float[][] mgraf = new float[time.length][time.length];
        for (int i = 0; i < mgraf.length; i++) {
            System.arraycopy(time[i], 0, mgraf[i], 0, mgraf.length);
        }
        for (int i = 0; i < dist.length; i++) {
            dist[i][0] = mgraf[current][i];
            dist[i][1] = current;
        }
        dist[current][0] = 0;
        boolean[] mark = new boolean[mgraf.length];
        Arrays.fill(mark, false);
        mark[current] = true;
        for (int i = 1; i < mgraf.length; i++) {
            int v = -1;
            for (int j = 0; j < mgraf.length; j++) {
                if ((!mark[j]) && ((v == -1) || (dist[v][0] > dist[j][0]))) {
                    v = j;
                }
            }
            mark[v] = true;
            for (int j = 0; j < mgraf.length; j++) {
                if (dist[j][0] > (dist[v][0] + mgraf[v][j])) {
                    dist[j][0] = dist[v][0] + mgraf[v][j];
                    dist[j][1] = v;
                }
            }
        }
//        for (int i = 0; i < dist.length; i++) {
//            System.out.print(dist[i][0] + " ");
//        }
//        System.out.println("");
//        for (int i = 0; i < dist.length; i++) {
//            System.out.format("%1.0f ", dist[i][1]);
//        }
//        System.out.println("");
        int[] ret = new int[dist.length];
        int temp = last;
        int i = 0;
        float tempTime = 0;
        while (temp != current) {
//            System.out.print((temp + 1) + " ");
            ret[i++] = temp;
            tempTime += dist[temp][0];
            temp = (int) dist[temp][1];
        }
        ret[i] = temp;
        int[] newRet = new int[i + 1];
//        System.out.println((temp + 1));
//        System.out.println(tempTime);
        for (int j = 0; j < newRet.length / 2; j++) {
            int k = ret[j];
            ret[j] = ret[newRet.length - 1 - j];
            ret[newRet.length - 1 - j] = k;
        }
        System.arraycopy(ret, 0, newRet, 0, newRet.length);
        return newRet;
    }

    public void print() {
        System.out.println("Time: ");
        for (float[] aTime : time) {
            for (int j = 0; j < time.length; j++) {
                if (aTime[j] == -1) {
                    System.out.print("-----   ");
                } else if (aTime[j] / 100 > 1) {
                    System.out.print(aTime[j] + "   ");
                } else if (aTime[j] / 10 > 1) {
                    System.out.print(aTime[j] + "    ");
                } else {
                    System.out.print(aTime[j] + "     ");
                }
            }
            System.out.println("");
        }
    }
}