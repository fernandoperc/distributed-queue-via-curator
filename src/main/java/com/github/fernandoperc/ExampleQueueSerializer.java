package com.github.fernandoperc;

import org.apache.curator.framework.recipes.queue.QueueSerializer;

public class ExampleQueueSerializer implements QueueSerializer<ExampleQueueData> {

    public byte[] serialize(ExampleQueueData item) {
        return item.getData().getBytes();
    }

    public ExampleQueueData deserialize(byte[] bytes) {

        ExampleQueueData queueData = new ExampleQueueData();
        queueData.setData(new String(bytes));
        return queueData;
    }
    
}






