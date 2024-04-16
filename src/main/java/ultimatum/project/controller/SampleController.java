// Controller 패키지
package ultimatum.project.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ultimatum.project.service.SampleService;

@Tag(name = "sample-controller", description = "테스트API")
@RestController
@RequestMapping("/api")
public class SampleController {

    private final SampleService sampleService;

    public SampleController(SampleService sampleService) {
        this.sampleService = sampleService;
    }

    @GetMapping("/sample")
    public String getSampleData() {
        return sampleService.retrieveData();
    }

    @GetMapping("/test")
    public String getTest() {return  sampleService.testError();}
}
