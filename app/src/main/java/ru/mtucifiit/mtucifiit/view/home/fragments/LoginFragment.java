package ru.mtucifiit.mtucifiit.view.home.fragments;

import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import ru.mtucifiit.mtucifiit.R;
import ru.mtucifiit.mtucifiit.config.NetConfig;
import ru.mtucifiit.mtucifiit.service.RequestService;
import ru.mtucifiit.mtucifiit.view.home.activity.AuthenticationActivity;

public class LoginFragment extends Fragment {


    private EditText username, code;

    private TextView error_username, error_code;

    private AppCompatButton enter;

    private AuthenticationActivity authenticationActivity;
    private RequestService requestService = null;

    public LoginFragment(AuthenticationActivity activity) {
        // Required empty public constructor
        authenticationActivity = activity;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        init(view);
        return view;
    }

    private void init(View view) {
        NetConfig netConfig = new NetConfig();

        requestService = new RequestService(getContext());;

        username = view.findViewById(R.id.username);
        code = view.findViewById(R.id.code);

        error_username = view.findViewById(R.id.error_text_username);
        error_code = view.findViewById(R.id.error_text_code);

        enter = view.findViewById(R.id.enter);

        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String us_ = username.getText().toString();
                Integer code_ = Integer.valueOf(code.getText().toString());
                if (us_.length() > 20) {
                    error_username.setText("Username invalid!");
                    return;
                }
                if (code_ < 1000 || code_ > 9999) {
                    error_code.setText("Code invalid!");
                    return;
                }


                requestService.getRequest(netConfig.auth+"?username="+us_+"&code_fast="+code_,listener->{
                    try {
                        JSONObject jsonObject = new JSONObject(listener);
                        if(jsonObject.has("message")){
                            if(jsonObject.getString("message").equals("USER_NOT_FOUND")){
                                error_username.setText("User authentication error");
                            }
                        }else {
                            requestService.saveMe(jsonObject.toString(), us_, code_);
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