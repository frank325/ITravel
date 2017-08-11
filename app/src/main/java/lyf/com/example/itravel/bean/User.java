package lyf.com.example.itravel.bean;

/**
 * 用户信息
 */

public class User{

    private String id_user;
    private String account;
    private String password;
    private String name;
    private String gender;
    private String signature;
    private String head_portrait_url;
    private String think_go_city;
    private String clooect_travel_notes;
    private String share_num;

    public User() {}

    public User(String account, String password) {
        super();
        this.account = account ;
        this.password = password ;
    }

    public User(String id_user, String account, String password, String name, String gender, String signature,
                String head_portrait_url, String think_go_city, String clooect_travel_notes, String share_num) {
        super();
        this.id_user = id_user;
        this.account = account;
        this.password = password;
        this.name = name;
        this.gender = gender;
        this.signature = signature;
        this.head_portrait_url = head_portrait_url;
        this.think_go_city = think_go_city;
        this.clooect_travel_notes = clooect_travel_notes;
        this.share_num = share_num;
    }

    public String getId_user() {
        return id_user;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getHead_portrait_url() {
        return head_portrait_url;
    }

    public void setHead_portrait_url(String head_portrait_url) {
        this.head_portrait_url = head_portrait_url;
    }

    public String getThink_go_city() {
        return think_go_city;
    }

    public void setThink_go_city(String think_go_city) {
        this.think_go_city = think_go_city;
    }

    public String getClooect_travel_notes() {
        return clooect_travel_notes;
    }

    public void setClooect_travel_notes(String clooect_travel_notes) {
        this.clooect_travel_notes = clooect_travel_notes;
    }

    public String getShare_num() {
        return share_num;
    }

    public void setShare_num(String share_num) {
        this.share_num = share_num;
    }

    @Override
    public String toString() {
        return "User [id_user=" + id_user + ", account=" + account + ", password=" + password + ", name=" + name
                + ", gender=" + gender + ", signature=" + signature + ", head_portrait_url=" + head_portrait_url
                + ", think_go_city=" + think_go_city + ", clooect_travel_notes=" + clooect_travel_notes + ", share_num="
                + share_num + "]";
    }
}
