package com.hooniegit.Consumer;

import java.util.Map;
import java.util.HashMap;
import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadFactory;

import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.WorkHandler;
import com.lmax.disruptor.dsl.ProducerType;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.clients.consumer.ConsumerRebalanceListener;

import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.WakeupException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.hooniegit.LMAX.*;
import com.hooniegit.Modules.ThreadMonitor;

/**
 * 해당 스크립트에서 단일 컨슈머 스레드를 구성합니다. 
 * - 수신한 데이터를 기반으로 작업 스레드(역직렬화 로직 포함)를 병렬 실행합니다.
 * - 오프셋 커밋 과정은 수동으로 진행합니다.
 */

public class ConsumerThread implements Runnable {
	// ** 데이터 형식자<Key, Message>는 기업 표준으로 수정되어야 합니다 **
	private final KafkaConsumer<String, String> consumer;
	private final String topic;
	private Map<TopicPartition, OffsetAndMetadata> currentOffsets = new HashMap<>();
	private final AtomicBoolean running = new AtomicBoolean(true);
	private ConsumerGroup group;
	private RingBuffer<TaskEvent> ringBuffer;
	private static final Logger logger = LogManager.getLogger(ConsumerThread.class);

	public ConsumerThread(Properties props, String topic, ExecutorService executor) {
		// TaskEvent 객체 기반의 디스럽터를 구성합니다.
        ThreadFactory threadFactory = Executors.defaultThreadFactory();
        Disruptor<TaskEvent> disruptor = new Disruptor<>(
                TaskEvent::new,
                1024,
                threadFactory,
                ProducerType.SINGLE,
                new BlockingWaitStrategy() // SleepingWaitStrategy 또는 BlockingWaitStrategy 전략을 사용합니다.
        );
        
        // 복수의 이벤트 핸들러를 추가하고, 이를 기반으로 링 버퍼를 구성합니다.
        WorkHandler<TaskEvent>[] handlers = new TaskEventHandler[20];
        for (int i = 0; i < handlers.length; i++) {
            handlers[i] = new TaskEventHandler();
        }
        disruptor.handleEventsWithWorkerPool(handlers);
        disruptor.start();
        ringBuffer = disruptor.getRingBuffer();
		
        // 컨슈머를 구성하고 토픽을 구독합니다.
		this.consumer = new KafkaConsumer<>(props);
		this.topic = topic;
		this.consumer.subscribe(Arrays.asList(this.topic), new HandleRebalance());
	}
	
	// 리밸런싱 발생 시 컨슈머 스레드에서 수행할 작업을 정의합니다.
	private class HandleRebalance implements ConsumerRebalanceListener{
		// 파티션이 추가되었을 경우:  
		public void onPartitionsAssigned(Collection<TopicPartition>partitions) {
		}
		
		// 파티션이 제거되었을 경우:
		public void onPartitionsRevoked(Collection<TopicPartition>partitions) {
			// 서버에 현재 오프셋 정보를 커밋합니다.
			consumer.commitSync(currentOffsets);
		}
	}

	@Override
	public void run() {
		try {
			while (running.get()) {
				// poll 작업을 수행하고, 반환값을 기반으로 ConsumerRecords 객체로 구성합니다.
				// ** 데이터 형식자<Key, Message>는 기업 표준으로 수정되어야 합니다 **
				ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
				
				// 수신 데이터를 기반으로 한 작업 스레드를 시작합니다.
				// ** 데이터 형식자<Key, Message>는 기업 표준으로 수정되어야 합니다 **
				for (ConsumerRecord<String, String> record : records) {
					
					// 링 버퍼를 통해 이벤트 핸들러에 이벤트를 전달합니다.
					ringBuffer.publishEvent((event, sequence) -> event.setRecord(record));

					// 수동 오프셋 커밋을 위해 오프셋 정보를 갱신합니다.
					currentOffsets.put(
							new TopicPartition(record.topic(), record.partition()), 
							new OffsetAndMetadata(record.offset()+1, null));
					
				} 
				// 수동으로 오프셋을 커밋합니다.
				consumer.commitAsync(currentOffsets, null);
			}
		} catch (WakeupException e) {
			String message = "[Expected] Wakeup Activated.";
			logger.error(message);
		} catch (Exception e) {
			String message = String.format("[Others] %s", e.getMessage());
			logger.error(message);
		} finally {
			try {
				// 스레드에 문제가 생기면 오프셋 정보를 서버에 커밋합니다.
				consumer.commitSync(currentOffsets);
			} finally {
				// 컨슈머 객체를 종료하고, 그룹이 새로운 컨슈머를 생성하도록 지시합니다. 
				consumer.close();
				group.notifyConsumerError(this);
			}
		}
	}
	
	// 스레드에 문제가 발생하면 poll 작업을 중단시킵니다.
    public void shutdown() {
        consumer.wakeup();
    }
}