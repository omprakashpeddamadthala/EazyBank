package com.bytes.accounts.mapper;


import com.bytes.accounts.dto.AccountsDto;
import com.bytes.accounts.entity.Accounts;

public class AccountsMapper {

    public static Accounts mapToAccount(AccountsDto accountDto){
        return Accounts.builder()
                .accountNumber(accountDto.getAccountNumber())
                .accountType(accountDto.getAccountType())
                .branchAddress(accountDto.getBranchAddress())
                .build();
    }

    public static AccountsDto mapToAccountDto(Accounts Accounts){
        return AccountsDto.builder()
                .accountNumber(Accounts.getAccountNumber())
                .accountType(Accounts.getAccountType())
                .branchAddress(Accounts.getBranchAddress())
                .build();
    }
}