# Currency Conversion Service

## How to Run
To build and start the service, run:

docker-compose up --build

## API Documentation
You can test the endpoints via Swagger UI:

http://localhost:8080/swagger-ui/index.html#/currency-controller

---

## API Endpoints

### 1. Get Exchange Rate

**URL:**
GET http://localhost:8080/v0/currency/exchange-rate?from=USD&to=EUR

**Description:**
Fetches the exchange rate between the specified currencies using the external service at http://apilayer.net/api/live.  
Results are cached for 1 minute based on the request key.

**Example Response:**
{
"result": {
"code": "SUCCESS",
"message": "Success"
},
"rate": 0.87704
}

---

### 2. Convert Currency

**URL:**
POST http://localhost:8080/v0/currency/convert

**Request Body:**
{
"sourceAmount": 150.75,
"sourceCurrency": "USD",
"targetCurrency": "EUR"
}

**Description:**
Converts the given amount from the source currency to the target currency.  
The result is saved to the database and returned with a unique transaction ID.

**Response Body:**
{
"result": {
"code": "SUCCESS",
"message": "Success"
},
"currencyConversionDtoList": [
{
"convertedAmount": 132.2846325,
"transactionId": 1
}
]
}

---

### 3. Bulk Currency Conversion

**URL:**
POST http://localhost:8080/v0/currency/bulk-convert

**Request Format (CSV):**
"sourceCurrency;targetCurrency;amount
USD;EUR;100.00"

**Description:**
Processes multiple currency conversion requests in a single operation using CSV input.  
The Strategy Design Pattern is used internally to handle CSV-based or single item requests.

---

### 4. Get Conversion History

**URL:**
GET http://localhost:8080/v0/currency/conversion-history?transactionId=2&transactionDate=2025-06-09

**Description:**
Fetches the conversion transaction history using the given transaction ID and transaction date.

