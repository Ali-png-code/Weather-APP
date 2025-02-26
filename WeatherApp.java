import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;

public class WeatherApp {
    private static final String API_KEY = "YOUR_API_KEY";
    private static final String API_URL = "http://api.openweathermap.org/data/2.5/weather?q=";

    public static void main(String[] args) {
        JFrame frame = new JFrame("Weather App_ali");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLayout(new FlowLayout());

        JLabel cityLabel = new JLabel("Enter City:");
        JTextField cityField = new JTextField(15);
        JButton searchButton = new JButton("Get Weather");
        JTextArea resultArea = new JTextArea(10, 30);
        resultArea.setEditable(false);

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String city = cityField.getText().trim();
                if (!city.isEmpty()) {
                    String weatherData = getWeather(city);
                    resultArea.setText(weatherData);
                } else {
                    resultArea.setText("Please enter a city name.");
                }
            }
        });

        frame.add(cityLabel);
        frame.add(cityField);
        frame.add(searchButton);
        frame.add(new JScrollPane(resultArea));
        frame.setVisible(true);
    }

    private static String getWeather(String city) {
        try {
            String urlString = API_URL + city + "&appid=" + API_KEY + "&units=metric";
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();
            return parseWeather(response.toString());
        } catch (Exception e) {
            return "Error fetching weather data!";
        }
    }

    private static String parseWeather(String json) {
        JSONObject obj = new JSONObject(json);
        String cityName = obj.getString("name");
        JSONObject main = obj.getJSONObject("main");
        double temp = main.getDouble("temp");
        int humidity = main.getInt("humidity");
        JSONObject weather = obj.getJSONArray("weather").getJSONObject(0);
        String description = weather.getString("description");

        return String.format("City: %s\nTemperature: %.2f°C\nHumidity: %d%%\nCondition: %s", cityName, temp, humidity, description);
    }
}