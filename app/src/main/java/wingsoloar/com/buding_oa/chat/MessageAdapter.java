package wingsoloar.com.buding_oa.chat;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.List;

import cn.jpush.im.android.api.content.TextContent;
import cn.jpush.im.android.api.enums.MessageDirect;
import cn.jpush.im.android.api.model.Message;
import wingsoloar.com.buding_oa.R;
import wingsoloar.com.buding_oa.information.User_info;

/**
 * Created by wingsolarxu on 2017/7/29.
 */

public class MessageAdapter extends ArrayAdapter<Message> {

    private List<Message> mdata;
    private LayoutInflater inflater;

    public MessageAdapter(Context context, List<Message> data) {
        super(context,-1, data);
        this.mdata=data;
        inflater= LayoutInflater.from(context);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        String name_the_other;
        ViewHolder holder=null;
        if(convertView==null){
            if(mdata.get(position).getDirect()== MessageDirect.send) {
                convertView = inflater.inflate(R.layout.jmui_chat_item_send_text, parent, false);
                holder = new ViewHolder();
                holder.textView = (TextView) convertView.findViewById(R.id.jmui_msg_content);
            }else{
                convertView=inflater.inflate(R.layout.jmui_chat_item_receive_text,parent,false);
                holder=new ViewHolder();
                holder.textView= (TextView) convertView.findViewById(R.id.jmui_receive_text);
            }
            convertView.setTag(holder);
        }else{
            holder= (ViewHolder) convertView.getTag();
        }

        TextContent textContent= (TextContent) mdata.get(position).getContent();

        holder.textView.setText(textContent.getText());
        return convertView;
    }

    private class ViewHolder{
        TextView textView;
    }
}
