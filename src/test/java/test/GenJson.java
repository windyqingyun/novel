package test;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.JavaType;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jeeplus.common.exception.EncodeException;
import com.jeeplus.common.mapper.JsonMapper;
import com.jeeplus.common.utils.Des3;
import com.jeeplus.common.utils.IdGen;
import com.jeeplus.modules.bus.entity.Book;
import com.jeeplus.modules.bus.entity.BookChapter;
import com.jeeplus.modules.bus.utils.JsonFieldConst;

public class GenJson {
	public static void main(String[] args) throws EncodeException {
//		JSONObject jsonObj = new JSONObject();
//		JSONObject data = new JSONObject();
//		data.put("bookList", genBookJson());
//		
//		jsonObj.put("officeId", "123123");
//		
//		jsonObj.put("data", data.toJSONString());
//		
//		Map<String, String> map = Maps.newHashMap();
//		map.put("para", jsonObj.toJSONString());
//		
//		String json = JsonMapper.toJsonString(map);
		
		JsonMapper jsonMapper = JsonMapper.getInstance();
		JavaType bookListType = jsonMapper.createCollectionType(List.class, Book.class);
		List<Book> bookList = jsonMapper.fromJson(genBookJson(), bookListType);
		for (Book book : bookList) {
			System.out.println(book.getChapterList().size());
		}
	}
	
	public static String genBookJson(){
		String result = null;
		
		List<Book> bookList = Lists.newArrayList();
		for (int i = 0; i < 3; i++) {
			bookList.add(genBook());
		}
		
		return JsonMapper.toJsonString(bookList);
	}

	public static Book genBook(){
		Book book = new Book();
		book.setAuthor(IdGen.uuid());
		book.setName(IdGen.uuid());
		book.setPublishDate(new Date());
		book.setTags(IdGen.uuid());
		book.setChapterList(genBookChapterList());
		
		return book;
	}
	
	public static List<BookChapter> genBookChapterList(){
		List<BookChapter> chapterList = Lists.newArrayList();
		
		Random random = new Random();
		int length = random.nextInt(10);
		for (int i = 1; i < length; i++) {
			chapterList.add(genBookChapter(i));
		}
		
		return chapterList;
	}
	
	public static BookChapter genBookChapter(int chapter){
		BookChapter bookChapter = new BookChapter();
		bookChapter.setChapter(chapter);
		bookChapter.setContent(IdGen.uuid());
		bookChapter.setTitle(IdGen.uuid());
		bookChapter.setIsvip("0");
		
		return bookChapter;
	}
}
