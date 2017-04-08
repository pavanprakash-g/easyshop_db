package com.easyshop;

/**
 * Created by admin-hp on 4/4/17.
 */
import com.easyshop.util.OrderUtil;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Configuration
@ComponentScan(basePackages = "com.easyshop")
@EnableScheduling

public class AppSchedule {

    //@Scheduled(fixedRate = 20000)
    public static void doTask(){
        System.out.println("We are in App Schedule");
    }
}
