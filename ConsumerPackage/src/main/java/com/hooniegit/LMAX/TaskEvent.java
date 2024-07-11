package com.hooniegit.LMAX;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import com.lmax.disruptor.EventFactory;

/**
 * 해당 스크립트에서 카프카 서버로부터 수신한 데이터를 캡슐화하여 이벤트를 표현합니다.
 * - 캡슐화한 데이터는 Disruptor Ring Buffer을 통해 전달합니다.
 */

public class TaskEvent {
	// ** 데이터 형식자<Key, Message>는 기업 표준으로 수정되어야 합니다 **
    private ConsumerRecord<String, String> record;

    // ** 데이터 형식자<Key, Message>는 기업 표준으로 수정되어야 합니다 **
    public ConsumerRecord<String, String> getRecord() {
        return record;
    }

    // ** 데이터 형식자<Key, Message>는 기업 표준으로 수정되어야 합니다 **
    public void setRecord(ConsumerRecord<String, String> record) {
        this.record = record;
    }
    
    public void clear() {
        // 클린업 로직
        this.record = null;
    }
    
    public final static EventFactory<TaskEvent> EVENT_FACTORY = TaskEvent::new;
}