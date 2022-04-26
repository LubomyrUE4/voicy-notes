package com.example.voicy_notes.service;

import com.google.api.gax.core.CredentialsProvider;
import com.google.cloud.speech.v1.*;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Base64;
import java.util.List;

@Service
public class SpeechToTextService {
    private static final Logger logger = LoggerFactory.getLogger(SpeechToTextService.class);
    private final CredentialsProvider credentialsProvider;
    private final Storage storage;

    private SpeechSettings settings = null;

    @Autowired
    public SpeechToTextService(CredentialsProvider credentialsProvider, Storage storage) {
        this.credentialsProvider = credentialsProvider;
        this.storage = storage;
    }

    @PostConstruct
    public void initialize() throws IOException {
        settings = SpeechSettings.newBuilder().setCredentialsProvider(credentialsProvider).build();
    }

    public void writeToCloud(String base64Audio) {
        BlobId blobId_old = BlobId.of("totext-browser", "record.flac");
        storage.delete(blobId_old);

        BlobId blobId = BlobId.of("totext-browser", "record.webm");
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();
        System.out.println(base64Audio);
        base64Audio = base64Audio.trim();
        byte[] decodedByte = null;
        try {
            decodedByte = Base64.getDecoder().decode(base64Audio.split(",")[1]);
        } catch(Exception e) {
            e.printStackTrace();
        }

        storage.create(blobInfo, decodedByte);
    }

    public String convertSpeechToText(String base64Audio) {
        writeToCloud(base64Audio);

        StringBuilder processedText = new StringBuilder();
        try(SpeechClient speechClient = SpeechClient.create(settings)) {
            RecognitionConfig config = RecognitionConfig.newBuilder().setEncoding(RecognitionConfig.AudioEncoding.WEBM_OPUS).setLanguageCode("en-US")
                    .setEnableAutomaticPunctuation(true).setEnableWordTimeOffsets(true).build();

            RecognitionAudio recognitionAudio = RecognitionAudio.newBuilder().setUri("gs://totext-browser/record.webm").build();
            RecognizeResponse response = speechClient.recognize(config, recognitionAudio);
            List<SpeechRecognitionResult> speechResults = response.getResultsList();

            for(SpeechRecognitionResult speechResult : speechResults) {
                SpeechRecognitionAlternative alternative = speechResult.getAlternativesList().get(0);
                processedText.append(alternative.getTranscript());
            }

        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        logger.info(processedText.toString());
        return processedText.toString();
    }
}
