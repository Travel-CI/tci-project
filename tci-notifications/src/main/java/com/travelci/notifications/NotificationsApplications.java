package com.travelci.notifications;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Created by Julien on 12/07/2017.
 */
//@EnableDiscoveryClient
@SpringBootApplication
public class NotificationsApplications {
    public static void main(String args[]){
        SpringApplication.run(NotificationsApplications.class, args);
    }
}