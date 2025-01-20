package com.example.demo.ticket.service.impl;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.ticket.model.dto.SeatCategoriesDto;
import com.example.demo.ticket.model.entity.SeatCategories;
import com.example.demo.ticket.repository.SeatCategoriesRepository;
import com.example.demo.ticket.repository.SeatsRepository;
import com.example.demo.ticket.service.SeatCategoriesService;


@Service
public class SeatCategoriesServiceImpl implements SeatCategoriesService{
	
	@Autowired
	private SeatCategoriesRepository seatCategoriesRepository;
	
	@Autowired
	private SeatsRepository seatsRepository;
	
	@Autowired
	private ModelMapper modelMapper;
	
	
	//private SeatsDao seatsDao = new SeatsDaoImpl();
	
	//新增
	@Override
	public void appendSeatCategory(Integer eventId, String[] categoryNames, String[] seatPrices, String[] numSeatss) {
		List<SeatCategories> seatCategories = IntStream.range(0, categoryNames.length)
		        .mapToObj(i -> {SeatCategories seatCategory = new SeatCategories();
					            seatCategory.setEventId(eventId);
					            seatCategory.setCategoryName(categoryNames[i]);
					            seatCategory.setSeatPrice(Integer.parseInt(seatPrices[i]));
					            seatCategory.setNumSeats(Integer.parseInt(numSeatss[i]));
					            return seatCategory;
					        }).collect(Collectors.toList());

		seatCategoriesRepository.saveAll(seatCategories);
	}
		
	// 刪除
	@Override
	public void deleteSeatCategorie(String seatCategoryId) {
		seatCategoriesRepository.deleteById(Long.parseLong(seatCategoryId));
	}
		
	// 取得該活動所有
	@Override
	public List<SeatCategoriesDto> getSeatCategories(String eventId) {
		
		return seatCategoriesRepository.getSeatCategories(Integer.parseInt(eventId)).stream()
						.map(sc -> modelMapper.map(sc, SeatCategoriesDto.class))
						.collect(Collectors.toList());
		
	}
	
	//修改
	@Override
	public void updateSeatCategory(String eventId, String[] seatCategoryIds, String[] categoryNames, String[] seatPrices, String[] numSeatss) {
		Integer eventIdInt = Integer.parseInt(eventId);  // 将eventId转为整数以减少重复转换
	    
	    List<SeatCategories> seatCategories = IntStream.range(0, seatCategoryIds.length)
				        .mapToObj(i -> {Integer seatCategoryId = Integer.parseInt(seatCategoryIds[i]);
							            String categoryName = categoryNames[i];
							            Integer seatPrice = Integer.parseInt(seatPrices[i]);
							            Integer numSeats = Integer.parseInt(numSeatss[i]);
						
							            if (seatCategoryId != 0) {
							                seatCategoriesRepository.updateSeatCategory(seatCategoryId, categoryName, seatPrice, numSeats);
							                return null;  // 对于更新的记录返回null，之后会过滤掉
							            }
						
							            // 对于新添加的座位分类，创建新的 SeatCategories 对象
							            SeatCategories seatCategory = new SeatCategories();
							            seatCategory.setEventId(eventIdInt);
							            seatCategory.setCategoryName(categoryName);
							            seatCategory.setSeatPrice(seatPrice);
							            seatCategory.setNumSeats(numSeats);
							            return seatCategory;
							        })
							        .filter(Objects::nonNull)  // 过滤掉为 null 的更新项
							        .collect(Collectors.toList());  // 将其转换为列表
	    
		seatCategoriesRepository.saveAll(seatCategories);
	}
	
	@Override
	public List<SeatCategoriesDto> getSeatCategoriesChart(String eventId){
		
		return seatCategoriesRepository.getSeatCategories(Integer.parseInt(eventId)).stream()
							.map( sc -> {
								SeatCategoriesDto seatCategoriesDto = modelMapper.map(sc, SeatCategoriesDto.class);
								
								Integer soldSeats = seatsRepository.getSoldSeatsNums(sc.getSeatCategoryId());
								
								seatCategoriesDto.setSoldSeats(soldSeats);
								
								return seatCategoriesDto;
							}).collect(Collectors.toList());
		
	}
}
