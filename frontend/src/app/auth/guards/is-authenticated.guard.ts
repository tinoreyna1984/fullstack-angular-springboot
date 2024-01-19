import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { TokenValuesService } from '../services/token-values.service';

export const isAuthenticatedGuard: CanActivateFn = (route, state) => {
  const tokenValuesService = inject( TokenValuesService );
  const router      = inject( Router );
  
  if(tokenValuesService.isLoggedIn())
    return true;

  router.navigateByUrl('/auth/login');
  return false;
};
