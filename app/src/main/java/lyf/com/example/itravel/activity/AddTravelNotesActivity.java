package lyf.com.example.itravel.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import com.zhy.http.okhttp.callback.Callback;

import java.io.File;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import lyf.com.example.itravel.ITravelApplication;
import lyf.com.example.itravel.R;
import lyf.com.example.itravel.model.OkhttpModel;
import lyf.com.example.itravel.utlis.ChangeFormatUtils;
import okhttp3.Call;
import okhttp3.Response;

/**
 * 添加游记页面
 */

public class AddTravelNotesActivity extends AppCompatActivity {

    private String path;
    private File file;
    private String account, title, money, dayNum, content;
    private boolean isImgSuccess = false;
    private HashMap<String, String> hashMap = new HashMap<>();
    private AlertDialog ad;

    @BindView(R.id.tb_add) Toolbar tbAdd;
    @BindView(R.id.et_city) EditText etCity;
    @BindView(R.id.et_money) EditText etMoney;
    @BindView(R.id.et_day_num) EditText etDayNum;
    @BindView(R.id.et_content) EditText etContent;
    @BindView(R.id.iv_travel_photo) ImageView ivPhoto;
    @BindView(R.id.tv_add) TextView tvAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_travel_notes);

        ButterKnife.bind(this); //绑定依赖注入ButterKnif
        initToolbar();
    }

    /**
     * 接收数据，辨别并进行相关操作
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    path = ChangeFormatUtils.getImagePathFromUri(data.getData(), this); //将URI转换为文件地址
                    file = new File(path);
                    isImgSuccess = true;
                    Picasso.with(AddTravelNotesActivity.this)
                            .load(file)
                            .memoryPolicy(MemoryPolicy.NO_CACHE)
                            .resize(200, 200)
                            .error(R.drawable.add_big)
                            .into(ivPhoto);
                }
                break;
            case 2:
                if (resultCode == RESULT_OK) { //点击确定
                    file = new File(Environment.getExternalStorageDirectory() + "/"
                            + account + ".jpg"); //创建新的文件
                    isImgSuccess = true;
                    Picasso.with(AddTravelNotesActivity.this)
                            .load(file)
                            .memoryPolicy(MemoryPolicy.NO_CACHE)
                            .resize(200, 200)
                            .error(R.drawable.add_big)
                            .into(ivPhoto);
                }
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 初始化Toolbar
     */
    private void initToolbar() {
        tbAdd.setNavigationIcon(R.drawable.back);
        tbAdd.setTitle("");

        setSupportActionBar(tbAdd); //设置SupportActionBar为Toolbar

        etCity.requestFocus();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE); //自动弹出软键盘

        tbAdd.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        }); //监听点击事件，返回
    }

    /**
     * 监听点击事件，弹出窗口
     */
    @OnClick(R.id.iv_travel_photo) public void addPhoto() {
        final String[] s={"从图库中选取","拍摄照片"}; //Dialog普通列表数据

        ad = new AlertDialog.Builder(AddTravelNotesActivity.this)  //创建Dialog普通列表
                .setItems(s, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent();
                        if ("从图库中选取".equals(s[which])) { //点击从图库中选取
                            intent = new Intent(Intent.ACTION_PICK, null); //调用手机相册
                            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*"); //调用手机相册
                            startActivityForResult(intent, 1);
                        }else if ("拍摄照片".equals(s[which])) { //点击拍摄照片
                            intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); //调用手机拍摄功能
                            intent.putExtra(MediaStore.EXTRA_OUTPUT,
                                    Uri.fromFile(new File(Environment.getExternalStorageDirectory()
                                            , account + ".jpg"))); //调用手机拍摄功能
                            startActivityForResult(intent, 2);
                        }
                        dialog.dismiss(); //关闭Dialog
                    }

                }).create();  //创建
        ad.show(); //关闭显示
    }

    /**
     * 响应点击事件，执行添加游记
     */
    @OnClick(R.id.tv_add) public void add() {
        account = ITravelApplication.getiTravelApplication().getUser().getAccount();
        title = etCity.getText().toString().trim();
        money = etMoney.getText().toString().trim();
        dayNum = etDayNum.getText().toString().trim();
        content = etContent.getText().toString().trim();

        if (!isImgSuccess) {
            Toast.makeText(this, "请至少添加一张图片", Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(title) || TextUtils.isEmpty(money) || TextUtils.isEmpty(dayNum) || TextUtils.isEmpty(content)) {
            Toast.makeText(this, "请填写完整", Toast.LENGTH_SHORT).show();
            return;
        }

        if(file == null) {
            Toast.makeText(this, "请至少添加一张图片", Toast.LENGTH_SHORT).show();
            return;
        }

        tvAdd.setEnabled(false);
        hashMap.put("account", account);
        hashMap.put("travel_title", title);
        hashMap.put("travel_content", content);
        hashMap.put("travel_money", money);
        hashMap.put("travel_day_num", dayNum);

        /**
         * 发送网络请求
         */
        OkhttpModel okhttpModel = new OkhttpModel();
        okhttpModel.doPostFile("addTravelNotesInfo.ao", file, hashMap, new Callback() {

            @Override
            public Object parseNetworkResponse(Response response, int id) throws Exception {
                return response.body().string();
            }

            @Override
            public void onError(Call call, Exception e, int id) {
                Toast.makeText(AddTravelNotesActivity.this, "添加失败", Toast.LENGTH_SHORT).show();
                tvAdd.setEnabled(true);
            }

            @Override
            public void onResponse(Object response, int id) {
                Toast.makeText(AddTravelNotesActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
                if ("添加成功".equals(response.toString())) {
                    finish();
                }else {
                    tvAdd.setEnabled(true);
                }
            }
        });
    }

 }
