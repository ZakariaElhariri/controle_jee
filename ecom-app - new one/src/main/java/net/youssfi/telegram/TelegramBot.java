package net.youssfi.aiagentchatbotspringaimcp.telegram;

import jakarta.annotation.PostConstruct;
import net.youssfi.aiagentchatbotspringaimcp.agents.AIAgent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.ActionType;
import org.telegram.telegrambots.meta.api.methods.send.SendChatAction;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Component
public class TelegramBot extends TelegramLongPollingBot {

    @Value("${telegram.api.key}")
    private String telegramBotToken;
    private AIAgent aiAgent;



    public TelegramBot(AIAgent aiAgent) {
        this.aiAgent = aiAgent;
        //au demarrage l'agent doit faire subscribe au telegram api
        //chaque fois un message arrive, notre agent va le recevoir
    }


    //s'execute juste après execution = annotation --
    @PostConstruct
    public void registerTelegramBot(){
        try {
            TelegramBotsApi api = new TelegramBotsApi(DefaultBotSession.class);
            //subscribe
            api.registerBot(this);
        } catch (TelegramApiException e) {
            System.out.println("Error: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }


    //le bot telegram utilise notre agent

    @Override
    public void onUpdateReceived(Update telegramRequest) {
        try {
            if(!telegramRequest.hasMessage()) return;
            String messageText = telegramRequest.getMessage().getText();
            Long chatId=telegramRequest.getMessage().getChatId();

            sendTypingMessage(chatId);

            String answer = aiAgent.askAgent(messageText);
            //envoyer reponse a telegram

            sendTextMessage(chatId,answer);
        } catch (TelegramApiException e) {
            System.out.println("Error: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }




    @Override
    public String getBotUsername() {
        return "spring-aibot";
    }

    @Override
    public String getBotToken() {
        return telegramBotToken;
    }

    private void sendTextMessage(long chatId, String text) throws TelegramApiException {
        SendMessage sendMessage = new SendMessage(String.valueOf(chatId),text);
        execute(sendMessage);
    }

    //cette fonction montre que je suis entrain d'écrire
    private void sendTypingMessage(long chatId) throws TelegramApiException {
        SendChatAction sendChatAction= new SendChatAction();
        sendChatAction.setChatId(String.valueOf(chatId));
        sendChatAction.setAction(ActionType.TYPING);
        execute(sendChatAction);
    }
}
