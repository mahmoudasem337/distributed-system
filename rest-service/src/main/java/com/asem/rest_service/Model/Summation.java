package com.asem.rest_service.Model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Summation {

        @JsonProperty("requestId")
        private String requestId;

        @JsonProperty("First Number")
        private int firstNumber;

        @JsonProperty("Second Number")
        private int secondNumber;

        @JsonProperty("Result")
        private int result;

        public String getRequestId() {
                return requestId;
        }

        public void setRequestId(String requestId) {
                this.requestId = requestId;
        }

        public int getFirstNumber() {
                return firstNumber;
        }

        public void setFirstNumber(int firstNumber) {
                this.firstNumber = firstNumber;
        }

        public int getSecondNumber() {
                return secondNumber;
        }

        public void setSecondNumber(int secondNumber) {
                this.secondNumber = secondNumber;
        }

        public int getResult() {
                return result;
        }

        public void setResult(int result) {
                this.result = result;
        }
}

