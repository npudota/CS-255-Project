import java.sql.Array;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Collections;

public class Main {

    public static void main(String[] args){
        long start = System.nanoTime();

        ArrayList<TimeWindow> arr = new ArrayList<TimeWindow>();
        arr.add(new TimeWindow(0, 3, 1));
        arr.add(new TimeWindow(4, 10, 1));
//        arr.add(new TimeWindow(8, 10, 1));
//        arr.add(new TimeWindow(10, 17, 6));
        Customer a = new Customer("a", arr);


        ArrayList<TimeWindow> arr2 = new ArrayList<TimeWindow>();
        arr2.add(new TimeWindow(12, 18, 3));
        arr2.add(new TimeWindow(20, 25, 3));
        //arr2.add(new TimeWindow(8, 10, 1));
       // arr2.add(new TimeWindow(10, 17, 6));
        Customer b = new Customer("b", arr2);

       ArrayList<TimeWindow> arr3 = new ArrayList<TimeWindow>();
        arr3.add(new TimeWindow(24, 26, 1));
        arr3.add(new TimeWindow(27, 30, 1));
//        arr3.add(new TimeWindow(8, 10, 1));
//        arr3.add(new TimeWindow(10, 17, 6));
        Customer c = new Customer("c", arr3);


        ArrayList<TimeWindow> arr4 = new ArrayList<TimeWindow>();
        arr4.add(new TimeWindow(32, 33, 1));
        arr4.add(new TimeWindow(35, 37, 2));
//        arr4.add(new TimeWindow(8, 10, 1));
//        arr4.add(new TimeWindow(10, 17, 6));
        Customer d = new Customer("d", arr4);

        ArrayList<TimeWindow> arr5 = new ArrayList<TimeWindow>();
        arr5.add(new TimeWindow(39, 41, 1));
        arr5.add(new TimeWindow(43, 45, 1));
//        arr5.add(new TimeWindow(8, 10, 1));
//        arr5.add(new TimeWindow(10, 17, 6));
        Customer e = new Customer("e", arr5);

       /*ArrayList<TimeWindow> arr6 = new ArrayList<TimeWindow>();
       arr6.add(new TimeWindow(2, 7, 1));
       arr6.add(new TimeWindow(6, 8, 2));
//        arr6.add(new TimeWindow(8, 10, 1));
//        arr6.add(new TimeWindow(10, 17, 6));
        Customer f = new Customer("f", arr6);
//
       ArrayList<TimeWindow> arr7 = new ArrayList<TimeWindow>();
       arr7.add(new TimeWindow(2, 7, 1));
       arr7.add(new TimeWindow(6, 8, 2));
//        arr7.add(new TimeWindow(8, 10, 1));
//        arr7.add(new TimeWindow(10, 17, 6));
        Customer g = new Customer("g", arr7);*/

        Customer[] customers = {a,b,c,d,e};

        /*Customer[] customers = new Customer[500];

        for(int i = 0; i < 500; i++){
            ArrayList<TimeWindow> test = new ArrayList<TimeWindow>();
            for(int j = 0; j < 100; j++){
                test.add(new TimeWindow((int)(Math.random() * ( 24 - 1 )), (int)(Math.random() * ( 24 - 1 )), (int)(Math.random() * Math.floor(Math.random() * ( 10 - 1 )))));
            }

            Customer cus = new Customer("customer", test);
            customers[i] = cus;
        }*/


      System.out.println(TDTSPMTW(customers));

        long end = System.nanoTime();
        double seconds = (double)(end - start)/1000000000;
        System.out.println("Time: " + seconds + " seconds");

    }

    public static boolean topDownProcessing(Customer[] c){
        ArrayList<TimeWindow> windows = c[c.length - 1].getTimeWindows();
        int max = 0;
        for(int i = 0; i < windows.size(); i++) {
            if (max < windows.get(i).getEndTime() - windows.get(i).getDuration()) {
                max = windows.get(i).getEndTime() - windows.get(i).getDuration();
            }
        }


        for(int i = c.length - 1; i > 0; i--){
            //start of time window exceed max remove time window
            ArrayList<TimeWindow> w = c[i].getTimeWindows();
            for(int j = 0; j < w.size(); j ++){
                if((w.get(j).getStartTime() + w.get(j).getDuration()) > max)
                    w.remove(j);

                if(w.isEmpty())
                    return false;
            }

            for(int k = 0; k < w.size(); k++){
                TimeWindow tw = new TimeWindow(w.get(k).getStartTime(), Math.min(w.get(k).getEndTime(), max), w.get(k).getDuration());
                w.set(k, tw);
            }

            for(int l = 0; l < w.size(); l++){
                if (max < w.get(l).getEndTime() - w.get(l).getDuration()) {
                    max = w.get(l).getEndTime() - w.get(l).getDuration();
                }
            }

            c[i].setTimeWindows(w);
        }

        return true;
    }

