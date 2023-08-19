package ru.mtucifiit.mtucifiit.view.sch.activitys;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import ru.mtucifiit.mtucifiit.R;
import ru.mtucifiit.mtucifiit.adapters.MtuciSchoolPageAdapter;
import ru.mtucifiit.mtucifiit.view.sch.ege_activitys.RusEgeActivity;

public class SchoolMainActivity extends AppCompatActivity {

    private LinearLayout dots = null;
    private TextView[] dotsTextView;

    private ViewPager viewPager;
    private AppCompatButton ege;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school_main);
        init();
    }


    private void init() {
        dots = findViewById(R.id.dots);
        ege = findViewById(R.id.ege);
        viewPager = findViewById(R.id.why_mtuci_pager);
        viewPager.setAdapter(new MtuciSchoolPageAdapter(this));
        ege.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SchoolMainActivity.this, RusEgeActivity.class));
            }
        });
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                addDtos(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        addDtos(0);

    }

    public void addDtos(Integer position) {
        dots.removeAllViews();
        dotsTextView = new TextView[7];
        for (int i = 0; i < dotsTextView.length; i++) {
            TextView textView = new TextView(this);
            textView.setText(Html.fromHtml("&#8226"));
            textView.setTextSize(35);
            textView.setTextColor(getResources().getColor(R.color.grey_100));
            dots.addView(textView);
            dotsTextView[i] = textView;
        }
        if (dotsTextView.length > 0) {
            dotsTextView[position].setTextColor(getResources().getColor(R.color.grey_900));
        }


    }
}