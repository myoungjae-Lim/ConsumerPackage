package com.hooniegit.Tasks;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.kafka.clients.consumer.ConsumerRecord;

/**
 * 해당 스크립트에서 역직렬화한 데이터로 수행할 작업을 정의합니다.
 * - 이 곳에 실제로 사용할 기능의 메서드를 추가해야 합니다.
 * - 각 작업은 별도의 스레드를 통해 실행됩니다.
 * - 해당 클래스에서는 인스턴스를 구성하지 않고 static 메서드를 정의해서 사용합니다.
 */

public class Tasks {
	// [예제 함수] 수신한 데이터를 로그 파일에 기록합니다.
	public static void SAMPLE_FileWriter(ConsumerRecord<String, String> record) throws IOException{
		long startTime = System.currentTimeMillis();
		
        String topic = record.topic();
		int partition = record.partition();
		// String key = record.key();
		String value = record.value();
		long timestamp = record.timestamp();
		String content = String.format("[INFO] value: %s, timestamp: %d\n", value, timestamp); 
		
		String logDir = "C:\\Users\\dhkim\\Desktop\\Logs\\KafkaJavaLogs\\";
		String filePath = logDir + String.valueOf(partition) + ".log";
		
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(filePath, true));
            writer.write(content);
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        
        // 경과 시간을 측정합니다.
        long endTime = System.currentTimeMillis();
        long elapsedTime = endTime - startTime;
        // ** Need Logging Logics Here.. **
	}

	// [예제 함수] 터미널 환경에 "Hello, Kafka!" 메세지를 출력합니다.
	public static void SAMPLE_Hello() {
		System.out.println("Hello, Kafka!");
	}
}
