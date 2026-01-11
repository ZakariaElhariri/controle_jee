package net.youssfi.aiagentchatbotspringaimcp.agents;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.support.ToolCallbacks;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import reactor.core.publisher.Flux;

import java.util.Arrays;

@Component
public class AIAgent {

    private ChatClient chatClient;



    //le builder il s'occupe de nous créer un objet
    public AIAgent(ChatClient.Builder builder,
                   ChatMemory memory,
                   ToolCallbackProvider tools
                   ) {
        Arrays.stream(tools.getToolCallbacks()).forEach(toolCallback -> {
            System.out.println("---------------------");
            System.out.println(toolCallback.getToolDefinition());
            System.out.println("---------------------");
        });
        this.chatClient = builder
                //on va créer une mémoire comme ça on'a un AGENT car LLM n'a pas de mémoire
                //avec les 20 dernières questions réponses
                .defaultSystem("You are un assistant that answers questions of users depending on the given context. If no context is given, answer: \"I DON'T KNOW\""
                        )
                .defaultAdvisors(
                        MessageChatMemoryAdvisor.builder(memory).build())
                //maintenant il'a une mémoire ou il va places les conversations = qst + réponses pour donner contexte à l'agent
                .defaultToolCallbacks(tools)
                .build();
    }


    public String askAgent(String query){
        return chatClient.prompt()
                .user(query)
                //call = 1 reponse jusqu'a ce qu'il fini
                //stream = repond au fur et a mesur ... comme gpt
                .call()
                .content();
    }

}
