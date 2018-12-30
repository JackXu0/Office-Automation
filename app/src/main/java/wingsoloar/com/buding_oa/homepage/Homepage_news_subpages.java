package wingsoloar.com.buding_oa.homepage;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import wingsoloar.com.buding_oa.R;

/**
 * Created by wingsolarxu on 2017/8/25.
 */

public class Homepage_news_subpages extends Activity{

    private String image;
    private String title;
    private String date;
    private String text;

    private ImageView image_iv;
    private TextView title_tv;
    private TextView date_tv;
    private TextView text_tv;
    private LinearLayout back_button;
    private TextView top_message_title;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage_news_subpage);
        image=getIntent().getStringExtra("image");
        title=getIntent().getStringExtra("title");
        date=getIntent().getStringExtra("date");
        text=getIntent().getStringExtra("text");

        image_iv=findViewById(R.id.image);
        title_tv=findViewById(R.id.title);
        date_tv=findViewById(R.id.date);
        text_tv=findViewById(R.id.content);
        back_button=findViewById(R.id.back_button);

        image_iv.setBackgroundResource(getResources().getIdentifier(image, "drawable", "wingsoloar.com.buding_oa"));
        title_tv.setText(title);
        date_tv.setText(date);
        text_tv.setText(text);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
