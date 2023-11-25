import { Component, OnDestroy, OnInit } from '@angular/core';
import { Router } from '@angular/router';

import { Subscription } from 'rxjs';
import { ApiResponse } from 'src/app/interface/api-response';
import { ConversationResponse } from 'src/app/interface/conversation-response';
import { MessageRequest } from 'src/app/interface/message-request';
import { MessageResponse } from 'src/app/interface/message-response';
import { WebSocketResponse } from 'src/app/interface/web-socket-response';
import { User } from 'src/app/model/user';
import { AuthService } from 'src/app/service/auth.service';
import { ChatService } from 'src/app/service/chat.service';
import { StompService } from 'src/app/service/stomp.service';
import { UserService } from 'src/app/service/user.service';


@Component({
  selector: 'app-chat',
  templateUrl: './chat.component.html',
  styleUrls: ['./chat.component.css'],
})
export class ChatComponent implements OnInit, OnDestroy {
  currentUser: User 
  // all users except current user
  users: User[] = [];
  // users all conversations
  userConversations: ConversationResponse[] = [];
  // current user conversation subscription
  stompUserSub: Subscription | undefined;

  // selected conversation
  selectedConversationId: number = -1;
  selectedConversationReceiverId: number = -1;
  selectedConversationReceiverName: string = '';
  selectedUser:User
  // selected conversation messages
  selectedConversation: MessageResponse[] = [];
  selectedConversations: MessageResponse[] = [];
  // selected conversation messages subscription
  stompConvSub: Subscription | undefined;

  // Boolean flag to indicate whether showing users or conversation on left column
  showUserState: boolean = false;
  // Input field for send message
  message: string = '';

  constructor(
    private router: Router,
    private userService: UserService,
    private stomp: StompService,
    private authService:AuthService,
    private chatService:ChatService
    
  ) {
    this.currentUser =authService.getAuthUserFromCache();
    this.currentUser.profilePhoto='data:image/jpeg;base64,'+this.currentUser.pimg;
    
  }

  ngOnInit(): void {
    // Subscribe to userId websocket to get updated conversation when gets new messages
    this.subscribeToCurrentUserConversation();
    this.userService
    .getAllUser()
    .subscribe((res:any) => {
      this.users = res;
      this.users.forEach(user=>{
        user.profilePhoto='data:image/jpeg;base64,'+user.pimg;
      })
    });
    
  }

  ngOnDestroy(): void {
    // Unsubscribe from all channels onDestroy
    this.stompUserSub?.unsubscribe();
    this.stompConvSub?.unsubscribe();
  }

  // When click the new/add button Then get all users and set users list
  onShowHideUserConversation() {
    this.showUserState = !this.showUserState;
    if (this.showUserState) {
      this.userService
        .getAllUser()
        .subscribe((res:any) => {

          this.users = res;
        });
    }
  }

  // Close a chat from dropdown menu
  onCloseChat() {
    this.stompConvSub?.unsubscribe();
    this.selectedConversationId = -1;
  }

  // When click logout button Then remove user from localStorage and navigate to homepage
  onUserLogout() {
    localStorage.removeItem('user');
    this.router.navigate(['.']);
  }

  subscribeToCurrentUserConversation() {
    // setting one second delayed to successfully connect the stomp to server
    setTimeout(() => {
      this.stompUserSub = this.stomp.subscribe(
        'user/' + this.currentUser.id,
        (payload: any) => {
          let res: WebSocketResponse = payload;
          if (res.type == 'ALL') {
            this.userConversations = res.data;
            this.userConversations.forEach(user=>{
              const id=user.otherUserId
              this.users.forEach(u=>{
                if(id==u.id){
                  user.profilePhoto=u.profilePhoto
                }
              })
            })
            const found = this.userConversations.find(
              (item) => item.conversationId === this.selectedConversationId
            );
            if (found === undefined) {
              this.onCloseChat();
            }
          }
        }
      );
      // Notify that I'm subscribed to get initial data
      this.stomp.send('user', this.currentUser.id);
    }, 1000);
  }

  // When new or exiting user selected Then set the variables and get the two users
  // conversationId from the database
  onUserSelected(receiverId: number) {
    this.selectedConversationReceiverId = receiverId;
    // this.selectedConversationReceiverName = receiverName;
    this.userService.getUserById(this.selectedConversationReceiverId).subscribe((res:any)=>{
      this.selectedUser=res.user;
      this.selectedUser.profilePhoto='data:image/jpeg;base64,'+this.selectedUser.pimg;  
    })
    this.chatService.getConversationIdByUser1IdAndUser2Id(receiverId, this.currentUser.id)
        .subscribe((res: ApiResponse) => {
        this.selectedConversationId = res.data;
        // this.onShowHideUserConversation();
        this.setConversation();
      });
  }

  // When user select a conversation from the list
  onConversationSelected(index: number) {
    this.selectedConversationId = this.userConversations[index].conversationId;
    this.selectedConversationReceiverId =
      this.userConversations[index].otherUserId;
    this.selectedConversationReceiverName =
      this.userConversations[index].otherUserName;
      this.userService.getUserById(this.selectedConversationReceiverId).subscribe((res:any)=>{
        this.selectedUser=res.user;
        this.selectedUser.profilePhoto='data:image/jpeg;base64,'+this.selectedUser.pimg;  
      })

    this.setConversation();
  }

  // Set a conversation of selected conversationId
  setConversation() {
    // unsubscribe any previous subscription
    this.stompConvSub?.unsubscribe();
    // then subscribe to selected conversation
    // when get new message then add the message to first of the array
    this.stompConvSub = this.stomp.subscribe(
      'conv/' + this.selectedConversationId,
      (payload: any) => {
        let res: WebSocketResponse = payload;
        if (res.type == 'ALL') {
          this.selectedConversation = res.data;
          this.selectedConversations=this.selectedConversation.reverse();
        } else if (res.type == 'ADDED') {
          let msg: MessageResponse = res.data;
          this.selectedConversation.unshift(msg);
        }
      }
    );
    // Notify that I'm subscribed to get initial data
    this.stomp.send('conv', this.selectedConversationId);
  }

  // Send message to other user
  onSendMessage() {
    // If message field is empty then return
    if (this.message.trim().length == 0) return;

    const timestamp = new Date();
    let body: MessageRequest = {
      conversationId: this.selectedConversationId,
      senderId: this.currentUser.id,
      receiverId: this.selectedConversationReceiverId,
      message: this.message.trim(),
      timestamp: timestamp
    };
    this.stomp.send('sendMessage', body);
    this.message = '';
  }

  // When click Delete chat from the dropdown menu Then delete the conversation
  // with it's all messages
  onDeleteConversation() {
    this.stomp.send('deleteConversation', {
      conversationId: this.selectedConversationId,
      user1Id: this.currentUser.id,
      user2Id: this.selectedConversationReceiverId,
    });
  }

  // When click delete on a message menu Then delete from database Then refresh
  // conversation list
  onDeleteMessage(messageId: number) {
    this.stomp.send('deleteMessage', {
      conversationId: this.selectedConversationId,
      messageId: messageId,
    });
  }

  
}