    public static boolean bottomUpProcessing(Customer[] c){
        ArrayList<TimeWindow> windows = c[0].getTimeWindows();
        int min = Integer.MAX_VALUE;
        //System.out.println(windows.get(0).getStartTime());
        for(int i = 0; i < windows.size(); i++) {
            if (min > windows.get(i).getStartTime() + windows.get(i).getDuration()) {
                min = windows.get(i).getStartTime() + windows.get(i).getDuration();
            }
        }

        for(int i = 1; i < c.length; i++){
            //start of time window exceed max remove time window
            ArrayList<TimeWindow> w = c[i].getTimeWindows();
            for(int j = 0; j < w.size(); j ++){
                if((w.get(j).getEndTime() - w.get(j).getDuration()) < min)
                    w.remove(j);

                if(w.isEmpty())
                    return false;
            }

            for(int k = 0; k < w.size(); k++){
                TimeWindow tw = new TimeWindow(Math.max(w.get(k).getStartTime(), min), w.get(k).getEndTime(), w.get(k).getDuration());
                w.set(k, tw);
            }

            for(int l = 0; l < w.size(); l++){
                if (min > w.get(l).getStartTime() + w.get(l).getDuration()) {
                    min = w.get(l).getStartTime() + w.get(l).getDuration();
                }
            }

            c[i].setTimeWindows(w);
        }

        return true;
    }

    public static int constructFeasibleSubSchedule(int index, int departure, Customer[] c){
        while(index <= c.length-1){
            ArrayList<Integer> max = new ArrayList<Integer>();
            ArrayList<TimeWindow> tw = c[index].getTimeWindows();
            for(int i = 0; i < tw.size(); i++){
                int maxes = Math.max(tw.get(i).getStartTime(),departure) + tw.get(i).getDuration();
                if(tw.get(i).getEndTime() - tw.get(i).getDuration() >= departure)
                {
                    max.add(maxes);
                }
                if(max.size() > 0)
                {
                    departure = Collections.min(max);

                }
            }

            index++;
        }
        return departure;
    }

    public static int constructDominantSubSchedule(int index, int arrival, Customer[] c){
        while(index >= 0){
            ArrayList<TimeWindow> tw = c[index].getTimeWindows();
            ArrayList<Integer> min = new ArrayList<Integer>();
            for(int i = 0; i < tw.size(); i++){
                int minimums = Math.min(tw.get(i).getEndTime() , arrival) - tw.get(i).getDuration();
                if(tw.get(i).getStartTime() + tw.get(i).getDuration() <= arrival)
                {
                    min.add(minimums);
                }
                if(min.size() > 0)
                {
                    arrival = Collections.max(min);
                }
            }
            index--;
        }

        return arrival;
    }

    public static int TDTSPMTW(Customer[] c){
        int x = Integer.MAX_VALUE;
        //int toReturn = 0;
        if(topDownProcessing(c) && bottomUpProcessing(c)){
            for(int i = 0; i < c.length-1; i++){
                ArrayList<TimeWindow> tw = c[i].getTimeWindows();
                for(int j = 0; j < tw.size(); j++){
                    int departure, arrival;
                    departure = constructFeasibleSubSchedule(i+1, tw.get(j).getEndTime(), c);
                    arrival = constructDominantSubSchedule(i-1, tw.get(j).getEndTime() - tw.get(j).getDuration(), c);
                    //System.out.println(departure);
                    //System.out.println(arrival);
                    System.out.println(departure-arrival);

                    int temp = departure - arrival;
                    if(x > temp){
                        x = temp;
                    }
                }
            }
        }

        return x;
    }


}
