import { Injectable } from '@angular/core';
import jwt_decode from 'jwt-decode';

@Injectable({
  providedIn: 'root'
})
export class TokenValuesService {

  isLoggedIn(): boolean {
    const jwt: any = localStorage.getItem('jwt');
    return jwt !== null;
  }

  getToken(): any {
    const jwt: any = localStorage.getItem('jwt');
    return jwt;
  }

  getDecodedToken(): any {
    const jwt: any = this.getToken();
    const token: any = jwt_decode(jwt);
    return token;
  }

  getUsername(): any{
    const token: any = this.getDecodedToken();
    return token.username;
  }

  getName(): any{
    const token: any = this.getDecodedToken();
    return token.name;
  }

  getLastName(): any{
    const token: any = this.getDecodedToken();
    return token.lastName;
  }

  getFullName(): any{
    return `${this.getName()} ${this.getLastName()}`;
  }

  getRole(): any{
    const token: any = this.getDecodedToken();
    return token.role;
  }

  isAdmin(): boolean {
    return this.getRole() === 'ADMINISTRATOR';
  }

  getPermissions():any {
    const token: any = this.getDecodedToken();
    return token.permissions;
  }

  hasPermission(p: string): boolean {
    const permissions = this.getPermissions();
    return permissions.includes(p);
  }

  getRoutes(): any{
    const token: any = this.getDecodedToken();
    return token.routes;
  }

  getTokenLifetime(): any {
    const token: any = this.getDecodedToken();
    const issuedAt = token.iat;
    const expires = token.exp;
    return [issuedAt, expires]; // fechas de acceso otorgado y de expiración
  }

  headers():any {
    const jwt: any = this.getToken();
    const headers = { 'Authorization': 'Bearer ' + jwt };
    return { headers };
  }

}
