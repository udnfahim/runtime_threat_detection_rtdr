package com.systemgrade.rtdr.controller.websocket;

import com.systemgrade.rtdr.dto.response.AlertStreamDTO;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class AlertStreamController {

    @MessageMapping("/subscribe.alerts")
    @SendTo("/topic/alerts")
    public AlertStreamDTO subscribe() { return new AlertStreamDTO(); }
}
