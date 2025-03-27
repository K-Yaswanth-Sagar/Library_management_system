package com.tw.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tw.service.IssueRecordService;

@RestController
@RequestMapping("/issuerecords")
public class IssueRecordController {
	
	private static final Logger logger = LoggerFactory.getLogger(BookController.class);
	
	@Autowired
	private IssueRecordService issueRecordService;

	@PostMapping("/issuebook/{id}")
	public ResponseEntity<?> issueBook(@PathVariable Long id){
		logger.info("Fetching book with ID: {}", id);
		return ResponseEntity.ok(issueRecordService.issueTheBook(id));
	}
	
	@PostMapping("/returnbook/{issueId}")
	public ResponseEntity<?> returnBook(@PathVariable Long issueId){
		logger.info("Returning book with issue ID: {}", issueId);
		return ResponseEntity.ok(issueRecordService.returnTheBook(issueId));
	}
}
