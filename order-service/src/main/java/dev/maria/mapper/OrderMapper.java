package dev.maria.mapper;

import dev.maria.dto.CreateOrderRequest;
import dev.maria.dto.CreateOrderResponse;
import dev.maria.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface OrderMapper {
    @Mapping(source = "id", target = "orderId")
    CreateOrderResponse toResponse(Order order);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    Order toEntity(CreateOrderRequest request);
}