package com.example.myredistest.pubsub;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessageListenService implements MessageListener {

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            Object obj = mapper.readValue(message.getBody(), Object.class);
            log.info("Received Message : {}",obj.toString());

        } catch (IOException ex) {
            throw new RuntimeException();
        }

    }
}
