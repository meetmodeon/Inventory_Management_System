package com.meet.service.impl;

import com.meet.dto.ProductDto;
import com.meet.dto.Response;
import com.meet.entity.Category;
import com.meet.entity.Product;
import com.meet.exceptions.NotFoundException;
import com.meet.repository.CategoryRepository;
import com.meet.repository.ProductRepository;
import com.meet.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;
    private final CategoryRepository categoryRepository;
    private static final String IMAGE_DIRECTORY=System.getProperty("user.dir")+"/product-image/";
    private static final String IMAGE_DIRECTORY_FRONTEND="C:/full-stack/full feature e-commerce/ims-angular/public/products/";
    @Override
    public Response saveProduct(ProductDto productDto, MultipartFile imageFile) {
        Category category= categoryRepository.findById(productDto.getCategoryId())
                .orElseThrow(()->new NotFoundException("Category Not Found"));
        //map out product dto to product entity
        Product productToSave= Product.builder()
                .name(productDto.getName())
                .sku(productDto.getSku())
                .price(productDto.getPrice())
                .stockQuantity(productDto.getStockQuantity())
                .description(productDto.getDescription())
                .category(category)
                .build();
        if (imageFile !=null){
            String imagePath= saveImageToFrontendPulicFolder(imageFile);
            productToSave.setImageUrl(imagePath);
        }

        //save the product to our database
        productRepository.save(productToSave);
        return Response.builder()
                .status(200)
                .message("Product successfully saved")
                .build();
    }

    @Override
    public Response updateProduct(ProductDto productDto, MultipartFile imageFile) {
        Product existingProduct= productRepository.findById(productDto.getProductId())
                .orElseThrow(()-> new NotFoundException("Product Not Found"));
        //check if image is associated with the update request
        if(imageFile != null && !imageFile.isEmpty()){
            String imagePath=saveImageToFrontendPulicFolder(imageFile);
            existingProduct.setImageUrl(imagePath);
        }
        //check if category is to be changed for the product
        if(productDto.getCategoryId() != null  && productDto.getCategoryId()>0){
            Category category =categoryRepository.findById(productDto.getCategoryId())
                    .orElseThrow(()-> new NotFoundException("Category Not Found"));
            existingProduct.setCategory(category);


        }
        //check and update fields
        if(productDto.getName() !=null && !productDto.getName().isBlank()){
            existingProduct.setName(productDto.getName());
        }
        if (productDto.getSku() !=null && !productDto.getSku().isBlank()){
            existingProduct.setSku(productDto.getSku());
        }
        if (productDto.getDescription()!=null && !productDto.getDescription().isBlank()){
            existingProduct.setDescription(productDto.getDescription());
        }
        if (productDto.getPrice() != null && productDto.getPrice().compareTo(BigDecimal.ZERO)>= 0){
            existingProduct.setPrice(productDto.getPrice());
        }
        if ((productDto.getStockQuantity() != null) && productDto.getStockQuantity() >= 0){
            existingProduct.setStockQuantity(productDto.getStockQuantity());
        }
        //update the product
        productRepository.save(existingProduct);
        return Response.builder()
                .status(200)
                .message("Product successfully updated")
                .build();
    }

    @Override
    public Response getAllProducts() {
        List<Product> products= productRepository.findAll(Sort.by(Sort.Direction.DESC,"id"));
        List<ProductDto> productDtos=modelMapper.map(products,new TypeToken<List<ProductDto>>(){}.getType());

        return Response.builder()
                .status(200)
                .message("success")
                .products(productDtos)
                .build();
    }

    @Override
    public Response getProductById(Long id) {
       Product product= productRepository.findById(id)
               .orElseThrow(()-> new NotFoundException("Product Not Found"));
       return Response.builder()
               .status(200)
               .message("success")
               .product(modelMapper.map(product,ProductDto.class))
               .build();
    }

    @Override
    public Response deleteProduct(Long id) {
        productRepository.findById(id)
                .orElseThrow(()-> new NotFoundException("Product Not Found"));
        productRepository.deleteById(id);
        return Response.builder()
                .status(200)
                .message("Product Deleted successfully")
                .build();
    }

    private String saveImage(MultipartFile imageFile){
        //validate image check
        if (!imageFile.getContentType().startsWith("image/")){
            throw new IllegalArgumentException("Only image files are allowed");
        }
        //create the directory to store image if it doesn't exist
        File directory= new File(IMAGE_DIRECTORY);
        if (!directory.exists()){
            directory.mkdir();
            log.info("Directory was created");
        }
        //generate unique file name for the the image
        String uniqueFileName= UUID.randomUUID()+ "_"+imageFile.getOriginalFilename();
        //get the absolute path of the image
        String imagePath= IMAGE_DIRECTORY+ uniqueFileName;
        try {
            File destinationFile= new File(imagePath);
            imageFile.transferTo(destinationFile);
        }catch (Exception e){
            throw new IllegalArgumentException("Error occurend with saving image"+e.getMessage());
        }
        return imagePath;
    }

    private String saveImageToFrontendPulicFolder(MultipartFile imageFile){
        //validate image check
        if (!imageFile.getContentType().startsWith("image/")){
            throw new IllegalArgumentException("Only image files are allowed");
        }
        //create the directory to store image if it doesn't exist
        File directory= new File(IMAGE_DIRECTORY_FRONTEND);
        if (!directory.exists()){
            directory.mkdir();
            log.info("Directory was created");
        }
        //generate unique file name for the the image
        String uniqueFileName= UUID.randomUUID()+ "_"+imageFile.getOriginalFilename();
        //get the absolute path of the image
        String imagePath= IMAGE_DIRECTORY_FRONTEND+ uniqueFileName;
        try {
            File destinationFile= new File(imagePath);
            imageFile.transferTo(destinationFile);
        }catch (Exception e){
            throw new IllegalArgumentException("Error occurend with saving image"+e.getMessage());
        }
        return "products/"+uniqueFileName;
    }
}
