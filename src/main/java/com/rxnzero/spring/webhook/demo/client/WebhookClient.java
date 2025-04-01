package com.rxnzero.spring.webhook.demo.client;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.OutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import spark.Spark;

public class WebhookClient {
    public static void main(String[] args) throws Exception {
        // 웹훅 수신 서버 시작
        Spark.port(8081);
        Spark.post("/webhook/callback", (req, res) -> {
            System.out.println("서버로부터 웹훅 호출 수신: " + req.body());
            return "클라이언트 웹훅 수신 완료";
        });

        // 서버로 웹훅 트리거 요청 보내기
        String webhookUrl = "http://localhost:8080/webhook/receive";
        String jsonPayload = "{\"message\": \"Hello, Webhook!\"}";
        
        URL url = new URL(webhookUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);
        
        try (OutputStream os = conn.getOutputStream()) {
            os.write(jsonPayload.getBytes());
            os.flush();
        }
        
        System.out.println("웹훅 응답 코드: " + conn.getResponseCode());
        try (InputStream is = conn.getInputStream(); Scanner scanner = new Scanner(is, StandardCharsets.UTF_8.name())) {
            System.out.println("서버 응답: " + scanner.useDelimiter("\\A").next());
        }
        conn.disconnect();
    }
}
