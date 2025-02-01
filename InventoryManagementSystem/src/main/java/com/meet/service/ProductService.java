package com.meet.service;

import com.meet.dto.ProductDto;
import com.meet.dto.Response;
import com.meet.dto.SupplierDto;
import org.springframework.web.multipart.MultipartFile;

public interface ProductService {
    Response saveProduct(ProductDto productDto, MultipartFile imageFile);
    Response updateProduct(ProductDto productDto, MultipartFile imageFile);
    Response getAllProducts();
    Response getProductById(Long id);
    Response deleteProduct(Long id);

}
