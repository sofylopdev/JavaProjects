# JavaProjects
Diverse Java Projects

## **Worm Game**
Small simple game done as part of the online course Object-Oriented Programming with Java, Part I and II 
of the University of Helsinki, Department of Computer Science. 

![alt text](https://github.com/sofylopdev/JavaProjects/blob/master/WormGame.png)



## **Elevadores/Elevators**
An elevator system simulator. Not all the requirements were met.

### Project requirements:
► A skyscraper has a series of elevators of different types, namely,

 - **normal**: can carry up to 4 people and stop at all the floors
      
 - **wide**: equal to normal, allowing the transport of 10 people
      
 - **cargo**: only carries cargo and never people up to a limit equivalent to the weight of 20 people. 
                  You can only stop on floors -2, -1 and the penultimate floor 
                  and moves more slowly when compared to others.
                  
  - **express**: equal to the wide but stops every 10 floors (10, 20, etc.)
      
 - **restricted**: it's a normal lift. However, only a few people can use it by entering a code.
      
►  It should be possible to interact with a lift by means of a direction button. This last one is should 
      tell the system if the person or cargo wants to go up or down.
 
►  An elevator has a series of "moves" such as:
 - the movement between floors takes a certain period of time;
 - the opening and closing of the doors also take a certain period of time;
 -  movement of the doors can be interrupted by the entry of a person or by pressing an appropriate button.
      
►  The security of the skyscraper has the possibility of monitoring the state of all the elevators of the 
      skyscraper, being able to carry out certain operations on the elevators, such as locking a lift on a floor to
      report failure.
      
►  Some uncontrollable anomalies can arise, for example a power outage or failure of an elevator. It was defined by the 
      skyscraper administration that with the appearance of any anomaly in an elevator this would be locked for repair.
      
      
### Description:
►  The simulator is a multiprocessing application that aims to simulate the environment of using elevators. 
      It shall provide all necessary mechanisms for the user to interact with the elevators.
      Any data necessary for the execution of the program, such as the number of floors in the building, should be
      inserted into a configuration file.
      
►  In the simulator there is a "Azar" ("Bad Luck") thread that aims to provoke certain events affecting one 
      or more elevators. Examples include a malfunction or a power outage. It is suggested that the events occur ramdomly.
      
►  This project was developed considering the following topics:
 - OOP
 - Polymorphism
 - Exceptions (creating and using hierarchy of exceptions)
 - Heritage
 - Threads
 - Synchronization
      

### Images:

 - **Configuration:**

![alt text](https://github.com/sofylopdev/JavaProjects/blob/master/Elevadores1.png)

 - **Skyscraper, individual elevator control and security guard control center:**

![alt text](https://github.com/sofylopdev/JavaProjects/blob/master/Elevadores2.png)


## **Chat_Service**

A Chat application.

### Project requirements:

► Each user is assigned a nickname and a socket (IP and Port), when registering 
  in the name service specifically built for this application;

► The Chat application name service is also programmed in Java and can run on Windows or Linux. 
  It provides the BIND functionality, in which it provides the socket according to the nickname;

► When the Chat application boots, it prompts the user to enter their socket. 
  The user of the application only needs to know the nicknames of the users with whom he wants to exchange messages. 
  For each message sent, it uses the BIND function of the name service to know the recipient's socket;

► The Chat application has a privacy service consisting of two methods, one to encrypt and 
  another to decipher the messages that are sent by the network;

 1. Login UI
 2. If only one user is connected, it shows "No connections available"
 3. In the chat, If the message was sent by the user it shows an orange color and if it wasn't it shows a blue color

![alt text](https://github.com/sofylopdev/JavaProjects/blob/master/ChatApp.png)

## License

 http://www.apache.org/licenses/LICENSE-2.0
 

