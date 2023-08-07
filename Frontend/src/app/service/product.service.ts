import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ProductService {
  url=environment.apiUrl;

  constructor(private httpClient:HttpClient) { }

  add(data:any){
    return this.httpClient.post(this.url+"/product/add",data);
  }

  update(data:any){
    return this.httpClient.post(this.url+"/product/update",data);
  }

  getProducts(){
    return this.httpClient.get(this.url+"/product/get");
  }

  updateStatus(data:any){
    return this.httpClient.post(this.url+"/product/updateStatus",data);
  }

  delete(id:any){
    return this.httpClient.post(this.url+"/product/delete/"+id,
      {headers:new HttpHeaders().set('Content-Type',"application/json")});
  }
  getProductByCategory(id:any){
    return this.httpClient.get("http://localhost:8080/product/getByCategory/"+id);
  }

  getProductById(id:any){
    return this.httpClient.get("http://localhost:8080/product/getById/"+id);
  }
}
