package be.pxl.projects.simulation;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Random;
import java.util.UUID;

public class CreateProjectsTask {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final Random RANDOM = new Random();
    private static final String API_URL = "http://localhost:8080/projects";
    private static final String[] PRIORITIES = {"HIGH", "MEDIUM", "LOW"};
    private static final int PROJECTS_PER_THREAD = 10;  // Number of projects each thread will create
    private final ChronoUnit timeUnit;
    private final int timeValue;

    public CreateProjectsTask(ChronoUnit timeUnit, int timeValue) {
        this.timeUnit = timeUnit;
        this.timeValue = timeValue;
    }

    private UUID sendProjectRequest(int projectNumber, LocalDateTime dueDate) {
        try {
            String projectName = Thread.currentThread().getName() + " - " + projectNumber;
            String jsonBody = generateProjectJson(projectName, dueDate);
            System.out.println("Sending " + jsonBody);
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_URL))
                    .header("Content-Type", "application/json")
                    .timeout(Duration.ofSeconds(5))
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                System.out.println("creation succeeded....");
                return UUID.fromString(response.body().trim().replace("\"", "")); // Assuming body contains the UUID string
            } else {
                System.out.println("Failed to created project: " + response.body());
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        return new UUID(0L, 0L);
    }

    private String generateProjectJson(String projectName, LocalDateTime dueDate) {
        String description = "Description of " + projectName;
        int budget = RANDOM.nextInt(67000) + 3000;  // Random budget between 3000 and 70000
        String priority = PRIORITIES[RANDOM.nextInt(PRIORITIES.length)];
        String dueDateAsString = FORMATTER.format(dueDate);

        return String.format(
                "{\"name\": \"%s\", \"description\": \"%s\", \"dueDate\": \"%s\", \"budget\": %d, \"priority\": \"%s\"}",
                projectName, description, dueDateAsString, budget, priority
        );
    }
}
