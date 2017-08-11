package lyf.com.example.itravel.bean;

/**
 * 游记信息
 */

public class TravelNotes {

	private String id_travel_notes;
    private String travel_account;
	private String add_time;
	private String travel_city;
	private String travel_content;
	private String travel_photo_url;
	private String travel_money;
	private String travel_day_num;
	private String clooect_num;
	
	public TravelNotes() {}

	public TravelNotes(String travel_account, String add_time, String travel_city,
			String travel_content, String travel_photo_url, String travel_money, String travel_day_num) {
		super();
		this.travel_account = travel_account;
		this.add_time = add_time;
		this.travel_city = travel_city;
		this.travel_content = travel_content;
		this.travel_photo_url = travel_photo_url;
		this.travel_money = travel_money;
		this.travel_day_num = travel_day_num;
	}
	
	public TravelNotes(String id_travel_notes, String travel_account, String add_time, String travel_city,
			String travel_content, String travel_photo_url, String travel_money, String travel_day_num,
					   String clooect_num) {
		super();
		this.id_travel_notes = id_travel_notes;
		this.travel_account = travel_account;
		this.add_time = add_time;
		this.travel_city = travel_city;
		this.travel_content = travel_content;
		this.travel_photo_url = travel_photo_url;
		this.travel_money = travel_money;
		this.travel_day_num = travel_day_num;
		this.clooect_num = clooect_num;
	}

	public String getId_travel_notes() {
		return id_travel_notes;
	}

	public void setId_travel_notes(String id_travel_notes) {
		this.id_travel_notes = id_travel_notes;
	}

	public String getTravel_account() {
		return travel_account;
	}

	public void setTravel_account(String travel_account) {
		this.travel_account = travel_account;
	}

	public String getAdd_time() {
		return add_time;
	}

	public void setAdd_time(String add_time) {
		this.add_time = add_time;
	}

	public String getTravel_city() {
		return travel_city;
	}

	public void setTravel_city(String travel_city) {
		this.travel_city = travel_city;
	}

	public String getTravel_content() {
		return travel_content;
	}

	public void setTravel_content(String travel_content) {
		this.travel_content = travel_content;
	}

	public String getTravel_photo_url() {
		return travel_photo_url;
	}

	public void setTravel_photo_url(String travel_photo_url) {
		this.travel_photo_url = travel_photo_url;
	}

	public String getTravel_money() {
		return travel_money;
	}

	public void setTravel_money(String travel_money) {
		this.travel_money = travel_money;
	}

	public String getTravel_day_num() {
		return travel_day_num;
	}

	public void setTravel_day_num(String travel_day_num) {
		this.travel_day_num = travel_day_num;
	}

	public String getClooect_num() {
		return clooect_num;
	}

	public void setClooect_num(String clooect_num) {
		this.clooect_num = clooect_num;
	}

	@Override
	public String toString() {
		return "TravelNotes [id_travel_notes=" + id_travel_notes + ", travel_account=" + travel_account + ", add_time="
				+ add_time + ", travel_city=" + travel_city + ", travel_content=" + travel_content
				+ ", travel_photo_url=" + travel_photo_url + ", travel_money=" + travel_money + ", travel_day_num="
				+ travel_day_num + ", clooect_num=" + clooect_num + "]";
	}
	
}
