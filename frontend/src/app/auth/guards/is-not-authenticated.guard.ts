import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { TokenValuesService } from '../services/token-values.service';

export const isNotAuthenticatedGuard: CanActivateFn = (route, state) => {
  const tokenValuesService = inject( TokenValuesService );
  const router      = inject( Router );

  if(tokenValuesService.isLoggedIn())
  {
    router.navigateByUrl('/main');
    return false;
  }

  return true;
};
