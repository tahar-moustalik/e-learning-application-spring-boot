package com.springdev.mepdev;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@SpringBootApplication
public class MepDevApplication {

    public static void main(String[] args) {
        SpringApplication.run(MepDevApplication.class, args);
    }

}
