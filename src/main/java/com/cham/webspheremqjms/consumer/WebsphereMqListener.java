package com.cham.webspheremqjms.consumer;

import com.cham.webspheremqjms.domain.Tweet;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class WebsphereMqListener {

     @JmsListener(destination = "DEV.QUEUE.1", containerFactory = "JmsListenerContainerFac", selector = "tweet_prop='exec'")
     //@JmsListener(destination = "DEV.QUEUE.1", containerFactory = "JmsListenerContainerFac")
    public void receiveMessage(Tweet tweet) {
        System.out.println("Received <" + tweet + ">");
    }

}
