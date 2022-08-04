package karate;

import com._42talents.spring_boot_karate_example.SpringBootKarateExampleApplication;
import com.intuit.karate.Results;
import com.intuit.karate.junit5.Karate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

@SpringBootTest(classes = {SpringBootKarateExampleApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AllKarateFeatures {

    @LocalServerPort
    private String localServerPort;

    @Test
    public void runAllFeaturesInParallel() {
        Results results = Karate.run(".")
                .systemProperty("karate.port", localServerPort)
                .karateEnv("dev")
                .parallel(3);
        Assertions.assertEquals(0, results.getFailCount(), results.getErrorMessages());
    }
}
