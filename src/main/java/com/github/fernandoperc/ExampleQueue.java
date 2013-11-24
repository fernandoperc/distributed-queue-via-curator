package com.github.fernandoperc;

import java.util.Random;
import org.apache.curator.ensemble.fixed.FixedEnsembleProvider;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.queue.DistributedQueue;
import org.apache.curator.framework.recipes.queue.QueueBuilder;
import org.apache.curator.retry.RetryOneTime;

public class ExampleQueue {
    private DistributedQueue<ExampleQueueData> queue = null;
    private String connectionString = null;
    private String operation = null; 
    
    public static void main( String[] args ) {
        if(args.length < 2) {
            System.out.println("ERROR: Must pass at least 2 arguments!");
            return;
        } 
        
        ExampleQueue exampleQueue = new ExampleQueue();

        try {
            exampleQueue.init(args[0], args[1]);
        
        } catch (Exception ex) {
           System.out.println("Something wrong happened...");
           System.out.println("Exception message: " + ex.getMessage());
        }
    }
    
    public void init(String ConnectionString, String Operation) 
            throws Exception {
        
        this.connectionString = ConnectionString;
        this.operation = Operation;
        
        CuratorFramework client = CuratorFrameworkFactory.builder()
            .retryPolicy(new RetryOneTime(10))
            .ensembleProvider(new FixedEnsembleProvider(this.connectionString))
            .build();
        
        client.start();
        
        ExampleQueueConsumer queueConsumer = new ExampleQueueConsumer();
        ExampleQueueSerializer queueSerializer = new ExampleQueueSerializer();
        
        QueueBuilder<ExampleQueueData> queueBuilder = QueueBuilder.builder(
                client, 
                queueConsumer, 
                queueSerializer, 
                "/example/queue");
        
        queueBuilder.lockPath("/example/lock");
        
        this.queue = queueBuilder.buildQueue();
        this.queue.start();
        
        if (this.operation.equals("fill")) {
            
            System.out.println("Starting to fill queue...");
            
            try {
                
               fillDistributedQueue();
                
                
            } catch (Exception ex) {
                System.out.println("Something wrong happened...");
                System.out.println("Exception message: " + ex.getMessage());
            }
            
            
        } else if (this.operation.equals("consume")) {
            
            System.out.println("Starting to consume queue...");
            consumeDistributedQueue();
        }
        
        
        
    }
    
    public void fillDistributedQueue() throws InterruptedException, Exception {
        
        String data;
        Random random = new Random();
        System.out.println("Filling queue in 10 seconds...");
        Thread.sleep(10000);
        for (int i = 0; i < 20; i++) {
            ExampleQueueData queueData = new ExampleQueueData();
            data = "data " + i;
            queueData.setData(data);
             System.out.println("Queued: " + data);
            this.queue.put(queueData);
            Thread.sleep(random.nextInt(3000));
        }
        
    }
    
    public void consumeDistributedQueue() throws InterruptedException {
        while(true) Thread.sleep(3000);
    }
    
    public String getConnectionString() {
        return this.connectionString;
    }

    public void setConnectionString(String connectionStr) {
        this.connectionString = connectionStr;
    }

    public String getOperation() {
        return this.operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    
}
