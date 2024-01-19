import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, map } from 'rxjs';
import { TokenValuesService } from 'src/app/auth/services/token-values.service';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class UsersService {

  private baseUrl: string = environment.baseUrl;
  private headers: any;

  constructor(
    private http: HttpClient,
    private tokenValuesService: TokenValuesService
  ) {
    this.headers = this.tokenValuesService.headers();
   }

   listarUsuarios(): Observable<any>{
    return this.http.get<any>(`${this.baseUrl}/users`, this.headers)
    .pipe(
      map((res: any) => res)
    );
  }

  buscarUsuario(id: number){
    return this.http.get<any>(`${this.baseUrl}/users/${id}`, this.headers)
    .pipe(
      map((res: any) => res)
    );
  }

  guardarUsuario(req: any){
    return this.http.post<any>(`${this.baseUrl}/users`, req, this.headers)
    .pipe(
      map((response: any) => response)
    );
  }

  editarUsuario(id: number, req: any){
    return this.http.put<any>(`${this.baseUrl}/users/${id}`, req, this.headers)
    .pipe(
      map((response: any) => response)
    );
  }

  borrarUsuario(id: number){
    return this.http.delete<any>(`${this.baseUrl}/users/${id}`, this.headers)
    .pipe(
      map((res: any) => res)
    );
  }

  cargarDesdeCSV(req: any){
    return this.http.post<any>(`${this.baseUrl}/users/csv`, req, this.headers)
    .pipe(
      map((response: any) => response)
    );
  }

  getUsersDashboard(){
    return this.http.get<any>(`${this.baseUrl}/users/dashboard`, this.headers)
    .pipe(
      map((res: any) => res)
    );
  }
  

}
