package com.hooniegit;

import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.context.annotation.EnableAspectJAutoProxy;
import com.hooniegit.Consumer.ConsumerGroup;
import com.hooniegit.Modules.GarbageCollection;
import com.hooniegit.Modules.ThreadMonitor;
import com.hooniegit.Config.ConsumerConfig;

/**
 * 해당 스크립트에서 어플리케이션을 실행합니다.
 * - com.hooniegit.Config 이하의 Configuration 클래스를 참조합니다(환경 설정은 해당 위치에서 이루어집니다).
 */

@SpringBootApplication
@EnableAspectJAutoProxy
public class ConsumerApplication {
	public static void main(String[] args) {
		// 컨슈머 그룹 객체를 구성합니다.
		String topic = ConsumerConfig.getTopic();
		int range = ConsumerConfig.getRange();
		ConsumerGroup group = new ConsumerGroup(topic, range);
		
		GarbageCollection.run();
		
		// 사용중인 스레드 개수를 모니터링합니다.
		ThreadMonitor.startGenerating();
		
		// 컨슈머 그룹 스레드의 종료 시 shutdown 메서드를 실행합니다.
		Runtime.getRuntime().addShutdownHook(new Thread(group::shutdown));
	}
}