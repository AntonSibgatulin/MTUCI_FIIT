package ru.mtucifiit.mtucifiit.view.home.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;

import android.os.Bundle;
import android.view.View;

import ru.mtucifiit.mtucifiit.R;
import ru.mtucifiit.mtucifiit.view.home.fragments.LoginFragment;
import ru.mtucifiit.mtucifiit.view.home.fragments.RegFragment;

public class AuthenticationActivity extends AppCompatActivity {

    private FragmentContainerView fragmentContainerView;

    private Fragment[] fragments = {
            new LoginFragment(this),
            new RegFragment(this)

    };

    private AppCompatButton auth,reg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);
        init();
    }

    public void init(){
       // fragmentContainerView = findViewById(R.id.fragmentView);
        auth = findViewById(R.id.auth);
        reg = findViewById(R.id.reg);

        auth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reg();
            }
        });
        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth();
            }
        });
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentView,fragments[0]).commit();
    }
    public void reg() {
        reg.setVisibility(View.VISIBLE);
        auth.setVisibility(View.GONE);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentView,fragments[0]).commit();
    }
    public void auth() {

        reg.setVisibility(View.GONE);
        auth.setVisibility(View.VISIBLE);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentView,fragments[1]).commit();
    }

    public void fine() {
        finish();
    }
}