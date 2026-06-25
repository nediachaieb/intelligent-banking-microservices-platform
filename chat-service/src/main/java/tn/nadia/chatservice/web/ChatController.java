package tn.nadia.chatservice.web;


import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import tn.nadia.chatservice.agents.AIAgent;

@RestController
public class ChatController {

    private final AIAgent aiAgent;

    public ChatController(AIAgent aiAgent) {
        this.aiAgent = aiAgent;
    }

    @GetMapping("/chat")
    public Flux<String> chat(@RequestParam String query) {
        return aiAgent.ask(query);

    }


}