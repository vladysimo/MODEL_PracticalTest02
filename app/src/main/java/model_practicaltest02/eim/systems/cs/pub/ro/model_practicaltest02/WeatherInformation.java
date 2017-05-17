package model_practicaltest02.eim.systems.cs.pub.ro.model_practicaltest02;

/**
 * Created by vlad on 17/05/2017.
 */

public class WeatherInformation {

    public String temperature;
    public String windSpeed;
    public String pressure;
    public String humidity;

    public WeatherInformation() {}

    public WeatherInformation(String temperature, String windSpeed, String pressure, String humidity) {
        this.temperature = temperature;
        this.windSpeed = windSpeed;
        this.pressure = pressure;
        this.humidity = humidity;
    }

    @Override
    public String toString() {
        return temperature + windSpeed + pressure + humidity;
    }
}
