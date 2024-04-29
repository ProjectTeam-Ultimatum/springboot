package ultimatum.project.domain.dto.place;

public class PlaceDTO {
    private Long id;
    private String title;
    private String address;
    private String imageUrl;

    // 기본 생성자, getters, setters 생략

    public PlaceDTO(Long id, String title, String address, String imageUrl) {
        this.id = id;
        this.title = title;
        this.address = address;
        this.imageUrl = imageUrl;
    }

    // getter 및 setter 메소드
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
