import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { LocationStrategy, PathLocationStrategy } from '@angular/common';
import { AppRoutingModule } from './app.routing';

import { TranslateLoader, TranslateModule } from '@ngx-translate/core';
import { Http, HttpModule } from '@angular/http';
import { TranslateHttpLoader } from '@ngx-translate/http-loader';

import { AppComponent } from './app.component';

import { LayoutComponent } from './layout/layout.component';
import { BsDropdownModule } from 'ngx-bootstrap/dropdown';
import { TabsModule } from 'ngx-bootstrap';
import { SIDEBAR_TOGGLE_DIRECTIVES } from './shared/sidebar/sidebar.directive';
import { NAV_DROPDOWN_DIRECTIVES } from './shared/nav-dropdown.directive';
import { SidebarComponent } from './shared/sidebar/sidebar.component';
import { TopbarComponent } from './shared/topbar/topbar.component';
import { FooterComponent } from './shared/footer/footer.component';
import { DashboardComponent } from './dashboard/dashboard.component';
import { SidebarService } from './shared/sidebar/sidebar.service';
import { SidebarChildComponent } from './shared/sidebar/sidebar-child.component';

// AoT requires an exported function for factories
export function HttpLoaderFactory(http: Http) {
  return new TranslateHttpLoader(http);
}

@NgModule({
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpModule,
    TranslateModule.forRoot({
      loader: {
        provide: TranslateLoader,
        useFactory: HttpLoaderFactory,
        deps: [ Http ]
      }
    }),
    BsDropdownModule.forRoot(),
    TabsModule.forRoot()
  ],
  declarations: [
    AppComponent,
    TopbarComponent,
    SidebarComponent,
    SidebarChildComponent,
    FooterComponent,
    NAV_DROPDOWN_DIRECTIVES,
    SIDEBAR_TOGGLE_DIRECTIVES,
    LayoutComponent,
    DashboardComponent
  ],
  providers: [{
    provide: LocationStrategy,
    useClass: PathLocationStrategy
  }, SidebarService ],
  bootstrap: [ AppComponent ]
})
export class AppModule { }
