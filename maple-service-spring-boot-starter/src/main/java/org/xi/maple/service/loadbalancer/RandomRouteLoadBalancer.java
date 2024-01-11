package org.xi.maple.service.loadbalancer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.*;
import org.springframework.cloud.loadbalancer.core.*;
import org.springframework.http.HttpHeaders;
import org.xi.maple.common.constant.MapleHttpHeaders;
import org.xi.maple.common.util.ArrayUtils;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;
import java.util.stream.Collectors;

public class RandomRouteLoadBalancer implements ReactorServiceInstanceLoadBalancer {

    private static final Log log = LogFactory.getLog(RandomRouteLoadBalancer.class);
    final String serviceId;
    ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSupplierProvider;

    public RandomRouteLoadBalancer(ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSupplierProvider, String serviceId) {
        this.serviceId = serviceId;
        this.serviceInstanceListSupplierProvider = serviceInstanceListSupplierProvider;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Mono<Response<ServiceInstance>> choose(Request request) {
        final Function<ServiceInstance, Boolean> filter = getFilter(request);
        ServiceInstanceListSupplier supplier = serviceInstanceListSupplierProvider
                .getIfAvailable(NoopServiceInstanceListSupplier::new);
        return supplier.get(request).next()
                .map(serviceInstances -> processInstanceResponse(supplier, serviceInstances, filter));
    }

    @SuppressWarnings("rawtypes")
    private Function<ServiceInstance, Boolean> getFilter(Request request) {
        if (request.getContext() == null || !(request.getContext() instanceof RequestDataContext)) {
            return null;
        }

        RequestDataContext context = (RequestDataContext) request.getContext();
        HttpHeaders headers;
        if (context.getClientRequest() == null || (headers = context.getClientRequest().getHeaders()) == null) {
            return null;
        }

        String serviceIdPrefix = serviceId + ":";
        int serviceIdLength = serviceId.length() + 1;
        Function<List<String>, Optional<String>> getTarget =
                list -> list == null || list.isEmpty()
                        ? Optional.empty()
                        : list.stream()
                        .map(String::trim)
                        .filter(s -> s.startsWith(serviceIdPrefix) && s.length() > serviceIdLength)
                        .map(s -> s.substring(serviceIdLength))
                        .findAny();

        Optional<String> targetInstance = getTarget.apply(headers.get(MapleHttpHeaders.INSTANCE));
        if (targetInstance.isPresent()) {
            return instance -> targetInstance.get().equals(instance.getHost() + ":" + instance.getPort());
        }

        Optional<String> targetTag = getTarget.apply(headers.get(MapleHttpHeaders.TAG));
        return targetTag.<Function<ServiceInstance, Boolean>>map(s -> instance -> {
            String instanceTags = instance.getMetadata().get(MapleHttpHeaders.TAG);
            if (instanceTags == null || instanceTags.trim().isEmpty()) {
                return false;
            }
            return ArrayUtils.contains(instanceTags.trim().split("\\s*,\\s*"), s);
        }).orElse(null);
    }

    private Response<ServiceInstance> processInstanceResponse(ServiceInstanceListSupplier supplier,
                                                              List<ServiceInstance> serviceInstances,
                                                              Function<ServiceInstance, Boolean> filter) {
        List<ServiceInstance> list = filter == null
                ? serviceInstances
                : serviceInstances.stream().filter(filter::apply).collect(Collectors.toList());
        Response<ServiceInstance> serviceInstanceResponse = getInstanceResponse(list);
        if (supplier instanceof SelectedInstanceCallback && serviceInstanceResponse.hasServer()) {
            ((SelectedInstanceCallback) supplier).selectedServiceInstance(serviceInstanceResponse.getServer());
        }
        return serviceInstanceResponse;
    }

    private Response<ServiceInstance> getInstanceResponse(List<ServiceInstance> instances) {
        if (instances.isEmpty()) {
            if (log.isWarnEnabled()) {
                log.warn("No servers available for service: " + serviceId);
            }
            return new EmptyResponse();
        }
        int index = ThreadLocalRandom.current().nextInt(instances.size());
        ServiceInstance instance = instances.get(index);
        return new DefaultResponse(instance);
    }
}