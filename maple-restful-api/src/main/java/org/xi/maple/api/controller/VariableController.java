package org.xi.maple.api.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.xi.maple.api.model.request.ReplaceVariablesRequest;
import org.xi.maple.common.util.VariableUtils;

@RestController
@RequestMapping("/variable")
public class VariableController {

    @PostMapping("/replace")
    public String replaceVariables(@RequestBody ReplaceVariablesRequest request) {
        return VariableUtils.replaceVariables(request.getContent(), request.getVariables());
    }
}
