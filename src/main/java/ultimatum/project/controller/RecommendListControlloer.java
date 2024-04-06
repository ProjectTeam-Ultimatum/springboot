package ultimatum.project.controller;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ultimatum.project.dto.food.RecommendListDTO;
import ultimatum.project.service.RecommendListService;
import java.util.List;

@Builder
@Getter
@Setter
@ToString
@Log4j2
//@Controller //빈 스캐닝
@RestController
@RequestMapping("/recommend")
public class RecommendListControlloer {

    //Service 생성자 주입
    private final RecommendListService recommendListService;

    @GetMapping("list")
    public String findRecommendList(Model model){
        List<RecommendListDTO> recommendList = recommendListService.findRecommendList();
        model.addAttribute("recommendList", recommendList);
        log.info("recommendList 는", recommendList);
        return "recommendList";
    }

}
