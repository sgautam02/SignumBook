import { Component, Inject, OnDestroy, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import { Subscription } from 'rxjs';
import { ApiResponse } from 'src/app/interface/api-response';
import { MessageRequest } from 'src/app/interface/message-request';
import { MessageResponse } from 'src/app/interface/message-response';
import { WebSocketResponse } from 'src/app/interface/web-socket-response';
import { User } from 'src/app/model/user';
import { AuthService } from 'src/app/service/auth.service';
import { ChatService } from 'src/app/service/chat.service';
import { StompService } from 'src/app/service/stomp.service';
import { UserService } from 'src/app/service/user.service';

@Component({
  selector: 'app-chat-popup',
  templateUrl: './chat-popup.component.html',
  styleUrls: ['./chat-popup.component.css']
})
export class ChatPopupComponent implements OnInit, OnDestroy {

  reciverId:any
  senderId:any
  authUser:any
  stompConvSub: Subscription | undefined;
  stompUserSub: Subscription | undefined;
  receiver: User;
  selectedConversationId: any;
  selectedConversation: MessageResponse[] = [];
  message: string = '';
  
  

  constructor(
  @Inject(MAT_DIALOG_DATA) 
  public data: any,
  private authService:AuthService,
  private userService: UserService,
  private stomp: StompService,
  private chatService:ChatService
  ) {
    this.reciverId = data;
  }
  

  ngOnInit(): void {
    this.authUser=this.authService.getAuthUserFromCache();
    // this.setConversation();
    this.onConversationSelected()
  }
  ngOnDestroy(): void {
    this.stompUserSub?.unsubscribe();
    this.stompConvSub?.unsubscribe();
  }

  onConversationSelected() {
      this.userService.getUserById(this.reciverId).subscribe((res:any)=>{
        this.receiver=res.user;
        this.receiver.profilePhoto='data:image/jpeg;base64,'+this.receiver.pimg;  
      })

      this.chatService.getConversationIdByUser1IdAndUser2Id(this.reciverId, this.authUser.id)
        .subscribe((res: ApiResponse) => {
        this.selectedConversationId = res.data;
        this.setConversation();
      });

    
  }

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
          const chatHistory:MessageResponse[]=res.data
          this.selectedConversation = chatHistory.reverse();
        } else if (res.type == 'ADDED') {
          let msg: MessageResponse = res.data;
          this.selectedConversation.unshift(msg);
        }
      }
    );
    // Notify that I'm subscribed to get initial data
    this.stomp.send('conv', this.selectedConversationId);
  }

  onSendMessage() {
    // If message field is empty then return
    if (this.message.trim().length == 0) return;

    const timestamp = new Date();
    let body: MessageRequest = {
      conversationId: this.selectedConversationId,
      senderId: this.authUser.id,
      receiverId: this.receiver.id,
      message: this.message.trim(),
      timestamp: timestamp,
    };
    this.stomp.send('sendMessage', body);
    this.message = '';
  }

}
