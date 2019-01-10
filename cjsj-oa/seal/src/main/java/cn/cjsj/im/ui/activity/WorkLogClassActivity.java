package cn.cjsj.im.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.RelativeLayout;

import com.jakewharton.rxbinding.view.RxView;

import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.cjsj.im.R;
import rx.functions.Action1;

/**
 * Created by LuoYang on 2019/1/8 14:54
 * 工作日志
 */
public class WorkLogClassActivity extends BaseActivity {

    @Bind(R.id.log_dailypaper_layout)
    RelativeLayout mDailyPaper;

    @Bind(R.id.log_week_layout)
    RelativeLayout mWeekLog;

    @Bind(R.id.log_month_layout)
    RelativeLayout monthLog;

    @Bind(R.id.log_market_layout)
    RelativeLayout marketLog;

    @Bind(R.id.log_visit_layout)
    RelativeLayout mVisitLog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_class);
        ButterKnife.bind(this);
        setTitle("工作日志");

        RxView.clicks(mDailyPaper)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Intent intent = new Intent(WorkLogClassActivity.this, DailyPaperActivity.class);
                        intent.putExtra("actDefId", "6");
                        startActivity(intent);
                    }
                });

        RxView.clicks(mWeekLog)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Intent intent = new Intent(WorkLogClassActivity.this, WeeklyLogActivity.class);
                        intent.putExtra("actDefId", "2");
                        startActivity(intent);
                    }
                });

        RxView.clicks(monthLog)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Intent intent = new Intent(WorkLogClassActivity.this, MonthlyActivity.class);
                        intent.putExtra("actDefId", "3");
                        startActivity(intent);
                    }
                });

        RxView.clicks(marketLog)
                .throttleFirst(1,TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Intent intent = new Intent(WorkLogClassActivity.this, MarketInfoListActivity.class);
                        intent.putExtra("actDefId", "scxx:1:10000002380008");
                        startActivity(intent);
                    }
                });

        RxView.clicks(mVisitLog)
                .throttleFirst(1,TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Intent intent = new Intent(WorkLogClassActivity.this, CallOnRecordActivity.class);
                        intent.putExtra("actDefId", "4");
                        startActivity(intent);
                    }
                });
    }
}
