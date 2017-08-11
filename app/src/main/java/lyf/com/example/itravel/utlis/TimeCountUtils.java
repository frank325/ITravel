package lyf.com.example.itravel.utlis;

import android.graphics.Color;
import android.os.CountDownTimer;
import android.widget.Button;

/**
 * 时间倒计时类
 */

public class TimeCountUtils extends CountDownTimer {

    private Button btn;

    public TimeCountUtils(long millisInFuture, long countDownInterval, Button btn) {
        super(millisInFuture, countDownInterval);

        this.btn = btn;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        btn.setEnabled(false); //设置不能点击
        btn.setText(millisUntilFinished / 1000 + "秒后可重新获取"); //设置倒计时时间
        btn.setTextColor(Color.GRAY);
    }

    @Override
    public void onFinish() {
        btn.setEnabled(true); //重新获得点击
        btn.setText("获取验证码");
        btn.setTextColor(Color.parseColor("#339966"));
    }
}
