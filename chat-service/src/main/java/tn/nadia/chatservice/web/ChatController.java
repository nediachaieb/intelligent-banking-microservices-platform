package tn.nadia.chatservice.web;


import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import tn.nadia.chatservice.agents.AIAgent;

import java.awt.*;

@RestController
public class ChatController {

    private final AIAgent aiAgent;

    public ChatController(AIAgent aiAgent) {
        this.aiAgent = aiAgent;
    }

//    @GetMapping(value = "/chat" ,produces = MediaType.TEXT_PLAIN_VALUE)
//    public String chat(@RequestParam String query) {
//        return aiAgent.ask(query);
//
//    }
    @GetMapping(value = "/chat" ,produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> chat(@RequestParam String query) {
        return aiAgent.ask(query);

    }


}