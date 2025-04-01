package com.tw.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class IssueRecordDTO {
	
	private Long id;
	private LocalDate issueDate;
	private LocalDate dueDate;
	private LocalDate returnDate;
	private boolean isReturened;
	
	private String bookTitle;
	private Long userIdNumber;
	private String userName;

}
