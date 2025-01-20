package com.example.demo.ticket.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.ticket.model.entity.Events;
import com.example.demo.ticket.repository.custom.EventRepositoryCustom;

@Repository
public interface EventRepository extends JpaRepository<Events, Long>, EventRepositoryCustom{
	
	
	// 多筆:查詢所有
	@Query("SELECT e FROM Events e ORDER BY e.sellDate")
	List<Events> findAllEvents();

	
	/*
	// 查詢該筆
	Events getEvent(Integer eventId);
	
	用findById()
	*/
	
	/*
	// 新增
	Integer addEvent(Events events);
	
	用save()
	*/
	
	// 更新活動名稱
    @Modifying
    @Transactional
    @Query("UPDATE Events e SET e.eventName = :eventName WHERE e.eventId = :eventId")
    void updateEventName(Integer eventId, String eventName);
    
    // 更新活動日期
    @Modifying
    @Transactional
    @Query("UPDATE Events e SET e.eventDate = :eventDate WHERE e.eventId = :eventId")
    void updateEventDate(Integer eventId, Date eventDate);
    
    // 更新開賣日期
    @Modifying
    @Transactional
    @Query("UPDATE Events e SET e.sellDate = :sellDate WHERE e.eventId = :eventId")
    void updateSellDate(Integer eventId, Date sellDate);
    
    // 更新活動地點
    @Modifying
    @Transactional
    @Query("UPDATE Events e SET e.venue = :venue WHERE e.eventId = :eventId")
    void updateVenue(Integer eventId, String venue);
    
    // 更新活動地址
    @Modifying
    @Transactional
    @Query("UPDATE Events e SET e.address = :address WHERE e.eventId = :eventId")
    void updateAddress(Integer eventId, String address);
    
    // 更新活動簡介
    @Modifying
    @Transactional
    @Query("UPDATE Events e SET e.description = :description WHERE e.eventId = :eventId")
    void updateDescription(Integer eventId, String description);
    
    // 更新活動圖片
    @Modifying
    @Transactional
    @Query("UPDATE Events e SET e.eventImage = :eventImage WHERE e.eventId = :eventId")
    void updateEventImage(Integer eventId, byte[] eventImage);
	
	/*
	// 刪除
	void deleteEvent(Integer eventId);
	
	用deleteById()
	*/
	
}
