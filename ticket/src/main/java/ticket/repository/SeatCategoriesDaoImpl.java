package ticket.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import ticket.model.entity.SeatCategories;

public class SeatCategoriesDaoImpl implements SeatCategoriesDao{


	@Override
	public List<SeatCategories> getSeatCategories(Integer eventId) {
		List<SeatCategories> seatCategories = new ArrayList<>();
		String sql = "select seat_category_id, event_id, category_name, seat_price, num_seats from seat_categories where event_id=?";
		try(Connection conn = DatabaseConnectionPool.getConnection()){
			try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
				pstmt.setInt(1, eventId);
				
				try(ResultSet rs = pstmt.executeQuery()){
					while (rs.next()) {
						SeatCategories seatCategory = new SeatCategories();
						seatCategory.setSeatCategoryId(rs.getInt("seat_category_id"));
						seatCategory.setEventId(rs.getInt("event_id"));
						seatCategory.setCategoryName(rs.getString("category_name"));
						seatCategory.setSeatPrice(rs.getInt("seat_price"));
						seatCategory.setNumSeats(rs.getInt("num_seats"));
	
						seatCategories.add(seatCategory);
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return seatCategories;
	}

	@Override
	public void addSeatCategories(List<SeatCategories> seatCategories) {
		String sql = "insert into seat_categories(event_id, category_name, seat_price, num_seats) value(?, ?, ?, ?)";
		try(Connection conn = DatabaseConnectionPool.getConnection()){
			try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
				pstmt.clearBatch();
				for (SeatCategories seatCategory : seatCategories) {
					pstmt.setInt(1, seatCategory.getEventId());
					pstmt.setString(2, seatCategory.getCategoryName());
					pstmt.setInt(3, seatCategory.getSeatPrice());
					pstmt.setInt(4, seatCategory.getNumSeats());
					
					pstmt.addBatch();
				}
				
				pstmt.executeBatch();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	@Override
	public void deleteSeatCategories(Integer seatCategoryId) {
		String sql = "delete from seat_categories where seat_category_id = ?";
		try(Connection conn = DatabaseConnectionPool.getConnection()){
			try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
				
				pstmt.setInt(1, seatCategoryId);
				
				int rowcount = pstmt.executeUpdate();
				if (rowcount != 1) {
					throw new RuntimeException("刪除失敗 Id:" + seatCategoryId);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void updateSeatCategory(Integer seatCategoryId, String categoryName, Integer seatPrice, Integer numSeats) {
		String sql = "update seat_categories set category_name=?, seat_price=?, num_seats=? where seat_category_id=?";
		try(Connection conn = DatabaseConnectionPool.getConnection()){
			try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
				pstmt.setString(1, categoryName);
				pstmt.setInt(2, seatPrice);
				pstmt.setInt(3, numSeats);
				pstmt.setInt(4, seatCategoryId);
				
				int rowcount = pstmt.executeUpdate();
				if (rowcount != 1) {
					throw new RuntimeException("修改失敗 Id:" + seatCategoryId);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
}
