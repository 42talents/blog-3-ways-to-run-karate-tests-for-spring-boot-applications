package karate;

import com._42talents.starter_spring_karate.StarterSpringKarateApplication;
import com.intuit.karate.junit5.Karate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

@SpringBootTest(classes = {StarterSpringKarateApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SpringActuatorFeature {

    @LocalServerPort
    private String localServerPort;

    @Karate.Test
    public Karate springActuator() {
        return Karate
                .run()
                .relativeTo(getClass())
                .systemProperty("karate.port", localServerPort)
                .outputHtmlReport(true)
                .outputJunitXml(true)
                .outputCucumberJson(true);
    }
}
