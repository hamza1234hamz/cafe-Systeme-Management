import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  url = environment.apiUrl;

  constructor(private httpClient:HttpClient) { }
 

  singup(data:any){
    return this.httpClient.post(this.url+"/api/signup",data);
  }
  
  forgotPassword(data:any){
    return this.httpClient.post(this.url+"/api/forgotPassword",data);
  }
  
  login(data:any){
    return this.httpClient.post(this.url+"/api/signin",data);
  }

  checkToken(){
    return this.httpClient.get(this.url+"/api/checkToken");
  }

  changePassword(data:any){
    return this.httpClient.post(this.url+"/api/changePassword",data);
  }

  getUsers(){
    return this.httpClient.get(this.url+"/api/get")
  }

  update(data:any){
    return this.httpClient.post(this.url+"/api/update",data);
  }
}
