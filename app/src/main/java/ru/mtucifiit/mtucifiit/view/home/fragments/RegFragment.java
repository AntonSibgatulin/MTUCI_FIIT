package ru.mtucifiit.mtucifiit.view.home.fragments;

import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import ru.mtucifiit.mtucifiit.R;
import ru.mtucifiit.mtucifiit.config.NetConfig;
import ru.mtucifiit.mtucifiit.service.RequestService;
import ru.mtucifiit.mtucifiit.view.home.activity.AuthenticationActivity;

public class RegFragment extends Fragment {


    private EditText username, code,code_fast;

    private TextView error_username, error_code,error_code_fast;

    private AppCompatButton enter;

    private AuthenticationActivity authenticationActivity;
    private RequestService requestService = null;

    public RegFragment(AuthenticationActivity authenticationActivity) {
        // Required empty public constructor
        this.authenticationActivity = authenticationActivity;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reg, container, false);
        init(view);
        return view;
    }

    private void init(View view) {
        NetConfig netConfig = new NetConfig();

        requestService = new RequestService(getContext());;

        username = view.findViewById(R.id.username);
        code = view.findViewById(R.id.code);
        code_fast = view.findViewById(R.id.code_fast);

        error_username = view.findViewById(R.id.error_text_username);
        error_code = view.findViewById(R.id.error_text_code);
        error_code_fast = view.findViewById(R.id.error_code_fast);

        enter = view.findViewById(R.id.enter);

        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String us_ = username.getText().toString();
                Integer code_ = Integer.valueOf(code.getText().toString());

                Integer code_fast_ = Integer.valueOf(code_fast.getText().toString());

                if (us_.length() > 20) {
                    error_username.setText("Username invalid!");
                    return;
                }
                if (code_ < 100000000 || code_ > 999999999) {
                    error_code.setText("Code invalid!");
                    return;
                }
                if (code_fast_ < 1000 || code_fast_ > 9999) {
                    error_code_fast.setText("Code invalid!");
                    return;
                }

                 requestService.getRequest(netConfig.reg+"?username="+us_+"&code="+code_+"&code_fast="+code_fast_,listener->{
                     try {
                         JSONObject jsonObject = new JSONObject(listener);
                         if(jsonObject.has("message")){
                             if(jsonObject.getString("message").equals("USERNAME_EXIST")){
                                 error_username.setText("User already exist");
                             }
                         }else {
                             requestService.saveMe(jsonObject.toString(),us_,code_fast_);
                             authenticationActivity.fine();
                         }

                     } catch (JSONException e) {
                         throw new RuntimeException(e);
                     }

                 },error -> {
                     error.printStackTrace();
                 });

            }
        });


    }
}