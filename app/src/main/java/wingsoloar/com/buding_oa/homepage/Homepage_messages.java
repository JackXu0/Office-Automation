package wingsoloar.com.buding_oa.homepage;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import wingsoloar.com.buding_oa.R;
import wingsoloar.com.buding_oa.chat.Sending_interface;

/**
 * Created by wingsolarxu on 2017/8/25.
 */

public class Homepage_messages extends Activity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private TextView title;
    private RelativeLayout top_message;
    private ArrayList<String> sender_names;
    private ArrayList<String> send_dates;
    private ArrayList<String> contents;
    private ArrayList<Boolean> isRead;
    private ArrayList<String> filenames;
    private ArrayList<String> filepaths;
    private ImageView return_button;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage_messages_main);
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        sender_names=new ArrayList<>();
        send_dates=new ArrayList<>();
        contents=new ArrayList<>();
        isRead=new ArrayList<>();
        filenames=new ArrayList<>();
        filepaths=new ArrayList<>();

        sender_names.add("马化腾@801013");
        sender_names.add("leijun@801013");
        sender_names.add("James Robort@801013");

        send_dates.add("June 4, 2011");
        send_dates.add("May 13, 2013");
        send_dates.add("August 8, 2016");

        contents.add("Dear all,\n" +
                "Please click the links below to view the notices:\n" +
                " \n" +
                "Notice for Power Supply Cut-off on the Morning of 28th August in South Campus\n" +
                " \n" +
                " \n" +
                " \n" +
                "Note: If OS of mobile device is Android, please use browser “Opera” to open file.");

        contents.add("Dear all,\n" +
                "Please click the links below to view the notices:\n" +
                " \n" +
                "Notice for Power Supply Cut-off on the Morning of 28th August in South Campus\n" +
                " \n" +
                " \n" +
                " \n" +
                "Note: If OS of mobile device is Android, please use browser “Opera” to open file.");
        contents.add("Dear all,\n" +
                "Please click the links below to view the notices:\n" +
                " \n" +
                "Notice for Power Supply Cut-off on the Morning of 28th August in South Campus\n" +
                " \n" +
                " \n" +
                " \n" +
                "Note: If OS of mobile device is Android, please use browser “Opera” to open file.");

        filenames.add("项目支出明细");
        filenames.add("项目用工明细");
        filenames.add("项目流程");

        isRead.add(true);
        isRead.add(false);
        isRead.add(true);

        mAdapter = new MyAdapter(sender_names,send_dates,contents,filenames,null,isRead);
        initView();
        initData();

    }

    private void initData() {

        title.setText("所有消息");
        return_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void initView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.homepage_messages_main_listview);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        title=findViewById(R.id.top_message_title);
        top_message=findViewById(R.id.top_message);
        return_button=findViewById(R.id.top_message_return_button);
        return_button.setVisibility(View.VISIBLE);
    }


    class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

        private ArrayList<String> sender_names;
        private ArrayList<String> send_dates;
        private ArrayList<String> contents;
        private ArrayList<Boolean> isRead;
        private ArrayList<String> filenames;
        private ArrayList<String> filepaths;

        public MyAdapter(@Nullable ArrayList<String> sender_names,
                         @Nullable ArrayList<String> send_dates,
                         @Nullable ArrayList<String> contents,
                         @Nullable ArrayList<String> filenames,
                         @Nullable ArrayList<String> filepaths,
                         ArrayList<Boolean> isRead) {
            this.sender_names=sender_names;
            this.send_dates=send_dates;
            this.contents=contents;
            this.isRead=isRead;
            this.filenames=filenames;
            this.filepaths=filepaths;
        }

        public void updateData(ArrayList<Boolean> isRead) {

            this.isRead=isRead;
            notifyDataSetChanged();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            // 实例化展示的view
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.homepage_messages_item, parent, false);
            // 实例化viewholder
            ViewHolder viewHolder = new ViewHolder(v);

            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            // 绑定数据
            holder.sender_name.setText(sender_names.get(position).split("@")[0]);
            holder.send_time.setText(send_dates.get(position));
            holder.content.setText(contents.get(position));
            if(isRead.get(position))
                holder.isRead.setVisibility(View.GONE);
            holder.reply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(Homepage_messages.this, Sending_interface.class);
                    ArrayList<String> receivers=new ArrayList<String>();
                    receivers.add(sender_names.get(position));
                    intent.putStringArrayListExtra("receivers",receivers);
                    intent.putExtra("isReply",true);
                    startActivity(intent);
                }
            });
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(Homepage_messages.this,Homepage_messages_subpage.class);
                    intent.putExtra("sender_name",sender_names.get(position));
                    intent.putExtra("send_date",send_dates.get(position));
                    intent.putExtra("content",contents.get(position));
                    intent.putStringArrayListExtra("filenames",filenames);
                    intent.putStringArrayListExtra("filepaths",filepaths);
                    isRead.set(position,true);
                    updateData(isRead);
                    startActivity(intent);
                }
            });
        }


        @Override
        public int getItemCount() {
            return isRead == null ? 0 : isRead.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            TextView content;
            TextView isRead;
            TextView sender_name;
            TextView send_time;
            ImageView reply;
            View itemView;

            public ViewHolder(View itemView) {
                super(itemView);
                this.itemView=itemView;
                content = (TextView) itemView.findViewById(R.id.message);
                isRead=itemView.findViewById(R.id.is_read);
                sender_name=itemView.findViewById(R.id.sender_name);
                send_time=itemView.findViewById(R.id.send_time);
                reply=itemView.findViewById(R.id.reply);
            }
        }
    }
}


