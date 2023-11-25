import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { ApiResponse } from '../interface/api-response';
;


@Injectable({
  providedIn: 'root',
})
export class ChatService {

    private baseUrl = environment.apiUrl;
    constructor(private http: HttpClient) {}

    getConversationIdByUser1IdAndUser2Id(
        user1Id: number,
        user2Id: number
      ): Observable<ApiResponse> {
        return this.http.get<ApiResponse>(this.baseUrl.concat('/conversation/id'), {
          params: { user1Id: user1Id, user2Id: user2Id },
        });
      }
}