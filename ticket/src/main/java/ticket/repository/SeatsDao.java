package ticket.repository;

import java.util.List;

import ticket.model.entity.Seats;

public interface SeatsDao {
	
	//購買座位
	public List<Seats> buySeat(List<Seats> seats);
	
	//同時更新多筆座位的指定狀態
	public void updateSeatsStatus(List<Integer> seatIds, String seatStatus , String eventId);
	
	public Integer getSoldSeatsNums(Integer seatCategoryId);
}
