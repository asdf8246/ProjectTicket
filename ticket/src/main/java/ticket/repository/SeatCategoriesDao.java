package ticket.repository;

import java.util.List;

import ticket.model.entity.SeatCategories;

public interface SeatCategoriesDao {
	
	// 查詢該活動設定之座位等級
	List<SeatCategories> getSeatCategories(Integer eventId);
	
	// 新增
	void addSeatCategories(List<SeatCategories> seatCategories);
	
	// 修改
	void updateSeatCategory(Integer seatCategoryId, String categoryName, Integer seatPrice, Integer numSeats);
		
	// 刪除
	void deleteSeatCategories(Integer seatCategoryId);
	
}
