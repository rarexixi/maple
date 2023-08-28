package org.xi.maple.k8s;

import io.fabric8.kubernetes.client.*;
import io.fabric8.kubernetes.client.informers.ResourceEventHandler;
import io.fabric8.kubernetes.client.informers.SharedIndexInformer;
import org.apache.commons.cli.*;
import org.xi.maple.k8s.flink.crds.FlinkDeployment;
import org.xi.maple.k8s.flink.crds.FlinkDeploymentList;

public class Main {
    static String NAMESPACE = "flink-dev";
    static final String TOKEN_NAME = "token";
    static final String MASTER_NAME = "master";
    static final String DEPLOYMENT_NAME = "deployment";

    public static void main(String[] args) throws ParseException {
        Option tokenOption = new Option("t", TOKEN_NAME, true, "token");
        System.out.println(tokenOption.getLongOpt());
        Option masterOption = new Option("m", MASTER_NAME, true, "master-url");
        Option deploymentOption = new Option("d", DEPLOYMENT_NAME, true, "deployment path");
        tokenOption.setRequired(true);
        masterOption.setRequired(true);
        deploymentOption.setRequired(true);

        Options options = new Options();
        options.addOption(tokenOption);
        options.addOption(masterOption);
        options.addOption(deploymentOption);

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();

        // 输出使用方式
        // formatter.printHelp("xi-submit [OPTIONS]", options);

        CommandLine cmd = parser.parse(options, args);

        String token = cmd.getOptionValue(TOKEN_NAME);
        String master = cmd.getOptionValue(MASTER_NAME);
        try {

            Config config = new ConfigBuilder().withMasterUrl(master).withTrustCerts(true).withOauthToken(token).build();

            try (KubernetesClient kubernetesClient = new KubernetesClientBuilder().withConfig(config).build()) {

                SharedIndexInformer<FlinkDeployment> inform = kubernetesClient.resources(FlinkDeployment.class, FlinkDeploymentList.class).inNamespace(NAMESPACE).inform(new ResourceEventHandler<FlinkDeployment>() {
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