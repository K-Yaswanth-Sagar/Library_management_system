package com.tw.service;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.tw.entity.Book;
import com.tw.entity.IssueRecord;
import com.tw.entity.User;
import com.tw.repository.BookRepo;
import com.tw.repository.IssueRecordRepo;
import com.tw.repository.UserRepo;

@Service
public class IssueRecordService {

	@Autowired
	private IssueRecordRepo issueRecordRepo;
	
	@Autowired
	private BookRepo bookRepo;
	
	@Autowired
	private UserRepo userRepo;
	
	
	public IssueRecord issueTheBook(Long id) {

		Book book = bookRepo.findById(id)
				.orElseThrow(() -> new RuntimeException("Book not found"));
		
		if(book.getQuantity() <= 0 || !book.isAvaliable()) {
			throw new RuntimeException("Book is not avaliable");
		}
		
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		User user = userRepo.findByUsername(username)
				.orElseThrow(() -> new RuntimeException("User not found"));
		
		IssueRecord issueRecord = new IssueRecord();
		issueRecord.setIssueDate(LocalDate.now());
		issueRecord.setDueDate(LocalDate.now().plusDays(15));
		issueRecord.setReturened(false);
		issueRecord.setBook(book);
		issueRecord.setUser(user);
		
		book.setQuantity(book.getQuantity() - 1);
		if(book.getQuantity() == 0) {
			book.setAvaliable(false);
		}
		
		bookRepo.save(book);
		return issueRecordRepo.save(issueRecord);
		
	}

	public IssueRecord returnTheBook(Long issueId) {

		IssueRecord issueRecord = issueRecordRepo.findById(issueId)
				.orElseThrow(() -> new RuntimeException("Record is not found"));
		
		if(issueRecord.isReturened()) {
			throw new RuntimeException("Book is already returned");
		}
		
		Book book = issueRecord.getBook();
		book.setQuantity(book.getQuantity() + 1);
		book.setAvaliable(true);
		bookRepo.save(book);
		
		issueRecord.setReturnDate(LocalDate.now());
		issueRecord.setReturened(true);
		
		return issueRecordRepo.save(issueRecord);
	}

}
