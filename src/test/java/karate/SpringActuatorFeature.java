package karate;

import com._42talents.spring_boot_karate_example.SpringBootKarateExampleApplication;
import com.intuit.karate.junit5.Karate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

@SpringBootTest(classes = {SpringBootKarateExampleApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SpringActuatorFeature {

    @LocalServerPort
    private String localServerPort;

    @Karate.Test
    public Karate actuatorResourceIsAvailable() {
        return karateSzenario("actuator resource is available");
    }

    @Karate.Test
    public Karate healthResourceStatusIsUp() {
        return karateSzenario("health resource status is \"up\"");
    }

    private Karate karateSzenario(String s) {
        return Karate
                .run()
                .scenarioName(s)
                .relativeTo(getClass())
                .systemProperty("karate.port", localServerPort)
                .karateEnv("dev");
    }
}
