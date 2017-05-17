package model_practicaltest02.eim.systems.cs.pub.ro.model_practicaltest02;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;

public class ModelPracticalTest02MainActivity extends AppCompatActivity {

    Button connectButton;
    EditText serverPortEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_model_practical_test02_main);

        connectButton = (Button)findViewById(R.id.connect_button);
        serverPortEditText = (EditText)findViewById(R.id.server_port_edit_text);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.server_frame_layout, new ServerFragment());
        fragmentTransaction.add(R.id.client_frame_layout, new ClientFragment());
        fragmentTransaction.commit();
    }
}
