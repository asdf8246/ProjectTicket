package ticket.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import ticket.model.entity.Seats;
import ticket.socket.SeatDataSocket;

public class SeatsDaoImpl implements SeatsDao {

	@Override
	public List<Seats> buySeat(List<Seats> seats) {
		List<Seats> orderSeats = new ArrayList<Seats>();
		
		String updateSQL = """
							update seats set seat_status = 'reserved' 
							where event_id = ? and seat_category_id = ? and seat_status = 'available' 
							order by seat_number limit ?
							""".trim();
		
		String selectSQL = """
							select a.seat_id, a.event_id, a.seat_category_id, a.seat_number, a.seat_status, b.category_name
							from seats a inner join seat_categories b
							on a.event_id = b.event_id and a.seat_category_id = b.seat_category_id 
							where a.event_id = ? and a.seat_category_id = ? and a.seat_status = 'reserved'
							order by seat_number limit ?
							""".trim();
		
		try(Connection conn = DatabaseConnectionPool.getConnection()){
				try (PreparedStatement updateStmt = conn.prepareStatement(updateSQL);
					 PreparedStatement selectStmt = conn.prepareStatement(selectSQL)) {
					
					// 開始事務
				    conn.setAutoCommit(false);
				    
				    // 2. 更新座位狀態為 'reserved'
				    updateStmt.clearBatch();
				   for (Seats seat : seats) {
				    	updateStmt.setInt(1, seat.getEventId());
				    	updateStmt.setInt(2, seat.getSeatCategoryId());
				    	updateStmt.setInt(3, seat.getNumSeats());
				    	
				    	updateStmt.addBatch();
					}
				    updateStmt.executeBatch();
				    
				    // 3. 查詢更新後的資料
				    for (Seats seat : seats) {
					    selectStmt.setInt(1, seat.getEventId());
					    selectStmt.setInt(2, seat.getSeatCategoryId());
					    selectStmt.setInt(3, seat.getNumSeats());
						
						try (ResultSet rs = selectStmt.executeQuery()) {
							
							while(rs.next()) {
								Seats seat2 = new Seats();
								seat2.setSeatId(rs.getInt("seat_id"));
								seat2.setEventId(rs.getInt("event_id"));
								seat2.setSeatCategoryId(rs.getInt("seat_category_id"));
								seat2.setSeatNumber(rs.getInt("seat_number"));
								seat2.setCategoryName(rs.getString("category_name"));
								
								orderSeats.add(seat2);
							}
						}
				    }
				    
				    // 提交事務
				    conn.commit();
			} catch (SQLException e) {
				if (conn != null) {
					try {
						conn.rollback();  // 發生錯誤時回滾
					} catch (SQLException ex) {
						ex.printStackTrace();
					}
				}
				e.printStackTrace();
			} finally {
			    try {
					conn.setAutoCommit(true);// 恢复自动提交
				} catch (SQLException e) {
					e.printStackTrace();
				}  
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		return orderSeats;
	}

	@Override
	public void updateSeatsStatus(List<Integer> seatIds, String seatStatus , String eventId) {
		String sql = "update seats set seat_status = ? where seat_id = ?";
		try(Connection conn = DatabaseConnectionPool.getConnection()){
			try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
	
				pstmt.clearBatch();
				
				for (Integer seatId : seatIds) {
					pstmt.setString(1, seatStatus);
					pstmt.setInt(2, seatId);
					
					pstmt.addBatch();
				}
				
				pstmt.executeBatch();
				
				// 這裡進行 WebSocket 資料推送
	            SeatDataSocket.sendUpdatedData(eventId);  // 發送資料給所有連線的客戶端
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Integer getSoldSeatsNums(Integer seatCategoryId) {
		String sql = "SELECT COUNT(*) FROM seats WHERE seat_status = 'sold' and seat_category_id = ? ";
		try(Connection conn = DatabaseConnectionPool.getConnection()){
			try (PreparedStatement pstmt = conn.prepareStatement(sql)){
				pstmt.setInt(1, seatCategoryId);
				try(ResultSet rs = pstmt.executeQuery()){
					if (rs.next()) {
	                    Integer soldSeats = rs.getInt(1);  // 获取查询结果的第一列（计数值）
	                    return soldSeats;
	                }
				}
			}
		} catch (SQLException e) {
            e.printStackTrace();
		}
		 return null;
	}
	
}
