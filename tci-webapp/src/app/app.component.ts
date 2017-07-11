import {Component} from "@angular/core";
import { GlobalState } from './global.state';
import { BaImageLoaderService, BaThemePreloader, BaThemeSpinner } from './shared/services';
import {TranslateService} from "@ngx-translate/core";

@Component({
  selector: 'app',
  template: '<router-outlet></router-outlet>'
})
export class AppComponent {

  isMenuCollapsed: boolean = false;

  constructor(private _state: GlobalState,
              private translate: TranslateService,
              private _imageLoader: BaImageLoaderService,
              private _spinner: BaThemeSpinner) {

    this._state.subscribe('menu.isCollapsed', (isCollapsed) => {
      this.isMenuCollapsed = isCollapsed;
    });
  }

  public ngAfterViewInit(): void {
    // hide spinner once all loaders are completed
    BaThemePreloader.load().then((values) => {
      this._spinner.hide();
    });
  }
}
