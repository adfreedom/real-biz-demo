package com.demo.business;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * demo 启动类。扫描业务与基础组件两棵包树下的全部组件，扫描交易域 Mapper。
 */
@SpringBootApplication(scanBasePackages = {"com.demo.business", "com.demo.foundation"})
@MapperScan({"com.demo.business.infrastructure.trade.mapper"})
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}
