import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";

@Injectable({
  providedIn: "root",
})
export class PruebaService {
  constructor(private http: HttpClient) {}

  getPokemon(desde: number, limite: number) {
    return this.http.get(
      "http://pokeapi.co/api/v2/evolution-chain/?limit=" +
        limite +
        "&offset=" +
        desde +
        ""
    );
  }
}
