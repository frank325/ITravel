package lyf.com.example.itravel.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
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
import lyf.com.example.itravel.ITravelConstant;
import lyf.com.example.itravel.R;
import lyf.com.example.itravel.model.OkhttpModel;
import lyf.com.example.itravel.utlis.ChangeFormatUtils;
import lyf.com.example.itravel.view.CircleTransform;
import okhttp3.Call;
import okhttp3.Response;

/**
 * 修改头像页面
 */

public class UpdateHeadPortraitActivity extends AppCompatActivity {

    private String account;
    private String data;
    private AlertDialog ad;
    private File file;
    private Bitmap bitmap;
    private HashMap<String, String> hashMap = new HashMap<>();

    @BindView(R.id.tb_update_head_portrait) Toolbar tbUpdateHeadPortrait;
    @BindView(R.id.iv_head_portrait) ImageView ivHeadPortrait;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_head_portrait);

        ButterKnife.bind(this);
        initData();
        initToolbar();
        initView();
    }

    /**
     * 接收数据，辨别并进行相关操作
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) { //点击确定
                    cropPhoto(data.getData());// 裁剪图片
                }
                break;
            case 2:
                if (resultCode == RESULT_OK) { //点击确定
                    file = new File(Environment.getExternalStorageDirectory() + "/"
                            + account + ".jpg"); //创建新的文件
                    cropPhoto(Uri.fromFile(file));// 裁剪图片
                }
                break;
            case 3:
                if (data != null) {
                    Bundle extras = data.getExtras(); //使用Bundle获取Intent传来的值
                    bitmap = extras.getParcelable("data");
                    if (bitmap != null) {
                        file = ChangeFormatUtils.compressImage(bitmap, account); //将bitmap转换为文件
                        hashMap.put("account", account);

                        /**
                         * 发送网络请求
                         */
                        OkhttpModel okhttpModel = new OkhttpModel();
                        okhttpModel.doPostFile("updateHeadPortrait.do", file, hashMap,  new Callback() {
                            @Override
                            public Object parseNetworkResponse(Response response, int id) throws Exception {
                                return response.body().string();
                            }

                            @Override
                            public void onError(Call call, Exception e, int id) {
                                Toast.makeText(UpdateHeadPortraitActivity.this, "修改失败", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onResponse(Object response, int id) {
                                Toast.makeText(UpdateHeadPortraitActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
                                if ("修改成功".equals(response.toString())) {
                                    Picasso.with(UpdateHeadPortraitActivity.this)
                                            .load(file)
                                            .memoryPolicy(MemoryPolicy.NO_CACHE)
                                            .resize(150, 150)
                                            .transform(new CircleTransform())
                                            .error(R.drawable.default_head_gray)
                                            .into(ivHeadPortrait);
                                }
                            }
                        });
                    }
                }
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        Intent intent = getIntent();
        data = intent.getStringExtra("data");

        account = ITravelApplication.getiTravelApplication().getUser().getAccount();
    }

    /**
     * 初始化Toolbar
     */
    private void initToolbar() {
        tbUpdateHeadPortrait.setNavigationIcon(R.drawable.back);
        tbUpdateHeadPortrait.setTitle("");

        setSupportActionBar(tbUpdateHeadPortrait); //设置SupportActionBar为Toolbar

        tbUpdateHeadPortrait.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    /**
     * 初始化控件
     */
    private void initView() {
        Picasso.with(this).load(ITravelConstant.URL + data)
                .memoryPolicy(MemoryPolicy.NO_CACHE).resize(150, 150)
                .transform(new CircleTransform()).error(R.drawable.default_head_gray)
                .into(ivHeadPortrait);
    }

    /**
     * 调用系统的裁剪功能
     *
     */
    public void cropPhoto(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, 3);
    }

    /**
     * 监听点击事件，弹出窗口
     */
    @OnClick({R.id.iv_head_portrait, R.id.tv_update_head_portrait}) public void updateHeadPortrait() {
        final String[] s={"从图库中选取","拍摄照片"}; //Dialog普通列表数据

        ad = new AlertDialog.Builder(UpdateHeadPortraitActivity.this)  //创建Dialog普通列表
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

}
