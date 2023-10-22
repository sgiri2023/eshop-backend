package com.example.eshopbackend.eshopbackend.modelconverter;

import com.example.eshopbackend.eshopbackend.common.utills.InvoiceStateCode;
import com.example.eshopbackend.eshopbackend.datamodel.AuditResponse;
import com.example.eshopbackend.eshopbackend.entity.AuditTrailEntity;
import com.example.eshopbackend.eshopbackend.entity.InvoiceEntity;

public class AuditTrailModelConverter {

    public static AuditTrailEntity requestToEntity(InvoiceEntity invoiceEntity, InvoiceStateCode stateCode){

        AuditTrailEntity invoiceAuditTrailEntity = new AuditTrailEntity();
        invoiceAuditTrailEntity.setInvoiceState(stateCode);
        invoiceAuditTrailEntity.setInvoiceEntity(invoiceEntity);
        return invoiceAuditTrailEntity;
    }

    public static AuditResponse entityToResponse(AuditTrailEntity auditTrailEntity){
        AuditResponse auditResponse = new AuditResponse();
        auditResponse.setId(auditTrailEntity.getId());
        auditResponse.setInvoiceState(auditTrailEntity.getInvoiceState().getValue());

        return auditResponse;
    }
}
