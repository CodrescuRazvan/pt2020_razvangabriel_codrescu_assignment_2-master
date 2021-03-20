import java.util.Arrays;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

import static java.lang.Thread.sleep;

public class Server implements Runnable {
    private BlockingQueue<Task> tasks;
    private AtomicInteger waitingPeriod;

    public Server() {
        waitingPeriod = new AtomicInteger(0);
        tasks = new ArrayBlockingQueue<Task>(1024);
    }


    public void addTask(Task newTask) {
        tasks.add(newTask);
        waitingPeriod.incrementAndGet();
    }

    @Override
    public void run() {

        while (true) {
            Task taskFromServer = new Task(0, 0, 0);
            Task tFS = new Task(0, 0, 0);
            long sleeping;
            if (!tasks.isEmpty()) {
                taskFromServer = tasks.element();
                try {
                    if(taskFromServer.getProcessingTime() == 0)
                        tasks.take();
                } catch (InterruptedException e2) {
                    e2.printStackTrace();
                }
                if(taskFromServer.getProcessingTime() % 2 != 0 && taskFromServer.getProcessingTime() > 1)
                    sleeping = (taskFromServer.getProcessingTime() - 2) * 1000;
                else
                    sleeping = (taskFromServer.getProcessingTime() - 1) * 1000;
                taskFromServer.setProcessingTime(tasks.element().getProcessingTime() - 1);
                try {
                    if(tasks.element().getProcessingTime() == 0)
                        tasks.take();
                } catch (InterruptedException e2) {
                    e2.printStackTrace();
                }
                try {
                    sleep(sleeping);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                waitingPeriod.decrementAndGet();
                try{
                    sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public BlockingQueue<Task> getTasks() {
        return this.tasks;
    }

    public void setTasks(BlockingQueue<Task> tasks) {
        this.tasks = tasks;
    }

    public AtomicInteger getWaitingPeriod() {
        return waitingPeriod;
    }

    public void setWaitingPeriod(AtomicInteger waitingPeriod) {
        this.waitingPeriod = waitingPeriod;
    }

}