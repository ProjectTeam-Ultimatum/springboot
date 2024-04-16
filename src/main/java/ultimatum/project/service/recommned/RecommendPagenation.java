package ultimatum.project.service.recommned;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.domain.Page;

@Builder
@Getter
@Setter
@ToString
public class RecommendPagenation {

    public static RecommendPagingButtonInfo getPagingButtonInfo(
            Page<?> listFoodPage,
            Page<?> listPlacePage,
            Page<?> listHotelPage,
            Page<?> listEventPage
    ) {

        int currentPage = listFoodPage.getNumber() + 1;
        int defaultButtonCount = 10;
        int startPage;
        int endPage;

        startPage = (int) (Math.ceil((double) currentPage / defaultButtonCount) - 1)
                * defaultButtonCount + 1;
        endPage = startPage + defaultButtonCount - 1;

        if(listFoodPage.getTotalPages() < endPage)
            endPage = listFoodPage.getTotalPages();

        if(listFoodPage.getTotalPages() == 0 && endPage == 0)
            endPage = startPage;

        return new RecommendPagingButtonInfo(currentPage, startPage, endPage);
    }

}
