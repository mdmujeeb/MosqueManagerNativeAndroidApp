package org.mujeeb.mosquemanager.beans;

import java.util.HashMap;
import java.util.Map;

public abstract class Errors {

	protected static Map<Integer,String> errorMap = new HashMap<Integer,String>();
	
	static {
		errorMap.put(-1, "Something went wrong wile communicating with Server.\nPlease try after sometime.");
		errorMap.put(1, "Invalid User Id.");
		errorMap.put(2, "Invalid EMail id.");
		errorMap.put(3, "First Name cannot be left blank.");
		errorMap.put(4, "A User with specified Mobile number already exists.");
		errorMap.put(5, "Security PIN must be 6 digits numberic only.");
		errorMap.put(6, "Invalid System Property key.");
		errorMap.put(7, "Invalid Message template key.");
		errorMap.put(8, "Invalid EMail Hash.");
		errorMap.put(9, "Your Verification Token has Expired.");
		errorMap.put(10, "Invalid OTP.");
		errorMap.put(11, "Your OTP has Expired.");
		errorMap.put(12, "Invalid Password.");
		errorMap.put(13, "No such User in System.");
		errorMap.put(14, "Authentication Failed.");
		errorMap.put(15, "You are not Authorized to perform this operation.");
		errorMap.put(16, "Invalid Authentication Token.");
		errorMap.put(17, "Delivery date has to be specified.");
		errorMap.put(18, "Delivery date cannot be in past.");
		errorMap.put(19, "Invalid PIN Code.");
		errorMap.put(20, "No Address found.");
		errorMap.put(21, "No User found.");
		errorMap.put(22, "No Email for User Account.");
		errorMap.put(23, "User Mobile number is not verified.");
		errorMap.put(24, "Invalid Day of Week.");
		errorMap.put(25, "Invalid Fuel name.");
		errorMap.put(26, "Invalid Fuel Quantity.");
		errorMap.put(27, "Invalid Order Id.");
		errorMap.put(28, "Invalid Delivery Start time.");
		errorMap.put(29, "Invalid Delivery End time.");
		errorMap.put(30, "Invalid Delivery Person mobile number.");
		errorMap.put(31, "Order details do not Match.");
		errorMap.put(32, "Online payment amount cannot be greater than Order amount.");
		errorMap.put(33, "Invalid Online payment method.");
		errorMap.put(34, "Unable to connect to Payment Gateway.");
		errorMap.put(35, "Duplicate Order Id.");
		errorMap.put(36, "Invalid Address Id.");
		errorMap.put(37, "We have still not started Delivering in your Area.\nWe will inform you as soon as we start Delivery in your Area.");
		errorMap.put(38, "We are currently not accepting new Orders.\nPlease try Tomorrow.");
		errorMap.put(39, "Our Payment gateway is currently down, Please check back after some time.");
		errorMap.put(40, "We are currently not accepting Cash on Delivery payments.");
		errorMap.put(41, "Invalid Order status.");
		errorMap.put(42, "Invalid Refund Amount.");
		errorMap.put(43, "Invalid Link Id.");
		errorMap.put(44, "SMS Gateway is down.");
		errorMap.put(45, "EMail Gateway is down.");
		errorMap.put(46, "Status of a Draft Order cannot be changed.");
		errorMap.put(47, "No Payment Link found.");
		errorMap.put(48, "Ordered fuel Quantity is less than minimum allowed.");
		errorMap.put(49, "Ordered fuel Quantity is more than maximum allowed.");
		errorMap.put(50, "Order cannot be cancelled by this api call.");
		errorMap.put(51, "Order is not eligible for Cancellation.");
		errorMap.put(52, "Invalid Transaction Id.");
		errorMap.put(53, "Invalid Vehicle Id.");
		errorMap.put(54, "Order Type cannot be empty.");
		errorMap.put(55, "Vehicle Id is required for Full Tank Order.");
	}
	
	public static String getErrorDescription(int errorCode) {
		return errorMap.get(errorCode);
	}
}
