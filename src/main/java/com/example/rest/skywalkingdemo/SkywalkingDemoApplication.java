package com.example.rest.skywalkingdemo;

import com.example.rest.entities.Party;
import com.example.rest.entities.PartyMember;
import com.example.rest.repos.PartyMemberRepo;
import com.example.rest.repos.PartyRepo;
import org.apache.skywalking.apm.meter.micrometer.SkywalkingConfig;
import org.apache.skywalking.apm.meter.micrometer.SkywalkingMeterRegistry;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.Arrays;
@SpringBootApplication
@ComponentScan(basePackages = {"com.example"})
@EntityScan("come.example.rest.entities")
@EnableJpaRepositories(basePackages = "com.example.rest.repos")
public class SkywalkingDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(SkywalkingDemoApplication.class, args);
    }
    @Bean
    SkywalkingMeterRegistry skywalkingMeterRegistry() {
        // Add rate configs If you need, otherwise using none args construct
        SkywalkingConfig config = new SkywalkingConfig(Arrays.asList(""));
        return new SkywalkingMeterRegistry(config);
    }

    @Bean
    CommandLineRunner commandLineRunner(PartyMemberRepo partyMemberRepo, PartyRepo partyRepo){
        return args -> {
            Party p1 = new Party(
                    "CoolCats"
            );

            partyRepo.save(p1);

            PartyMember pm1 = new PartyMember(
                    1L,
                    "Loki",
                    "Rogue",
                    45,
                    10,
                    p1
            );

            partyMemberRepo.save(pm1);
        };
    }

}

