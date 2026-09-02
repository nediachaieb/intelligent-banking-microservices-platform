package tn.nadia.chatservice.agents;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.ToolCallAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.model.tool.ToolCallingManager;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.Arrays;

@Component
public class AIAgent {

    private final ChatClient chatClient;

    public AIAgent(
            ChatClient.Builder builder,
            ChatMemory memory,
            ToolCallbackProvider tools,
            ToolCallingManager toolCallingManager
    ) {

        Arrays.stream(tools.getToolCallbacks())
                .forEach(toolCallback -> {

                    System.out.println("--------------");
                    System.out.println(
                            toolCallback.getToolDefinition()
                    );
                    System.out.println("--------------");

                });


        var memoryAdvisor =
                MessageChatMemoryAdvisor
                        .builder(memory)
                        .build();


        var toolCallAdvisor =
                ToolCallAdvisor
                        .builder()

                        .toolCallingManager(
                                toolCallingManager
                        )

                        .disableMemory()

                        .suppressToolCallStreaming()

                        .build();


        this.chatClient =
                builder
                        .defaultSystem("""
                                Tu es un assistant bancaire intelligent.

                                Pour les salutations et les questions générales,
                                réponds normalement sans utiliser les outils.

                                Pour toute question concernant les clients ou les
                                comptes bancaires, utilise les outils MCP disponibles
                                et n'invente aucune information.

                                Les appels aux outils MCP et leurs résultats sont
                                internes. Ne les affiche jamais à l'utilisateur.

                                Après avoir utilisé les outils nécessaires,
                                formule toujours une réponse naturelle, claire
                                et concise pour l'utilisateur.

                                Si une donnée client ou bancaire demandée n'est
                                disponible ni dans le contexte ni via les outils MCP,
                                réponds uniquement : JE NE SAIS PAS.
                                """)

                        .defaultAdvisors(
                                memoryAdvisor,
                                toolCallAdvisor
                        )

                        .defaultToolCallbacks(tools)

                        .build();
    }


    public Flux<String> ask(String query) {

        return chatClient
                .prompt()
                .user(query)

                .advisors(advisor ->
                        advisor.param(
                                ChatMemory.CONVERSATION_ID,
                                "bank-chat"
                        )
                )

                .stream()
                .content();
    }
}
//    public String ask(String query) {
//        return chatClient
//                .prompt()
//                .user(query)
//                .call()
//                .content();
//    }

