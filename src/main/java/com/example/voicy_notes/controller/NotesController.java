package com.example.voicy_notes.controller;

import com.example.voicy_notes.entity.Note;
import com.example.voicy_notes.service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class NotesController {

    private final NoteService noteService;

    @Autowired
    public NotesController(NoteService noteService) {
        this.noteService = noteService;
    }

    @GetMapping("/notes/all")
    public ResponseEntity<List<Note>> getAllNotes() {
        return new ResponseEntity<>(noteService.allNotes(), HttpStatus.OK);
    }

    @GetMapping("/notes/{id}")
    public ResponseEntity<Note> getNote(@PathVariable Long id) {
        return new ResponseEntity<>(noteService.getNoteById(id).get(), HttpStatus.OK);
    }

    @PostMapping(value = "/notes")
    public ResponseEntity<Note> addNote(@RequestBody Note note) {
        return new ResponseEntity<>(noteService.saveNote(note), HttpStatus.CREATED);
    }

    @PutMapping(value = "/notes")
    public ResponseEntity<Note> updateNote(@RequestBody Note note) {
        return new ResponseEntity<>(noteService.saveNote(note), HttpStatus.OK);
    }

    @DeleteMapping("/notes/{id}")
    public ResponseEntity<String> deleteNote(@PathVariable Long id) {
        return new ResponseEntity<>(noteService.deleteNoteById(id), HttpStatus.OK);
    }

    @GetMapping("/notes/all/{id}")
    public ResponseEntity<?> getAllNotesByUserId(@PathVariable Long id)  {
        return new ResponseEntity<>(noteService.allNotes(id), HttpStatus.OK);
    }
}
