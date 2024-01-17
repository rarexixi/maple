package org.xi.maple.service.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import feign.FeignException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.xi.maple.common.model.ResponseError;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FeignResponseError {

    private ResponseError error;

    @JsonIgnore
    private FeignException exception;
}
