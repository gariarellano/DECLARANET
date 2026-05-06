import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class PruebaPaginationService {

  constructor(private http:HttpClient) { }

  getData(limit:number,offset:number){
    return this.http.get(`https://pokeapi.co/api/v2/evolution-chain/?limit=${limit}&offset=${offset}`);
  }

  fetch() {
    return this.http.get('https://jsonplaceholder.typicode.com/todos');
  }

  getUsers(params) {
    return this.http.post('http//users', params);
  }
}
