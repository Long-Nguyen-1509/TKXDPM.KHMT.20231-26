package com.example.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeliveryForm {
    private String name;
    private String phone;
    private String province;
    private String address;
    private String email;
    private boolean rush;
    private String deliveryInstruction;
}
