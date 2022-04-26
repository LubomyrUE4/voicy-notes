package com.example.voicy_notes.controller;

import com.example.voicy_notes.service.SpeechToTextService;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class SpeechToTextController {

    private final SpeechToTextService speechToTextService;

    @Autowired
    public SpeechToTextController(SpeechToTextService speechToTextService) {
        this.speechToTextService = speechToTextService;
    }

    @PostMapping("/speechToText")
    public ResponseEntity<String> convertSpeechToText(@RequestBody String base64Audio) {
        String result = speechToTextService.convertSpeechToText(base64Audio);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("result", result);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(jsonObject.toString(), HttpStatus.OK);
    }
}
