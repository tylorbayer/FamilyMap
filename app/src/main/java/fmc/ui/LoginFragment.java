package fmc.ui;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import fmshared.fmrequest.LoginRequest;

public class LoginFragment extends Fragment {

    private EditText edtUsername;
    private EditText edtPassword;

    public LoginFragment() {
        // Required empty public constructor
    }

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_login, container, false);

        edtUsername = v.findViewById(R.id.username);
        edtPassword = v.findViewById(R.id.password);

        return v;
    }

    public boolean checkInput() {
        return !(edtUsername.getText().length() == 0 || edtPassword.getText().length() == 0);
    }

    public LoginRequest getLogReq() {
        return new LoginRequest(edtUsername.getText().toString(), edtPassword.getText().toString());
    }
}