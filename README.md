# Advanced Programing 2

Advanced programming 2 course assignments by Ofek Binder 213902638, Bar Ben Tovim 325323806 and Ariel Oscar 209341684.

## Ex.3

### Description

Implementing a chat app, using Android studio, java, xml, NodeJS, MongoDB and FireBase (webSocket). We built a chat app, with a home, login, register page. And aften signing you'll get contacts page and chat page. All the data about the users and their chats is saved in a data base of MongoDB. In the login we made sure that only signed up users can login to the chats, if you havn't register yet, you can press on the link "register" and it gets you into the register page. In the register page you have to register with username, password, email, profile image (optional), and a profile name. While registering to our app you cannot leave most of the fields empty and have to fill them according to the demands. If the username or password are already used by another user, you'll have to choose another ones. After connecting to our app you'll see all the contacts you alreadt added to your contacts list. You can add a new contact by clicking on the "+contact" button and typing its username. You can press on one of your contact and your chat will show on the screen on a new page. Now you can send anything to the person you chat with and also get messages from it in live. By Clicking on another contact, the chat with him will show up. Our web saves all your chats and contacts after logout. Even if you get out of the app without logout, u can come back and youl will be connected authomaticly.

#### Explaining the functuality:
In this project we took our own "swagger" functions of the server from the last project (Ex.2). We changed them a little bit so they will work with the FireBase. Here is a quick reminder how these functions work. Some of the main methods are: get useres, add user. Some of the methods of each user: add contact, get contacts, send message, get chat. To use the methods of a specific user we send the token of the user as a input to the function. A token is a string that changes every few seconds and belong to the user that is connected. The token is like an authorization of the user to do all the things in the chat page. We calculate the tocken by username and passsword of the user each time the user tries to do something (add contact, send message, etc).
We have 2 databases:
  1. One is remote data base, we used "MongoDB" database, that saves all the data for our app: registered users including all their data (username, password, email, profile image and display name), and all the data that relevante for each of them: thier contacts list, their chats. This database is updated at live time as each user use the app as long as you have internet. If you don't have internet you won't be able to register, login, getting/sending new messages. If you'll try to do one of these actions without internet, you'll get a popup "Problem with connecting to the server".
  2. The second database is a local database that we built by using "Room". This database takes care of a case when you don't have an internet and you are not connected to the server. If you are already connected to our app with your user, and you get in you will be able to see all your contects and your chats with them (without the opportunity to send or get a new message, and to add a new contact).

We also took care of an incident when 2 users are connected and send messages to each other, so they will get the messages from the other user in live immidiatly.
We use FireBase to implement it. The FireBase is used when some user send a meesage to another user. The server gets the new message, and the server uses a function of the FireBase to pass the message to the other user at live time.

In terms of visualization, we edited all our pages with xml codes. In the settings you have an option to change the server adress, and also change to night mode (the app will be dark colored).
XML Pages of layout:
  1. Home page- with the options of sign in or sign up.
  2. Login page- signing in by username and password.
  3. Register page- signing up by writing username, password, email, profile image (optional), and a profile name inputs.
  4. Contacts page- this page display you all of you contacts. you also have 3 buttons: logout, add new contact and settings. To display each contact we use contact_layout.xml file. By clicking on one of the contact you will get to another page of the actual chat with him.
  5. Contact layout- this xml file is a layout of each contact so we will be able to add/remove contacts from the list.
  6. Chat page- shows you the display name of the person you are talking with, and his profile image (if he doesn't have a profile img, it will displayes a defult profile image. You have a button to get back to the contacts page. And at the bottom you have input section to write new messages and a send button. The main atrraction of the page is the actual chat when you can see all the mesages you sent and got and the date (hour and day) they were sent. We implemented these by a chat_layout.xml page.
  7. Chat layout- this xml file is a layout of one message so you will be able to add new messages to the chat section. It structured by chat bubble with the message inside and a date under the bubble. There is a slightly diffrent design to the message you get and the message you send (background color, diffrent side of the screen, diffrent margin).
  8. Add new contact page- this page has a button to go back to the contacts page. You can add a new contact by it's username and clicking on the "OK" button.
  9. Settings page- this page has 2 options: one changing to dark mode or back to bright mode. And the other is to change the server adrees. You also have a button to go back to the contacts page.

  *not that all the xml file int he layout uses files from the drawable such as images, icons, and other xml.files. 
 
### Executing the program

```
from the client :
Clicking on the Run button from the android studio.
in the server we run:
node app.js
And also turning on the MongoDB database.
```
