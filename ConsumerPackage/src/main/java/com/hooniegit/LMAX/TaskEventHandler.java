package com.hooniegit.LMAX;

import com.lmax.disruptor.WorkHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import com.hooniegit.Tasks.*;

/**
 * 해당 스크립트에서 수신 데이터를 역직렬화하고, 해당 데이터를 기반으로 작업을 실행합니다.
 * - 모든 작업은 직렬화가 완료된 후에 실행이 가능합니다.
 * - 데이터 가공 및 전송 로직은 메인 이벤트 스레드에서 실행합니다.
 * - 기타 로직은 별도의 스레드를 열어 실행합니다.
 */

public class TaskEventHandler implements WorkHandler<TaskEvent> {
	
	public void deserialize(ConsumerRecord<String, String> record) {
		// ** 이 곳에 데이터 역직렬화 로직을 구성해야 합니다. **
	}
	
    @Override
    public void onEvent(TaskEvent event) {
        ConsumerRecord<String, String> record = event.getRecord();

        // TEST
        System.out.println("Receive message: " + record.value() + ", Partition: "
                + record.partition() + ", Offset: " + record.offset() + ", by ThreadID: "
                + Thread.currentThread().getId());

        // ** 이 곳에 데이터 역직렬화 로직(deserialize)이 포함되어야 합니다. **
        
        // ** 이 곳에 데이터 가공 및 전송 로직이 포함되어야 합니다. **

        // 기타 로직을 스레드로 구성하고 실행합니다.
        List<Thread> threadList = new ArrayList<>();
        threadList.add(new Thread(() -> {
            try {
                Tasks.SAMPLE_FileWriter(record);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }));
        threadList.add(new Thread(Tasks::SAMPLE_Hello));

        // 기타 로직 스레드를 실행합니다. 스레드의 응답은 기다리지 않습니다.
        for (Thread thread : threadList) {
            thread.start();
        }
    }
}

