package com.jme.spatch.backend;

import com.jme.spatch.backend.model.user.service.UserRepository;
import com.jme.spatch.backend.model.wallet.service.WalletRepository;
import com.jme.spatch.backend.model.wallet.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class Application {

	@Autowired
	private  UserRepository userRepository;
	@Autowired
	private  WalletRepository walletRepository;

	@Autowired
	private WalletService walletService;






	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}




}
