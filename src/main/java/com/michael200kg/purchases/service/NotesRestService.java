package com.michael200kg.purchases.service;


import com.michael200kg.purchases.mongo.model.Note;
import com.michael200kg.purchases.mongo.service.NoteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

//import org.springframework.http.HttpHeaders;


//@CrossOrigin(methods = {RequestMethod.DELETE, RequestMethod.GET, RequestMethod.POST, RequestMethod.OPTIONS, RequestMethod.HEAD},
//        origins = {"http://localhost:8080", "http://localhost:8080"},
//        maxAge = 3600)
@RestController
@RequestMapping("/notes/service")
public class NotesRestService {

    private static final Logger log = LoggerFactory.getLogger(NotesRestService.class);
    //	public final static String IP_ADDRESS = "79.111.14.182";

    @Autowired
    NoteService noteService;

    public static Comparator<Note> noteDateComparator
            = (note1, note2) -> {
                //descending order
                return note2.getCreatedDate().compareTo(note1.getCreatedDate());
            };

    @RequestMapping(value = "/notes/generate_note_id", method = RequestMethod.GET)
    Long generateNoteId() {
        List<Note> notes = noteService.getAllNotes();
        int maxId = 0;
        for (Note note : notes) {
            if (note.getNoteId().intValue() > maxId) {
                maxId = note.getNoteId().intValue();
            }
        }
        log.info("Generating next ID={}", Long.valueOf(maxId + 1));
        return Long.valueOf(maxId + 1);
    }

    @RequestMapping(value = "/notes/find_by_user/{user_name}", method = RequestMethod.GET)
    List<Note> getNotesByUsername(@PathVariable(value = "user_name") String username,
                                  @RequestParam(name = "archive", defaultValue = "false") Boolean archive) {
        List<Note> notes = noteService.getNotesByUsername(username, archive);
        Collections.sort(notes, noteDateComparator);
        return notes;
    }


    @RequestMapping(value = "/notes/find_by_id/{id}", method = RequestMethod.GET)
    Note getNoteById(@PathVariable(value = "id") Long id) {
        return noteService.findById(id);
    }


    @RequestMapping(value = "/notes/save_one_note", method = RequestMethod.POST)
    String saveOneNote(@RequestBody(required = true) Note instance) {
        log.info("Saving node: {}", instance);
        noteService.saveOneNote(instance);
        log.info("Saved OK!!!");
        return "OK";
    }

    //	                       /notes/3/item/3/set_checked_item/true
    @RequestMapping(value = "/notes/{id}/item/{item_id}/set_checked_item/{checked}", method = RequestMethod.POST)
    String setCheckedItem(@PathVariable(value = "id") Long id,
                          @PathVariable(value = "item_id") Long itemId,
                          @PathVariable(value = "checked") Boolean checked) {
        noteService.setCheckedItem(id, itemId, checked);
        return "OK";
    }

    @RequestMapping(value = "/notes/{id}/set_archive/{archive}", method = RequestMethod.POST)
    String setArchiveItem(@PathVariable(value = "id") Long id,
                          @PathVariable(value = "archive") Boolean archive) {
        Note note = noteService.findById(id);
        note.setArchive(archive);
        noteService.saveOneNote(note);
        return "OK";
    }

    @RequestMapping(value = "/notes/{id}/delete_one_note", method = RequestMethod.DELETE)
    String deleteOneNote(@PathVariable(value = "id") Long id) {
        log.info("Deleting node with id: {}", id);
        noteService.deleteOneNote(id);
        log.info("Deleted OK!");
        return "OK";
    }

    @RequestMapping(value = "/notes/test_save", method = RequestMethod.GET)
    String testSave() {
        Note note = new Note();
        Long id = generateNoteId();
        note.setNoteId(id);
        note.setCreatedDate(new Date());
        note.setName("TEST ITEM " + id);
        note.setText("TEST TEXT " + id);
        note.setUsername("MICHAEL");
        noteService.saveOneNote(note);
        return "OK";
    }

    @RequestMapping(value = "/notes/get_all", method = RequestMethod.GET)
    List<Note> getAll() {
        return noteService.getAllNotes();
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> rulesForCustomerNotFound(HttpServletRequest req, Exception e) {
        System.out.println(e.getMessage());
        log.error("ERROR OCCURED: ", e);
        return new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

}
// 176.195.58.172:8888