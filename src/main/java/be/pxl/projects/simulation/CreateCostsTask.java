package be.pxl.projects.simulation;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

public class CreateCostsTask {

    private static final Random RANDOM = new Random();
    private String projectId;

    public CreateCostsTask(String projectId) {
        this.projectId = projectId;
    }

    private void createCostsForProject() {
        String costJson = generateCostJson(projectId);
        System.out.println("Sending " + costJson);
        sendCostRequest(costJson);
    }

    private boolean sendCostRequest(String costJson) {
        try {
            URL url = new URL("http://localhost:8080/costs");  // Replace with actual cost API endpoint
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            connection.getOutputStream().write(costJson.getBytes());

            int responseCode = connection.getResponseCode();
            return responseCode == HttpURLConnection.HTTP_OK;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private String generateCostJson(String projectId) {
        double amount = (RANDOM.nextInt(690000) + 10000) / 1000.0;  // Random cost between 100 and 7000
        String description = "Random cost description";
        String formattedAmount = String.format(Locale.US, "%.2f", amount);
        return String.format(
                "{\"projectId\": \"%s\", \"cost\": %s, \"description\": \"%s\"}",
                projectId, formattedAmount, description
        );
    }
}
