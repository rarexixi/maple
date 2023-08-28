package org.xi.maple.k8s.spark.watcher;

import io.fabric8.kubernetes.client.Watcher;
import io.fabric8.kubernetes.client.WatcherException;
import org.xi.maple.k8s.flink.crds.FlinkDeployment;
import org.xi.maple.k8s.spark.crds.SparkDeployment;

public class SparkDeploymentWatcher implements Watcher<SparkDeployment> {
    @Override
    public void eventReceived(Action action, SparkDeployment deployment) {

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