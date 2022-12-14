### About the project ###
1. Chatroom starts server and connects up to 10 clients simultaneously
2. The chatroom allows clients to send direct messages with other connected users, broadcast an insult message, query connected users
3. It contains a template folder with insult grammar, imports a random sentence generator library, which imports gson to parse json files.
### How to Run the Program ###
### server side: ###
1. Run the class ChatRoom.java main function (this is the server class).
2. The server port number is 8000, and the hostname is local host (assuming local connection)
3. Starts by typing a user name
### client side: ###
1. Client class contains the client
2. Edit the client.java configuration, input the host as localhost, port 8000.
3. Click modify options to enable running multiple clients
### command ###
1. the command includes the command key word, followed by a space, and a message
2. type ? to show a list of all valid commands.
3. valid command list
• ?: list all commands
• logoff: sends a DISCONNECT_MESSAGE to the chatroom
• who: sends a QUERY_CONNECTED_USERS to the chatroom
• @user: sends a DIRECT_MESSAGE to the specified user to the chatroom
• @all: sends a BROADCAST_MESSAGE to the chatroom, to be sent to all users connected
• !user: sends a SEND_INSULT message to the chatroom, to be sent to the specified user
