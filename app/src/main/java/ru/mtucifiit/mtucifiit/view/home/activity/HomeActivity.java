package ru.mtucifiit.mtucifiit.view.home.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import ru.mtucifiit.mtucifiit.R;
import ru.mtucifiit.mtucifiit.view.home.fragments.ProjectFragment;
import ru.mtucifiit.mtucifiit.view.home.fragments.ScheduleFragment;

public class HomeActivity extends AppCompatActivity {

    private Fragment[] fragments = {new ScheduleFragment(), new ProjectFragment()};

    private int[] schedule_img = {R.drawable.schedule_active,R.drawable.schedule_unactive};
    private int[] project_img = {R.drawable.project_active,R.drawable.project_unactive};

    private ConstraintLayout bottom_button_1, bottom_button_2;

    private int[] textColor = {R.color.top_panel_active,R.color.grey_600};

    private LinearLayout linearLayout;
    private FragmentContainerView fragmentContainerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        init();

    }

    public void init() {
        linearLayout = findViewById(R.id.bottom_menu);
        fragmentContainerView = findViewById(R.id.fragments);

         bottom_button_1 = (ConstraintLayout)linearLayout.getChildAt(0);
         bottom_button_2 = (ConstraintLayout)linearLayout.getChildAt(2);

        bottom_button_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                selectItem(0);
            }
        });


        bottom_button_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                selectItem(1);
            }
        });

        selectItem(0);
    }

    public void doActive(ConstraintLayout view, int id) {
        ImageView imageView = (ImageView) view.getChildAt(0);
        if(id == 0){
            imageView.setImageResource(schedule_img[0]);

        }else if(id == 1){
            imageView.setImageResource(project_img[0]);
        }
        setTextColor(view,0);
    }

    //0 - active 1 - unactive
    private void setTextColor(ConstraintLayout view,int type) {
        TextView textView = (TextView) view.getChildAt(1);
        textView.setTextColor(getResources().getColor(textColor[type]));
    }

    public void doUnActive(ConstraintLayout view, int id) {
        ImageView imageView = (ImageView) view.getChildAt(0);
        if(id == 0){
            imageView.setImageResource(schedule_img[1]);
        }else if(id == 1){
            imageView.setImageResource(project_img[1]);
        }
        setTextColor(view,1);

    }

    public void selectItem(int id) {
        if(id == 0){
            doActive(bottom_button_1,0);
            doUnActive(bottom_button_2,1);
        }else{
            doActive(bottom_button_2,1);
            doUnActive(bottom_button_1,0);
        }

        Fragment fragment = fragments[id];
        getSupportFragmentManager().beginTransaction().replace(R.id.fragments,fragment).addToBackStack(null).commit();


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (data != null) {
                String receivedData = data.getStringExtra("project_menu");
                if(receivedData!=null){
                    if(receivedData.equals("update")){
                        ProjectFragment projectFragment = (ProjectFragment) fragments[1];
                        projectFragment.update();
                    }
                }

            }
        }
    }
}