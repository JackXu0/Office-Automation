package wingsoloar.com.buding_oa.Contact;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.DataSetObserver;
import android.media.Image;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.rahatarmanahmed.cpv.CircularProgressView;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import cn.jpush.im.android.api.JMessageClient;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import wingsoloar.com.buding_oa.R;
import wingsoloar.com.buding_oa.chat.Sending_interface;
import wingsoloar.com.buding_oa.chat.chat_main;
import wingsoloar.com.buding_oa.information.User_info;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by wingsolarxu on 2017/7/25.
 */

public class ContactList_main_fragment extends Fragment {

    private View rootView;
    private ExpandableListView expandablelistview;
    private ExpandableListAdapter expandableListAdapter;

    private TextView extra;

    private Request requestUser;
    private Request requestGroup;
    private String id;
    private String ip;
    private ArrayList<Integer> tmp;
    private CircularProgressView progressView;
    private TextView loading_tx;
    private Button choose;
    private int number=0;

    private ArrayList<String> group;
    private ArrayList<ArrayList<String>> user;
    private ArrayList<ArrayList<Integer>> check_states;
    private ArrayList<String> receive_users;

    public ContactList_main_fragment(){}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        SharedPreferences preferences=getContext().getSharedPreferences("code_scan_id",MODE_PRIVATE);;
        id= preferences.getString("id", "");
        SharedPreferences preferences2=getContext().getSharedPreferences("login",MODE_PRIVATE);;
        ip = preferences2.getString("server_ip", "");


        group=new ArrayList<String>();
        user=new ArrayList<ArrayList<String>>();
        check_states= new ArrayList<ArrayList<Integer>>();
        group.add("官兵");
        ArrayList<String> temp=new ArrayList<String>();
        temp.add("1");
        user.add(temp);
        ArrayList<Integer> temp2=new ArrayList<Integer>();
        temp2.add(0);
        check_states.add(temp2);


        expandableListAdapter=new ExpandableListAdapter(group,user);
        Log.e("rea","contact1");


