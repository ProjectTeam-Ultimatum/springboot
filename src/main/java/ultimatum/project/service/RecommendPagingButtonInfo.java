package ultimatum.project.service;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Builder
@Getter
@Setter
@ToString
public class RecommendPagingButtonInfo {

    private int currentPage;
    private int startPage;
    private int endPage;

    public RecommendPagingButtonInfo() {}

    public RecommendPagingButtonInfo(int currentPage, int startPage, int endPage) {
        super();
        this.currentPage = currentPage;
        this.startPage = startPage;
        this.endPage = endPage;
    }
}
