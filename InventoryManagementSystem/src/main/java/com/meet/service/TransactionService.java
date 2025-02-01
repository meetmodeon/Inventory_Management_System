package com.meet.service;

import com.meet.dto.CategoryDto;
import com.meet.dto.Response;
import com.meet.dto.TransactionRequest;
import com.meet.enums.TransactionStatus;

public interface TransactionService {
    Response restockInventory(TransactionRequest transactionRequest);
    Response sell(TransactionRequest transactionRequest);
    Response returnToSupplier(TransactionRequest transactionRequest);
    Response getAllTransactions(int page,int size,String searchText);
    Response getTransactionById(Long id);
    Response getAllTransactionByMothAndYear(int month,int year);
    Response updateTransactionStatus(Long transactionId, TransactionStatus transactionStatus);
}
