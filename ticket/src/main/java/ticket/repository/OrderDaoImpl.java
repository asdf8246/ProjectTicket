package ticket.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import ticket.model.entity.Order;

public class OrderDaoImpl implements OrderDao {

	@Override
	public List<Order> findAllOrders(Integer userId) {
		List<Order> orders = new ArrayList<Order>();
		String sql = "select * from orders where user_id = ?";
		try(Connection conn = DatabaseConnectionPool.getConnection()){
			try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
				pstmt.setInt(1, userId);
				try(ResultSet rs = pstmt.executeQuery()){
					while (rs.next()) {
						Order order = new Order();
						order.setOrderId(rs.getInt("order_id"));
						order.setUserId(rs.getInt("user_id"));
						order.setEventId(rs.getInt("event_id"));
						order.setEventName(rs.getString("event_name"));
						order.setOrderPrice(rs.getInt("order_price"));
						order.setOrderDate(rs.getString("order_date"));
						order.setOrderStatus(rs.getString("ticket_status"));
						
						orders.add(order);
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return orders;
	}
	
	@Override
	public Order getOrder(Integer orderId) {
		String sql = "select * from orders where order_id = ?";
		try(Connection conn = DatabaseConnectionPool.getConnection()){
			try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
				pstmt.setInt(1, orderId);
				try(ResultSet rs = pstmt.executeQuery()){
					if (rs.next()) {
						Order order = new Order();
						order.setOrderId(rs.getInt("order_id"));
						order.setUserId(rs.getInt("user_id"));
						order.setEventId(rs.getInt("event_id"));
						order.setEventName(rs.getString("event_name"));
						order.setOrderPrice(rs.getInt("order_price"));
						order.setOrderDate(rs.getString("order_date"));
						return order;
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public List<Order> getUserOrders(Integer userId){
		List<Order> userOrders = new ArrayList<Order>();
		String sql = "SELECT * FROM orders WHERE user_id = ? ORDER BY order_date DESC";
		try(Connection conn = DatabaseConnectionPool.getConnection()){
			try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
				pstmt.setInt(1, userId);
				try(ResultSet rs = pstmt.executeQuery()){
					while (rs.next()) {
						Order order = new Order();
						order.setOrderId(rs.getInt("order_id"));
						order.setUserId(rs.getInt("user_id"));
						order.setEventId(rs.getInt("event_id"));
						order.setEventName(rs.getString("event_name"));
						order.setOrderPrice(rs.getInt("order_price"));
						order.setOrderDate(rs.getString("order_date"));
						order.setOrderStatus(rs.getString("order_status"));
						
						userOrders.add(order);
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return userOrders;
	}

	@Override
	public List<Order> getOrderSeats(Integer orderId) {
		List<Order> orderSeats = new ArrayList<Order>();
		String sql = """
						SELECT os.order_id, os.seat_id, os.category_name, os.seat_number, sc.seat_price
						FROM  orders_seats os JOIN seats s ON os.seat_id = s.seat_id
						JOIN seat_categories sc ON s.seat_category_id = sc.seat_category_id
						WHERE os.order_id = ?;
						""".trim();
		
		try(Connection conn = DatabaseConnectionPool.getConnection()){
			try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
				pstmt.setInt(1, orderId);
				try(ResultSet rs = pstmt.executeQuery()){
					while (rs.next()) {
						Order order = new Order();
						order.setOrderId(rs.getInt("order_id"));
						order.setSeatId(rs.getInt("seat_id"));
						order.setCategoryName(rs.getString("category_name"));
						order.setSeatNumber(rs.getInt("seat_number"));
						order.setSeatPrice(rs.getInt("seat_price"));
						
						orderSeats.add(order);
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return orderSeats;
	}

	
	@Override
	public Integer addOrder(Order order) {
		String sql = "insert into orders(user_id, event_id, event_name, order_price, order_date) value(?, ?, ?, ?, ?)";
		try(Connection conn = DatabaseConnectionPool.getConnection()){
			try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
				pstmt.setInt(1, order.getUserId());
				pstmt.setInt(2, order.getEventId());
				pstmt.setString(3, order.getEventName());
				pstmt.setInt(4, order.getOrderPrice());
				pstmt.setString(5, order.getOrderDate());
				
				int rowcount = pstmt.executeUpdate();
				if (rowcount != 1) {
					throw new RuntimeException("新增失敗");
				}
				
				try(ResultSet rs = pstmt.getGeneratedKeys()){
					if (rs.next()) {
						Integer orderId = rs.getInt(1);
						return orderId;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void addOrderSeats(List<Order> orders) {
		String sql = "insert into orders_seats(order_id, seat_id, category_name, seat_number) value(?, ?, ?, ?)";
		try(Connection conn = DatabaseConnectionPool.getConnection()){
			try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
				pstmt.clearBatch();
				for (Order order: orders) {
					pstmt.setInt(1, order.getOrderId());
					pstmt.setInt(2, order.getSeatId());
					pstmt.setString(3, order.getCategoryName());
					pstmt.setInt(4, order.getSeatNumber());
					
					pstmt.addBatch();
				}		
				pstmt.executeBatch();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void updateOrderStatus(Integer orderId, String orderStatus) {
		String sql = "update orders set order_status = ? where order_id = ?";
		try(Connection conn = DatabaseConnectionPool.getConnection()){
			try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
				pstmt.setString(1, orderStatus);
				pstmt.setInt(2, orderId);
				
				pstmt.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void deleteOrder(Integer orderId) {
		String sql = "delete from orders where order_id = ?";
		try(Connection conn = DatabaseConnectionPool.getConnection()){
			try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
				
				pstmt.setInt(1, orderId);
				
				int rowcount = pstmt.executeUpdate();
				if (rowcount != 1) {
					throw new RuntimeException("刪除失敗 Id:" + orderId);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Order checkUserOrderStatus(Integer userId, Integer eventId) {
		String sql = "select order_id, order_status from orders where user_id = ? and event_id = ? and order_status != 'canceled'";
		try(Connection conn = DatabaseConnectionPool.getConnection()){
			try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
				pstmt.setInt(1, userId);
				pstmt.setInt(2, eventId);
				try(ResultSet rs = pstmt.executeQuery()){
					if (rs.next()) {
						Order order = new Order();
						order.setOrderId(rs.getInt("order_id"));
						order.setOrderStatus(rs.getString("order_status"));
						return order;
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	
	
}
