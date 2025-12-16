package com.ciberaccion.chisme_chat;

import org.springframework.boot.SpringApplication;

public class TestChismeChatApplication {

	public static void main(String[] args) {
		SpringApplication.from(ChismeChatApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
