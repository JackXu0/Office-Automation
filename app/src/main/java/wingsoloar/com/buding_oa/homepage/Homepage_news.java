package wingsoloar.com.buding_oa.homepage;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
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

/**
 * Created by wingsolarxu on 2017/8/25.
 */

public class Homepage_news extends Activity {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private TextView title;
    private RelativeLayout top_message;
    private ArrayList<String> images;
    private ArrayList<String> dates;
    private ArrayList<String> titles;
    private ArrayList<String> texts;
    private ImageView return_button;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage_news_main);
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        images=new ArrayList<>();
        dates=new ArrayList<>();
        titles=new ArrayList<>();
        texts=new ArrayList<>();

        images.add("homepage_news_test_image1");
        images.add("homepage_news_test_image2");

        dates.add("2017年8月22日");
        dates.add("2017年8月8日");

        titles.add("西浦2017招生工作结束 将迎来4000余名国内外本硕新生");
        titles.add("西浦学子暑假“不休假” 忙于参与国际科研项目");

        texts.add("“西交利物浦大学强调教职员工与学生之间的共同协作，学生独立自主的学习能力以及教师的悉心指导。” Barry Godfrey教授（上图）指出，“我们旨在培养出未来的领导者和新的思想家，这些人才将会以全新的方式来面对21世纪的挑战与机遇。”\n" +
                "\n" +
                "Godfrey教授表示，西交利物浦大学的学术发展战略是充分结合两所知名大学的传统优势与一所年轻大学的活力，同时以充满热情的教职员工为基础，在中国高等教育开拓一条创新之路。\n" +
                "\n" +
                "他还领导了利物浦大学和其他英国大学之间的合作项目——数字化圆形监狱，这是英国艺术与人文研究委员会的数字化转型计划的一部分，其目的是利用数字化科技的潜力改变艺术和人文学科的研究。\n" +
                "\n" +
                "Godfrey教授认为，数字化正在影响所有学科，他进一步阐释说，“‘数字化’几乎跨越了所有学科，从建筑学到工程学再到社会科学。西浦作为一个正在快速发展的大学，我们应该聚集各个学科的优秀研究人员，让他们共同参与到下一阶段的数字研究发展——虚拟工程和人工智能，并将我们的研究应用到现实世界中。”\n" +
                "\n" +
                "Barry Godfrey教授是比较犯罪学领域的专家，在该领域拥有超过二十年的教学和科研经验，研究领域包括国际犯罪史、犯罪中止以及违法行为纵向研究。他1995年任职英国基尔大学，从讲师开始一直晋升至犯罪学教授和科研主任，并于1997年获得英国莱斯特大学社会史学博士学位。\n" +
                "\n" +
                "在加入西交利物浦大学之前，Godfrey教授是社会正义学的教授，也是利物浦大学人文与社会科学学院的副院长。在利物浦大学时，他鼓励跨学科的合作，并认为这是创新研究的关键所在。他说：“感谢学校给予我专业的支持，也感谢一群友好且热情的研究专家，帮助我在各个领域进行前沿、有影响力的研究。”\n" +
                "\n" +
                "2016年4月， Godfrey教授首次来到西交利物浦大学参加中国文化遗产保护国际会议。会议中，他谈到了保护文化遗产与城市发展之间的冲突，并以英国的利物浦市作为案例。\n" +
                "\n" +
                "“我对苏州工业园区印象深刻，这里是西交利物浦大学的所在地，离苏州古城区很近，传统与创新相结合的特色非常吸引人。”他补充道：“作为中国最大的中外合作大学，西交利物浦大学在当时就给我留下了深刻的印象，十年前它的成立可以看作是中国高等教育发展历程中的一个重要里程碑。”\n" +
                "\n" +
                "最后，Godfrey教授表达了对西浦教职人员的赞赏，并谈到了帮助他们实现职业发展的计划，“我们拥有非常优秀的教职人员，学校通过各种培训和全方位的支持来提升他们的专业知识，我相信他们会留在西浦与我们一起获得更好的职业发展。对于那些刚步入职业生涯的教职人员来说，我希望能在西浦帮助他们发展成为未来世界知名的教授、企业家和研究领导者。”\n" +
                "\n");
        texts.add("暑假中，学生们通常会去旅行、看电视剧或拜访家人朋友，但有一些人有不一样的选择。\n" +
                "\n" +
                "一群西交利物浦大学的中国学生和国际生就选择充分利用暑假时光，在生物科学系实验室中参与科研项目。" +
                "\n今年七月份刚从西浦生物科学专业毕业的石浩然（下图）目前在Boris Tefsen博士的指导下开展关于抗生素检测的研究。");


        mAdapter = new MyAdapter(images,dates,titles);
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
        mRecyclerView = (RecyclerView) findViewById(R.id.homepage_news_main_listview);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        title=findViewById(R.id.top_message_title);
        top_message=findViewById(R.id.top_message);
        return_button=findViewById(R.id.top_message_return_button);
        return_button.setVisibility(View.VISIBLE);

    }


    class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

        private ArrayList<String> images;
        private ArrayList<String> dates;
        private ArrayList<String> titles;

        public MyAdapter(@Nullable ArrayList<String> images,
                         @Nullable ArrayList<String> dates,
                         @Nullable ArrayList<String> titles) {

            this.images=images;
            this.dates=dates;
            this.titles=titles;
        }


        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            // 实例化展示的view
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.homepage_news_item, parent, false);
            // 实例化viewholder
            ViewHolder viewHolder = new MyAdapter.ViewHolder(v);

            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            // 绑定数据
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                holder.image.setBackgroundResource(getResources().getIdentifier(images.get(position), "drawable", "wingsoloar.com.buding_oa"));
            }
            holder.date.setText(dates.get(position));
            holder.title.setText(titles.get(position));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(Homepage_news.this,Homepage_news_subpages.class);
                    intent.putExtra("image",images.get(position));
                    intent.putExtra("date",dates.get(position));
                    intent.putExtra("title",titles.get(position));
                    intent.putExtra("text",texts.get(position));
                    startActivity(intent);
                }
            });

        }


        @Override
        public int getItemCount() {
            return dates == null ? 0 : dates.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            View itemView;
            ImageView image;
            TextView date;
            TextView title;

            public ViewHolder(View itemView) {
                super(itemView);
                this.itemView=itemView;
                image=itemView.findViewById(R.id.image_news);
                date=itemView.findViewById(R.id.date);
                title=itemView.findViewById(R.id.title);
            }
        }
    }
}
