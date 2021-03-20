import java.util.ArrayList;
import java.util.List;

public class Scheduler {
    private List<Server> servers;
    private int maxNoServers;
    private int avgWaitingPer = 0;

    public Scheduler(int maxNoServers){
        this.maxNoServers = maxNoServers;
        servers = new ArrayList<>();
        for(int i = 0; i < maxNoServers; i++){
            Server server = new Server();
            servers.add(server);
            Thread thread = new Thread(server);
            thread.start();
        }
    }

    public void distributionMethod(Task task){
        int bestTime = 9999;
        int bestQueuePos = 0;
        for(int i = 0; i < maxNoServers; i++){
                if(servers.get(i).getWaitingPeriod().get() < bestTime){
                    bestTime = servers.get(i).getWaitingPeriod().get();
                    bestQueuePos = i;
            }
        }
        servers.get(bestQueuePos).addTask(task);
    }

    public int getAvgWaitingPer(){
        for(int i = 0; i < maxNoServers; i++)
            avgWaitingPer += servers.get(i).getWaitingPeriod().get();
        avgWaitingPer = avgWaitingPer / maxNoServers;
        return avgWaitingPer;
    }

    public void setAvgWaitingPer(int avgWaitingPer) {
        this.avgWaitingPer = avgWaitingPer;
    }

    public List<Server> getServers(){
        return servers;
    }

    public int getMaxNoServers() {
        return maxNoServers;
    }

    public void setMaxNoServers(int maxNoServers) {
        this.maxNoServers = maxNoServers;
    }

}
