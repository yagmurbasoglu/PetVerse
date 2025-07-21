package com.petverse.weatherservice.model;

public class WeatherResponse {
    private CurrentWeather current_weather;

    public CurrentWeather getCurrent_weather() {
        return current_weather;
    }

    public void setCurrent_weather(CurrentWeather current_weather) {
        this.current_weather = current_weather;
    }

    public static class CurrentWeather {
        private double temperature;
        private double windspeed;
        private double winddirection;
        private int weathercode;
        private String time;

        public double getTemperature() { return temperature; }
        public void setTemperature(double temperature) { this.temperature = temperature; }

        public double getWindspeed() { return windspeed; }
        public void setWindspeed(double windspeed) { this.windspeed = windspeed; }

        public double getWinddirection() { return winddirection; }
        public void setWinddirection(double winddirection) { this.winddirection = winddirection; }

        public int getWeathercode() { return weathercode; }
        public void setWeathercode(int weathercode) { this.weathercode = weathercode; }

        public String getTime() { return time; }
        public void setTime(String time) { this.time = time; }
    }
}
