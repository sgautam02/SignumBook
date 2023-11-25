export interface ConversationResponse {
    conversationId: number;
    otherUserId: number;
    profilePhoto: string;
    otherUserName: string;
    lastMessage: string;
    lastMessageTimestamp: string;
  }