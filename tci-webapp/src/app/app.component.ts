import { Component } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';

@Component({
  selector: 'body',
  template: '<router-outlet></router-outlet>'
})
export class AppComponent {

  constructor(private _translate: TranslateService) {

    _translate.setDefaultLang('fr');
    _translate.use('fr');
  }
}
