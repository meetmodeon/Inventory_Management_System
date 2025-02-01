package com.meet.service.impl;

import com.meet.dto.Response;
import com.meet.dto.TransactionDto;
import com.meet.dto.TransactionRequest;
import com.meet.entity.Product;
import com.meet.entity.Supplier;
import com.meet.entity.Transaction;
import com.meet.entity.User;
import com.meet.enums.TransactionStatus;
import com.meet.enums.TransactionType;
import com.meet.exceptions.NameValueRequiredException;
import com.meet.exceptions.NotFoundException;
import com.meet.repository.ProductRepository;
import com.meet.repository.SupplierRepository;
import com.meet.repository.TransactionRepository;
import com.meet.service.TransactionService;
import com.meet.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;
    private final ModelMapper modelMapper;
    private final SupplierRepository supplierRepository;
    private final UserService userService;
    private final ProductRepository productRepository;

    @Override
    public Response restockInventory(TransactionRequest transactionRequest) {
        Long productId= transactionRequest.getProductId();
        Long supplierId= transactionRequest.getSupplierId();
        Integer quantity= transactionRequest.getQuantity();

        if (quantity == null) throw  new NameValueRequiredException("Supplier id is Required");

        Product product= productRepository.findById(productId)
                .orElseThrow(()-> new NotFoundException("Product Not Found"));

        Supplier supplier= supplierRepository.findById(supplierId)
                .orElseThrow(()-> new NotFoundException("Supplier Not Found"));

        User user= userService.getCurrentLoggedInUser();

        //update the stock quantity adn re-save
        product.setStockQuantity(product.getStockQuantity()+quantity);
        productRepository.save(product);

        Transaction transaction= Transaction.builder()
                .transactionType(TransactionType.PURCHASE)
                .status(TransactionStatus.COMPLETED)
                .product(product)
                .user(user)
                .supplier(supplier)
                .totalProducts(quantity)
                .totalPrice(product.getPrice().multiply(BigDecimal.valueOf(quantity)))
                .description(transactionRequest.getDescription())
                .build();

        transactionRepository.save(transaction);
        return Response.builder()
                .status(200)
                .message("Transaction Made Successfully")
                .build();
    }

    @Override
    public Response sell(TransactionRequest transactionRequest) {
        Long productId= transactionRequest.getProductId();
        Integer quantity= transactionRequest.getQuantity();

        Product product= productRepository.findById(productId)
                .orElseThrow(()-> new NotFoundException("Product Not Found"));

        User user= userService.getCurrentLoggedInUser();

        //update the stock quantity and sell
        product.setStockQuantity(product.getStockQuantity()-quantity);
        quantity=product.getStockQuantity();
        productRepository.save(product);

        Transaction transaction= Transaction.builder()
                .transactionType(TransactionType.SALE)
                .status(TransactionStatus.COMPLETED)
                .product(product)
                .user(user)
                .totalProducts(quantity)
                .totalPrice(product.getPrice().multiply(BigDecimal.valueOf(quantity)))
                .description(transactionRequest.getDescription())
                .build();

        transactionRepository.save(transaction);

        return Response.builder()
                .status(200)
                .message("Transaction sold Successfully")
                .build();
    }

    @Override
    public Response returnToSupplier(TransactionRequest transactionRequest) {
        Long productId= transactionRequest.getProductId();
        Long supplierId= transactionRequest.getSupplierId();
        Integer quantity= transactionRequest.getQuantity();

        if(supplierId ==null) throw new NameValueRequiredException("Supplier Id is Required");

        Product product= productRepository.findById(productId)
                .orElseThrow(()-> new NotFoundException("Product Not Found"));

        Supplier supplier = supplierRepository.findById(supplierId)
                .orElseThrow(()-> new NotFoundException("Supplier Not Found"));
        User user= userService.getCurrentLoggedInUser();

        product.setStockQuantity(product.getStockQuantity()+quantity);
        quantity=product.getStockQuantity();
        productRepository.save(product);

        Transaction transaction= Transaction.builder()
                .transactionType(TransactionType.RETURN_TO_SUPPLIER)
                .status(TransactionStatus.PROCESSING)
                .product(product)
                .user(user)
                .supplier(supplier)
                .totalProducts(quantity)
                .totalPrice(BigDecimal.ZERO)
                .description(transactionRequest.getDescription())
                .build();
        transactionRepository.save(transaction);

        return Response.builder()
                .status(200)
                .message("Transaction Returned Successfully Initialized")
                .build();
    }

    @Override
    public Response getAllTransactions(int page, int size, String searchText) {
        Pageable pageable= PageRequest.of(page,size, Sort.by(Sort.Direction.DESC,"id"));
        Page<Transaction> transactionPage= transactionRepository.searchTransactions(searchText,pageable);
        List<TransactionDto> transactionDto=modelMapper.map(transactionPage.getContent(),new TypeToken<List<TransactionDto>>(){}.getType());

        transactionDto.forEach(transactionDto1 -> {
            transactionDto1.setUser(null);
            transactionDto1.setProduct(null);
            transactionDto1.setSupplier(null);
        });

        return Response.builder()
                .status(200)
                .message("success")
                .transactions(transactionDto)
                .build();
    }

    @Override
    public Response getTransactionById(Long id) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(()-> new NotFoundException("Transaction Not Found"));

        TransactionDto transactionDto= modelMapper.map(transaction,TransactionDto.class);
        transactionDto.getUser().setTransactions(null);

        return Response.builder()
                .status(200)
                .message("success")
                .transaction(transactionDto)
                .build();
    }

    @Override
    public Response getAllTransactionByMothAndYear(int month, int year) {
       List<Transaction> transactions=transactionRepository.findAllByMonthAndYear(month,year);

       List<TransactionDto> transactionDtos= modelMapper.map(transactions,new TypeToken<List<TransactionDto>>(){}.getType());

       transactionDtos.forEach(transactionDtoItems->{
           transactionDtoItems.setUser(null);
           transactionDtoItems.setProduct(null);
           transactionDtoItems.setSupplier(null);
       });

       return Response.builder()
               .status(200)
               .message("success")
               .transactions(transactionDtos)
               .build();
    }

    @Override
    public Response updateTransactionStatus(Long transactionId, TransactionStatus transactionStatus) {
        Transaction existingTransaction = transactionRepository.findById(transactionId)
                .orElseThrow(()-> new NotFoundException("Transaction Not Found"));

        existingTransaction.setStatus(transactionStatus);
        existingTransaction.setUpdatedAt(LocalDateTime.now());

        transactionRepository.save(existingTransaction);

        return Response.builder()
                .status(200)
                .message("Transaction Status Successfully Updated")
                .build();

    }
}
