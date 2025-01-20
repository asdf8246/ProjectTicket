package com.example.demo.ticket.service.impl;


import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.ticket.model.dto.OrderDto;
import com.example.demo.ticket.model.entity.Seats;
import com.example.demo.ticket.repository.SeatsRepository;
import com.example.demo.ticket.service.SeatsService;
import com.example.demo.ticket.socket.SeatDataSocketHandler;


@Service
public class SeatsServiceImpl implements SeatsService{
	
	@Autowired
	private SeatsRepository seatsRepository ;
	
	@Autowired
	private SeatDataSocketHandler seatDataSocketHandler;
	
	@Transactional
	@Override
	public List<Seats> buySeats(String eventId, String[] seatCategoryIds, String[] numSeatss){
		return IntStream.range(0, seatCategoryIds.length)
                .filter(i -> Integer.parseInt(numSeatss[i]) > 0)  // 篩選掉零座位
                .mapToObj(i -> {
                    Integer seatCategoryId = Integer.parseInt(seatCategoryIds[i]);
                    Integer numSeats = Integer.parseInt(numSeatss[i]);
                    
                    // 查詢可用座位並更新座位為 reserved
                    PageRequest pageable = PageRequest.of(0, numSeats);
                    List<Seats> availableSeats = seatsRepository.findAvailableSeats(Integer.parseInt(eventId), seatCategoryId, pageable);
                    seatsRepository.updateSeatStatusToReserved(Integer.parseInt(eventId), seatCategoryId, numSeats);

                    // 返回查詢到的座位列表
                    return availableSeats;
                })
                .flatMap(List::stream)  // 將 List<Seats> 展開為單個流
                .collect(Collectors.toList());  // 收集為最終的 List<Seats>
	}
	
	@Transactional
	@Override
	public void updateSeatsStatus(List<OrderDto> orderSeatsDto, String seatStatus , String eventId) {
		List<Integer> seatIds = orderSeatsDto.stream()
									.map(OrderDto::getSeatId)
									.collect(Collectors.toList());
		seatsRepository.updateSeatsStatus(seatIds, seatStatus);
		
		// 发送 WebSocket 更新
		seatDataSocketHandler.sendUpdatedData(eventId);
	}
	
}
