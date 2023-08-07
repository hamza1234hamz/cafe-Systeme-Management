import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class BillService {
  url=environment.apiUrl;

  constructor(private httpClient:HttpClient) { }

  generateRapport(data:any){
    return this.httpClient.post(this.url+"/Bill/generateRaport",data);
  }

  getPdf(data:any):Observable<Blob>{
    return this.httpClient.post(this.url+"/Bill/getPdf",data,{responseType:'blob'});
  }

  getBills(){
    return this.httpClient.get(this.url+"/Bill/getBills");
  }

  delete(id:any){
    return this.httpClient.post(this.url+"/Bill/delete/"+id,
    {headers: new HttpHeaders().set('Content-Type',"application/json")
  });
  }
}
