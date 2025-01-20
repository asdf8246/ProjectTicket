package com.example.demo.ticket.repository;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.ticket.model.entity.Seats;
import com.example.demo.ticket.repository.custom.SeatsRepositoryCustom;

@Repository
public interface SeatsRepository extends JpaRepository<Seats, Long>, SeatsRepositoryCustom {
	
	//購買座位
	@Query("SELECT s FROM Seats s WHERE s.eventId = :eventId AND s.seatCategoryId = :seatCategoryId AND s.seatStatus = 'available' ORDER BY s.seatNumber")
    List<Seats> findAvailableSeats(@Param("eventId") Integer eventId, @Param("seatCategoryId") Integer seatCategoryId, PageRequest pageable);

	
	//同時更新多筆座位的指定狀態
	@Transactional
	@Modifying
    @Query("UPDATE Seats s SET s.seatStatus = :seatStatus WHERE s.seatId IN :seatIds")
    void updateSeatsStatus(List<Integer> seatIds, String seatStatus);
	
	@Query("SELECT COUNT(s) FROM Seats s WHERE s.seatStatus = 'sold' AND s.seatCategoryId = :seatCategoryId")
	Integer getSoldSeatsNums(Integer seatCategoryId);
}
