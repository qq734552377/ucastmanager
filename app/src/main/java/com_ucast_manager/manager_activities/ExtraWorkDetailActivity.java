package com_ucast_manager.manager_activities;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.alibaba.fastjson.JSON;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com_ucast_manager.R;
import com_ucast_manager.entity.BaseReturnMsg;
import com_ucast_manager.entity.ExtraWorkMSg;
import com_ucast_manager.entity.WorkorderMSg;
import com_ucast_manager.my_adapter.ExtraWorkDetailItemAdapter;
import com_ucast_manager.tools.MyHttpSucessCallback;
import com_ucast_manager.tools.MyTools;
import com_ucast_manager.tools.MyXHttpRequest;

@ContentView(R.layout.activity_extra_work_detail)
public class ExtraWorkDetailActivity extends AppCompatActivity implements MyHttpSucessCallback {
    @ViewInject(R.id.tool_bar)
    Toolbar toolbar;

    @ViewInject(R.id.extral_detail_recycler_view)
    RecyclerView rv;
    @ViewInject(R.id.extral_work_count)
    Button bt_count;
    @ViewInject(R.id.extral_work_refresh)
    FloatingActionButton fbt;


    ExtraWorkDetailItemAdapter adapter;
    ArrayList<ExtraWorkMSg> lists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        initToolbar(getString(R.string.ExtraWorkDetailActivity));
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(manager);

        lists = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            ExtraWorkMSg en = new ExtraWorkMSg();
            en.setExtra_work_reason("外出加班" + (i + 1));
            en.setExtra_work_start_time("2017-5-26 19:26:23");
            en.setExtra_work_end_time("2017-5-26 22:21:23");
            lists.add(en);
        }

        adapter = new ExtraWorkDetailItemAdapter(lists, this);
        rv.setAdapter(adapter);

    }

    @Override
    protected void onResume() {
        bt_count.setText((lists == null ? 0 : lists.size()) + "");
        super.onResume();
    }

    private void initToolbar(String s) {
        toolbar.setTitle(s);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }


    @Event(R.id.extral_work_refresh)
    private void refresh(View v) {

        doquery();
    }

    private void doquery() {
        MyXHttpRequest.postParamsRequestWithToken(this, MyTools.OVERTIME_QUERRY_URL, null, this);
    }

    @Override
    public void sucess(String data) {
        List<ExtraWorkMSg> msg = JSON.parseArray(data, ExtraWorkMSg.class);

        if (!lists.isEmpty()) {
            lists.clear();
        }
        if (msg != null && msg.size() > 0) {
            for (int i = 0; i < msg.size(); i++) {
                lists.add(msg.get(i));
            }
        }
        bt_count.setText((lists == null ? 0 : lists.size()) + "");
        adapter.notifyDataSetChanged();
    }
}
