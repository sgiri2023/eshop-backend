package com.example.eshopbackend.eshopbackend.datamodel.Email;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailRequest {
    private String recipient;
    private String msgBody;
    private String subject;
    private String attachment;
}
