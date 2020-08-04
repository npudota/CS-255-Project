public class TimeWindow {
    private int startTime;
    private int endTime;
    private int duration; //service time + travel time

    public TimeWindow(int startTime, int endTime, int duration){
        this.startTime = startTime;
        this.endTime = endTime;
        this.duration = duration;
    }

    public int getStartTime() {
        return startTime;
    }

    public int getEndTime() {
        return endTime;
    }

    public int getDuration() {
        return duration;
    }
}
