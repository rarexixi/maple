package org.xi.maple.manager.yarn.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.xi.maple.manager.model.ClusterQueue;

import java.io.Serializable;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class YarnClusterQueue implements ClusterQueue, Serializable {

    // todo 这里还可以关联队列，暂时可以限制一个队列只能一个用户提交
    private Integer numPendingApplications = 0;

    @Override
    public boolean idle() {
        return numPendingApplications == null || numPendingApplications == 0;
    }
}
