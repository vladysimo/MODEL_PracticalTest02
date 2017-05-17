package model_practicaltest02.eim.systems.cs.pub.ro.model_practicaltest02;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ClientFragment extends Fragment {

    private EditText addressEditText;
    private EditText portEditText;
    private EditText cityEditText;
    private EditText parametersEditText;
    private Button getForecastButton;
    private TextView weatherTextView;

    private ForecastButtonClickListener forecastButtonClickListener = new ForecastButtonClickListener();
    private class ForecastButtonClickListener implements Button.OnClickListener {

        @Override
        public void onClick(View view) {
            String address = addressEditText.getText().toString();
            String port = portEditText.getText().toString();
            String city = cityEditText.getText().toString();
            String parameters = parametersEditText.getText().toString();
            System.out.println(address + " " + port + " " + city + " " + parameters);
            ClientCommunicationThread clientCommunicationThread = new ClientCommunicationThread(address, port, city, parameters, weatherTextView);
            clientCommunicationThread.start();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle state) {
        return inflater.inflate(R.layout.fragment_client, parent, false);
    }

    @Override
    public void onActivityCreated(Bundle state) {
        super.onActivityCreated(state);

        addressEditText = (EditText)getActivity().findViewById(R.id.server_address_edit_text);
        portEditText = (EditText)getActivity().findViewById(R.id.server_port_edit_text);
        cityEditText = (EditText)getActivity().findViewById(R.id.city_edit_text);
        parametersEditText = (EditText)getActivity().findViewById(R.id.parameters_edit_text);
        getForecastButton = (Button)getActivity().findViewById(R.id.get_forecast_button);
        getForecastButton.setOnClickListener(forecastButtonClickListener);
        weatherTextView = (TextView)getActivity().findViewById(R.id.weather_text_view);
    }

}
