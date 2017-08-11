package lyf.com.example.itravel.bean;

/**
 * 景点信息
 */

public class Scenic {

    private String id_scenic;
    private String scenic_city_name;
    private String scenic_name;
    private String scenic_introduce;
    private String scenic_photo_url;

    public Scenic() {}

    public Scenic(String id_scenic, String scenic_city_name, String scenic_name, String scenic_introduce,
                  String scenic_photo_url) {
        super();
        this.id_scenic = id_scenic;
        this.scenic_city_name = scenic_city_name;
        this.scenic_name = scenic_name;
        this.scenic_introduce = scenic_introduce;
        this.scenic_photo_url = scenic_photo_url;
    }

    public String getId_scenic() {
        return id_scenic;
    }

    public void setId_scenic(String id_scenic) {
        this.id_scenic = id_scenic;
    }

    public String getScenic_city_name() {
        return scenic_city_name;
    }

    public void setScenic_city_name(String scenic_city_name) {
        this.scenic_city_name = scenic_city_name;
    }

    public String getScenic_name() {
        return scenic_name;
    }

    public void setScenic_name(String scenic_name) {
        this.scenic_name = scenic_name;
    }

    public String getScenic_introduce() {
        return scenic_introduce;
    }

    public void setScenic_introduce(String scenic_introduce) {
        this.scenic_introduce = scenic_introduce;
    }

    public String getScenic_photo_url() {
        return scenic_photo_url;
    }

    public void setScenic_photo_url(String scenic_photo_url) {
        this.scenic_photo_url = scenic_photo_url;
    }

    @Override
    public String toString() {
        return "Scenic [id_scenic=" + id_scenic + ", scenic_city_name=" + scenic_city_name + ", scenic_name="
                + scenic_name + ", scenic_introduce=" + scenic_introduce + ", scenic_photo_url=" + scenic_photo_url + "]";
    }


}
