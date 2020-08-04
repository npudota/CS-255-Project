import java.util.ArrayList;

public class Customer {
    private String name;
    private ArrayList<TimeWindow> timeWindows;

    public Customer(String name, ArrayList<TimeWindow> w){
        this.name = name;
        this.timeWindows = w;
    }

    public ArrayList<TimeWindow> getTimeWindows(){
        return timeWindows;
    }

    public void setTimeWindows(ArrayList<TimeWindow> t){
        this.timeWindows = t;
    }
}
