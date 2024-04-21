package com.yamo.cdsysmng;

import com.yamo.cdcommonrabbitmq.enable.EnableDefaultRabbitmq;
import com.yamo.cdcommonsupport.annotation.enable.EnableAuth;
import com.yamo.cdcommonsupport.annotation.enable.EnableControllerResponse;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

import java.net.InetAddress;

@EnableControllerResponse
@EnableAuth
@EnableDefaultRabbitmq
@Slf4j
@SpringBootApplication
public class CdSysMngApplication {

    @SneakyThrows
    public static void main(String[] args) {
        SpringApplication app=new SpringApplication(CdSysMngApplication.class);
        ConfigurableApplicationContext application=app.run(args);
        Environment env = application.getEnvironment();
        log.info("\n----------------------------------------------------------\n\t" +
                        "Application '{}' is running! Access URLs:\n\t" +
                        "Local: \t\thttp://localhost:{}\n\t" +
                        "External: \thttp://{}:{}\n\t"+
                        "Doc: \thttp://{}:{}/doc.html\n"+
                        "----------------------------------------------------------",
                env.getProperty("spring.application.name"),
                env.getProperty("server.port"),
                InetAddress.getLocalHost().getHostAddress(),
                env.getProperty("server.port"),
                InetAddress.getLocalHost().getHostAddress(),
                env.getProperty("server.port"));
    }

}
