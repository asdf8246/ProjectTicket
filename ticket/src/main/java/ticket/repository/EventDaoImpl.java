package ticket.repository;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import ticket.model.entity.Events;

public class EventDaoImpl implements EventDao {

	@Override
	public List<Events> findAllEvents() {
		List<Events> events = new ArrayList<>();
		String sql = "select * from events ORDER BY sell_date";
		try(Connection connection = DatabaseConnectionPool.getConnection() ) {
			try (Statement stmt = connection.createStatement();
					ResultSet rs = stmt.executeQuery(sql)) {
				//逐筆尋訪
				while (rs.next()) {
					//建立 event 物件並將資料配置進去
					Events event = new Events();
					event.setEventId(rs.getInt("event_id"));
					event.setEventName(rs.getString("event_name"));
					event.setEventDate(rs.getString("event_date"));
					event.setSellDate(rs.getString("sell_date"));
					event.setVenue(rs.getString("venue"));
					event.setAddress(rs.getString("address"));
					event.setDescription(rs.getString("description"));
					event.setEventStatus(rs.getString("event_status"));
					// 將 event 物件放到 events 集合中保存
					events.add(event);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return events; // 回傳有 event 物件的集合
	}

	@Override
	public Events getEvent(Integer eventId) {
		String sql = "select * from events where event_id=?";
		try(Connection conn = DatabaseConnectionPool.getConnection()){
			try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
				pstmt.setInt(1, eventId);
				
				try(ResultSet rs = pstmt.executeQuery()){
					if (rs.next()) {
						Events event = new Events();
						event.setEventId(rs.getInt("event_id"));
						event.setEventName(rs.getString("event_name"));
						event.setEventDate(rs.getString("event_date"));
						event.setSellDate(rs.getString("sell_date"));
						event.setVenue(rs.getString("venue"));
						event.setAddress(rs.getString("address"));
						event.setDescription(rs.getString("description"));
						event.setEventStatus(rs.getString("event_status"));
						event.setEventImage(rs.getBlob("event_image").getBinaryStream());
						return event;
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Integer addEvent(Events event) {
		String sql = "insert into events(event_name, event_date, sell_date, venue, address, description , event_image, event_status) value(?, ?, ?, ?, ?, ?, ? ,?)";
		try(Connection conn = DatabaseConnectionPool.getConnection()){
			try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
				pstmt.setString(1, event.getEventName());
				pstmt.setString(2, event.getEventDate());
				pstmt.setString(3, event.getSellDate());
				pstmt.setString(4, event.getVenue());
				pstmt.setString(5, event.getAddress());
				pstmt.setString(6, event.getDescription());
				pstmt.setBlob(7, event.getEventImage());
				pstmt.setString(8, event.getEventStatus());
				
				int rowcount = pstmt.executeUpdate();
				if (rowcount != 1) {
					throw new RuntimeException("新增失敗!");
				}
				
				try(ResultSet rs = pstmt.getGeneratedKeys()){
					if (rs.next()) {
						Integer eventId = rs.getInt(1);
						return eventId;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void updateEventName(Integer eventId, String eventName) {
		String sql = "update events set event_name = ? where event_id = ?";
		try(Connection conn = DatabaseConnectionPool.getConnection()){
			try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
				pstmt.setString(1, eventName);
				pstmt.setInt(2, eventId);
				
				int rowcount = pstmt.executeUpdate();
				if (rowcount != 1) {
					throw new RuntimeException("修改失敗 Id:" + eventId + "name:" + eventName);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void updateEventDate(Integer eventId, String eventDate) {
		String sql = "update events set event_date = ? where event_id = ?";
		try(Connection conn = DatabaseConnectionPool.getConnection()){
			try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
				pstmt.setString(1, eventDate);
				pstmt.setInt(2, eventId);
				
				int rowcount = pstmt.executeUpdate();
				if (rowcount != 1) {
					throw new RuntimeException("修改失敗 Id:" + eventId + "name:" + eventDate);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void updateSellDate(Integer eventId, String sellDate) {
		String sql = "update events set sell_date = ? where event_id = ?";
		try(Connection conn = DatabaseConnectionPool.getConnection()){
			try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
				pstmt.setString(1, sellDate);
				pstmt.setInt(2, eventId);
				
				int rowcount = pstmt.executeUpdate();
				if (rowcount != 1) {
					throw new RuntimeException("修改失敗 Id:" + eventId + "name:" + sellDate);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void updateVenue(Integer eventId, String venue) {
		String sql = "update events set venue = ? where event_id = ?";
		try(Connection conn = DatabaseConnectionPool.getConnection()){
			try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
				pstmt.setString(1, venue);
				pstmt.setInt(2, eventId);
				
				int rowcount = pstmt.executeUpdate();
				if (rowcount != 1) {
					throw new RuntimeException("修改失敗 Id:" + eventId + "name:" + venue);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void updateAddress(Integer eventId, String address) {
		String sql = "update events set address = ? where event_id = ?";
		try(Connection conn = DatabaseConnectionPool.getConnection()){
			try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
				pstmt.setString(1, address);
				pstmt.setInt(2, eventId);
				
				int rowcount = pstmt.executeUpdate();
				if (rowcount != 1) {
					throw new RuntimeException("修改失敗 Id:" + eventId + "name:" + address);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void updateDescription(Integer eventId, String description) {
		String sql = "update events set description = ? where event_id = ?";
		try(Connection conn = DatabaseConnectionPool.getConnection()){
			try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
				pstmt.setString(1, description);
				pstmt.setInt(2, eventId);
				
				int rowcount = pstmt.executeUpdate();
				if (rowcount != 1) {
					throw new RuntimeException("修改失敗 Id:" + eventId + "name:" + description);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void updateEventImage(Integer eventId, InputStream eventImage) {
		String sql = "update events set event_image = ? where event_id = ?";
		try(Connection conn = DatabaseConnectionPool.getConnection()){
			try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
				pstmt.setBlob(1, eventImage);
				pstmt.setInt(2, eventId);
				
				int rowcount = pstmt.executeUpdate();
				if (rowcount != 1) {
					throw new RuntimeException("修改失敗 Id:" + eventId + "name:" + eventImage);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void deleteEvent(Integer eventId) {
		String sql = "delete from events where event_id = ?";
		try(Connection conn = DatabaseConnectionPool.getConnection()){
			try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
				
				pstmt.setInt(1, eventId);
				
				int rowcount = pstmt.executeUpdate();
				if (rowcount != 1) {
					throw new RuntimeException("刪除失敗 Id:" + eventId);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<Events> getSearchEvents(String search) {
		List<Events> events = new ArrayList<>();
		String sql = "select * from events where event_name like ? ";
		try(Connection connection = DatabaseConnectionPool.getConnection()) {
			try(PreparedStatement pstmt = connection.prepareStatement(sql)){
				pstmt.setString(1, "%" + search + "%");
				try (ResultSet rs = pstmt.executeQuery()) {
					//逐筆尋訪
					while (rs.next()) {
						//建立 event 物件並將資料配置進去
						Events event = new Events();
						event.setEventId(rs.getInt("event_id"));
						event.setEventName(rs.getString("event_name"));
						event.setEventDate(rs.getString("event_date"));
						event.setSellDate(rs.getString("sell_date"));
						event.setVenue(rs.getString("venue"));
						event.setAddress(rs.getString("address"));
						event.setDescription(rs.getString("description"));
						event.setEventStatus(rs.getString("event_status"));
						// 將 event 物件放到 events 集合中保存
						events.add(event);
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return events; // 回傳有 event 物件的集合
	}

}
