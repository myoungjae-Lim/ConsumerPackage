package com.hooniegit.Modules;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.hooniegit.Config.ConsumerConfig;

/**
 * 해당 스크립트에서 스레드 개수를 측정하고 기타 기능을 수행합니다.
 * - 
 */

public class ThreadMonitor {
    private static final int THREAD_THRESHOLD_ACTIVE = ConsumerConfig.getRange() * 22 + 9;
    private static final int THREAD_THRESHOLD_DAEMON = ConsumerConfig.getRange() + 7;
    private static final Logger logger = LogManager.getLogger(ThreadMonitor.class);

    public static void startGenerating() {
        // 스케줄러를 통해 5초에 1번씩 메서드를 실행합니다.
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(ThreadMonitor::measureThreads, 0, 5, TimeUnit.SECONDS);
    }

    public static void measureThreads() {
        // 현재 활성화된 스레드의 수를 계산합니다.
        ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
        int activeThreadCount = threadMXBean.getThreadCount();
        int daemonThreadCount = threadMXBean.getDaemonThreadCount();
        int peakThreadCount = threadMXBean.getPeakThreadCount();
        // long totalStartedThreadCount = threadMXBean.getTotalStartedThreadCount();

        System.out.println(">>>    활성화 스레드 : " + activeThreadCount);
        System.out.println(">>>      데몬 스레드 : " + daemonThreadCount);
        System.out.println(">>>      최대 스레드 : " + peakThreadCount);
        // System.out.println(">>> 총 시작된 스레드 : " + totalStartedThreadCount);

        // 스레드 개수에 이상이 있으면 로그 메세지를 출력합니다.
        // 자동 복구 기능이 동작하지 않는 상황으로 예상되기 때문에 후속 조치가 필요합니다.
        if (activeThreadCount < THREAD_THRESHOLD_ACTIVE | daemonThreadCount < THREAD_THRESHOLD_DAEMON) {
        	logger.error("Thread is Deprecated. Logic is In Danger.");
        }
    }
}
