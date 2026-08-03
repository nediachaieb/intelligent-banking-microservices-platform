package tn.nadia.chatservice.agents;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.mcp.client.common.autoconfigure.properties.McpClientCommonProperties;
import org.springframework.ai.support.ToolCallbacks;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.Arrays;

@Component
public class AIAgent {

    private ChatClient chatClient;

    public AIAgent(ChatClient.Builder builder , ChatMemory memory , ToolCallbackProvider tools) {
        Arrays.stream(tools.getToolCallbacks()).forEach(toolCallback -> {

            System.out.println("--------------");
            System.out.println(toolCallback.getToolDefinition());
            System.out.println("--------------");


        });

        this.chatClient = builder
                .defaultSystem("""
                         Vous êtes un assistant chargé de répondre aux questions
                                de l'utilisateur à partir du contexte et des outils disponibles.
                                                
                                Utilisez les outils disponibles lorsque la question nécessite
                                des données externes.
                                                
                                Si l'information demandée n'est disponible ni dans le contexte
                                ni dans les outils, répondez uniquement : JE NE SAIS PAS.         
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
