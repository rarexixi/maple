package org.apache.spark.status.api.v1;

import org.xi.maple.spark3.MapleApp;
import org.xi.maple.spark3.model.SqlRequest;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 * @author xishihao
 */
@Path("/maple")
public class MapleService {

    @GET
    @Path("stop")
    @Produces(MediaType.APPLICATION_JSON)
    public void stop() {
        System.out.println("Stopping spark session...");
        MapleApp.close();
    }

    /**
     * 执行SQL，并返回结果
     * 需要添加 jersey-media-json-jackson 依赖，否则会报 415
     *
     * @param sqlRequest sql 请求
     * @return sql 执行结果
     */
    @POST
    @Path("exec-sql")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Object sql(SqlRequest sqlRequest) {
        System.out.println("============================ api sql ============================");
        System.out.println(sqlRequest.getSql());
        System.out.println("============================ api sql ============================");
        return MapleApp.execResult(sqlRequest.getJobId(), sqlRequest.getSql());
    }

}
