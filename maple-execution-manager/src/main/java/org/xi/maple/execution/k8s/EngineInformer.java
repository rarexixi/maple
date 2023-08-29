package org.xi.maple.execution.k8s;

import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.ConfigBuilder;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import io.fabric8.kubernetes.client.informers.ResourceEventHandler;
import io.fabric8.kubernetes.client.informers.SharedIndexInformer;
import org.xi.maple.execution.k8s.flink.crds.FlinkDeployment;
import org.xi.maple.execution.k8s.flink.crds.FlinkDeploymentList;

public class EngineInformer {

    public void startK8sEngine(String master, String token, String namespace) {
        try {

            Config config = new ConfigBuilder().withMasterUrl(master).withTrustCerts(true).withOauthToken(token).build();

            try (KubernetesClient kubernetesClient = new KubernetesClientBuilder().withConfig(config).build()) {

                SharedIndexInformer<FlinkDeployment> inform = kubernetesClient.resources(FlinkDeployment.class, FlinkDeploymentList.class).inNamespace(namespace).inform(new ResourceEventHandler<FlinkDeployment>() {
                    @Override
                    public void onAdd(FlinkDeployment obj) {
                        obj.getMetadata().getLabels().getOrDefault("maple-id", "0");
                        System.out.println("onAdd: " + obj.getMetadata().getName());
                    }

                    @Override
                    public void onUpdate(FlinkDeployment oldObj, FlinkDeployment newObj) {
                        System.out.println("onUpdate: " + newObj.getMetadata().getName());
                    }

                    @Override
                    public void onDelete(FlinkDeployment obj, boolean deletedFinalStateUnknown) {
                        System.out.println("onDelete: " + obj.getMetadata().getName());
                    }
                }, 10000L);

                inform.start();
                System.in.read();
                inform.close();
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
