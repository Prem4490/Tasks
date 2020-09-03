Email Client Application is created using SpringBoot application , enabled with below Resturl’s.
http://localhost:8080/sendemail/external/plaintext
http://localhost:8080/sendemail/internal/encrypted-html 
http://localhost:8080/sendemail/external/encrypted-html 
http://localhost:8080/sendemail/external/plaintext 


Steps to run the application:

Unzip the file email-client-0.0.1-SNAPSHOT.zip file

1. Download the jar of your local machine.
2. Go to the jar downloaded location, Open cmd prompt, type the below command
to start the application java -jar email-client-0.0.1-SNAPSHOT.jar

Once the application is started. You can run the below curl commands,
Note: In emailTo Address, Instead of provideyourid@gmail.com , please provide valid gmail 

Scenario 1: Sending a plain text email to an outside resource, with a disclaimer added at the end, unencrypted and no retry

curl --request POST http://localhost:8080/sendemail/external/plaintext --header "Content-Type: application/json" --data "{\"emailTo\":\"provideyourid@gmail.com\",\"subject\":\"Plain text mail with disclaimer\",\"message\":\"Email Service application is created to send a plain text email to an outside resource, with a disclaimer added at the end, unencrypted and no retry\",\"encryptMessage\":false}"


Scenario 2: Sending an HTML email to an internal server (so without the disclaimer), encrypted with DES, with the retry functionality

curl --request POST http://localhost:8080/sendemail/internal/encrypted-html --header "Content-Type: application/json" --data "{\"emailTo\":\"provideyourid@gmail.com\",\"subject\":\"HTML mail encrypted with DES with retry option\",\"message\":\"Email Service application is created to send an HTML email to an internal server (so without the disclaimer), encrypted with DES, with the retry functionality\",\"encryptMessage\" : true}"


Scenario 3: Sending an HTML email to an outside resource, with a disclaimer added at the end and encrypted with AES with retries in case of errors

curl --request POST http://localhost:8080/sendemail/external/encrypted-html --header "Content-Type: application/json" --data "{\"emailTo\":\"provideyourid@gmail.com\",\"subject\":\"HTML mail encrypted with AES with retry option\",\"message\":\"Email Service application is created to send a HTML email to an outside resource, with a disclaimer added at the end and encrypted with AES with retries in case of errors\",\"encryptMessage\" : true}"

Scenario 4: Sending a plain text email to an outside resource and encrypted first with DES and then with AES

curl --request POST http://localhost:8080/sendemail/external/plaintext --header "Content-Type: application/json" --data "{\"emailTo\":\"provideyourid@gmail.com\",\"subject\":\"Plain text mail encrypted with DES then AES\",\"message\":\"Email Service application is created to send a plain text email to an outside resource and encrypted first with DES and then with AES\",\"encryptMessage\" : true}"
