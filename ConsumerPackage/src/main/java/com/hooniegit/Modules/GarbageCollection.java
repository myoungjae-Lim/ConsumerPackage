package com.hooniegit.Modules;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class GarbageCollection {
    public static void run() {
        // 스케줄러를 통해 60초에 1번씩 메서드를 실행합니다.
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(GarbageCollection::cleanGarbage, 0, 10, TimeUnit.SECONDS);
    }
    
    private static void cleanGarbage() {
    	System.gc();
    	System.out.println("Garbage Memory Collected <<<<<<");
    }
}
