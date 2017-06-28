import {BrowserModule} from "@angular/platform-browser";
import {NgModule} from "@angular/core";
import {LocationStrategy, PathLocationStrategy} from "@angular/common";
import {AppRoutingModule} from "./app.routing";
import {HttpModule} from "@angular/http";
import {AppComponent} from "./app.component";
import {LayoutComponent} from "./layout/layout.component";
import {BsDropdownModule} from "ngx-bootstrap/dropdown";
import {TabsModule} from "ngx-bootstrap";
import {SIDEBAR_TOGGLE_DIRECTIVES} from "./shared/sidebar/sidebar.directive";
import {NAV_DROPDOWN_DIRECTIVES} from "./shared/nav-dropdown.directive";
import {SidebarComponent} from "./shared/sidebar/sidebar.component";
import {TopbarComponent} from "./shared/topbar/topbar.component";
import {FooterComponent} from "./shared/footer/footer.component";
import {DashboardComponent} from "./dashboard/dashboard.component";
import {SidebarService} from "./shared/sidebar/sidebar.service";
import {BaImageLoaderService, BaMenuService, BaThemePreloader, BaThemeSpinner} from "./services";
import {GlobalState} from "./global.state";
import {SidebarChildComponent} from "./shared/sidebar/menu/sidebar-child.component";
import {AppTranslationModule} from "./app.translation.module";
import {FormsModule} from "@angular/forms";
import { ProjectComponent } from './project/project.component';

const NGA_SERVICES = [
  BaImageLoaderService,
  BaThemePreloader,
  BaThemeSpinner,
  BaMenuService,
  SidebarService
];

const NGA_COMPONENTS = [
  AppComponent,
  TopbarComponent,
  SidebarComponent,
  SidebarChildComponent,
  FooterComponent,
  NAV_DROPDOWN_DIRECTIVES,
  SIDEBAR_TOGGLE_DIRECTIVES,
  LayoutComponent,
  DashboardComponent
];

const APP_PROVIDERS = [
  GlobalState
];

@NgModule({
  imports: [
    FormsModule,
    BrowserModule,
    AppRoutingModule,
    HttpModule,
    AppTranslationModule,
    BsDropdownModule.forRoot(),
    TabsModule.forRoot()
  ],
  declarations: [
    NGA_COMPONENTS,
    ProjectComponent
  ],
  providers: [{
    provide: LocationStrategy,
    useClass: PathLocationStrategy
  },
    ...NGA_SERVICES,
    APP_PROVIDERS
  ],

  bootstrap: [ AppComponent ]
})
export class AppModule { }
