package com.example.demo.ticket.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.ticket.model.entity.SeatCategories;


@Repository
public interface SeatCategoriesRepository extends JpaRepository<SeatCategories, Long> {
	
	// 查詢該活動設定之座位等級
	@Query("select s from SeatCategories s where s.eventId = :eventId")
	List<SeatCategories> getSeatCategories(Integer eventId);
	
	/*
	// 新增
	void addSeatCategories(List<SeatCategories> seatCategories);
	
	用saveBatch()
	*/
	
	// 修改
	@Transactional
	@Modifying
	@Query("update SeatCategories sc set sc.categoryName = :categoryName, sc.seatPrice = :seatPrice, sc.numSeats = :numSeats where sc.seatCategoryId = :seatCategoryId")
	void updateSeatCategory(Integer seatCategoryId, String categoryName, Integer seatPrice, Integer numSeats);
	
	/*
	// 刪除
	void deleteSeatCategories(Integer seatCategoryId);
	
	用deleteById()
	*/
}
