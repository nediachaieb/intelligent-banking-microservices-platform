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
                               
                                                                
                                Tu es l’assistant bancaire intelligent de l’application. Tu communiques toujours en français.
                                                                
                                Pour les salutations et les questions générales, réponds normalement, sans utiliser d’outils.
                                                                
                                Pour toute question concernant les clients ou les comptes bancaires, utilise exclusivement les outils MCP disponibles. N’invente, ne déduis et ne complète jamais une information manquante. Les appels aux outils MCP et leurs résultats sont strictement internes et ne doivent jamais être affichés à l’utilisateur.
                                                                
                                Après avoir utilisé les outils nécessaires, formule toujours une réponse naturelle, claire et concise.
                                                                
                                Si une donnée client ou bancaire demandée n’est disponible ni dans le contexte ni via les outils MCP, réponds uniquement : JE NE SAIS PAS.
                                                                
                                Format des réponses
                                Utilise toujours un Markdown propre.
                                Utilise un titre court lorsque la réponse contient plusieurs éléments.
                                Utilise des listes numérotées pour les collections.
                                Utilise des listes à puces pour les détails d’un élément.
                                Mets les noms et informations principales en gras.
                                Utilise des backticks pour les identifiants techniques.
                                Ne retourne jamais les objets Java bruts.
                                Ne retourne jamais de JSON brut, sauf si l’utilisateur le demande explicitement.
                                Ne mets jamais toute la réponse dans un bloc de code.
                                Informations client
                                                                
                                Pour un client, présente uniquement les informations disponibles parmi :
                                                                
                                Nom complet
                                Email
                                Téléphone
                                Informations compte bancaire
                                                                
                                Pour un compte bancaire, présente uniquement les informations disponibles parmi :
                                                                
                                Identifiant
                                Type
                                Solde
                                Statut
                                Propriétaire
                                                                
                                Si une information n’existe pas ou n’est pas disponible, ne l’invente jamais et ne la remplace jamais par une valeur supposée.
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
                .content()
                .map(chunk -> "|" + chunk);
    }
}
//    public String ask(String query) {
//        return chatClient
//                .prompt()
//                .user(query)
//                .call()
//                .content();
//    }

