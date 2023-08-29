package org.xi.maple.execution.k8s.flink.watcher;

import io.fabric8.kubernetes.client.Watcher;
import io.fabric8.kubernetes.client.WatcherException;
import org.xi.maple.execution.k8s.flink.crds.FlinkDeployment;

public class FlinkDeploymentWatcher implements Watcher<FlinkDeployment> {
    @Override
    public void eventReceived(Action action, FlinkDeployment deployment) {

        switch (action) {
            case ADDED:
                break;
            case DELETED:
                break;
            case ERROR:
                break;
            case BOOKMARK:
                break;
            case MODIFIED:
                break;
            default:
                break;
        }
        System.out.println(action.name() + " " + deployment.getMetadata().getName());
    }

    @Override
    public void onClose(WatcherException e) {
        System.out.println("Watcher close due to: ");
        e.printStackTrace();
    }
}