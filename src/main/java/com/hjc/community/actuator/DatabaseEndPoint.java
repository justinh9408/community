package com.hjc.community.actuator;

import com.hjc.community.util.CommunityUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * @Classname DatabaseEndPoint
 * @Description TODO
 * @Date 2020-04-01 4:41 p.m.
 * @Created by Justin
 */
@Component
@Endpoint(id = "database")
public class DatabaseEndPoint {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseEndPoint.class);

    @Autowired
    DataSource dataSource;

    @ReadOperation
    public String checkConnection() {
        try (
                Connection conn = dataSource.getConnection();
        ) {
            return CommunityUtil.getJsonObject(0, "获取连接成功!");
        } catch (SQLException e) {
            logger.error("获取连接失败:" + e.getMessage());
            return CommunityUtil.getJsonObject(1, "获取连接失败!");
        }
    }

}
