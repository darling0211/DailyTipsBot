package com.example.demo;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.*;

@SuppressWarnings("deprecation")
@Service
public class BotService extends TelegramLongPollingBot {

    // ✅ Your bot token & username
    private final String BOT_TOKEN = "8216545890:AAGes5adK3PF43R5XRagqbXxrM1kG0X4aFY";
    private final String BOT_USERNAME = "Life_hackz_Bot";

    // ✅ Store predefined daily business tips
    private final List<String> dailyTips = Arrays.asList(
            "💡 Focus on solving problems, not selling products. Customers love solutions.",
            "🚀 Consistency beats motivation. Small daily steps = big success.",
            "📊 Never depend on one income. Build multiple streams.",
            "🤝 Networking is your net worth. Connect with people daily.",
            "📱 Digital presence is mandatory in 2025. Grow online or stay behind.",
            "💰 Save money, but invest wisely. Inflation eats savings.",
            "🕒 Time is your biggest currency. Spend it smartly.",
            "🎯 A clear goal with deadlines is more powerful than talent."
    );

    private final Map<Long, Boolean> subscribers = new HashMap<>();
    private final Random random = new Random();

    @Override
    public String getBotUsername() {
        return BOT_USERNAME;
    }

    @Override
    public String getBotToken() {
        return BOT_TOKEN;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText().trim();
            Long chatId = update.getMessage().getChatId();

            switch (messageText.toLowerCase()) {
                case "/start":
                    sendMessage(chatId, "👋 Welcome to Daily Business Bot!\n\n" +
                            "Here you’ll get inspiring and practical business tips every day.\n\n" +
                            "➡ Use /help to explore commands.");
                    break;

                case "/help":
                    sendMessage(chatId, "📌 Available Commands:\n" +
                            "/start - Start using the bot\n" +
                            "/daily - Get today’s business tip\n" +
                            "/subscription - Check subscription status\n" +
                            "/pay - Subscribe to premium content\n" +
                            "/referral - Invite friends & earn rewards\n" +
                            "/cancel - Cancel your subscription\n" +
                            "/about - About this bot\n" +
                            "/info - Get bot information\n" +
                            "/unsubscribe - Stop daily updates");
                    break;

                case "/daily":
                    sendMessage(chatId, "🌟 Today’s Tip:\n\n" + getRandomTip());
                    break;

                case "/subscription":
                    if (subscribers.containsKey(chatId)) {
                        sendMessage(chatId, "✅ You are currently subscribed to daily tips!");
                    } else {
                        sendMessage(chatId, "❌ You are not subscribed yet. Use /subscribe or /pay to join.");
                    }
                    break;

                case "/pay":
                    sendMessage(chatId, "💳 To access premium business insights, complete your payment.\n" +
                            "👉 A payment link will be added soon (Razorpay Integration). Stay tuned!");
                    break;

                case "/referral":
                    sendMessage(chatId, "📢 Invite your friends!\n" +
                            "Share this bot link and earn bonus tips when they subscribe.\n" +
                            "🔗 https://t.me/" + BOT_USERNAME);
                    break;

                case "/cancel":
                case "/unsubscribe":
                    subscribers.remove(chatId);
                    sendMessage(chatId, "❌ You have cancelled your subscription. No more daily updates.");
                    break;

                case "/subscribe":
                    subscribers.put(chatId, true);
                    sendMessage(chatId, "✅ You are subscribed! You’ll receive tips every day at 9AM IST.");
                    break;

                case "/about":
                    sendMessage(chatId, "🤖 About This Bot:\n" +
                            "I share professional business tips and strategies daily to keep you motivated.\n\n" +
                            "👨‍💻 Powered by Spring Boot + Telegram Bot API.");
                    break;

                case "/info":
                    sendMessage(chatId, "ℹ Bot Information:\n" +
                            "• Name: Daily Business Bot\n" +
                            "• Version: 1.0\n" +
                            "• Creator: Your Team\n" +
                            "• Language: Java (Spring Boot)");
                    break;

                default:
                    sendMessage(chatId, "⚡ Unknown command! Type /help to see all available commands.");
            }
        }
    }

    // ✅ Get random tip
    private String getRandomTip() {
        return dailyTips.get(random.nextInt(dailyTips.size()));
    }

    // ✅ Auto send daily tips to subscribers at 9AM
    @Scheduled(cron = "0 0 9 * * *", zone = "Asia/Kolkata")
    public void sendDailyTips() {
        for (Long chatId : subscribers.keySet()) {
            sendMessage(chatId, "🌟 Your Daily Business Tip:\n\n" + getRandomTip());
        }
    }

    // ✅ Helper to send messages
    private void sendMessage(Long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());
        message.setText(text);
        message.enableMarkdown(true); // professional formatting

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}