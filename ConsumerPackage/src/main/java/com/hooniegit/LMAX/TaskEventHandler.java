package com.hooniegit.LMAX;

import com.lmax.disruptor.WorkHandler;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.hooniegit.Tasks.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 해당 스크립트에서 수신 데이터를 역직렬화하고, 해당 데이터를 기반으로 작업을 실행합니다.
 * - 주요 작업에 소요되는 시간을 측정 및 로깅합니다.
 * - 데이터 가공 및 전송 로직은 메인 이벤트 스레드에서, 기타 로직은 별도의 스레드를 열어 실행합니다.
 */

public class TaskEventHandler implements WorkHandler<TaskEvent> {
    private static final Logger logger = LogManager.getLogger(TaskEventHandler.class);

    // 경과 시간을 측정하고 로그를 남깁니다.
    public void measureExecutionTime(Runnable task) {
        long startTime = System.currentTimeMillis();
        try {
            task.run();
        } finally {
            long endTime = System.currentTimeMillis();
            long elapsedTime = endTime - startTime;
            logger.info(String.format("Spent Time : %d ms", elapsedTime));
        }
    }

    @Override
    public void onEvent(TaskEvent event) {
        measureExecutionTime(() -> {
            ConsumerRecord<String, String> record = event.getRecord();

            // TEST: 콘솔에 수신 데이터를 출력합니다.
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
                    logger.error("Error in SAMPLE_FileWriter", e);
                }
            }));
            threadList.add(new Thread(Tasks::SAMPLE_Hello));
            for (Thread thread : threadList) {
                thread.start();
            }
        });
    }

    private void deserialize(ConsumerRecord<String, String> record) {
        // ** 이 곳에 데이터 역직렬화 로직을 구성해야 합니다. **
    }
}
