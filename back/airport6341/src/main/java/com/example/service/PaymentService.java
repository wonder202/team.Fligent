// package com.example.service;

// import static com.google.common.base.Preconditions.checkNotNull;

// import java.math.BigDecimal;
// import java.time.ZoneId;
// import java.util.Objects;
// import java.util.Optional;

// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.boot.context.properties.ConfigurationProperties;
// import org.springframework.stereotype.Service;
// import org.springframework.transaction.annotation.Transactional;

// import com.example.entity.Member;
// import com.example.entity.Payment;
// import com.example.entity.PaymentMethod;
// import com.example.entity.PaymentStatus;
// import com.example.repository.PaymentRepository;
// import com.siot.IamportRestHttpClientJava.IamportClient;
// import com.siot.IamportRestHttpClientJava.response.IamportResponse;

// import lombok.RequiredArgsConstructor;


// @ConfigurationProperties(prefix = "pgmodule")
// @RequiredArgsConstructor
// @Service
// public class PaymentService {
    
//     private final PaymentRepository paymentRepository;

//     @Value("${pgmodule.app-id}") private String apiKey;
//     @Value("${pgmodule.secret-key}") private String apiSecret;

//     @Transactional
//     public Payment requestPayment(Member buyer, String name, BigDecimal amount) {
//         Payment payment = new Payment();
//         payment.setBuyer(buyer);
//         payment.setOrderId(buyer.getNickname() + "_" + Objects.hash(buyer, name, amount, System.currentTimeMillis()));
//         payment.setName(name);
//         payment.setAmount(amount);
//         return paymentRepository.save(payment);
//     }

//     @Transactional
//     public Payment verifyPayment(Payment payment, Member buyer) throws Exception {
//         checkNotNull(payment, "payment must be provided.");
//         checkNotNull(buyer, "buyer must be provided.");

//         if (!payment.getBuyer().equals(buyer)) {
//             // throw new NotFoundException("Could not found payment for " + buyer.getNickname() + ".");
//             throw new Exception("Could not found payment for " + buyer.getNickname() + ".");
//         } 

//         IamportClient iamportClient = new IamportClient(apiKey, apiSecret);
//         try {
//             IamportResponse<com.siot.IamportRestHttpClientJava.response.Payment> paymentResponse = iamportClient.paymentByImpUid(payment.getReceiptId());
//             if (Objects.nonNull(paymentResponse.getResponse())) {
//                 com.siot.IamportRestHttpClientJava.response.Payment paymentData = paymentResponse.getResponse();
//                 if (payment.getReceiptId().equals(paymentData.getImpUid()) && payment.getOrderId().equals(paymentData.getMerchantUid()) && payment.getAmount().compareTo(paymentData.getAmount()) == 0) {
//                     PaymentMethod method = PaymentMethod.valueOf(paymentData.getPayMethod().toUpperCase());
//                     PaymentStatus status = PaymentStatus.valueOf(paymentData.getStatus().toUpperCase());
//                     payment.setMethod(method);
//                     payment.setStatus(status);
//                     paymentRepository.save(payment);
//                     if (status.equals(PaymentStatus.READY)) {
//                         if (method.equals(PaymentMethod.VBANK)) {
//                             // throw new PaymentRequiredException(paymentData.getVbankNum() + " " + paymentData.getVbankDate() + " " + paymentData.getVbankName());
//                             throw new Exception(paymentData.getVbankNum() + " " + paymentData.getVbankDate() + " " + paymentData.getVbankName());
//                         } else {
//                             // throw new PaymentRequiredException("Payment was not completed.");
//                             throw new Exception("Payment was not completed.");
//                         }
//                     } else if (status.equals(PaymentStatus.PAID)) {
//                         payment.setPaidAt(paymentData.getPaidAt().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
//                         paymentRepository.save(payment);
//                     } else if (status.equals(PaymentStatus.FAILED)) {
//                         // throw new ForbiddenException("Payment failed.");
//                         throw new Exception("Payment failed.");
//                     } else if (status.equals(PaymentStatus.CANCELLED)) {
//                         // throw new ForbiddenException("This is a cancelled payment.");
//                         throw new Exception("This is a cancelled payment.");
//                     }
//                 } else {
//                     // throw new ForbiddenException("The amount paid and the amount to be paid do not match.");
//                     throw new Exception("The amount paid and the amount to be paid do not match.");
//                 }
//             } else {
//                 // throw new NotFoundException("Could not found payment for " + payment.getReceiptId() + ".");
//                 throw new Exception("Could not found payment for " + payment.getReceiptId() + ".");
//             }
//         } catch (Exception e) {
//             e.printStackTrace();
//             // switch (e.getHttpStatusCode()) {
//             //     // case 401 -> throw new InternalServerErrorException("Authentication token not passed or invalid.");
//             //     case 401 : throw new Exception("Authentication token not passed or invalid.");
//             //     // case 404 -> throw new NotFoundException("Could not found payment for " + payment.getReceiptId() + ".");
//             //     case 404 : throw new Exception("Could not found payment for " + payment.getReceiptId() + ".");
//             // }
//         }

//         return payment;
//     }

//     @Transactional
//     public Payment verifyPayment(String receiptId, String orderId, Member buyer) throws Exception {
//         checkNotNull(receiptId, "receiptId must be provided.");

//         Optional<Payment> optionalPayment = paymentRepository.findByOrderIdAndBuyer(orderId, buyer);
//         if (optionalPayment.isPresent()) {
//             Payment payment = optionalPayment.get();
//             payment.setReceiptId(receiptId);
//             return verifyPayment(payment, buyer);
//         } else {
//             // throw new Exception("Could not found payment for " + orderId + ".");
//             return null;
//         }
//     }
// }
