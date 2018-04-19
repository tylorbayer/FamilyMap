package fmc.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;

import java.net.URL;

import fmc.model.Model;
import fmc.server.Proxy;
import fmshared.fmrequest.LoginRequest;
import fmshared.fmrequest.RegisterRequest;
import fmshared.model.Events;
import fmshared.model.Persons;


public class MainActivity extends FragmentActivity implements Proxy.Context {
    private Model model = Model.getInstance();

    private LoginFragment loginFragment;
    private RegisterFragment registerFragment;
    private MapFragment mapFragment;
    private CheckBox chkRegister;
    private Button btnLogReg;
    private EditText edtServHost;
    private EditText edtServPort;

    private String hostNum;
    private int portNum;

    private LoginRequest logReq;
    private String authToken;

    private Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Iconify.with(new FontAwesomeModule());

        edtServHost = findViewById(R.id.serv_host);
        edtServPort = findViewById(R.id.serv_port);

        chkRegister = findViewById(R.id.chkRegister);
        chkRegister.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                chkRegisterChanged();
            }
        });

        btnLogReg = findViewById(R.id.btnLogReg);
        btnLogReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnLogRegClicked();
            }
        });

        FragmentManager fm = this.getSupportFragmentManager();
        loginFragment = (LoginFragment) fm.findFragmentById(R.id.loginFrameLayout);
        if (loginFragment == null) {
            loginFragment = LoginFragment.newInstance();
            fm.beginTransaction()
                    .add(R.id.loginFrameLayout, loginFragment)
                    .commit();
        }
    }

    private void chkRegisterChanged() {
        if (!chkRegister.isChecked()) {
            loginFragment = LoginFragment.newInstance();
            switchFragment(registerFragment, R.id.loginFrameLayout, loginFragment);

            btnLogReg.setText(R.string.login);
        }
        else {
            registerFragment = RegisterFragment.newInstance();
            switchFragment(loginFragment, R.id.registerFrameLayout, registerFragment);

            btnLogReg.setText(R.string.register);
        }
    }

    private void btnLogRegClicked() {
        if (!chkRegister.isChecked()) {
            if(!loginFragment.checkInput() || !checkInput()) {
                Toast.makeText(this, "Login information insufficient", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                logReq = loginFragment.getLogReq();
                login(logReq);
            }
            catch (Exception e) {
                Toast.makeText(this, "Login unsuccessful, " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
        else {
            if(!registerFragment.checkInput() || !checkInput()) {
                Toast.makeText(this, "Register information insufficient", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                register(registerFragment.getRegReq());
            }
            catch (Exception e) {
                Toast.makeText(this, "Register unsuccessful, " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }

    void switchFragment(Fragment removeFrag, int fragLayout, Fragment fragment) {
        FragmentManager fm = this.getSupportFragmentManager();
        fm.beginTransaction()
                .remove(removeFrag)
                .add(fragLayout, fragment)
                .commit();
    }

    @Override
    public void logRegPassed(boolean passed, String type, String authToken, String personID) {
        this.authToken = authToken;

        model.setAuthToken(authToken);
        model.setPersonID(personID);

        if (!passed)
            Toast.makeText(this, "Login or Register failed. Host or Port may be wrong. Try again.", Toast.LENGTH_LONG).show();
        else {
            if (type.equals("login")) {
                mapFragment = MapFragment.newInstance();
                switchFragment(loginFragment, R.id.mapFrameLayout, mapFragment);
            }
            else {
                mapFragment = MapFragment.newInstance();
                switchFragment(registerFragment, R.id.mapFrameLayout, mapFragment);
            }
        }
    }

    @Override
    public void populateMap(Events[] events, Persons[] persons) {
        model.setHostNum(hostNum);
        model.setPortNum(portNum);
        model.setLogReq(logReq);
        model.setAuthToken(authToken);
        model.setEvents(events);
        model.setPersons(persons);
    }

    @Override
    public void rePopulateMap(Events[] events, Persons[] persons) {}

    public boolean checkInput() {
        return !(edtServHost.getText().length() == 0 || edtServPort.getText().length() == 0);
    }

    public void login(LoginRequest logReq) throws Exception {
        try {
            hostNum = edtServHost.getText().toString();
            portNum = Integer.parseInt(edtServPort.getText().toString());

            Proxy proxy = new Proxy(gson.toJson(logReq), this, "login", hostNum, portNum);
            proxy.execute(new URL("http", hostNum, portNum, "user/login"));
        }
        catch (Exception e) {
            Toast.makeText(this, "Login failed. Host or Port may be wrong. Try again.", Toast.LENGTH_LONG).show();
        }
    }

    public void register(RegisterRequest regReq) throws Exception {
        try {
            hostNum = edtServHost.getText().toString();
            portNum = Integer.parseInt(edtServPort.getText().toString());

            Proxy proxy = new Proxy(gson.toJson(regReq), this, "register", hostNum, portNum);
            proxy.execute(new URL("http", hostNum, portNum, "user/register"));
        }
        catch (Exception e) {
            Toast.makeText(this, "Register failed. Host or Port may be wrong. Try again.", Toast.LENGTH_LONG).show();
        }
    }
}