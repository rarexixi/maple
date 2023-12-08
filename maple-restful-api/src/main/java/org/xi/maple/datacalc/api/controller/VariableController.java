package org.xi.maple.datacalc.api.controller;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.shared.Application;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.xi.maple.datacalc.api.model.request.ReplaceVariablesRequest;
import org.xi.maple.common.util.VariableUtils;

import java.util.List;

@RestController
@RequestMapping("/variable")
public class VariableController {

    @Autowired
    EurekaClient eurekaClient;

    @PostMapping("/replace")
    public String replaceVariables(@RequestBody ReplaceVariablesRequest request) {
        return VariableUtils.replaceVariables(request.getContent(), request.getVariables());
    }

    @GetMapping("/applications")
    public Application applications(@RequestParam("name") String name) {
        return eurekaClient.getApplication(name);
    }

    @GetMapping("/instances")
    public List<InstanceInfo> instances(@RequestParam("id") String id) {
        return eurekaClient.getInstancesById(id);
    }

    @GetMapping("/instances-by-vip-address")
    public List<InstanceInfo> instances(@RequestParam("vipAddress") String vipAddress,
                                        @RequestParam(value = "secure", defaultValue = "false") boolean secure,
                                        @RequestParam(value = "region", defaultValue = "") String region) {
        return eurekaClient.getInstancesByVipAddress(vipAddress, secure);
    }
}
