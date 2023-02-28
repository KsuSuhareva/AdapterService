package by.suhareva.adapterservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages =  "by.suhareva.adapterservice")
public class AdapterServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AdapterServiceApplication.class, args);

    }

}
