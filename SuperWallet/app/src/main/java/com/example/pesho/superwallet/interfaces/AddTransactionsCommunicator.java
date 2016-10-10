package com.example.pesho.superwallet.interfaces;

import android.support.v4.app.Fragment;

import com.example.pesho.superwallet.model.Account;
import com.example.pesho.superwallet.model.Category;

/**
 * Created by Pesho on 10/9/2016.
 */

public interface AddTransactionsCommunicator {
    String getTransactionType();
    void registerFragment(Fragment fragment);
    void setCategory(Category category);
    void setAccountFrom(Account account);
    void setAccountTo(Account account);
    void setAmount(String amount);
    double getAmount();
    void setDescription(String description);
}
