package ir.msisoft;

import ir.msisoft.Repositories.CityRepository;
import ir.msisoft.Repositories.TicketRepository;
import ir.msisoft.Repositories.UserRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
        CityRepository.init();
        UserRepository.init();
        TicketRepository.init();
    }
}
