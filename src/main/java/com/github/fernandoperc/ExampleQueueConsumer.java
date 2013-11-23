package com.github.fernandoperc;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.queue.QueueConsumer;
import org.apache.curator.framework.state.ConnectionState;

public class ExampleQueueConsumer implements QueueConsumer<ExampleQueueData>  {
    
    public void stateChanged(CuratorFramework framework, ConnectionState state) {
        System.out.println("State Changed: [" + state + "]");
    }
    
    public void consumeMessage(ExampleQueueData queueData) throws Exception {
        System.out.println("Consumindo fila distribuida: [" + queueData.getData() + "]");		
    }
    
}
