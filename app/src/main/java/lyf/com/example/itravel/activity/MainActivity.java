package lyf.com.example.itravel.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import lyf.com.example.itravel.R;
import lyf.com.example.itravel.fragment.HomePageFragment;
import lyf.com.example.itravel.fragment.PersonFragment;
import lyf.com.example.itravel.fragment.ShareFragment;
import lyf.com.example.itravel.view.AddPopWindow;

/**
 * 主页面
 */

public class MainActivity extends AppCompatActivity {

    private final int HOME_PAGE = 0;
    private final int SHARE = 1;
    private final int PERSON = 2;

    private int selectPage = HOME_PAGE;
    private Fragment fgHomePage, fgShare, fgPerson;

    @BindView(R.id.tb_main) Toolbar tbMain;
    @BindView(R.id.tv_title) TextView tvTitle;
    @BindView(R.id.tv_parent) TextView tvParent;
    @BindView(R.id.ll_main) LinearLayout llMain;
    @BindView(R.id.ib_home_page) ImageButton ibHomePage;
    @BindView(R.id.ib_share) ImageButton ibShare;
    @BindView(R.id.ib_person) ImageButton ibPerson;
    @BindView(R.id.tv_home_page) TextView tvHomePage;
    @BindView(R.id.tv_share) TextView tvShare;
    @BindView(R.id.tv_person) TextView tvPerson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this); //绑定依赖注入ButterKnif
        tbMain.setTitle("");
        setSelect(HOME_PAGE);
        setSupportActionBar(tbMain); //设置SupportActionBar为Toolbar
    }

    /**
     * 显示菜单
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        switch (selectPage) {
            case HOME_PAGE:
                getMenuInflater().inflate(R.menu.menu_home_page, menu);
                return true;
            case SHARE:
                getMenuInflater().inflate(R.menu.menu_share, menu);
                return true;
            case PERSON:
                getMenuInflater().inflate(R.menu.menu_person, menu);
                return  true;
            default:
                return false;
        }
    }

    /**
     * 响应菜单点击事件
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent();
        switch (item.getItemId()) {
            case R.id.action_add: //添加菜单
                AddPopWindow addPopWindow = new AddPopWindow(this);
                addPopWindow.showPopupWindow(tvParent); //显示PopupWindow
                break;
            case R.id.action_search: //搜索菜单
                intent.setClass(MainActivity.this, SearchActivity.class);
                startActivity(intent);
                break;
            case R.id.action_setting: //设置菜单
                intent.setClass(MainActivity.this, SettingActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 设置要显示的Fragment
     */
    private void setSelect(int tabe)
    {
        selectPage = tabe;
        invalidateOptionsMenu(); //刷新菜单

        resetImgs();

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        hideFragment(transaction); //隐藏fragment

        // 设置内容区域
        switch (tabe)
        {
            case HOME_PAGE:
                if (fgHomePage == null) //没有被创建过
                {
                    fgHomePage = new HomePageFragment();
                    transaction.add(R.id.fl_main, fgHomePage); //设置页面
                } else
                {
                    transaction.show(fgHomePage); //直接显示页面
                }
                tvTitle.setText("首页");
                ibHomePage.setImageResource(R.drawable.home_page_light);
                tvHomePage.setTextColor(Color.parseColor("#86D2C6"));
                break;
            case SHARE:
                if (fgShare == null) //没有被创建过
                {
                    fgShare = new ShareFragment();
                    transaction.add(R.id.fl_main, fgShare); //设置页面
                } else
                {
                    transaction.show(fgShare); //直接显示页面
                }
                tvTitle.setText("分享");
                ibShare.setImageResource(R.drawable.share_light);
                tvShare.setTextColor(Color.parseColor("#86D2C6"));
                break;
            case PERSON:
                if (fgPerson == null) //没有被创建过
                {
                    fgPerson = new PersonFragment();
                    transaction.add(R.id.fl_main, fgPerson); //设置页面
                } else
                {
                    transaction.show(fgPerson); //直接显示页面
                }
                tvTitle.setText("");
                ibPerson.setImageResource(R.drawable.person_light);
                tvPerson.setTextColor(Color.parseColor("#86D2C6"));
                break;
            default:
                break;
        }
        transaction.commit(); //提交
    }

    /**
     * 将图标和文字设置为灰色
     */
    private void resetImgs() {
        ibHomePage.setImageResource(R.drawable.home_page_nomal);
        ibShare.setImageResource(R.drawable.share_nomal);
        ibPerson.setImageResource(R.drawable.person_nomal);

        tvHomePage.setTextColor(Color.parseColor("#999999"));
        tvShare.setTextColor(Color.parseColor("#999999"));
        tvPerson.setTextColor(Color.parseColor("#999999"));
    }

    /**
     * 隐藏fragment
     */
    private void hideFragment(FragmentTransaction transaction)
    {
        if (fgHomePage != null)
        {
            transaction.hide(fgHomePage);
        }
        if (fgShare != null)
        {
            transaction.hide(fgShare);
        }
        if (fgPerson != null)
        {
            transaction.hide(fgPerson);
        }
    }

    /**
     * 响应点击事件，显示首页Fragment
     */
    @OnClick(R.id.ll_home_page) public void homePage() {
        setSelect(HOME_PAGE);
    }

    /**
     * 响应点击事件，显示分享Fragment
     */
    @OnClick(R.id.ll_share) public void share() {
        setSelect(SHARE);
    }

    /**
     * 响应点击事件，显示我Fragment
     */
    @OnClick(R.id.ll_person) public void person() {
        setSelect(PERSON);
    }

}
