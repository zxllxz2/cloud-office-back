# Cloud Office Platform
The Cloud Office Platform is a staff admin system with Role-Based Access Control. Here is the website back-end APIs.

## Tech stack
Spring Boot, Spring Security, JWT, Swagger2, MyBatis-Plus, MySQL, Redis, RabbitMQ, WebSocket, EasyPoi...

## Getting Start
There are two main modules in this project - the server module, which contains most of the functionalities, and the mail module, which contains the APIs to send emails. To get start, simply run the YebApplication. If you want the function of sending emails automatically to new employees, you should run the MailApplication simultaneously.

## API documentatiom
This project uses Swagger2 to generate the API documentation. After running the YebApplication, you can go to _**localhost:8081/doc.html**_ to check the documentation.
(You may need to enter _**localhost:8081/doc.html?cache=1&lang=en**_ for English website)

![image](https://user-images.githubusercontent.com/56448228/148741966-8cf9b071-39cf-49d7-ab04-3db6ae7a8b57.png)

You need to login in order to test those APIs. First, go to the captcha-controller to get the captcha.

![image](https://user-images.githubusercontent.com/56448228/148742034-31b7a71e-d4fa-4e09-b07f-acbb09bb5c29.png)

Then, go to the LoginController to login. There are 3 available username: 'admin', 'taomeng', and 'naqiao', and the passwords are all '123'. Use the second API to login.

![image](https://user-images.githubusercontent.com/56448228/148742153-16eeeb46-8cec-4739-ba51-b02819957f71.png)

After you get the token, go to the 'Authorize' section in the upper left corner. Enter 'Bearer ' with the token that you just got. Don't forget the SPACE between them! Click 'Save' to finish your login.

![image](https://user-images.githubusercontent.com/56448228/148742215-3a51413a-79fa-4822-a892-dac57edb9877.png)

Now, you are free to test any APIs listed here!



