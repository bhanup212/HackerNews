package bhanupro.hackernews.Views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import bhanupro.hackernews.R;
import butterknife.BindView;
import butterknife.ButterKnife;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class DetailsActivity extends AppCompatActivity {
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.viewPager) ViewPager viewPager;
    @BindView(R.id.tabs) TabLayout tabLayout;
    @BindView(R.id.toolbar_title) TextView titleTv;
    @BindView(R.id.toolbar_url) TextView urlTv;
    @BindView(R.id.toolbar_date) TextView dateTv;
    ArticleFragment articleFrag;
    CommentFragment commentFrag;
    String totalComments = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        String url = getIntent().getStringExtra("URL");
        String comment = getIntent().getStringExtra("COMMENT");
        totalComments = getIntent().getStringExtra("TOTAL");
        String title = getIntent().getStringExtra("TITLE");
        String date = getIntent().getStringExtra("DATE");

        articleFrag = new ArticleFragment();
        Bundle b = new Bundle();
        b.putString("URL",url);
        Log.e("url","url is "+url);
        articleFrag.setArguments(b);
        urlTv.setText(url);
        titleTv.setText(title);
        dateTv.setText(date);

        Bundle commentBundle = new Bundle();
        commentBundle.putString("COMMENT",comment);
        commentFrag = new CommentFragment();
        commentFrag.setArguments(commentBundle);
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(commentFrag, "("+totalComments+")Total Comments");
        adapter.addFragment(articleFrag, "Article");
        viewPager.setAdapter(adapter);
    }
    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
