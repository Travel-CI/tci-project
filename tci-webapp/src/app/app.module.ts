import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpModule } from '@angular/http';

import { AppComponent } from './app.component';
import { HeadNavbarMainMenuComponent } from './head-navbar-main-menu/head-navbar-main-menu.component';
import { HeadNavbarLeftSideComponent } from './head-navbar-left-side/head-navbar-left-side.component';
import { HeadNavbarRightSideComponent } from './head-navbar-right-side/head-navbar-right-side.component';
import { LeftSidebarComponent } from './left-sidebar/left-sidebar.component';
import { MainContentComponent } from './main-content/main-content.component';

@NgModule({
  declarations: [
    AppComponent,
    HeadNavbarMainMenuComponent,
    HeadNavbarLeftSideComponent,
    HeadNavbarRightSideComponent,
    LeftSidebarComponent,
    MainContentComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
