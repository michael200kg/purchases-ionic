package com.michael200kg.purchases.mongo.converters;

import com.michael200kg.purchases.mongo.model.Note;
import com.michael200kg.purchases.mongo.model.NoteItem;
import com.mongodb.DBObject;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Set;

@ReadingConverter
public class NoteReadConverter implements Converter<Document, Note> {

	@Override
	public Note convert(Document source) {
        Document dbo = (Document)source.get("items");
		ArrayList<NoteItem> items=null;
		if(dbo!=null) {
			SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
			items = new ArrayList<>();
			Set<String> keys = (Set<String>)dbo.keySet();
			for(String key: keys) {
                Document obj = (Document)dbo.get(key);
				Date date=null;
				try {
					date=sdf.parse((String) obj.get("checkedDate"));
				} catch (Exception e) {
					date=new Date();
				}
				NoteItem item = new NoteItem(Long.valueOf(key),
						 (Boolean)obj.get("checked"),
	                     date,
	                     (String) obj.get("itemName"),
	                     (String) obj.get("itemDescription"));
				items.add(item);
			}
		}
		SimpleDateFormat iso = new SimpleDateFormat();
		Note note=null;
		try {
			note = new Note((ObjectId) new ObjectId(((String)source.get("_id"))), 
							     (Date) source.get("createdDate"),
					             (Long) source.get("noteId"), 
					             (Boolean) source.get("checked"),
					             (Date) source.get("checkedDate"),
					             (String) source.get("name"),
					             (String) source.get("text"),
					             (String) source.get("username"),
					             (Boolean) source.get("archive"),
							     (Boolean) source.get("shared"),
					             (String) source.get("sharedForUsername"),
					             items);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	    return note;
	}

}
