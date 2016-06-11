package demo.likang.com.embeddedwebview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private EmbeddedWebView mWebView;

    private MyRecyclerView mRecyclerView;


    private String[] results = {"1","2","2","24","23","266","2555","255","25","24","23","22","2","2","24","23","266","2555","255","25","24","23","22"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mWebView = (EmbeddedWebView)this.findViewById(R.id.mWebView);
        mRecyclerView = (MyRecyclerView)findViewById(R.id.mRecyclerView);

        mWebView.loadUrl("http://www.jianshu.com/p/4535442d568f");
//        mWebView.getSettings().setSupportZoom(true);
//        mWebView.getSettings().setBuiltInZoomControls(true);
//        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.setInitialScale(100);
        mWebView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(new RecyclerView.Adapter() {
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return new ViewHolder(new TextView(parent.getContext()));
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
                ViewHolder viewHolder = (ViewHolder)holder;
                viewHolder.textView.setText(results[position]);
            }

            @Override
            public int getItemCount() {
                return results.length;
            }
        });





    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public ViewHolder(View view){
            super(view);
            textView = (TextView)view;
            textView.setHeight(200);
        }
        TextView textView;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}
