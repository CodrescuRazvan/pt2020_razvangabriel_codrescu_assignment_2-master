import java.awt.*;
import java.io.*;
import java.nio.channels.SeekableByteChannel;
import java.util.*;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import static java.lang.Thread.*;

public class SimulationManager implements Runnable {
    public int timeLimit;
    public int maxProcessingTime;
    public int minProcessingTime;
    public int numberOfServers;
    public int numberOfTasks;
    public int maxArrivalTime;
    public int minArrivalTime;
    public int allProcTime;
    public PrintWriter writer;
    private Scheduler scheduler;

    private List<Task> generatedTasks;

    public SimulationManager(String[] arg) throws FileNotFoundException, UnsupportedEncodingException {
        dataRdWr(arg[0]);
        writer = new PrintWriter(arg[1], "UTF-8");
        generatedTasks = new ArrayList<Task>();
        scheduler = new Scheduler(numberOfServers);
        for (int i = 0; i < numberOfServers; i++) {
            Thread t = new Thread();
            t.start();
        }
    }

    private void generateRandomTasks() {
        int arrTime;
        int procTime = 0;
        allProcTime = 0;
        for (int i = 0; i < numberOfTasks; i++) {
                arrTime = ThreadLocalRandom.current().nextInt(minArrivalTime, maxArrivalTime);
            procTime = ThreadLocalRandom.current().nextInt(minProcessingTime, maxProcessingTime);
            allProcTime += procTime;
            Task t = new Task(arrTime, procTime, i + 1);
            generatedTasks.add(t);
        }
        Collections.sort(generatedTasks);
        allProcTime = allProcTime / numberOfTasks;
    }

    @Override
    public void run() {
            int currentTime = 0;
            generateRandomTasks();
            while (currentTime <= timeLimit) {
                writer.write("Time: " + currentTime + "\n");
                writer.write("Waiting clients: ");
                Iterator<Task> taskIterator = generatedTasks.iterator();
                while(taskIterator.hasNext()){
                    Task task = taskIterator.next();
                    if(task.getArrivalTime() == currentTime){
                        scheduler.distributionMethod(task);
                        taskIterator.remove();
                    }
                    else break;
                }
                if(generatedTasks.isEmpty() || currentTime < generatedTasks.get(0).getArrivalTime()){
                    int x = 0;
                    for(int i = 0; i < numberOfServers; i++)
                        if(scheduler.getServers().get(i).getTasks().isEmpty())
                            x++;
                    if(x == numberOfServers && generatedTasks.isEmpty()) {
                        writer.write("\nAverage waiting time: " + allProcTime);
                        writer.close();
                        System.exit(0);
                    }
                }
                for (Task task : generatedTasks)
                    writer.write("(" + task.getId() + "," +
                            task.getArrivalTime() + "," +
                            task.getProcessingTime() +");");
                    writer.write("\n");
                for (int i = 0; i < numberOfServers; i++) {
                    writer.write("Queue " + i + "\n");
                    if(scheduler.getServers().get(i).getTasks().isEmpty())
                        writer.write("Closed\n");
                    else{
                        for(Task task: scheduler.getServers().get(i).getTasks()){
                            writer.write("(" + task.getId() + "," +
                                    task.getArrivalTime() + "," +
                                    task.getProcessingTime() +");");
                        }
                        writer.write("\n");
                    }
                }
                currentTime++;
                writer.write("\n");
                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            writer.close();

    }

    public void dataRdWr(String arg) {
        String[] values;
        try {
            Scanner scanner = new Scanner(new File(arg));
            numberOfTasks = scanner.nextInt();
            numberOfServers = scanner.nextInt();
            timeLimit = scanner.nextInt();
            scanner.nextLine();
            values = scanner.nextLine().split(", ");
            minArrivalTime = Integer.parseInt(values[0]);
            maxArrivalTime = Integer.parseInt(values[1]);
            values = scanner.nextLine().split(", ");
            minProcessingTime = Integer.parseInt(values[0]);
            maxProcessingTime = Integer.parseInt(values[1]);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        SimulationManager gen = new SimulationManager(args);
        Thread t = new Thread(gen);
        t.start();
    }
}
