package model_practicaltest02.eim.systems.cs.pub.ro.model_practicaltest02;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class ServerFragment extends Fragment {

    private ServerThread serverThread;

    private Button connectButton;
    private EditText serverPortEditText;

    private class ConnectButtonClickListener implements Button.OnClickListener {

        @Override
        public void onClick(View v) {
            String serverPort = serverPortEditText.getText().toString();
            if (serverPort == null || serverPort.isEmpty()) {
                Log.d(Constants.TAG, "[MAIN] Port should be specified for server!");
                return;
            }
            serverThread = new ServerThread(Integer.parseInt(serverPort));
            if (serverThread.getServerSocket() == null) {
                Log.d(Constants.TAG, "[MAIN] Could not open server socket!");
                return;
            }
            serverThread.start();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle state) {
        return inflater.inflate(R.layout.fragment_server, parent, false);
    }

    @Override
    public void onActivityCreated(Bundle state) {
        super.onActivityCreated(state);

        connectButton = (Button)getActivity().findViewById(R.id.connect_button);
        serverPortEditText = (EditText)getActivity().findViewById(R.id.port_edit_text);

        View.OnClickListener connectButtonClickListener = new ConnectButtonClickListener();
        connectButton.setOnClickListener(connectButtonClickListener);
    }

    @Override
    public void onDestroy() {
        if (serverThread != null) {
            serverThread.stopServer();
        }
        super.onDestroy();
    }

}