        buildRequests();
    }

    private void buildRequests(){
        FormBody requestBodyBuilderGroup=new FormBody.Builder()
                .add("ticker",id.replace("\"",""))
                .add("JsonCondition","")
                .add("page","0")
                .add("rows","0")
                .build();

        FormBody requestBodyUser=new FormBody.Builder()
                .add("ticker",id.replace("\"",""))
                .add("Sqlname","getOaUser")
                .add("JsonCondition","")
                .add("StartPage","0")
                .add("PageRows","0")
                .add("TotalRows","0")
                .build();
        Request.Builder builder=new Request.Builder();
        requestGroup=builder.url("http://"+ip+"/Sys/ND_ORGFacade.asmx/getDataByCondition_Java").post(requestBodyBuilderGroup).build();
        requestUser=builder.url("http://"+ip+"/Sys/ND_USERFacade.asmx/getDataBySql_Java").post(requestBodyUser).build();
    }

    private void executeRequestGroup(Request request){
        OkHttpClient okHttpClient=new OkHttpClient();
        Call call=okHttpClient.newCall(request);
        Log.e("1","contact1");
        group=new ArrayList<>();
        user=new ArrayList<ArrayList<String>>();
        check_states=new ArrayList<ArrayList<Integer>>();

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("2","contact1");
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(),
                                "服务器异常1", Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (response.isSuccessful()) {

                    String jsonData = response.body().string();


                    try {
                        JSONObject jsonObject = new JSONObject(jsonData);
                        JSONArray orgArray = jsonObject.getJSONArray("ND_ORGINAZITION");
                        for (int i =0 ; i < orgArray.length(); i++) {
                            JSONObject org_Array = orgArray.getJSONObject(i);
                            group.add(org_Array.getString("ORGI_NAME"));
                        }
                        Log.e("3","contact1");

                    } catch (JSONException e) {

                    }finally {
                        Log.e("6","contact1");
                        executeRequestUser(requestUser);
                    }


                }else{

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(),
                                    "服务器异常2", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }

        });
    }

    private void executeRequestUser(Request request){
        final OkHttpClient okHttpClient=new OkHttpClient();
        Call call=okHttpClient.newCall(request);
        Log.e("7","contact1");

        //execute contact_list_child
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("4","contact1");
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(),
                                "服务器异常3", Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (response.isSuccessful()) {

                    String jsonData = response.body().string();
                    HanyuPinyinOutputFormat format= new HanyuPinyinOutputFormat();
                    Log.e("5","contact1");

                    try {
                        JSONObject jsonObject = new JSONObject(jsonData);
                        JSONArray orgArray = jsonObject.getJSONArray("rows");

                        for (int i=0;i<group.size();i++) {



                            ArrayList<String> temp=new ArrayList<String>();

                            for (int j = 0; j < orgArray.length(); j++) {
                                String groupname = group.get(i);
                                JSONObject org_Array = orgArray.getJSONObject(j);
                                String[] pinyinArray = null;
                                if(groupname.equals(org_Array.getString("orgi_name"))){
                                    temp.add(org_Array.getString("username")+"@"+org_Array.getString("u_userid"));
                                }

//                                StringBuilder charCollector=new StringBuilder();
//                                for (int m=0;m<org_Array.getString("username").length();m++){
//                                    try{
//                                        String ch=getCharacterPinYin(org_Array.getString("username").charAt(m));
//                                        charCollector.append(ch);
//                                    }catch (Exception e){
//                                        e.printStackTrace();
//                                    }
//                                }
//
                                JMessageClient.register(org_Array.getString("u_userid"),"123456",null);

                            }

                            user.add(temp);
                        }

                        for(int i=0;i<group.size();i++){
                            tmp = new ArrayList<Integer>();
                            for (int j=0;j<user.get(i).size();j++){
                                tmp.add(0);
                            }
                            check_states.add(tmp);
                        }

                        Log.e("2","contact1");

                        expandablelistview.post(new Runnable() {
                            @Override
                            public void run() {
                                expandableListAdapter=new ExpandableListAdapter(group,user);
                                expandablelistview.setAdapter(expandableListAdapter);
                                expandablelistview.setVisibility(View.VISIBLE);
                                progressView.stopAnimation();
                                progressView.setVisibility(View.GONE);
                                loading_tx.setVisibility(View.GONE);
                                Log.e("12","contact1");
                            }
                        });
                    } catch (Exception e) {
                        Log.e("11","contact1");
                    }



                }else{
                    Log.e("7","contact1");
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(),
                                    "服务器异常4", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }

        });

    }

    private String getCharacterPinYin(char c)

    {
        String[] pinyin=null;
        HanyuPinyinOutputFormat format =new HanyuPinyinOutputFormat();
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);

        try {
            pinyin = PinyinHelper.toHanyuPinyinStringArray(c, format);
        } catch (BadHanyuPinyinOutputFormatCombination e) {
            e.printStackTrace();
        }
        if(pinyin == null)
            return null;
        return pinyin[0];
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView=inflater.inflate(R.layout.contact_list_main,container,false);
        expandablelistview = (ExpandableListView) rootView.findViewById(R.id.lv);
        expandablelistview.setVisibility(View.GONE);
        progressView=rootView.findViewById(R.id.progress_view);
        progressView.setVisibility(View.VISIBLE);
        loading_tx=rootView.findViewById(R.id.loading_textview);
        loading_tx.setVisibility(View.VISIBLE);
        choose=rootView.findViewById(R.id.contact_list_choose);

        extra= (TextView) rootView.findViewById(R.id.top_message_title);

        extra.setText("通讯录");

        choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<String> receive_users=new ArrayList<String>();
                for(int i=0;i<check_states.size();i++){
                    for(int j=0;j<check_states.get(i).size();j++){
                        if(check_states.get(i).get(j)==1){
                            receive_users.add(user.get(i).get(j));
                        }
                    }
                }

                Intent intent=new Intent(getActivity(), Sending_interface.class);
                intent.putStringArrayListExtra("receivers",receive_users);
                startActivity(intent);
            }
        });

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        executeRequestGroup(requestGroup);
    }

    private class ExpandableListAdapter extends BaseExpandableListAdapter {

        private final LayoutInflater inf;
        private ArrayList<String> groups;
        private ArrayList<ArrayList<String>>  users;

        public ExpandableListAdapter(ArrayList<String> groups, ArrayList<ArrayList<String>> children) {
            this.groups = groups;
            users = children;
            inf = LayoutInflater.from(getActivity());

        }

        @Override
        public int getGroupCount() {
            return groups.size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return users.get(groupPosition).size();
        }

        @Override
        public Object getGroup(int groupPosition) {
            return groups.get(groupPosition);
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return users.get(groupPosition).get(childPosition);
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

            final ChildViewHolder holder;
            if (convertView == null) {
                convertView = inf.inflate(R.layout.contact_list_child, parent, false);
                holder = new ChildViewHolder();
                holder.checkbox=(CheckBox) convertView.findViewById(R.id.contact_list_checkbox);
                holder.avator=convertView.findViewById(R.id.contact_list_avator);
                holder.avator.setImageResource(R.drawable.avator_male);
                holder.text1 = (TextView) convertView.findViewById(R.id.contact_list_name);
                holder.text2 = (TextView) convertView.findViewById(R.id.contact_list_user_id);
                convertView.setTag(holder);
            } else {
                holder = (ChildViewHolder) convertView.getTag();
            }

            final String name_display=getChild(groupPosition, childPosition).toString().split("@")[0];
            final String name_id=getChild(groupPosition, childPosition).toString().split("@")[1];

            if(check_states.get(groupPosition).get(childPosition) == 1) {
                holder.checkbox.setBackgroundResource(R.drawable.checked);
            }
            else {
                holder.checkbox.setBackgroundResource(R.drawable.checked_false);
            }

            holder.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked){
                        check_states.get(groupPosition).set(childPosition,1);
                        buttonView.setBackgroundResource(R.drawable.checked);
                        number++;
                    }else{
                        check_states.get(groupPosition).set(childPosition,0);
                        buttonView.setBackgroundResource(R.drawable.checked_false);
                        number--;
                    }

                    if(number==0){
                        choose.setVisibility(View.GONE);
                    }else{
                        choose.setVisibility(View.VISIBLE);
                    }
                }
            });

            holder.text1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(getActivity(),Sending_interface.class);
                    ArrayList<String> receivers=new ArrayList<String>();
                    receivers.add(getChild(groupPosition,childPosition).toString());
                    intent.putStringArrayListExtra("receivers",receivers);
                    startActivity(intent);
                }
            });


            holder.avator.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    Intent intent=new Intent(getActivity(),User_info.class);
