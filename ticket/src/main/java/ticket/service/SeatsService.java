package ticket.service;

import java.util.ArrayList;
import java.util.List;

import ticket.model.dto.OrderDto;
import ticket.model.entity.Order;
import ticket.model.entity.Seats;
import ticket.repository.SeatsDao;
import ticket.repository.SeatsDaoImpl;

public class SeatsService {
	
	private SeatsDao seatsDao = new SeatsDaoImpl();
	
	public List<Seats> buySeats(String eventId, String[] seatCategoryIds, String[] numSeatss){
		List<Seats> seats = new ArrayList<Seats>();
		
		for(int i=0;i<seatCategoryIds.length;i++) {
			Integer seatCategoryId = Integer.parseInt(seatCategoryIds[i]);
			Integer numSeats = Integer.parseInt(numSeatss[i]);
			
			if (numSeats==0) {
				continue;
			}
			
			Seats seat = new Seats();
			seat.setEventId(Integer.parseInt(eventId));
			seat.setSeatCategoryId(seatCategoryId);
			seat.setNumSeats(numSeats);
			
			seats.add(seat);
		}
		List<Seats> orderSeats = seatsDao.buySeat(seats);
		return orderSeats;
	}
	
	public void updateSeatsStatus(List<OrderDto> orderSeatsDto, String seatStatus , String eventId) {
		List<Integer> seatIds = new ArrayList<Integer>();
		for (OrderDto orderDto : orderSeatsDto) {
			seatIds.add(orderDto.getSeatId());
		}
		seatsDao.updateSeatsStatus(seatIds, seatStatus , eventId);
	}
	
}
