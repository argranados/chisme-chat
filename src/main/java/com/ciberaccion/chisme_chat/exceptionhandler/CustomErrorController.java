package com.ciberaccion.chisme_chat.exceptionhandler;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public ResponseEntity<Map<String, Object>> handleError(HttpServletRequest request) {
        Object statusCode = request.getAttribute("jakarta.servlet.error.status_code");

        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        if (statusCode != null) {
            status = HttpStatus.valueOf(Integer.parseInt(statusCode.toString()));
        }

        return ResponseEntity.status(status).body(Map.of(
                "error", status.getReasonPhrase(),
                "status", status.value(),
                "path", request.getAttribute("jakarta.servlet.error.request_uri")
        ));
    }
}

