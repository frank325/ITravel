package lyf.com.example.itravel.bean;

/**
 * 城市信息
 */

public class City{

	private String id_city;
    private String city_name;
	private String city_address;
	private String think_go_num;
	private String city_photo_url;
	
	public City() {}

	public City(String id_city, String city_name, String city_address, String think_go_num, String city_photo_url) {
		super();
		this.id_city = id_city;
		this.city_name = city_name;
		this.city_address = city_address;
		this.think_go_num = think_go_num;
		this.city_photo_url = city_photo_url;
	}

	public String getId_city() {
		return id_city;
	}

	public void setId_city(String id_city) {
		this.id_city = id_city;
	}

	public String getCity_name() {
		return city_name;
	}

	public void setCity_name(String city_name) {
		this.city_name = city_name;
	}

	public String getCity_address() {
		return city_address;
	}

	public void setCity_address(String city_address) {
		this.city_address = city_address;
	}

	public String getThink_go_num() {
		return think_go_num;
	}

	public void setThink_go_num(String think_go_num) {
		this.think_go_num = think_go_num;
	}

	public String getCity_photo_url() {
		return city_photo_url;
	}

	public void setCity_photo_url(String city_photo_url) {
		this.city_photo_url = city_photo_url;
	}
	@Override
	public String toString() {
		return "City [id_city=" + id_city + ", city_name=" + city_name + ", city_address=" + city_address
				+ ", think_go_num=" + think_go_num + ", city_photo_url=" + city_photo_url + "]";
	}
}
