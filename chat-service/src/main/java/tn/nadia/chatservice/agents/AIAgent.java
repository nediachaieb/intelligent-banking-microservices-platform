package tn.nadia.chatservice.agents;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.mcp.client.common.autoconfigure.properties.McpClientCommonProperties;
import org.springframework.ai.support.ToolCallbacks;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Component
public class AIAgent {

    private ChatClient chatClient;

    public AIAgent(ChatClient.Builder builder , ChatMemory memory , ToolCallbackProvider tools) {
        this.chatClient = builder
                .defaultSystem("""
                        Vous un assistant qui se charge de répondre aux question
                        de l'utilisateur en fonction du contexte fourni.
                        Si aucun contexte n'est fourni, répond avec JE NE SAIS PAS         
                        """)
                .defaultAdvisors(
                        MessageChatMemoryAdvisor.builder(memory).build())
               .defaultToolCallbacks(tools)
                .build();
    }


    public Flux<String> ask(String message) {
        return chatClient.prompt()
                .user(message)
                .stream()
                .content();

    }
}
