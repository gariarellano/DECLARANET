import { Injectable } from "@angular/core";
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable()
export class ServicioService {
    public url: string;
    constructor(public _http: HttpClient, ){
        this.url = "https://reqres.in/";
    }

    getUser(): Observable<any>{
        return this._http.get(this.url + 'api/users?page=2');
    };
}