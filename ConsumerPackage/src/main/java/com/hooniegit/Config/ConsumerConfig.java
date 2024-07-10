package com.hooniegit.Config;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import org.ini4j.Ini;
import org.ini4j.InvalidFileFormatException;

/**
 * 해당 스크립트에서 컨슈머 및 그룹 관련 설정을 입력합니다.
 * - 설정값은 운영 환경에 맞추어 수정할 수 있습니다.
 * - 연결 구성 정보는 외부 로직을 통해 암호화할 수 있습니다.
 */

public class ConsumerConfig {
	private static String topic;
	private static int range;
	private static Properties props = new Properties();

	public static String getTopic() {
		topic = "DemoTopic7";
		return topic;
	}
	
	public static int getRange() {
		range = 7;
		return range;
	}
	
	public static Properties getProps() {
		// 외부의 srvr.ini 파일을 통해 인스턴스 정보를 호출합니다.
		File fileToParse = new File("src\\main\\java\\com\\hooniegit\\Config\\srvr.ini");
		try {
			Ini ini = new Ini(fileToParse);
			String srvr = ini.get("kafka", "bootstrap.servers");
			props.put("bootstrap.servers", srvr);
		} catch (InvalidFileFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		props.put("group.id", "DemoGroup7");
		props.put("enable.auto.commit", "False");
		props.put("auto.commit.interval.ms", "1000");
		props.put("session.timeout.ms", "30000");
		props.put("auto.offset.reset", "latest");
		props.put("partition.assignment.strategy", "org.apache.kafka.clients.consumer.CooperativeStickyAssignor");
		// ** 역직렬화 로직(De-Serializer)은 기업 표준으로 수정되어야 합니다 **
		props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		return props;
	}
}

