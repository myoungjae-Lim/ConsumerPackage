package com.hooniegit.Consumer;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.hooniegit.Config.ConsumerConfig;

/**
 * 해당 스크립트에서 컨슈머 그룹 객체를 구성합니다.
 * - 컨슈머 그룹 객체를 통해 컨슈머 스레드를 실행 및 관리합니다.
 */

public final class ConsumerGroup {
	private Properties props;
	private final String topic;
	private ExecutorService executor = Executors.newCachedThreadPool();
	private List<ConsumerThread> consumers;

	// 그룹 인스턴스 생성 시 컨슈머를 실행합니다.
	public ConsumerGroup(String topic, int numberOfConsumers) {		
	    this.topic = topic;
	    props = ConsumerConfig.getProps();
	    consumers = new ArrayList<>();
		for (int i = 0; i < numberOfConsumers; i++) {
			startNewConsumer();
		}
	}
	
	// 새로운 컨슈머 스레드를 시작합니다.
    public void startNewConsumer() {
        ConsumerThread consumer = new ConsumerThread(this.props, this.topic, this.executor);
        consumers.add(consumer);
        executor.submit(consumer);
    }

    // 문제가 되는 컨슈머를 리스트에서 삭제하고, 새로운 컨슈머를 등록 및 실행합니다.
    public void notifyConsumerError(ConsumerThread consumer) {
        consumers.remove(consumer);
        startNewConsumer();
    }

    // 문제 상황 발생 시 컨슈머 스레드를 종료시킵니다.
    public void shutdown() {
        for (ConsumerThread consumer : consumers) {
            consumer.shutdown();
        }
        executor.shutdown();
    }
}
