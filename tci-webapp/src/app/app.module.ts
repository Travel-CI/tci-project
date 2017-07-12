import {BrowserModule} from "@angular/platform-browser";
import {NgModule} from "@angular/core";
import {CommonModule, LocationStrategy, PathLocationStrategy} from "@angular/common";
import {AppRoutingModule} from "./app.routing";
import {HttpModule} from "@angular/http";
import {AppComponent} from "./app.component";
import {LayoutComponent} from "./pages/layout/layout.component";
import {BsDropdownModule} from "ngx-bootstrap/dropdown";
import {TabsModule} from "ngx-bootstrap";
import {SIDEBAR_TOGGLE_DIRECTIVES} from "./shared/sidebar/sidebar.directive";
import {NAV_DROPDOWN_DIRECTIVES} from "./shared/nav-dropdown.directive";
import {SidebarComponent} from "./shared/sidebar/sidebar.component";
import {TopbarComponent} from "./shared/topbar/topbar.component";
import {FooterComponent} from "./shared/footer/footer.component";
import {SidebarService} from "./shared/sidebar/sidebar.service";
import {BaImageLoaderService, BaMenuService, BaThemePreloader, BaThemeSpinner} from "./shared/services";
import {GlobalState} from "./global.state";
import {SidebarChildComponent} from "./shared/sidebar/menu/sidebar-child.component";
import {AppTranslationModule} from "./shared/translation/app.translation.module";
import {FormsModule} from "@angular/forms";
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import { ProjectsComponent } from './pages/projects/projects.component';
import {DataTableModule, DialogModule, DropdownModule} from 'primeng/primeng';
import {AddComponent} from './pages/add/add.component';
import {BuildsComponent} from './pages/builds/builds.component';
import {ProjectService} from "app/services/project.service";
import {CommandService} from './services/command.service';
import {LoggerService} from "app/services/logger.service";
import {SelectModule} from "ng2-select-compat";
import {ToasterModule} from 'angular2-toaster';
import { StepComponent } from './pages/step/step.component';
import {AccordionModule} from 'primeng/primeng';
import {BusyModule} from 'angular2-busy';

const NGA_SERVICES = [
  BaImageLoaderService,
  BaThemePreloader,
  BaThemeSpinner,
  BaMenuService,
  SidebarService,
  ProjectService,
  CommandService,
  LoggerService
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
  ProjectsComponent,
  AddComponent,
  BuildsComponent
];

const APP_PROVIDERS = [
  GlobalState
];

@NgModule({
  imports: [
    // Angular
    FormsModule,
    CommonModule,
    HttpModule,
    BrowserModule,
    BrowserAnimationsModule,

    // PrimeNg
    DataTableModule,
    DialogModule,
    DropdownModule,
    SelectModule,
    AccordionModule,

    // Bootstrap
    BsDropdownModule.forRoot(),
    TabsModule.forRoot(),

    ToasterModule,
    BusyModule,
    
    AppRoutingModule,
    AppTranslationModule
  ],
  declarations: [
    NGA_COMPONENTS,
    StepComponent
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
