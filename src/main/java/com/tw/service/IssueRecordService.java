package com.tw.service;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.tw.dto.IssueRecordDTO;
import com.tw.entity.Book;
import com.tw.entity.IssueRecord;
import com.tw.entity.User;
import com.tw.globalexceptionhandler.ResourceNotFoundException;
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
	
	
	public IssueRecordDTO issueTheBook(Long id) {

		Book book = bookRepo.findById(id)
	            .orElseThrow(() -> new ResourceNotFoundException("Book not found with ID: " + id));
		
		if(book.getQuantity() <= 0 || !book.isAvaliable()) {
			throw new RuntimeException("Book is not avaliable");
		}
		
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		User user = userRepo.findByUsername(username)
				.orElseThrow(() -> new ResourceNotFoundException("User not found"));
		
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
		issueRecordRepo.save(issueRecord);
		
		IssueRecordDTO issueRecordDTO = new IssueRecordDTO();
		issueRecordDTO.setBookTitle(issueRecord.getBook().getTitle());
		issueRecordDTO.setUserIdNumber(issueRecord.getUser().getId());
		issueRecordDTO.setUserName(issueRecord.getUser().getUsername());
		issueRecordDTO.setDueDate(issueRecord.getDueDate());
		issueRecordDTO.setId(issueRecord.getId());
		issueRecordDTO.setIssueDate(issueRecord.getIssueDate());
		
		return issueRecordDTO;
		
	}

	public IssueRecordDTO returnTheBook(Long issueId) {

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
		issueRecordRepo.save(issueRecord);
		 
		IssueRecordDTO issueRecordDTO = new IssueRecordDTO(); 
		issueRecordDTO.setReturnDate(issueRecord.getReturnDate());
		issueRecordDTO.setReturened(issueRecord.isReturened());
		issueRecordDTO.setBookTitle(issueRecord.getBook().getTitle());
		issueRecordDTO.setUserIdNumber(issueRecord.getUser().getId());
		issueRecordDTO.setUserName(issueRecord.getUser().getUsername());
		issueRecordDTO.setDueDate(issueRecord.getDueDate());
		issueRecordDTO.setId(issueRecord.getId());
		issueRecordDTO.setIssueDate(issueRecord.getIssueDate());
		
		return issueRecordDTO;
	}

}
