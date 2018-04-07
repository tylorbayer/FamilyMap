package fmc.ui;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;

import fmshared.fmrequest.RegisterRequest;

public class RegisterFragment extends Fragment {

    private EditText edtUsername;
    private EditText edtPassword;
    private EditText edtFirstName;
    private EditText edtLastName;
    private EditText edtEmail;
    private RadioButton rdoFemale;
    private RadioButton rdoMale;


    public RegisterFragment() {
        // Required empty public constructor
    }

    public static RegisterFragment newInstance() {
        return new RegisterFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_register, container, false);

        edtUsername = v.findViewById(R.id.username);
        edtPassword = v.findViewById(R.id.password);
        edtFirstName = v.findViewById(R.id.first_name);
        edtLastName = v.findViewById(R.id.last_name);
        edtEmail = v.findViewById(R.id.email);
        rdoFemale = v.findViewById(R.id.rdoFemale);
        rdoMale = v.findViewById(R.id.rdoMale);

        return v;
    }

    public boolean checkInput() {
        return !(edtUsername.getText().length() == 0 || edtPassword.getText().length() == 0 ||
                edtFirstName.getText().length() == 0 || edtLastName.getText().length() == 0 ||
                edtEmail.getText().length() == 0 || (!rdoFemale.isChecked() && !rdoMale.isChecked()));
    }

    public RegisterRequest getRegReq() {
        String gender;

        if (rdoFemale.isChecked())
            gender = "f";
        else
            gender = "m";

        return new RegisterRequest(edtUsername.getText().toString(), edtPassword.getText().toString(),
                edtEmail.getText().toString(), edtFirstName.getText().toString(), edtLastName.getText().toString(),
                gender);
    }
}