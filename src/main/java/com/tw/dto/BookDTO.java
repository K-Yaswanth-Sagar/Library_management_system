package com.tw.dto;

import lombok.Data;

@Data
public class BookDTO {
	
	private String title;
	private String auther;
	private String isbn;
	private Integer quantity;
	private boolean isAvaliable;

}
