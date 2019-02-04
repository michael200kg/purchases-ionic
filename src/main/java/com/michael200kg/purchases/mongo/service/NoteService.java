package com.michael200kg.purchases.mongo.service;


import com.michael200kg.purchases.mongo.model.Note;

import java.util.List;

public interface NoteService {
	   public void saveManyNotes(List<Note> notes);
	   public void saveOneNote(Note note);	   
	   public List<Note> getAllNotes(); 
	   public void deleteOneNote(Long noteId);
	   public Note findById(Long noteId);
	   public void dropAllNotes();
	   public List<Note> getNotesByUsername(String username, boolean completed);
	   public void setCheckedItem(Long id, Long itemId, Boolean checked);
}
