package com.meet.service.impl;

import com.meet.dto.Response;
import com.meet.dto.SupplierDto;
import com.meet.entity.Supplier;
import com.meet.exceptions.NotFoundException;
import com.meet.repository.SupplierRepository;
import com.meet.service.SupplierService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SupplierServiceImpl implements SupplierService {
    private final SupplierRepository supplierRepository;
    private final ModelMapper modelMapper;
    @Override
    public Response addSupplier(SupplierDto supplierDto) {
        Supplier supplierToSave= modelMapper.map(supplierDto,Supplier.class);
        supplierRepository.save(supplierToSave);

        return Response.builder()
                .status(200)
                .message("Supplier added Successfully")
                .build();
    }


    @Override
    public Response getAllSuppliers() {
        List<Supplier> suppliers = supplierRepository.findAll(Sort.by(Sort.Direction.DESC,"id"));
        List<SupplierDto> supplierDtos= modelMapper.map(suppliers,new TypeToken<List<SupplierDto>>(){}.getType());

        return Response.builder()
                .status(200)
                .message("Success")
                .suppliers(supplierDtos)
                .build();
    }

    @Override
    public Response getSupplierById(Long id) {
        Supplier supplier= supplierRepository.findById(id).orElseThrow(()-> new NotFoundException("Supplier Not Found"));
        SupplierDto supplierDto=modelMapper.map(supplier,SupplierDto.class);

        return Response.builder()
                .status(200)
                .message("Success")
                .supplier(supplierDto)
                .build();
    }

    @Override
    public Response updateSupplier(Long id, SupplierDto supplierDto) {
       Supplier existingSupplier= supplierRepository.findById(id)
               .orElseThrow(()-> new NotFoundException("Supplier Not Found"));

       if(supplierDto.getName() != null) existingSupplier.setName(supplierDto.getName());
       if(supplierDto.getAddress() != null) existingSupplier.setAddress(supplierDto.getAddress());
       supplierRepository.save(existingSupplier);

       return Response.builder()
               .status(200)
               .message("Supplier Successfully Updated")
               .build();
    }

    @Override
    public Response deleteSupplier(Long id) {
        supplierRepository.findById(id).orElseThrow(()-> new NotFoundException("Supplier Not Found"));

        supplierRepository.deleteById(id);

        return Response.builder()
                .status(200)
                .message("Supplier Successfully Deleted")
                .build();
    }
}