//                    intent.putExtra("info_name",name_display);
//                    startActivity(intent);

                    Intent intent=new Intent(getActivity(),Sending_interface.class);
                    ArrayList<String> receivers=new ArrayList<String>();
                    receivers.add(getChild(groupPosition,childPosition).toString());
                    intent.putStringArrayListExtra("receivers",receivers);
                    startActivity(intent);
                }
            });


            holder.text1.setText(name_display);
            holder.text2.setText(name_id);

            return convertView;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            GroupViewHolder holder;

            if (convertView == null) {
                convertView = inf.inflate(R.layout.contact_list_group, parent, false);

                holder = new GroupViewHolder();
                holder.text = (TextView) convertView.findViewById(R.id.buddy_listview_group_name);

                convertView.setTag(holder);
            } else {
                holder = (GroupViewHolder) convertView.getTag();
            }

            holder.text.setText(getGroup(groupPosition).toString());

            return convertView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }

        @Override
        public void registerDataSetObserver(DataSetObserver observer) {
            super.registerDataSetObserver(observer);
        }

    }
    static class ChildViewHolder {
        protected TextView text1;
        protected TextView text2;
        protected CheckBox checkbox;
        protected ImageView avator;
    }

    static class GroupViewHolder{
        protected TextView text;
    }

}


