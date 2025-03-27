package com.tw.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tw.entity.IssueRecord;

public interface IssueRecordRepo extends JpaRepository<IssueRecord, Long> {

}
