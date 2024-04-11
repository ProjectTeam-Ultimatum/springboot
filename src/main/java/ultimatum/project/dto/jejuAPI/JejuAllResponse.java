package ultimatum.project.dto.jejuAPI;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigInteger;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class JejuAllResponse {

    private String result;
    private String resultMessage;
    private Integer totalCount;
    private Integer resultCount;
    private BigInteger pageSize;
    private Integer pageCount;
    private Integer currentPage;
    private List<Item> items;

    // Getters and setters...

    //Lombok은 내부 클래스에 대해서는 상위 클래스의 애너테이션을 상속받지 않기 때문
    //각자 선언해줘야함
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Item {
        private String alltag;
        private String contentsid;
        private Contentscd contentscd;
        private String title;
        private Region1cd region1cd;
        private Region2cd region2cd;
        private String address;
        private String roadaddress;
        private String tag;
        private String introduction;
        private Double latitude;
        private Double longitude;
        private String postcode;
        private String phoneno;
        private RepPhoto repPhoto;

        // Getters and setters...

        @Getter
        @Setter
        @AllArgsConstructor
        @NoArgsConstructor
        public static class Contentscd {
            private String value;
            private String label; //음식점, 관광지, 테마여행
            private String refId;

            // Getters and setters...
        }

        @Getter
        @Setter
        @AllArgsConstructor
        @NoArgsConstructor
        public static class Region1cd {
            private String value;
            private String label;
            private String refId;

            // Getters and setters...
        }

        @Getter
        @Setter
        @AllArgsConstructor
        @NoArgsConstructor
        public static class Region2cd {
            private String value;
            private String label;
            private String refId;

            // Getters and setters...
        }

        @Getter
        @Setter
        @AllArgsConstructor
        @NoArgsConstructor
        public static class RepPhoto {
            private String descseo;

            @JsonProperty("photoid")
            private PhotoId photoId;

            // Getters and setters...

            @Getter
            @Setter
            @AllArgsConstructor
            @NoArgsConstructor
            public static class PhotoId {
                private String imgpath;
                private String thumbnailpath;

                // Getters and setters...
            }
        }
    }
}
