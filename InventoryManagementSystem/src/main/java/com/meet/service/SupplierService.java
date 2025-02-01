package com.meet.service;

import com.meet.dto.Response;
import com.meet.dto.SupplierDto;

public interface SupplierService {
    Response addSupplier(SupplierDto supplierDto);
    Response getAllSuppliers();
    Response getSupplierById(Long id);
    Response updateSupplier(Long id, SupplierDto supplierDto);
    Response deleteSupplier(Long id);
}
