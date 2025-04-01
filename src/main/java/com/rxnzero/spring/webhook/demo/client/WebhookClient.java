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
        // ���� ���� ���� ����
        Spark.port(8081);
        Spark.post("/webhook/callback", (req, res) -> {
            System.out.println("�����κ��� ���� ȣ�� ����: " + req.body());
            return "Ŭ���̾�Ʈ ���� ���� �Ϸ�";
        });

        // ������ ���� Ʈ���� ��û ������
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
        
        System.out.println("���� ���� �ڵ�: " + conn.getResponseCode());
        try (InputStream is = conn.getInputStream(); Scanner scanner = new Scanner(is, StandardCharsets.UTF_8.name())) {
            System.out.println("���� ����: " + scanner.useDelimiter("\\A").next());
        }
        conn.disconnect();
    }
}
