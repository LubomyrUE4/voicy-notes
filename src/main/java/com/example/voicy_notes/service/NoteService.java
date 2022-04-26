package com.example.voicy_notes.service;

import com.example.voicy_notes.entity.Note;
import com.example.voicy_notes.entity.User;
import com.example.voicy_notes.repository.NoteRepository;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;


@Service
public class NoteService {
    private final NoteRepository noteRepository;

    public NoteService(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    public Note saveNote(Note note) {
        note.setUser((User)SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        return noteRepository.save(note);
    }

    public Note saveDefaultNote(User user) {
        Note defaultNote = new Note();
        defaultNote.setName("Example Note");
        defaultNote.setText("Here you can write text or use Speech To Text Converter.");
        defaultNote.setLastModified(Timestamp.valueOf(LocalDateTime.now()));
        defaultNote.setUser(user);
        return noteRepository.save(defaultNote);
    }

    public String deleteNoteById(Long id) {
        JSONObject jsonObject = new JSONObject();
        try {
            noteRepository.deleteById(id);
            jsonObject.put("message", "User deleted successfully");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    public List<Note> allNotes() {
        List<Note> result = noteRepository.findAll();
        List<Note> toRemove = new ArrayList<>();

        result.forEach((note) -> {
            if(note.getUser().getId() != ((User)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId()) {
                toRemove.add(note);
            }
        });

        result.removeAll(toRemove);
        result.sort(Comparator.comparing(Note::getId).reversed());

        return result;
    }

    public List<Note> allNotes(Long userId) {
        List<Note> result = noteRepository.findAll();
        List<Note> toRemove = new ArrayList<>();

        result.forEach((note) -> {
            if(note.getUser().getId() != userId || !note.getIsPublic()) {
                toRemove.add(note);
            }
        });

        result.removeAll(toRemove);

        return result;
    }

    public Optional<Note> getNoteById(Long id) {
        return noteRepository.findById(id);
    }
}
