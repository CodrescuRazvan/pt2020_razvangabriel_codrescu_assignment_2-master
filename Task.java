public class Task implements Comparable<Task>{
    private int arrivalTime;
    private int processingTime;
    private int id;

    public Task(int arrivalTime, int processingTime, int id) {
        this.arrivalTime = arrivalTime;
        this.processingTime = processingTime;
        this.id = id;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(int arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public int getProcessingTime() {
        return processingTime;
    }

    public void setProcessingTime(int processingTime) {
        this.processingTime = processingTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public int compareTo(Task task) {
        if(arrivalTime < task.getArrivalTime())
            return -1;
        else if(arrivalTime == task.getArrivalTime())
                return 0;
        else
            return 1;
    }
}
