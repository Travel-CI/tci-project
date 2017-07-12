import {NgModule} from "@angular/core";
import {Http} from "@angular/http";
import {TranslateModule, TranslateLoader, TranslateService} from "@ngx-translate/core";
import {TranslateHttpLoader} from "@ngx-translate/http-loader";

export function createTranslateLoader(http: Http) {
    return new TranslateHttpLoader(http);
}

const translationOptions = {
  loader: {
    provide: TranslateLoader,
    useFactory: (createTranslateLoader),
    deps: [Http]
  }
};

@NgModule({
  imports: [TranslateModule.forRoot(translationOptions)],
  exports: [TranslateModule],
  providers: [TranslateService]
})
export class AppTranslationModule {
  constructor(private translate: TranslateService) {
    translate.addLangs(["en"]);
    translate.setDefaultLang('en');
    translate.use('en');
  }
}
