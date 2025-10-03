package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@SpringBootApplication
public class DailyTipsBotApplication {

    public static void main(String[] args) {
        try {
            // Start Spring Boot
            SpringApplication.run(DailyTipsBotApplication.class, args);

            // Register your Telegram Bot
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(new BotService());

            System.out.println("✅ Daily Tips Bot is running...");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}