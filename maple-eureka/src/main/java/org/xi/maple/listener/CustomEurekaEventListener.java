package org.xi.maple.listener;


import com.netflix.discovery.EurekaEvent;
import com.netflix.discovery.EurekaEventListener;
import org.springframework.cloud.netflix.eureka.server.event.EurekaInstanceCanceledEvent;
import org.springframework.cloud.netflix.eureka.server.event.EurekaInstanceRegisteredEvent;
import org.springframework.cloud.netflix.eureka.server.event.EurekaInstanceRenewedEvent;
import org.springframework.cloud.netflix.eureka.server.event.EurekaRegistryAvailableEvent;
import org.springframework.cloud.netflix.eureka.server.event.EurekaServerStartedEvent;
import org.springframework.stereotype.Component;

@Component
public class CustomEurekaEventListener implements EurekaEventListener {

    @Override
    public void onEvent(EurekaEvent event) {
        if (event instanceof EurekaInstanceCanceledEvent) {
            EurekaInstanceCanceledEvent instanceCanceledEvent = (EurekaInstanceCanceledEvent) event;
            System.out.println("Instance canceled: " + instanceCanceledEvent.getAppName());
        } else if (event instanceof EurekaInstanceRegisteredEvent) {
            EurekaInstanceRegisteredEvent instanceRegisteredEvent = (EurekaInstanceRegisteredEvent) event;
            System.out.println("Instance registered: " + instanceRegisteredEvent.getInstanceInfo().getAppName());
        } else if (event instanceof EurekaInstanceRenewedEvent) {
            EurekaInstanceRenewedEvent instanceRenewedEvent = (EurekaInstanceRenewedEvent) event;
            System.out.println("Instance renewed: " + instanceRenewedEvent.getInstanceInfo().getAppName());
        } else if (event instanceof EurekaRegistryAvailableEvent) {
            EurekaRegistryAvailableEvent registryAvailableEvent = (EurekaRegistryAvailableEvent) event;
            System.out.println("Instance available: " + registryAvailableEvent.getSource());
        } else if (event instanceof EurekaServerStartedEvent) {
            EurekaServerStartedEvent serverStartedEvent = (EurekaServerStartedEvent) event;
            System.out.println("Instance status changed: " + serverStartedEvent.getSource());
        }
    }
}