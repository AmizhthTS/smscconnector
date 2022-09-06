package com.smsapi.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smsapi.model.SMSRequest;
import com.smsapi.model.SMSResponse;
import com.smsapi.service.SMSService;

@RestController
@RequestMapping(value = "/sms")
public class SMSController {
	
	
	@Autowired
	SMSService smsservice;
	
	@PostMapping("/send")
	public ResponseEntity<?> changepassword(@RequestBody SMSRequest smsrequest) throws Exception {

		List<SMSResponse> authOTPVerifyResponseModel = smsservice.send(smsrequest);
		return ResponseEntity.ok().body(authOTPVerifyResponseModel);
	
	}

}
